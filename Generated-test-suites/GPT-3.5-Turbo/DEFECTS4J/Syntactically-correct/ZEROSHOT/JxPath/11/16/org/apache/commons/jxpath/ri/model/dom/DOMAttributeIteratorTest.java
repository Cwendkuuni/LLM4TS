import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.*;
import org.apache.commons.jxpath.ri.*;
import org.w3c.dom.*;

public class DOMAttributeIteratorTest {
    @Test
    public void testConstructor() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        int position = iterator.getPosition();
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(2);
        assertFalse(result);
    }
}