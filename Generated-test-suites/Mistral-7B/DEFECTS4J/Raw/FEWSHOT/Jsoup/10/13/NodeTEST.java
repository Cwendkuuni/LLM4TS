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
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com";
        String attributeKey = "testAttribute";
        String attributeValue = "testValue";

        Node node = new Element("div") {
            {
                setBaseUri(baseUri);
                setAttributes(new Attributes().put(attributeKey, attributeValue));
            }
        };

        // Test nodeName()
        assertEquals("div", node.nodeName());

        // Test attr()
        assertEquals(attributeValue, node.attr(attributeKey));
        assertEquals(baseUri, node.attr("abs:baseUri"));
        assertNull(node.attr("nonExistentAttribute"));

        // Test attributes()
        Attributes attributes = node.attributes();
        assertTrue(attributes.hasKey(attributeKey));
        assertEquals(attributeValue, attributes.get(attributeKey));

        // Test hasAttr()
        assertTrue(node.hasAttr(attributeKey));
        assertFalse(node.hasAttr("nonExistentAttribute"));

        // Test removeAttr()
        node.removeAttr(attributeKey);
        assertFalse(node.hasAttr(attributeKey));

        // Test absUrl()
        assertEquals(baseUri + "/div" + attributeValue, node.absUrl(attributeKey));
        assertNull(node.absUrl("nonExistentAttribute"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://example.com";
        String childNodeName = "p";
        String childNodeText = "Child text";

        Node parentNode = new Element("div") {
            {
                setBaseUri(baseUri);
                setAttributes(new Attributes().put("id", "parent"));
            }
        };

        Node childNode = new Element(childNodeName) {
            {
                setBaseUri(baseUri);
                setAttributes(new Attributes().put("id", "child"));
                appendText(childNodeText);
            }
        };

        parentNode.appendChild(childNode);

        // Test childNodes()
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode, childNodes.get(0));

        // Test childNode()
        Node firstChild = parentNode.childNode(0);
        assertEquals(childNode, firstChild);

        // Test removeChild()
        parentNode.removeChild(childNode);
        childNodes = parentNode.childNodes();
        assertEquals(0, childNodes.size());

        // Test remove()
        parentNode.remove(); // removes parent from its parent, and itself
    }

    @Test
    public void testNodeWithParent() {
        String baseUri = "http://example.com";
        String parentNodeName = "body";
        String childNodeName = "div";
        String childNodeText = "Child text";

        Node parentNode = new Element(parentNodeName) {
            {
                setBaseUri(baseUri);
            }
        };

        Node childNode = new Element(childNodeName) {
            {
                setBaseUri(baseUri);
                setAttributes(new Attributes().put("class", "child"));
                appendText(childNodeText);
                setParentNode(parentNode);
            }
        };

        parentNode.appendChild(childNode);

        // Test parent()
        Node parent = childNode.parent();
        assertEquals(parentNode, parent);

        // Test ownerDocument()
        Document document = parentNode.ownerDocument();
        assertEquals(parentNode, document.getElementById("parent"));

        // Test replaceWith()
        Node replacement = new Element("p");
        childNode.replaceWith(replacement);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(replacement, childNodes.get(0));
    }
}