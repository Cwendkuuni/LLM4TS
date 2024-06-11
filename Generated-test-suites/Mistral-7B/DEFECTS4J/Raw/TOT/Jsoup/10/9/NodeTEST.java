package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the abstract Node class
 */
public class NodeTest {

 @Before
 public void setUp() {
 // initialize some test data
 String baseUri = "https://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "testId");
 Node node = new Element("div", baseUri, attributes);
 }

 /**
 * Test constructor with baseUri and attributes
 */
 @Test
 public void testNodeWithBaseUriAndAttributes() {
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertTrue(node.hasAttr("id"));
 assertEquals("testId", node.attr("id"));
 }

 /**
 * Test constructor without baseUri, attributes or children
 */
 @Test
 public void testNodeWithoutBaseUriOrAttributesOrChildren() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 }

 /**
 * Test getAttribute method with existing attribute
 */
 @Test
 public void testGetAttributeWithExistingAttribute() {
 assertEquals("testId", node.attr("id"));
 }

 /**
 * Test getAttribute method with non-existent attribute
 */
 @Test
 public void testGetAttributeWithNonExistentAttribute() {
 assertEquals("", node.attr("nonExistentAttribute"));
 }

 /**
 * Test getAttribute method with relative URL attribute
 */
 @Test
 public void testGetAttributeWithRelativeUrlAttribute() {
 String relativeUrl = "relative.html";
 attributes.put("href", relativeUrl);
 String absoluteUrl = node.absUrl("href");
 assertEquals("https://example.com/relative.html", absoluteUrl);
 }

 /**
 * Test getAttribute method with absolute URL attribute
 */
 @Test
 public void testGetAttributeWithAbsoluteUrlAttribute() {
 String absoluteUrl = "https://example.com/absolute.html";
 attributes.put("href", absoluteUrl);
 assertEquals(absoluteUrl, node.absUrl("href"));
 }

 /**
 * Test getAttribute method with empty attribute
 */
 @Test
 public void testGetAttributeWithEmptyAttribute() {
 String emptyAttribute = "";
 attributes.put("empty", emptyAttribute);
 assertEquals(emptyAttribute, node.attr("empty"));
 }

 /**
 * Test setAttribute method
 */
 @Test
 public void testSetAttribute() {
 String newAttribute = "newAttribute";
 node.attr("newAttribute", newAttribute);
 assertTrue(node.hasAttr("newAttribute"));
 assertEquals(newAttribute, node.attr("newAttribute"));
 }

 /**
 * Test hasAttribute method
 */
 @Test
 public void testHasAttribute() {
 assertTrue(node.hasAttr("id"));
 assertFalse(node.hasAttr("nonExistentAttribute"));
 }

 /**
 * Test removeAttribute method
 */
 @Test
 public void testRemoveAttribute() {
 node.removeAttr("id");
 assertFalse(node.hasAttr("id"));
 }

 /**
 * Test addChild method
 */
 @Test
 public void testAddChild() {
 Node child = new Element("p", baseUri, new Attributes());
 node.addChild(child);
 assertEquals(1, node.childNodes().size());
 assertEquals(child, node.childNode(0));
 }

 /**
 * Test addChildren method
 */
 @Test
 public void testAddChildren() {
 Node child1 = new Element("p", baseUri, new Attributes());
 Node child2 = new Element("p", baseUri, new Attributes());
 node.addChildren(child1, child2);
 assertEquals(2, node.childNodes().size());
 }

 /**
 * Test removeChild method
 */
 @Test
 public void testRemoveChild() {
 Node child = node.childNode(0);
 node.removeChild(child);
 assertEquals(0, node.childNodes().size());
 }

 /**
 * Test getBaseUri method
 */
 @Test
 public void testGetBaseUri() {
 assertEquals(baseUri, node.baseUri());
 }

 /**
 * Test setBaseUri method
 */
 @Test
 public void testSetBaseUri() {
 String newBaseUri = "https://newexample.com";
 node.setBaseUri(newBaseUri);
 assertEquals(newBaseUri, node.baseUri());
 }

 /**
 * Test absUrl method with existing URL attribute
 */
 @Test
 public void testAbsUrlWithExistingUrlAttribute() {
 String attributeUrl = "https://example.com/existing.html";
 attributes.put("href", attributeUrl);
 String absoluteUrl = node.absUrl("href");
 assertEquals(attributeUrl, absoluteUrl);
 }

 /**
 * Test absUrl method with non-existent URL attribute
 */
 @Test
 public void testAbsUrlWithNonExistentUrlAttribute() {
 String relativeUrl = "relative.html";
 attributes.put("src", relativeUrl);
 String absoluteUrl = node.absUrl("src");
 assertEquals("", absoluteUrl);
 }

 /**
 * Test childNodes method
 */
 @Test
 public void testChildNodes() {
 Node child = new Element("p", baseUri, new Attributes());
 node.addChild(child);
 List<Node> childNodes = node.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(child, childNodes.get(0));
 }

 /**
 * Test siblingNodes method
 */
 @Test
 public void testSiblingNodes() {
 Node sibling = new Element("div", baseUri, new Attributes());
 node.parentNode.addChild(sibling);
 node.parentNode.addChild(node);
 node.parentNode.addChild(new Element("p", baseUri, new Attributes()));
 List<Node> siblingNodes = node.siblingNodes();
 assertEquals(2, siblingNodes.size());
 assertTrue(siblingNodes.contains(sibling));
 assertTrue(siblingNodes.contains(new Element("p", baseUri, new Attributes())));
 }

 /**
 * Test nextSibling method
 */
 @Test
 public void testNextSibling() {
 Node sibling = new Element("div", baseUri, new Attributes());
 Node nextSibling = new Element("p", baseUri, new Attributes());
 node.parentNode.addChild(node);
 node.parentNode.addChild(sibling);
 node.parentNode.addChild(nextSibling);
 node.setNextSibling(nextSibling);
 assertEquals(nextSibling, node.nextSibling());
 }

 /**
 * Test previousSibling method
 */
 @Test
 public void testPreviousSibling() {
 Node previousSibling = new Element("p", baseUri, new Attributes());
 Node sibling = new Element("div", baseUri, new Attributes());
 node.parentNode.addChild(node);
 node.parentNode.addChild(sibling);
 node.parentNode.addChild(previousSibling);
 node.setPreviousSibling(previousSibling);
 assertEquals(previousSibling, node.previousSibling());
 }

 /**
 * Test outerHtml method
 */
 @Test
 public void testOuterHtml() {
 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);
 String expectedHtml = "<div id=\"testId\"></div>";
 assertEquals(expectedHtml, accum.toString());
 }
}