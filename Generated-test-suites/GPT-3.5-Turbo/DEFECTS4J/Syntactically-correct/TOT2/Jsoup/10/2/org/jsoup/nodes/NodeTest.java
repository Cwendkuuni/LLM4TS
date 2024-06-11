import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {
    
    @Test
    public void testAttr() {
        // Typical use case
        Node node = new Element("div");
        node.attr("class", "test");
        assertEquals("test", node.attr("class"));
        
        // Edge case: Attribute key with abs prefix
        assertEquals("http://example.com", node.attr("abs:href"));
        
        // Error scenario: Null attribute key
        assertThrows(IllegalArgumentException.class, () -> {
            node.attr(null);
        });
    }

    @Test
    public void testBaseUri() {
        // Typical use case
        Node node = new Element("a");
        node.setBaseUri("http://example.com/page");
        assertEquals("http://example.com/page", node.baseUri());
        
        // Edge case: Setting null base URI
        assertThrows(IllegalArgumentException.class, () -> {
            node.setBaseUri(null);
        });
    }

    @Test
    public void testChildNodes() {
        // Typical use case
        Element parent = new Element("div");
        Element child1 = new Element("span");
        Element child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        
        // Edge case: Empty child nodes list
        Element emptyParent = new Element("div");
        assertTrue(emptyParent.childNodes().isEmpty());
    }

    @Test
    public void testParent() {
        // Typical use case
        Element parent = new Element("div");
        Element child = new Element("span");
        parent.addChildren(child);
        assertEquals(parent, child.parent());
        
        // Edge case: Node without parent
        Element singletonNode = new Element("p");
        assertNull(singletonNode.parent());
    }

    @Test
    public void testNextSibling() {
        // Typical use case
        Element parent = new Element("div");
        Element child1 = new Element("span");
        Element child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(child2, child1.nextSibling());
        
        // Edge case: Last sibling
        assertNull(child2.nextSibling());
    }

    @Test
    public void testPreviousSibling() {
        // Typical use case
        Element parent = new Element("div");
        Element child1 = new Element("span");
        Element child2 = new Element("span");
        parent.addChildren(child1, child2);
        assertEquals(child1, child2.previousSibling());
        
        // Edge case: First sibling
        assertNull(child1.previousSibling());
    }
}