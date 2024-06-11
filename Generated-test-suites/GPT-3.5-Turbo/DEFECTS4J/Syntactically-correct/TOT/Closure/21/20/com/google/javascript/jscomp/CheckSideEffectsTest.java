import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  // Test for process() method
  @Test
  public void testProcess() {
    // Test case with a side effect
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    Node externs = new Node(Token.EXPR_RESULT);
    Node root = new Node(Token.EXPR_RESULT);
    cse.process(externs, root);
    // Assert that some problemNodes are added
    assertNotNull(cse.getProblemNodes());
    // Assert that code change is reported
    assertTrue(cse.getCompiler().hasReportedCodeChange());

    // Test case with no side effect
    CheckSideEffects cse2 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    Node externs2 = new Node(Token.EXPR_RESULT);
    Node root2 = new Node(Token.EXPR_RESULT);
    cse2.process(externs2, root2);
    // Assert no problemNodes are added
    assertNull(cse2.getProblemNodes());
    // Assert that code change is not reported
    assertFalse(cse2.getCompiler().hasReportedCodeChange());
  }

  // Test for hotSwapScript() method
  @Test
  public void testHotSwapScript() {
    // Test case with a side effect
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    Node scriptRoot = new Node(Token.EXPR_RESULT);
    Node originalRoot = new Node(Token.EXPR_RESULT);
    cse.hotSwapScript(scriptRoot, originalRoot);
    // Assert that some problemNodes are added
    assertNotNull(cse.getProblemNodes());
    // Assert that code change is reported
    assertTrue(cse.getCompiler().hasReportedCodeChange());

    // Test case with no side effect
    CheckSideEffects cse2 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    Node scriptRoot2 = new Node(Token.EXPR_RESULT);
    Node originalRoot2 = new Node(Token.EXPR_RESULT);
    cse2.hotSwapScript(scriptRoot2, originalRoot2);
    // Assert no problemNodes are added
    assertNull(cse2.getProblemNodes());
    // Assert that code change is not reported
    assertFalse(cse2.getCompiler().hasReportedCodeChange());
  }

  // Test for visit() method
  @Test
  public void testVisit() {
    // Test case with a side effect
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    NodeTraversal t = new NodeTraversal(cse.getCompiler(), null);
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EXPR_RESULT);
    cse.visit(t, n, parent);
    // Assert that some problemNodes are added
    assertNotNull(cse.getProblemNodes());
    // Assert that code change is reported
    assertTrue(cse.getCompiler().hasReportedCodeChange());

    // Test case with no side effect
    CheckSideEffects cse2 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    NodeTraversal t2 = new NodeTraversal(cse2.getCompiler(), null);
    Node n2 = new Node(Token.EMPTY);
    Node parent2 = new Node(Token.EXPR_RESULT);
    cse2.visit(t2, n2, parent2);
    // Assert no problemNodes are added
    assertNull(cse2.getProblemNodes());
    // Assert that code change is not reported
    assertFalse(cse2.getCompiler().hasReportedCodeChange());
  }

  // Test for protectSideEffects() method
  @Test
  public void testProtectSideEffects() {
    // Test case with problemNodes
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    Node n1 = new Node(Token.NAME);
    Node n2 = new Node(Token.NAME);
    cse.getProblemNodes().add(n1);
    cse.getProblemNodes().add(n2);
    cse.protectSideEffects();
    // Assert that nodes are replaced with a function call
    assertEquals(Node.CALL, n1.getParent().getType());
    assertEquals(Node.CALL, n2.getParent().getType());
    // Assert that code change is reported
    assertTrue(cse.getCompiler().hasReportedCodeChange());

    // Test case with no problemNodes
    CheckSideEffects cse2 = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    cse2.protectSideEffects();
    // Assert that no nodes are replaced
    assertNull(cse2.getProblemNodes());
    // Assert that code change is not reported
    assertFalse(cse2.getCompiler().hasReportedCodeChange());
  }

  // Test for addExtern() method
  @Test
  public void testAddExtern() {
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    cse.addExtern();
    // Assert that a new var node is added
    Node externs = cse.getCompiler().getSynthesizedExternsInput().getAstRoot(cse.getCompiler());
    Node var = externs.getLastChild();
    assertEquals(Node.VAR, var.getType());
    // Assert that code change is reported
    assertTrue(cse.getCompiler().hasReportedCodeChange());
  }

  // Test for StripProtection class
  @Test
  public void testStripProtection() {
    // Test case with single call node
    CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(new AbstractCompiler());
    NodeTraversal t = new NodeTraversal(sp.getCompiler(), null);
    Node n = new Node(Token.CALL);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(n);
    sp.visit(t, n, parent);
    // Assert that the call node is replaced with its child
    assertEquals(n.getFirstChild(), parent.getFirstChild());
  }

}