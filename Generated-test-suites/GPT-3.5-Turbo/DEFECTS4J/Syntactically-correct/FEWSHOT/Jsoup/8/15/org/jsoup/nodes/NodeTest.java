
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeTest {
    private static final String BASE_URI = "http://example.com/";
    private Node node;

    @Before
    public void setup() {
        node = new Element("div", BASE_URI);
    }

    @Test
    public void testNodeName() {
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("class", "container");
        assertEquals("container", node.attr("class"));
        
        node.attr("data-id", "123");
        assertEquals("123", node.attr("data-id"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(node.hasAttr("class"));
        node.attr("class", "container");
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("class", "container");
        assertTrue(node.hasAttr("class"));
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals(BASE_URI, node.baseUri());
        node.setBaseUri("http://example.org/");
        assertEquals("http://example.org/", node.baseUri());
    }

    @Test
    public void testChildNodes() {
        Node child1 = new Element("p", BASE_URI);
        Node child2 = new TextNode("Hello world");
        node.addChildren(child1, child2);
        assertEquals(2, node.childNodes().size());
        assertEquals(child1, node.childNode(0));
        assertEquals(child2, node.childNode(1));
    }

    @Test
    public void testParent() {
        Node parent = new Element("div", BASE_URI);
        parent.addChildren(node);
        assertEquals(parent, node.parent());
    }
    
    @Test
    public void testOwnerDocument() {
        Document document = new Document(BASE_URI);
        assertEquals(document, node.ownerDocument());
        
        node.setParentNode(document);
        assertEquals(document, node.ownerDocument());
        
        node = new Element("div");
        assertNull(node.ownerDocument());
    }
    
    @Test
    public void testRemove() {
        Node parent = new Element("div", BASE_URI);
        parent.addChildren(node);
        assertEquals(1, parent.childNodes().size());
        
        node.remove();
        assertEquals(0, parent.childNodes().size());
        assertNull(node.parent());
    }
    
    @Test
    public void testReplaceWith() {
        Node newNode = new Element("p", BASE_URI);
        Node parent = new Element("div", BASE_URI);
        parent.addChildren(node);
        assertEquals(1, parent.childNodes().size());
        
        node.replaceWith(newNode);
        assertEquals(1, parent.childNodes().size());
        assertEquals(newNode, parent.childNode(0));
        assertNull(node.parent());
    }
}
