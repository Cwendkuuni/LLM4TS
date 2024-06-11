import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.junit.Test;
import java.util.Arrays;

public class NodeTest {

    @Test
    public void testConstructorWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        Node node = new Node("http://example.com", attributes);

        assertEquals("http://example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testDefaultConstructor() {
        Node node = new Node();

        assertNotNull(node.baseUri());
        assertTrue(node.childNodes().isEmpty());
        assertNull(node.attributes());
    }

    @Test
    public void testAttrAndGetAttr() {
        Node node = new Node();
        node.attr("key", "value");

        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testRemoveAttr() {
        Node node = new Node();
        node.attr("key", "value");
        node.removeAttr("key");

        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUriOperations() {
        Node node = new Node();
        node.setBaseUri("http://example.com");

        assertEquals("http://example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Node();
        node.setBaseUri("http://example.com");
        node.attr("href", "/path");

        assertEquals("http://example.com/path", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node childNode = new Node();
        Node parentNode = new Node();
        parentNode.addChildren(childNode);

        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testParent() {
        Node parentNode = new Node();
        Node childNode = new Node();
        parentNode.addChildren(childNode);

        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Node();
        Node childNode = new Node();
        parentNode.addChildren(childNode);

        childNode.remove();
        assertTrue(parentNode.childNodes().isEmpty());
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Node();
        Node childNode = new Node();
        Node newNode = new Node();

        parentNode.addChildren(childNode);
        childNode.replaceWith(newNode);

        assertFalse(parentNode.childNodes().contains(childNode));
        assertTrue(parentNode.childNodes().contains(newNode));
    }

    @Test
    public void testNextSibling() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        
        parentNode.addChildren(childNode1);
        parentNode.addChildren(childNode2);

        assertEquals(childNode2, childNode1.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        
        parentNode.addChildren(childNode1);
        parentNode.addChildren(childNode2);

        assertEquals(childNode1, childNode2.previousSibling());
    }

    @Test
    public void testSiblingNodes() {
        Node parentNode = new Node();
        Node childNode1 = new Node();
        Node childNode2 = new Node();
        
        parentNode.addChildren(childNode1);
        parentNode.addChildren(childNode2);

        assertTrue(childNode1.siblingNodes().contains(childNode2));
        assertTrue(childNode2.siblingNodes().contains(childNode1));
    }

}