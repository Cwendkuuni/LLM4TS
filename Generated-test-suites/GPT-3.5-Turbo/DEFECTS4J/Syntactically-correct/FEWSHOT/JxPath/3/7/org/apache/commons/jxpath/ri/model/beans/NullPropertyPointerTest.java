package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.*;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        QName expected = new QName("*");
        QName actual = pointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        int expected = 0;
        int actual = pointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Object expected = null;
        Object actual = pointer.getBaseValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Object expected = null;
        Object actual = pointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean actual = pointer.isLeaf();
        assertTrue(actual);
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer expected = new NullPointer(pointer, new QName(pointer.getPropertyName()));
        NodePointer actual = pointer.getValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean actual = pointer.isActualProperty();
        assertFalse(actual);
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean actual = pointer.isActual();
        assertFalse(actual);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean actual = pointer.isContainer();
        assertTrue(actual);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue(null);
    }

    @Test
    public void testCreatePathWithoutValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer newPath = pointer.createPath(context);
        assertNull(newPath);
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer newPath = pointer.createPath(context, null);
        assertNull(newPath);
    }

    @Test
    public void testCreateChildWithoutValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("name");
        int index = 0;
        NodePointer child = pointer.createChild(context, name, index);
        assertNotNull(child);
        assertTrue(child instanceof NodePointer);
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("name");
        int index = 0;
        Object value = null;
        NodePointer child = pointer.createChild(context, name, index, value);
        assertNotNull(child);
        assertTrue(child instanceof NodePointer);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String expected = "*";
        String actual = pointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String propertyName = "propertyName";
        pointer.setPropertyName(propertyName);
        String expected = propertyName;
        String actual = pointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String attributeValue = "attributeValue";
        pointer.setNameAttributeValue(attributeValue);
        String expected = attributeValue;
        String actual = pointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean actual = pointer.isCollection();
        assertFalse(actual);
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        int expected = 0;
        int actual = pointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String[] expected = new String[0];
        String[] actual = pointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String expected = "/[@name='*']";
        String actual = pointer.asPath();
        assertEquals(expected, actual);
    }

    private static class QName {
        private final String name;

        public QName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QName qName = (QName) o;
            return name != null ? name.equals(qName.name) : qName.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}