import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator jdomAttributeIterator;
    private NodePointer mockParent;
    private QName mockName;
    private Element mockElement;
    private Attribute mockAttribute;
    private Namespace mockNamespace;

    @Before
    public void setUp() {
        mockParent = mock(NodePointer.class);
        mockName = mock(QName.class);
        mockElement = mock(Element.class);
        mockAttribute = mock(Attribute.class);
        mockNamespace = mock(Namespace.class);

        when(mockParent.getNode()).thenReturn(mockElement);
        when(mockName.getPrefix()).thenReturn("prefix");
        when(mockElement.getAttribute("name", mockNamespace)).thenReturn(mockAttribute);

        jdomAttributeIterator = new JDOMAttributeIterator(mockParent, mockName);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertSame(mockAttribute, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(jdomAttributeIterator.setPosition(0));
        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());
        assertTrue(jdomAttributeIterator.setPosition(2));
        assertEquals(2, jdomAttributeIterator.getPosition());
        assertFalse(jdomAttributeIterator.setPosition(3));
    }
}