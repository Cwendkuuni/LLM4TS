import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    @Test
    public void testNodeName() {
        Node node = new Element("div");
        assertEquals("div", node.nodeName());
    }
    
    @Test
    public void testAttribute() {
        Node node = new Element("a");
        node.attr("href", "https://www.example.com");
        assertEquals("https://www.example.com", node.attr("href"));
    }
    
    @Test
    public void testHasAttribute() {
        Node node = new Element("img");
        node.attr("src", "image.jpg");
        assertTrue(node.hasAttr("src"));
        assertFalse(node.hasAttr("alt"));
    }
    
    @Test
    public void testRemoveAttribute() {
        Node node = new Element("p");
        node.attr("class", "paragraph");
        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }
    
    @Test
    public void testBaseUri() {
        Node node = new Element("img");
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
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("span");
        parent.appendChild(child);
        assertEquals(child, parent.childNode(0));
    }
    
    // Additional test cases for more methods can be added here

}