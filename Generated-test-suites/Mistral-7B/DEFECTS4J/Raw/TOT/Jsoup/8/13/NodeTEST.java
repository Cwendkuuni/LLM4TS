package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeWithBaseUriAndAttributes() {
 Node node = new Element("div", "baseUri", new Attributes().put("class", "test"));

 assertEquals("div", node.nodeName());
 assertEquals("baseUri", node.baseUri());
 assertEquals("test", node.attr("class"));
 // Test absUrl method
 assertEquals("", node.absUrl(""));
 assertEquals("baseUri/test", node.absUrl("class"));
 assertEquals("baseUri/test", node.absUrl("abs:class"));
 // Test attr method with existing attribute
 assertEquals("test", node.attr("class"));
 // Test attr method with non-existing attribute
 assertEquals("", node.attr("nonexistent"));
 // Test hasAttr method
 assertTrue(node.hasAttr("class"));
 // Test hasAttr method with non-existing attribute
 assertFalse(node.hasAttr("nonexistent"));
 // Test removeAttr method
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testNodeWithChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p", "child1");
 Node child2 = new Element("p", "child2");
 parent.addChildren(child1, child2);

 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 @Test
 public void testNodeWithParent() {
 Node child = new Element("p");
 Node parent = new Element("div");
 child.setParentNode(parent);

 assertEquals(parent, child.parent());
 assertNull(child.parent()); // default constructor
 }

 @Test(expected = NullPointerException.class)
 public void testNodeWithNullBaseUri() {
 Node node = new Element("div", null, new Attributes());
 }

 @Test(expected = NullPointerException.class)
 public void testNodeWithNullAttributes() {
 Node node = new Element("div", "", null);
 }
}