package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class NodeTest {

 // Test constructor with baseUri and attributes
 @Test
 public void testNodeWithBaseUriAndAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "node1");
 Node node = new Node(baseUri, attributes);

 assertEquals("node1", node.nodeName());
 assertEquals("http://example.com", node.baseUri());
 assertEquals(attributes, node.attributes());
 }

 // Test constructor without baseUri and with attributes
 @Test
 public void testNodeWithoutBaseUriAndAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("id", "node1");
 Node node = new Node("", attributes);

 assertEquals("node1", node.nodeName());
 assertNull(node.baseUri());
 assertEquals(attributes, node.attributes());
 }

 // Test constructor without baseUri, attributes or children
 @Test
 public void testNodeWithoutBaseUriOrAttributes() {
 Node node = new Node();

 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.baseUri());
 assertNull(node.attributes());
 }

 // Test getNodeName method
 @Test
 public void testgetNodeName() {
 String nodeName = "element";
 Node node = new Node() {
 @Override
 public String nodeName() {
 return nodeName;
 }
 };

 assertEquals(nodeName, node.nodeName());
 }

 // Test getAttribute method with existing attribute
 @Test
 public void testgetAttributeWithExistingAttribute() throws MalformedURLException {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "node1");
 attributes.put("href", "about.html");
 Node node = new Node(baseUri, attributes);

 assertEquals("about.html", node.absUrl("href"));
 }

 // Test getAttribute method with missing attribute
 @Test
 public void testgetAttributeWithMissingAttribute() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node node = new Node(baseUri, attributes);

 assertEquals("", node.absUrl("missingAttr"));
 }

 // Test getAttribute method with absolute attribute
 @Test
 public void testgetAttributeWithAbsoluteAttribute() throws MalformedURLException {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "http://example.org");
 Node node = new Node(baseUri, attributes);

 assertEquals("http://example.org", node.absUrl("href"));
 }

 // Test hasAttr method
 @Test
 public void testHasAttr() {
 Attributes attributes = new Attributes();
 attributes.put("id", "node1");
 Node node = new Node("", attributes);

 assertTrue(node.hasAttr("id"));
 assertFalse(node.hasAttr("class"));
 }

 // Test setAttribute method
 @Test
 public void testSetAttribute() {
 Attributes attributes = new Attributes();
 Node node = new Node("", attributes);

 attributes.put("id", "node1");
 node.attr("id", "node2");
 assertEquals("node2", node.attr("id"));
 }

 // Test removeAttribute method
 @Test
 public void testRemoveAttribute() {
 Attributes attributes = new Attributes();
 Node node = new Node("", attributes);

 attributes.put("id", "node1");
 node.removeAttr("id");
 assertNull(node.attr("id"));
 }

 // Test getBaseUri method
 @Test
 public void testGetBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Node(baseUri, new Attributes());

 assertEquals(baseUri, node.baseUri());
 }

 // Test setBaseUri method
 @Test
 public void testSetBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Node("", new Attributes());

 node.setBaseUri(baseUri);
 assertEquals(baseUri, node.baseUri());
 }

 // Test getChildNode method
 @Test
 public void testGetChildNode() {
 List<Node> children = new ArrayList<>();
 children.add(new Node());
 children.add(new Node());
 Node parent = new Node("", new Attributes());
 parent.addChildren(children.toArray(new Node[0]));

 Node child1 = parent.childNode(0);
 Node child2 = parent.childNode(1);

 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 // Test getChildNodes method
 @Test
 public void testGetChildNodes() {
 List<Node> children = new ArrayList<>();
 children.add(new Node());
 children.add(new Node());
 Node parent = new Node("", new Attributes());
 parent.addChildren(children.toArray(new Node[0]));

 assertEquals(children, parent.childNodes());
 }

 // Test removeChild method
 @Test
 public void testRemoveChild() {
 List<Node> children = new ArrayList<>();
 children.add(new Node());
 children.add(new Node());
 Node parent = new Node("", new Attributes());
 parent.addChildren(children.toArray(new Node[0]));

 Node childToRemove = parent.childNode(0);
 parent.removeChild(childToRemove);
 assertEquals(1, parent.childNodes().size());
 assertEquals(childToRemove, parent.childNode(0));
 }

 // Test addChildren method
 @Test
 public void testAddChildren() {
 List<Node> children = new ArrayList<>();
 Node parent = new Node("", new Attributes());
 parent.addChildren(children.toArray(new Node[0]));

 assertEquals(0, parent.childNodes().size());

 children.add(new Node());
 children.add(new Node());
 parent.addChildren(children.toArray(new Node[0]));
 assertEquals(2, parent.childNodes().size());
 }

 // Test replaceChild method
 @Test
 public void testReplaceChild() throws MalformedURLException {
 List<Node> children = new ArrayList<>();
 Node oldChild = new Node("", new Attributes());
 oldChild.attr("id", "oldChild");
 children.add(oldChild);
 Node newChild = new Node("", new Attributes());
 newChild.attr("id", "newChild");
 Node parent = new Node("", new Attributes());
 parent.addChildren(children.toArray(new Node[0]));

 parent.replaceChild(oldChild, newChild);
 assertEquals(newChild, parent.childNode(0));
 assertEquals(1, parent.childNodes().size());
 }
}