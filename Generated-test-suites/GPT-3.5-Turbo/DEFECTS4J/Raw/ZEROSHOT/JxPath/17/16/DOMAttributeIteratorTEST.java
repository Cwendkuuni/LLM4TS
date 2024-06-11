import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);
        when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(2));
        assertEquals(0, iterator.getPosition());
    }
}