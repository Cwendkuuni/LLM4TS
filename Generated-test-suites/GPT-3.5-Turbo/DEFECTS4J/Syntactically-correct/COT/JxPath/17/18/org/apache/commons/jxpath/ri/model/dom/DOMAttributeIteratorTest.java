import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        NodePointer parent = new MockNodePointer();
        QName name = new QName("attribute");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointerEmpty() {
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionValid() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
}

