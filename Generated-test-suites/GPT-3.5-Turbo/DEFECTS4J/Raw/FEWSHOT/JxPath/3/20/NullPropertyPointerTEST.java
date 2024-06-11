package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        QName expected = new QName("*");
        assertEquals(expected, pointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NullPointer nullPointer = new NullPointer(pointer, new QName(getPropertyName()));
        assertEquals(nullPointer, pointer.getValuePointer());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertFalse(pointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertTrue(pointer.isContainer());
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        Object value = new Object();
        try {
            pointer.setValue(value);
            fail("Expected JXPathInvalidAccessException to be thrown");
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property null, the target object is null", e.getMessage());
        }
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer result = pointer.createPath(context);
        NullPointer nullPointer = new NullPointer(pointer, new QName(getPropertyName()));
        assertEquals(nullPointer, result);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer result = pointer.createPath(context, value);
        NullPointer nullPointer = new NullPointer(pointer, new QName(getPropertyName()));
        nullPointer.setValue(value);
        assertEquals(nullPointer, result);
    }

    @Test
    public void testCreateChildWithIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName();
        int index = 1;
        NodePointer result = pointer.createChild(context, name, index);
        NullPointer nullPointer = new NullPointer(pointer, new QName(getPropertyName()));
        assertEquals(nullPointer, result);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName();
        int index = 1;
        Object value = new Object();
        NodePointer result = pointer.createChild(context, name, index, value);
        NullPointer nullPointer = new NullPointer(pointer, new QName(getPropertyName()));
        nullPointer.createChild(context, name, index, value);
        assertEquals(nullPointer, result);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String expected = "*";
        assertEquals(expected, pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String propertyName = "test";
        pointer.setPropertyName(propertyName);
        assertEquals(propertyName, pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String attributeValue = "name";
        pointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, pointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String[] expected = new String[0];
        assertArrayEquals(expected, pointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        String expected = "[][@name='*'][1]";
        assertEquals(expected, pointer.asPath());
    }

    private String getPropertyName() {
        return "*";
    }
}