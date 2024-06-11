package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testProcess_EmptyProgram() {
    String sourceCode = "";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithParameters() {
    String sourceCode = "function foo(x: number, y: string) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithReturnType() {
    String sourceCode = "function foo(): string {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_ClassWithConstructor() {
    String sourceCode = "class Foo {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_ClassWithInterfaceImplementation() {
    String sourceCode = "class Foo implements Bar {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionCallWithChecks() {
    String sourceCode = "function foo(x: number, y: string) {" +
        "  checkType(x, [valueChecker('number')]);" +
        "  checkType(y, [valueChecker('string')]);" +
        "}" +
        "foo(1, 'hello');";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithUnionTypeParameter() {
    String sourceCode = "function foo(x: number | string) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithExternClassParameter() {
    String sourceCode = "function foo(x: com.example.Foo) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithNullTypeParameter() {
    String sourceCode = "function foo(x: null) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithBooleanTypeParameter() {
    String sourceCode = "function foo(x: boolean) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithNumberTypeParameter() {
    String sourceCode = "function foo(x: number) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithStringTypeParameter() {
    String sourceCode = "function foo(x: string) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithVoidTypeParameter() {
    String sourceCode = "function foo(x: void) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_FunctionWithInstanceTypeParameter() {
    String sourceCode = "function foo(x: com.example.Foo) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test
  public void testProcess_WithLogFunction() {
    String sourceCode = "function foo(x: number, y: string) {" +
        "  checkType(x, [valueChecker('number')]);" +
        "  checkType(y, [valueChecker('string')]);" +
        "  log('checkType called for number: ' + x + ', string: ' + y);" +
        "}" +
        "foo(1, 'hello');";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, "println")
        .process(null, root);
    assertTrue(compiler.getErrorCount() == 0);
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_WithInvalidLogFunction() {
    String sourceCode = "function foo(x: number, y: string) {" +
        "  checkType(x, [valueChecker('number')]);" +
        "  checkType(y, [valueChecker('string')]);" +
        "  log('checkType called for number: ' + x + ', string: ' + y);" +
        "}" +
        "foo(1, 'hello');";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, "invalidLogFunction")
        .process(null, root);
  }

  @Test
  public void testAddBoilerplateCode() throws IOException {
    String expectedBoilerplateCode = Resources.readString(
        RuntimeTypeCheckTest.class.getResource("/js/runtime_type_check_test.js"),
        Charsets.UTF_8);

    String sourceCode = "function foo(x: number, y: string) {}";
    Node root = NodeUtil.parseJavascript(sourceCode, null);
    new RuntimeTypeCheck(compiler, null).process(null, root);
    Node boilerplateCode = compiler.getNodeForCodeInsertion(null);

    assertTrue(Arrays.equals(
        NodeUtil.parseJavascript(expectedBoilerplateCode, null).getChildren(),
        boilerplateCode.getChildren()));
  }
}