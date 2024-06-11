import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    
    @Test
    public void testGetNodePointer() {
        // create a parent node
        Node mockNode = new MockNode();
        
        // create a QName
        QName qName = new QName("namespace", "name");
        
        // create a parent pointer
        NodePointer parentPointer = new NodePointer(mockNode);
        
        // create a DOMAttributeIterator
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, qName);
        
        // get the node pointer
        NodePointer nodePointer = iterator.getNodePointer();
        
        // assert that the node pointer is not null
        assertNotNull(nodePointer);
        
        // assert that the node pointer is of type DOMAttributePointer
        assertTrue(nodePointer instanceof DOMAttributePointer);
        
        // assert that the parent pointer of the node pointer is the same as the parent pointer used to create the DOMAttributeIterator
        assertEquals(parentPointer, nodePointer.getParent());
        
        // assert that the attribute in the DOMAttributePointer is the same as the first attribute in the attributes list of the DOMAttributeIterator
        assertEquals(iterator.getAttributes().get(0), ((DOMAttributePointer)nodePointer).getAttribute());
    }
    
    @Test
    public void testGetPosition() {
        // create a parent node
        Node mockNode = new MockNode();
        
        // create a QName
        QName qName = new QName("namespace", "name");
        
        // create a parent pointer
        NodePointer parentPointer = new NodePointer(mockNode);
        
        // create a DOMAttributeIterator
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, qName);
        
        // set the position to 5
        int position = 5;
        iterator.setPosition(position);
        
        // assert that the position returned by getPosition() is the same as the position set
        assertEquals(position, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        // create a parent node
        Node mockNode = new MockNode();
        
        // create a QName
        QName qName = new QName("namespace", "name");
        
        // create a parent pointer
        NodePointer parentPointer = new NodePointer(mockNode);
        
        // create a DOMAttributeIterator
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, qName);
        
        // set the position to 7
        int position = 7;
        boolean result = iterator.setPosition(position);
        
        // assert that the result of setPosition() is true
        assertTrue(result);
        
        // assert that the position returned by getPosition() is the same as the position set
        assertEquals(position, iterator.getPosition());
    }
}