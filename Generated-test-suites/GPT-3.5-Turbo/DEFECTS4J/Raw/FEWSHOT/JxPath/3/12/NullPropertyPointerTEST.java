package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;


public class NullPropertyPointerTest {
    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @After
    public void tearDown() {
        nullPropertyPointer = null;
    }

    @Test
    public void testGetName() {
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        int expected = 0;
        int actual = nullPropertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        Object expected = null;
        Object actual = nullPropertyPointer.getBaseValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        Object expected = null;
        Object actual = nullPropertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        boolean expected = true;
        boolean actual = nullPropertyPointer.isLeaf();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValuePointer() {
        NullPointer expected = new NullPointer(null, new QName("*"));
        NodePointer actual = nullPropertyPointer.getValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        boolean expected = false;
        boolean actual = nullPropertyPointer.isActualProperty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        boolean expected = false;
        boolean actual = nullPropertyPointer.isActual();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainer() {
        boolean expected = true;
        boolean actual = nullPropertyPointer.isContainer();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetValue() {
        try {
            nullPropertyPointer.setValue(null);
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NullPointer expected = new NullPointer(null,  new QName("*"));
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = null;
        NodePointer expected = new NullPointer(null,  new QName("*"));
        NodePointer actual = nullPropertyPointer.createPath(context, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithNameAndIndex() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        int index = 1;
        NodePointer expected = new NullPointer(null,  new QName("*"));
        NodePointer actual = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithNameIndexAndValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        int index = 1;
        Object value = null;
        NodePointer expected = new NullPointer(null,  new QName("*"));
        NodePointer actual = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        String expected = "*";
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "test";
        nullPropertyPointer.setPropertyName(propertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "test";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
    }

    @Test
    public void testIsCollection() {
        boolean expected = false;
        boolean actual = nullPropertyPointer.isCollection();
        assertEquals(expected, actual);
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
        String expected = "[@name='*']";
        String actual = nullPropertyPointer.asPath();
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        String string = "test";
        String expected = "test";
        String actual = nullPropertyPointer.escape(string);
        assertEquals(expected, actual);
    }
}