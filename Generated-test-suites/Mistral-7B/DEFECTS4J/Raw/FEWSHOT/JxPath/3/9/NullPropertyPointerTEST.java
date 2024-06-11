package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        QName name = pointer.getName();
        assertEquals("*", name.getLocalPart());
        assertEquals("", name.getNamespaceURI());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer valuePointer = pointer.getValuePointer();
        assertNotNull(valuePointer);
        assertEquals(NullPointer.class, valuePointer.getClass());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isContainer());
    }

    @Test
    public void testSetValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.setValue(new Object());
            fail();
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        NodePointer newParent = null;
        try {
            newParent = pointer.createPath(context);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(newParent);
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        Object value = new Object();
        NodePointer newParent = null;
        try {
            newParent = pointer.createPath(context, value);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(newParent);
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        QName name = null;
        int index = 1;
        NodePointer newChild = null;
        try {
            newChild = pointer.createChild(context, name, index);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(newChild);
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = null;
        QName name = null;
        int index = 1;
        Object value = new Object();
        NodePointer newChild = null;
        try {
            newChild = pointer.createChild(context, name, index, value);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(newChild);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("propName");
        assertEquals("propName", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("propName");
        assertEquals("propName", pointer.getPropertyName());
        assertTrue(pointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String[] propertyNames = pointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("", pointer.asPath());
        NullPropertyPointer attributePointer = new NullPropertyPointer(null);
        attributePointer.setNameAttributeValue("name");
        pointer.setImmediateParentPointer(attributePointer);
        pointer.setIndex(1);
        assertEquals("[@name=''][1]", pointer.asPath());
    }

    @Test
    public void testEscape() {
        String string = "abc'def'";
        String escapedString = NullPropertyPointer.escape(string);
        assertEquals("abc&apos;def&apos;", escapedString);
    }
}