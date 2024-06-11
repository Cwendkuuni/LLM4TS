package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

public class NodeTest {

 @Test
 public void testNodeConstructor() {
 Node node = new Node("http://example.com", new Attributes());
 assertNotNull(node);
 assertNotNull(node.attributes());
 assertEquals("http://example.com", node.baseUri());
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.parentNode());
 }

 @Test
 public void testNodeConstructorWithAttributes() {
 Node node = new Node("http://example.com", new Attributes().put("id", "example"));
 assertNotNull(node);
 assertNotNull(node.attributes());
 assertEquals("http://example.com", node.baseUri());
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.parentNode());
 assertEquals("example", node.attr("id"));
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Node();
 assertNotNull(node);
 assertNull(node.baseUri());
 assertNull(node.attributes());
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.parentNode());
 }

 @Test
 public void testNodeName() {
 Node node = new Node("http://example.com", new Attributes());
 assertNotNull(node.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidNodeName() {
 Node node = new Node("", new Attributes());
 fail("Should throw an AssertionError.");
 }

 @Test
 public void testAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "example"));
 assertEquals("example", node.attr("id"));
 assertEquals("", node.attr("nonexistent"));
 }

 @Test
 public void testAttributesMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "example").put("class", "exampleClass"));
 Attributes attributes = node.attributes();
 assertEquals("example", attributes.get("id"));
 assertEquals("exampleClass", attributes.get("class"));
 assertEquals(2, attributes.size());
 }

 @Test
 public void testHasAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "example"));
 assertTrue(node.hasAttr("id"));
 assertFalse(node.hasAttr("nonexistent"));
 }

 @Test
 public void testRemoveAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("id", "example"));
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 @Test
 public void testGetBaseUriMethod() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testSetBaseUriMethod() {
 Node node = new Node("http://example.com", new Attributes());
 node.setBaseUri("http://newbase.com");
 assertEquals("http://newbase.com", node.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Node node = new Node("http://example.com", new Attributes());
 try {
 node.setBaseUri(null);
 fail("Should throw an IllegalArgumentException.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbsUrlMethod() {
 Node node = new Node("http://example.com", new Attributes().put("href", "relative.html"));
 assertEquals("http://example.com/relative.html", node.absUrl("href"));
 assertEquals("", node.absUrl("nonexistent"));
 }

 @Test(expected = MalformedURLException.class)
 public void testInvalidUrlAbsUrl() {
 Node node = new Node("http://example.com", new Attributes());
 try {
 node.absUrl("invalid:url");
 fail("Should throw a MalformedURLException.");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testChildNodeMethod() {
 Node parent = new Node("http://example.com", new Attributes());
 Node child = new Node("http://child.com", new Attributes());
 parent.childNodes.add(child);
 assertEquals(child, parent.childNode(0));
 }

 @Test
 public void testChildNodesMethod() {
 Node parent = new Node("http://example.com", new Attributes());
 Node child1 = new Node("http://child1.com", new Attributes());
 Node child2 = new Node("http://child2.com", new Attributes());
 parent.childNodes.add(child1);
 parent.childNodes.add(child2);
 List<Node> childNodes = parent.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 @Test
 public void testChildNodesAsArrayMethod() {
 Node parent = new Node("http://example.com", new Attributes());
 Node child1 = new Node("http://child1.com", new Attributes());
 Node child2 = new Node("http://child2.com", new Attributes());
 parent.childNodes.add(child1);
 parent.childNodes.add(child2);
 Node[] childNodes = parent.childNodesAsArray();
 assertEquals(2, childNodes.length);
 assertEquals(child1, childNodes[0]);
 assertEquals(child2, childNodes[1]);
 }

 @Test
 public void testParentMethod() {
 Node child = new Node("http://child.com", new Attributes().put("href", "parent.html"));
 Node parent = new Node("http://parent.com", new Attributes());
 parent.childNodes.add(child);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testOwnerDocumentMethod() {
 Node child = new Node("http://child.com", new Attributes().put("href", "parent.html"));
 Node parent = new Node("http://parent.com", new Attributes());
 parent.childNodes.add(child);
 assertEquals(parent, child.ownerDocument());
 }

 @Test
 public void testRemoveMethod() {
 Node parent = new Node("http://parent.com", new Attributes());
 Node child = new Node("http://child.com", new Attributes().put("href", "child.html"));
 parent.childNodes.add(child);
 parent.remove();
 assertTrue(parent.childNodes().isEmpty());
 }

 @Test
 public void testReplaceWithMethod() {
 Node parent = new Node("http://parent.com", new Attributes());
 Node oldChild = new Node("http://oldchild.com", new Attributes().put("href", "oldchild.html"));
 Node newChild = new Node("http://newchild.com", new Attributes().put("href", "newchild.html"));
 parent.childNodes.add(oldChild);
 parent.replaceWith(newChild);
 assertEquals(newChild, parent.childNodes().get(0));
 }

 @Test
 public void testSetParentNodeMethod() {
 Node child = new Node("http://child.com", new Attributes().put("href", "child.html"));
 Node parent1 = new Node("http://parent1.com", new Attributes());
 Node parent2 = new Node("http://parent2.com", new Attributes());
 child.setParentNode(parent1);
 assertEquals(parent1, child.parentNode());
 child.setParentNode(parent2);
 assertEquals(parent2, child.parentNode());
 }

 @Test
 public void testReplaceChildMethod() {
 Node parent = new Node("http://parent.com", new Attributes());
 Node oldChild = new Node("http://oldchild.com", new Attributes().put("href", "oldchild.html"));
 Node newChild = new Node("http://newchild.com", new Attributes().put("href", "newchild.html"));
 parent.childNodes.add(oldChild);
 parent.replaceChild(oldChild, newChild);
 assertEquals(newChild, parent.childNodes().get(0));
 }

 @Test
 public void testRemoveChildMethod() {
 Node parent = new Node("http://parent.com", new Attributes());
 Node child = new Node("http://child.com", new Attributes().put("href", "child.html"));
 parent.childNodes.add(child);
 parent.removeChild(child);
 assertTrue(parent.childNodes().isEmpty());
 }

 @Test
 public void testAddChildrenMethod() {
 Node parent = new Node("http://parent.com", new Attributes());
 Node child1 = new Node("http://child1.com", new Attributes().put("href", "child1.html"));
 Node child2 = new Node("http://child2.com", new Attributes().put("href", "child2.html"));
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNodes().get(0));
 assertEquals(child2, parent.childNodes().get(1));
 }

 @Test
 public void testAddChildrenMethodWithArray() {
 Node parent = new Node("http://parent.com", new Attributes());
 Node child1 = new Node("http://child1.com", new Attributes().put("href", "child1.html"));
 Node child2 = new Node("http://child2.com", new Attributes().put("href", "child2.html"));
 parent.addChildren(new Node[] {child1, child2});
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNodes().get(0));
 assertEquals(child2, parent.childNodes().get(1));
 }
}