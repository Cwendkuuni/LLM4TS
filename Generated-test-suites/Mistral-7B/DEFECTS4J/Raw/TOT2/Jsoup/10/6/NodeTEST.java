import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 Validate.notNull(node);
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.baseUri());
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 }

 @Test
 public void testNodeWithAttributes() {
 Node node = new Element("img").attr("src", "test.png");
 Validate.notNull(node);
 assertEquals("img", node.nodeName());
 assertEquals("test.png", node.attr("src"));
 assertNull(node.parentNode());
 assertNull(node.baseUri());
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 assertNotNull(node.attributes());
 assertEquals(1, node.attributes().size());
 }

 @Test
 public void testNodeWithBaseUri() {
 Node node = new Element("a").attr("href", "test.html").baseUri("http://example.com");
 Validate.notNull(node);
 assertEquals("a", node.nodeName());
 assertEquals("http://example.comtest.html", node.absUrl("href"));
 assertNull(node.parentNode());
 assertNotNull(node.baseUri());
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 assertNotNull(node.attributes());
 assertEquals(1, node.attributes().size());
 }

 @Test
 public void testNodeWithParent() {
 Document doc = new Document();
 Node parent = doc.createElement("ul");
 Node node = doc.createElement("li");
 parent.appendChild(node);
 Validate.notNull(parent);
 Validate.notNull(node);
 assertEquals("ul", parent.nodeName());
 assertEquals("li", node.nodeName());
 assertSame(parent, node.parentNode());
 assertNull(node.baseUri());
 assertSame(Collections.EMPTY_LIST, parent.childNodes());
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 assertNotNull(parent.attributes());
 assertEquals(0, parent.attributes().size());
 }

 @Test
 public void testNodeWithChildren() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").text("Item 1");
 Node child2 = new Element("li").text("Item 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 Validate.notNull(parent);
 Validate.notNull(child1);
 Validate.notNull(child2);
 assertEquals("ul", parent.nodeName());
 assertEquals(2, parent.childNodes().size());
 assertEquals("li", child1.nodeName());
 assertEquals("Item 1", child1.outerHtml());
 assertEquals("li", child2.nodeName());
 assertEquals("Item 2", child2.outerHtml());
 assertSame(parent, child1.parentNode());
 assertSame(parent, child2.parentNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 new Element("a").baseUri(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributeName() {
 new Element("img").attr(null, "test.png");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributeValue() {
 new Element("img").attr("src", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyStringBaseUri() {
 new Element("a").baseUri("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyListChildren() {
 Node node = new Element("li");
 node.childNodes(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndexChild() {
 Node parent = new Element("ul");
 Node child = new Element("li");
 parent.appendChild(child);
 parent.removeChild(-1);
 }

 @Test
 public void testRemoveChild() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").text("Item 1");
 Node child2 = new Element("li").text("Item 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 parent.removeChild(child1);
 Validate.notNull(parent);
 Validate.notNull(child1);
 Validate.notNull(child2);
 assertEquals(1, parent.childNodes().size());
 assertSame(child2, parent.childNodes().get(0));
 assertNull(child1.parentNode());
 }

 @Test
 public void testAddChildren() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").text("Item 1");
 Node child2 = new Element("li").text("Item 2");
 parent.addChildren(child1, child2);
 Validate.notNull(parent);
 Validate.notNull(child1);
 Validate.notNull(child2);
 assertEquals(2, parent.childNodes().size());
 assertSame(child1, parent.childNodes().get(0));
 assertSame(child2, parent.childNodes().get(1));
 }

 @Test
 public void testAddChildrenWithIndex() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").text("Item 1");
 Node child2 = new Element("li").text("Item 2");
 parent.addChildren(1, child1, child2);
 Validate.notNull(parent);
 Validate.notNull(child1);
 Validate.notNull(child2);
 assertEquals(2, parent.childNodes().size());
 assertSame(child1, parent.childNodes().get(0));
 assertSame(child2, parent.childNodes().get(1));
 }

 @Test
 public void testGetTextFromTextNode() {
 Node textNode = new TextNode("Test Text");
 assertEquals("Test Text", textNode.outerHtml());
 assertEquals("Test Text", textNode.toString());
 assertEquals("Test Text", textNode.text());
 }

 @Test
 public void testGetTextFromElementWithoutChildren() {
 Node element = new Element("p");
 assertEquals("", element.outerHtml());
 assertEquals("", element.toString());
 assertEquals("", element.text());
 }

 @Test
 public void testGetTextFromElementWithChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p").text("Paragraph 1");
 Node child2 = new Element("p").text("Paragraph 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 assertEquals("Paragraph 1\nParagraph 2", parent.outerHtml());
 assertEquals("Paragraph 1\nParagraph 2", parent.toString());
 assertEquals("Paragraph 1\nParagraph 2", parent.text());
 }

 @Test
 public void testCloneNode() {
 Node parent = new Element("ul");
 Node child1 = new Element("li").text("Item 1");
 Node child2 = new Element("li").text("Item 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 Node clonedParent = parent.clone();
 Validate.notNull(clonedParent);
 Validate.notNull(child1);
 Validate.notNull(child2);
 assertEquals(2, clonedParent.childNodes().size());
 assertSame(child1, clonedParent.childNodes().get(0));
 assertSame(child2, clonedParent.childNodes().get(1));
 }

 @Test
 public void testCloneNodeWithParent() {
 Document doc = new Document();
 Node parent = doc.createElement("ul");
 Node child1 = doc.createElement("li").text("Item 1");
 Node child2 = doc.createElement("li").text("Item 2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 Node clonedChild1 = child1.cloneNode(true);
 Node clonedChild2 = child2.cloneNode(true);
 parent.removeChild(child1);
 parent.removeChild(child2);
 parent.appendChild(clonedChild1);
 parent.appendChild(clonedChild2);
 Validate.notNull(parent);
 Validate.notNull(child1);
 Validate.notNull(child2);
 Validate.notNull(clonedChild1);
 Validate.notNull(clonedChild2);
 assertEquals(2, parent.childNodes().size());
 assertSame(clonedChild1, parent.childNodes().get(0));
 assertSame(clonedChild2, parent.childNodes().get(1));
 }
}