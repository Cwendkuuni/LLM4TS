package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

 @Test
 public void testAbstractNodeConstructor() {
 Node node = new Node("", new Attributes());
 assertEquals("", node.baseUri());
 assertEquals(new Attributes(), node.attributes());
 assertNull(node.parentNode);
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 }

 @Test
 public void testNodeConstructorWithBaseUri() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testNodeConstructorWithoutBaseUri() {
 Node node = new Node();
 assertNull(node.baseUri());
 }

 @Test
 public void testNodeAbstractMethods() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertFalse(node.hasAttr("class"));
 assertNull(node.attr("class"));
 }

 @Test
 public void testAttrMethodWithExistingAttribute() {
 Element element = new Element("a").attr("href", "http://example.com");
 assertEquals("http://example.com", element.attr("href"));
 }

 @Test
 public void testAttrMethodWithNonExistentAttribute() {
 Element element = new Element("a");
 assertEquals("", element.attr("href"));
 }

 @Test
 public void testAttrMethodWithAbsUrl() {
 Element element = new Element("a").attr("abs:href", "http://example.com");
 assertEquals("http://example.com", element.absUrl("href"));
 }

 @Test
 public void testAttributesMethod() {
 Element element = new Element("a").attr("href", "http://example.com").attr("class", "link");
 Attributes attributes = element.attributes();
 assertEquals("href=\"http://example.com\" class=\"link\"", attributes.toString());
 }

 @Test
 public void testRemoveAttrMethod() {
 Element element = new Element("a").attr("href", "http://example.com").attr("class", "link");
 element.removeAttr("class");
 assertEquals("href=\"http://example.com\"", element.attributes().toString());
 }

 @Test
 public void testSetBaseUriMethod() {
 Element element = new Element("a").attr("href", "http://example.com");
 element.setBaseUri("http://test.com");
 assertEquals("http://test.com", element.baseUri());
 assertEquals("http://test.com/http://example.com", element.absUrl("href"));
 }

 @Test
 public void testChildNodeMethod() {
 Element parent = new Element("ul");
 Element child = new Element("li").setText("Test");
 parent.appendChild(child);
 Node node = parent.childNode(0);
 assertSame(child, node);
 }

 @Test
 public void testChildrenMethod() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertSame(child1, children.get(0));
 assertSame(child2, children.get(1));
 }

 @Test
 public void testChildAsArrayMethod() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 Node[] children = parent.childNodesAsArray();
 assertEquals(2, children.length);
 assertSame(child1, children[0]);
 assertSame(child2, children[1]);
 }

 @Test
 public void testParentMethod() {
 Element child = new Element("li").setText("Test");
 Element parent = new Element("ul").appendChild(child);
 assertSame(parent, child.parent());
 }

 @Test
 public void testOwnerDocumentMethod() {
 Element child = new Element("li").setText("Test");
 Document document = new Document();
 document.appendChild(child);
 assertSame(document, child.ownerDocument());
 }

 @Test
 public void testRemoveMethod() {
 Element parent = new Element("ul");
 Element child = new Element("li").setText("Test");
 parent.appendChild(child);
 parent.removeChild(child);
 assertFalse(parent.hasChildNodes());
 }

 @Test
 public void testReplaceWithMethod() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.appendChild(child1);
 parent.replaceChild(child1, child2);
 assertTrue(parent.hasChildNodes());
 assertSame(child2, parent.childNode(0));
 }

 @Test
 public void testSetParentNodeMethod() {
 Element child = new Element("li").setText("Test");
 Element parent1 = new Element("ul");
 Element parent2 = new Element("ol");
 parent1.appendChild(child);
 child.setParentNode(parent2);
 assertSame(parent2, child.parentNode);
 parent2.removeChild(child);
 assertFalse(parent2.hasChildNodes());
 }

 @Test
 public void testReplaceChildMethod() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.appendChild(child1);
 parent.replaceChild(child1, child2);
 assertTrue(parent.hasChildNodes());
 assertSame(child2, parent.childNode(0));
 }

 @Test
 public void testRemoveChildMethod() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 parent.removeChild(child1);
 assertFalse(parent.hasChildNodes());
 assertSame(child2, parent.childNode(0));
 }

 @Test
 public void testAddChildrenMethod() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.addChildren(child1, child2);
 assertTrue(parent.hasChildNodes());
 assertSame(child1, parent.childNodes().get(0));
 assertSame(child2, parent.childNodes().get(1));
 }

 @Test
 public void testAddChildrenMethodWithIndex() {
 Element parent = new Element("ul");
 Element child1 = new Element("li").setText("Test 1");
 Element child2 = new Element("li").setText("Test 2");
 parent.addChildren(1, child1, child2);
 assertTrue(parent.hasChildNodes());
 assertSame(child1, parent.childNodes().get(1));
 assertSame(child2, parent.childNodes().get(2));
 }

 @Test
 public void testOuterHtmlMethod() {
 Element element = new Element("p").setText("Test");
 assertEquals("<p>Test</p>", element.outerHtml());
 }

 @Test
 public void testCloneMethod() {
 Element parent = new Element("ul");
 Element child = new Element("li").setText("Test");
 parent.appendChild(child);
 Node clonedChild = child.clone();
 assertFalse(clonedChild.equals(child));
 assertEquals(child.outerHtml(), clonedChild.outerHtml());
 }

 @Test
 public void testEqualsMethod() {
 Element element1 = new Element("p").setText("Test 1");
 Element element2 = new Element("p").setText("Test 1");
 Element element3 = new Element("p").setText("Test 2");
 assertTrue(element1.equals(element1));
 assertTrue(element1.equals(element2));
 assertFalse(element1.equals(element3));
 }

 @Test
 public void testHashCodeMethod() {
 Element element1 = new Element("p").setText("Test 1");
 Element element2 = new Element("p").setText("Test 1");
 Element element3 = new Element("p").setText("Test 2");
 int hashCode1 = element1.hashCode();
 int hashCode2 = element2.hashCode();
 int hashCode3 = element3.hashCode();
 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 }
}