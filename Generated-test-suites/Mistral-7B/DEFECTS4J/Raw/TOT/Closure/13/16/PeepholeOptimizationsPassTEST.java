package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.jscomp.test.CompilerTestCase;

public class PeepholeOptimizationsPassTest extends CompilerTestCase {
  private Compiler compiler;
  private CompilerPass peepholePass;

  @Before
  public void setUp() {
    compiler = newCompiler();
    peepholePass = new PeepholeOptimizationsPass(compiler,
        new ConstantFolding(), new DeadCodeElimination());
  }

  @Test
  public void testEmptyProgram() {
    Node root = emptySource("");
    Node optimized = peepholePass.process(null, root).getRootNode();
    assertNotNull(optimized);
    assertFalse(optimized.isScript());
    assertFalse(optimized.isFunction());
    assertNull(optimized.getFirstChild());
  }

  @Test
  public void testConstantFolding() {
    Node root = constantSource("3 + 3;");
    Node optimized = peepholePass.process(null, root).getRootNode();
    assertNotNull(optimized);
    assertEquals(Int.intValue(6), optimized.getFirstChild().getInt());
  }

  @Test
  public void testDeadCodeElimination() {
    Node root = deadCodeSource("if (true) { return; } 3 + 3;");
    Node optimized = peepholePass.process(null, root).getRootNode();
    assertNotNull(optimized);
    assertEquals(Int.intValue(3), optimized.getFirstChild().getInt());
  }

  @Test
  public void testNestedFunction() {
    Node root = nestedFunctionSource("function foo() { function bar() { return 1 + 1; } return bar(); }");
    Node optimized = peepholePass.process(null, root).getRootNode();
    assertNotNull(optimized);
    assertEquals(Int.intValue(2), optimized.getFirstChild().getInt());
  }

  @Test
  public void testErrorHandling() {
    Node root = errorHandlingSource("function foo() { return 1 / 0; }");
    Node optimized = null;
    try {
      optimized = peepholePass.process(null, root).getRootNode();
    } catch (RuntimeException e) {
      // expected
    }
    assertNull(optimized);
  }

  @Test
  public void testRecursiveFunction() {
    Node root = recursiveFunctionSource("function foo(x) { return foo(x - 1); } function bar() { return foo(3); }");
    Node optimized = peepholePass.process(null, root).getRootNode();
    assertNotNull(optimized);
    assertEquals(Int.intValue(2), optimized.getFirstChild().getInt()); // recursion limit
  }

  private static Node constantSource(String code) {
    return parse(code);
  }

  private static Node deadCodeSource(String code) {
    return parse(code);
  }

  private static Node emptySource(String code) {
    return parse(code);
  }

  private static Node nestedFunctionSource(String code) {
    return parse(code);
  }

  private static Node errorHandlingSource(String code) {
    return parse(code);
  }

  private static Node recursiveFunctionSource(String code) {
    return parse(code);
  }

  private static Node parse(String code) {
    return compiler.compile(new Node(CodeKind.SOURCE_FILE, code), null, null).getRootNode();
  }
}