import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        // Create a parent node
        NodePointer parent = new NodePointer();
        parent.setNode(new Element());

        // Set a QName for testing
        QName name = new QName("test");

        // Create a DOMAttributeIterator object
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        // Test when the position is 0
        iterator.setPosition(0);
        assertNull(iterator.getNodePointer());

        // Test when the position is 1
        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());

        // Test when the position is greater than attributes.size()
        iterator.setPosition(iterator.attributes.size() + 1);
        assertNull(iterator.getNodePointer());

        // Test when the position is within the range of attributes.size()
        iterator.setPosition(2);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        // Test when the position is 0
        iterator.setPosition(0);
        assertEquals(0, iterator.getPosition());

        // Test when the position is within the range of attributes.size()
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        // Test when the position is greater than attributes.size()
        iterator.setPosition(iterator.attributes.size() + 1);
        assertEquals(iterator.attributes.size() + 1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test when the position is within the range of attributes.size()
        int position = 1;
        assertTrue(iterator.setPosition(position));
        assertEquals(position, iterator.getPosition());

        // Test when the position is 0
        position = 0;
        assertFalse(iterator.setPosition(position));
        assertEquals(1, iterator.getPosition());

        // Test when the position is greater than attributes.size()
        position = iterator.attributes.size() + 1;
        assertFalse(iterator.setPosition(position));
        assertEquals(1, iterator.getPosition());
    }
}