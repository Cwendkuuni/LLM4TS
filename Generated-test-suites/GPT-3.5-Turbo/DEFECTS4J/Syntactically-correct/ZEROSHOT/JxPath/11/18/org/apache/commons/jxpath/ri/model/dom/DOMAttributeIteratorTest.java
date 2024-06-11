import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator domAttributeIterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("test");
        domAttributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = domAttributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPositionWithinRange() {
        boolean result = domAttributeIterator.setPosition(1);
        assertTrue(result);
    }

    @Test
    public void testSetPositionOutsideRange() {
        boolean result = domAttributeIterator.setPosition(0);
        assertFalse(result);
    }
}