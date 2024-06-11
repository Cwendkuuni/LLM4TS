package org.jsoup.nodes;
import static org.junit.Assert.*;
import static org.jsoup.nodes.TextDocument.*;
import static org.jsoup.select.Elements.*;

import org.junit.BeforeClass;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class LeafNodeTest {

    static LeafNode leafNode;
    static LeafNode leafNodeWithAttributes;

    @BeforeClass
    public static void setup() {
        leafNode = new Elements().html("<html><body><div nodeName='test'><p>Test</p></div></body></html>").selectFirst("div[nodeName='test']");
        leafNodeWithAttributes = new Elements().html("<html><body><div nodeName='test' class='test' data-custom='value'><p>Test</p></div></body></html>").selectFirst("div[nodeName='test']");
    }

    @Test
    public void testCoreValue() {
        assertEquals("Test", leafNode.coreValue());
    }

    @Test
    public void testHasAttributes() {
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testSetAndGetAttribute() {
        leafNode.attr("class", "test");
        assertEquals("test", leafNode.attr("class"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAttributeWithEmptyKey() {
        leafNode.attr("", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAttributeWithNullKey() {
        leafNode.attr(null, "test");
    }
}