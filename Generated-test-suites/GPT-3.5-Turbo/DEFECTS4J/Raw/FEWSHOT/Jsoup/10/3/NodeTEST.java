package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class NodeTest {
    @Test
    public void testNodeName() {
        Element node = new Element("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        Element node = new Element("div");
        assertNull(node.attr("class"));
        node.attr("class", "test");
        assertEquals("test", node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        Element node = new Element("div");
        assertFalse(node.hasAttr("class"));
        node.attr("class", "test");
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        Element node = new Element("div");
        node.attr("class", "test");
        assertTrue(node.hasAttr("class"));
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        Element node = new Element("div");
        assertNull(node.baseUri());
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testSiblingIndex() {
        Element node = new Element("div");
        assertNull(node.siblingIndex());
        node.setSiblingIndex(1);
        assertEquals(1, node.siblingIndex());
    }

    @Test
    public void testOuterHtml() {
        Element node = new Element("div");
        node.attr("class", "test");
        assertEquals("<div class=\"test\"></div>", node.outerHtml());
    }
    
    @Test
    public void testClone() {
        Element node = new Element("div");
        node.attr("class", "test");
        Element clone = node.clone();
        assertEquals(clone.nodeName(), node.nodeName());
        assertEquals(clone.attr("class"), node.attr("class"));
        assertFalse(clone == node);
    }
}