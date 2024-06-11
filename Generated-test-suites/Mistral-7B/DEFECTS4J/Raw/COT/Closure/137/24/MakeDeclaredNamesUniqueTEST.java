package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique_EmptyCode_NoChanges() {
    String code = "// Empty code";
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
    assertEquals(code, rootNode.toSource());
  }

  @Test
  public void testMakeDeclaredNamesUnique_SingleVariableDeclaration_UniqueName() {
    String code = "var x = 1;";
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
    String expectedCode = "var x_0 = 1;"; // "_0" is added as a suffix to make the name unique
    assertEquals(expectedCode, rootNode.toSource());
  }

  @Test
  public void testMakeDeclaredNamesUnique_MultipleVariableDeclarations_UniqueNames() {
    String code = "var x = 1, y = 2, x = 3;";
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
    String expectedCode = "var x_0 = 1, y_1 = 2, x_2 = 3;"; // "_0", "_1", "_2" are added as suffixes to make the names unique
    assertEquals(expectedCode, rootNode.toSource());
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionDeclaration_UniqueNames() {
    String code = "function foo(x, y) {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  var x = 3;\n" +
        "}";
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
    String expectedCode = "function foo(x_0, y_1) {\n" +
        "  var x_2 = 1;\n" +
        "  var y_3 = 2;\n" +
        "  var x_4 = 3;\n" +
        "}"; // "_0", "_1", "_2", "_3", "_4" are added as suffixes to make the names unique
    assertEquals(expectedCode, rootNode.toSource());
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionCall_NoChanges() {
    String code = "function foo() {\n" +
        "  bar();\n" +
        "}" +
        "function bar() {}";
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
    assertEquals(code, rootNode.toSource());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMakeDeclaredNamesUnique_NullCode_IllegalArgumentException() {
    String code = null;
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
  }

  @Test
  public void testMakeDeclaredNamesUnique_GlobalVariableDeclaration_UniqueName() {
    String code = "var x = 1;";
    Node rootNode = new TokenStream(code.toCharArray(), 0, code.length()).getRootNode();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.builder(null).build(), rootNode, null);
    String expectedCode = "var x_0 = 1;"; // "_0" is added as a suffix to make the name unique for global scope
    assertEquals(expectedCode, rootNode.toSource());
  }
}