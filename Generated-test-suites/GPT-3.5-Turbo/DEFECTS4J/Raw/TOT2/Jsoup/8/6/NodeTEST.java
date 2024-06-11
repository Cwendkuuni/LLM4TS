import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeName() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new Element("a");
        node.attr("href", "http://example.com");
        assertEquals("http://example.com", node.attr("href"));
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("img");
        node.attr("alt", "Example Image");
        assertTrue(node.hasAttr("alt"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("a");
        node.attr("href", "http://example.com");
        node.removeAttr("href");
        assertFalse(node.hasAttr("href"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("div");
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a");
        node.setBaseUri("http://example.com");
        node.attr("href", "/page");
        assertEquals("http://example.com/page", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node parent = new Element("div");
        Node child = new Element("span");
        parent.addChildren(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testParent() {
        Node parent = new Element("div");
        Node child = new Element("span");
        parent.addChildren(child);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testNextSibling() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("a");
        parent.addChildren(child1, child2);
        assertEquals(child2, child1.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new Element("div");
        Node child1 = new Element("span");
        Node child2 = new Element("a");
        parent.addChildren(child1, child2);
        assertEquals(child1, child2.previousSibling());
    }
}