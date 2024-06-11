import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator attributeIterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new NodePointer();
        attributeIterator = new DOMAttributeIterator(parent, new QName("test"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPositionValid() {
        boolean result = attributeIterator.setPosition(2);

        assertTrue(result);
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        boolean result = attributeIterator.setPosition(0);

        assertFalse(result);
        assertEquals(0, attributeIterator.getPosition());
    }
}