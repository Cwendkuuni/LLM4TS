package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class NodeTest {

    @Test
    public void testAbstractNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes.put("key1", "value1");
        attributes2.put("key2", "value2");

        Node node = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("nodeName should be abstract and return an implementation specific name", "", node.nodeName());
        assertEquals("baseUri should be set correctly", baseUri, node.baseUri());
        assertEquals("attributes should be set correctly", attributes, node.attributes());

        Node node4 = new Element("elementName") {
            @Override
            public String nodeName() {
                return "elementName";
            }
        };
        assertEquals("nodeName should return 'elementName'", "elementName", node4.nodeName());

        List<Node> childNodes = new ArrayList<>();
        childNodes.add(node);
        node.setChildNodes(childNodes);
        assertEquals("childNodes should be set correctly", childNodes, node.childNodes());

        List<Node> childNodes2 = Arrays.asList(node, node);
        node.setChildNodes(childNodes2);
        assertEquals("childNodes should be updated correctly", childNodes2, node.childNodes());

        assertEquals("hasAttr should return true if attribute exists", true, node.hasAttr("key1"));
        assertEquals("hasAttr should return false if attribute does not exist", false, node.hasAttr("key2"));
        assertEquals("attr should return value of attribute", "value1", node.attr("key1"));
        assertEquals("attr should return empty string if attribute does not exist", "", node.attr("key2"));

        Node node5 = new Element("elementName") {
            @Override
            public String nodeName() {
                return "elementName";
            }

            @Override
            public String attr(String attributeKey) {
                if (attributeKey.equals("key1")) {
                    return "modifiedValue1";
                } else {
                    return super.attr(attributeKey);
                }
            }
        };
        assertEquals("attr should return modified value if implemented in subclass", "modifiedValue1", node5.attr("key1"));
        assertEquals("attr should return original value if not implemented or unmatched key", "value1", node5.attr("key2"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes.put("key1", "value1");
        attributes2.put("key2", "value2");

        Node parentNode = new Element("parent") {
            @Override
            public String nodeName() {
                return "parent";
            }
        };
        parentNode.setBaseUri(baseUri);
        parentNode.setAttributes(attributes);

        Node childNode = new Element("child") {
            @Override
            public String nodeName() {
                return "child";
            }
        };
        childNode.setBaseUri(baseUri);
        childNode.setAttributes(attributes2);
        childNode.setParentNode(parentNode);

        List<Node> childNodes = new ArrayList<>();
        childNodes.add(childNode);
        parentNode.setChildNodes(childNodes);

        assertEquals("parentNode should be set correctly", parentNode, childNode.parent());
        assertEquals("parent should return null if no parent", null, node3.parent());

        Node parentNode2 = new Element("parent2") {
            @Override
            public String nodeName() {
                return "parent2";
            }
        };
        parentNode2.setBaseUri(baseUri);
        parentNode2.setAttributes(attributes);

        childNode.setParentNode(parentNode2);
        assertEquals("parentNode should be updated correctly", parentNode2, childNode.parent());

        Node childNode2 = new TextNode("text");
        childNode2.setBaseUri(baseUri);
        childNode.appendChild(childNode2);
        List<Node> children = childNode.childNodes();
        assertEquals("childNodes should reflect changes", Arrays.asList(childNode2), children);

        parentNode2.replaceChild(childNode, new Element("replacement"));
        children = childNode.parent().childNodes();
        assertEquals("childNodes should be removed and replaced", Arrays.asList(new Element("replacement")), children);

        parentNode2.removeChild(childNode);
        children = parentNode2.childNodes();
        assertEquals("childNodes should be removed", Collections.EMPTY_LIST, children);

        parentNode2.remove();
        assertEquals("parentNode should be removed and all children", null, parentNode2.parent());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("node") {
            @Override
            public String nodeName() {
                return "node";
            }
        };
        node.setBaseUri(baseUri);
        node.setAttributes(attributes);

        assertEquals("absUrl should return baseUri if attribute is absolute", baseUri, node.absUrl("http://example.com/path"));
        assertEquals("absUrl should return derived URL if attribute is relative", "http://example.com/path", node.absUrl("path"));

        String baseUri2 = "https://example.com";
        Attributes attributes2 = new Attributes();
        attributes2.put("href", "relative.html");

        Node node2 = new Element("node2") {
            @Override
            public String nodeName() {
                return "node2";
            }
        };
        node2.setBaseUri(baseUri2);
        node2.setAttributes(attributes2);

        assertEquals("absUrl should handle multiple base URIs", "https://example.com/relative.html", node2.absUrl("relative.html"));
        assertEquals("absUrl should handle multiple base URIs with children", "https://example.com/parent/relative.html", node2.childNodes().get(0).absUrl("relative.html"));

        String baseUri3 = null;
        Attributes attributes3 = new Attributes();
        attributes3.put("href", "relative.html");

        Node node3 = new Element("node3") {
            @Override
            public String nodeName() {
                return "node3";
            }
        };
        node3.setBaseUri(baseUri3);
        node3.setAttributes(attributes3);

        assertEquals("absUrl should return empty string if baseUri is null and attribute is relative", "", node3.absUrl("relative.html"));
        assertEquals("absUrl should return empty string if baseUri is null and attribute is absolute", "", node3.absUrl("https://example.com/path"));

        String baseUri4 = "http://example.com";
        Attributes attributes4 = new Attributes();
        attributes4.put("href", "http://another.com/path");

        Node node4 = new Element("node4") {
            @Override
            public String nodeName() {
                return "node4";
            }
        };
        node4.setBaseUri(baseUri4);
        node4.setAttributes(attributes4);

        assertEquals("absUrl should handle baseUri from parent", "http://another.com/path", node4.absUrl("href"));

        String baseUri5 = "http://example.com";
        Attributes attributes5 = new Attributes();
        attributes5.put("abs:href", "http://another.com/path");

        Node node5 = new Element("node5") {
            @Override
            public String nodeName() {
                return "node5";
            }
        };
        node5.setBaseUri(baseUri5);
        node5.setAttributes(attributes5);

        assertEquals("absUrl should handle abs: prefix", "http://another.com/path", node5.absUrl("abs:href"));
    }
}