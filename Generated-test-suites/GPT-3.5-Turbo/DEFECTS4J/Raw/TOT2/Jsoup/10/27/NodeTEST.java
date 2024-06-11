import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private Node testNode;

    @Before
    public void setUp() {
        testNode = new Element("testURI", new Attributes());
    }

    @Test
    public void testNodeConstructor() {
        assertNotNull(testNode);
        assertEquals("testURI", testNode.baseUri());
        assertEquals(0, testNode.childNodes().size());
        assertNotNull(testNode.attributes());
    }

    @Test
    public void testNodeChildNode() {
        Node childNode = new Element("childURI", new Attributes());
        testNode.addChildren(childNode);
        assertEquals(childNode, testNode.childNode(0));
    }

    @Test
    public void testNodeParent() {
        Node parentNode = new Element("parentURI", new Attributes());
        testNode.setParentNode(parentNode);
        assertEquals(parentNode, testNode.parent());
    }

    @Test
    public void testNodeAttr() {
        testNode.attr("key", "value");
        assertTrue(testNode.hasAttr("key"));
        assertEquals("value", testNode.attr("key"));
    }

    // Add more test cases for other methods here

}