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
    public void testAttr() {
        Node node = new Element("div", new Attributes());
        node.attr("id", "testId");
        assertTrue(node.hasAttr("id"));
        assertEquals("testId", node.attr("id"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("a", new Attributes());
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }
    
    @Test
    public void testAbsUrl() {
        Node node = new Element("a", new Attributes());
        node.setBaseUri("http://www.example.com");
        node.attr("href", "/page.html");
        assertEquals("http://www.example.com/page.html", node.absUrl("href"));
    }
    
    @Test
    public void testAddRemoveChild() {
        Node parentNode = new Element("div", new Attributes());
        Node childNode = new Element("span", new Attributes());
        parentNode.addChildren(childNode);
        assertEquals(childNode, parentNode.childNode(0));
        
        parentNode.removeChild(childNode);
        assertTrue(parentNode.childNodes().isEmpty());
    }

    // Add more test cases as needed

}