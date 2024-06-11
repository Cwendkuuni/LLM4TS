import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class NodeTest {

// Test case for constructor with base URI and attributes
@Test
public void testNodeWithBaseUriAndAttributes() {
Node node = new Element("div");
node.setBaseUri("http://example.com");
Attributes attributes = new Attributes();
attributes.put("class", "example");
node.setAttributes(attributes);

assertEquals("div", node.nodeName());
assertEquals("http://example.com", node.baseUri());
assertEquals("class=example", node.outerHtml());
}

// Test case for constructor without base URI and attributes
@Test
public void testNodeWithoutBaseUriAndAttributes() {
Node node = new Element("div");
assertEquals("div", node.nodeName());
assertEquals(null, node.baseUri());
assertEquals("", node.outerHtml());
}

// Test case for default constructor
@Test
public void testDefaultConstructor() {
Node node = new Node();
assertEquals(Node.class, node.getClass());
assertEquals(null, node.nodeName());
assertEquals(null, node.baseUri());
assertEquals(Collections.EMPTY_LIST, node.childNodes());
assertEquals(null, node.attributes());
}

// Test case for getAttribute method
@Test
public void testGetAttribute() {
Element element = new Element("a");
element.setBaseUri("http://example.com");
element.attributes().put("href", "/test");
assertEquals("/test", element.attr("href"));
assertEquals(null, element.attr("nonexistent"));
}

// Test case for getAttributes method
@Test
public void testGetAttributes() {
Element element = new Element("div");
Attributes attributes = new Attributes();
attributes.put("class", "example");
attributes.put("id", "test");
element.setAttributes(attributes);
assertEquals("class=example id=test", element.outerHtml());
}

// Test case for setAttribute method
@Test
public void testSetAttribute() {
Element element = new Element("a");
element.setAttribute("href", "/test");
assertEquals("/test", element.attr("href"));
}

// Test case for hasAttribute method
@Test
public void testHasAttribute() {
Element element = new Element("a");
element.attributes().put("href", "/test");
assertTrue(element.hasAttr("href"));
assertFalse(element.hasAttr("nonexistent"));
}

// Test case for removeAttribute method
@Test
public void testRemoveAttribute() {
Element element = new Element("a");
element.attributes().put("href", "/test");
element.removeAttribute("href");
assertFalse(element.hasAttr("href"));
}

// Test case for absUrl method with absolute URL
@Test
public void testAbsUrlWithAbsoluteUrl() {
Element element = new Element("a");
element.attributes().put("href", "http://example.com/test");
assertEquals("http://example.com/test", element.absUrl("href"));
}

// Test case for absUrl method with relative URL
@Test
public void testAbsUrlWithRelativeUrl() {
Element element = new Element("a");
element.attributes().put("href", "/test");
Document document = new Document();
document.setBaseUri("http://example.com");
element.setOwnerDocument(document);
assertEquals("http://example.com/test", element.absUrl("href"));
}

// Test case for absUrl method with missing attribute
@Test
public void testAbsUrlWithMissingAttribute() {
Element element = new Element("a");
assertEquals("", element.absUrl("nonexistent"));
}

// Test case for getChildNode method
@Test
public void testGetChildNode() {
Element parent = new Element("div");
Element child = new Element("p");
parent.addChild(child);
assertEquals(child, parent.childNode(0));
}

// Test case for getChildNodes method
@Test
public void testGetChildNodes() {
Element parent = new Element("div");
Element child1 = new Element("p");
Element child2 = new Element("p");
parent.addChildren(child1, child2);
assertEquals(2, parent.childNodes().size());
assertEquals(child1, parent.childNode(0));
assertEquals(child2, parent.childNode(1));
}

// Test case for setParentNode method
@Test
public void testSetParentNode() {
Element child = new Element("p");
Element parent = new Element("div");
child.setParentNode(parent);
assertEquals(parent, child.parent());
}

// Test case for remove method
@Test
public void testRemove() {
Element parent = new Element("div");
Element child = new Element("p");
parent.addChild(child);
parent.removeChild(child);
assertEquals(0, parent.childNodes().size());
}

// Test case for replaceWith method
@Test
public void testReplaceWith() {
Element parent = new Element("div");
Element oldChild = new Element("p");
Element newChild = new Element("p");
parent.addChild(oldChild);
parent.replaceWith(newChild, oldChild);
assertEquals(newChild, parent.childNode(0));
}

// Test case for reindexChildren method
@Test
public void testReindexChildren() {
Element parent = new Element("div");
Element child1 = new Element("p");
Element child2 = new Element("p");
parent.addChildren(child1, child2);
parent.reindexChildren();
assertEquals(0, child1.siblingIndex());
assertEquals(1, child2.siblingIndex());
}
}