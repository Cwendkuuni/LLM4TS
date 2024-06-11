import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;
  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    // Initialize the scope and control flow graph for testing.
    // Replace this with actual initialization code for your specific test case.
    jsScope = new Scope();
    cfg = new ControlFlowGraph<Node>(new Node(Token.SCRIPT), null);
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testEmptyProgram() {
    LiveVariablesAnalysis emptyProgramAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    assertEquals("{},", emptyProgramAnalysis.getEntryLattice().toString());
  }

  @Test
  public void testSimpleVariableAssignment() {
    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.NAME, "x");
    assignmentNode.addChild(lhs);
    Node rhs = new Node(Token.NUMBER, 5);
    assignmentNode.addChild(rhs);

    liveVariablesAnalysis.addCFGEdge(assignmentNode, null, null);

    LiveVariablesAnalysis analysis = liveVariablesAnalysis.computeLattice(null, null);
    assertTrue(analysis.isLive("x"));
  }

  @Test
  public void testVariableAssignmentInConditionalBlock() {
    Node ifNode = new Node(Token.IF, null);
    Node condition = new Node(Token.NAME, "x");
    ifNode.addChild(condition);

    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.NAME, "y");
    assignmentNode.addChild(lhs);
    Node rhs = new Node(Token.NUMBER, 5);
    assignmentNode.addChild(rhs);

    ifNode.addChild(assignmentNode);
    ifNode.addChild(new Node(Token.BLOCK, null));

    liveVariablesAnalysis.addCFGEdge(ifNode, condition, null);

    LiveVariablesAnalysis analysis = liveVariablesAnalysis.computeLattice(null, null);
    assertFalse(analysis.isLive("y")); // y is not live outside the if block
    assertTrue(analysis.isLive("y")); // y is live inside the if block
  }

  @Test
  public void testVariableAssignmentInLoop() {
    Node forNode = new Node(Token.FOR, null);
    Node condition = new Node(Token.NAME, "x");
    forNode.addChild(condition);

    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.NAME, "y");
    assignmentNode.addChild(lhs);
    Node rhs = new Node(Token.NUMBER, 5);
    assignmentNode.addChild(rhs);

    forNode.addChild(assignmentNode);
    forNode.addChild(new Node(Token.BLOCK, null));

    liveVariablesAnalysis.addCFGEdge(forNode, condition, null);

    LiveVariablesAnalysis analysis = liveVariablesAnalysis.computeLattice(null, null);
    assertTrue(analysis.isLive("y")); // y is live inside the loop
  }

  @Test(expected = AssertionError.class)
  public void testVariableAssignmentInExceptionBlock() {
    Node tryNode = new Node(Token.TRY, null);
    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.NAME, "y");
    assignmentNode.addChild(lhs);
    Node rhs = new Node(Token.NUMBER, 5);
    assignmentNode.addChild(rhs);

    Node exceptionNode = new Node(Token.THROW, null);
    tryNode.addChild(assignmentNode);
    tryNode.addChild(exceptionNode);
    tryNode.addChild(new Node(Token.BLOCK, null));

    liveVariablesAnalysis.addCFGEdge(tryNode, null, exceptionNode);

    LiveVariablesAnalysis analysis = liveVariablesAnalysis.computeLattice(null, null);
    assertTrue(analysis.isLive("y")); // y is live before the exception

    analysis.flowThrough(exceptionNode, analysis.getEntryLattice());

    // This test should fail due to the exception, but JUnit should print an
    // error message indicating that analysis.isLive("y") threw an
    // AssertionError.
    assertFalse(analysis.isLive("y")); // y should not be live after the exception
  }

  @Test
  public void testEscapedVariables() {
    String functionName = "testFunction";
    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.addChild(new Node(Token.NAME, functionName));

    Node argumentNode = new Node(Token.NAME, "arg");
    functionNode.addChild(argumentNode);

    Node assignmentNode = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.NAME, "x");
    assignmentNode.addChild(lhs);
    Node rhs = new Node(Token.NAME, "arg");
    assignmentNode.addChild(rhs);

    functionNode.addChild(assignmentNode);
    functionNode.addChild(new Node(Token.BLOCK, null));

    liveVariablesAnalysis.addCFGEdge(functionNode, null, null);

    LiveVariablesAnalysis analysis = liveVariablesAnalysis.computeLattice(null, null);
    assertTrue(analysis.isLive("arg")); // arg is live in the function
    assertTrue(analysis.isLive("x")); // x is live because it escapes the function

    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertEquals(1, escapedLocals.size());
    assertEquals("x", escapedLocals.iterator().next().getName());
  }
}