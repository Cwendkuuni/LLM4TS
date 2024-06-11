import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

public class NodeTest {

 // Test constructor with baseUri and attributes
 @Test
 public void testNodeWithBaseUriAndAttributes() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test"));
 assertEquals("http://example.com", node.baseUri());
 assertEquals("test", node.attr("id"));
 }

 // Test constructor without baseUri and attributes
 @Test
 public void testNodeWithoutBaseUriAndAttributes() {
 Node node = new Node();
 assertNull(node.baseUri());
 assertNull(node.attributes());
 }

 // Test getNodeName() method
 @Test
 public void testgetNodeName() {
 Node element = new Element("div");
 assertEquals("div", element.nodeName());
 }

 // Test getAttribute() method
 @Test
 public void testAttr() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test").put("class", "example"));
 assertEquals("test", node.attr("id"));
 assertEquals("example", node.attr("class"));
 assertEquals("", node.attr("nonExistentAttribute"));
 }

 // Test getAttributes() method
 @Test
 public void testAttributes() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test").put("class", "example"));
 Attributes attributes = node.attributes();
 assertTrue(attributes.hasKey("id"));
 assertTrue(attributes.hasKey("class"));
 assertEquals("test", attributes.get("id"));
 assertEquals("example", attributes.get("class"));
 }

 // Test setAttribute() method
 @Test
 public void testSetAttribute() {
 Node node = new Node("http://example.com", new Attributes());
 node.attr("id", "test");
 node.attr("class", "example");
 Attributes attributes = node.attributes();
 assertEquals("test", attributes.get("id"));
 assertEquals("example", attributes.get("class"));
 }

 // Test hasAttr() method
 @Test
 public void testHasAttr() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test").put("class", "example"));
 assertTrue(node.hasAttr("id"));
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("nonExistentAttribute"));
 }

 // Test removeAttribute() method
 @Test
 public void testRemoveAttribute() {
 Node node = new Node("http://example.com", new Attributes().put("id", "test").put("class", "example"));
 node.removeAttr("id");
 Attributes attributes = node.attributes();
 assertFalse(attributes.hasKey("id"));
 assertTrue(attributes.hasKey("class"));
 }

 // Test getBaseUri() method
 @Test
 public void testGetBaseUri() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 }

 // Test setBaseUri() method
 @Test
 public void testSetBaseUri() {
 Node node = new Node("", new Attributes());
 node.setBaseUri("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 // Test absUrl() method with absolute URL
 @Test
 public void testAbsUrlAbsolute() {
 Node node = new Node("http://example.com", new Attributes().put("href", "/path/to/file.html"));
 assertEquals("http://example.com/path/to/file.html", node.absUrl("href"));
 }

 // Test absUrl() method with relative URL
 @Test
 public void testAbsUrlRelative() {
 Node node = new Node("http://example.com/index.html", new Attributes().put("href", "path/to/file.html"));
 assertEquals("http://example.com/path/to/file.html", node.absUrl("href"));
 }

 // Test getChildNode() method
 @Test
 public void testGetChildNode() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode = new Node("", new Attributes());
 parentNode.addChild(childNode);
 Node retrievedChildNode = parentNode.childNode(0);
 assertEquals(childNode, retrievedChildNode);
 }

 // Test getChildren() method
 @Test
 public void testGetChildren() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode1 = new Node("", new Attributes());
 Node childNode2 = new Node("", new Attributes());
 parentNode.addChildren(childNode1, childNode2);
 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertEquals(childNode1, children.get(0));
 assertEquals(childNode2, children.get(1));
 }

 // Test getParent() method
 @Test
 public void testGetParent() {
 Node childNode = new Node("", new Attributes());
 Node parentNode = new Node("http://example.com", new Attributes());
 parentNode.addChild(childNode);
 Node retrievedParentNode = childNode.parent();
 assertEquals(parentNode, retrievedParentNode);
 }

 // Test remove() method
 @Test
 public void testRemove() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode = new Node("", new Attributes());
 parentNode.addChild(childNode);
 parentNode.remove();
 assertFalse(parentNode.hasChildNodes());
 }

 // Test replaceWith() method
 @Test
 public void testReplaceWith() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node oldNode = new Node("", new Attributes());
 Node newNode = new Node("", new Attributes());
 parentNode.addChild(oldNode);
 parentNode.replaceWith(newNode);
 assertEquals(newNode, parentNode.childNode(0));
 }

 // Test setParentNode() method
 @Test
 public void testSetParentNode() {
 Node childNode = new Node("", new Attributes());
 Node parentNode1 = new Node("http://example.com1", new Attributes());
 Node parentNode2 = new Node("http://example.com2", new Attributes());
 childNode.setParentNode(parentNode1);
 parentNode1.addChild(childNode);
 childNode.setParentNode(parentNode2);
 assertEquals(parentNode2, childNode.parent());
 }

 // Test replaceChild() method
 @Test
 public void testReplaceChild() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node oldNode = new Node("", new Attributes());
 Node newNode = new Node("", new Attributes());
 parentNode.addChild(oldNode);
 parentNode.replaceChild(oldNode, newNode);
 assertEquals(newNode, parentNode.childNode(0));
 }

 // Test removeChild() method
 @Test
 public void testRemoveChild() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node oldNode = new Node("", new Attributes());
 parentNode.addChild(oldNode);
 parentNode.removeChild(oldNode);
 assertFalse(parentNode.hasChildNodes());
 }

 // Test addChildren() method
 @Test
 public void testAddChildren() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode1 = new Node("", new Attributes());
 Node childNode2 = new Node("", new Attributes());
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 }

 // Test addChildren() method with index
 @Test
 public void testAddChildrenWithIndex() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode1 = new Node("", new Attributes());
 Node childNode2 = new Node("", new Attributes());
 parentNode.addChildren(1, childNode1, childNode2);
 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertEquals(childNode1, children.get(1));
 assertEquals(childNode2, children.get(2));
 }
}