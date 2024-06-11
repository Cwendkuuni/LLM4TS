import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeName() {
        // Typical use case
        Node node = new Element("div");
        assertEquals("Element", node.nodeName());
        
        // Edge case
        Node node2 = new TextNode("Hello World");
        assertEquals("TextNode", node2.nodeName());
    }

    @Test
    public void testAttr() {
        Node node = new Element("a");
        node.attr("href", "https://www.example.com");
        
        // Typical use case
        assertEquals("https://www.example.com", node.attr("href"));
        
        // Edge case
        assertEquals("", node.attr("nonexistent"));
    }

    @Test
    public void testHasAttr() {
        Node node = new Element("img");
        node.attr("src", "image.jpg");
        
        // Typical use case
        assertTrue(node.hasAttr("src"));
        
        // Edge case
        assertFalse(node.hasAttr("alt"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("span");
        node.attr("class", "highlight");
        node.attr("id", "123");
        
        // Typical use case
        assertTrue(node.hasAttr("class"));
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
        
        // Edge case
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("a");
        node.setBaseUri("https://www.example.com");
        
        // Typical use case
        assertEquals("https://www.example.com", node.baseUri());
        
        // Edge case
        node.setBaseUri("");
        assertEquals("", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a");
        node.setBaseUri("https://www.example.com");
        node.attr("href", "/page");
        
        // Typical use case
        assertEquals("https://www.example.com/page", node.absUrl("href"));
        
        // Edge case
        node.attr("href", "invalid-url");
        assertEquals("", node.absUrl("href"));
    }
}