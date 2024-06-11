import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testAttributeName() {
        Node node = new Element("div");

        node.attr("id", "testId");
        assertEquals("testId", node.attr("id"));
    }

    @Test
    public void testHasAttribute() {
        Node node = new Element("span");

        assertFalse(node.hasAttr("class"));
        node.attr("class", "testClass");
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testRemoveAttribute() {
        Node node = new Element("a");

        node.attr("href", "http://www.example.com");
        assertTrue(node.hasAttr("href"));

        node.removeAttr("href");
        assertFalse(node.hasAttr("href"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("img");

        assertEquals("", node.baseUri());
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a");

        node.setBaseUri("http://www.example.com");
        node.attr("href", "/index.html");

        assertEquals("http://www.example.com/index.html", node.absUrl("href"));
    }

    // Add more test cases for each public method to cover typical use cases, edge cases, and error scenarios
    
}