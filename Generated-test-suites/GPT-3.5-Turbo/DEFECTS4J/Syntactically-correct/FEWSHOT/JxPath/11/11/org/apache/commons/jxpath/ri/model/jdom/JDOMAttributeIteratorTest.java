
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
    }

    @Test
    public void testGetNodePointer_FirstPosition() {
        Element element = mock(Element.class);
        Attribute attribute = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        when(parent.getNode()).thenReturn(element);
        when(element.getAttribute("name", Namespace.XML_NAMESPACE)).thenReturn(attribute);
        when(attribute.getNamespace()).thenReturn(Namespace.XML_NAMESPACE);

        iterator = new JDOMAttributeIterator(parent, new QName("name", "xml"));

        assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointer_NonFirstPosition() {
        Element element = mock(Element.class);
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);

        when(parent.getNode()).thenReturn(element);
        when(element.getAttributes()).thenReturn(attributes);
        when(attribute1.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);
        when(attribute2.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);

        iterator = new JDOMAttributeIterator(parent, new QName("*", ""));

        assertEquals(attribute1, iterator.getNodePointer().getNode());

        iterator.setPosition(2);

        assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetPosition() {
        Element element = mock(Element.class);
        Attribute attribute1 = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);

        when(parent.getNode()).thenReturn(element);
        when(element.getAttribute("name", Namespace.XML_NAMESPACE)).thenReturn(attribute1);
        when(attribute1.getNamespace()).thenReturn(Namespace.XML_NAMESPACE);

        iterator = new JDOMAttributeIterator(parent, new QName("name", "xml"));

        assertEquals(0, iterator.getPosition());

        iterator.getNodePointer();

        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Element element = mock(Element.class);
        Attribute attribute1 = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);

        when(parent.getNode()).thenReturn(element);
        when(element.getAttribute("name", Namespace.XML_NAMESPACE)).thenReturn(attribute1);
        when(attribute1.getNamespace()).thenReturn(Namespace.XML_NAMESPACE);

        iterator = new JDOMAttributeIterator(parent, new QName("name", "xml"));

        boolean result = iterator.setPosition(1);

        assertTrue(result);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Element element = mock(Element.class);
        Attribute attribute1 = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);

        when(parent.getNode()).thenReturn(element);
        when(element.getAttribute("name", Namespace.XML_NAMESPACE)).thenReturn(attribute1);
        when(attribute1.getNamespace()).thenReturn(Namespace.XML_NAMESPACE);

        iterator = new JDOMAttributeIterator(parent, new QName("name", "xml"));

        boolean result = iterator.setPosition(2);

        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition_InvalidAttributes() {
        iterator = new JDOMAttributeIterator(parent, new QName("name", "xml"));

        boolean result = iterator.setPosition(1);

        assertFalse(result);
        assertEquals(0, iterator.getPosition());
    }
}
