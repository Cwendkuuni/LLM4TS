
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointerWithEmptyAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mock(NodePointer.class), new QName("attribute"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithNonMatchingAttributes() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("nonmatchingattribute"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithMatchingAttribute() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        Namespace ns = mock(Namespace.class);
        when(element.getAttribute("attribute", ns)).thenReturn(null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithEmptyAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mock(NodePointer.class), new QName("attribute"));
        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testSetPositionWithValidPosition() {
        NodePointer parent = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        NodePointer parent = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));
        assertFalse(iterator.setPosition(0));
    }
}
