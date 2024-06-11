package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jsoup.helper.Validate;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = mock(LeafNode.class);
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
        when(leafNode.value).thenReturn(new Attributes());
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Attributes attributes = mock(Attributes.class);
        when(leafNode.value).thenReturn(attributes);
        assertEquals(attributes, leafNode.attributes());
        verify(leafNode).ensureAttributes();
    }

    @Test
    public void testEnsureAttributes() {
        assertNull(leafNode.value);
        when(leafNode.nodeName()).thenReturn("key");
        when(leafNode.coreValue()).thenReturn("value");
        leafNode.ensureAttributes();
        assertTrue(leafNode.value instanceof Attributes);
        assertEquals("value", ((Attributes) leafNode.value).get("key"));
    }

    @Test
    public void testCoreValue() {
        when(leafNode.nodeName()).thenReturn("key");
        when(leafNode.attr("key")).thenReturn("value");
        assertEquals("value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_set() {
        String key = "key";
        String value = "value";
        when(leafNode.nodeName()).thenReturn(key);
        leafNode.coreValue(value);
        verify(leafNode).attr(key, value);
    }

    @Test
    public void testAttr() {
        String key = "key";
        Validate.notNull(key);
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.nodeName()).thenReturn(key);
        when(leafNode.value).thenReturn("value");
        assertEquals("value", leafNode.attr(key));
        assertEquals("", leafNode.attr("otherKey"));
    }

    @Test
    public void testAttr_set() {
        String key = "key";
        String value = "value";
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.nodeName()).thenReturn(key);
        leafNode.attr(key, value);
        assertEquals(value, leafNode.value);
        verify(leafNode).ensureAttributes();
        verify(leafNode).super().attr(key, value);
    }

    @Test
    public void testHasAttr() {
        String key = "key";
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.super().hasAttr(key)).thenReturn(true);
        assertTrue(leafNode.hasAttr(key));
        assertFalse(leafNode.hasAttr("otherKey"));
    }

    @Test
    public void testRemoveAttr() {
        String key = "key";
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.super().removeAttr(key)).thenReturn(leafNode);
        assertEquals(leafNode, leafNode.removeAttr(key));
        verify(leafNode).ensureAttributes();
        verify(leafNode).super().removeAttr(key);
    }

    @Test
    public void testAbsUrl() {
        String key = "key";
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.super().absUrl(key)).thenReturn("absUrl");
        assertEquals("absUrl", leafNode.absUrl(key));
    }

    @Test
    public void testBaseUri() {
        Node parent = mock(Node.class);
        when(leafNode.hasParent()).thenReturn(true);
        when(leafNode.parent()).thenReturn(parent);
        when(parent.baseUri()).thenReturn("baseUri");
        assertEquals("baseUri", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        String baseUri = "baseUri";
        leafNode.doSetBaseUri(baseUri);
        // No operation, nothing to verify
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