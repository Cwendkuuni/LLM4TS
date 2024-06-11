package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

 private static Node createNode(String baseUri, Attributes attributes) {
 return new AbstractNode() {
 @Override
 public String nodeName() {
 throw new UnsupportedOperationException();
 }
 }.getInstance(baseUri, attributes);
 }

 @Test
 public void testCreateNode() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 Node node = createNode(baseUri, attributes);

 assertEquals("http://example.com", node.baseUri());
 assertEquals("test", node.attr("id"));
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testCreateNodeWithEmptyAttributes() {
 String baseUri = "http://example.com";
 Node node = createNode(baseUri);

 assertEquals("", node.attr(""));
 }

 @Test
 public void testGetAttributeWithMissingAttribute() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node node = createNode(baseUri, attributes);

 assertEquals("", node.attr("nonExistingAttribute"));
 }

 @Test
 public void testGetAttributeWithAbsUrlAttribute() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("abs:href", "/test.html");
 Node node = createNode(baseUri, attributes);

 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 @Test
 public void testGetAttributeWithRelativeUrlAttribute() {
 String baseUri = "http://example.com/page1.html";
 Attributes attributes = new Attributes();
 attributes.put("href", "./page2.html");
 Node node = createNode(baseUri, attributes);

 assertEquals("http://example.com/page2.html", node.absUrl("href"));
 }

 @Test
 public void testGetAttributeWithMalformedUrlAttribute() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("bad:url", "//example.com/test.html");
 Node node = createNode(baseUri, attributes);

 assertEquals("", node.absUrl("bad:url"));
 }

 @Test
 public void testGetChildNode() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node childNode = new Elements.TextElement("child", parentNode.attributes());
 parentNode.addChild(childNode);

 Node node = parentNode.childNode(0);

 assertEquals("child", node.nodeName());
 assertEquals(parentNode, node.parent());
 }

 @Test
 public void testChildren() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node childNode1 = new Elements.TextElement("child1", parentNode.attributes());
 parentNode.addChild(childNode1);
 Node childNode2 = new Elements.TextElement("child2", parentNode.attributes());
 parentNode.addChild(childNode2);

 List<Node> children = parentNode.childNodes();

 assertEquals(2, children.size());
 assertEquals(childNode1, children.get(0));
 assertEquals(childNode2, children.get(1));
 }

 @Test
 public void testRemoveChild() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node childNode1 = new Elements.TextElement("child1", parentNode.attributes());
 parentNode.addChild(childNode1);
 Node childNode2 = new Elements.TextElement("child2", parentNode.attributes());
 parentNode.addChild(childNode2);

 parentNode.removeChild(childNode1);

 List<Node> children = parentNode.childNodes();

 assertEquals(1, children.size());
 assertNull(children.get(0).attr("id")); // child1 had id="child1"
 }

 @Test
 public void testReplaceChild() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node childNode1 = new Elements.TextElement("child1", parentNode.attributes());
 parentNode.addChild(childNode1);
 Node childNode2 = new Elements.TextElement("child2", parentNode.attributes());

 parentNode.replaceChild(childNode1, childNode2);

 List<Node> children = parentNode.childNodes();

 assertEquals(1, children.size());
 assertEquals(childNode2, children.get(0));
 }

 @Test
 public void testGetParentNode() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node childNode = createNode(baseUri, attributes);
 Node parentNode = new Elements.DivElement("parent", attributes);
 parentNode.addChild(childNode);

 Node node = childNode.parent();

 assertEquals("parent", node.nodeName());
 assertEquals(parentNode, node);
 }

 @Test
 public void testGetOwnerDocument() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node node = createNode(baseUri, attributes);

 Document document = node.ownerDocument();

 assertEquals(baseUri, document.baseUri());
 }

 @Test
 public void testRemove() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node childNode = new Elements.TextElement("child", parentNode.attributes());
 parentNode.addChild(childNode);

 childNode.remove();

 List<Node> children = parentNode.childNodes();

 assertEquals(0, children.size());
 }

 @Test
 public void testReplaceWith() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node oldNode = new Elements.TextElement("old", parentNode.attributes());
 parentNode.addChild(oldNode);
 Node newNode = new Elements.TextElement("new", parentNode.attributes());

 parentNode.replaceWith(newNode);

 List<Node> children = parentNode.childNodes();

 assertEquals(1, children.size());
 assertEquals(newNode, children.get(0));
 assertNull(oldNode.parent());
 }

 @Test
 public void testSetParentNode() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node childNode = createNode(baseUri, attributes);
 Node parentNode = new Elements.DivElement("parent", attributes);

 childNode.setParentNode(parentNode);
 parentNode.addChild(childNode);

 assertEquals(parentNode, childNode.parent());
 }

 @Test
 public void testSetSiblingIndex() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = createNode(baseUri, attributes);
 Node siblingNode1 = new Elements.TextElement("sibling1", parentNode.attributes());
 parentNode.addChild(siblingNode1);
 Node siblingNode2 = new Elements.TextElement("sibling2", parentNode.attributes());
 parentNode.addChild(siblingNode2);
 Node node = new Elements.TextElement("node", parentNode.attributes());

 siblingNode1.setSiblingIndex(1);
 siblingNode2.setSiblingIndex(2);
 node.setSiblingIndex(0);

 List<Node> children = parentNode.childNodes();

 assertEquals(3, children.size());
 assertEquals(node, children.get(0));
 assertEquals(siblingNode1, children.get(1));
 assertEquals(siblingNode2, children.get(2));
 }

 @Test
 public void testOuterHtml() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "test");
 Node node = new Elements.DivElement("div", attributes);

 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);

 assertEquals("<div class=\"test\">", accum.toString());
 }

 @Test
 public void testOuterHtmlWithAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 attributes.put("class", "test");
 Node node = new Elements.DivElement("div", attributes);

 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);

 assertEquals("<div id=\"test\" class=\"test\">", accum.toString());
 }

 @Test
 public void testOuterHtmlWithNestedNodes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Attributes childAttributes = new Attributes();
 childAttributes.put("class", "child");
 Node parentNode = new Elements.DivElement("div", attributes);
 Node childNode = new Elements.DivElement("div", childAttributes);
 parentNode.addChild(childNode);

 StringBuilder accum = new StringBuilder();
 parentNode.outerHtml(accum);

 assertEquals("<div>\n<div class=\"child\"></div>\n</div>", accum.toString());
 }

 @Test
 public void testOuterHtmlWithDocumentSettings() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.setPrettyPrint(true);
 settings.setIndentAmount(4);
 Node node = new Elements.DivElement("div", attributes);

 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum, 0, settings);

 assertEquals("<div>\n</div>", accum.toString());
 }

 @Test
 public void testToString() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node node = new Elements.DivElement("div", attributes);

 assertEquals("<div>", node.toString());
 }

}