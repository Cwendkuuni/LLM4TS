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
        Attributes attributes = new Attributes();
        attributes.put("id", "elementId");
        Node node = new Element("div", baseUri, attributes);

        // Test nodeName()
        assertEquals("div", node.nodeName());

        // Test attr()
        assertEquals("elementId", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttribute"));

        // Test attributes()
        assertEquals(attributes, node.attributes());

        // Test hasAttr()
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttribute"));

        // Test removeAttr()
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));

        // Test baseUri()
        assertEquals(baseUri, node.baseUri());

        // Test setBaseUri()
        String newBaseUri = "http://newexample.com";
        node.setBaseUri(newBaseUri);
        assertEquals(newBaseUri, node.baseUri());

        // Test absUrl()
        String relAttribute = "href";
        String absUrl = node.absUrl(relAttribute);
        assertEquals("", absUrl); // No base URI, can't make absolute URL

        String baseWithRelUrl = "http://example.com/relative.html";
        node.setBaseUri(baseWithRelUrl);
        absUrl = node.absUrl(relAttribute);
        assertEquals(baseWithRelUrl + relAttribute, absUrl); // Relative URL made absolute with base URI

        String absUrlBase = "http://example.com/absolutebase.html";
        node.setBaseUri(absUrlBase);
        absUrl = node.absUrl(relAttribute);
        assertEquals(absUrlBase + relAttribute, absUrl); // Absolute URL doesn't change when base URI is absolute

        // Test childNode()
        Node child = new TextNode("Child text");
        node.appendChild(child);
        assertEquals(child, node.childNode(0));

        // Test childNodes()
        List<Node> childNodes = node.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(child, childNodes.get(0));

        // Test addChildren()
        Node anotherChild = new TextNode("Another child text");
        node.addChildren(anotherChild);
        childNodes = node.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(child, childNodes.get(0));
        assertEquals(anotherChild, childNodes.get(1));

        // Test parent()
        Element parentElement = new Element("parent", baseUri, Collections.emptyMap());
        parentElement.appendChild(node);
        assertEquals(parentElement, node.parent());
        assertEquals(null, node.parent()); // Node with no parent

        // Test ownerDocument()
        Document document = new Document(baseUri);
        document.appendChild(parentElement);
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());

        // Test remove()
        parentElement.removeChild(node);
        assertEquals(null, node.parent());

        // Test replaceWith()
        Node replacement = new Element("replacement");
        parentElement.replaceChild(node, replacement);
        assertEquals(replacement, node.parent().childNode(node.siblingIndex()));
        assertEquals(node, replacement.previousSibling());

        // Test setParentNode()
        Element newParent = new Element("newParent", "http://newparent.com", Collections.emptyMap());
        node.setParentNode(newParent);
        assertEquals(newParent, node.parent());

        // Test replaceChild()
        Node oldChild = node.childNodes().get(0);
        node.replaceChild(newTextNode("New child text"), oldChild);
        assertEquals("New child text", node.childNode(0).text());

        // Test removeChild()
        node.removeChild(newTextNode("New child text"));
        assertEquals(0, node.childNodes().size());
    }

    // Helper method to create a TextNode for testing
    Node newTextNode(String text) {
        return new TextNode(text);
    }
}