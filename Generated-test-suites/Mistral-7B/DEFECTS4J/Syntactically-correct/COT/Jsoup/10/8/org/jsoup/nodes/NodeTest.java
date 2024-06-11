package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeConstructorWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals("http://example.com", node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNullBaseUriThrowsException() {
        Attributes attributes = new Attributes();

        try {
            new Node(null, attributes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullAttributesThrowsException() {
        String baseUri = "http://example.com";

        try {
            new Node(baseUri, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNodeAbstractMethodNodeName() {
        try {
            new Node() {
                @Override
                public String nodeName() {
                    throw new UnsupportedOperationException();
                }
            };
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        attributes.put("test", "testValue");

        assertEquals("testValue", node.attr("test"));
        assertEquals("", node.attr("nonExistentAttribute"));
        assertEquals("", node.attr(""));
        assertEquals("", node.attr(null));
        assertEquals("", node.attr(" "));
    }

    @Test
    public void testGetAttributeWithPrefix() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        attributes.put("href", "testValue");

        assertEquals("http://example.com/testValue", node.absUrl("href"));
    }

    @Test
    public void testGetAttributeNullBaseUri() {
        Attributes attributes = new Attributes();
        Node node = new Node();
        attributes.put("test", "testValue");
        node.setBaseUri("http://example.com");

        assertEquals("testValue", node.attr("test"));
        assertEquals("", node.absUrl("href"));
    }

    @Test
    public void testGetAttributeWithEmptyBaseUri() {
        Attributes attributes = new Attributes();
        Node node = new Node();
        node.setBaseUri("");
        attributes.put("href", "testValue");

        assertEquals("testValue", node.attr("href"));
        assertEquals("", node.absUrl("href"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        attributes.put("test", "testValue");

        assertTrue(node.hasAttr("test"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("test", "testValue");
        assertEquals("testValue", node.attr("test"));
        assertEquals("", node.attr("nonExistentAttribute"));

        node.attr("test", "newTestValue");
        assertEquals("newTestValue", node.attr("test"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        attributes.put("test", "testValue");

        node.removeAttr("test");
        assertEquals("", node.attr("test"));
    }

    @Test
    public void testNullAttributeKeyThrowsException() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        try {
            node.removeAttr(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "http://newexample.com";
        Attributes attributes = new Attributes();
        Node node = new Node("http://example.com", attributes);

        node.setBaseUri(newBaseUri);
        assertEquals("http://newexample.com", node.baseUri());
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        Node retrievedChildNode = parentNode.childNode(0);

        assertSame(childNode, retrievedChildNode);
    }

    @Test
    public void testGetEmptyChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        List<Node> childNodes = node.childNodes();
        assertTrue(childNodes.isEmpty());
    }

    @Test
    public void testGetChildNodesAsArray() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);
        Node[] childNodesArray = parentNode.childNodesAsArray();

        assertSame(childNode1, childNodesArray[0]);
        assertSame(childNode2, childNodesArray[1]);
    }

    @Test
    public void testGetParentNode() {
        String baseUri1 = "http://parent1.com";
        Attributes attributes1 = new Attributes();
        Node parentNode1 = new Node(baseUri1, attributes1);

        String baseUri2 = "http://parent2.com";
        Attributes attributes2 = new Attributes();
        Node childNode = new Node(baseUri2, attributes2);

        childNode.setParentNode(parentNode1);
        Node retrievedParentNode = childNode.parent();

        assertSame(parentNode1, retrievedParentNode);
    }

    @Test
    public void testGetParentNodeNull() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        assertNull(node.parent());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);

        parentNode.addChild(childNode);
        Node clonedChildNode = childNode.clone();

        assertFalse(childNode == clonedChildNode);
        assertTrue(childNode.equals(clonedChildNode));

        assertNull(clonedChildNode.parentNode());
        List<Node> clonedChildNodes = clonedChildNode.childNodes();
        assertTrue(clonedChildNodes.isEmpty());

        Node clonedParentNode = new Node(baseUri, attributes);
        clonedParentNode.addChild(clonedChildNode);
        Node retrievedClonedChildNode = clonedParentNode.childNode(0);

        assertSame(clonedChildNode, retrievedClonedChildNode);
    }

    @Test
    public void testCloneNullParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes);

        Node clonedChildNode = childNode.clone();

        assertNull(clonedChildNode.parentNode());
        List<Node> clonedChildNodes = clonedChildNode.childNodes();
        assertTrue(clonedChildNodes.isEmpty());
    }

    @Test
    public void testTraverseNodeWithVisitor() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        parentNode.traverse(new NodeTraversor(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                node.outerHtmlHead(accum, depth, outputSettings);
            }

            @Override
            public void tail(Node node, int depth) {
                node.outerHtmlTail(accum, depth, outputSettings);
            }
        }));

        String expectedHtml = "<html>\n<head>\n<body>\n<p>\n<p>\n</body>\n</html>";
        assertEquals(expectedHtml, accum.toString());
    }
}