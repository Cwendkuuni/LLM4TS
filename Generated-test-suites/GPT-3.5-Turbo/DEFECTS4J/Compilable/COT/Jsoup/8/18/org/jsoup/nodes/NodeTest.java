package org.jsoup.nodes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        // Create a Node object and check if the nodeName method returns the correct value
        Node node = new Node() {
            @Override
            public String nodeName() {
                return "testNode";
            }
            // Implement the abstract methods of the Node class
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        };

        assertEquals("testNode", node.nodeName());
    }

    @Test
    public void testAttr() {
        // Create a Node object with some attributes
        Node node = new Node("https://www.example.com") {
            @Override
            public String nodeName() {
                return "testNode";
            }
            // Implement the abstract methods of the Node class
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        };
        node.attr("key1", "value1");
        node.attr("key2", "value2");

        // Check if the attr method returns the correct attribute value
        assertEquals("value1", node.attr("key1"));
        assertEquals("value2", node.attr("key2"));
    }

    @Test
    public void testHasAttr() {
        // Create a Node object with some attributes
        Node node = new Node("https://www.example.com") {
            @Override
            public String nodeName() {
                return "testNode";
            }
            // Implement the abstract methods of the Node class
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        };
        node.attr("key1", "value1");
        node.attr("key2", "value2");

        // Check if the hasAttr method returns the correct value
        assertTrue(node.hasAttr("key1"));
        assertTrue(node.hasAttr("key2"));
        assertFalse(node.hasAttr("key3"));
    }

    @Test
    public void testRemoveAttr() {
        // Create a Node object with some attributes
        Node node = new Node("https://www.example.com") {
            @Override
            public String nodeName() {
                return "testNode";
            }
            // Implement the abstract methods of the Node class
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        };
        node.attr("key1", "value1");
        node.attr("key2", "value2");

        // Remove an attribute and check if the attribute is no longer present
        node.removeAttr("key1");
        assertFalse(node.hasAttr("key1"));
    }

    @Test
    public void testBaseUri() {
        // Create a Node object with a base URI
        Node node = new Node("https://www.example.com") {
            @Override
            public String nodeName() {
                return "testNode";
            }
            // Implement the abstract methods of the Node class
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        };

        // Check if the baseUri method returns the correct value
        assertEquals("https://www.example.com", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        // Create a Node object with a base URI
        Node node = new Node("https://www.example.com") {
            @Override
            public String nodeName() {
                return "testNode";
            }
            // Implement the abstract methods of the Node class
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {}
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}
        };

        // Set a new base URI and check if the baseUri method returns the new value
        node.setBaseUri("https://www.newexample.com");
        assertEquals("https://www.newexample.com", node.baseUri());
    }
}