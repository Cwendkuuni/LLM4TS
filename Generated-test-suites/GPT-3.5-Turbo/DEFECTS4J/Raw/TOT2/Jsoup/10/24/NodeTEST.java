import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreationWithAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Node node = new ElementNode("http://example.com", attributes);

        assertEquals("http://example.com", node.baseUri());
        assertTrue(node.hasAttr("key1"));
        assertTrue(node.hasAttr("key2"));
        assertEquals("value1", node.attr("key1"));
        assertEquals("value2", node.attr("key2"));
    }

    @Test
    public void testNodeCreationWithoutAttributes() {
        Node node = new ElementNode("http://example.com");

        assertEquals("http://example.com", node.baseUri());
        assertFalse(node.hasAttr("key1"));
        assertFalse(node.hasAttr("key2"));
        assertEquals("", node.attr("key1"));
        assertEquals("", node.attr("key2"));
    }

    @Test
    public void testSetBaseUri() {
        Node node = new ElementNode("http://example.com");
        node.setBaseUri("http://newexample.com");

        assertEquals("http://newexample.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new ElementNode("http://example.com");
        node.attr("href", "/path/to/page.html");

        assertEquals("http://example.com/path/to/page.html", node.absUrl("href"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new ElementNode("http://example.com");
        node.attr("key1", "value1");
        node.removeAttr("key1");

        assertFalse(node.hasAttr("key1"));
        assertEquals("", node.attr("key1"));
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new ElementNode("http://example.com");
        Node oldNode = new TextNode("example text");

        parentNode.addChildren(oldNode);

        Node newNode = new ElementNode("http://newexample.com");
        oldNode.replaceWith(newNode);

        assertFalse(parentNode.childNodes().contains(oldNode));
        assertTrue(parentNode.childNodes().contains(newNode));
    }

    @Test
    public void testNextSibling() {
        Node parentNode = new ElementNode("http://example.com");
        Node node1 = new ElementNode("http://example.com");
        Node node2 = new ElementNode("http://example.com");

        parentNode.addChildren(node1, node2);

        assertEquals(node2, node1.nextSibling());
        assertNull(node2.nextSibling());
    }

}