import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    
    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;
    
    @Before
    public void setup() {
        parent = new DOMNodePointer(null, null);
        name = new QName("attributeName");
        iterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer pointer = iterator.getNodePointer();
        assertNotNull(pointer);
        assertEquals(DOMAttributePointer.class, pointer.getClass());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
        assertFalse(iterator.setPosition(-1));
    }
}