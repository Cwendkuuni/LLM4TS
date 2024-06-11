import static org.junit.Assert.*;
import static com.google.common.base.Predicates.not;
import static com.google.javascript.rhino.NodeUtil.has;
import static com.google.javascript.rhino.NodeUtil.isAssignmentOp;
import static com.google.javascript.rhino.NodeUtil.isForIn;
import static com.google.javascript.rhino.NodeUtil.isFunction;
import static com.google.javascript.rhino.NodeUtil.isName;
import static com.google.javascript.rhino.NodeUtil.isVoid;
import static com.google.javascript.rhino.NodeUtil.matchNode;
import static com.google.javascript.rhino.Token.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope.Var;

@RunWith(Parameterized.class)
public class DeadAssignmentsEliminationTest {

  private static final Predicate<Node> MATCH_REMOVABLE_ASSIGNS =
      new Predicate<Node>() {
        @Override
        public boolean apply(Node n) {
          return isAssignmentOp(n) && isName(n.getFirstChild()) ||
              n.getType() == INC || n.getType() == DEC;
        }
      };

  private DeadAssignmentsElimination deadAssignmentsElimination;
  private AbstractCompiler compiler;

  @Parameterized.Parameters(name = "{0}")
  public static Collection<String> data() {
    return ImmutableList.of(
        "TestClass.js",
        "TestClass2.js",
        "TestClass3.js",
        "TestClass4.js");
  }

  public DeadAssignmentsEliminationTest(String file) {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    compiler.init(options, null, null);
    compiler.addPass(deadAssignmentsElimination);
    Node rootNode = Parser.parseJsonToRoot(Parser.parseJsSource(file, null), null);
    deadAssignmentsElimination.process(null, rootNode);
    compiler.dispose();
  }

  @Test
  public void testBasicFunctionality() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    assertTrue(has(rootNode, not(MATCH_REMOVABLE_ASSIGNS),
        Predicates.<Node>alwaysTrue()));
  }

  @Test
  public void testNoAssignments() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    assertTrue(isVoid(functionNode.getBody()));
  }

  @Test
  public void testNoDeadAssignments() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode = bodyNode.getFirstChild();

    // No dead assignments should be removed
    assertEquals(statementNode, bodyNode.getLastChild());
  }

  @Test
  public void testDeadAssignments() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node statementNode2 = statementNode1.getNext();

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInIfStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node ifStatementNode = NodeUtil.getIfStatement(bodyNode);
    Node statementNode1 = ifStatementNode.getCondition();
    Node statementNode2 = NodeUtil.getStatement(ifStatementNode.getNext());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInForStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node forStatementNode = NodeUtil.getForStatement(bodyNode);
    Node initializerNode = forStatementNode.getInitializer();
    Node statementNode1 = NodeUtil.getStatement(forStatementNode.getCondition());
    Node statementNode2 = NodeUtil.getStatement(forStatementNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode1);
  }

  @Test
  public void testDeadAssignmentsInWhileStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node whileStatementNode = NodeUtil.getWhileStatement(bodyNode);
    Node conditionNode = whileStatementNode.getCondition();
    Node statementNode1 = NodeUtil.getStatement(whileStatementNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode1);
  }

  @Test
  public void testDeadAssignmentsInSwitchStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node switchStatementNode = NodeUtil.getSwitchStatement(bodyNode);
    Node expressionNode = switchStatementNode.getExpression();
    Node statementNode1 = NodeUtil.getStatement(switchStatementNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode1);
  }

  @Test
  public void testDeadAssignmentsInFunctionExpression() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node expressionStatementNode = statementNode1.getNext();
    Node functionExpressionNode = expressionStatementNode.getNext();
    Node statementNode2 = NodeUtil.getStatement(functionExpressionNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInVariableDeclarationStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node variableDeclarationStatementNode = (Node) statementNode1.getFirstChild();
    Node statementNode2 = statementNode1.getNext();

    // Dead assignment should not be removed
    assertNotNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInReturnStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node returnStatementNode = statementNode1.getNext();

    // Dead assignment should be removed
    assertNull(statementNode1.getNext());
  }

  @Test
  public void testDeadAssignmentsInForInStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node forInStatementNode = statementNode1.getNext();
    Node initializerNode = forInStatementNode.getFirstChild();
    Node statementNode2 = NodeUtil.getStatement(forInStatementNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInSwitchCaseStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node switchStatementNode = NodeUtil.getSwitchStatement(bodyNode);
    Node statementNode1 = switchStatementNode.getExpression();
    Node caseStatementNode1 = NodeUtil.getCaseStatement(statementNode1, "1");
    Node statementNode2 = NodeUtil.getStatement(caseStatementNode1.getBody());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInCaseDefaultStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node switchStatementNode = NodeUtil.getSwitchStatement(bodyNode);
    Node statementNode1 = switchStatementNode.getExpression();
    Node defaultStatementNode = NodeUtil.getCaseStatement(statementNode1, "default");
    Node statementNode2 = NodeUtil.getStatement(defaultStatementNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInTryStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node tryStatementNode = statementNode1.getNext();
    Node blockStatementNode = tryStatementNode.getFirstChild();
    Node statementNode2 = NodeUtil.getStatement(tryStatementNode.getLastChild());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }

  @Test
  public void testDeadAssignmentsInCatchStatement() {
    Node rootNode = deadAssignmentsElimination.getCompiler().getJsRoot();
    Node functionNode = NodeUtil.getFunction(rootNode);
    Node bodyNode = functionNode.getBody();
    Node statementNode1 = bodyNode.getFirstChild();
    Node tryStatementNode = statementNode1.getNext();
    Node catchStatementNode = NodeUtil.getStatement(tryStatementNode.getNext());
    Node statementNode2 = NodeUtil.getStatement(catchStatementNode.getBody());

    // Dead assignment should be removed
    assertNull(statementNode2);
  }
}