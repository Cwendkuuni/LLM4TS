import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private Node testNode;

    @Before
    public void setup() {
        testNode = new Node(){};
    }

    @Test
    public void testNodeCreation() {
        assertNotNull(testNode);
    }

    @Test
    public void testNodeHasParent() {
        assertNull(testNode.parent());
    }

    @Test
    public void testNodeBaseUri() {
        testNode.setBaseUri("http://example.com");
        assertEquals("http://example.com", testNode.baseUri());
    }

    @Test
    public void testNodeAttr() {
        testNode.attr("key", "value");
        assertTrue(testNode.hasAttr("key"));
        assertEquals("value", testNode.attr("key"));
    }

    @Test
    public void testNodeAbsUrl() {
        testNode.setBaseUri("http://example.com");
        testNode.attr("href", "/page");
        assertEquals("http://example.com/page", testNode.absUrl("href"));
    }

    @Test
    public void testNodeChildNode() {
        Node childNode = new Node(){};
        testNode.addChildren(childNode);
        assertEquals(childNode, testNode.childNode(0));
    }

    @Test
    public void testNodeNextSibling() {
        Node sibling = new Node(){};
        testNode.addChildren(new Node(){}, sibling);
        assertEquals(sibling, testNode.nextSibling());
    }

    @Test
    public void testNodeClone() {
        Node clonedNode = testNode.clone();
        assertNotSame(testNode, clonedNode);
    }

}