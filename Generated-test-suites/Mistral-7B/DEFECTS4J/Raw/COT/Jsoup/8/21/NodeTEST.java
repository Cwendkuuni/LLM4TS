package org.jsoup.nodes; // update the package name if needed

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    

    // Test constructor
    @Test
    public void testNodeConstruction() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeConstructionWithAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Node(baseUri, attributes);
        assertNotNull(node);
        assertEquals(baseUri, node.baseUri());
        assertEquals("test", node.attr("class"));
        assertTrue(node.childNodes().isEmpty());
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        Attributes attributes = new Attributes();
        node.setBaseUri("http://newbase.com");
        node.setAttributes(attributes);

        assertEquals("http://newbase.com", node.baseUri());
        assertEquals(attributes, node.attributes());

        node.setBaseUri(baseUri);
        node.setAttributes(null);

        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    // Test abstract methods
    @Test(expected = AssertionError.class)
    public void testAbstractMethods() {
        Node node = new Node();
        node.nodeName(); // should throw an AssertionError
    }

    // Test methods related to attributes
    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        node.setAttributes(attributes);

        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("non_existent_attr"));
    }

    @Test
    public void testHasAttr() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        node.setAttributes(attributes);

        assertTrue(node.hasAttr("class"));
        assertFalse(node.hasAttr("non_existent_attr"));
    }

    @Test
    public void testSetAttr() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        Attributes attributes = new Attributes();

        node.attr("class", "test");
        assertEquals("test", node.attr("class"));

        node.attr("class", "updated_test");
        assertEquals("updated_test", node.attr("class"));
    }

    @Test
    public void testRemoveAttr() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        node.setAttributes(attributes);

        node.removeAttr("class");
        assertEquals("", node.attr("class"));
    }

    // Test methods related to URLs
    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals(baseUri, node.baseUri());

        node.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);
        node.attributes().put("href", "/relative/path");

        assertEquals("http://example.com/relative/path", node.absUrl("href"));

        node.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com/relative/path", node.absUrl("href"));
    }

    // Test methods related to children
    @Test
    public void testChildNode() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Node childNode = new Node(baseUri);
        node.addChild(childNode);

        assertEquals(childNode, node.childNode(0));
        assertEquals(Collections.singletonList(childNode), node.childNodes());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildNodeInvalidIndex() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        node.addChild(new Node(baseUri));
        node.addChild(new Node(baseUri));

        node.childNode(2); // should throw IndexOutOfBoundsException
    }

    @Test
    public void testChildNodesEmptyList() {
        Node node = new Node();

        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testChildNodesWithChildren() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);

        for (int i = 0; i < 10; i++) {
            Node childNode = new Node(baseUri);
            parentNode.addChild(childNode);
        }

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(10, childNodes.size());
    }

    // Test methods related to parent and siblings
    @Test
    public void testParent() {
        String baseUri1 = "http://example1.com";
        String baseUri2 = "http://example2.com";

        Node childNode = new Node(baseUri1);
        Node parentNode1 = new Node(baseUri2);
        childNode.setParentNode(parentNode1);

        assertEquals(parentNode1, childNode.parent());
        assertNull(parentNode1.childNodes().get(0).parent());
    }

    @Test
    public void testOwnerDocument() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertTrue(node.ownerDocument() == null);

        Node document = new Document(baseUri);
        node.setOwnerDocument(document);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testRemoveAndReplaceChildren() {
        String baseUri = "http://example.com";
        Node parentNode = new Node(baseUri);

        Node child1 = new Node(baseUri);
        Node child2 = new Node(baseUri);
        Node child3 = new Node(baseUri);

        parentNode.addChildren(child1, child2, child3);

        assertEquals(3, parentNode.childNodes().size());

        parentNode.removeChild(child1);

        assertEquals(2, parentNode.childNodes().size());

        parentNode.replaceChild(child2, child3);

        assertEquals(child3, parentNode.childNodes().get(1));
    }

    
}