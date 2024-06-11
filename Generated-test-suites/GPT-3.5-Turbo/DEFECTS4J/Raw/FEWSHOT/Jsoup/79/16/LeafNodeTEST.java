package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {
    
    private LeafNode leafNode;
    
    @Before
    public void setUp() {
        leafNode = mock(LeafNode.class, CALLS_REAL_METHODS);
    }
    
    @Test
    public void testHasAttributes() {
        when(leafNode.hasAttributes()).thenCallRealMethod();
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        when(leafNode.attributes()).thenCallRealMethod();
        leafNode.value = new Attributes();
        assertEquals(Attributes.class, leafNode.attributes().getClass());
    }
    
    @Test
    public void testEnsureAttributes_whenNoAttributes() {
        when(leafNode.ensureAttributes()).thenCallRealMethod();
        leafNode.value = null;
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testEnsureAttributes_whenAttributesExist() {
        when(leafNode.ensureAttributes()).thenCallRealMethod();
        leafNode.value = new Attributes();
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testCoreValue() {
        when(leafNode.coreValue()).thenCallRealMethod();
        leafNode.value = "test";
        assertEquals("test", leafNode.coreValue());
    }
    
    @Test
    public void testCoreValue_whenNoValue() {
        when(leafNode.coreValue()).thenCallRealMethod();
        leafNode.value = null;
        assertEquals("", leafNode.coreValue());
    }
    
    @Test
    public void testCoreValue_setValue() {
        when(leafNode.coreValue(anyString())).thenCallRealMethod();
        leafNode.value = null;
        leafNode.coreValue("test");
        assertEquals("test", leafNode.attr(leafNode.nodeName()));
    }
    
    @Test
    public void testAttr() {
        when(leafNode.attr(anyString())).thenCallRealMethod();
        leafNode.value = "test";
        assertEquals("test", leafNode.attr(leafNode.nodeName()));
    }
    
    @Test
    public void testAttr_whenNoValue() {
        when(leafNode.attr(anyString())).thenCallRealMethod();
        leafNode.value = null;
        assertEquals("", leafNode.attr(leafNode.nodeName()));
    }
    
    @Test
    public void testAttr_withKeyAndValue() {
        when(leafNode.attr(anyString(), anyString())).thenCallRealMethod();
        leafNode.value = "test";
        leafNode.attr(leafNode.nodeName(), "value");
        assertEquals("value", leafNode.attr(leafNode.nodeName()));
    }
    
    @Test
    public void testHasAttr() {
        when(leafNode.hasAttr(anyString())).thenCallRealMethod();
        leafNode.value = "test";
        assertTrue(leafNode.hasAttr(leafNode.nodeName()));
    }
    
    @Test
    public void testRemoveAttr() {
        when(leafNode.removeAttr(anyString())).thenCallRealMethod();
        leafNode.value = "test";
        leafNode.removeAttr(leafNode.nodeName());
        assertNull(leafNode.attr(leafNode.nodeName()));
    }
    
    @Test
    public void testAbsUrl() {
        when(leafNode.absUrl(anyString())).thenCallRealMethod();
        leafNode.value = "test";
        assertEquals("test", leafNode.absUrl(leafNode.nodeName()));
    }
    
    @Test
    public void testBaseUri() {
        when(leafNode.baseUri()).thenCallRealMethod();
        assertTrue(leafNode.baseUri().isEmpty());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        when(leafNode.ensureChildNodes()).thenCallRealMethod();
        leafNode.ensureChildNodes();
    }
    
    @Test
    public void testChildNodeSize() {
        when(leafNode.childNodeSize()).thenCallRealMethod();
        assertEquals(0, leafNode.childNodeSize());
    }
    
}