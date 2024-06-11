package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = NodeTraversal.create(null, null);
    Compiler compiler = new Compiler();
    nodeTraversal.setCompiler(compiler);
    Node rootNode = new Node(Token.PROGRAM);
    makeDeclaredNamesUnique.process(rootNode, null);
    nodeTraversal.traverse(compiler, rootNode, null);

    // Test simple cases
    String originalName1 = "a";
    String newName1 = "a";
    assertEquals(newName1, NodeUtil.getName(rootNode, originalName1));

    String originalName2 = "b";
    String newName2 = "b$$0";
    assertEquals(newName2, NodeUtil.getName(rootNode, originalName2));

    String originalName3 = "c";
    String newName3 = "c$$1";
    assertEquals(newName3, NodeUtil.getName(rootNode, originalName3));

    String originalName4 = "d";
    String newName4 = "d$$2";
    assertEquals(newName4, NodeUtil.getName(rootNode, originalName4));

    // Test cases with multiple declarations of the same name
    String originalName5 = "e";
    String newName5 = "e$$3";
    assertEquals(newName5, NodeUtil.getName(rootNode, originalName5));

    String originalName6 = "e";
    String newName6 = "e$$4";
    assertEquals(newName6, NodeUtil.getName(rootNode, originalName6));

    // Test reused name after renaming
    String originalName7 = "f";
    String newName7 = "f$$5";
    assertEquals(newName7, NodeUtil.getName(rootNode, originalName7));

    String originalName8 = "f";
    String newName8 = "a"; // Should be renamed to a$$6
    assertEquals(newName8, NodeUtil.getName(rootNode, originalName8));

    // Test reused name with identical original name
    String originalName9 = "g";
    String newName9 = "g$$7";
    assertEquals(newName9, NodeUtil.getName(rootNode, originalName9));

    String originalName10 = "g";
    String newName10 = "g$$8";
    assertEquals(newName10, NodeUtil.getName(rootNode, originalName10));

    // Test renaming of global variables
    String originalName11 = "h";
    String newName11 = "h"; // Should remain unchanged
    assertEquals(newName11, NodeUtil.getName(rootNode, originalName11));

    String originalName12 = "i";
    String newName12 = "i$$9";
    assertEquals(newName12, NodeUtil.getName(rootNode, originalName12));

    // Test renaming of global variables with identical name
    String originalName13 = "j";
    String newName13 = "j$$10";
    assertEquals(newName13, NodeUtil.getName(rootNode, originalName13));

    String originalName14 = "j";
    String newName14 = "j$$11";
    assertEquals(newName14, NodeUtil.getName(rootNode, originalName14));
  }

  @Test
  public void testInlineRenamer() {
    String prefix = "prefix";
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(() -> prefix + "_");
    NodeTraversal nodeTraversal = NodeTraversal.create(null, null);
    Compiler compiler = new Compiler();
    nodeTraversal.setCompiler(compiler);
    Node rootNode = new Node(Token.PROGRAM);
    makeDeclaredNamesUnique.process(rootNode, null);
    nodeTraversal.traverse(compiler, rootNode, null);

    // Test simple cases
    String originalName1 = "a";
    String newName1 = "a" + prefix + "_0";
    assertEquals(newName1, NodeUtil.getName(rootNode, originalName1));

    String originalName2 = "b";
    String newName2 = "b" + prefix + "_1";
    assertEquals(newName2, NodeUtil.getName(rootNode, originalName2));

    String originalName3 = "c";
    String newName3 = "c" + prefix + "_2";
    assertEquals(newName3, NodeUtil.getName(rootNode, originalName3));

    String originalName4 = "d";
    String newName4 = "d" + prefix + "_3";
    assertEquals(newName4, NodeUtil.getName(rootNode, originalName4));

    // Test cases
    // with multiple declarations of the same name
    String originalName5 = "e";
    String newName5 = "e" + prefix + "_4";
    assertEquals(newName5, NodeUtil.getName(rootNode, originalName5));

    String originalName6 = "e";
    String newName6 = "e" + prefix + "_5";
    assertEquals(newName6, NodeUtil.getName(rootNode, originalName6));

    // Test reused name after renaming
    String originalName7 = "f";
    String newName7 = "f" + prefix + "_6";
    assertEquals(newName7, NodeUtil.getName(rootNode, originalName7));

    String originalName8 = "f";
    String newName8 = "a"; // Should be renamed to a + prefix + "_6"
    assertEquals(newName8, NodeUtil.getName(rootNode, originalName8));

    // Test reused name with identical original name
    String originalName9 = "g";
    String newName9 = "g" + prefix + "_7";
    assertEquals(newName9, NodeUtil.getName(rootNode, originalName9));

    String originalName10 = "g";
    String newName10 = "g" + prefix + "_8";
    assertEquals(newName10, NodeUtil.getName(rootNode, originalName10));

    // Test renaming of global variables
    String originalName11 = "h";
    String newName11 = "h"; // Should remain unchanged
    assertEquals(newName11, NodeUtil.getName(rootNode, originalName11));

    String originalName12 = "i";
    String newName12 = "i" + prefix + "_9";
    assertEquals(newName12, NodeUtil.getName(rootNode, originalName12));

    // Test renaming of global variables with identical name
    String originalName13 = "j";
    String newName13 = "j" + prefix + "_10";
    assertEquals(newName13, NodeUtil.getName(rootNode, originalName13));

    String originalName14 = "j";
    String newName14 = "j" + prefix + "_11";
    assertEquals(newName14, NodeUtil.getName(rootNode, originalName14));
  }

  @Test
  public void testContextualRenamerWithFunctionDeclarations() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = NodeTraversal.create(null, null);
    Compiler compiler = new Compiler();
    nodeTraversal.setCompiler(compiler);
    Node rootNode = new Node(Token.PROGRAM);
    rootNode.setString("function foo() {}");
    rootNode.addChild(rootNode.getFirstChild());
    rootNode.addChild(new Node(Token.FUNCTION, "bar", "()", null, null));
    rootNode.addChild(new Node(Token.BLOCK));
    rootNode.lastChild().addChild(new Node(Token.VAR, null, "b", null, null));
    rootNode.addChild(new Node(Token.FUNCTION, "baz", "(x)", null, null));
    rootNode.lastChild().addChild(new Node(Token.BLOCK, null, null));
    rootNode.lastChild().lastChild().addChild(new Node(Token.VAR, null, "x", null, null));
    rootNode.lastChild().lastChild().addChild(new Node(Token.RETURN, null, null, null, null));
    rootNode.lastChild().lastChild().addChild(new Node(Token.NAME, null, "x", null, null));
    rootNode.lastChild().lastChild().addChild(Node.createToken(Token.SEMICOLON));
    makeDeclaredNamesUnique.process(rootNode, null);
    nodeTraversal.traverse(compiler, rootNode, null);

    // Test simple cases
    String originalName1 = "a";
    String newName1 = "a";
    assertEquals(newName1, NodeUtil.getName(rootNode, originalName1));

    String originalName2 = "b";
    String newName2 = "b$$0";
    assertEquals(newName2, NodeUtil.getName(rootNode, originalName2));

    String originalName3 = "c";
    String newName3 = "c$$1";
    assertEquals(newName3, NodeUtil.getName(rootNode, originalName3));

    String originalName4 = "d";
    String newName4 = "d$$2";
    assertEquals(newName4, NodeUtil.getName(rootNode, originalName4));

    // Test case with function declarations
    String originalName5 = "e";
    String newName5 = "e$$3";
    assertEquals(newName5, NodeUtil.getName(rootNode, originalName5));

    String originalName6 = "e";
    String newName6 = "e$$4";
    assertEquals(newName6, NodeUtil.getName(rootNode, originalName6));

    String originalName7 = "f";
    String newName7 = "f$$5";
    assertEquals(newName7, NodeUtil.getName(rootNode, originalName7));

    String originalName8 = "f";
    String newName8 = "bar";
    assertEquals(newName8, NodeUtil.getName(rootNode, originalName8));

    String originalName9 = "g";
    String newName9 = "g$$6";
    assertEquals(newName9, NodeUtil.getName(rootNode, originalName9));

    String originalName10 = "g";
    String newName10 = "baz";
    assertEquals(newName10, NodeUtil.getName(rootNode, originalName10));

    String originalName11 = "h";
    String newName11 = "h"; // Should remain unchanged
    assertEquals(newName11, NodeUtil.getName(rootNode, originalName11));

    String originalName12 = "i";
    String newName12 = "i$$7";
    assertEquals(newName12, NodeUtil.getName(rootNode, originalName12));

    String originalName13 = "j";
    String newName13 = "j$$8";
    assertEquals(newName13, NodeUtil.getName(rootNode, originalName13));

    String originalName14 = "k";
    String newName14 = "k$$9";
    assertEquals(newName14, NodeUtil.getName(rootNode, originalName14));

    String originalName15 = "k";
    String newName15 = "x"; // Should be renamed to x$$10
    assertEquals(newName15, NodeUtil.getName(rootNode, originalName15));
  }

  private static NodeUtil NodeUtil = new NodeUtil();

}