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
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    private Node node;
    private Document doc;

    @Before
    public void setUp() {
        doc = new Document();
        node = new Element("div", "", doc);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        assertEquals("", node.attr(""));
        node.attr("test", "test");
        assertEquals("test", node.attr("test"));
        assertEquals("", node.attr("nonexistent"));
        node.attr("abs:test", "test");
        assertEquals("test", node.absUrl("test"));
    }

    @Test
    public void testAttributes() {
        Attributes attr = new Attributes();
        attr.put("test1", "test1");
        attr.put("test2", "test2");

        node.attributes = attr;

        assertEquals(attr, node.attributes());
        assertEquals("test1", node.attr("test1"));
        assertEquals("test2", node.attr("test2"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testHasAttr() {
        node.attributes().put("test", "test");
        assertTrue(node.hasAttr("test"));
    }

    @Test
    public void testRemoveAttr() {
        node.attributes().put("test", "test");
        node.removeAttr("test");
        assertFalse(node.hasAttr("test"));
    }

    @Test
    public void testBaseUri() {
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        node.attributes().put("href", "relative.html");
        assertEquals("", node.absUrl(""));
        assertEquals("http://example.com/relative.html", node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlInvalidUrl() {
        node.attributes().put("href", "invalid:url");
        node.absUrl("href");
    }

    @Test
    public void testChildNode() {
        Element child = new Element("p", "", doc);
        node.appendChild(child);
        assertEquals(child, node.childNode(0));
    }

    @Test
    public void testChildNodes() {
        Element child1 = new Element("p", "", doc);
        Element child2 = new Element("p", "", doc);
        node.appendChild(child1);
        node.appendChild(child2);
        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testChildNodesAsArray() {
        Element child1 = new Element("p", "", doc);
        Element child2 = new Element("p", "", doc);
        node.appendChild(child1);
        node.appendChild(child2);
        Node[] children = node.childNodesAsArray();
        assertEquals(2, children.length);
        assertEquals(child1, children[0]);
        assertEquals(child2, children[1]);
    }

    @Test
    public void testParent() {
        doc.appendChild(node);
        assertNull(node.parent());

        Element parent = new Element("body", "", doc);
        doc.appendChild(parent);
        parent.appendChild(node);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        doc.appendChild(node);
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        doc.appendChild(node);
        doc.removeChild(node);
        assertNull(node.parent());
    }

    @Test
    public void testReplaceWith() {
        doc.appendChild(node);
        Element replace = new Element("p", "", doc);
        node.replaceWith(replace);
        assertEquals(replace, node.parent().childNode(node.siblingIndex()));
    }

    @Test
    public void testSetParentNode() {
        Element parent = new Element("body", "", doc);
        doc.appendChild(parent);
        node.setParentNode(parent);
        parent.appendChild(node);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testReplaceChild() {
        Element parent = new Element("body", "", doc);
        doc.appendChild(parent);
        Element child1 = new Element("p", "", doc);
        Element child2 = new Element("p", "", doc);
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.replaceChild(child1, node);
        assertEquals(node, parent.childNode(0));
    }

    @Test
    public void testRemoveChild() {
        Element parent = new Element("body", "", doc);
        doc.appendChild(parent);
        Element child1 = new Element("p", "", doc);
        Element child2 = new Element("p", "", doc);
        parent.appendChild(child1);
        parent.appendChild(child2);
        parent.removeChild(child1);
        assertEquals(child2, parent.childNode(0));
    }

    @Test
    public void testAddChildren() {
        Element parent = new Element("body", "", doc);
        doc.appendChild(parent);
        Element child1 = new Element("p", "", doc);
        Element child2 = new Element("p", "", doc);
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testAddChildrenMultiple() {
        Element parent = new Element("body", "", doc);
        doc.appendChild(parent);
        Element child1 = new Element("p", "", doc);
        Element child2 = new Element("p", "", doc);
        Element child3 = new Element("p", "", doc);
        parent.addChildren(child1, child2, child3);
        assertEquals(3, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
        assertEquals(child3, parent.childNode(2));
    }
}