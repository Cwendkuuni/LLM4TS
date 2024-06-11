import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator domAttributeIterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setup() {
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
    public void testSetPositionValid() {
        boolean result = domAttributeIterator.setPosition(1);
        assertTrue(result);

        int position = domAttributeIterator.getPosition();
        assertEquals(1, position);
    }

    @Test
    public void testSetPositionInvalid() {
        boolean result = domAttributeIterator.setPosition(5);
        assertFalse(result);

        int position = domAttributeIterator.getPosition();
        assertEquals(0, position);
    }
}