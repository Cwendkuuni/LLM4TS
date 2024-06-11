import org.junit.Test;

public class NodeTest {
    
    // Test for basic functionality of the Node class
    
    @Test
    public void testNodeConstructorWithAttributes() {
        Attributes attributes = new Attributes();
        Node node = new Element("http://example.com", attributes);

        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithDefaultAttributes() {
        Node node = new Element("http://example.com");

        assertNotNull(node);
        assertEquals("http://example.com", node.baseUri());
        assertNotNull(node.attributes());
        assertEquals(0, node.attributes().size());
    }

    @Test
    public void testNodeNullBaseUri() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Element(null);
        });
    }

    @Test
    public void testNodeNullAttributes() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Element("http://example.com", null);
        });
    }
    
    // Add more tests to cover all methods and edge cases of the Node class

}