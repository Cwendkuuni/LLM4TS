package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeCreationWithoutBaseUri() {
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", attributes);
        assertEquals("div", node.nodeName());
        assertNull(node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeCreationWithNullAttributes() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, null);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeCreationWithEmptyList() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeCreationWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node child1 = new TextNode("Child 1");
        Node child2 = new TextNode("Child 2");
        Node node = new Element("div", baseUri, attributes, new Node[]{child1, child2});
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNotNull(node.parentNode());
        List<Node> childNodes = node.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child1, childNodes.get(0));
        assertEquals(child2, childNodes.get(1));
    }

    @Test
    public void testNodeCreationWithNullChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes, null);
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "test-class");
        Node node = new Element("div", baseUri, attributes);
        assertEquals("test", node.attr("id"));
        assertEquals("test-class", node.attr("class"));
        assertEquals("", node.attr("non-existent"));
        assertEquals("", node.attr("abs:href"));
    }

    @Test
    public void testAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "test-class");
        Node node = new Element("div", baseUri, attributes);
        assertEquals(attributes, node.attributes());
        assertEquals("test", node.attributes().get("id"));
        assertEquals("test-class", node.attributes().get("class"));
        assertFalse(node.attributes().hasKey("non-existent"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Node node = new Element("div", baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non-existent"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "test-class");
        Node node = new Element("div", baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String href = "relative.html";
        Node node = new Element("a", baseUri, new Attributes().put("href", href));
        assertEquals("", node.absUrl("non-existent"));
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrlAbsUrl() {
        String baseUri = "http://example.com";
        String invalidUrl = "invalid:/relative.html";
        Node node = new Element("a", baseUri, new Attributes().put("href", invalidUrl));
        node.absUrl("href");
    }

    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child");
        Node child = new Element("p", baseUri, attributes);
        parent.addChild(child);
        assertEquals(child, parent.childNode(0));
        assertEquals(child, parent.childNodes().get(0));
        assertEquals(child, parent.childNodesAsArray()[0]);
    }

    @Test
    public void testChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child1");
        Node child1 = new Element("p", baseUri, attributes);
        attributes = new Attributes();
        attributes.put("id", "child2");
        Node child2 = new Element("p", baseUri, attributes);
        parent.addChildren(new Node[]{child1, child2});
        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        Node[] childrenArray = parent.childNodesAsArray();
        assertEquals(2, childrenArray.length);
        assertEquals(child1, childrenArray[0]);
        assertEquals(child2, childrenArray[1]);
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child1");
        Node child1 = new Element("p", baseUri, attributes);
        attributes = new Attributes();
        attributes.put("id", "child2");
        Node child2 = new Element("p", baseUri, attributes);
        parent.addChildren(new Node[]{child1, child2});
        parent.removeChild(child1);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));
        Node[] childrenArray = parent.childNodesAsArray();
        assertEquals(1, childrenArray.length);
        assertEquals(child2, childrenArray[0]);
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child1");
        Node child1 = new Element("p", baseUri, attributes);
        attributes = new Attributes();
        attributes.put("id", "child2");
        Node child2 = new Element("p", baseUri, attributes);
        parent.addChildren(new Node[]{child1, child2});
        parent.replaceChild(child1, child2);
        List<Node> children = parent.childNodes();
        assertEquals(1, children.size());
        assertEquals(child2, children.get(0));
        Node[] childrenArray = parent.childNodesAsArray();
        assertEquals(1, childrenArray.length);
        assertEquals(child2, childrenArray[0]);
    }

    @Test
    public void testParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "child");
        Node child = new Element("p", baseUri, attributes);
        attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.singletonList(child));
        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child");
        Node child = new Element("p", baseUri, attributes);
        parent.addChild(child);
        assertEquals(parent, child.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child");
        Node child = new Element("p", baseUri, attributes);
        parent.addChild(child);
        parent.remove();
        assertEquals(null, child.parent());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "parent");
        Node parent = new Element("div", baseUri, attributes, Collections.EMPTY_LIST);
        attributes = new Attributes();
        attributes.put("id", "child1");
        Node child1 = new Element("p", baseUri, attributes);
        attributes = new Attributes();
        attributes.put("id", "child2");
        Node child2 = new Element("p", baseUri, attributes);
        parent.addChildren(new Node[]{child1, child2});
        Node replacedChild = child1;
        Node newChild = new Element("p", baseUri, new Attributes().put("id", "newChild"));
        parent.replaceChild(child1, newChild);
        assertEquals(newChild, child1.parent());
        assertEquals(newChild, parent.childNode(0));
        assertEquals(newChild, parent.childNodes().get(0));
        assertEquals(newChild, parent.childNodesAsArray()[0]);
        assertEquals(replacedChild, parent.childNode(1));
        assertEquals(replacedChild, parent.childNodes().get(1));
        assertEquals(replacedChild, parent.childNodesAsArray()[1]);
    }
}