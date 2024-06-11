import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = new QName("test", "attr");
    }

    @Test
    public void testConstructor_WithValidElement() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        verify(element, times(1)).getNodeName();
        verify(parent, times(1)).getNode();
    }

    @Test
    public void testConstructor_WithNonElementNode() {
        Node node = mock(Node.class);
        when(parent.getNode()).thenReturn(node);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        verify(node, never()).getAttributes();
        verify(parent, times(1)).getNode();
    }

    @Test
    public void testConstructor_WithWildcardName() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);
        name = new QName("*");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        verify(element, times(1)).getAttributes();
        verify(parent, times(1)).getNode();
    }

    @Test
    public void testGetNodePointer_WithValidPosition() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        assertEquals(parent, nodePointer.getParent());
        assertEquals(position, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer_WithInvalidPosition() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(0);

        NodePointer nodePointer = iterator.getNodePointer();

        assertEquals(null, nodePointer);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        int position = iterator.getPosition();

        assertEquals(1, position);
    }

    @Test
    public void testSetPosition_WithValidPosition() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(2);

        assertEquals(true, result);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition_WithInvalidPosition() {
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(0);

        assertEquals(false, result);
        assertEquals(0, iterator.getPosition());
    }
}



// Additional test cases for edge cases and exceptions

