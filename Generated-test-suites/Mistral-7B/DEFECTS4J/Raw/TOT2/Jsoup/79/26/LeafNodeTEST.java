package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class LeafNodeTest {
    // ... Test cases for all methods go here ...

    @Test
    public void testHasAttributesEmptyLeafNode() {
        LeafNode leafNode = new LeafNode();
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributesEmptyLeafNode() {
        LeafNode leafNode = new LeafNode();
        Assert.assertNull(leafNode.attributes());
    }

    @Test
    public void testEnsureAttributesEmptyLeafNode() {
        LeafNode leafNode = new LeafNode();
        Attributes attributes = new Attributes();
        leafNode.ensureAttributes();
        Assert.assertEquals(attributes, leafNode.attributes());
    }

    // ... Other test cases for coreValue(), attr(), etc. go here ...

    @Test
    public void testEmptyStringKeyAsValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("test", "");
        String value = leafNode.attr("test");
        Assert.assertEquals("", value);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyAsValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKeyAsValue() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("123", "value");
    }

    // ... Other test cases for multiple attributes, null values, etc. go here ...

    @Test
    public void testMultipleAttributesWithSameKey() {
        LeafNode leafNode = new LeafNode();
        leafNode.attr("test", "value1");
        leafNode.attr("test", "value2");
        String value = leafNode.attr("test");
        Assert.assertEquals("value2", value);
    }

    // ... Other test cases for setting and retrieving attributes with null values, empty strings, etc. go here ...

    @Test
    public void testRemovingNonExistentAttribute() {
        LeafNode leafNode = new LeafNode();
        leafNode.removeAttr("test");
    }

    @Test
    public void testRetrievingNonExistentAttribute() {
        LeafNode leafNode = new LeafNode();
        String value = leafNode.attr("test");
        Assert.assertEquals(EmptyString.INSTANCE, value);
    }

    // ... Other test cases for setting attributes with long keys, keys containing special characters, etc. go here ...
}