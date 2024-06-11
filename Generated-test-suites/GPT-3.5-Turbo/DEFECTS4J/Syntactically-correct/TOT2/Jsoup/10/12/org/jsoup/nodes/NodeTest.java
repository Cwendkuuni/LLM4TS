import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("https://www.example.com");
        assertNotNull(node);
        assertEquals("https://www.example.com", node.baseUri());
        assertTrue(node.attributes().isEmpty());
    }

    @Test
    public void testNodeAttr() {
        Node node = new Element("https://www.example.com");
        node.attr("testKey", "testValue");
        assertTrue(node.hasAttr("testKey"));
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("https://www.example.com");
        node.attr("testKey", "testValue");
        assertTrue(node.hasAttr("testKey"));
        node.removeAttr("testKey");
        assertFalse(node.hasAttr("testKey"));
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("https://www.example.com");
        node.attr("href", "/page.html");
        assertEquals("https://www.example.com/page.html", node.absUrl("href"));
    }

    // Add more test cases for the remaining methods

}