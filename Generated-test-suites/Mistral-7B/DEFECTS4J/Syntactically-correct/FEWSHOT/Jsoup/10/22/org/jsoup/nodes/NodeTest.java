package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNewInstance() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("Node", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertEquals(attributes, node1.attributes());

        assertEquals("Node", node2.nodeName());
        assertEquals(baseUri, node2.baseUri());
        assertNull(node2.attributes());

        assertEquals("Node", node3.nodeName());
        assertEquals(null, node3.baseUri());
        assertNull(node3.attributes());
    }

    @Test
    public void testAttributeAccess() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "attrValue");
        Node node = new Element("test", baseUri, attributes);

        String attrValue = node.attr("test");
        assertEquals("attrValue", attrValue);

        String absAttrValue = node.absUrl("test");
        assertEquals(baseUri + "#test", absAttrValue);

        assertNull(node.attr("nonExistentAttr"));
        assertNull(node.absUrl("nonExistentAttr"));
    }

    @Test
    public void testAttributeSetters() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node node = new Element("test", baseUri, attributes);
        String attrValue = "attrValue";

        node.attr("test", attrValue);
        assertEquals(attrValue, node.attr("test"));
        assertEquals(attrValue, node.absUrl("test"));

        node.removeAttr("test");
        assertNull(node.attr("test"));
        assertNull(node.absUrl("test"));

        node.attr("test2", "newAttrValue");
        assertEquals("newAttrValue", node.attr("test2"));
        assertEquals(baseUri + "#test2", node.absUrl("test2"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "attrValue");

        Node node = new Element("test", baseUri, attributes);

        boolean hasAttr = node.hasAttr("test");
        assertTrue(hasAttr);

        hasAttr = node.hasAttr("nonExistentAttr");
        assertFalse(hasAttr);
    }

    @Test
    public void testAttributeRemoval() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("test", "attrValue");

        Node node = new Element("test", baseUri, attributes);

        node.removeAttr("test");
        assertNull(node.attr("test"));
        assertNull(node.absUrl("test"));
    }

    @Test
    public void testBaseUriSetter() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node node = new Element("test", baseUri, attributes);

        String newBaseUri = "https://new-example.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node node = new Element("test", baseUri, attributes);

        String relUrl = "test.html";
        String absUrl = node.absUrl(relUrl);
        assertEquals(baseUri + "/" + relUrl, absUrl);

        node.setBaseUri("https://new-example.com");
        absUrl = node.absUrl(relUrl);
        assertEquals("https://new-example.com" + "/" + relUrl, absUrl);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node parent = new Element("parent", baseUri, attributes);

        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new Element("child2", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertSame(child1, childNodes.get(0));
        assertSame(child2, childNodes.get(1));

        List<Node> childNodesAsArray = parent.childNodesAsArray();
        assertArrayEquals(new Node[] {child1, child2}, childNodesAsArray);
    }

    @Test
    public void testParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node child = new Element("child", baseUri, attributes);
        Node parent = new Element("parent", baseUri, attributes);

        child.setParentNode(parent);
        assertEquals(parent, child.parent());

        parent.appendChild(child);
        assertEquals(parent, child.parent());

        parent.removeChild(child);
        assertNull(child.parent());
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node parent = new Element("parent", baseUri, attributes);
        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new Element("child2", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.removeChild(child1);

        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertSame(child2, childNodes.get(0));

        List<Node> childNodesAsArray = parent.childNodesAsArray();
        assertArrayEquals(new Node[] {child2}, childNodesAsArray);
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node parent = new Element("parent", baseUri, attributes);
        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new Element("child2", baseUri, attributes);

        parent.appendChild(child1);
        parent.appendChild(child2);

        parent.replaceChild(child1, child2);

        List<Node> childNodes = parent.childNodes();
        assertEquals(1, childNodes.size());
        assertSame(child2, childNodes.get(0));

        List<Node> childNodesAsArray = parent.childNodesAsArray();
        assertArrayEquals(new Node[] {child2}, childNodesAsArray);
    }

    @Test
    public void testAddChildren() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node parent = new Element("parent", baseUri, attributes);

        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new Element("child2", baseUri, attributes);
        Node child3 = new Element("child3", baseUri, attributes);

        parent.addChildren(child1, child2, child3);

        List<Node> childNodes = parent.childNodes();
        assertEquals(3, childNodes.size());
        assertSame(child1, childNodes.get(0));
        assertSame(child2, childNodes.get(1));
        assertSame(child3, childNodes.get(2));

        List<Node> childNodesAsArray = parent.childNodesAsArray();
        assertArrayEquals(new Node[] {child1, child2, child3}, childNodesAsArray);
    }

    @Test
    public void testAddChildrenWithIndex() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node parent = new Element("parent", baseUri, attributes);

        Node child1 = new Element("child1", baseUri, attributes);
        Node child2 = new Element("child2", baseUri, attributes);
        Node child3 = new Element("child3", baseUri, attributes);

        parent.addChildren(1, child1, child2, child3);

        List<Node> childNodes = parent.childNodes();
        assertEquals(3, childNodes.size());
        assertSame(child1, childNodes.get(1));
        assertSame(child2, childNodes.get(2));
        assertSame(child3, childNodes.get(3));

        List<Node> childNodesAsArray = parent.childNodesAsArray();
        assertArrayEquals(new Node[] {child1, child2, child3}, childNodesAsArray);
    }

    @Test
    public void testSetParentNode() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node child = new Element("child", baseUri, attributes);
        Node parent = new Element("parent", baseUri, attributes);

        child.setParentNode(parent);
        assertEquals(parent, child.parent());

        parent.appendChild(child);
        assertEquals(parent, child.parent());

        parent.removeChild(child);
        assertNull(child.parent());
    }

    @Test
    public void testReplaceChildWithNull() {
        String baseUri = "https://example.com";
        Attributes attributes = new AttributesImpl();

        Node parent = new Element("parent", baseUri, attributes);
        Node child = new Element("child", baseUri, attributes);

        parent.replaceChild(child, null);
        assertEquals(Collections.emptyList(), parent.childNodes());
    }
}