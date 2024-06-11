package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertNull(leafNode.coreValue());
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.coreValue());
    }
    
    @Test
    public void testCoreValueWithSetCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }
    
    @Test
    public void testAttrWithoutAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertNull(leafNode.attr("key"));
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }
    
    @Test
    public void testAttrWithAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        leafNode.attr("key", "value");
        leafNode.attr("otherKey", "otherValue");
        assertEquals("value", leafNode.attr("key"));
        assertEquals("otherValue", leafNode.attr("otherKey"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertFalse(leafNode.hasAttr("key"));
        
        leafNode.attr("key", "value");
        assertEquals(true, leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        leafNode.attr("key", "value");
        leafNode.removeAttr("key");
        assertEquals(false, leafNode.hasAttr("key"));
    }
    
    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertNull(leafNode.absUrl("key"));
        
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.absUrl("key"));
    }
    
    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertEquals("", leafNode.baseUri());
    }
    
    @Test
    public void testSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        leafNode.setBaseUri("http://example.com");
        assertEquals("", leafNode.baseUri());
    }
    
    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            protected void doSetBaseUri(String baseUri) {

            }
        };
        
        leafNode.ensureChildNodes();
    }
}