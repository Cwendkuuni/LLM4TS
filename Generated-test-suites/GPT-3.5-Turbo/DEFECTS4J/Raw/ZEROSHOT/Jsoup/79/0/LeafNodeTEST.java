
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertNotNull(leafNode.attributes());
        Assert.assertTrue(leafNode.attributes() instanceof Attributes);
    }

    @Test
    public void testEnsureAttributes() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.ensureAttributes();
        Assert.assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertNull(leafNode.coreValue());

        leafNode.coreValue("test");
        Assert.assertEquals("test", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertNull(leafNode.attr("key"));

        leafNode.ensureAttributes();
        leafNode.attr("key", "value");
        Assert.assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertFalse(leafNode.hasAttr("key"));

        leafNode.ensureAttributes();
        leafNode.attr("key", "value");
        Assert.assertTrue(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertFalse(leafNode.hasAttr("key"));

        leafNode.ensureAttributes();
        leafNode.attr("key", "value");
        Assert.assertTrue(leafNode.hasAttr("key"));

        leafNode.removeAttr("key");
        Assert.assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertNull(leafNode.absUrl("key"));

        leafNode.ensureAttributes();
        leafNode.attr("key", "http://example.com");
        Assert.assertEquals("http://example.com", leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUriWithParent() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertEquals("", leafNode.baseUri());

        Element parentElement = new Element("parent");
        parentElement.appendChild(leafNode);
        parentElement.setBaseUri("http://example.com");
        
        Assert.assertEquals("http://example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.doSetBaseUri("http://example.com");

        // No assertion as method is a noop
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new TestLeafNode();
        Assert.assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TestLeafNode();
        leafNode.ensureChildNodes();
    }

    private static class TestLeafNode extends LeafNode {
        @Override
        public String nodeName() {
            return "test";
        }

    }

}
