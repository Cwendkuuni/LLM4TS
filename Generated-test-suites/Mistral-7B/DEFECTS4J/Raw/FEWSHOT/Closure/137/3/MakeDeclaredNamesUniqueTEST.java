package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Renamer;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.NodeTestingUtil;

import java.util.Map;
import java.util.Set;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    String js = "function foo(x) {\n" +
        "  var y = x;\n" +
        "  function bar(z) {\n" +
        "    var x = z;\n" +
        "  }\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo", "x", "y", "bar", "z"));
  }

  @Test
  public void testInlineRenamer() {
    String js = "function foo(x) {\n" +
        "  var y = x;\n" +
        "  function bar(z) {\n" +
        "    var x = z;\n" +
        "  }\n" +
        "}";

    String idPrefix = "_tmp_";
    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique(new InlineRenamer(() -> idPrefix + "_", idPrefix, true)));
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_" + idPrefix + "_0", "x_" + idPrefix + "_1", "y_" + idPrefix + "_2", "bar_" + idPrefix + "_3", "z_" + idPrefix + "_4"));
  }

  @Test
  public void testContextualAndInlineRenamer() {
    String js = "function foo(x) {\n" +
        "  var y = x;\n" +
        "  function bar(z) {\n" +
        "    var x = z;\n" +
        "  }\n" +
        "  var baz = x;\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique(new ContextualRenamer(), new InlineRenamer(() -> "_", "", true)));
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo", "x_", "y_", "bar_", "z_", "baz_"));
  }

  @Test
  public void testGlobalRenamer() {
    String js = "var x = 1;\n" +
        "function foo(x) {\n" +
        "  var y = x;\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("x_", "foo_0"));
  }

  @Test
  public void testFunctionDeclaration() {
    String js = "function foo(x) {\n" +
        "  var y = x;\n" +
        "}\n" +
        "function bar(z) {\n" +
        "  var x = z;\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_", "y_", "bar_1", "z_"));
  }

  @Test
  public void testFunctionCall() {
    String js = "function foo(x) {\n" +
        "  var y = x;\n" +
        "  bar(x);\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_", "y_", "bar_2"));
  }

  @Test
  public void testBlockDeclaration() {
    String js = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_", "y_"));
  }

  @Test
  public void testBlockDeclarationAndFunctionCall() {
    String js = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  bar();\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_", "y_", "bar_3"));
  }

  @Test
  public void testBlockDeclarationAndFunctionDeclaration() {
    String js = "function foo() {\n" +
        "  var x = 1;\n" +
        "  function bar() {\n" +
        "  }\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_", "bar_1"));
  }

  @Test
  public void testBlockDeclarationAndBlockDeclaration() {
    String js = "function foo() {\n" +
        "  var x = 1;\n" +
        "  {\n" +
        "    var y = 2;\n" +
        "  }\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_"));
  }

  @Test
  public void testBlockDeclarationAndBlockDeclarationWithFunctionCall() {
    String js = "function foo() {\n" +
        "  var x = 1;\n" +
        "  {\n" +
        "    var y = 2;\n" +
        "    bar();\n" +
        "  }\n" +
        "}";

    Node root = NodeTestingUtil.createNodeFromString(js);
    NodeTraversal traverser = new NodeTraversal(root, new MakeDeclaredNamesUnique());
    traverser.traverse();

    checkDeclaredNamesUnique(root, ImmutableSet.of("foo_0", "x_", "bar_4"));
  }

  private void checkDeclaredNamesUnique(Node root, Set<String> expectedDeclaredNames) {
    Scope scope = root.getScope();
    Map<String, Node> names = scope.getAllDeclaredNames();

    for (Map.Entry<String, Node> entry : names.entrySet()) {
      String name = entry.getKey();
      assertTrue(expectedDeclaredNames.contains(name), "Unexpected declared name: " + name);
    }

    for (Node node : NodeUtil.getDeclaredNames(root)) {
      String name = node.getString();
      assertTrue(expectedDeclaredNames.contains(name), "Missing declared name: " + name);
    }

    for (Node node : root.getChildren()) {
      if (node.getType() == Token.FUNCTION || node.getType() == Token.VAR) {
        checkDeclaredNamesUnique(node, expectedDeclaredNames);
      }
    }
  }
}