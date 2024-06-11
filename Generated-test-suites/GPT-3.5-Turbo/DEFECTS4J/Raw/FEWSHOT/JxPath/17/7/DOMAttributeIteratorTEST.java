
package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Mock
    private NodePointer parent;
    @Mock
    private QName name;
    @Mock
    private Node node;
    @Mock
    private Element element;
    @Mock
    private Attr attr1;
    @Mock
    private Attr attr2;

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("testName");
        when(name.getPrefix()).thenReturn("testPrefix");
        when(parent.getNamespaceURI("testPrefix")).thenReturn("testNS");
        
        when(element.getAttributeNodeNS("testNS", "testName")).thenReturn(attr1);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributes().getLength()).thenReturn(2);
        when(element.getAttributes().item(0)).thenReturn(attr1);
        when(element.getAttributes().item(1)).thenReturn(attr2);
        
        iterator = spy(new DOMAttributeIterator(parent, name));
        doReturn(element).when(iterator).getAttribute(any(Element.class), any(QName.class));
        doReturn(true).when(iterator).testAttr(any(Attr.class));
    }

    @Test
    public void testConstructor() {
        verify(iterator).getAttribute(any(Element.class), any(QName.class));
        assertEquals(2, iterator.attributes.size());
    }

    @Test
    public void testGetNodePointer() {
        assertNull(iterator.getNodePointer());

        iterator.setPosition(1);
        assertEquals(attr1, iterator.getNodePointer().getNode());
        iterator.setPosition(2);
        assertEquals(attr2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }
}
