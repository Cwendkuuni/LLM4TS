package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Node class.
 */
public class NodeTest {
    private Node node;

    @Before
    public void setup() {
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        attributes.put("testAttr", "testValue");
        node = new Node(baseUri, attributes);
    }

    @Test
    public void testNodeName() {
        String expectedNodeName = "Element"; // Assuming Node is an abstract class for Elements
        assertEquals(expectedNodeName, node.nodeName());
    }

    @Test
    public void testAttr() {
        String attributeKey = "testAttr";
        String attributeValue = "testValue";
        String expectedAttributeValue = "testValue";

        node.attr(attributeKey, attributeValue);
        String actualAttributeValue = node.attr(attributeKey);

        assertEquals(expectedAttributeValue, actualAttributeValue);
    }

    @Test
    public void testAbsUrlWithAbsoluteUrl() {
        String attributeKey = "testAbsUrl";
        String absoluteUrl = "https://example.com/test.html";
        String expectedAbsoluteUrl = absoluteUrl;

        node.attr(attributeKey, absoluteUrl);
        String actualAbsoluteUrl = node.absUrl(attributeKey);

        assertEquals(expectedAbsoluteUrl, actualAbsoluteUrl);
    }

    @Test
    public void testAbsUrlWithRelativeUrl() {
        String attributeKey = "testAbsUrl";
        String relativeUrl = "test.html";
        String expectedAbsoluteUrl = "https://example.com/test.html";

        node.attr(attributeKey, relativeUrl);
        String actualAbsoluteUrl = node.absUrl(attributeKey);

        assertEquals(expectedAbsoluteUrl, actualAbsoluteUrl);
    }

    @Test
    public void testAbsUrlWithMissingAttribute() {
        String attributeKey = "testAbsUrl";

        String actualAbsoluteUrl = node.absUrl(attributeKey);

        assertNull(actualAbsoluteUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testAbsUrlWithInvalidUrl() {
        String attributeKey = "testAbsUrl";
        String invalidUrl = "invalid:url";

        node.attr(attributeKey, invalidUrl);
        String actualAbsoluteUrl = node.absUrl(attributeKey);
    }

    @Test
    public void testHasAttr() {
        String attributeKey = "testAttr";

        assertTrue(node.hasAttr(attributeKey));
    }

    @Test
    public void testHasAttrWithMissingAttribute() {
        String attributeKey = "missingAttr";

        assertFalse(node.hasAttr(attributeKey));
    }

    @Test
    public void testRemoveAttr() {
        String attributeKey = "testAttr";
        String expectedEmptyString = "";

        node.removeAttr(attributeKey);
        String actualEmptyString = node.attr(attributeKey);

        assertEquals(expectedEmptyString, actualEmptyString);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveAttrWithNullAttributeKey() {
        String attributeKey = null;

        node.removeAttr(attributeKey);
    }

    @Test
    public void testSetBaseUri() {
        String newBaseUri = "https://example.net";

        node.setBaseUri(newBaseUri);
        String expectedNewBaseUri = newBaseUri;

        assertEquals(expectedNewBaseUri, node.baseUri());
    }

    @Test
    public void testGetChildNode() {
        Node childNode = new Node("child");
        node.childNodes.add(childNode);
        Node expectedChild = childNode;

        Node actualChild = node.childNode(0);

        assertSame(expectedChild, actualChild);
    }

    @Test
    public void testGetChildren() {
        Node childNode1 = new Node("child1");
        Node childNode2 = new Node("child2");
        node.childNodes.clear();
        node.childNodes.addAll(Arrays.asList(childNode1, childNode2));
        List<Node> expectedChildren = Arrays.asList(childNode1, childNode2);
        List<Node> actualChildren = node.childNodes();

        assertEquals(expectedChildren, actualChildren);
    }

    @Test
    public void testGetParent() {
        Node parentNode = new Node("parent");
        parentNode.childNodes.add(node);

        Node expectedParent = parentNode;
        Node actualParent = node.parent();

        assertSame(expectedParent, actualParent);
    }

    @Test
    public void testClone() {
        Node clonedNode = node.clone();
        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new Node("child1"));
        childNodes.add(new Node("child2"));
        node.childNodes = childNodes;
        node.attributes = new Attributes();
        node.attributes.put("testAttr", "testValue");

        Node clonedChild1 = clonedNode.childNodes(0);
        Node clonedChild2 = clonedNode.childNodes(1);
        List<Node> clonedChildren = clonedNode.childNodes();
        Attributes clonedAttributes = clonedNode.attributes();

        assertEquals(childNodes.get(0), clonedChild1);
        assertEquals(childNodes.get(1), clonedChild2);
        assertEquals(clonedChildren, clonedNode.childNodes());
        assertEquals(node.attributes(), clonedAttributes);
        assertFalse(clonedNode.equals(node));
    }
}