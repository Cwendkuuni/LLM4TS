
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.attr("key"));
    }

    @Test
    public void testSetAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.removeAttr("key"));
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNodeImpl();
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.doSetBaseUri("http://example.com");
        // No assert as this method is a noop
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNodeImpl();
        assertEquals(0, leafNode.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.ensureChildNodes();
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test
    public void testEnsureAttributesWithExistingAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testEnsureAttributesWithNullCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.coreValue(null);
        assertTrue(leafNode.hasAttr("nodeName"));
    }
    
    @Test
    public void testEnsureAttributesWithNonNullCoreValue() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.coreValue("value");
        assertTrue(leafNode.hasAttr("nodeName"));
    }
    
    @Test
    public void testRemoveExistingAttr() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.attr("key", "value");
        assertNotNull(leafNode.removeAttr("key"));
        assertFalse(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testAbsUrlWithExistingAttributes() {
        LeafNode leafNode = new LeafNodeImpl();
        leafNode.attr("abs:href", "http://example.com");
        assertEquals("http://example.com", leafNode.absUrl("href"));
    }
    
    private static class LeafNodeImpl extends LeafNode {
        
        @Override
        protected String nodeName() {
            return "nodeName";
        }
    }
}
