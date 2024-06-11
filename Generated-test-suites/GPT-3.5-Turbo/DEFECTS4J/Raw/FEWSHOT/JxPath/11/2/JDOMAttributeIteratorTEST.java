package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setup() {
        parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);

        name = new QName("test");

        attributeIterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, attributeIterator.parent);
        assertNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testGetNodePointer() {
        Attribute attribute = mock(Attribute.class);
        attributeIterator.attributes = Collections.singletonList(attribute);

        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
        assertEquals(parent, nodePointer.getParent());
        assertEquals(attribute, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.position = 3;

        assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_WithNullAttributes() {
        attributeIterator.attributes = null;

        boolean result = attributeIterator.setPosition(1);

        assertFalse(result);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testSetPosition_WithInvalidPosition() {
        attributeIterator.attributes = Collections.singletonList(mock(Attribute.class));

        boolean result = attributeIterator.setPosition(-1);

        assertFalse(result);
        assertEquals(0, attributeIterator.position);

        result = attributeIterator.setPosition(2);

        assertFalse(result);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testSetPosition_WithValidPosition() {
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        attributeIterator.attributes = Arrays.asList(attribute1, attribute2);

        boolean result = attributeIterator.setPosition(1);

        assertTrue(result);
        assertEquals(1, attributeIterator.position);

        result = attributeIterator.setPosition(2);

        assertTrue(result);
        assertEquals(2, attributeIterator.position);
    }
}