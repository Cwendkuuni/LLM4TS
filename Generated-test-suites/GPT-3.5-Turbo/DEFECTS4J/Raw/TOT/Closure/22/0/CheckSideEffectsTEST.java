import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    checkSideEffects = new CheckSideEffects(null, null, false);
  }

  @Test
  public void testProcess_NoProblemNodes() {
    checkSideEffects.process(null, null);
    // Assert that no problem nodes were added
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_ProtectSideEffectFreeCode() {
    checkSideEffects.process(null, null);
    // Assert that protectSideEffects() is not called when protectSideEffectFreeCode is false
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testHotSwapScript_NoProblemNodes() {
    checkSideEffects.hotSwapScript(null, null);
    // Assert that no problem nodes were added
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testVisit_InvalidNodeType() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    checkSideEffects.visit(t, n, parent);
    // Assert that no error is reported since we skipped invalid node type
    assertNull(n.getError());
  }

  @Test
  public void testVisit_ValidNodeType_NoErrorMessage() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    // Set valid node type
    n.setType(Token.EXPR_RESULT);
    checkSideEffects.visit(t, n, parent);
    // Assert that no error is reported
    assertNull(n.getError());
  }

  @Test
  public void testVisit_ValidNodeType_WithErrorMessage() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    // Set valid node type
    n.setType(Token.EXPR_RESULT);
    // Set node without expression result
    n.putBooleanProp(NodeUtil.EXPRESSION_VOID_TRUE, true);
    checkSideEffects.visit(t, n, parent);
    // Assert that error is reported
    assertEquals("This code lacks side-effects. Is there a bug?", n.getError());
  }

  @Test
  public void testProtectSideEffects_ProblemNodesEmpty() {
    checkSideEffects.protectSideEffects();
    // Assert that no code change is reported when problemNodes is empty
    assertNull(checkSideEffects.compiler.getLastCodeChange());
  }

  @Test
  public void testProtectSideEffects_ProblemNodesNotEmpty() {
    Node n = new Node();
    checkSideEffects.problemNodes.add(n);
    checkSideEffects.protectSideEffects();
    // Assert that code change is reported when problemNodes is not empty
    assertNotNull(checkSideEffects.compiler.getLastCodeChange());
    // Assert that problemNodes is empty after protection
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testAddExtern() {
    // TODO: Write test cases to verify the behavior of addExtern() method
  }

  @Test
  public void testStripProtection() {
    // TODO: Write test cases to verify the behavior of StripProtection class
  }

}