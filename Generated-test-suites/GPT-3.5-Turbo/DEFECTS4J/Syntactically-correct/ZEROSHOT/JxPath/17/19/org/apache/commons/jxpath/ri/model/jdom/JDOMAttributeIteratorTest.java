import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("test");
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        assertTrue(result);
    }
}