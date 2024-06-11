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
        node.attr("href", "https://www.example.com");
        assertEquals("https://www.example.com", node.attr("href"));
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("img");
        node.attr("src", "img.jpg");
        assertTrue(node.hasAttr("src"));
        assertFalse(node.hasAttr("alt"));
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttributeKey() {
        Node node = new Element("span");
        node.attr(null, "value");
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("p");
        assertNull(node.baseUri());
        node.setBaseUri("https://www.example.com");
        assertEquals("https://www.example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a");
        node.setBaseUri("https://www.example.com");
        node.attr("href", "/page");
        assertEquals("https://www.example.com/page", node.absUrl("href"));
    }

    @Test
    public void testChildNodes() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
    }

    // Complete JUnit 4 test file

    @Test
    public void testSiblingNodes() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(2, child1.siblingNodes().size());
    }

    @Test
    public void testNextSibling() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(child2, child1.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(child1, child2.previousSibling());
    }

    @Test
    public void testSiblingIndex() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(Integer.valueOf(1), child2.siblingIndex());
    }
}