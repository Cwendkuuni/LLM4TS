package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new Elements().html().body().first();
        assertFalse(leafNode.hasAttributes());

        Element element = new Element("div");
        element.attr("class", "test");
        assertFalse(element.hasAttributes());
        assertEquals("test", element.attr("class"));
        assertTrue(element.hasAttr("class"));
        assertSame(element, element.attributes().put("data-test", "test"));
        assertTrue(element.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new Elements().html().body().first();
        assertNull(leafNode.attributes());

        Element element = new Element("div");
        element.attr("class", "test");
        Attributes attributes = element.attributes();
        assertEquals(1, attributes.size());
        assertEquals("test", attributes.get("class"));
    }

    @Test
    public void testCoreValue() {
        Element element = new Element("div").attr("id", "test");
        assertEquals("div", element.nodeName());
        assertEquals("test", element.coreValue());
        element.coreValue("newValue");
        assertEquals("newValue", element.coreValue());
        assertEquals("test", element.attr("id"));
    }

    @Test
    public void testCoreValueSetting() {
        Element element = new Element("div");
        element.coreValue("value");
        assertEquals("value", element.attr(element.nodeName()));
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new Elements().html().body().first();
        assertFalse(leafNode.hasAttr("class"));

        Element element = new Element("div").attr("class", "test");
        assertFalse(element.hasAttr("class", "other"));
        assertTrue(element.hasAttr("class"));
        assertTrue(element.hasAttr(element.nodeName()));
    }

    @Test
    public void testAttrGetSet() {
        Element element = new Element("div");
        String value = "test";
        element.attr("class", value);
        assertEquals(value, element.attr("class"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttr() {
        Element element = new Element("div").attr("class", "test");
        element.removeAttr("class");
    }

    @Test
    public void testAbsUrl() {
        Element element = new Element("a").attr("href", "/test");
        assertEquals("/test", element.absUrl("href"));
    }

    @Test
    public void testBaseUri() {
        Element element = new Element("div").parent(new Element("html").attr("baseURI", "http://test.com"));
        assertEquals("http://test.com", element.baseUri());
    }

    @Test
    public void testChildNodeSize() {
        Element element = new Element("div");
        assertEquals(0, element.childNodeSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        Element element = new Element("div");
        element.ensureChildNodes();
    }
}