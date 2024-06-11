import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            protected String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.value = new Attributes();
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertFalse(leafNode.hasAttributes());
        Attributes attributes = leafNode.attributes();
        assertTrue(leafNode.hasAttributes());
        assertEquals(attributes, leafNode.attributes());
    }

    @Test
    public void testEnsureAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.ensureAttributes();
        assertTrue(leafNode.hasAttributes());
        assertTrue(leafNode.value instanceof Attributes);
        assertEquals(1, leafNode.attributes().size());
    }

    @Test
    public void testCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testSetCoreValue() {
        assertNull(leafNode.coreValue());
        leafNode.coreValue("testValue");
        assertEquals("testValue", leafNode.coreValue());
    }

    @Test
    public void testAttrWithoutAttributes() {
        assertNull(leafNode.attr("key"));
        leafNode.value = "testValue";
        assertEquals("testValue", leafNode.attr("testNode"));
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void testAttrWithAttributes() {
        assertNull(leafNode.attr("key"));
        leafNode.value = new Attributes();
        leafNode.attr("testNode", "testValue");
        assertEquals("testValue", leafNode.attr("testNode"));
        assertEquals("", leafNode.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("key"));
        leafNode.value = new Attributes();
        leafNode.attributes().put("testNode", "testValue");
        assertTrue(leafNode.hasAttr("testNode"));
        assertFalse(leafNode.hasAttr("key"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("testNode"));
        leafNode.value = new Attributes();
        leafNode.attributes().put("testNode", "testValue");
        assertTrue(leafNode.hasAttr("testNode"));
        leafNode.removeAttr("testNode");
        assertFalse(leafNode.hasAttr("testNode"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("key"));
        leafNode.value = new Attributes();
        leafNode.attributes().put("testNode", "testValue");
        assertEquals("testValue", leafNode.absUrl("testNode"));
        assertNull(leafNode.absUrl("key"));
    }

    @Test
    public void testBaseUriWithoutParent() {
        assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testBaseUriWithParent() {
        Node parent = mock(Node.class);
        when(parent.baseUri()).thenReturn("parentUri");
        leafNode.setParentNode(parent);
        assertEquals("parentUri", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        leafNode.doSetBaseUri("testBaseUri");
        // No assertions, as the method is a noop
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