import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Typical use case
    // Create a compiler and CheckSideEffects instance
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    // Create the input JavaScript code
    String code = "var x = 5;";
    Node externs = compiler.parseInputs("", code);
    Node root = compiler.parseInputs(code, "");

    // Call the process method to run the checks
    checkSideEffects.process(externs, root);

    // Check that the expected diagnostic message was reported
    String expectedMessage = "This code lacks side-effects. Is there a bug?";
    assertEquals(expectedMessage, compiler.getWarningLevelOfLastReportedMessage().getType().toString());
  }

  @Test
  public void testHotSwapScript() {
    // Edge case
    // Create a compiler and CheckSideEffects instance
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    // Create the input JavaScript code
    String code = "var x = 5;";
    Node scriptRoot = compiler.parseInputs("", code);
    Node originalRoot = compiler.parseInputs("", code);

    // Call the hotSwapScript method to run the checks
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // Check that the expected diagnostic message was reported
    String expectedMessage = "This code lacks side-effects. Is there a bug?";
    assertEquals(expectedMessage, compiler.getWarningLevelOfLastReportedMessage().getType().toString());
  }

  @Test
  public void testVisit() {
    // Error scenario
    // Create a compiler and CheckSideEffects instance
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    // Create a sample node to visit
    Node n = new Node(Token.NAME, "x");
    Node parent = new Node(Token.EXPR_RESULT);

    // Call the visit method to run the checks
    NodeTraversal t = new NodeTraversal(compiler, null);
    checkSideEffects.visit(t, n, parent);

    // Check that the expected diagnostic message was reported
    String expectedMessage = "This code lacks side-effects. Is there a bug?";
    assertEquals(expectedMessage, compiler.getWarningLevelOfLastReportedMessage().getType().toString());
  }

  @Test
  public void testProtectSideEffects() {
    // Edge case
    // Create a compiler and CheckSideEffects instance
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    // Create a sample problem node
    Node problemNode = new Node(Token.NAME, "x");

    // Add the problem node to the list of problem nodes
    checkSideEffects.problemNodes.add(problemNode);

    // Call the protectSideEffects method to protect the side-effect free nodes
    checkSideEffects.protectSideEffects();

    // Check that the problem node was properly protected
    String expectedProtectionFunction = "JSCOMPILER_PRESERVE";
    assertEquals(expectedProtectionFunction, problemNode.getParent().getFirstChild().getString());
  }

  @Test
  public void testStripProtection() {
    // Typical use case
    // Create a compiler and StripProtection instance
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);

    // Create the input JavaScript code
    String code = "JSCOMPILER_PRESERVE('protected code');";
    Node externs = compiler.parseInputs("", code);
    Node root = compiler.parseInputs(code, "");

    // Call the process method to strip the protection and remove the function call
    stripProtection.process(externs, root);

    // Check that the protection function call was removed
    assertNull(root.getFirstChild());
  }
}