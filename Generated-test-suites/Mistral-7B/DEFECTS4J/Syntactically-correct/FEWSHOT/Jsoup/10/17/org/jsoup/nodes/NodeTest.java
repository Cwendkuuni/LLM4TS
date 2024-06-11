package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node1 = new Element("element") {
            @Override
            public String nodeName() {
                return "element";
            }
        };
        node1.setBaseUri(baseUri);
        node1.setAttributes(attributes);

        Node node2 = new Element("element2") {
            @Override
            public String nodeName() {
                return "element2";
            }
        };
        node2.setBaseUri(baseUri);
        node2.setAttributes(new Attributes());

        Node node3 = new TextNode("text");
        node3.setBaseUri(baseUri);

        // Test attr method
        assertEquals("value1", node1.attr("key1"));
        assertEquals("value2", node1.attr("key2"));
        assertNull(node1.attr("nonExistentKey"));
        assertEquals("value1", node1.attr("abs:key1"));
        assertEquals("http://test.com/path/to/file.html", node1.absUrl("abs:href"));
        assertEquals("", node1.absUrl("nonExistentKey"));

        // Test attributes method
        assertEquals(attributes, node1.attributes());

        // Test attr method with null values
        Node node4 = new Element("element4") {
            @Override
            public String nodeName() {
                return "element4";
            }
        };
        node4.setBaseUri(baseUri);
        node4.setAttributes(new Attributes());
        node4.attr("key1", null);
        node4.attr("key2", null);
        assertEquals(null, node4.attr("key1"));
        assertEquals(null, node4.attr("key2"));

        // Test hasAttr method
        assertTrue(node1.hasAttr("key1"));
        assertTrue(node1.hasAttr("key2"));
        assertFalse(node1.hasAttr("nonExistentKey"));

        // Test removeAttr method
        node1.removeAttr("key1");
        assertFalse(node1.hasAttr("key1"));
        assertEquals("value2", node1.attr("key2"));

        // Test setBaseUri method
        assertEquals(baseUri, node1.baseUri());
        node1.setBaseUri("http://newbase.com");
        assertEquals("http://newbase.com", node1.baseUri());

        // Test absUrl method
        assertEquals("http://test.com/path/to/file.html", node1.absUrl("href"));

        // Test childNode method
        Node childNode = new Element("child") {
            @Override
            public String nodeName() {
                return "child";
            }
        };
        node1.appendChild(childNode);
        assertEquals(childNode, node1.childNode(0));

        // Test childNodes method
        List<Node> childNodes = node1.childNodes();
        assertEquals(childNode, childNodes.get(0));

        // Test getParent method
        Node parentNode = new Element("parent") {
            @Override
            public String nodeName() {
                return "parent";
            }
        };
        node1.setParentNode(parentNode);
        assertEquals(parentNode, node1.parent());

        // Test getOwnerDocument method
        Document document = new Document();
        node1.setOwnerDocument(document);
        assertEquals(document, node1.ownerDocument());

        // Test remove method
        node1.remove();
        assertNull(node1.parent());

        // Test replaceWith method
        Node node5 = new Element("replace") {
            @Override
            public String nodeName() {
                return "replace";
            }
        };
        node1.replaceWith(node5);
        assertEquals(node5, node1.parent().childNode(node1.siblingIndex()));

        // Test setParentNode method
        Node parentNode2 = new Element("parent2") {
            @Override
            public String nodeName() {
                return "parent2";
            }
        };
        node1.setParentNode(parentNode2);
        assertEquals(parentNode2, node1.parent());

        // Test replaceChild method
        Node replaceChild = new Element("replaceChild") {
            @Override
            public String nodeName() {
                return "replaceChild";
            }
        };
        node1.replaceChild(node3, replaceChild);
        assertEquals(node3, node1.childNode(0));
        assertEquals(replaceChild, node1.childNode(1));

        // Test removeChild method
        node1.removeChild(node3);
        assertEquals(replaceChild, node1.childNode(0));
        assertNull(node1.childNode(1));

        // Test addChildren method
        Node node6 = new Element("node6") {
            @Override
            public String nodeName() {
                return "node6";
            }
        };
        node1.addChildren(node6);
        assertEquals(node6, node1.childNode(node1.childNodes().size() - 1));

        // Test addChildren method with index
        node1.addChildren(0, node6);
        assertEquals(node6, node1.childNode(0));

        // Test addChildren method with multiple nodes
        Node node7 = new Element("node7") {
            @Override
            public String nodeName() {
                return "node7";
            }
        };
        Node node8 = new Element("node8") {
            @Override
            public String nodeName() {
                return "node8";
            }
        };
        node1.addChildren(node7, node8);
        assertEquals(node7, node1.childNode(0));
        assertEquals(node8, node1.childNode(1));
    }
}