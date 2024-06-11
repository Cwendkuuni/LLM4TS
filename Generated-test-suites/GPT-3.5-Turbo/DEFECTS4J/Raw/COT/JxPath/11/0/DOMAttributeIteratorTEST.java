import static org.junit.Assert.*;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    @Test
    public void testConstructor() {
        assertEquals(null, new DOMAttributeIterator(null, new QName("name")));
        assertEquals(null, new DOMAttributeIterator(new NodePointer(), null));
        assertEquals(null, new DOMAttributeIterator(null, null));
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        assertEquals(null, iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        iterator.setPosition(0);
        assertEquals(null, iterator.getNodePointer());

        iterator = new DOMAttributeIterator(null, new QName("name"));
        assertEquals(null, iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        assertFalse(iterator.setPosition(0));

        iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        assertFalse(iterator.setPosition(2));

        iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(2));

        iterator = new DOMAttributeIterator(new NodePointer(), new QName("name"));
        assertTrue(iterator.setPosition(1));

        iterator = new DOMAttributeIterator(null, new QName("name"));
        assertFalse(iterator.setPosition(1));
    }
}