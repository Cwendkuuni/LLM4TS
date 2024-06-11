package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setup() {
        leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertFalse(leafNode.hasAttributes());
        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttributes());
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.size() == 1);
        assertEquals("testValue", attributes.get("testAttr"));
    }

    @Test
    public void testAttr() {
        assertNull(leafNode.attr("testAttr"));
        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.attr("testAttr"));
    }

    @Test
    public void testAttr_multipleAttributes() {
        assertNull(leafNode.attr("testAttribute"));
        leafNode.attr("testAttr1", "testValue1");
        leafNode.attr("testAttr2", "testValue2");
        assertNull(leafNode.attr("testAttribute"));
        assertEquals("testValue1", leafNode.attr("testAttr1"));
        assertEquals("testValue2", leafNode.attr("testAttr2"));
    }

    @Test
    public void testAttr_nullKey() {
        try {
            leafNode.attr(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAttr_nullValue() {
        leafNode.attr("testAttr", "testValue");
        assertEquals("testValue", leafNode.attr("testAttr"));
        leafNode.attr("testAttr", null);
        assertNull(leafNode.attr("testAttr"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr("testAttr"));
        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testRemoveAttr() {
        assertFalse(leafNode.hasAttr("testAttr"));
        leafNode.attr("testAttr", "testValue");
        assertTrue(leafNode.hasAttr("testAttr"));
        leafNode.removeAttr("testAttr");
        assertFalse(leafNode.hasAttr("testAttr"));
    }

    @Test
    public void testAbsUrl() {
        assertNull(leafNode.absUrl("testAttr"));
        leafNode.attr("testAttr", "https://www.example.com");
        assertEquals("https://www.example.com", leafNode.absUrl("testAttr"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", leafNode.baseUri());
        leafNode.setParent(new Element("parent"));
        assertEquals("", leafNode.baseUri());
        leafNode.parent().setBaseUri("https://www.example.com");
        assertEquals("https://www.example.com", leafNode.baseUri());
    }

    @Test
    public void testDoSetBaseUri() {
        // No-op, nothing to test
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