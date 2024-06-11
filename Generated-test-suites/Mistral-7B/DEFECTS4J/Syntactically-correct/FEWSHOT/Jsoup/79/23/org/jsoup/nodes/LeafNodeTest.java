package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import static org.jsoup.nodes.Attributes.fromMap;

public class LeafNodeTest {

    @Test
    public void testLeafNodeHasAttributes() {
        LeafNode leafNode = new Element("test");
        Assert.assertFalse(leafNode.hasAttributes());

        Attributes attributes = fromMap("test", "value");
        leafNode.setValue(attributes);
        Assert.assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testLeafNodeCoreValue() {
        LeafNode leafNode = new Element("test");
        leafNode.coreValue("value");
        Assert.assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testLeafNodeCoreValueSetter() {
        LeafNode leafNode = new Element("test");
        leafNode.coreValue("value1");
        leafNode.coreValue("value2");
        Assert.assertEquals("value2", leafNode.coreValue());
    }

    @Test
    public void testLeafNodeAttributeGetters() {
        LeafNode leafNode = new Element("test");
        leafNode.attributes().put("test", "value");

        Assert.assertEquals("value", leafNode.attr("test"));
        Assert.assertNull(leafNode.attr("nonexistent"));
    }

    @Test
    public void testLeafNodeAttributeSetter() {
        LeafNode leafNode = new Element("test");
        leafNode.attr("test", "value1");
        leafNode.attr("test", "value2");

        Assert.assertEquals("value2", leafNode.attr("test"));
    }

    @Test
    public void testLeafNodeHasAttr() {
        LeafNode leafNode = new Element("test");
        leafNode.attr("test", "value");

        Assert.assertTrue(leafNode.hasAttr("test"));
        Assert.assertFalse(leafNode.hasAttr("nonexistent"));
    }

    @Test
    public void testLeafNodeRemoveAttr() {
        LeafNode leafNode = new Element("test");
        leafNode.attr("test", "value");

        leafNode.removeAttr("test");
        Assert.assertFalse(leafNode.hasAttr("test"));
    }

    @Test
    public void testLeafNodeAbsUrl() {
        LeafNode leafNode = new Element("img").attr("src", "test.html");
        Assert.assertEquals("test.html", leafNode.absUrl("src"));
    }

    @Test
    public void testLeafNodeBaseUri() {
        LeafNode leafNode = new Element("test");
        Element parent = new Element("parent");
        parent.add(leafNode);

        Assert.assertEquals("", leafNode.baseUri());
        Assert.assertEquals("/parent", parent.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLeafNodeChildNodeSize() {
        LeafNode leafNode = new Element("test");
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLeafNodeEnsureChildNodes() {
        LeafNode leafNode = new Element("test");
        leafNode.ensureChildNodes();
    }
}