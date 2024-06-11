package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  private InlineObjectLiterals pass;
  private Compiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = new Compiler();
    safeNameIdSupplier = () -> "test_id_";
    pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testInlineSimpleObject() {
    String code = "var foo = {'a': 1, 'b': 2};";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      Node node = var.getInitialValue();
      assertTrue(node.getType() == Token.VAR);
      assertEquals("test_a_", node.getFirstChild().getString());
      assertEquals("test_b_", node.getLastChild().getString());
    }
  }

  @Test
  public void testInlineComplexObject() {
    String code = "var foo = {'a': {'b': 1, 'c': {'d': 1}}};";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      Node node = var.getInitialValue();
      assertTrue(node.getType() == Token.VAR);
      Node child = node.getFirstChild();
      assertEquals("test_b_", child.getFirstChild().getString());
      assertEquals("test_c_", child.getLastChild().getFirstChild().getString());
      Node grandChild = child.getLastChild().getLastChild();
      assertEquals("test_d_", grandChild.getString());
      it.next(); // Skip 'foo'
    }
  }

  @Test
  public void testNoInline() {
    String code = "var foo = {'a': 1}; foo.a = 2;";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertEquals("{'a': 1}", node.getString());
    }
  }

  @Test
  public void testNoInlineWithFunctionCall() {
    String code = "function foo() { var obj = {'a': 1}; obj.a = 2; }";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertEquals("function foo() { var obj = {'a': 1}; }", node.getString());
    }
  }

  @Test
  public void testNoInlineWithVariableReference() {
    String code = "var foo = {'a': 1}; var bar = foo.a;";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertEquals("{'a': 1}", node.getString());
      it.next(); // Skip 'bar'
    }
  }

  @Test
  public void testNoInlineWithAssignment() {
    String code = "var foo = {'a': 1}; foo.a = 2;";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertEquals("{'a': 1}", node.getString());
    }
  }

  @Test
  public void testInlineWithSelfReference() {
    String code = "var foo = {'a': function() { return foo.a; }};";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertTrue(node.getType() == Token.VAR);
      List<Node> children = Lists.newArrayList(node.getFirstChild());
      assertEquals("test_a_", children.get(0).getString());
      Node body = children.get(1);
      assertEquals(1, body.getNumChildren());
      Node function = body.getFirstChild();
      assertEquals("function test_a__() { return test_a__a; }", function.getString());
    }
  }

  @Test
  public void testNoInlineWithExportedVariable() {
    String code = "export var foo = {'a': 1};";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertEquals("export var foo = {'a': 1};", node.getString());
    }
  }

  @Test
  public void testNoInlineWithExternVariable() {
    String code = "var foo = exports.bar = {'a': 1};";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("foo"));
      Node node = var.getInitialValue();
      assertEquals("var foo = exports.bar = {'a': 1};", node.getString());
    }
  }

  @Test
  public void testInlineWithCycle() {
    String code = "var a = {'b': {'a': a}};";
    Node root = NodePrinter.printJs(code);

    Scope scope = new Scope(null);
    scope.setParent(null);
    scope.setRootNode(root);

    pass.process(NodePrinter.printJs(code), root);

    Iterator<Scope.Var> it = scope.getVars().iterator();
    while (it.hasNext()) {
      Scope.Var var = it.next();
      assertTrue(var.getName().equals("a"));
      Node node = var.getInitialValue();
      assertFalse(node.getType() == Token.VAR);
      List<Node> children = Lists.newArrayList(node.getFirstChild());
      assertEquals("test_b_", children.get(0).getString());
      children = Lists.newArrayList(children.get(0).getFirstChild());
      assertEquals("test_a_", children.get(0).getString());
      it.next(); // Skip 'a'
    }
  }
}