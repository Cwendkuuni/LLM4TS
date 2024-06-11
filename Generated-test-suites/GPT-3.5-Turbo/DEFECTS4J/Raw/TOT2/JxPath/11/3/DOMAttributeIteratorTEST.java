import static org.junit.Assert.*;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid parent and name
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        
        // Test constructor with null parent
        DOMAttributeIterator iteratorNullParent = new DOMAttributeIterator(null, name);
        assertNull(iteratorNullParent);
        
        // Test constructor with null name
        DOMAttributeIterator iteratorNullName = new DOMAttributeIterator(parent, null);
        assertNull(iteratorNullName);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        // Test setting position to 1
        assertTrue(iterator.setPosition(1));
        
        // Test setting position to a negative number
        assertFalse(iterator.setPosition(-1));
        
        // Test setting position to a number larger than the size of attributes
        assertFalse(iterator.setPosition(100));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        // Test getNodePointer on an empty iterator
        assertNull(iterator.getNodePointer());
        
        // Test getNodePointer after setting position
        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testEqualStrings() {
        String s1 = "test1";
        String s2 = "test1";
        assertTrue(DOMAttributeIterator.equalStrings(s1, s2));
        
        String s3 = "test1";
        String s4 = "test2";
        assertFalse(DOMAttributeIterator.equalStrings(s3, s4));
        
        String s5 = null;
        String s6 = null;
        assertTrue(DOMAttributeIterator.equalStrings(s5, s6));
        
        String s7 = "test1";
        String s8 = null;
        assertFalse(DOMAttributeIterator.equalStrings(s7, s8));
    }

}