package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Arrays;

public class NodeTest {

 @Test
 public void testNodeConstructor() {
 Node node = new Node("http://www.example.com", new Attributes());
 assertEquals("http://www.example.com", node.baseUri());
 Assert.assertNotNull(node.attributes());
 Assert.assertEquals(0, node.childNodes().size());
 Assert.assertNull(node.parentNode());
 }

 @Test
 public void testNodeConstructorWithEmptyAttributes() {
 Node node = new Node("http://www.example.com");
 assertEquals("http://www.example.com", node.baseUri());
 Assert.assertNull(node.attributes());
 Assert.assertEquals(0, node.childNodes().size());
 Assert.assertNull(node.parentNode());
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Node();
 Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
 Assert.assertNull(node.attributes());
 Assert.assertNull(node.parentNode());
 }

 @Test
 public void testNodeName() {
 Node node = new Elements.TextNode("Test");
 assertEquals("Test", node.nodeName());
 }

 @Test
 public void testAttr() {
 Node node = new Elements.Element("div", new Attributes().put("id", "test"));
 assertEquals("test", node.attr("id"));
 assertEquals("", node.attr("nonExistentAttribute"));
 }

 @Test
 public void testAttributes() {
 Node node = new Elements.Element("div", new Attributes().put("id", "test").put("class", "testClass"));
 Attributes attributes = node.attributes();
 assertEquals("test", attributes.get("id"));
 assertEquals("testClass", attributes.get("class"));
 assertEquals(2, attributes.size());
 }

 @Test
 public void testHasAttr() {
 Node node = new Elements.Element("div", new Attributes().put("id", "test").put("class", "testClass"));
 assertTrue(node.hasAttr("id"));
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("nonExistentAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 Node node = new Elements.Element("div", new Attributes().put("id", "test"));
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 @Test
 public void testAbsUrl() {
 Node node = new Elements.Element("a", new Attributes().put("href", "relative.html"));
 assertEquals("", node.absUrl("href")); // no base URL
 assertEquals("http://www.example.com/relative.html", node.absUrl("href", "http://www.example.com"));
 }

 @Test
 public void testChildNodes() {
 Node parent = new Elements.Element("div");
 Node child1 = new Elements.Element("p", new Attributes().put("class", "child1"));
 Node child2 = new Elements.Element("p", new Attributes().put("class", "child2"));
 parent.addChildren(child1, child2);
 List<Node> childNodes = parent.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testParent() {
 Node child = new Elements.Element("p", new Attributes().put("class", "child"));
 Node parent = new Elements.Element("div", new Attributes().put("id", "parent"));
 parent.addChild(child);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testOwnerDocument() {
 Node child = new Elements.Element("p", new Attributes().put("class", "child"));
 Node parent = new Document("").parseBodyElements().get(0);
 parent.addChild(child);
 assertEquals(parent, child.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parent = new Elements.Element("div");
 Node child1 = new Elements.Element("p", new Attributes().put("class", "child1"));
 Node child2 = new Elements.Element("p", new Attributes().put("class", "child2"));
 parent.addChildren(child1, child2);
 parent.removeChild(child1);
 List<Node> childNodes = parent.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(child2, childNodes.get(0));
 }

 @Test
 public void testReplaceWith() {
 Node parent = new Elements.Element("div");
 Node child1 = new Elements.Element("p", new Attributes().put("class", "child1"));
 Node child2 = new Elements.Element("p", new Attributes().put("class", "child2"));
 parent.addChildren(child1, child2);
 Node newChild = new Elements.Element("p", new Attributes().put("class", "newChild"));
 parent.replaceChild(child1, newChild);
 List<Node> childNodes = parent.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(newChild, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testClone() {
 Node original = new Elements.Element("div", new Attributes().put("id", "original"));
 Node cloned = original.clone();
 assertEquals(original, cloned); // equals checks identity
 List<Node> originalChildren = original.childNodes();
 List<Node> clonedChildren = cloned.childNodes();
 assertEquals(originalChildren.size(), clonedChildren.size());
 for (int i = 0; i < originalChildren.size(); i++) {
 Node originalChild = originalChildren.get(i);
 Node clonedChild = clonedChildren.get(i);
 assertEquals(originalChild.nodeName(), clonedChild.nodeName());
 assertEquals(originalChild.attributes(), clonedChild.attributes());
 }
 }
}