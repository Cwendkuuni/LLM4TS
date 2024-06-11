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
    public void testAttributeManipulation() {
        Node node = new Element("p");
        node.attr("id", "paragraph");
        assertTrue(node.hasAttr("id"));
        assertEquals("paragraph", node.attr("id"));
    }

    @Test
    public void testBaseUri() {
        Node node = new Element("a");
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com", node.baseUri());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("img");
        node.attr("src", "/images/image.png");
        node.setBaseUri("http://www.example.com");
        assertEquals("http://www.example.com/images/image.png", node.absUrl("src"));
    }

    @Test
    public void testChildNode() {
        Element parent = new Element("div");
        Element child = new Element("p");
        parent.appendChild(child);
        assertEquals(child, parent.childNode(0));
    }

    @Test
    public void testSiblingNodes() {
        Element parent = new Element("div");
        Element sibling1 = new Element("p");
        Element sibling2 = new Element("span");
        parent.appendChild(sibling1);
        parent.appendChild(sibling2);
        assertEquals(2, parent.siblingNodes().size());
        assertTrue(parent.siblingNodes().contains(sibling1));
        assertTrue(parent.siblingNodes().contains(sibling2));
    }

    @Test
    public void testRemove() {
        Node parent = new Element("div");
        Node child = new Element("p");
        parent.appendChild(child);
        child.remove();
        assertEquals(0, parent.childNodes().size());
        assertNull(child.parent());
    }

    @Test
    public void testReplaceWith() {
        Element parent = new Element("div");
        Element original = new Element("p");
        Element replacement = new Element("span");
        parent.appendChild(original);
        original.replaceWith(replacement);
        assertFalse(parent.childNodes().contains(original));
        assertTrue(parent.childNodes().contains(replacement));
    }

    @Test
    public void testClone() {
        Element original = new Element("p");
        original.attr("id", "paragraph");
        Element clone = (Element) original.clone();
        assertEquals(original.nodeName(), clone.nodeName());
        assertEquals(original.attr("id"), clone.attr("id"));
        assertNotSame(original, clone);
        assertNotSame(original.attributes(), clone.attributes());
    }
}