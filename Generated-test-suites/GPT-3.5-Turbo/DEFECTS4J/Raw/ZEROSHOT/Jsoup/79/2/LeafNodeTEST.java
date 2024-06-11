package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        this.leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Attributes attributes = leafNode.attributes();
        Assert.assertNotNull(attributes);
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        Assert.assertTrue(leafNode.hasAttributes());
        Assert.assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testCoreValue() {
        leafNode.coreValue("value");
        Assert.assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        leafNode.attr("key", "value");
        Assert.assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        Assert.assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        Assert.assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr("key1", "value1");
        Assert.assertTrue(leafNode.hasAttr("key1"));
        leafNode.removeAttr("key1");
        Assert.assertFalse(leafNode.hasAttr("key1"));
    }

    @Test
    public void testAbsUrl() {
        leafNode.attr("absUrl", "http://example.com");
        Assert.assertEquals("http://example.com", leafNode.absUrl("absUrl"));
    }

    @Test
    public void testBaseUri() {
        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://example.com");
        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        Assert.assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}