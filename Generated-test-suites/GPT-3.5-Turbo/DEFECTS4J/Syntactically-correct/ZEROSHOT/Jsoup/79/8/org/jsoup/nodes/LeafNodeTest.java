import org.junit.Test;
import org.mockito.Mock;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeafNodeTest {

    @Mock
    private Attributes attributesMock;
    
    @Test
    public void testHasAttributes() {
        LeafNode leafNode = createLeafNode();
        assertTrue(leafNode.hasAttributes());
    }
    
    @Test
    public void testAttributes() {
        LeafNode leafNode = createLeafNode();
        assertNotNull(leafNode.attributes());
    }
    
    @Test
    public void testCoreValue() {
        LeafNode leafNode = createLeafNode();
        assertNull(leafNode.coreValue());
        leafNode.coreValue("value");
        assertEquals("value", leafNode.coreValue());
    }
    
    @Test
    public void testAttr() {
        LeafNode leafNode = createLeafNode();
        assertNull(leafNode.attr("key"));
        leafNode.attr("key", "value");
        assertEquals("value", leafNode.attr("key"));
    }
    
    @Test
    public void testHasAttr() {
        LeafNode leafNode = createLeafNode();
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
    }
    
    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = createLeafNode();
        assertFalse(leafNode.hasAttr("key"));
        leafNode.attr("key", "value");
        assertTrue(leafNode.hasAttr("key"));
        leafNode.removeAttr("key");
        assertFalse(leafNode.hasAttr("key"));
    }
    
    private LeafNode createLeafNode() {
        LeafNode leafNode = mock(LeafNode.class);
        when(leafNode.hasAttributes()).thenReturn(true);
        when(leafNode.attributes()).thenReturn(attributesMock);
        return leafNode;
    }
}