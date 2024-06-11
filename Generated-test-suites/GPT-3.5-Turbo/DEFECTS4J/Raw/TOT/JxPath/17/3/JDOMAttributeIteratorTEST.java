import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointerWithElementNode() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(elementNode), new QName("attribute"));

        // Test getting the first node pointer
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertTrue(nodePointer1 instanceof JDOMAttributePointer);
        assertEquals(elementNode.getAttributeValue("attribute"), ((JDOMAttributePointer) nodePointer1).getLeaf());
        
        // Test getting the second node pointer
        assertTrue(iterator.setPosition(2));
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertTrue(nodePointer2 instanceof JDOMAttributePointer);
        assertEquals(elementNode.getAttributeValue("attribute2"), ((JDOMAttributePointer) nodePointer2).getLeaf());
    }

    @Test
    public void testGetNodePointerWithNonElementNode() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(nonElementNode), new QName("attribute"));

        // Test getting node pointer when parent is not an element node
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(elementNode), new QName("attribute"));

        // Test getting initial position
        assertEquals(0, iterator.getPosition());

        // Test setting position and getting it
        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());
        
        // Test setting invalid position
        assertFalse(iterator.setPosition(0));
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(elementNode), new QName("attribute"));

        // Test setting valid position
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        // Test setting invalid position
        assertFalse(iterator.setPosition(0));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
        assertEquals(2, iterator.getPosition());
    }

}