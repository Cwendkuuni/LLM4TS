import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div", new Attributes());
        assertNotNull(node);
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testGetAttributeValue() {
        Node node = new Element("a", new Attributes().put("href", "http://example.com"));
        assertEquals("http://example.com", node.attr("href"));
        assertEquals("", node.attr("nonExistentAttribute"));
    }

    @Test
    public void testSetAttributeValue() {
        Node node = new Element("a", new Attributes());
        node.attr("href", "http://example.com");
        assertEquals("http://example.com", node.attr("href"));
    }

    @Test
    public void testHasAttribute() {
        Node node = new Element("img", new Attributes().put("alt", "Image"));
        assertTrue(node.hasAttr("alt"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAttribute() {
        Node node = new Element("img", new Attributes().put("src", "image.jpg"));
        assertTrue(node.hasAttr("src"));
        node.removeAttr("src");
        assertFalse(node.hasAttr("src"));
    }

    @Test
    public void testGetBaseUri() {
        Node node = new Element("a", new Attributes());
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testGetAbsUrl() {
        Node node = new Element("a", new Attributes().put("href", "/page.html"));
        node.setBaseUri("http://example.com");
        assertEquals("http://example.com/page.html", node.absUrl("href"));
    }

    // Additional test cases for edge cases and exceptions go here

}