package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;



public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div");
        assertNotNull(node);
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttributeGettersAndSetters() {
        Element element = new Element("span");
        element.attr("id", "testId");
        assertTrue(element.hasAttr("id"));
        assertEquals("testId", element.attr("id"));
    }

    @Test
    public void testRemoveAttribute() {
        Element element = new Element("a");
        element.attr("href", "http://www.example.com");
        assertTrue(element.hasAttr("href"));
        element.removeAttr("href");
        assertFalse(element.hasAttr("href"));
    }

    @Test
    public void testBaseUri() {
        Element element = new Element("img");
        element.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Element element = new Element("a");
        element.setBaseUri("http://www.example.com");
        element.attr("href", "/relative/url");
        assertEquals("http://www.example.com/relative/url", element.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testParentNode() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChildren(child);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceChildNode() {
        Element parent = new Element("div");
        Element originalChild = new Element("p");
        Element newChild = new Element("span");
        parent.addChildren(originalChild);
        parent.replaceChild(originalChild, newChild);
        assertEquals(newChild, parent.childNode(0));
    }

    @Test
    public void testRemoveChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChildren(child);
        parent.removeChild(child);
        assertEquals(0, parent.childNodes().size());
    }

}

