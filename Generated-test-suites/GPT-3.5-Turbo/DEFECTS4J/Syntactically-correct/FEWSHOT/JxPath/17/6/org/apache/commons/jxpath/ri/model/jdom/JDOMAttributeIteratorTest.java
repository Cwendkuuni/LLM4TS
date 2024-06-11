package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JDOMAttributeIteratorTest {
    private NodePointer parentMock;
    private QName nameMock;
    private Element elementMock;
    private Attribute attributeMock;

    @Before
    public void setup() {
        parentMock = mock(NodePointer.class);
        nameMock = mock(QName.class);
        elementMock = mock(Element.class);
        attributeMock = mock(Attribute.class);
    }

    @Test
    public void testConstructor() {
        Mockito.when(parentMock.getNode()).thenReturn(elementMock);
        Mockito.when(elementMock.getAttribute(Mockito.anyString(), Mockito.any(Namespace.class))).thenReturn(attributeMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, nameMock);

        assertEquals(elementMock, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, nameMock);

        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, nameMock);

        assertEquals(true, iterator.setPosition(1));
    }

    @Test
    public void testSetPositionInvalid() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, nameMock);

        assertEquals(false, iterator.setPosition(-1));
    }
}