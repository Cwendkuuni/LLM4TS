package com.google.javascript.jscomp.CheckSideEffectsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.Parser;

/**
 * Tests for {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { x = foo(); } "
        + "function qux() { return x == 42; }";

    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The first call to foo() is side-effect free, so no warning should be generated.
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { x = foo(); } "
        + "function qux() { return x; }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The second call to foo() in function 'baz' is not side-effect free,
    // as it assigns the value returned by foo() to 'x'.
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getErrorMessages().get(0).getMessage());

    source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { return foo(); } "
        + "function qux() { return x == 42; }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The call to foo() in function 'baz' is side-effect free, as it is the return statement.
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { x = foo(); } "
        + "function qux() { return x; } "
        + "function quux() { return x + 1; }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The second call to foo() in function 'baz' is not side-effect free,
    // as it assigns the value returned by foo() to 'x'.
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getErrorMessages().get(0).getMessage());

    source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { x = foo(); } "
        + "function qux() { return x; } "
        + "function quuz() { return x * 2; }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The second call to foo() in function 'baz' is not side-effect free,
    // as it assigns the value returned by foo() to 'x'.
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getErrorMessages().get(0).getMessage());

    // Test for a case with an empty statement
    source = "function foo() { return 42; } "
        + "function bar() { } "
        + "function baz() { }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // Empty statements are not considered as side-effects,
    // so no warning should be generated.
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    source = "function foo() { return 42; } "
        + "function bar() { } "
        + "function baz() { ; }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // Empty statements are not considered as side-effects,
    // so no warning should be generated.
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    String source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { x = foo(); } "
        + "function qux() { return x; }";

    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The second call to foo() in function 'baz' is not side-effect free,
    // but protecting side-effect free code will prevent its removal.
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getErrorMessages().get(0).getMessage());

    source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { x = foo(); } "
        + "function qux() { return x; } "
        + "function quuz() { return x * 2; }";

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The second call to foo() in function 'baz' is not side-effect free,
    // but protecting side-effect free code will prevent its removal.
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getErrorMessages().get(0).getMessage());
  }

  @Test
  public void testStripProtection() {
    String source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); } "
        + "function baz() { JSCOMPILER_PRESERVE(x); } "
        + "function qux() { return x; }";

    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    StripProtection stripProtection = new StripProtection(compiler);

    Node ast = Parser.parse(source);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);

    // The call to JSCOMPILER_PRESERVE in function 'baz' should be removed.
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals("JSC_USELESS_CODE: Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getErrorMessages().get(0).getMessage());

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    compiler.process(null, ast);
    NodeTraversal.traverse(compiler, ast, stripProtection);
    compiler.process(null, ast);

    // After stripping protection, the call to JSCOMPILER_PRESERVE in function 'baz' should be removed.
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}