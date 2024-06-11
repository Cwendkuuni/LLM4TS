import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        Node node = new Element("http://example.com", attributes);
        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        Node node = new Element("http://example.com");
        assertEquals("http://example.com", node.baseUri());
        assertEquals(0, node.attributes().size());
    }

    // Additional test cases for each public method should be added to cover edge cases and exception handling scenarios

}