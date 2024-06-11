import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeafNodeTest {

    @Mock
    private Attributes attributes;

    private LeafNode leafNode;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        leafNode = mock(LeafNode.class, CALLS_REAL_METHODS);
        leafNode.value = attributes;
    }

    @Test
    public void testHasAttributes() {
        assertTrue(leafNode.hasAttributes());
        leafNode.value = "testValue";
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        Attributes expectedAttributes = new Attributes();
        when(attributes.nodeName()).thenReturn("testNode");
        when(attributes.get("testNode")).thenReturn("testValue");
        when(attributes.put("testNode", "testValue")).thenReturn("testValue");
        when(attributes.hasKey("testKey")).thenReturn(true);

        Attributes actualAttributes = leafNode.attributes();

        assertEquals(expectedAttributes, actualAttributes);
        verify(attributes, times(1)).put("testNode", "testValue");
    }

    @Test
    public void testEnsureAttributes() {
        leafNode.ensureAttributes();
        verify(attributes, times(1)).put(eq("testNode"), anyString());

        leafNode.value = "testValue";
        leafNode.ensureAttributes();
        verify(attributes, never()).put(eq("testNode"), anyString());
    }

    @Test
    public void testCoreValue() {
        when(leafNode.attr("testNode")).thenReturn("testValue");
        String expectedValue = "testValue";
        String actualValue = leafNode.coreValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetCoreValue() {
        leafNode.coreValue("testValue");
        verify(leafNode, times(1)).attr("testNode", "testValue");
    }

    @Test
    public void testAttrWithoutAttributes() {
        leafNode.value = "testValue";
        when(leafNode.nodeName()).thenReturn("testNode");

        String expectedValue = "testValue";
        String actualValue = leafNode.attr("testNode");

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testAttrWithAttributes() {
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.attr("testKey")).thenReturn("testValue");

        String expectedValue = "testValue";
        String actualValue = leafNode.attr("testKey");

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testAttrNullKey() {
        Validate.notNull(null);

        String expectedValue = LeafNode.EmptyString;
        String actualValue = leafNode.attr(null);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetAttrWithoutAttributes() {
        leafNode.value = "testValue";
        when(leafNode.nodeName()).thenReturn("testNode");

        leafNode.attr("testNode", "newValue");

        verify(leafNode, times(1)).attr("testNode", "newValue");
        verify(leafNode, never()).ensureAttributes();
    }

    @Test
    public void testSetAttrWithAttributes() {
        when(leafNode.hasAttributes()).thenReturn(true);

        leafNode.attr("testKey", "testValue");

        verify(leafNode, never()).value = "testValue";
        verify(leafNode, times(1)).ensureAttributes();
        verify(leafNode, times(1)).attr("testKey", "testValue");
    }

    @Test
    public void testHasAttr() {
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.hasAttr("testKey")).thenReturn(true);

        assertTrue(leafNode.hasAttr("testKey"));
        verify(leafNode, times(1)).ensureAttributes();
    }

    @Test
    public void testRemoveAttr() {
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.removeAttr("testKey")).thenReturn(leafNode);

        LeafNode actualNode = leafNode.removeAttr("testKey");

        assertEquals(leafNode, actualNode);
        verify(leafNode, times(1)).ensureAttributes();
        verify(leafNode, times(1)).removeAttr("testKey");
    }

    @Test
    public void testAbsUrl() {
        when(leafNode.absUrl("testKey")).thenReturn("http://www.example.com");

        String expectedUrl = "http://www.example.com";
        String actualUrl = leafNode.absUrl("testKey");

        assertEquals(expectedUrl, actualUrl);
        verify(leafNode, times(1)).ensureAttributes();
    }

    @Test
    public void testBaseUriWithParent() {
        Node parent = mock(Node.class);
        when(leafNode.parent()).thenReturn(parent);
        when(parent.baseUri()).thenReturn("http://www.example.com");

        String expectedUri = "http://www.example.com";
        String actualUri = leafNode.baseUri();

        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void testBaseUriWithoutParent() {
        when(leafNode.hasParent()).thenReturn(false);

        String expectedUri = "";
        String actualUri = leafNode.baseUri();

        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("http://www.example.com");
        // No assertion required, just making sure the method does not throw an exception
    }

    @Test
    public void testChildNodeSize() {
        int expectedSize = 0;
        int actualSize = leafNode.childNodeSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}