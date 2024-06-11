import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
  private Scope scope;
  private ControlFlowGraph<Node> cfg;
  private AbstractCompiler compiler;

  @Before
  public void setup() {
    // Set up test data here, e.g. create a simple Node tree.
    Node root = new Node(Token.SCRIPT);
    Node funcNode = new Node(Token.FUNCTION);
    root.addChild(funcNode);
    funcNode.addChild(new Node(Token.VAR, "a"));
    funcNode.addChild(new Node(Token.VAR, "b"));
    funcNode.addChild(new Node(Token.ASSIGN, "a", new Node(Token.NAME, "b")));
    scope = new Scope(null);
    cfg = new ControlFlowGraph<>(root);
    compiler = new AbstractCompiler(cfg) {};
  }

  @Test
  public void testLiveVariablesAnalysis_SimpleAssignment() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    LiveVariableLattice expectedResult = new LiveVariableLattice(scope.getVarCount());
    expectedResult.liveSet.set(0); // a
    expectedResult.liveSet.set(1); // b

    LiveVariableLattice result = analysis.process(funcNode, analysis.createEntryLattice());

    assertEquals(expectedResult, result);
  }

  @Test
  public void testLiveVariablesAnalysis_ConditionalAssignment() {
    Node conditionNode = new Node(Token.IF);
    Node trueBranch = new Node(Token.ASSIGN, "a", new Node(Token.NAME, "b"));
    conditionNode.addChild(trueBranch);
    Node falseBranch = new Node(Token.ASSIGN, "a", new Node(Token.LITERAL, 0));
    conditionNode.addChild(falseBranch);
    funcNode.addChild(conditionNode);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    LiveVariableLattice expectedResult = new LiveVariableLattice(scope.getVarCount());
    expectedResult.liveSet.set(0); // a
    expectedResult.liveSet.set(1); // b

    LiveVariableLattice result = analysis.process(funcNode, analysis.createEntryLattice());

    assertEquals(expectedResult, result);
  }

  @Test
  public void testLiveVariablesAnalysis_DeadAssignment() {
    Node deadAssignmentNode = new Node(Token.ASSIGN, "a", new Node(Token.NAME, "a"));
    funcNode.addChild(deadAssignmentNode);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);
    LiveVariableLattice expectedResult = new LiveVariableLattice(scope.getVarCount());
    expectedResult.liveSet.set(0); // a

    LiveVariableLattice result = analysis.process(funcNode, analysis.createEntryLattice());

    assertEquals(expectedResult, result);
  }

  @Test
  public void testLiveVariablesAnalysis_EscapedVariables() {
    Node funcNodeWithArguments = new Node(Token.FUNCTION);
    Node lpNode = new Node(Token.LPAREN);
    funcNodeWithArguments.addChild(lpNode);
    Node argNode1 = new Node(Token.NAME, "arg1");
    Node argNode2 = new Node(Token.NAME, "arg2");
    lpNode.addChild(argNode1);
    lpNode.addChild(argNode2);
    funcNodeWithArguments.addChild(new Node(Token.RPAREN));
    root.addChild(funcNodeWithArguments);

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);

    Set<Var> escapedLocals = analysis.getEscapedLocals();
    List<Var> expectedEscapedLocals = Arrays.asList(scope.getVar("arg1"), scope.getVar("arg2"));

    assertEquals(expectedEscapedLocals, escapedLocals);
  }
}