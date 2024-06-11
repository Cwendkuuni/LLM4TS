package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeafNodeTest {

 LeafNode node;
 Attributes attributes;

 @Before
 public void init() {
 node = new TextNode("Test LeafNode");
 attributes = new Attributes();
 }

 @Test
 public void testHasAttributes() {
 assertFalse(node.hasAttributes());
 }

 @Test
 public void testGetAttributes() {
 node.value = attributes;
 assertTrue(node.hasAttributes());
 assertSame(attributes, node.attributes());
 }

 @Test
 public void testEnsureAttributes() {
 node.value = "Test Value";
 node.ensureAttributes();
 assertFalse(node.hasAttributes());
 attributes = node.attributes();
 assertEquals("Test Value", attributes.get("nodeName"));
 }

 @Test
 public void testCoreValue() {
 assertEquals("Test LeafNode", node.coreValue());
 node.coreValue("Test New Value");
 assertEquals("Test New Value", node.coreValue());
 }

 @Test
 public void testCoreValueSet() {
 node.coreValue("Test New Value");
 assertEquals("Test New Value", node.coreValue());
 assertEquals("Test New Value", node.attr(node.nodeName()));
 }

 @Test
 public void testAttrGet() {
 assertNull(node.attr("nonexistentKey"));
 assertEquals("Test LeafNode", node.attr(node.nodeName()));
 }

 @Test
 public void testAttrSet() {
 node.attr(node.nodeName(), "Test New Value");
 assertEquals("Test New Value", node.attr(node.nodeName()));
 }

 @Test
 public void testHasAttr() {
 assertFalse(node.hasAttr("nonexistentKey"));
 assertTrue(node.hasAttr(node.nodeName()));
 }

 @Test
 public void testRemoveAttr() {
 node.attr(node.nodeName(), "Test Value");
 assertEquals("Test Value", node.attr(node.nodeName()));
 node.removeAttr(node.nodeName());
 assertNull(node.attr(node.nodeName()));
 }

 @Test
 public void testAbsUrl() {
 assertEquals("", node.absUrl(node.nodeName()));
 }

 @Test
 public void testBaseUri() {
 node.parent(new TextNode("Parent Node"));
 assertEquals("Parent Node", node.baseUri());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testChildNodeSize() {
 node.childNodeSize();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEnsureChildNodes() {
 node.ensureChildNodes();
 }

 @Test
 public void testValidateNotNull() {
 Validate.notNull(node);
 }

 @Test(expected = NullPointerException.class)
 public void testValidateNullKey() {
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 node.attr(null, "Test Value");
 }

 @Test
 public void testValidateEqualsKeyAndValue() {
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 node.attr(node.nodeName(), "Test Value");
 node.attr(node.nodeName(), "Test Value");
 }

 @Test
 public void testValidateDifferentKeys() {
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 node.attr(node.nodeName(), "Test Value 1");
 node.attr("Test Value 2", "Test Value");
 }

 @Test
 public void testValidateDifferentValuesForSameKey() {
 Validate.notNull(node);
 Validate.notNull(node.nodeName());
 node.attr(node.nodeName(), "Test Value 1");
 node.attr(node.nodeName(), "Test Value 2");
 }

 @Test
 public void testValidateEmptyListForChildNodes() {
 List<Node> nodes = new ArrayList<>();
 LeafNode leafNode = new TextNode("Test LeafNode");
 leafNode.ensureChildNodes();
 assertEquals(nodes, leafNode.childNodes());
 }

}