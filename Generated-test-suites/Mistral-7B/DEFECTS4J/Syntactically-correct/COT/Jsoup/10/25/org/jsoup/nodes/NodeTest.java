package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        // Test abstract methods should be implemented in subclasses
        try {
            new Node("", new Attributes()) {
                @Override
                public String nodeName() {
                    throw new UnsupportedOperationException();
                }
            };
            fail("Should not be able to instantiate abstract Node with missing nodeName method.");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("testAttr", "testVal");

        Node node = new Node(baseUri, attributes);
        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeConstructorWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode);
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testGetNodeName() {
        String baseUri = "http://example.com";
        Element element = new Element("div") {
            @Override
            public String nodeName() {
                return "div";
            }
        };
        element.setBaseUri(baseUri);
        assertEquals("div", element.nodeName());

        TextNode textNode = new TextNode("Hello World");
        textNode.setBaseUri(baseUri);
        assertEquals("#text", textNode.nodeName());
    }

    @Test
    public void testGetAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("testAttr", "testVal");

        Node node = new Node(baseUri, attributes);
        assertEquals("testVal", node.attr("testAttr"));
        assertEquals("", node.attr("nonExistentAttr"));

        Node nodeWithAbsUrlAttr = new Node(baseUri) {
            @Override
            public String attr(String attributeKey) {
                if (attributeKey.toLowerCase().startsWith("abs:")) {
                    return "http://test.com";
                }
                return super.attr(attributeKey);
            }
        };
        assertEquals("http://test.com", nodeWithAbsUrlAttr.attr("abs:testAttr"));
    }

    @Test
    public void testGetAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("testAttr1", "testVal1");
        attributes.put("testAttr2", "testVal2");

        Node node = new Node(baseUri, attributes);
        Attributes nodeAttributes = node.attributes();
        assertEquals(attributes.size(), nodeAttributes.size());
        assertEquals(attributes.get("testAttr1"), nodeAttributes.get("testAttr1"));
        assertEquals(attributes.get("testAttr2"), nodeAttributes.get("testAttr2"));
    }

    @Test
    public void testSetAttr() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri) {
            @Override
            public Node attr(String key, String value) {
                super.attr(key, value);
                return this;
            }
        };

        node.attr("testAttr1", "testVal1");
        node.attr("testAttr2", "testVal2");

        Attributes nodeAttributes = node.attributes();
        assertEquals("testVal1", nodeAttributes.get("testAttr1"));
        assertEquals("testVal2", nodeAttributes.get("testAttr2"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("testAttr1", "testVal1");

        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("testAttr1"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("testAttr", "testVal");

        Node node = new Node(baseUri, attributes);
        node.removeAttr("testAttr");
        assertFalse(node.hasAttr("testAttr"));
    }

    @Test
    public void testGetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node();
        node.setBaseUri(baseUri);
        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child = new Node(baseUri);
        parent.appendChild(child);

        Node retrievedChild = parent.childNode(0);
        assertSame(child, retrievedChild);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNodeNegativeIndex() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        parent.appendChild(new Node(baseUri));

        parent.childNode(-1);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> children = parent.childNodes();
        assertEquals(2, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
    }

    @Test
    public void testGetOrphanChildren() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        child1.setParentNode(parent);
        child2.setParentNode(parent);
        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> orphanChildren = parent.childNodesAsArray();
        assertEquals(2, orphanChildren.length);
        assertSame(child1, orphanChildren[0]);
        assertSame(child2, orphanChildren[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetParentWhenOrphan() {
        String baseUri = "http://example.com";
        Node child = new Node(baseUri);
        child.setParentNode(null);
        child.parentNode;
    }

    @Test
    public void testGetParent() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child = new Node(baseUri);
        parent.appendChild(child);

        assertEquals(parent, child.parentNode);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetParentWhenOrphan() {
        String baseUri = "http://example.com";
        Node child = new Node(baseUri);
        child.setParentNode(null);
        child.setParentNode(new Node(baseUri));
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node oldChild = new Node(baseUri);
        Node newChild = new Node(baseUri);
        parent.appendChild(oldChild);

        parent.replaceChild(oldChild, newChild);
        assertEquals(newChild, parent.childNode(0));
        assertEquals(newChild, oldChild.parentNode);
        assertEquals(parent, newChild.parentNode);
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child = new Node(baseUri);
        parent.appendChild(child);

        parent.removeChild(child);
        assertTrue(parent.childNodes().isEmpty());
        assertNull(child.parentNode);
    }

    @Test
    public void testAddChildren() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);

        parent.addChildren(1, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(1));
        assertEquals(child2, parent.childNode(2));
    }

    @Test
    public void testGetDocument() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertEquals(null, node.ownerDocument());

        Document doc = new Document(baseUri);
        doc.appendChild(node);
        assertEquals(doc, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Node parent = new Node(baseUri);
        Node nodeToRemove = new Node(baseUri);
        parent.appendChild(nodeToRemove);

        parent.remove();
        assertFalse(parent.hasChildren());
        assertNull(nodeToRemove.parentNode);
    }
}