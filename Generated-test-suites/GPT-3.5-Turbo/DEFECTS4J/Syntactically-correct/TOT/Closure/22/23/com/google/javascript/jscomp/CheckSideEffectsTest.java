import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Typical use case
    // The method should generate warnings for suspicious code
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    checkSideEffects.process(externs, root);
    // Assert that warnings are generated
    assertEquals(1, checkSideEffects.getCompiler().getErrorCount());
    assertEquals(1, checkSideEffects.getCompiler().getWarningCount());

    // Edge case
    // The method should handle an empty externs and root node
    CheckSideEffects checkSideEffects2 = new CheckSideEffects(null, null, false);
    Node emptyExterns = new Node(Token.EXTERN);
    Node emptyRoot = new Node(Token.ROOT);
    checkSideEffects2.process(emptyExterns, emptyRoot);
    // Assert that no warnings or errors are generated
    assertEquals(0, checkSideEffects2.getCompiler().getErrorCount());
    assertEquals(0, checkSideEffects2.getCompiler().getWarningCount());
  }

  @Test
  public void testHotSwapScript() {
    // Typical use case
    // The method should generate warnings for suspicious code
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.SCRIPT);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    // Assert that warnings are generated
    assertEquals(1, checkSideEffects.getCompiler().getErrorCount());
    assertEquals(1, checkSideEffects.getCompiler().getWarningCount());

    // Edge case
    // The method should handle an empty scriptRoot and originalRoot
    CheckSideEffects checkSideEffects2 = new CheckSideEffects(null, null, false);
    Node emptyScriptRoot = new Node(Token.SCRIPT);
    Node emptyOriginalRoot = new Node(Token.SCRIPT);
    checkSideEffects2.hotSwapScript(emptyScriptRoot, emptyOriginalRoot);
    // Assert that no warnings or errors are generated
    assertEquals(0, checkSideEffects2.getCompiler().getErrorCount());
    assertEquals(0, checkSideEffects2.getCompiler().getWarningCount());
  }

  @Test
  public void testVisit() {
    // Typical use case
    // The method should generate a warning for code with no side-effects
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
    NodeTraversal t = new NodeTraversal(checkSideEffects.getCompiler(), checkSideEffects);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.EXPR_RESULT);
    checkSideEffects.visit(t, n, parent);
    // Assert that a warning is generated
    assertEquals(1, checkSideEffects.getCompiler().getErrorCount());
    assertEquals(0, checkSideEffects.getCompiler().getWarningCount());

    // Edge case
    // The method should handle an empty NodeTraversal and null values for n and parent
    CheckSideEffects checkSideEffects2 = new CheckSideEffects(null, null, false);
    NodeTraversal emptyTraversal = new NodeTraversal(checkSideEffects2.getCompiler(), checkSideEffects2);
    checkSideEffects2.visit(emptyTraversal, null, null);
    // Assert that no warnings or errors are generated
    assertEquals(0, checkSideEffects2.getCompiler().getErrorCount());
    assertEquals(0, checkSideEffects2.getCompiler().getWarningCount());
  }

  @Test
  public void testProtectSideEffects() {
    // Typical use case
    // The method should protect side-effect free nodes by making them parameters to an extern function call
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, true);
    Node node = new Node(Token.VAR);
    NodeTraversal t = new NodeTraversal(checkSideEffects.getCompiler(), checkSideEffects);
    checkSideEffects.getProblemNodes().add(node);
    checkSideEffects.protectSideEffects();
    // Assert that the node is replaced with a function call
    Node replacement = node.getParent().getFirstChild();
    assertEquals(Token.CALL, replacement.getType());

    // Edge case
    // The method should handle an empty list of problem nodes
    CheckSideEffects checkSideEffects2 = new CheckSideEffects(null, null, true);
    checkSideEffects2.protectSideEffects();
    // Assert that no warnings or errors are generated
    assertEquals(0, checkSideEffects2.getCompiler().getErrorCount());
    assertEquals(0, checkSideEffects2.getCompiler().getWarningCount());
  }

  @Test
  public void testAddExtern() {
    // Typical use case
    // The method should add an external function to the input's abstract syntax tree
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, true);
    checkSideEffects.addExtern();
    // Assert that an extern function is added to the input's abstract syntax tree
    assertNotNull(checkSideEffects.getCompiler().getSynthesizedExternsInput().getAstRoot(checkSideEffects.getCompiler()).getFirstChild());
    assertTrue(checkSideEffects.getCompiler().getSynthesizedExternsInput().getAstRoot(checkSideEffects.getCompiler()).getFirstChild().isVar());

    // Edge case
    // The method should handle an input without an abstract syntax tree
    CheckSideEffects checkSideEffects2 = new CheckSideEffects(null, null, true);
    checkSideEffects2.getCompiler().setSynthesizedExternsInput(null);
    checkSideEffects2.addExtern();
    // Assert that no warnings or errors are generated
    assertEquals(0, checkSideEffects2.getCompiler().getErrorCount());
    assertEquals(0, checkSideEffects2.getCompiler().getWarningCount());
  }

  @Test
  public void testStripProtection() {
    // Typical use case
    // The method should remove side-effect sync functions
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
    NodeTraversal t = new NodeTraversal(stripProtection.getCompiler(), stripProtection);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.CALL);
    n.setString("JSCOMPILER_PRESERVE");
    parent.addChildToBack(n);
    stripProtection.visit(t, parent, parent.getParent());
    // Assert that the side-effect sync function is removed
    assertEquals(0, parent.getChildCount());

    // Edge case
    // The method should handle an empty NodeTraversal and null values for n and parent
    CheckSideEffects.StripProtection stripProtection2 = new CheckSideEffects.StripProtection(null);
    NodeTraversal emptyTraversal = new NodeTraversal(stripProtection2.getCompiler(), stripProtection2);
    stripProtection2.visit(emptyTraversal, null, null);
    // Assert that no warnings or errors are generated
    assertEquals(0, stripProtection2.getCompiler().getErrorCount());
    assertEquals(0, stripProtection2.getCompiler().getWarningCount());
  }

}