package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node.
 */
public class NodeTest {

 private Node node;
 private Document doc;

 @Before
 public void setUp() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "test");
 node = new Node(baseUri, attributes);
 doc = new Document();
 doc.setBaseUri(baseUri);
 doc.appendChild(node);
 }

 /**
  * Test method for Node.nodeName().
  */
 @Test
 public void testNodeName() {
 String nodeName = node.nodeName();
 // Replace this with an appropriate assertion.
 assertEquals("Node name should be implemented in subclasses", "", nodeName);
 }

 /**
  * Test method for Node.attr().
  */
 @Test
 public void testAttr() {
 String attributeKey = "test";
 String attributeValue = "value";
 node.attr(attributeKey, attributeValue);
 String attrValue = node.attr(attributeKey);
 // Replace this with an appropriate assertion.
 assertEquals("Attribute value should be set correctly", attributeValue, attrValue);
 }

 /**
  * Test method for Node.hasAttr().
  */
 @Test
 public void testHasAttr() {
 assertTrue("Node should have the specified attribute", node.hasAttr(attributeKey));
 }

 /**
  * Test method for Node.removeAttr().
  */
 @Test
 public void testRemoveAttr() {
 node.removeAttr(attributeKey);
 assertFalse("Node should no longer have the specified attribute", node.hasAttr(attributeKey));
 }

 /**
  * Test method for Node.baseUri().
  */
 @Test
 public void testBaseUri() {
 String baseUri = node.baseUri();
 // Replace this with an appropriate assertion.
 assertEquals("Base URI should be set correctly", baseUri, node.baseUri());
 }

 /**
  * Test method for Node.setBaseUri().
  */
 @Test
 public void testSetBaseUri() {
 String newBaseUri = "http://newexample.com";
 node.setBaseUri(newBaseUri);
 String newBaseUriResult = node.baseUri();
 // Replace this with an appropriate assertion.
 assertEquals("Base URI should be updated correctly", newBaseUri, newBaseUriResult);
 }

 /**
  * Test method for Node.absUrl().
  */
 @Test
 public void testAbsUrl() {
 String relativeUrl = "relative.html";
 node.attr("href", relativeUrl);
 String absUrl = node.absUrl("href");
 // Replace this with an appropriate assertion.
 assertEquals("Absolute URL should be generated correctly", "http://example.com/relative.html", absUrl);
 }

 /**
  * Test method for Node.childNode().
  */
 @Test
 public void testChildNode() {
 Node childNode = new Node(baseUri, new Attributes());
 node.appendChild(childNode);
 Node childNodeFromNode = node.childNode(0);
 Node childNodeFromList = node.childNodes().get(0);
 // Replace this with an appropriate assertion.
 assertEquals("Child nodes should be the same", childNode, childNodeFromNode);
 assertEquals("Child nodes should be the same", childNode, childNodeFromList);
 }

 /**
  * Test method for Node.childNodes().
  */
 @Test
 public void testChildNodes() {
 List<Node> childNodesList = node.childNodes();
 // Replace this with an appropriate assertion.
 assertEquals("Number of child nodes should be 1", 1, childNodesList.size());
 }

 /**
  * Test method for Node.remove().
  */
 @Test
 public void testRemove() {
 List<Node> childrenBeforeRemove = new ArrayList<>(node.childNodes());
 node.remove();
 List<Node> childrenAfterRemove = new ArrayList<>(doc.childNodes());
 // Replace this with an appropriate assertion.
 assertEquals("Node should no longer be in the document", false, childrenAfterRemove.contains(node));
 assertEquals("Number of children in the document should be one less", childrenBeforeRemove.size(), childrenAfterRemove.size()-1);
 }

 /**
  * Test method for Node.replaceWith().
  */
 @Test
 public void testReplaceWith() {
 Node replacementNode = new Node(baseUri, new Attributes());
 node.replaceWith(replacementNode);
 // Replace this with an appropriate assertion.
 assertEquals("Node should be replaced with the new node", replacementNode, node.parentNode().childNode(node.siblingIndex()));
 }

 /**
  * Test method for Node.addChildren().
  */
 @Test
 public void testAddChildren() {
 Node child1 = new Node(baseUri, new Attributes());
 Node child2 = new Node(baseUri, new Attributes());
 node.addChildren(child1, child2);
 List<Node> children = node.childNodes();
 // Replace this with an appropriate assertion.
 assertEquals("Number of children should be 3", 3, children.size());
 assertEquals("First child should be child1", child1, children.get(0));
 assertEquals("Second child should be child2", child2, children.get(1));
 assertEquals("Third child should be the original node", node, children.get(2));
 }

 /**
  * Test method for Node.addChildren(int, Node...).
  */
 @Test
 public void testAddChildrenInt() {
 Node child1 = new Node(baseUri, new Attributes());
 Node child2 = new Node(baseUri, new Attributes());
 node.addChildren(1, child1, child2);
 List<Node> children = node.childNodes();
 // Replace this with an appropriate assertion.
 assertEquals("Number of children should be 3", 3, children.size());
 assertEquals("First child should be child1", child1, children.get(0));
 assertEquals("Second child should be child2", child2, children.get(1));
 assertEquals("Third child should be the original node", node, children.get(2));
 }

 /**
  * Test method for Node.reparentChild().
  */
 @Test
 public void testReparentChild() {
 Node parentNode = new Node(baseUri, new Attributes());
 Node child = new Node(baseUri, new Attributes());
 doc.appendChild(parentNode);
 doc.appendChild(child);
 node.reparentChild(child);
 List<Node> children = node.childNodes();
 // Replace this with an appropriate assertion.
 assertEquals("Child should be a child of the new parent", node, child.parentNode());
 assertEquals("New parent should have the child as a child", child, node.childNode(node.childNodes().size()-1));
 List<Node> siblings = node.siblingNodes();
 // Replace this with an appropriate assertion.
 assertEquals("Child should have been removed from old parent", false, node.parentNode().childNodes().contains(child));
 assertEquals("Number of children of the old parent should be one less", 1, node.parentNode().childNodes().size());
 assertEquals("Number of siblings should be the same", siblings.size(), node.siblingNodes().size());
 }

 /**
  * Test method for Node.reindexChildren().
  */
 @Test
 public void testReindexChildren() {
 Node node1 = new Node(baseUri, new Attributes());
 Node node2 = new Node(baseUri, new Attributes());
 Node node3 = new Node(baseUri, new Attributes());
 Node node4 = new Node(baseUri, new Attributes());
 node.addChildren(node1, node2, node3, node4);
 node.reindexChildren();
 List<Node> siblings = node.siblingNodes();
 // Replace this with an appropriate assertion.
 assertEquals("First sibling should be node1", node1, siblings.get(0));
 assertEquals("Second sibling should be node2", node2, siblings.get(1));
 assertEquals("Third sibling should be node3", node3, siblings.get(2));
 assertEquals("Fourth sibling should be node4", node4, siblings.get(3));
 }

 /**
  * Test method for Node.nextSibling().
  */
 @Test
 public void testNextSibling() {
 Node nextSibling = new Node(baseUri, new Attributes());
 node.appendChild(nextSibling);
 Node nextSiblingFromNode = node.nextSibling();
 Node nextSiblingFromList = node.siblingNodes().get(node.siblingIndex()+1);
 // Replace this with an appropriate assertion.
 assertEquals("Next sibling should be the next sibling node", nextSibling, nextSiblingFromNode);
 assertEquals("Next sibling should be the next sibling node", nextSibling, nextSiblingFromList);
 }

 /**
  * Test method for Node.previousSibling().
  */
 @Test
 public void testPreviousSibling() {
 Node previousSibling = new Node(baseUri, new Attributes());
 node.appendChild(previousSibling);
 Node previousSiblingFromNode = node.previousSibling();
 Node previousSiblingFromList = node.siblingNodes().get(node.siblingIndex()-1);
 // Replace this with an appropriate assertion.
 assertEquals("Previous sibling should be the previous sibling node", previousSibling, previousSiblingFromNode);
 assertEquals("Previous sibling should be the previous sibling node", previousSibling, previousSiblingFromList);
 }

 /**
  * Test method for Node.siblingIndex().
  */
 @Test
 public void testSiblingIndex() {
 List<Node> siblings = node.siblingNodes();
 // Replace this with an appropriate assertion.
 assertEquals("Sibling index should be 0", 0, node.siblingIndex());
 }

 /**
  * Test method for Node.outerHtml().
  */
 @Test
 public void testOuterHtml() {
 StringBuilder accum = new StringBuilder(32*1024);
 node.outerHtml(accum);
 String outerHtml = accum.toString();
 // Replace this with an appropriate assertion.
 assertEquals("Outer HTML should be generated correctly", "<html><head></head><body><node class=\"test\"></node></body></html>", outerHtml);
 }
}