import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    
    @Test
    public void testGetNodePointer() {
        // test when attributes is null
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
        assertNull(iterator.getNodePointer());
        
        // test when attributes is empty
        iterator = new JDOMAttributeIterator(new NodePointer(), new QName());
        assertNull(iterator.getNodePointer());
        
        // test when attributes is not empty
        NodePointer parent = new NodePointer();
        QName name = new QName("prefix", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        // test when position is 0
        parent.setNode(new Element("element"));
        assertNull(iterator.getNodePointer());
        
        // test when position is 1
        Element element = (Element) parent.getNode();
        element.setAttribute("name", "value");
        JDOMAttributePointer attributePointer = new JDOMAttributePointer(parent, element.getAttribute("name"));
        assertEquals(attributePointer, iterator.getNodePointer());
        
        // test when position is greater than the size of attributes
        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName("prefix", "name");
        
        // test when attributes is null
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
        assertEquals(0, iterator.getPosition());
        
        // test when attributes is empty
        iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        
        // test when attributes is not empty
        parent.setNode(new Element("element"));
        iterator = new JDOMAttributeIterator(parent, name);
        
        // test getPosition() returns the correct position
        Element element = (Element) parent.getNode();
        element.setAttribute("name", "value");
        assertEquals(1, iterator.getPosition());
        
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName("prefix", "name");
        
        // test when attributes is null
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
        assertFalse(iterator.setPosition(1));
        
        // test when attributes is empty
        iterator = new JDOMAttributeIterator(parent, name);
        assertFalse(iterator.setPosition(1));
        
        // test when attributes is not empty
        parent.setNode(new Element("element"));
        iterator = new JDOMAttributeIterator(parent, name);
        Element element = (Element) parent.getNode();
        element.setAttribute("name", "value");
        
        // test when position is less than 1
        assertFalse(iterator.setPosition(0));
        
        // test when position is greater than the size of attributes
        assertFalse(iterator.setPosition(2));
        
        // test when position is within the range of attributes size
        assertTrue(iterator.setPosition(1));
    }
    
}