package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CompilerPass.Stage.TOP_LEVEL;
import static com.google.javascript.rhino.NodeTree.isFunction;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Serialization;
import com.google.javascript.rhino.SourceFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private static final String SOURCE_CODE =
      "var Foo = function() {}\n"
      + "Foo.prototype.bar = function() {}\n"
      + "var A = function() {}\n"
      + "A.prototype.baz = function() {}\n"
      + "A.prototype.bar = Foo.prototype.bar;\n"
      + "var B = function() {}\n"
      + "B.prototype.qux = function() {}\n"
      + "B.prototype.bar = A.prototype.bar;\n";

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5_STRICT);
    compiler = new Compiler(options);
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
  }

  @After
  public void tearDown() {
    compiler = null;
  }

  @Test
  public void testCrossModuleMethodMotion() throws IOException {
    SourceFile input = Parser.parseJavaScriptSource(SOURCE_CODE, "test.js");
    Node root = compiler.parseTree(input, null, null);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    ErrorCollector errorCollector = new ErrorCollector();

    NodeTraversal.traverse(root, pass);
    List<Node> ast = Serialization.serializeToBinaryTree(root);
    Node deserialized = compiler.parseTree(
        new SourceFile("test.js", new String(Serialization.toBase64String(ast))),
        null, null);

    assertFalse(errorCollector.hasErrors());
    assertNotNull(idGenerator.getIds());
    assertNotNull(idGenerator.getIds().get(0));
    assertNotNull(idGenerator.getIds().get(1));

    Iterator<Node> functionTraversal = NodeTraversal.depthFirst(deserialized);
    List<Node> functions = new ArrayList<>();
    while (functionTraversal.hasNext()) {
      Node node = functionTraversal.next();
      if (isFunction(node)) {
        functions.add(node);
      }
    }

    Node barA = null;
    Node barB = null;
    for (Node function : functions) {
      if (function.getParent().getClassName().equals("getprop")) {
        Node propName = function.getFirstChild();
        if (propName.isString() && propName.getString().equals("bar")) {
          if (function.getParent().getParent().getClassName().equals("A")) {
            barA = function;
          } else {
            barB = function;
          }
          break;
        }
      }
    }

    assertNotNull(barA);
    assertNotNull(barB);

    Iterator<Node> children = barA.getChildren().iterator();
    Node callExpression = children.next();
    Node idExpression = callExpression.getFirstChild();
    Node numberLiteral = idExpression.getFirstChild();
    int id = Integer.parseInt(numberLiteral.getString());

    children = barB.getChildren().iterator();
    callExpression = children.next();
    idExpression = callExpression.getFirstChild();
    assertIterationOrder(idGenerator.getIds(), id);

    Node unstubbedFunction = null;
    for (Node node : deserialized.getDescendants()) {
      if (node.getClassName().equals("call")
          && node.getFirstChild().isName()
          && node.getFirstChild().getString().equals(UNSTUB_METHOD_NAME)) {
        Node arguments = node.getLastChild();
        Node unstubbedFunctionExpression = arguments.getFirstChild();
        unstubbedFunction = unstubbedFunctionExpression.getLastChild();
        break;
      }
    }

    assertNotNull(unstubbedFunction);

    Iterator<Node> unstubbedFunctionChildren = unstubbedFunction.getChildren()
        .iterator();
    Node functionExpression = unstubbedFunctionChildren.next();
    Node body = functionExpression.getLastChild();
    assertIterationOrder(idGenerator.getIds(), Iterators.getFirst(body.getChildren())
        .getFirstChild()
        .getNumber()
        .getIntValue());

    assertEquals(STUB_METHOD_NAME, functionExpression.getClassName());
  }

  @Test
  public void testCrossModuleMethodMotionError() throws IOException {
    SourceFile input = Parser.parseJavaScriptSource(
        "var Foo = function() {}\n"
        + "Foo.prototype.bar = function() {}\n"
        + "var A = function() {}\n"
        + "A.prototype.baz = function() {}\n"
        + "A.prototype.bar = Foo.prototype.bar;\n"
        + "var B = function() {}\n"
        + "B.prototype.bar = A.prototype.bar;\n"
        + "B.prototype.qux = function() {}\n", "test.js");
    Node root = compiler.parseTree(input, null, null);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    ErrorCollector errorCollector = new ErrorCollector();

    NodeTraversal.traverse(root, pass);

    List<Compiler.Diagnostic> errors = errorCollector.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(
        "JSC_INTERNAL_ERROR_MODULE_DEPEND: null deepest common module",
        errors.get(0).getMessage());
  }

  @Test
  public void testCrossModuleMethodMotionNoMove() throws IOException {
    SourceFile input = Parser.parseJavaScriptSource(
        "var Foo = function() {}\n"
        + "Foo.prototype.bar = function() {}\n"
        + "var A = function() {}\n"
        + "A.prototype.baz = function() {}\n"
        + "A.prototype.bar = Foo.prototype.bar;\n"
        + "var B = function() {}\n"
        + "B.prototype.bar = A.prototype.bar;\n"
        + "B.prototype.qux = function() {}\n"
        + "B.prototype.bar = Foo.prototype.bar;\n", "test.js");
    Node root = compiler.parseTree(input, null, null);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    ErrorCollector errorCollector = new ErrorCollector();

    NodeTraversal.traverse(root, pass);

    List<Compiler.Diagnostic> errors = errorCollector.getDiagnostics();
    assertTrue(errors.isEmpty());

    Iterator<Node> functionTraversal = NodeTraversal.depthFirst(root);
    List<Node> functions = new ArrayList<>();
    while (functionTraversal.hasNext()) {
      Node node = functionTraversal.next();
      if (isFunction(node)) {
        functions.add(node);
      }
    }

    Node barA = null;
    Node barB = null;
    for (Node function : functions) {
      if (function.getParent().getClassName().equals("getprop")) {
        Node propName = function.getFirstChild();
        if (propName.isString() && propName.getString().equals("bar")) {
          if (function.getParent().getParent().getClassName().equals("A")) {
            barA = function;
          } else {
            barB = function;
          }
          break;
        }
      }
    }

    assertNotNull(barA);
    assertNotNull(barB);

    Iterator<Node> children = barA.getChildren().iterator();
    Node callExpression = children.next();
    Node idExpression = callExpression.getFirstChild();
    Node name = idExpression.getFirstChild();
    assertFalse(name.isString());
    assertFalse(name.isName());
    assertFalse(name.isGetterDef());

    children = barB.getChildren().iterator();
    callExpression = children.next();
    idExpression = callExpression.getFirstChild();
    name = idExpression.getFirstChild();
    assertFalse(name.isString());
    assertFalse(name.isName());
    assertFalse(name.isGetterDef());
  }

  @Test
  public void testCrossModuleMethodMotionNoMoveWithExterns() throws IOException {
    SourceFile input = Parser.parseJavaScriptSource(
        "/** @extern {function() {}} */\n"
        + "var Foo = function() {}\n"
        + "Foo.prototype.bar = function() {}\n"
        + "var A = function() {}\n"
        + "A.prototype.baz = function() {}\n"
        + "A.prototype.bar = Foo.prototype.bar;\n"
        + "var B = function() {}\n"
        + "B.prototype.bar = A.prototype.bar;\n"
        + "B.prototype.qux = function() {}\n"
        + "B.prototype.bar = Foo.prototype.bar;\n", "test.js");
    Node root = compiler.parseTree(input, null, null);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    ErrorCollector errorCollector = new ErrorCollector();

    NodeTraversal.traverse(root, pass);

    List<Compiler.Diagnostic> errors = errorCollector.getDiagnostics();
    assertTrue(errors.isEmpty());

    Iterator<Node> functionTraversal = NodeTraversal.depthFirst(root);
    List<Node> functions = new ArrayList<>();
    while (functionTraversal.hasNext()) {
      Node node = functionTraversal.next();
      if (isFunction(node)) {
        functions.add(node);
      }
    }

    Node barA = null;
    Node barB = null;
    for (Node function : functions) {
      if (function.getParent().getClassName().equals("getprop")) {
        Node propName = function.getFirstChild();
        if (propName.isString() && propName.getString().equals("bar")) {
          if (function.getParent().getParent().getClassName().equals("A")) {
            barA = function;
          } else {
            barB = function;
          }
          break;
        }
      }
    }

    assertNotNull(barA);
    assertNotNull(barB);

    Iterator<Node> children = barA.getChildren().iterator();
    Node callExpression = children.next();
    Node idExpression = callExpression.getFirstChild();
    Node name = idExpression.getFirstChild();
    assertFalse(name.isString());
    assertFalse(name.isName());
    assertFalse(name.isGetterDef());

    children = barB.getChildren().iterator();
    callExpression = children.next();
    idExpression = callExpression.getFirstChild();
    name = idExpression.getFirstChild();
    assertFalse(name.isString());
    assertFalse(name.isName());
    assertFalse(name.isGetterDef());
  }

  private void assertIterationOrder(List<Integer> ids, int id) {
    Iterator<Integer> it = ids.iterator();
    Integer expected = it.next();
    while (it.hasNext()) {
      Integer next = it.next();
      if (next.intValue() > id) {
        fail("Expected id to be before: " + next);
      } else if (next.intValue() == id) {
        it.remove();
        break;
      }
    }
    assertEquals(ids.size(), 2);
  }

  private void assertEquals(String expected, Node node) {
    assertEquals(expected, node.toString());
  }
}