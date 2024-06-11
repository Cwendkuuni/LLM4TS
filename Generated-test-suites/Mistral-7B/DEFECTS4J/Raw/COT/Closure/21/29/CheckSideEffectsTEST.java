import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeUtil;

/**
 * Test class for CheckSideEffects class
 */
@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  /**
   * Test method for CheckSideEffects constructor.
   */
  @Test
  public void testCheckSideEffectsConstructor() {
    AbstractCompiler compiler = new Checker();
    CheckLevel level = WARNING;
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    assertNotNull(checkSideEffects);
  }

  /**
   * Test method for process method.
   */
  @Test
  public void testProcessMethod() {
    CompilerInput input = new TestSourceFile("Test.js", "var s = \"this string is \";\n" +
            "var x = foo();\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input.getExternsNode(), root);
    // Add assertions for warning messages and side effect free nodes here
  }

  /**
   * Test method for hotSwapScript method.
   */
  @Test
  public void testHotSwapScriptMethod() {
    // Add code for testing hotSwapScript method here
  }

  /**
   * Test method for visit method: VOID nodes.
   */
  @Test
  public void testVisitVoidNodes() {
    CompilerInput input = new TestSourceFile("Test.js", "var s = \"this string is \";\n" +
            ";\n" +
            "var x = foo();\n" +
            ";\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input.getExternsNode(), root);
    // Add assertions for VOID nodes here
  }

  /**
   * Test method for visit method: Non-VOID nodes in parent NodeType = COMMA.
   */
  @Test
  public void testVisitNonVoidNodesParentComma() {
    CompilerInput input = new TestSourceFile("Test.js", "var s = \"this string is \";\n" +
            "var x = foo();,\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input.getExternsNode(), root);
    // Add assertions for non-VOID nodes in parent NodeType = COMMA here
  }

  /**
   * Test method for visit method: Parent NodeType != EXPR_RESULT and != BLOCK.
   */
  @Test
  public void testVisitParentNodeTypeNotExprResultAndNotBlock() {
    CompilerInput input = new TestSourceFile("Test.js", "for (var i = 0; i < 5; i++) {\n" +
            "  var s = \"this string is \";\n" +
            "  var x = foo();\n" +
            "}\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input.getExternsNode(), root);
    // Add assertions for parent NodeType != EXPR_RESULT and != BLOCK here
  }

  /**
   * Test method for visit method: Parent FOR with 4 children.
   */
  @Test
  public void testVisitParentFORWithFourChildren() {
    CompilerInput input = new TestSourceFile("Test.js", "for (var i = 0, j = 0; i < 5, j < 5; i++, j++) {\n" +
            "  var s = \"this string is \";\n" +
            "  var x = foo();\n" +
            "}\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input.getExternsNode(), root);
    // Add assertions for parent FOR with 4 children here
  }

  /**
   * Test method for protectSideEffects method.
   */
  @Test
  public void testProtectSideEffectsMethod() {
    CompilerInput input = new TestSourceFile("Test.js", "var s = \"this string is \";\n" +
            "var x = foo();\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    Node root = input.getAstRoot(compiler);
    checkSideEffects.process(input.getExternsNode(), root);
    // Add assertions for protected side effect free nodes here
  }

  /**
   * Test method for StripProtection class constructor.
   */
  @Test
  public void testStripProtectionConstructor() {
    AbstractCompiler compiler = new Checker();
    StripProtection stripProtection = new StripProtection(compiler);
    assertNotNull(stripProtection);
  }

  /**
   * Test method for StripProtection class process method.
   */
  @Test
  public void testStripProtectionProcessMethod() {
    CompilerInput input = new TestSourceFile("Test.js", "function JSCOMPILER_PRESERVE() {\n" +
            "  var s = \"this string is \";\n" +
            "  var x = foo();\n" +
            "}\n" +
            "JSCOMPILER_PRESERVE();\n" +
            "foo();;\n").toInput(new CompilerOptions());
    AbstractCompiler compiler = new Checker();
    StripProtection stripProtection = new StripProtection(compiler);
    Node root = input.getAstRoot(compiler);
    stripProtection.process(input.getExternsNode(), root);
    // Add assertions for removed protected nodes here
  }
}