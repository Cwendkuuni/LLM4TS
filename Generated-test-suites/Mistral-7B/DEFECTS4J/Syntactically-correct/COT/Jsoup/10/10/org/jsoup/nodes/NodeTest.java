package org.test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstruction() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node1 = new Node(baseUri, attributes);
        assertEquals("elementId", node1.attr("id"));

        Node node2 = new Node(baseUri);
        assertNull(node2.attributes());

        Node node3 = new Node();
        assertNull(node3.attributes());
        assertNotNull(node3.childNodes());
        assertEquals(0, node3.childNodes().size());
    }

    @Test
    public void testAbstractMethods() {
        Node node = new Node() {
            @Override
            public String nodeName() {
                return "div";
            }
        };
        assertNotNull(node.nodeName());
        assertEquals("div", node.nodeName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        new Node("http://example.com", null);
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        attributes.put("class", "exampleClass");

        Node node = new Node(baseUri, attributes);
        assertEquals("elementId", node.attr("id"));
        assertEquals("exampleClass", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testGetAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        attributes.put("class", "exampleClass");

        Node node = new Node(baseUri, attributes);
        Attributes attributesTest = node.attributes();
        assertEquals("exampleClass", attributesTest.get("class"));
        assertEquals("elementId", attributesTest.get("id"));
        assertNull(attributesTest.get("nonExistentAttribute"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node = new Node(baseUri, attributes);
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        node.attr("id", "newId");
        assertEquals("newId", node.attr("id"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");

        Node node = new Node(baseUri, attributes);
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test(expected = NullPointerException.class)
    public void testSetAttributeNullAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attr(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testSetAttributeNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);
        node.attr(null, "value");
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        String attributeValue = "relativeUrl";

        Node node = new Node(baseUri, new Attributes());
        node.attr("abs:href", attributeValue);
        assertEquals("http://example.com" + attributeValue, node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlMalformedBaseUri() {
        String baseUri = "jsoup://example.com";
        String attributeValue = "relativeUrl";

        Node node = new Node(baseUri, new Attributes());
        node.attr("abs:href", attributeValue);
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child = new Node(baseUri, attributes);
        parent.childNodes.add(child);

        Node testChild = parent.childNode(0);
        assertEquals(child, testChild);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parent.childNodes.add(child1);
        parent.childNodes.add(child2);

        List<Node> testChildren = parent.childNodes();
        assertEquals(2, testChildren.size());
        assertEquals(child1, testChildren.get(0));
        assertEquals(child2, testChildren.get(1));
    }

    @Test
    public void testArrayOfChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parent.childNodes.add(child1);
        parent.childNodes.add(child2);

        Node[] testChildren = parent.childNodesAsArray();
        assertEquals(2, testChildren.length);
        assertEquals(child1, testChildren[0]);
        assertEquals(child2, testChildren[1]);
    }

    @Test
    public void testParentNode() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();

        Node parent1 = new Node(baseUri1, attributes1);
        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();

        Node child = new Node(baseUri2, attributes2);
        parent1.childNodes.add(child);
        child.setParentNode(parent1);

        Node testParent = child.parent();
        assertEquals(parent1, testParent);
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        Document document = new Document(baseUri);
        document.setBody(node);

        Node testDocument = node.ownerDocument();
        assertEquals(document, testDocument);
    }

    @Test
    public void testRemoveNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child = new Node(baseUri, attributes);
        parent.childNodes.add(child);

        parent.removeChild(child);
        List<Node> testChildren = parent.childNodes();
        assertEquals(0, testChildren.size());
    }

    @Test
    public void testReplaceNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node nodeToReplace = new Node(baseUri, attributes);
        Node newNode = new Node(baseUri, attributes);

        parent.childNodes.add(nodeToReplace);
        parent.replaceChild(nodeToReplace, newNode);
        assertEquals(newNode, nodeToReplace);
    }

    @Test
    public void testSetParentNode() {
        String baseUri1 = "http://example1.com";
        Attributes attributes1 = new Attributes();
        String baseUri2 = "http://example2.com";
        Attributes attributes2 = new Attributes();

        Node child = new Node(baseUri1, attributes1);
        Node parent = new Node(baseUri2, attributes2);

        child.setParentNode(parent);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testSetSiblingIndex() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parent = new Node(baseUri, attributes);
        Node child1 = new Node(baseUri, attributes);
        Node child2 = new Node(baseUri, attributes);
        parent.childNodes.add(child1);
        parent.childNodes.add(child2);

        child1.setSiblingIndex(1);
        assertEquals(1, child1.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes);
        StringBuilder accum = new StringBuilder();

        node.outerHtml(accum);
        String testOuterHtml = accum.toString();
        assertTrue(testOuterHtml.contains("<node>"));
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node original = new Node(baseUri, attributes);
        Node clone = original.clone();

        assertEquals(original, clone);
        assertNotSame(original, clone);
    }
}