import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
    
    @Test
    public void testAttr() {
        Node node = new Node("https://www.example.com");
        node.attr("name", "value");
        assertEquals("value", node.attr("name"));
    }
    
    @Test
    public void testAttributes() {
        Node node = new Node("https://www.example.com");
        node.attr("name1", "value1");
        node.attr("name2", "value2");
        assertEquals("value1", node.attributes().get("name1"));
        assertEquals("value2", node.attributes().get("name2"));
    }
    
    @Test
    public void testHasAttr() {
        Node node = new Node("https://www.example.com");
        assertFalse(node.hasAttr("name"));
        node.attr("name", "value");
        assertTrue(node.hasAttr("name"));
    }
    
    @Test
    public void testRemoveAttr() {
        Node node = new Node("https://www.example.com");
        node.attr("name", "value");
        assertTrue(node.hasAttr("name"));
        node.removeAttr("name");
        assertFalse(node.hasAttr("name"));
    }
    
    @Test
    public void testBaseUri() {
        Node node = new Node("https://www.example.com");
        assertEquals("https://www.example.com", node.baseUri());
        node.setBaseUri("https://www.example.com/new");
        assertEquals("https://www.example.com/new", node.baseUri());
    }
    
    @Test
    public void testAbsUrl() {
        Node node = new Node("https://www.example.com");
        node.attr("href", "/new");
        assertEquals("https://www.example.com/new", node.absUrl("href"));
    }
    
    @Test
    public void testChildNode() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("https://www.example.com/child1");
        Node child2 = new Node("https://www.example.com/child2");
        parent.addChildren(child1, child2);
        
        assertEquals(child1, parent.childNode(0));
        assertEquals(child2, parent.childNode(1));
    }
    
    @Test
    public void testParent() {
        Node parent = new Node("https://www.example.com");
        Node child = new Node("https://www.example.com/child");
        parent.addChildren(child);
        
        assertEquals(parent, child.parent());
    }
    
    @Test
    public void testOwnerDocument() {
        Node parent = new Node("https://www.example.com");
        Node child = new Node("https://www.example.com/child");
        parent.addChildren(child);
        
        assertEquals(parent, child.ownerDocument());
        assertNull(parent.ownerDocument());
    }
    
    @Test
    public void testRemove() {
        Node parent = new Node("https://www.example.com");
        Node child = new Node("https://www.example.com/child");
        parent.addChildren(child);
        
        assertTrue(parent.childNodes().contains(child));
        child.remove();
        assertFalse(parent.childNodes().contains(child));
    }
    
    @Test
    public void testReplaceWith() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("https://www.example.com/child1");
        Node child2 = new Node("https://www.example.com/child2");
        parent.addChildren(child1);
        
        assertTrue(parent.childNodes().contains(child1));
        parent.replaceWith(child2);
        assertFalse(parent.childNodes().contains(child1));
        assertTrue(parent.childNodes().contains(child2));
    }
    
    @Test
    public void testNextSibling() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("https://www.example.com/child1");
        Node child2 = new Node("https://www.example.com/child2");
        parent.addChildren(child1, child2);
        
        assertEquals(child2, child1.nextSibling());
        assertNull(child2.nextSibling());
    }
    
    @Test
    public void testPreviousSibling() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("https://www.example.com/child1");
        Node child2 = new Node("https://www.example.com/child2");
        parent.addChildren(child1, child2);
        
        assertEquals(child1, child2.previousSibling());
        assertNull(child1.previousSibling());
    }
    
    @Test
    public void testSiblingIndex() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("https://www.example.com/child1");
        Node child2 = new Node("https://www.example.com/child2");
        parent.addChildren(child1, child2);
        
        assertEquals((Integer) 0, child1.siblingIndex());
        assertEquals((Integer) 1, child2.siblingIndex());
    }
    
    @Test
    public void testOuterHtml() {
        Node parent = new Node("https://www.example.com");
        Node child1 = new Node("https://www.example.com/child1");
        Node child2 = new Node("https://www.example.com/child2");
        parent.addChildren(child1, child2);
        
        String expected = "<Parent>\n" +
                          "  <Child1></Child1>\n" +
                          "  <Child2></Child2>\n" +
                          "</Parent>";
        assertEquals(expected, parent.outerHtml());
    }
}