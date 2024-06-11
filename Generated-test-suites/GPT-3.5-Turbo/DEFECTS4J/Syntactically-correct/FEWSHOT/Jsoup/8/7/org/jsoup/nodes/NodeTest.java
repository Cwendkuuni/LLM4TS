package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeName() {
        Element node = new Element("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        Element node = new Element("div");
        node.attr("class", "container");
        assertEquals("container", node.attr("class"));
    }

    @Test
    public void testHasAttr() {
        Element node = new Element("div");
        node.attr("class", "container");
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        Element node = new Element("div");
        node.attr("class", "container");
        node.removeAttr("class");
        assertNull(node.attr("class"));
    }

    @Test
    public void testBaseUri() {
        Element node = new Element("div");
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Element node = new Element("a");
        node.setBaseUri("http://example.com");
        node.attr("href", "page.html");
        assertEquals("http://example.com/page.html", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Element node = new Element("div");
        node.appendElement("p");
        assertEquals("p", node.childNode(0).nodeName());
    }

    @Test
    public void testNextSibling() {
        Element parent = new Element("div");
        Element node1 = new Element("p");
        Element node2 = new Element("span");
        parent.appendChild(node1);
        parent.appendChild(node2);
        assertEquals("span", node1.nextSibling().nodeName());
    }

    @Test
    public void testPreviousSibling() {
        Element parent = new Element("div");
        Element node1 = new Element("p");
        Element node2 = new Element("span");
        parent.appendChild(node1);
        parent.appendChild(node2);
        assertEquals("p", node2.previousSibling().nodeName());
    }

    @Test
    public void testOuterHtml() {
        Element node = new Element("div");
        node.appendElement("p");
        assertEquals("<div><p></p></div>", node.outerHtml());
    }
}