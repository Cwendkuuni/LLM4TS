import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        Node node = new Element("div");
        assertNotNull(node);
        assertEquals("div", node.nodeName());
    }

    @Test
    public void testAttributeMethods() {
        Node node = new Element("span");
        node.attr("id", "testId");
        assertTrue(node.hasAttr("id"));
        assertEquals("testId", node.attr("id"));
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testBaseUriMethods() {
        Node node = new Element("a");
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testChildNodesMethods() {
        Node parent = new Element("ul");
        Node child1 = new Element("li");
        Node child2 = new Element("li");

        parent.addChildren(child1, child2);

        assertEquals(2, parent.childNodes().size());
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }

    @Test
    public void testSiblingMethods() {
        Node parent = new Element("div");
        Node child1 = new Element("p");
        Node child2 = new Element("span");

        parent.addChildren(child1, child2);

        assertEquals(child2, child1.nextSibling());
        assertNull(child2.nextSibling());
        assertEquals(child1, child2.previousSibling());
        assertNull(child1.previousSibling());
    }

    @Test
    public void testCloneMethod() {
        Node node = new Element("img");
        Node clonedNode = node.clone();

        assertNotSame(node, clonedNode);
        assertEquals(node.nodeName(), clonedNode.nodeName());
        assertEquals(node.baseUri(), clonedNode.baseUri());
        assertEquals(node.attributes(), clonedNode.attributes());
    }
}