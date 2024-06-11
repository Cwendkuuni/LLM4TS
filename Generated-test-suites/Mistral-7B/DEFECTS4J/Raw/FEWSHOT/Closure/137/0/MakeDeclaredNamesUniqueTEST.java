package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
  private NodeTraversal nodeTraversal;

  @Before
  public void setup() {
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    nodeTraversal = new NodeTraversal();
  }

  @Test
  public void testEnterScope() {
    // Test entering a scope with no declarations
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique;
    nodeTraversal.setScopedCallback(renamer);
    nodeTraversal.traverse(new Node("root"), null);
    assertNull(renamer.getReplacementName("foo"));

    // Test entering a function scope with declarations
    renamer = new MakeDeclaredNamesUnique(new ContextualRenamer());
    nodeTraversal.setScopedCallback(renamer);
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null);
    Node paramsNode = new Node(Token.LIST, null, null, null, null);
    Node paramNode1 = new Node(Token.NAME, "a", null, null, null);
    Node paramNode2 = new Node(Token.NAME, "b", null, null, null);
    paramsNode.addChild(paramNode1);
    paramsNode.addChild(paramNode2);
    functionNode.addChild(paramsNode);
    Node bodyNode = new Node(Token.BLOCK, null, null, null, null);
    functionNode.addChild(bodyNode);
    nodeTraversal.traverse(functionNode, null);
    assertEquals("a", renamer.getReplacementName("a"));
    assertEquals("b", renamer.getReplacementName("b"));
    assertEquals(null, renamer.getReplacementName("c"));

    // Test entering a function scope with conflicts
    renamer = new MakeDeclaredNamesUnique(new ContextualRenamer());
    nodeTraversal.setScopedCallback(renamer);
    nodeTraversal.traverse(functionNode, null);
    assertEquals("a$0", renamer.getReplacementName("a"));
    assertEquals("b$1", renamer.getReplacementName("b"));

    // Test entering a function scope with a global name
    renamer = new MakeDeclaredNamesUnique(new ContextualRenamer());
    nodeTraversal.setScopedCallback(renamer);
    nodeTraversal.traverse(functionNode, null);
    assertEquals("global", renamer.getReplacementName("global"));

    // Test entering a function scope with a name already in use in the parent scope
    renamer = new MakeDeclaredNamesUnique(new ContextualRenamer());
    nodeTraversal.setScopedCallback(renamer);
    nodeTraversal.traverse(functionNode, null);
    assertEquals("a$0", renamer.getReplacementName("a"));
    assertEquals("b$1", renamer.getReplacementName("b"));
    assertEquals("parentA", renamer.getReplacementName("parentA"));

    // Test entering a block scope with declarations
    renamer = new MakeDeclaredNamesUnique(new InlineRenamer(
        () -> "suffix", "%s", true));
    nodeTraversal.setScopedCallback(renamer);
    Node blockNode = new Node(Token.BLOCK, null, null, null, null);
    Node varNode1 = new Node(Token.VAR, null, null, null, null);
    varNode1.addChild(new Node(Token.NAME, "a", null, null, null));
    varNode1.addChild(new Node(Token.EQUAL, null, null, null, null));
    varNode1.addChild(new Node(Token.INT_LITERAL, 1, null, null, null));
    blockNode.addChild(varNode1);
    Node varNode2 = new Node(Token.VAR, null, null, null, null);
    varNode2.addChild(new Node(Token.NAME, "a", null, null, null));
    varNode2.addChild(new Node(Token.EQUAL, null, null, null, null));
    varNode2.addChild(new Node(Token.INT_LITERAL, 2, null, null, null));
    blockNode.addChild(varNode2);
    nodeTraversal.traverse(blockNode, null);
    assertEquals("a$suffix1", renamer.getReplacementName("a"));
    assertEquals("a$suffix2", renamer.getReplacementName("a"));
  }

  @Test
  public void testExitScope() {
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique;
    nodeTraversal.setScopedCallback(renamer);
    nodeTraversal.traverse(new Node("root"), null);
    assertEquals(null, renamer.getReplacementName("foo"));
  }

  @Test
  public void testShouldTraverse() {
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique;
    nodeTraversal.setScopedCallback(renamer);
    Node node = new Node(Token.NAME, "foo", null, null, null);
    assertTrue(nodeTraversal.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testVisit() {
    MakeDeclaredNamesUnique renamer = makeDeclaredNamesUnique;
    nodeTraversal.setScopedCallback(renamer);
    Node node = new Node(Token.NAME, "foo", null, null, null);
    nodeTraversal.traverse(new Node("root"), null);
    assertEquals(null, renamer.getReplacementName("foo"));
    node.setString("foo");
    nodeTraversal.traverse(new Node("root"), null);
    assertEquals("foo", renamer.getReplacementName("foo"));
  }
}