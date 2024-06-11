package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

private Node rootNode;
private Node elementNode;
private Node attributeNode;

@Before
public void setup() {
rootNode = new Document(); // creates a root node
elementNode = rootNode.createElement("div");
attributeNode = elementNode.createAttribute("class", "test");
elementNode.attr("class", "test"); // sets the attribute using the attr() method
elementNode.attr("id", "test-id"); // sets another attribute
}

@Test
public void testNodeName() {
assertEquals("div", elementNode.nodeName());
}

@Test
public void testAttr() {
assertEquals("test", elementNode.attr("class"));
assertEquals("test-id", elementNode.attr("id"));
assertEquals("", elementNode.attr("nonexistent"));
assertEquals("", elementNode.attr("abs:nonexistent"));
}

@Test
public void testAttributes() {
Attributes attributes = elementNode.attributes();
assertEquals("class", attributes.keySet().iterator().next());
assertEquals("test", attributes.get("class"));
assertEquals("id", attributes.keySet().iterator().next());
assertEquals("test-id", attributes.get("id"));
assertEquals(0, attributes.size());
}

@Test
public void testHasAttr() {
assertTrue(elementNode.hasAttr("class"));
assertTrue(elementNode.hasAttr("id"));
assertFalse(elementNode.hasAttr("nonexistent"));
}

@Test
public void testRemoveAttr() {
elementNode.removeAttr("class");
assertFalse(elementNode.hasAttr("class"));
}

@Test
public void testBaseUri() {
assertEquals("", rootNode.baseUri());
elementNode.setBaseUri("http://example.com");
assertEquals("http://example.com", elementNode.baseUri());
}

@Test
public void testAbsUrl() {
assertEquals("", elementNode.absUrl(""));
assertEquals("http://example.com/div.html", elementNode.absUrl("href"));
assertEquals("http://example.com/img.png", elementNode.absUrl("src"));
assertEquals("http://example.com/div.html#fragment", elementNode.absUrl("abs:href"));
assertEquals("", elementNode.absUrl("nonexistent"));
}

@Test
public void testChildNodes() {
assertEquals(0, elementNode.childNodes().size());
elementNode.appendChild(rootNode.createTextNode("Hello World"));
assertEquals(1, elementNode.childNodes().size());
assertEquals("Hello World", elementNode.childNode(0).toString());
}

@Test
public void testParent() {
assertNull(elementNode.parent());
elementNode.setParentNode(rootNode);
assertEquals(rootNode, elementNode.parent());
}

@Test(expected = IllegalStateException.class)
public void testInvalidConstructorUsage() {
// Create a Node instance without the required parameters
new Node();
}
}