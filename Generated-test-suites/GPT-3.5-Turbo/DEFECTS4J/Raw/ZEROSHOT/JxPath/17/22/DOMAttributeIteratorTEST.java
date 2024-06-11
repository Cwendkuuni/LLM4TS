import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }

}