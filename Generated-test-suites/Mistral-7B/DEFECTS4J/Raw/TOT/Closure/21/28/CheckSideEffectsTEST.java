import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

  private static final String JS_SOURCE =
      "function foo() { return 42; } " +
      "function bar() { x = 1; }";

  @Test
  public void testCheckSideEffects() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CompilerOptions options = new CompilerOptions(null);
    options.setCheckSideEffects(level);
    Node root = NodeFactory.createProgram(JS_SOURCE, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " warning(s)", level.getLevel(), warningCount);
    if (warningCount > 0) {
      Node warning = compiler.getDiagnostics()[0].getSourceNode();
      String message = warning.getJSDocInfo().getSummary();
      assertTrue("Expected warning message to contain 'Suspicious code'", message.contains("Suspicious code"));
      assertTrue("Expected warning message to contain 'bar'", message.contains("bar"));
    }

    options.setCheckSideEffects(CheckLevel.ERROR);
    root = NodeFactory.createProgram(JS_SOURCE, null);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int errorCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + CheckLevel.ERROR.getLevel() + " error(s)", errorCount, 1);
    warningCount = compiler.getDiagnostics().length() - errorCount;
    assertEquals("Expected " + level.getLevel() + " warning(s)", warningCount, 0);

    root = NodeFactory.createProgram(JS_SOURCE + " x = 1;", null);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " warning(s)", warningCount, 0);

    root = NodeFactory.createProgram("function foo() { return 42; }", null);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected 0 warning(s)", warningCount, 0);
  }

  @Test
  public void testProtectSideEffects() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CompilerOptions options = new CompilerOptions(null);
    options.setCheckSideEffects(level);
    options.setProtectSideEffects(true);
    String source = "function foo() { return 42; } ";
    Node root = NodeFactory.createProgram(source, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " warning(s)", warningCount, 0);

    source += "function bar() { x = 1; }";
    root = NodeFactory.createProgram(source, null);
    checkSideEffects = new CheckSideEffects(compiler, level, true);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int protectionCount = compiler.getExterns().getChildCount();
    Node protection = compiler.getExterns().getChild(protectionCount - 1);
    assertNotNull("Expected protection function to be present", protection);
    String protectionName = protection.getFirstChild().getString();
    assertTrue("Expected protection function to be named 'JSCOMPILER_PRESERVE'",
        protectionName.equals("JSCOMPILER_PRESERVE"));

    root = NodeFactory.createProgram(JS_SOURCE, null);
    checkSideEffects = new CheckSideEffects(compiler, level, true);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " warning(s)", warningCount, 1);

    Node warning = compiler.getDiagnostics()[0].getSourceNode();
    String warningMessage = warning.getJSDocInfo().getSummary();
    assertTrue("Expected warning message to contain 'Suspicious code'",
        warningMessage.contains("Suspicious code"));
    assertTrue("Expected warning message to contain 'bar'", warningMessage.contains("bar"));
    Node warningCall = warning.getLastChild();
    String warningCallName = warningCall.getFirstChild().getString();
    assertTrue("Expected warning message to contain call to 'JSCOMPILER_PRESERVE'",
        warningCallName.equals("JSCOMPILER_PRESERVE"));
  }

  @Test
  public void testStripProtection() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CompilerOptions options = new CompilerOptions(null);
    options.setCheckSideEffects(level);
    String source = "function foo() { JSCOMPILER_PRESERVE(bar()); } ";
    Node root = NodeFactory.createProgram(source, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, false);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " warning(s)", warningCount, 1);

    CompilerPass stripProtection = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal stripProtectionNodeTraversal = new NodeTraversal(compiler, root, stripProtection, null);
    stripProtectionNodeTraversal.traverse();
    warningCount = compiler.getDiagnostics().length();
    assertEquals("Expected 0 warning(s)", warningCount, 0);

    Node call = root.getFirstChild();
    Node callTarget = call.getFirstChild();
    assertTrue("Expected call target to be named 'bar'", callTarget.getString().equals("bar"));
    Node callArgument = call.getLastChild();
    assertFalse("Expected call argument to be present", callArgument != null);
  }

  @Test
  public void testProtectSideEffectsError() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.ERROR;
    CompilerOptions options = new CompilerOptions(null);
    options.setCheckSideEffects(level);
    options.setProtectSideEffects(true);
    String source = "function foo() { return 42; } ";
    Node root = NodeFactory.createProgram(source, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int errorCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " error(s)", errorCount, 0);

    source += "function bar() { x = 1; }";
    root = NodeFactory.createProgram(source, null);
    checkSideEffects = new CheckSideEffects(compiler, level, true);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int protectionCount = compiler.getExterns().getChildCount();
    Node protection = compiler.getExterns().getChild(protectionCount - 1);
    assertNotNull("Expected protection function to be present", protection);
    String protectionName = protection.getFirstChild().getString();
    assertTrue("Expected protection function to be named 'JSCOMPILER_PRESERVE'",
        protectionName.equals("JSCOMPILER_PRESERVE"));

    root = NodeFactory.createProgram(JS_SOURCE, null);
    checkSideEffects = new CheckSideEffects(compiler, level, true);
    nodeTraversal = new NodeTraversal(compiler, root, checkSideEffects, null);
    nodeTraversal.traverse();
    int errorCount = compiler.getDiagnostics().length();
    assertEquals("Expected " + level.getLevel() + " error(s)", errorCount, 1);

    Node error = compiler.getDiagnostics()[0].getSourceNode();
    String errorMessage = error.getJSDocInfo().getSummary();
    assertTrue("Expected error message to contain 'JSC_USELESS_CODE'",
        errorMessage.contains("JSC_USELESS_CODE"));
    assertTrue("Expected error message to contain 'Suspicious code'",
        errorMessage.contains("Suspicious code"));
    assertFalse("Expected error message to not contain 'bar'", errorMessage.contains("bar"));
  }
}