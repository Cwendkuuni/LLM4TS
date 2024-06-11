package org.jsoup.nodes;

import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Mock
    private Attributes attributesMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        leafNode = mock(LeafNode.class, CALLS_REAL_METHODS);
    }

    @Test
    public void testHasAttributes() {
        // Test when value is an instance of Attributes
        when(leafNode.value).thenReturn(attributesMock);
        boolean result = leafNode.hasAttributes();
        assertTrue(result);

        // Test when value is not an instance of Attributes
        when(leafNode.value).thenReturn("foo");
        result = leafNode.hasAttributes();
        assertFalse(result);
    }

    @Test
    public void testAttributes() {
        // Test when value is not an instance of Attributes
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.nodeName()).thenReturn("foo");
        when(leafNode.value).thenReturn("bar");
        Attributes result = leafNode.attributes();
        assertNotNull(result);
        verify(leafNode).ensureAttributes();
        assertEquals("bar", result.get("foo"));

        // Test when value is an instance of Attributes
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.value).thenReturn(attributesMock);
        result = leafNode.attributes();
        assertNotNull(result);
        verify(leafNode, times(2)).ensureAttributes();
        assertEquals(attributesMock, result);
    }

    @Test
    public void testEnsureAttributes() {
        // Test when hasAttributes() returns true
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.nodeName()).thenReturn("foo");
        when(leafNode.value).thenReturn(attributesMock);
        leafNode.ensureAttributes();
        verify(leafNode, never()).attr(anyString(), anyString());

        // Test when hasAttributes() returns false
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.nodeName()).thenReturn("foo");
        when(leafNode.value).thenReturn("bar");
        leafNode.ensureAttributes();
        verify(leafNode).attr("foo", "bar");
        verify(leafNode).hasAttributes();
        verifyNoMoreInteractions(leafNode);
    }

    @Test
    public void testCoreValue() {
        // Test when attr() returns a value
        when(leafNode.attr(anyString())).thenReturn("foo");
        String result = leafNode.coreValue();
        assertEquals("foo", result);
        verify(leafNode).attr(leafNode.nodeName());

        // Test when attr() returns an empty string
        when(leafNode.attr(anyString())).thenReturn("");
        result = leafNode.coreValue();
        assertEquals("", result);
        verify(leafNode, times(2)).attr(leafNode.nodeName());

        // Test when attr() returns null
        when(leafNode.attr(anyString())).thenReturn(null);
        result = leafNode.coreValue();
        assertNull(result);
        verify(leafNode, times(3)).attr(leafNode.nodeName());
    }

    @Test
    public void testCoreValueString() {
        String value = "foo";
        leafNode.coreValue(value);
        verify(leafNode).attr(leafNode.nodeName(), value);
    }

    @Test
    public void testAttr() {
        String key = "foo";
        String value = "bar";

        // Test when hasAttributes() returns true
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.attr(eq(key))).thenCallRealMethod();
        String result = leafNode.attr(key);
        assertNotNull(result);
        verify(leafNode).ensureAttributes();
        verify(leafNode).attr(eq(key));

        // Test when hasAttributes() returns false and key equals nodeName()
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.attr(eq(key))).thenCallRealMethod();
        when(leafNode.nodeName()).thenReturn(key);
        when(leafNode.value).thenReturn(value);
        result = leafNode.attr(key);
        assertEquals(value, result);
        verify(leafNode, times(2)).ensureAttributes();
        verifyNoMoreInteractions(leafNode);
    }

    @Test
    public void testAttrStringString() {
        String key = "foo";
        String value = "bar";

        // Test when hasAttributes() returns true
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.attr(eq(key), eq(value))).thenCallRealMethod();
        leafNode.attr(key, value);
        verify(leafNode).ensureAttributes();
        verify(leafNode).attr(eq(key), eq(value));

        // Test when hasAttributes() returns false and key equals nodeName()
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.attr(eq(key), eq(value))).thenCallRealMethod();
        when(leafNode.nodeName()).thenReturn(key);
        leafNode.attr(key, value);
        verify(leafNode, times(2)).ensureAttributes();
        verify(leafNode).value = value;
    }

    @Test
    public void testHasAttr() {
        String key = "foo";

        // Test when hasAttributes() returns true
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.hasAttr(eq(key))).thenCallRealMethod();
        when(leafNode.keyPresent(eq(key))).thenReturn(true);
        boolean result = leafNode.hasAttr(key);
        assertTrue(result);
        verify(leafNode).ensureAttributes();
        verify(leafNode).hasAttr(eq(key));

        // Test when hasAttributes() returns false and key equals nodeName()
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.hasAttr(eq(key))).thenCallRealMethod();
        when(leafNode.nodeName()).thenReturn(key);
        boolean result = leafNode.hasAttr(key);
        assertFalse(result);
        verify(leafNode, times(2)).ensureAttributes();
        verifyNoMoreInteractions(leafNode);
    }

    @Test
    public void testRemoveAttr() {
        String key = "foo";

        // Test when hasAttributes() returns true
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.removeAttr(eq(key))).thenCallRealMethod();
        when(leafNode.removeAttr(eq(key))).thenCallRealMethod();
        when(leafNode.removeAttr(eq(key))).thenCallRealMethod();
        leafNode.removeAttr(key);
        verify(leafNode).ensureAttributes();
        verify(leafNode).removeAttr(eq(key));

        // Test when hasAttributes() returns false and key equals nodeName()
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.removeAttr(eq(key))).thenCallRealMethod();
        when(leafNode.nodeName()).thenReturn(key);
        leafNode.removeAttr(key);
        verify(leafNode, times(2)).ensureAttributes();
        verify(leafNode).value = null;
    }

    @Test
    public void testAbsUrl() {
        String key = "foo";

        // Test when hasAttributes() returns true
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.absUrl(eq(key))).thenCallRealMethod();
        when(leafNode.ensureAttributes()).thenReturn(true);
        when(leafNode.absUrl(eq(key))).thenCallRealMethod();
        String result = leafNode.absUrl(key);
        assertNotNull(result);
        verify(leafNode).ensureAttributes();
        verify(leafNode).absUrl(eq(key));

        // Test when hasAttributes() returns false
        when(leafNode.hasAttributes()).thenReturn(false);
        when(leafNode.absUrl(eq(key))).thenCallRealMethod();
        result = leafNode.absUrl(key);
        assertNotNull(result);
        verify(leafNode, times(2)).ensureAttributes();
        verifyNoMoreInteractions(leafNode);
    }

    @Test
    public void testBaseUri() {
        // Test when hasParent() returns true
        when(leafNode.hasParent()).thenReturn(true);
        when(leafNode.parent()).thenReturn(mock(Node.class));
        when(leafNode.baseUri()).thenCallRealMethod();
        when(leafNode.parent().baseUri()).thenReturn("foo");
        String result = leafNode.baseUri();
        assertEquals("foo", result);
        verify(leafNode).hasParent();
        verify(leafNode).parent();
        verify(leafNode).baseUri();

        // Test when hasParent() returns false
        when(leafNode.hasParent()).thenReturn(false);
        result = leafNode.baseUri();
        assertEquals("", result);
        verify(leafNode, times(2)).hasParent();
        verifyNoMoreInteractions(leafNode);
    }

    @Test
    public void testDoSetBaseUri() {
        String baseUri = "foo";
        leafNode.doSetBaseUri(baseUri);
        // No assertions, it should be a noop
    }

    @Test
    public void testChildNodeSize() {
        int result = leafNode.childNodeSize();
        assertEquals(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}