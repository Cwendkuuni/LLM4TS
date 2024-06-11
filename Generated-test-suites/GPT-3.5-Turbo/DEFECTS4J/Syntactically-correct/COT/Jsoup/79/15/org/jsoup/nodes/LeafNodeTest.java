
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new LeafNodeImpl();
    }

    @Test
    public void testHasAttributes() {
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Assert.assertNotNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        Assert.assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        leafNode.coreValue("value");
        Assert.assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        Assert.assertNull(leafNode.attr("key"));
    }

    @Test
    public void testSetAttr() {
        leafNode.attr("key", "value");
        Assert.assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        Assert.assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        Assert.assertEquals(leafNode, leafNode.removeAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        Assert.assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("baseUri");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }

    private static class LeafNodeImpl extends LeafNode {
        @Override
        protected String nodeName() {
            return "nodeName";
        }
    }
}
