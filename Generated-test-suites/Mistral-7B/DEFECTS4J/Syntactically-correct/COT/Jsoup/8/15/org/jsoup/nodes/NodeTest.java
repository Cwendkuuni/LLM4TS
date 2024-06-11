package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Node.
 */
public class NodeTest {

    @Test
    public void testNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("attrKey1", "attrValue1");
        Node node = new Node(baseUri, attributes);

        assertEquals("Node name should be defined", "", node.nodeName());
        assertEquals("Base URI should be set", baseUri, node.baseUri());
        assertNotNull("Attributes should not be null", node.attributes());
        assertFalse("Should not have any children by default", node.hasChildren());

        Node node2 = new Node(baseUri);
        assertEquals("Default constructor should create an empty node", "", node2.nodeName());
        assertNull("Default constructor should not set base URI", node2.baseUri());
        assertNull("Default constructor should not set attributes", node2.attributes());
    }

    @Test
    public void testAttr() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("attrKey1", "attrValue1");
        Node node = new Node(baseUri, attributes);

        String attrValue = node.attr("attrKey1");
        assertEquals("Attribute value should be retrieved correctly", "attrValue1", attrValue);

        String nonExistentAttr = node.attr("nonExistentAttr");
        assertEquals("Attribute value should be empty string if not present", "", nonExistentAttr);

        String absUrl = node.absUrl("attrKey1");
        assertEquals("Absolute URL from relative attribute should be created correctly", baseUri + "/attrValue1", absUrl);

        String urlString = "http://example.com/path/to/url";
        URL url = new URL(urlString);
        String absUrl2 = node.absUrl("abs:urlString");
        assertEquals("Absolute URL from absolute attribute string should be created correctly", url.toExternalForm(), absUrl2);
    }

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("attrKey1", "attrValue1");
        attributes.put("attrKey2", "attrValue2");

        Node node = new Node("", attributes);
        List<String> keys = new ArrayList<>(Arrays.asList("attrKey1", "attrKey2"));
        List<String> values = new ArrayList<>(Arrays.asList("attrValue1", "attrValue2"));

        for (String key : keys) {
            String value = node.attr(key);
            assertEquals("Attribute value should be retrieved correctly", value, values.get(values.indexOf(value)));
        }

        node.removeAttr("attrKey1");
        String removedAttr = node.attr("attrKey1");
        assertEquals("Attribute value should be empty string after removal", "", removedAttr);

        node.attr("attrKey3", "attrValue3");
        String newAttr = node.attr("attrKey3");
        assertEquals("Newly added attribute value should be retrieved correctly", "attrValue3", newAttr);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributes() {
        new Node("", null);
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedBaseUri() throws MalformedURLException {
        new Node("malformed://example.com", new Attributes());
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);

        Node childNode1 = new Node(baseUri, new Attributes());
        Node childNode2 = new Node(baseUri, new Attributes());

        parentNode.addChildren(childNode1, childNode2);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals("Number of children should be correct", 2, childNodes.size());
        assertTrue("Child nodes should be present", parentNode.hasChildren());

        Node childNode3 = new Node(baseUri, new Attributes());
        parentNode.replaceChild(childNode2, childNode3);
        List<Node> updatedChildNodes = parentNode.childNodes();
        assertEquals("Child node should be replaced", childNode3, updatedChildNodes.get(1));

        Node removedChildNode = parentNode.childNodes().get(0);
        parentNode.removeChild(removedChildNode);
        List<Node> removedChildNodes = parentNode.childNodes();
        assertEquals("Child node should be removed", childNode1, removedChildNodes.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullParentNode() {
        Node childNode = new Node();
        childNode.setParentNode(null);
    }
}