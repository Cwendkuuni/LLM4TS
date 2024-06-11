package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DOMAttributeIteratorTest {
    @Mock
    private NodePointer parent;
    @Mock
    private QName name;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        attributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        Attr attr = mock(Attr.class);
        attributeIterator.attributes.add(attr);
        when(parent.getNamespaceURI("testPrefix")).thenReturn("testURI");
        when(name.getName()).thenReturn("testName");
        when(name.getPrefix()).thenReturn("testPrefix");
        DOMAttributePointer expectedPointer = new DOMAttributePointer(parent, attr);
        assertEquals(expectedPointer, attributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        attributeIterator.position = 3;
        assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributeIterator.attributes.add(mock(Attr.class));
        attributeIterator.attributes.add(mock(Attr.class));
        attributeIterator.attributes.add(mock(Attr.class));
        assertEquals(false, attributeIterator.setPosition(0));
        assertEquals(true, attributeIterator.setPosition(1));
        assertEquals(true, attributeIterator.setPosition(2));
        assertEquals(true, attributeIterator.setPosition(3));
        assertEquals(false, attributeIterator.setPosition(4));
    }
}