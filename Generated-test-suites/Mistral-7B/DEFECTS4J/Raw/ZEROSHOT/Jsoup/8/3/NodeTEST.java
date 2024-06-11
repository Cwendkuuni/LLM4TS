package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for Node class
 */
public class NodeTest {

 @Before
 public void setUp() {
 // initialize test data
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "testNode");
 Node node = new Element("div", baseUri, attributes);
 Document doc = new Document(baseUri);
 doc.setBaseUri(baseUri);
 doc.appendBody(node);
 }

 /**
 * Test constructor with baseUri and attributes
 */
 @Test
 public void testNodeConstructor() {
 Node node = new Node(baseUri, attributes);
 assertNotNull(node.baseUri());
 assertNotNull(node.attributes());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 }

 /**
 * Test constructor without baseUri, attributes, or children
 */
 @Test
 public void testDefaultConstructor() {
 Node node = new Node();
 assertNull(node.baseUri());
 assertNull(node.attributes());
 assertNotNull(node.childNodes());
 assertFalse(node.childNodes().isEmpty());
 }

 /**
 * Test abstract nodeName method
 */
 @Test(expected = AbstractMethodError.class)
 public void testAbstractNodeNameMethod() {
 Node node = new Node();
 String nodeName = node.nodeName();
 fail("Node is abstract class, nodeName method must be overridden in subclasses.");
 }

 /**
 * Test attr method
 */
 @Test
 public void testAttrMethod() {
 String attrValue = node.attr("id");
 assertEquals("testNode", attrValue);
 String absAttrValue = node.absUrl("id");
 assertEquals(baseUri + "#testNode", absAttrValue);
 String nonExistentAttrValue = node.attr("nonexistentattr");
 assertEquals("", nonExistentAttrValue);
 }

 /**
 * Test attributes method
 */
 @Test
 public void testAttributesMethod() {
 assertEquals(attributes, node.attributes());
 }

 /**
 * Test hasAttr method
 */
 @Test
 public void testHasAttrMethod() {
 assertTrue(node.hasAttr("id"));
 assertFalse(node.hasAttr("nonexistentattr"));
 }

 /**
 * Test removeAttr method
 */
 @Test
 public void testRemoveAttrMethod() {
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 /**
 * Test baseUri method
 */
 @Test
 public void testBaseUriMethod() {
 assertEquals(baseUri, node.baseUri());
 }

 /**
 * Test setBaseUri method
 */
 @Test
 public void testSetBaseUriMethod() {
 String newBaseUri = "https://newbase.com";
 node.setBaseUri(newBaseUri);
 assertEquals(newBaseUri, node.baseUri());
 }

 /**
 * Test absUrl method with absolute URL
 */
 @Test
 public void testAbsUrlWithAbsoluteUrl() {
 String absoluteUrl = "https://example.com/absolute.html";
 String absUrl = node.absUrl("href:" + absoluteUrl);
 assertEquals(absoluteUrl, absUrl);
 }

 /**
 * Test absUrl method with relative URL
 */
 @Test
 public void testAbsUrlWithRelativeUrl() {
 String relativeUrl = "relative.html";
 String absUrl = node.absUrl("href:" + relativeUrl);
 assertTrue(absUrl.startsWith(baseUri));
 }

 /**
 * Test absUrl method with empty string
 */
 @Test
 public void testAbsUrlWithEmptyString() {
 String absUrl = node.absUrl("");
 assertEquals("", absUrl);
 }

 /**
 * Test childNode method
 */
 @Test
 public void testChildNodeMethod() {
 Node childNode = new TextNode("Child Node");
 node.appendChild(childNode);
 Node child = node.childNode(0);
 assertEquals(childNode, child);
 }

 /**
 * Test childNodes method
 */
 @Test
 public void testChildNodesMethod() {
 Node childNode1 = new TextNode("Child Node 1");
 Node childNode2 = new TextNode("Child Node 2");
 node.appendChild(childNode1);
 node.appendChild(childNode2);
 List<Node> childNodes = node.childNodes();
 assertEquals(2, childNodes.size());
 }

 /**
 * Test childNodesAsArray method
 */
 @Test
 public void testChildNodesAsArrayMethod() {
 Node childNode1 = new TextNode("Child Node 1");
 Node childNode2 = new TextNode("Child Node 2");
 node.appendChild(childNode1);
 node.appendChild(childNode2);
 Node[] childNodesArray = node.childNodesAsArray();
 assertEquals(2, childNodesArray.length);
 }

 /**
 * Test parent method
 */
 @Test
 public void testParentMethod() {
 Node parentNode = new Element("parent", baseUri);
 parentNode.appendChild(node);
 Node parent = node.parent();
 assertEquals(parentNode, parent);
 }

 /**
 * Test ownerDocument method
 */
 @Test
 public void testOwnerDocumentMethod() {
 Document doc = node.ownerDocument();
 assertEquals(doc, node.parentNode().ownerDocument());
 }

 /**
 * Test remove method
 */
 @Test
 public void testRemoveMethod() {
 Node sibling = new TextNode("Sibling Node");
 node.parentNode().appendChild(sibling);
 node.parentNode().insertBefore(node, sibling);
 node.remove();
 assertFalse(node.parentNode().contains(node));
 }

 /**
 * Test replaceWith method
 */
 @Test
 public void testReplaceWithMethod() {
 Node replacementNode = new Element("replacement", baseUri, attributes);
 node.parentNode().replaceChild(node, replacementNode);
 assertEquals(replacementNode, node.parentNode().getChildNodes().get(0));
 }

 /**
 * Test setParentNode method
 */
 @Test
 public void testSetParentNodeMethod() {
 Node newParent = new Element("newParent", baseUri);
 node.setParentNode(newParent);
 assertEquals(newParent, node.parentNode());
 }

 /**
 * Test replaceChild method
 */
 @Test
 public void testReplaceChildMethod() {
 Node oldChild = new TextNode("Old Child");
 Node newChild = new TextNode("New Child");
 node.appendChild(oldChild);
 node.replaceChild(oldChild, newChild);
 assertEquals(newChild, node.childNodes().get(0));
 }

 /**
 * Test removeChild method
 */
 @Test
 public void testRemoveChildMethod() {
 Node oldChild = new TextNode("Old Child");
 node.appendChild(oldChild);
 node.removeChild(oldChild);
 assertFalse(node.childNodes().contains(oldChild));
 }

 /**
 * Test addChildren method
 */
 @Test
 public void testAddChildrenMethod() {
 Node child1 = new TextNode("Child 1");
 Node child2 = new TextNode("Child 2");
 node.addChildren(child1, child2);
 assertEquals(2, node.childNodes().size());
 }

 /**
 * Test addChildren method with index
 */
 @Test
 public void testAddChildrenMethodWithIndex() {
 Node child1 = new TextNode("Child 1");
 Node child2 = new TextNode("Child 2");
 node.addChildren(0, child1, child2);
 assertEquals(child1, node.childNodes().get(0));
 assertEquals(child2, node.childNodes().get(1));
 }
}