import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        // Initialize parent node
        NodePointer parent = new NodePointer();

        // Create test attributes
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attr1);
        attributes.add(attr2);

        // Set attributes and parent for iterator
        iterator = new JDOMAttributeIterator(parent, attributes);
    }

    @Test
    public void testGetNodePointer() {
        // Test returning the first attribute
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertTrue(nodePointer1 instanceof JDOMAttributePointer);
        assertEquals("attr1", nodePointer1.getAttribute().getName());
        assertEquals("value1", nodePointer1.getAttribute().getValue());

        // Set position to 2
        iterator.setPosition(2);

        // Test returning the second attribute
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertTrue(nodePointer2 instanceof JDOMAttributePointer);
        assertEquals("attr2", nodePointer2.getAttribute().getName());
        assertEquals("value2", nodePointer2.getAttribute().getValue());

        // Set position to 3
        iterator.setPosition(3);

        // Test returning null for out-of-bound position
        NodePointer nodePointer3 = iterator.getNodePointer();
        assertNull(nodePointer3);
    }

    @Test
    public void testGetPosition() {
        // Test initial position is 0
        assertEquals(0, iterator.getPosition());

        // Set position to 1
        iterator.setPosition(1);

        // Test position is 1
        assertEquals(1, iterator.getPosition());

        // Set position to 2
        iterator.setPosition(2);

        // Test position is 2
        assertEquals(2, iterator.getPosition());

        // Set position to 3
        iterator.setPosition(3);

        // Test position is 0 for out-of-bound position
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test setting position to 1
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        // Test setting position to 2
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        // Test setting position to 3
        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }
}