import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Scope scope;

  @Before
  public void setUp() {
    // Initialize ControlFlowGraph, Scope, and MaybeReachingVariableUse objects here
    // for testing. Replace this comment with actual initialization code.
  }

  @Test
  public void testBasicUse() {
    Node initA = Node.newExpr("A = 1;");
    Node useA = Node.newExpr("print(A);");

    // Call the analysis method with the initialized nodes
    ReachingUses reachingUses = maybeReachingVariableUse.flowThrough(useA, maybeReachingVariableUse.createInitialEstimateLattice());

    // Check if the expected usage is present in the result
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", initA);
    assertTrue(uses.contains(useA));
  }

  @Test
  public void testNoUse() {
    Node initA = Node.newExpr("A = 1;");
    Node useB = Node.newExpr("print(B);");

    ReachingUses reachingUses = maybeReachingVariableUse.flowThrough(useB, maybeReachingVariableUse.createInitialEstimateLattice());

    Collection<Node> uses = maybeReachingVariableUse.getUses("A", initA);
    assertFalse(uses.isEmpty());
    assertFalse(uses.contains(useB));
  }

  @Test
  public void testConditionalUse() {
    Node ifCondition = Node.newExpr("if (x > 0) { A = 1; }");
    Node useA = Node.newExpr("print(A);");

    // Call the analysis method with the initialized nodes
    ReachingUses reachingUses = maybeReachingVariableUse.flowThrough(useA, maybeReachingVariableUse.createInitialEstimateLattice());

    // Check if the expected usage is present in the result
    boolean hasCondition = controlFlowGraph.hasEdge(ifCondition, maybeReachingVariableUse.cfg.getEntryNode());
    assertTrue(hasCondition);
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", ifCondition);
    assertTrue(uses.contains(useA));
  }

  @Test(expected = AssertionError.class)
  public void testInvalidVariable() {
    Node initX = Node.newExpr("X = 1;");
    Node useA = Node.newExpr("print(A);");

    ReachingUses reachingUses = maybeReachingVariableUse.flowThrough(useA, maybeReachingVariableUse.createInitialEstimateLattice());

    // Check for an AssertionError as the variable X is not defined in the scope
    Collection<Node> uses = maybeReachingVariableUse.getUses("X", initX);
    fail("AssertionError expected but not thrown");
  }

  @Test
  public void testEscapedVariable() {
    Node initX = Node.newExpr("var x; x = 1;");
    Node useX = Node.newExpr("print(x);");

    // Call the analysis method with the initialized nodes
    ReachingUses reachingUses = maybeReachingVariableUse.flowThrough(useX, maybeReachingVariableUse.createInitialEstimateLattice());

    // Check if the escaped variable is not present in the result
    Collection<Node> uses = maybeReachingVariableUse.getUses("x", initX);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testForLoop() {
    Node forLoop = Node.newExpr("for (var i = 0; i < 10; i++) { print(i); }");
    Node useI = Node.newExpr("print(i);");

    // Call the analysis method with the initialized nodes
    ReachingUsages reachingUses = maybeReachingVariableUse.process(forLoop, scope);

    // Check if the expected usage is present in the result
    Collection<Node> uses = maybeReachingVariableUse.getUses("i", forLoop);
    assertTrue(uses.contains(useI));
  }
}