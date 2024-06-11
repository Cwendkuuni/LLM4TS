package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

 @Test
 public void testAbstractNodeConstructor() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 assertSame(new Attributes(), node.attributes());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 }

 @Test
 public void testAbstractNodeStringConstructor() {
 Node node = new Node("baseUri");
 assertEquals("baseUri", node.baseUri());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 }

 @Test
 public void testAbstractNodeDefaultConstructor() {
 Node node = new Node();
 assertNull(node.baseUri());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 }

 @Test
 public void testNodeName() {
 // Abstract Node doesn't have a nodeName, so we cannot test it directly.
 }

 @Test
 public void testGetAttribute() {
 Node node = new ElementsElement("baseUri", new Attributes().put("key", "value"));
 assertEquals("value", node.attr("key"));
 assertEquals("", node.attr("nonExistentKey"));
 }

 @Test
 public void testSetAttribute() {
 Node node = new ElementsElement("baseUri", new Attributes());
 node.attr("key", "value");
 assertEquals("value", node.attr("key"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new ElementsElement("baseUri", new Attributes().put("key", "value"));
 assertTrue(node.hasAttr("key"));
 assertFalse(node.hasAttr("nonExistentKey"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new ElementsElement("baseUri", new Attributes().put("key", "value"));
 node.removeAttr("key");
 assertFalse(node.hasAttr("key"));
 }

 @Test
 public void testGetAbsoluteUrl() {
 Node node = new ElementsElement("baseUri", new Attributes().put("abs:href", "relativeUrl"));
 assertEquals("baseUri/relativeUrl", node.absUrl("href"));
 assertEquals("baseUri", node.absUrl(""));
 assertEquals("", node.absUrl("nonExistentAttr"));
 }

 @Test
 public void testGetChildNode() {
 Node parentNode = new ElementsElement("baseUri", new Attributes());
 Node childNode = new ElementsElement("childUri", new Attributes());
 parentNode.addChild(childNode);
 assertEquals(childNode, parentNode.childNode(0));
 }

 @Test
 public void testGetChildren() {
 Node parentNode = new ElementsElement("baseUri", new Attributes());
 Node childNode1 = new ElementsElement("child1Uri", new Attributes());
 Node childNode2 = new ElementsElement("child2Uri", new Attributes());
 parentNode.addChildren(childNode1, childNode2);
 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertEquals(childNode1, children.get(0));
 assertEquals(childNode2, children.get(1));
 }

 @Test
 public void testParentNode() {
 Node childNode = new ElementsElement("childUri", new Attributes());
 Node parentNode = new ElementsElement("parentUri", new Attributes());
 parentNode.addChild(childNode);
 assertEquals(parentNode, childNode.parentNode());
 assertNull(childNode.parentNode(), "No parentNode when no parent");
 }

 @Test
 public void testRemove() {
 Node parentNode = new ElementsElement("baseUri", new Attributes());
 Node childNode = new ElementsElement("childUri", new Attributes());
 parentNode.addChild(childNode);
 parentNode.remove();
 assertNull(childNode.parentNode());
 }

 @Test
 public void testReplaceChild() {
 Node parentNode = new ElementsElement("baseUri", new Attributes());
 Node childNode1 = new ElementsElement("child1Uri", new Attributes());
 Node childNode2 = new ElementsElement("child2Uri", new Attributes());
 parentNode.addChild(childNode1);
 parentNode.replaceChild(childNode1, childNode2);
 assertEquals(childNode2, parentNode.childNode(0));
 assertNull(childNode1.parentNode());
 }

 @Test
 public void testAddChildren() {
 Node parentNode = new ElementsElement("baseUri", new Attributes());
 Node childNode1 = new ElementsElement("child1Uri", new Attributes());
 Node childNode2 = new ElementsElement("child2Uri", new Attributes());
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 }

 @Test
 public void testAddChildrenWithArray() {
 Node parentNode = new ElementsElement("baseUri", new Attributes());
 Node[] children = {new ElementsElement("child1Uri", new Attributes()), new ElementsElement("child2Uri", new Attributes())};
 parentNode.addChildren(children);
 assertEquals(2, parentNode.childNodes().size());
 }
}