import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private Node node;

    @Before
    public void setUp() {
        node = new Element("testBaseUri", new Attributes());
    }

    @Test
    public void testNodeName() {
        assertEquals("Element", node.nodeName());
    }

    @Test
    public void testAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(node.hasAttr("nonExistingKey"));
        node.attr("existingKey", "value");
        assertTrue(node.hasAttr("existingKey"));
    }

    @Test
    public void testRemoveAttr() {
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("testBaseUri", node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        node.setBaseUri("newBaseUri");
        assertEquals("newBaseUri", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        node.setBaseUri("http://example.com");
        node.attr("href", "/page");
        assertEquals("http://example.com/page", node.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node child1 = new Element("child1", new Attributes());
        Node child2 = new Element("child2", new Attributes());
        node.addChildren(child1, child2);
        assertEquals(child1, node.childNode(0));
        assertEquals(child2, node.childNode(1));
    }

    @Test
    public void testParent() {
        assertNull(node.parent());
        Node parent = new Element("parent", new Attributes());
        parent.addChildren(node);
        assertEquals(parent, node.parent());
    }

    @Test
    public void testReplaceWith() {
        Node newNode = new Element("newNode", new Attributes());
        node.replaceWith(newNode);
        assertNull(node.parent());
        assertEquals(newNode, newNode.parent());
    }

    @Test
    public void testSiblingNodes() {
        Node sibling1 = new Element("sibling1", new Attributes());
        Node sibling2 = new Element("sibling2", new Attributes());
        Node parent = new Element("parent", new Attributes());
        parent.addChildren(sibling1, node, sibling2);
        assertEquals(3, node.siblingNodes().size());
        assertTrue(node.siblingNodes().contains(sibling1));
        assertTrue(node.siblingNodes().contains(sibling2));
    }

    @Test
    public void testNextSibling() {
        Node sibling1 = new Element("sibling1", new Attributes());
        Node sibling2 = new Element("sibling2", new Attributes());
        Node parent = new Element("parent", new Attributes());
        parent.addChildren(sibling1, node, sibling2);
        assertEquals(sibling2, node.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        Node sibling1 = new Element("sibling1", new Attributes());
        Node sibling2 = new Element("sibling2", new Attributes());
        Node parent = new Element("parent", new Attributes());
        parent.addChildren(sibling1, node, sibling2);
        assertEquals(sibling1, node.previousSibling());
    }

}