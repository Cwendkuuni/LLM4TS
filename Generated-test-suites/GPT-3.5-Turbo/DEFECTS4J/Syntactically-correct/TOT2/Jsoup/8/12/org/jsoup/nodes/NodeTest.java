import org.jsoup.nodes.*;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div");
        assertNotNull(node);
        assertEquals("div", node.nodeName());
        assertEquals("", node.baseUri());
        assertFalse(node.hasAttr("class"));
        assertEquals("", node.attr("class"));
    }

    @Test
    public void testAttributeOperations() {
        Node node = new Element("span");
        node.attr("id", "exampleId");
        assertTrue(node.hasAttr("id"));
        assertEquals("exampleId", node.attr("id"));
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testInvalidBaseUri() {
        Node node = new Element("a");
        node.setBaseUri("invalid base uri");
        assertEquals("invalid base uri", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a");
        node.attr("href", "path/to/page.html");
        assertEquals("https://www.example.com/path/to/page.html", node.absUrl("href"));
    }

    @Test
    public void testSiblingNodes() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("p");
        parent.addChildren(child1, child2);
        assertEquals(Arrays.asList(child1, child2), child1.siblingNodes());
        assertEquals(child2, child1.nextSibling());
        assertEquals(child1, child2.previousSibling());
    }

    @Test
    public void testRemoveNode() {
        Node parent = new Element("div");
        Node child = new Element("a");
        parent.addChildren(child);
        child.remove();
        assertTrue(parent.childNodes().isEmpty());
    }

    @Test
    public void testReplaceNode() {
        Node parent = new Element("div");
        Node oldNode = new Element("span");
        Node newNode = new Element("p");
        parent.addChildren(oldNode);
        oldNode.replaceWith(newNode);
        assertEquals(newNode, parent.childNode(0));
    }

    @Test
    public void testExceptionHandling() {
        Node node = new Element("img");
        try {
            node.absUrl("src");
            fail("MalformedURLException should have been thrown");
        } catch (MalformedURLException e) {
            assertTrue(true);
        }
    }

}