package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    private Node node;
    private Attributes attributes;

    @Before
    public void setup() {
        String baseUri = "http://example.com";
        attributes = new Attributes();
        attributes.put("name", "value");
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        assertNull(node.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidNodeName() {
        new Node("", new Attributes()) {
            @Override
            public String nodeName() {
                throw new AssertionError("Invalid node name.");
            }
        };
    }

    @Test
    public void testAttr() {
        assertEquals("value", node.attr("name"));
        assertEquals("", node.attr("nonExistentAttr"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeKey() {
        node.attr(null, "value");
    }

    @Test
    public void testAttributes() {
        List<String> keys = new ArrayList<>(Arrays.asList("name", "anotherName"));
        attributes.putAll(keys.stream().map(k -> k + ":value").collect(Collectors.toMap(Function.identity(), v -> v)));
        Assert.assertEquals(attributes, node.attributes());
    }

    @Test
    public void testHasAttr() {
        assertTrue(node.hasAttr("name"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttr() {
        node.removeAttr("name");
        assertFalse(node.hasAttr("name"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeValue() {
        node.attr("name", null);
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("http://example2.com");
        assertEquals("http://example2.com", node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        node.setBaseUri(null);
    }

    @Test
    public void testAbsUrl() {
        String relativeUrl = "relative.html";
        String absoluteUrl = "http://example.com/relative.html";
        node.attr("href", relativeUrl);
        assertEquals(absoluteUrl, node.absUrl("href"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeKeyForAbsUrl() {
        node.absUrl(null);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedBaseUri() {
        node.setBaseUri("malformed:/base");
    }

    @Test
    public void testChildNode() {
        Node child = new Node("", new Attributes());
        node.addChild(child);
        assertEquals(child, node.childNode(0));
    }

    @Test
    public void testChildren() {
        Node child1 = new Node("", new Attributes());
        Node child2 = new Node("", new Attributes());
        node.addChildren(child1, child2);
        assertEquals(Arrays.asList(child1, child2), node.childNodes());
    }

    @Test
    public void testParent() {
        Node parent = new Node("", new Attributes());
        parent.addChild(node);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemove() {
        Node parent = new Node("", new Attributes());
        parent.appendChild(node);
        node.remove();
        assertFalse(parent.hasChildNodes());
    }

    @Test
    public void testReplaceWith() {
        Node parent = new Node("", new Attributes());
        Node child1 = new Node("", new Attributes());
        parent.appendChild(child1);
        Node child2 = new Node("", new Attributes());
        parent.replaceChild(child1, child2);
        assertEquals(child2, parent.childNode(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsChildNode() {
        Node parent = new Node("", new Attributes());
        parent.appendChild(new Node("", new Attributes()));
        parent.childNode(1);
    }

    @Test
    public void testReplaceChildWithSelf() {
        Node node1 = new Node("");
        Node node2 = new Node("");
        node1.replaceChild(node1, node2);
        assertSame(node1, node2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildWithDifferentType() {
        Node nodeText = new TextNode("");
        Document document = new Document();
        document.appendChild(nodeText);
        Node nodeElement = new Node("", new Attributes());
        nodeElement.replaceChild(nodeText, nodeElement);
    }
}