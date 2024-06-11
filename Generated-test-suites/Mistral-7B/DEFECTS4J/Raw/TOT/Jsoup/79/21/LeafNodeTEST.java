package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                put("id", "test");
            }
        };

        assertTrue(leafNode.hasAttributes());

        LeafNode leafNodeWithoutAttributes = new LeafNode();
        assertFalse(leafNodeWithoutAttributes.hasAttributes());
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "Test Value";
            }
        };

        assertEquals("Test Value", leafNode.coreValue());

        LeafNode leafNodeWithAttributes = new LeafNode() {
            {
                value = new Attributes();
            }
        };
        assertNull(leafNodeWithAttributes.coreValue());
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr("test", "Test Value");
        assertEquals("Test Value", leafNode.attr("test"));

        leafNode.attr("test", null);
        assertNull(leafNode.attr("test"));

        leafNode.attr("other", "Other Value");
        assertEquals("Other Value", leafNode.attr("other"));
    }

    @Test
    public void testAttrSetting() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr("test", "Test Value");
        assertEquals("Test Value", leafNode.attr("test"));

        leafNode.attr("test", "New Test Value");
        assertEquals("New Test Value", leafNode.attr("test"));
    }

    @Test
    public void testAttrRemoval() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes() {
                    {
                        put("test", "Test Value");
                    }
                };
            }
        };

        assertEquals("Test Value", leafNode.attr("test"));

        leafNode.removeAttr("test");
        assertNull(leafNode.attr("test"));
    }

    @Test
    public void testAttrCaseSensitivity() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes() {
                    {
                        put("TEST", "Test Value");
                    }
                };
            }
        };

        assertEquals("Test Value", leafNode.attr("TEST"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttrNullKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr(null, "Test Value");
    }

    @Test
    public void testAttrEmptyStringValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr("test", "");
        assertEquals("", leafNode.attr("test"));
    }
}