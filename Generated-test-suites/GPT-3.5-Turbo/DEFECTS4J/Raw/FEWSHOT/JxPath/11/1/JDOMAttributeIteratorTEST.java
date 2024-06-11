package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);

        when(parent.getNode()).thenReturn(element);
        when(element.getAttribute("attr1", Namespace.NO_NAMESPACE)).thenReturn(attribute1);
        when(element.getAttribute("attr2", Namespace.NO_NAMESPACE)).thenReturn(attribute2);
        when(element.getAttributes()).thenReturn(Arrays.asList(attribute1, attribute2));

        attributeIterator = new JDOMAttributeIterator(parent, new QName(null, "*", null));
    }

    @Test
    public void testConstructor() {
        assertNotNull(attributeIterator);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(3));
    }

    @Test
    public void testGetNodePointer() {
        attributeIterator.setPosition(1);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("Attribute1Value", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetNodePointerNull() {
        attributeIterator.setPosition(3);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test(expected = JXPathException.class)
    public void testGetNodePointerException() {
        attributeIterator.setPosition(1);
        when(parent.getNode()).thenReturn(null);
        attributeIterator.getNodePointer();
    }
}