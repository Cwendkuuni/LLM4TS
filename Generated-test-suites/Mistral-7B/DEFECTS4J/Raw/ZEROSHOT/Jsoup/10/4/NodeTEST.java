package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

 @Test
 public void testNewInstance() {
 Node node = new Element("div");
 Validate.notNull(node.parentNode);
 Validate.notNull(node.attributes);
 assertEquals("", node.baseUri());
 assertFalse(node.hasAttr("attr"));
 }

 @Test
 public void testNewInstanceWithAttributes() {
 Node node = new Element("div", new Attributes().put("attr", "value"));
 Validate.notNull(node.parentNode);
 Validate.notNull(node.attributes);
 assertEquals("value", node.attr("attr"));
 }

 @Test
 public void testEmptyConstructor() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 }

 @Test
 public void testNodeName() {
 Element div = new Element("div");
 assertEquals("div", div.nodeName());
 TextNode textNode = new TextNode("text");
 assertEquals("text", textNode.nodeName());
 }

 @Test
 public void testGetAttribute() {
 Element div = new Element("div", new Attributes().put("attr", "value"));
 assertEquals("value", div.attr("attr"));
 }

 @Test
 public void testGetAttributeAbsUrl() {
 Element a = new Element("a", new Attributes().put("href", "link"));
 assertEquals("", a.absUrl("href"));
 assertEquals("link", a.absUrl("href"));
 }

 @Test
 public void testGetAllAttributes() {
 Element div = new Element("div", new Attributes().put("attr1", "value1").put("attr2", "value2"));
 Attributes attributes = div.attributes();
 assertTrue(attributes.size() == 2);
 assertEquals("value1", attributes.get("attr1"));
 assertEquals("value2", attributes.get("attr2"));
 }

 @Test
 public void testSetAttribute() {
 Element div = new Element("div");
 div.attr("attr", "value");
 assertEquals("value", div.attr("attr"));
 }

 @Test
 public void testHasAttribute() {
 Element div = new Element("div", new Attributes().put("attr", "value"));
 assertTrue(div.hasAttr("attr"));
 assertFalse(div.hasAttr("nonexistent"));
 }

 @Test
 public void testRemoveAttribute() {
 Element div = new Element("div", new Attributes().put("attr", "value"));
 div.removeAttr("attr");
 assertFalse(div.hasAttr("attr"));
 }

 @Test
 public void testBaseUri() {
 Element div = new Element("div", new Attributes());
 div.setBaseUri("http://localhost/test");
 assertEquals("http://localhost/test", div.baseUri());
 }

 @Test
 public void testGetChildNodeByIndex() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 @Test
 public void testGetChildren() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testGetChildrenAsArray() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 Node[] children = parent.childNodesAsArray();
 assertEquals(2, children.length);
 assertEquals(child1, children[0]);
 assertEquals(child2, children[1]);
 }

 @Test
 public void testGetParent() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.addChild(child);
 assertEquals(parent, child.parent());
 assertNull(child.parent());
 }

 @Test
 public void testGetDocument() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.addChild(child);
 Document document = new Document();
 parent.setParentDocument(document);
 assertEquals(document, child.ownerDocument());
 assertNull(new Element("p").ownerDocument());
 }

 @Test
 public void testRemove() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.addChild(child);
 parent.removeChild(child);
 assertFalse(parent.hasChild(child));
 }

 @Test
 public void testReplaceWith() {
 Element parent = new Element("div");
 Element oldChild = new Element("p");
 Element newChild = new Element("p");
 parent.addChild(oldChild);
 parent.replaceChild(oldChild, newChild);
 assertFalse(parent.hasChild(oldChild));
 assertTrue(parent.hasChild(newChild));
 }

 @Test
 public void testSetParentNode() {
 Element child = new Element("p");
 Element parent1 = new Element("div");
 Element parent2 = new Element("div");
 child.setParentNode(parent1);
 parent1.addChild(child);
 child.setParentNode(parent2);
 parent2.addChild(child);
 assertEquals(parent2, child.parent());
 }

 @Test
 public void testReplaceChild() {
 Element parent = new Element("div");
 Element out = new Element("p");
 Element in = new Element("p");
 parent.addChild(out);
 parent.replaceChild(out, in);
 assertEquals(in, parent.childNode(0));
 assertFalse(parent.hasChild(out));
 }

 @Test
 public void testAddChildren() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNodes().get(0));
 assertEquals(child2, parent.childNodes().get(1));
 }

 @Test
 public void testAddChildrenWithIndex() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(1, child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNodes().get(0));
 assertEquals(child2, parent.childNodes().get(1));
 }

 @Test
 public void testSiblingNodes() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element sibling2 = new Element("p");
 Element child = new Element("p");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(3, parent.childNodes().size());
 List<Node> siblings = parent.siblingNodes();
 assertEquals(2, siblings.size());
 assertEquals(sibling1, siblings.get(0));
 assertEquals(sibling2, siblings.get(1));
 }

 @Test
 public void testNextSibling() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element child = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(sibling2, child.nextSibling());
 assertNull(child.previousSibling());
 }

 @Test
 public void testPreviousSibling() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element child = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(sibling1, child.previousSibling());
 assertNull(child.nextSibling());
 }

 @Test
 public void testOuterHtml() {
 Element div = new Element("div");
 TextNode textNode1 = new TextNode("Hello");
 Element p = new Element("p");
 p.outerHtmlHead(null, 0, null);
 p.outerHtmlTail(null, 0, null);
 p.outerHtml("<span>Text</span>", 0, null);
 div.addChildren(textNode1, p);
 assertEquals("<div><text></text><p><span>Text</span></p></div>", div.outerHtml());
 }

 @Test
 public void testToString() {
 Element div = new Element("div");
 TextNode textNode1 = new TextNode("Hello");
 Element p = new Element("p");
 div.addChildren(textNode1, p);
 assertEquals("<div><text></text><p></p></div>", div.toString());
 }

 @Test
 public void testEquals() {
 Element div1 = new Element("div");
 Element div2 = new Element("div");
 assertEquals(div1, div1);
 assertNotEquals(div1, div2);
 }

 @Test
 public void testHashCode() {
 Element div1 = new Element("div");
 Element div2 = new Element("div");
 Element div3 = new Element("div");
 Element div4 = new Element("div");
 assertEquals(div1.hashCode(), div1.hashCode());
 assertNotEquals(div1.hashCode(), div2.hashCode());
 assertEquals(div1.hashCode(), div1.clone().hashCode());
 assertEquals(div1.hashCode(), div3.hashCode());
 assertEquals(div1.hashCode(), div4.hashCode());
 }

 @Test
 public void testClone() {
 Element div = new Element("div");
 TextNode textNode1 = new TextNode("Hello");
 Element p = new Element("p");
 div.addChildren(textNode1, p);
 Element cloned = div.clone();
 assertEquals(div, cloned);
 assertEquals(div.childNodes(), cloned.childNodes());
 }
}