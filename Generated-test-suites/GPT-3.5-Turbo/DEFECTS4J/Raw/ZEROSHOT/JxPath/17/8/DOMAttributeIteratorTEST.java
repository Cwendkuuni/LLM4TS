import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator domAttributeIterator;

    @Before
    public void setUp() {
        NodePointer parent = null;
        QName name = null;
        domAttributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertNotNull(domAttributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = domAttributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = domAttributeIterator.setPosition(5);
        assertFalse(result);
        int position = domAttributeIterator.getPosition();
        assertEquals(5, position);
    }
}