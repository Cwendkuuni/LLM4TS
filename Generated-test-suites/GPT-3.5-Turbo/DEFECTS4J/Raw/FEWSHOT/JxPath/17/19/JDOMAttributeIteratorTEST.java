package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;

    @Before
    public void setup() {
        parent = new NodePointer();
    }

    @Test
    public void testConstructor() {
        QName name = new QName("foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(attributeIterator);
    }

    @Test
    public void testConstructorWithXMLPrefix() {
        QName name = new QName("xml:foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(attributeIterator);
    }

    @Test
    public void testConstructorWithNullPrefix() {
        QName name = new QName(":foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(attributeIterator);
    }

    @Test
    public void testGetPositionWithNullAttributes() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("foo"));
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionWithNullAttributes() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("foo"));
        assertFalse(attributeIterator.setPosition(2));
    }

    @Test
    public void testSetPositionOutOfBounds() {
        QName name = new QName("foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(2));
    }

    @Test
    public void testSetPosition() {
        QName name = new QName("foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointerWithPositionZero() {
        QName name = new QName("foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        attributeIterator.setPosition(0);
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithPositionOutOfBounds() {
        QName name = new QName("foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        attributeIterator.setPosition(2);
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName("foo");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        attributeIterator.setPosition(1);
        assertNotNull(attributeIterator.getNodePointer());
    }
}