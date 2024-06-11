
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private List attributes;
    private JDOMAttributeIterator jdomAttributeIterator;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        attributes = Mockito.mock(ArrayList.class);

        jdomAttributeIterator = new JDOMAttributeIterator(parent, new QName("", ""));
        jdomAttributeIterator.attributes = attributes;
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, jdomAttributeIterator.parent);
        assertEquals(attributes, jdomAttributeIterator.attributes);
        assertEquals(0, jdomAttributeIterator.position);
    }

    @Test
    public void testGetNodePointer() {
        Attribute attribute = Mockito.mock(Attribute.class);
        Mockito.when(attributes.get(0)).thenReturn(attribute);

        NodePointer pointer = Mockito.mock(JDOMAttributePointer.class);
        Mockito.when(pointer.getPosition()).thenReturn(1);
        Mockito.when(pointer.getNode()).thenReturn(attribute);
        Mockito.when(attribute.getValue()).thenReturn("attribute value");

        Mockito.when(jdomAttributeIterator.getNodePointer()).thenReturn(pointer);

        NodePointer result = jdomAttributeIterator.getNodePointer();
        assertEquals(attribute, result.getNode());
        assertEquals("attribute value", ((Attribute) result.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.position = 2;
        assertEquals(2, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Mockito.when(attributes.size()).thenReturn(4);

        // valid position
        boolean result = jdomAttributeIterator.setPosition(2);
        assertTrue(result);
        assertEquals(2, jdomAttributeIterator.position);

        // invalid position
        result = jdomAttributeIterator.setPosition(5);
        assertFalse(result);
        assertEquals(5, jdomAttributeIterator.position);

        // valid position
        result = jdomAttributeIterator.setPosition(1);
        assertTrue(result);
        assertEquals(1, jdomAttributeIterator.position);
    }
}
