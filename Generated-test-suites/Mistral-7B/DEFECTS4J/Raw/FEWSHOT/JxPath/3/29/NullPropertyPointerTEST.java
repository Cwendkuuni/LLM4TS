package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNonPropertyOwnerParent() {
        NodePointer parent = new DomNodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueInvalidPath() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        NodePointer expected = new PropertyPointer(null);
        NodePointer actual = nullPropertyPointer.createPath(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        NodePointer expected = new PropertyPointer(null);
        Object value = new Object();
        NodePointer actual = nullPropertyPointer.createPath(null, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChild() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        NodePointer expected = new PropertyPointer(null);
        QName name = new QName("name");
        NodePointer actual = nullPropertyPointer.createChild(null, name, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithValue() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        NodePointer expected = new PropertyPointer(null);
        QName name = new QName("name");
        Object value = new Object();
        NodePointer actual = nullPropertyPointer.createChild(null, name, 0, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("propertyName");
        assertEquals("propertyName", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] expected = new String[0];
        String[] actual = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        String expected = "";
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPathByNameAttribute() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        nullPropertyPointer.setNameAttributeValue("name");
        String expected = "[@name='']";
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPathWithIndex() {
        PropertyOwnerPointer parentPropertyPointer = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(parentPropertyPointer);
        NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(parentPropertyPointer);
        nullPropertyPointer2.setPropertyName("name");
        nullPropertyPointer2.setIndex(1);
        String expected = "[name][1]";
        String actual = nullPropertyPointer2.asPath();
        assertEquals(expected, actual);
    }
}