package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

 LeafNode node;
 Attributes attributes;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 node = new TextNode("Test Value");
 attributes = new Attributes();
 }

 @Test
 public void testHasAttributes() {
 assertFalse(node.hasAttributes());
 }

 @Test
 public void testAttributes() {
 node.ensureAttributes();
 node.coreValue("Test Attribute");
 attributes.put("test", "Test Value");
 node.value = attributes;

 Attributes returnedAttributes = node.attributes();
 assertEquals(attributes, returnedAttributes);
 }

 @Test
 public void testCoreValue() {
 assertEquals("Test Value", node.coreValue());
 }

 @Test
 public void testCoreValueWithAttributes() {
 node.coreValue("Test Attribute");
 assertEquals("Test Attribute", node.coreValue());
 }

 @Test
 public void testAttr() {
 assertNull(node.attr("test"));
 node.coreValue("Test Attribute");
 assertEquals("Test Attribute", node.attr("test"));
 }

 @Test
 public void testAttrWithEmptyValue() {
 node.coreValue("Test Attribute");
 node.attr("test", "");
 assertNull(node.attr("test"));
 }

 @Test
 public void testAttrWithSameValue() {
 node.coreValue("Test Attribute");
 node.attr("test", "Test Attribute");
 assertEquals("Test Attribute", node.attr("test"));
 }

 @Test
 public void testAttrWithDifferentNodeName() {
 node.coreValue("Test Attribute");
 assertNull(node.attr("other"));
 }

 @Test
 public void testHasAttr() {
 assertFalse(node.hasAttr("test"));
 node.coreValue("Test Attribute");
 assertTrue(node.hasAttr("test"));
 }

 @Test
 public void testRemoveAttr() {
 node.coreValue("Test Attribute");
 node.removeAttr("test");
 assertFalse(node.hasAttr("test"));
 }

 @Test
 public void testAttrWithParentNode() {
 Element parent = new Element("parent");
 parent.add(node);
 node.coreValue("Test Attribute");
 parent.attr("test", "Test Value");
 assertEquals("Test Value", node.attr("test"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 node.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }
}