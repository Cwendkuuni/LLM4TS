package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.ReadOnlyNode;
import com.google.javascript.rhino.JsCompilationError;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPassManager passManager = new CompilerPassManager(options, null);

    String sourceCode = "class MyClass {\n" +
            "  static function foo(this) {\n" +
            "    return this.bar;\n" +
            "  }\n" +
            "  function bar() {\n" +
            "    return 42;\n" +
            "  }\n" +
            "}";

    TestSourceFile testSourceFile = new TestSourceFile("Test.js", sourceCode);
    Node root = testSourceFile.getRootNode();

    passManager.addPass(new DevirtualizePrototypeMethods(compiler));
    List<JsCompilationError> errors = passManager.runPasses(Collections.singletonList(root), ImmutableList.of());

    assertEquals(0, errors.size());
    Node rewrittenRoot = passManager.getRewrittenRoot();

    assertEquals(5, rewrittenRoot.getChildCount());
    Node functionNode = rewrittenRoot.getChild(3);
    assertEquals(6, functionNode.getChildCount());

    Node newNameNode = functionNode.getChild(0);
    assertEquals("JSCompiler_StaticMethods_foo", newNameNode.getString());

    Node argList = functionNode.getChild(1);
    assertEquals("this", argList.getChild(0).getString());

    Node body = functionNode.getChild(5);
    assertEquals("return this.bar;", body.getString());

    // Test rewritten call site
    Node callSite = rewrittenRoot.getChild(1);
    assertEquals(newNameNode, callSite.getChild(0));
    assertEquals(ImmutableList.of("this"), callSite.getArguments());

    // Test rewritten method call in an expression
    Node rewrittenExpression = rewrittenRoot.getChild(3);
    Node expression = rewrittenExpression.getChild(0);
    assertEquals("JSCompiler_StaticMethods_foo.call(MyClass, undefined)", expression.getString());

    // Test incompatible prototype method
    sourceCode = "class MyClass {\n" +
            "  prototype function foo(a) {\n" +
            "    return a;\n" +
            "  }\n" +
            "}";

    testSourceFile = new TestSourceFile("Test.js", sourceCode);
    root = testSourceFile.getRootNode();

    passManager.reset();
    errors = passManager.runPasses(Collections.singletonList(root), ImmutableList.of());

    assertEquals(1, errors.size());
    JsCompilationError error = errors.get(0);
    assertEquals("Error in function 'DevirtualizePrototypeMethods' line 113: " +
            "Prototype method 'foo' of class 'MyClass' is not eligible for rewriting.", error.getMessage());

    // Test undefined prototype method
    sourceCode = "class MyClass {\n" +
            "  prototype function foo() {\n" +
            "  }\n" +
            "}";

    testSourceFile = new TestSourceFile("Test.js", sourceCode);
    root = testSourceFile.getRootNode();

    passManager.reset();
    errors = passManager.runPasses(Collections.singletonList(root), ImmutableList.of());

    assertEquals(1, errors.size());
    error = errors.get(0);
    assertEquals("Error in function 'DevirtualizePrototypeMethods' line 79: " +
            "Prototype method 'foo' of class 'MyClass' is not defined in global scope.", error.getMessage());

    // Test exported prototype method
    sourceCode = "class MyClass {\n" +
            "  export function foo() {\n" +
            "    return 'foo';\n" +
            "  }\n" +
            "}";

    testSourceFile = new TestSourceFile("Test.js", sourceCode);
    root = testSourceFile.getRootNode();

    passManager.reset();
    errors = passManager.runPasses(Collections.singletonList(root), ImmutableList.of());

    assertEquals(1, errors.size());
    error = errors.get(0);
    assertEquals("Error in function 'DevirtualizePrototypeMethods' line 52: " +
            "Prototype method 'foo' of class 'MyClass' is exported.", error.getMessage());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testDevirtualizePrototypeMethodsException() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPassManager passManager = new CompilerPassManager(options, null);

    String sourceCode = "class MyClass {\n" +
            "  function foo() {\n" +
            "    throw new Error('Test error');\n" +
            "  }\n" +
            "}";

    TestSourceFile testSourceFile = new TestSourceFile("Test.js", sourceCode);
    Node root = testSourceFile.getRootNode();

    passManager.addPass(new DevirtualizePrototypeMethods(compiler));
    List<JsCompilationError> errors = passManager.runPasses(Collections.singletonList(root), ImmutableList.of());

    assertEquals(1, errors.size());
    JsCompilationError error = errors.get(0);
    assertEquals("Error in function 'DevirtualizePrototypeMethods' line 121: " +
            "Unable to devirtualize method 'foo' of class 'MyClass'.", error.getMessage());

    try {
      passManager.runPasses(Collections.singletonList(root), ImmutableList.of());
    } catch (RuntimeException e) {
      // expected
    }
  }
}