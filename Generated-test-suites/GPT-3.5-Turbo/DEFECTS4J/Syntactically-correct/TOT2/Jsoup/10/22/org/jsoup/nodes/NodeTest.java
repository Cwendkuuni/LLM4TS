import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreationWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        Node node = new Element("example.com", attributes);
        assertEquals("example.com", node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testAttrMethod() {
        Node node = new Element("example.com");
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        assertEquals("value", node.attr("key"));
    }

    @Test
    public void testRemoveAttrMethod() {
        Node node = new Element("example.com");
        node.attr("key", "value");
        assertTrue(node.hasAttr("key"));
        node.removeAttr("key");
        assertFalse(node.hasAttr("key"));
    }

    @Test
    public void testChildNodeMethod() {
        Node parent = new Element("example.com");
        Node child1 = new Element("example1.com");
        Node child2 = new Element("example2.com");
        parent.addChildren(child1, child2);
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testParentMethod() {
        Node parent = new Element("example.com");
        Node child = new Element("example1.com");
        parent.addChildren(child);
        assertEquals(parent, child.parent());
    }

    @Test
    public void testReplaceWithMethod() {
        Node parent = new Element("example.com");
        Node oldChild = new Element("example1.com");
        Node newChild = new Element("example2.com");
        parent.addChildren(oldChild);
        oldChild.replaceWith(newChild);
        assertFalse(parent.childNodes().contains(oldChild));
        assertTrue(parent.childNodes().contains(newChild));
    }

    @Test
    public void testCloneMethod() {
        Node node = new Element("example.com");
        Node clonedNode = node.clone();
        assertNotSame(node, clonedNode);
        assertEquals(node.baseUri(), clonedNode.baseUri());
    }
}