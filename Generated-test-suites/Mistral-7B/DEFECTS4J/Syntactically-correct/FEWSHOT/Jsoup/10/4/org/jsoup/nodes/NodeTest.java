package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        // Test protected Node constructor with baseUri and attributes
        Node node1 = new Node(baseUri, attributes);
        assertEquals("testNode", node1.nodeName());
        assertEquals(baseUri, node1.baseUri());
        assertNotNull(node1.attributes());
        assertTrue(node1.hasAttr("id"));
        assertEquals("testNode", node1.attr("id"));

        // Test default constructor with no setup
        Node node2 = new Node();
        assertEquals(Collections.EMPTY_LIST, node2.childNodes());
        assertNull(node2.attributes());
        assertNull(node2.baseUri());

        // Test abstract nodeName() method
        fail("Subclasses must implement nodeName() method");
    }

    @Test
    public void testAttributeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node = new Element("div", baseUri, attributes);

        // Test attr() method with existing attribute
        assertEquals("testValue", node.attr("testAttr", "testValue"));
        assertEquals("testValue", node.attr("testAttr"));

        // Test attr() method with non-existent attribute
        assertEquals("", node.attr("nonexistentAttr"));

        // Test attr() method with "abs:" prefix for absolute URLs
        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUri);
        } catch (MalformedURLException e) {
            fail("Could not create URL from baseUri: " + baseUri);
        }
        assertEquals(baseUrl.toString(), node.absUrl("abs:href"));

        // Test attributes() method
        assertEquals(attributes, node.attributes());

        // Test attr() method with setting an attribute
        node.attr("newAttr", "newValue");
        assertEquals("newValue", node.attr("newAttr"));

        // Test hasAttr() method
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonexistentAttr"));

        // Test removeAttr() method
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testAbsUrlMethod() {
        String baseUri = "http://example.com";
        String relativeUrl = "relative.html";

        Node node = new Element("a", baseUri, new Attributes().put("href", relativeUrl));

        // Test absUrl() method with existing URL
        assertEquals("http://example.com/relative.html", node.absUrl("href"));

        // Test absUrl() method with non-existent URL
        assertEquals("", node.absUrl("nonexistentUrl"));

        // Test absUrl() method with baseUri that cannot be parsed as URL
        String invalidBaseUri = "invalid:baseUri";
        try {
            new URL(invalidBaseUri);
            fail("Should not be able to create URL from invalid baseUri: " + invalidBaseUri);
        } catch (MalformedURLException e) {
            // expected
        }
        Node nodeWithInvalidBaseUri = new Element("a", invalidBaseUri, new Attributes());
        assertEquals("", nodeWithInvalidBaseUri.absUrl("href"));
    }

    @Test
    public void testChildNodeMethods() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "parentNode");
        Node parentNode = new Element("div", baseUri, attributes);

        Attributes childAttributes = new Attributes().put("class", "childNode");
        Node childNode = new Element("p", baseUri, childAttributes);

        parentNode.addChild(childNode);

        // Test childNode() method
        Node node = parentNode.childNode(0);
        assertEquals(childNode, node);

        // Test childNodes() method
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode, childNodes.get(0));

        // Test childNodesAsArray() method
        Node[] childNodesArray = parentNode.childNodesAsArray();
        assertEquals(1, childNodesArray.length);
        assertEquals(childNode, childNodesArray[0]);

        // Test parent() method
        assertEquals(parentNode, childNode.parent());

        // Test ownerDocument() method
        Document ownerDocument = new Document();
        childNode.setOwnerDocument(ownerDocument);
        assertEquals(ownerDocument, childNode.ownerDocument());
    }

    @Test
    public void testRemoveMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes().put("id", "parentNode");
        Node parentNode = new Element("div", baseUri, attributes);

        Attributes childAttributes = new Attributes().put("class", "childNode");
        Node childNode = new Element("p", baseUri, childAttributes);

        parentNode.addChild(childNode);

        // Test remove() method
        parentNode.removeChild(childNode);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());

        // Test replaceWith() method
        Node replaceNode = new Element("span", baseUri);
        parentNode.replaceChild(childNode, replaceNode);
        childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(replaceNode, childNodes.get(0));
    }

    @Test
    public void testSetParentNodeAndReplaceChildMethods() {
        String baseUri = "http://example.com";
        Attributes parentAttributes = new Attributes().put("id", "parentNode");
        Node parentNode = new Element("div", baseUri, parentAttributes);

        Attributes childAttributes = new Attributes().put("class", "childNode");
        Node childNode = new Element("p", baseUri, childAttributes);

        Node newChildNode = new Element("span", baseUri);

        parentNode.addChild(childNode);

        // Test setParentNode() method
        childNode.setParentNode(newChildNode);
        assertEquals(newChildNode, childNode.parent());
        assertEquals(1, newChildNode.childNodes().size());
        assertEquals(childNode, newChildNode.childNodes().get(0));

        // Test replaceChild() method
        parentNode.replaceChild(childNode, newChildNode);
        assertEquals(newChildNode, parentNode.childNode(0));
        assertEquals(1, newChildNode.childNodes().size());
        assertEquals(childNode, newChildNode.childNodes().get(0));
    }

    @Test
    public void testAddChildrenMethods() {
        String baseUri = "http://example.com";
        Node parentNode = new Element("div", baseUri);

        Attributes child1Attributes = new Attributes().put("class", "child1");
        Node child1 = new Element("p", baseUri, child1Attributes);

        Attributes child2Attributes = new Attributes().put("class", "child2");
        Node child2 = new Element("p", baseUri, child2Attributes);

        // Test addChildren() method with array
        parentNode.addChildren(new Node[] { child1, child2 });
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(child1, parentNode.childNode(0));
        assertEquals(child2, parentNode.childNode(1));

        // Test addChildren() method with variable number of arguments
        parentNode.addChildren(child1, child2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(child1, parentNode.childNode(0));
        assertEquals(child2, parentNode.childNode(1));
    }
}