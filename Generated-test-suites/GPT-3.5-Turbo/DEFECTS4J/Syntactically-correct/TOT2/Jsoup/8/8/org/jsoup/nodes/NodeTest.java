import org.jsoup.nodes.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
        assertNotNull(node.attributes());
        assertEquals(0, node.attributes().size());
    }

    @Test
    public void testAttributeManipulation() {
        Node node = new Element("a");
        node.attr("href", "https://example.com");
        assertTrue(node.hasAttr("href"));
        assertFalse(node.hasAttr("src"));
        assertEquals("https://example.com", node.attr("href"));
        node.removeAttr("href");
        assertFalse(node.hasAttr("href"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("span");
        assertEquals("", node.baseUri());
        node.setBaseUri("https://example.com");
        assertEquals("https://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Element element = new Element("a");
        element.setBaseUri("https://example.com");
        element.attr("href", "/page");
        assertEquals("https://example.com/page", element.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Element parent = new Element("ul");
        Element child1 = new Element("li");
        Element child2 = new Element("li");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertSame(child1, parent.childNode(0));
        assertSame(child2, parent.childNode(1));
    }

    @Test
    public void testSiblingNodes() {
        Element parent = new Element("div");
        Element child1 = new Element("p");
        Element child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(2, child1.siblingNodes().size());
        assertSame(child1, child1.siblingNodes().get(0));
        assertSame(child2, child1.nextSibling());
        assertSame(child1, child2.previousSibling());
    }
    
    @Test
    public void testRemoveAndReplace() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.addChildren(child);
        
        Element newChild = new Element("span");
        child.replaceWith(newChild);
        assertNull(child.parent());
        assertSame(newChild, parent.childNode(0));
        
        newChild.remove();
        assertEquals(0, parent.childNodes().size());
    }

    @Test
    public void testSiblingIndex() {
        Element parent = new Element("ul");
        Element child1 = new Element("li");
        Element child2 = new Element("li");
        parent.addChildren(child1, child2);
        assertEquals(0, child1.siblingIndex().intValue());
        assertEquals(1, child2.siblingIndex().intValue());
    }

    @Test
    public void testOuterHtml() {
        Element element = new Element("div");
        element.attr("id", "test");
        element.appendText("Hello World");
        
        assertEquals("<div id=\"test\">Hello World</div>", element.outerHtml());
    }

}