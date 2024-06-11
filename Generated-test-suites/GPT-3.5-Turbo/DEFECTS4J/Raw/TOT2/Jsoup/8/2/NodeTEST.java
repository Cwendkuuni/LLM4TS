import org.junit.Test;
import static org.junit.Assert.*;

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
        node.attr("src", "image.jpg");
        assertTrue(node.hasAttr("src"));
        assertFalse(node.hasAttr("alt"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Element("img");
        node.attr("src", "image.jpg");
        node.removeAttr("src");
        assertFalse(node.hasAttr("src"));
    }

    // Add more test cases for other methods as needed

}