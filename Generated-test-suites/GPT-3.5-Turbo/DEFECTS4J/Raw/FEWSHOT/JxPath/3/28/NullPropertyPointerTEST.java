package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    private NullPropertyPointer nullPropertyPointer;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(5);
        int expected = 5;
        int actual = nullPropertyPointer.getIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLength() {
        int expected = 0;
        int actual = nullPropertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.getValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValue() {
        try {
            nullPropertyPointer.setValue(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot set property , the target object is null", e.getMessage());
        }
    }

    @Test
    public void testCreatePathWithContext() {
        JXPathContext context = new JXPathContext();
        NodePointer newParent = nullPropertyPointer.createPath(context);
        assertNotNull(newParent);
    }

    @Test
    public void testCreatePathWithContextAndValue() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer newParent = nullPropertyPointer.createPath(context, value);
        assertNotNull(newParent);
        assertEquals(value, newParent.getValue());
    }

    @Test
    public void testCreateChildWithContextandNameAndIndex() {
        QName name = new QName("name");
        int index = 5;
        JXPathContext context = new JXPathContext();
        NodePointer child = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(child);
    }

    @Test
    public void testCreateChildWithContextAndNameAndIndexAndValue() {
        QName name = new QName("name");
        int index = 5;
        Object value = new Object();
        JXPathContext context = new JXPathContext();
        NodePointer child = nullPropertyPointer.createChild(context, name, index, value);
        assertNotNull(child);
        assertEquals(value, child.getValue());
    }

    @Test
    public void testPropertyNameGetterAndSetter() {
        String propertyName = "propertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        String expected = propertyName;
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "attributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        nullPropertyPointer.setPropertyIndex(5);
        assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        int expected = 0;
        int actual = nullPropertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[0];
        String[] actual = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        String expected = "/@name='propertyName'";
        nullPropertyPointer.setPropertyName("propertyName");
        nullPropertyPointer.setNameAttributeValue("propertyName");
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        String string = "string'\"string";
        String expected = "string&apos;&quot;string";
        String actual = nullPropertyPointer.escape(string);
        assertEquals(expected, actual);
    }
}