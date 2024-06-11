package org.jsoup.nodes; // update the package name if necessary

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeConstruction() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        Node node1 = new Node(baseUri, attributes);

        String nodeName1 = node1.nodeName();
        Assert.assertEquals("Node name should not be null", nodeName1, "");

        String attrValue1 = node1.attr("class");
        Assert.assertEquals("Attribute value should match", attrValue1, "test");

        Node node2 = new Node(baseUri);
        String nodeName2 = node2.nodeName();
        Assert.assertEquals("Node name should not be null", nodeName2, "");

        Node node3 = new Node();
        List<Node> childNodes3 = node3.childNodes();
        Assert.assertFalse("Child nodes should be empty", childNodes3.isEmpty());
        Assert.assertEquals("Number of child nodes", childNodes3.size(), 0);
    }

    @Test
    public void testMethodsThatReturnEmptyListOrArray() {
        Node node = new Node("http://example.com");

        List<Node> childNodes = node.childNodes();
        Assert.assertFalse("Child nodes should be empty", childNodes.isEmpty());
        Assert.assertEquals("Number of child nodes", childNodes.size(), 0);

        Node[] childNodesArray = node.childNodesAsArray();
        Assert.assertArrayEquals("Child nodes array should be empty", new Node[0], childNodesArray);
    }

    @Test
    public void testGettingParentNode() {
        Node parentNode = new Node("http://parent.com");
        Node childNode = new Node("http://child.com");
        childNode.setParentNode(parentNode);

        Node parent = childNode.parent();
        Assert.assertEquals("Parent node should match", parent, parentNode);

        Node noParentNode = new Node("http://no-parent.com");
        Node parentOfNoParent = noParentNode.parent();
        Assert.assertNull("No parent node", parentOfNoParent);
    }

    @Test
    public void testSettingParentNode() {
        Node childNode = new Node("http://child.com");
        Node parentNode = new Node("http://parent.com");

        childNode.setParentNode(parentNode);
        Node parent = childNode.parent();
        Assert.assertEquals("Parent node should match", parent, parentNode);

        childNode.setParentNode(null);
        Node noParent = childNode.parent();
        Assert.assertNull("No parent node", noParent);
    }

    @Test
    public void testGettingChildren() {
        Node parentNode = new Node("http://example.com");
        Node child1 = new Node("http://child1.com");
        Node child2 = new Node("http://child2.com");

        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        Assert.assertEquals("Number of children", children.size(), 2);
        Assert.assertTrue("Children should include child1", children.contains(child1));
        Assert.assertTrue("Children should include child2", children.contains(child2));

        Node[] childrenArray = parentNode.childNodesAsArray();
        Assert.assertArrayEquals("Children array should include child1", new Node[]{child1, child2}, childrenArray);
    }

    @Test
    public void testGettingAttributes() {
        Node node = new Node("http://example.com");
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        node.setAttributes(attributes);

        List<String> attributesList = node.attributes().list();
        Assert.assertEquals("Number of attributes", attributesList.size(), 1);
        Assert.assertEquals("Attribute name", attributesList.get(0), "class");
        Assert.assertEquals("Attribute value", node.attr("class"), "test");

        // Test for getting empty attribute list
        Node emptyAttributesNode = new Node("http://empty.com");
        List<String> emptyAttributesList = emptyAttributesNode.attributes().list();
        Assert.assertEquals("Number of attributes", emptyAttributesList.size(), 0);
        Assert.assertFalse("Empty attribute list should not contain any keys", emptyAttributesList.contains("class"));
    }

    @Test
    public void testSettingAttributes() {
        Node node = new Node("http://example.com");
        Attributes attributes = new Attributes();
        attributes.put("class", "test");

        node.setAttributes(attributes);

        List<String> attributesList = node.attributes().list();
        Assert.assertEquals("Number of attributes", attributesList.size(), 1);
        Assert.assertEquals("Attribute name", attributesList.get(0), "class");
        Assert.assertEquals("Attribute value", node.attr("class"), "test");
    }

    @Test
    public void testSettingAndGettingAttributeValue() {
        Node node = new Node("http://example.com");
        node.setAttr("class", "test");

        List<String> attributesList = node.attributes().list();
        Assert.assertEquals("Number of attributes", attributesList.size(), 1);
        Assert.assertEquals("Attribute name", attributesList.get(0), "class");
        Assert.assertEquals("Attribute value", node.attr("class"), "test");

        // Test for setting and getting an empty attribute value
        Node nodeWithEmptyAttribute = new Node("http://example.com");
        nodeWithEmptyAttribute.setAttr("class", "");

        List<String> attributesListWithEmptyAttribute = nodeWithEmptyAttribute.attributes().list();
        Assert.assertEquals("Number of attributes", attributesListWithEmptyAttribute.size(), 1);
        Assert.assertEquals("Attribute name", attributesListWithEmptyAttribute.get(0), "class");
        Assert.assertEquals("Attribute value", nodeWithEmptyAttribute.attr("class"), "");
    }

    @Test
    public void testRemovingAnAttribute() {
        Node node = new Node("http://example.com");
        node.setAttr("class", "test");

        List<String> attributesListBeforeRemoval = node.attributes().list();
        Assert.assertEquals("Number of attributes before removal", attributesListBeforeRemoval.size(), 1);

        node.removeAttr("class");

        List<String> attributesListAfterRemoval = node.attributes().list();
        Assert.assertEquals("Number of attributes after removal", attributesListAfterRemoval.size(), 0);
    }

    @Test
    public void testGettingBaseUri() {
        Node node = new Node("http://example.com");
        String baseUri = node.baseUri();
        Assert.assertEquals("Base URI should match", baseUri, "http://example.com");
    }

    @Test
    public void testSettingBaseUri() {
        Node node = new Node("");
        String baseUri = "http://example.com";
        node.setBaseUri(baseUri);
        String nodeBaseUri = node.baseUri();
        Assert.assertEquals("Base URI should match", nodeBaseUri, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testSettingBaseUriWithNullBaseUri() {
        Node node = new Node("");
        node.setBaseUri(null);
    }

    @Test
    public void testGettingAbsUrl() {
        Node node = new Node("http://example.com");
        String attributeValue = "http://sub.example.com";

        node.setAttr("abs:href", attributeValue);
        String absUrl = node.absUrl("href");
        Assert.assertEquals("Absolute URL should match", absUrl, "http://example.com/http://sub.example.com");

        String baseUri = "http://example.com";
        node = new Node(baseUri);
        String relUrl = "sub.html";
        String absUrlFromBase = node.absUrl(relUrl);
        Assert.assertEquals("Absolute URL from base URI should match", absUrlFromBase, "http://example.com/sub.html");
    }

    @Test(expected = NullPointerException.class)
    public void testGettingAbsUrlWithNullAttributeKey() {
        Node node = new Node("http://example.com");
        node.setBaseUri("http://example.com");
        node.setAttributes(new Attributes());
        node.getAbsUrl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGettingAbsUrlWithEmptyAttributeKey() {
        Node node = new Node("http://example.com");
        node.setBaseUri("http://example.com");
        node.setAttributes(new Attributes());
        node.getAbsUrl("");
    }

    @Test(expected = MalformedURLException.class)
    public void testGettingAbsUrlWithInvalidRelativeUrl() {
        Node node = new Node("http://example.com");
        node.setBaseUri("http://example.com");
        node.setAttributes(new Attributes());
        node.getAbsUrl("invalid_url");
    }

    @Test
    public void testGettingAbsUrlWithAbsoluteUrlAsAttributeValue() {
        Node node = new Node("http://example.com");
        String absoluteUrl = "http://example.com/sub.html";
        node.setAttr("abs:href", absoluteUrl);
        String absUrl = node.absUrl("href");
        Assert.assertEquals("Absolute URL should match", absUrl, absoluteUrl);
    }

    @Test
    public void testGettingAbsUrlWithEmptyBaseUri() {
        Node node = new Node("");
        String relUrl = "sub.html";
        String absUrl = node.absUrl(relUrl);
        Assert.assertEquals("Absolute URL should be empty", absUrl, "");
    }

    @Test
    public void testGettingAbsUrlWithNullBaseUri() {
        Node node = new Node("");
        node.setAttributes(new Attributes());
        String relUrl = "sub.html";
        String absUrl = node.absUrl(relUrl);
        Assert.assertEquals("Absolute URL should be empty", absUrl, "");
    }
}