package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeafNodeTest {
    
    private LeafNode leafNode;
    
    @Before
    public void setUp() {
        leafNode = mock(LeafNode.class);
    }
    
    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());
    }
    
    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        verify(leafNode).nodeName();
    }
    
    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
    }
    
    @Test
    public void testSetCoreValue() {
        String value = "test";
        leafNode.coreValue(value);
        assertEquals(value, leafNode.coreValue());
        verify(leafNode).attr(leafNode.nodeName(), value);
    }
    
    @Test
    public void testAttrWithoutAttributes() {
        String key = "test";
        String value = "value";
        when(leafNode.nodeName()).thenReturn(key);
        when(leafNode.attr(key)).thenCallRealMethod();
        String attrValue = leafNode.attr(key);
        assertEquals(value, attrValue);
    }
    
    @Test
    public void testAttrWithAttributes() {
        String key = "test";
        String value = "value";
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.attr(key)).thenCallRealMethod();
        when(leafNode.super.attr(key)).thenReturn(value);
        String attrValue = leafNode.attr(key);
        assertEquals(value, attrValue);
    }
    
    @Test
    public void testAttrNullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            leafNode.attr(null);
        });
    }
    
    @Test
    public void testAttrSetValue() {
        String key = "test";
        String value = "value";
        leafNode.attr(key, value);
        verify(leafNode).ensureAttributes();
        verify(leafNode).super.attr(key, value);
    }
    
    @Test
    public void testHasAttr() {
        String key = "test";
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.hasAttr(key)).thenCallRealMethod();
        assertTrue(leafNode.hasAttr(key));
        verify(leafNode).ensureAttributes();
        verify(leafNode).super.hasAttr(key);
    }
    
    @Test
    public void testHasAttrWithoutAttributes() {
        String key = "test";
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.hasAttr(key)).thenCallRealMethod();
        assertFalse(leafNode.hasAttr(key));
    }
    
    @Test
    public void testRemoveAttr() {
        String key = "test";
        when(leafNode.removeAttr(key)).thenCallRealMethod();
        Node node = leafNode.removeAttr(key);
        verify(leafNode).ensureAttributes();
        verify(leafNode).super.removeAttr(key);
        assertEquals(leafNode, node);
    }
    
    @Test
    public void testAbsUrl() {
        String key = "test";
        when(leafNode.absUrl(key)).thenCallRealMethod();
        String absUrl = leafNode.absUrl(key);
        verify(leafNode).ensureAttributes();
        verify(leafNode).super.absUrl(key);
        assertNull(absUrl);
    }
    
    @Test
    public void testBaseUri() {
        when(leafNode.baseUri()).thenCallRealMethod();
        String baseUri = leafNode.baseUri();
        assertTrue(baseUri.isEmpty());
        verify(leafNode).hasParent();
        verify(leafNode).parent();
    }
    
    @Test
    public void testDoSetBaseUri() {
        String baseUri = "test";
        leafNode.doSetBaseUri(baseUri);
        // No operation
    }
    
    @Test
    public void testChildNodeSize() {
        assertEquals(0, leafNode.childNodeSize());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
    
}