package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("test", node1.nodeName());
        assertEquals("", node2.nodeName());
        assertTrue(node3.childNodes().isEmpty());

        String attrValue = node1.attr("class");
        assertEquals("test", attrValue);

        List<Node> children = new ArrayList<>(3);
        children.add(node2);
        children.add(node3);
        Node parentNode = new Node();
        parentNode.childNodes = children;

        node1.setParentNode(parentNode);
        node2.setParentNode(parentNode);
        node3.setParentNode(parentNode);

        assertEquals(parentNode, node1.parent());
        assertEquals(3, parentNode.childNodes().size());
    }

    @Test
    public void testAbsUrl() {
        String baseUri = "http://example.com/index.html";
        String href = "about.html";
        Node node = new Node(baseUri, new Attributes());
        node.attributes.put("href", href);

        String absUrl = node.absUrl("href");
        String expected = "http://example.com/about.html";

        assertEquals(expected, absUrl);

        try {
            new URL(absUrl);
        } catch (MalformedURLException e) {
            fail("Failed to parse absolute URL.");
        }
    }

    @Test
    public void testAbsUrlRelative() {
        String baseUri = "http://example.com/index.html";
        String href = "images/logo.png";
        Node node = new Node(baseUri, new Attributes());
        node.attributes.put("src", href);

        String absUrl = node.absUrl("src");
        String expected = "http://example.com/images/logo.png";

        assertEquals(expected, absUrl);

        try {
            new URL(absUrl);
        } catch (MalformedURLException e) {
            fail("Failed to parse absolute URL.");
        }
    }

    @Test
    public void testAbsUrlInvalid() {
        String baseUri = "http://example.com/";
        Node node = new Node(baseUri, new Attributes());

        String attrValue = node.attr("abs:invalid");
        String absUrl = node.absUrl("abs:invalid");

        assertEquals("", attrValue);
        assertEquals("", absUrl);

        try {
            node.absUrl("abs:invalid");
            fail("Expected MalformedURLException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com/";

        Attributes attributes = new Attributes();
        attributes.put("class", "parent");

        Node parentNode = new Node(baseUri, attributes);

        String childNodeName = "child";
        String childNodeBaseUri = "http://example.com/child";
        Attributes childNodeAttributes = new Attributes();
        childNodeAttributes.put("class", "child");

        Node childNode = new Node(childNodeBaseUri, childNodeAttributes);
        parentNode.childNodes.add(childNode);

        List<Node> children = parentNode.childNodes();
        assertEquals(1, children.size());
        assertEquals(childNodeName, children.get(0).nodeName());

        Node childNodeFromParent = parentNode.childNode(0);
        assertEquals(childNode, childNodeFromParent);
        assertEquals(parentNode, childNodeFromParent.parent());

        String grandChildNodeName = "grandchild";
        String grandChildNodeBaseUri = "http://example.com/grandchild";
        Attributes grandChildNodeAttributes = new Attributes();
        grandChildChildNodeAttributes.put("class", "grandchild");

        Node grandChildNode = new Node(grandChildNodeBaseUri, grandChildNodeAttributes);
        childNode.childNodes.add(grandChildNode);

        children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertEquals(grandChildNodeName, children.get(1).nodeName());
        assertEquals(childNode, children.get(1).parent());
        assertEquals(parentNode, children.get(1).grandparent());

        Node grandChildNodeFromParent = parentNode.childNode(1);
        assertEquals(grandChildNode, grandChildNodeFromParent);
        assertEquals(parentNode, grandChildNodeFromParent.parent());
        assertEquals(childNode, grandChildNodeFromParent.grandparent());
    }

    @Test
    public void testGetAttributes() {
        String baseUri = "http://example.com/";

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("data-test", "value");

        Node node = new Node(baseUri, attributes);

        List<String> attributeKeys = Arrays.asList("class", "data-test");
        List<String> attributesValues = Arrays.asList("test", "value");

        for (int i = 0; i < attributeKeys.size(); i++) {
            String attributeKey = attributeKeys.get(i);
            String attributeValue = node.attr(attributeKey);
            assertEquals(attributesValues.get(i), attributeValue);
        }

        List<String> emptyAttributes = Collections.emptyList();
        Node emptyNode = new Node(baseUri);

        for (String attributeKey : attributeKeys) {
            String attributeValue = emptyNode.attr(attributeKey);
            assertEquals("", attributeValue);
        }
    }

    @Test
    public void testSetAttributes() {
        String baseUri = "http://example.com/";

        Node node = new Node(baseUri);
        Attributes attributes = node.attributes();

        attributes.put("class", "test");
        attributes.put("data-test", "value");

        node.attr("class", "newClass");
        node.attr("data-test", "newValue");

        List<String> attributeKeys = Arrays.asList("class", "data-test");
        List<String> attributesValues = Arrays.asList("newClass", "newValue");

        for (int i = 0; i < attributeKeys.size(); i++) {
            String attributeKey = attributeKeys.get(i);
            String attributeValue = node.attr(attributeKey);
            assertEquals(attributesValues.get(i), attributeValue);
        }

        node.removeAttr("class");
        node.removeAttr("data-test");

        for (String attributeKey : attributeKeys) {
            String attributeValue = node.attr(attributeKey);
            assertEquals("", attributeValue);
        }
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com/";

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("data-test", "value");

        Node node = new Node(baseUri, attributes);

        node.removeAttr("class");

        List<String> attributeKeys = Arrays.asList("class", "data-test");
        List<String> attributesValues = Arrays.asList("", "value");

        for (int i = 0; i < attributeKeys.size(); i++) {
            String attributeKey = attributeKeys.get(i);
            String attributeValue = node.attr(attributeKey);
            assertEquals(attributesValues.get(i), attributeValue);
        }

        node.removeAttr("data-test");

        for (String attributeKey : attributeKeys) {
            String attributeValue = node.attr(attributeKey);
            assertEquals("", attributeValue);
        }
    }

    @Test
    public void testBaseUri() {
        String baseUri = "http://example.com/";
        String newBaseUri = "http://another-example.com/";

        Node node = new Node(baseUri);
        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());

        String href = "relative.html";
        node.attributes.put("href", href);

        String absUrl = node.absUrl("href");
        String expected = newBaseUri + href;

        assertEquals(expected, absUrl);
    }
}