import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.AbstractNodeTraversingVisitor.TRAVERSE;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private Node rootNode;
  private ControlFlowGraph<Node> controlFlowGraph;
  private GraphReachability<Node, ControlFlowGraph.Branch> graphReachability;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    rootNode = new Node(Token.DUMMY_TOKEN, null, null);
    controlFlowGraph = new ControlFlowAnalysis(compiler, false, false).process(null, rootNode).getCfg();
    graphReachability = new GraphReachability<>(controlFlowGraph);
    graphReachability.compute(controlFlowGraph.getEntry().getValue());
  }

  @Test
  public void testEmptyFunction() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));
    assertFalse("Function should be removed", compiler.getRootNode().getChildAt(0).equals(functionNode));
  }

  @Test
  public void testReturnWithDeadCode() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.RETURN, null, null));
    bodyNode.appendChild(new Node(Token.ALERT, null, null));
    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));
    assertFalse("Function should be removed", compiler.getRootNode().getChildAt(0).equals(functionNode));
  }

  @Test
  public void testNoOpStatement() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.VOID, null, null));
    bodyNode.appendChild(new Node(Token.TRUE, null, null));
    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));
    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));
  }

  @Test
  public void testUnreachableBranch() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.IF, null, null));
    bodyNode.appendChild(new Node(Token.RETURN, null, null));
    bodyNode.appendChild(new Node(Token.ELSE, null, null));
    bodyNode.appendChild(new Node(Token.RETURN, null, null));
    bodyNode.appendChild(new Node(Token.BLOCK, null, null));
    bodyNode.appendChild(new Node(Token.RETURN, null, null));

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node ifStatement = functionNodeAfter.getChildAt(0);
    Node ifCondition = ifStatement.getFirstChild();
    Node ifTrueBlock = ifStatement.getLastChild();

    List<Node> statementsToRemove = Lists.newArrayList(
        ifCondition, ifTrueBlock.getLastChild(), ifTrueBlock.getLastChild().getNext());

    NodeTraversal.traverseChanged(compiler, functionNodeAfter, new AbstractNodeTraversingVisitor<Node>() {
      @Override
      public void visit(Node node, Node parent) {
        if (statementsToRemove.contains(node)) {
          NodeTraversal.removeChildren(parent, node);
        }
      }
    });

    Node functionNodeAfterRemoved = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be changed", functionNodeAfterRemoved.equals(functionNodeAfter));

    Node ifStatementAfterRemoved = functionNodeAfterRemoved.getChildAt(0);
    Node ifTrueBlockAfterRemoved = ifStatementAfterRemoved.getLastChild();

    assertTrue("Condition and first unreachable statement should be removed",
        ifStatementAfterRemoved.getChildCount() == 1);
    assertTrue("Second unreachable statement should be removed",
        ifTrueBlockAfterRemoved.getNext() == null);
  }

  @Test
  public void testUnreachableBreak() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.LABEL, null, null));
    bodyNode.appendChild(new Node(Token.BREAK, Token.BREAK_STATEMENT, null));
    bodyNode.appendChild(new Node(Token.LABEL, null, null));
    bodyNode.appendChild(new Node(Token.RETURN, null, null));

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node breakStatement = functionNodeAfter.getChildAt(1);
    Node returnStatement = functionNodeAfter.getChildAt(3);

    NodeTraversal.traverseChanged(compiler, functionNodeAfter, new AbstractNodeTraversingVisitor<Node>() {
      @Override
      public void visit(Node node, Node parent) {
        if (node.isLabel()) {
          NodeTraversal.removeChildren(parent, node);
        }
      }
    });

    Node functionNodeAfterRemoved = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be changed", functionNodeAfterRemoved.equals(functionNodeAfter));

    Node breakStatementAfterRemoved = functionNodeAfterRemoved.getChildAt(1);

    assertTrue("Break statement should be removed", breakStatementAfterRemoved == null);
    assertTrue("Return statement should not be removed", functionNodeAfterRemoved.getChildAt(3).equals(returnStatement));
  }

  @Test
  public void testUnreachableContinue() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.LABEL, null, null));
    bodyNode.appendChild(new Node(Token.CONTINUE, Token.CONTINUE_STATEMENT, null));
    bodyNode.appendChild(new Node(Token.LABEL, null, null));
    bodyNode.appendChild(new Node(Token.RETURN, null, null));

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node continueStatement = functionNodeAfter.getChildAt(1);
    Node returnStatement = functionNodeAfter.getChildAt(3);

    NodeTraversal.traverseChanged(compiler, functionNodeAfter, new AbstractNodeTraversingVisitor<Node>() {
      @Override
      public void visit(Node node, Node parent) {
        if (node.isLabel()) {
          NodeTraversal.removeChildren(parent, node);
        }
      }
    });

    Node functionNodeAfterRemoved = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be changed", functionNodeAfterRemoved.equals(functionNodeAfter));

    Node continueStatementAfterRemoved = functionNodeAfterRemoved.getChildAt(1);

    assertTrue("Continue statement should be removed", continueStatementAfterRemoved == null);
    assertTrue("Return statement should not be removed", functionNodeAfterRemoved.getChildAt(3).equals(returnStatement));
  }

  @Test
  public void testUnreachableReturn() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.RETURN, null, null));
    bodyNode.appendChild(new Node(Token.RETURN, null, null));

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node firstReturnStatement = functionNodeAfter.getChildAt(0);
    Node secondReturnStatement = functionNodeAfter.getChildAt(1);

    assertTrue("First return statement should not be removed", functionNodeAfter.getChildAt(0).equals(firstReturnStatement));
    assertTrue("Second return statement should be removed", functionNodeAfter.getChildCount() == 1);
  }

  @Test
  public void testUnreachableReturnWithSideEffect() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.RETURN, null, null));
    bodyNode.appendChild(new Node(Token.VOID, null, null));
    bodyNode.appendChild(new Node(Token.TRUE, null, null));

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node returnStatement = functionNodeAfter.getChildAt(0);
    Node voidStatement = functionNodeAfter.getChildAt(1);
    Node booleanStatement = functionNodeAfter.getChildAt(2);

    assertTrue("Return statement should not be removed", functionNodeAfter.getChildAt(0).equals(returnStatement));
    assertTrue("Void statement should not be removed", functionNodeAfter.getChildAt(1).equals(voidStatement));
    assertTrue("Boolean statement should not be removed", functionNodeAfter.getChildAt(2).equals(booleanStatement));
  }

  @Test
  public void testUnreachableSwitchDefault() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.SWITCH, null, null));
    Node switchStatement = bodyNode.getLastChild();
    Node switchDefault = new Node(Token.SWITCH_DEFAULT, null, null);
    switchStatement.appendChild(switchDefault);

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node switchStatementAfter = functionNodeAfter.getChildAt(0);
    Node switchDefaultAfter = switchStatementAfter.getLastChild();

    assertTrue("Switch statement should not be removed", functionNodeAfter.getChildAt(0).equals(switchStatementAfter));
    assertTrue("Switch default should be removed", switchStatementAfter.getLastChild().equals(null));
  }

  @Test
  public void testUnreachableSwitchCase() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.SWITCH, null, null));
    Node switchStatement = bodyNode.getLastChild();
    Node switchCase = new Node(Token.SWITCH_CASE, null, null);
    Node switchExpression = new Node(Token.NUMBER_LITERAL, null, null);
    switchExpression.setIntValue(5);
    switchCase.appendChild(switchExpression);
    switchStatement.appendChild(switchCase);

    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, false));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be removed", functionNodeAfter.equals(functionNode));

    Node switchStatementAfter = functionNodeAfter.getChildAt(0);
    Node switchCaseAfter = switchStatementAfter.getChildAt(0);

    assertTrue("Switch statement should not be removed", functionNodeAfter.getChildAt(0).equals(switchStatementAfter));
    assertTrue("Switch case should be removed", switchStatementAfter.getChildCount() == 0);
  }

  @Test
  public void testReachableDeadCodeWithSideEffect() {
    Node functionNode = new Node(Token.FUNCTION_DECLARATION, null, null);
    functionNode.setType(Token.FUNCTION_DECLARATION);
    Node bodyNode = new Node(Token.BLOCK, null, null);
    functionNode.appendChild(bodyNode);
    bodyNode.appendChild(new Node(Token.RETURN, null, null));
    bodyNode.appendChild(new Node(Token.VOID, null, null));
    bodyNode.appendChild(new Node(Token.TRUE, null, null));

    // Reachable dead code with side effect should not be removed
    NodeTraversal.traverse(compiler, functionNode, new UnreachableCodeElimination(compiler, true));

    Node functionNodeAfter = compiler.getRootNode().getChildAt(0);
    assertTrue("Function should not be changed", functionNodeAfter.equals(functionNode));

    Node returnStatement = functionNodeAfter.getChildAt(0);
    Node voidStatement = functionNodeAfter.getChildAt(1);
    Node booleanStatement = functionNodeAfter.getChildAt(2);

    assertTrue("Return statement should not be removed", functionNodeAfter.getChildAt(0).equals(returnStatement));
    assertTrue("Void statement should not be removed", functionNodeAfter.getChildAt(1).equals(voidStatement));
    assertTrue("Boolean statement should not be removed", functionNodeAfter.getChildAt(2).equals(booleanStatement));
  }
}