package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(QName("", "*"), nullPropertyPointer.getName());
    }

    @Test
    public void testGetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
        // No assertions as this method has no return value
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.getValuePointer();
        assertEquals(NullPointer.class, nodePointer.getClass());
    }

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

    @Test
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        try {
            nullPropertyPointer.setValue(null);
        } catch (Exception e) {
            assertTrue(e instanceof JXPathInvalidAccessException);
        }
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertEquals(NullPropertyPointer.class, nodePointer.getClass());
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = nullPropertyPointer.createPath(context, null);
        assertEquals(NullPropertyPointer.class, nodePointer.getClass());
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName();
        NodePointer nodePointer = nullPropertyPointer.createChild(context, name, 0);
        assertEquals(NullPropertyPointer.class, nodePointer.getClass());
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName();
        Object value = null;
        NodePointer nodePointer = nullPropertyPointer.createChild(context, name, 0, value);
        assertEquals(NullPropertyPointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("*");
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("*");
        // No assertions as this method has no return value
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("*");
        assertEquals("*", nullPropertyPointer.getPropertyName());
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
        assertArrayEquals(new String[]{}, nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath()); 
    }

    private QName QName(String string, String string2) {
        return new QName();
    }
}