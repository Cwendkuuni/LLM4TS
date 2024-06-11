import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        attributeIterator = new DOMAttributeIterator(parent, new QName("TestAttribute"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer expectedNodePointer = new DOMAttributePointer(parent, null);
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertEquals(expectedNodePointer, nodePointer);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(1, attributeIterator.getPosition());
    }
}