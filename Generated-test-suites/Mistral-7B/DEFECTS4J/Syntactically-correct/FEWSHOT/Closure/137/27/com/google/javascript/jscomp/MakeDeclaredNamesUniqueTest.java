package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = Node.createNode(Token.FUNCTION, "test");
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "c"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a_0");
    expectedNames.add("b");
    expectedNames.add("c");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_contextual_renamer() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = Node.createNode(Token.FUNCTION, "test");
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a_1");
    expectedNames.add("a_2");
    expectedNames.add("b");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_inline_renamer() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(Node::newUniqueId, "i_"));

    Node node = Node.createNode(Token.FUNCTION, "test");
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a_i_0");
    expectedNames.add("a_i_1");
    expectedNames.add("b");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_inline_renamer_removeConstness() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(Node::newUniqueId, "i_", true));

    Node node = Node.createNode(Token.FUNCTION, "test");
    node.addChild(Node.createNode(Token.NAME, "a", Node.IS_CONSTANT_NAME, true));
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a_i_0");
    expectedNames.add("a_i_1");
    expectedNames.add("b");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      } else if (child.getType() == Token.NAME && child.hasProp(Node.IS_CONSTANT_NAME)) {
        assertNull(child.getString());
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_contextual_renamer_global() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = Node.createNode(Token.SCRIPT, null);
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.FUNCTION, "test"));
    node.getLastChild().addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a");
    expectedNames.add("a_0");
    expectedNames.add("b");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_contextual_renamer_function() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new ContextualRenamer());

    Node node = Node.createNode(Token.FUNCTION, "test");
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a_0");
    expectedNames.add("a_1");
    expectedNames.add("b");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_inline_renamer_function() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(Node::newUniqueId, "i_", false));

    Node node = Node.createNode(Token.FUNCTION, "test");
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "a"));
    node.addChild(Node.createNode(Token.NAME, "b"));

    MakeDeclaredNamesUnique.traverse(node, makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("a_i_0");
    expectedNames.add("a_i_1");
    expectedNames.add("b");

    Iterator<Node> nodeIterator = node.getFirstChild().iterateNext();
    while (nodeIterator.hasNext()) {
      Node child = nodeIterator.next();
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
        expectedNames.remove(name);
      }
    }

    assertFalse(expectedNames.isEmpty());
  }

  @Test
  public void testMakeDeclaredNamesUnique_getReplacementName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new ContextualRenamer());

    String oldName = "a";
    String replacementName = makeDeclaredNamesUnique.getReplacementName(oldName);
    assertEquals("a_0", replacementName);

    oldName = "a_0";
    replacementName = makeDeclaredNamesUnique.getReplacementName(oldName);
    assertEquals("a_1", replacementName);

    oldName = "b";
    replacementName = makeDeclaredNamesUnique.getReplacementName(oldName);
    assertNull(replacementName);
  }
}