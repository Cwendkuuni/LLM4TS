import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(QUIET, WarningLevel.VERBOSE);
    AbstractCompiler compiler = new Compiler(options);

    // Test case for a simple expression without side-effects
    String source = "function foo() { return 42; }";
    CompilerInput input = new TestSourceFile("Test.java", source);
    Node ast = compiler.process(input, null).getTree();
    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a statement without side-effects
    source = "function foo() { }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a statement with side-effects
    source = "function foo() { document.write('Hello, World!'); }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a statement with side-effects in a loop
    source = "function foo() { for (var i = 0; i < 10; i++) { document.write('Number: ' + i); } }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(10, checker.problemNodes.size());

    // Test case for a statement with side-effects in an if statement
    source = "function foo() { if (true) { document.write('This is only printed if true'); } }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a statement with side-effects in a switch statement
    source = "function foo() { switch (1) { case 1: document.write('One'); break; } }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a statement with side-effects in an expression
    source = "function foo() { return document.write('Fourty-Two'); }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a statement with a function call without side-effects
    source = "function foo() { return bar(); } function bar() { return 42; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a statement with a function call with side-effects
    source = "function foo() { document.write('Hello, World!'); return bar(); } function bar() { return 42; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a statement with a simple operator without side-effects
    source = "function foo() { return 2 + 2; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a statement with a simple operator with side-effects
    source = "function foo() { document.write('Two plus two is '); return 2 + 2; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a block with no side-effects
    source = "function foo() { }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a block with side-effects
    source = "function foo() { document.write('Hello, World!'); }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a block with a statement without side-effects
    source = "function foo() { var x = 42; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a block with a statement with side-effects
    source = "function foo() { document.write('Hello, World!'); }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a block with a return statement without side-effects
    source = "function foo() { return 42; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a block with a return statement with side-effects
    source = "function foo() { document.write('Hello, World!'); return 42; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    // Test case for a void statement
    source = "function foo() { ; }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a void statement in a loop
    source = "function foo() { for (var i = 0; i < 10; i++) { ; } }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a void statement in an if statement
    source = "function foo() { if (true) { ; } }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    // Test case for a void statement in a switch statement
    source = "function foo() { switch (1) { case 1: ; break; } }";
    input = new TestSourceFile("Test.java", source);
    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());
  }

  @Test
  public void testStripProtection() {
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(QUIET, WarningLevel.VERBOSE);
    AbstractCompiler compiler = new Compiler(options);

    String source = "function foo() { JSCOMPILER_PRESERVE(document.write('Hello, World!')); }";
    CompilerInput input = new TestSourceFile("Test.java", source);
    Node ast = compiler.process(input, null).getTree();
    CheckSideEffects checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertFalse("Warning expected", checker.problemNodes.isEmpty());
    assertEquals(1, checker.problemNodes.size());

    StripProtection protector = new StripProtection(compiler);
    protector.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());

    ast = compiler.process(input, null).getTree();
    checker = new CheckSideEffects(compiler, WARNING, false);
    checker.process(input.getAstRoot(compiler), ast);
    assertTrue("No warnings expected", checker.problemNodes.isEmpty());
  }
}