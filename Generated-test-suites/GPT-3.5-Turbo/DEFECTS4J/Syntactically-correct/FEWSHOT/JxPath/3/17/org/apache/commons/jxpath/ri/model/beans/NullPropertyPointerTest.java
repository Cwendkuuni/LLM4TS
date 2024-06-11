
package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        QName name = new QName("*");
        assertEquals(name, nullPointer.getName());
    }

    @Test
    public void testGetPropertyIndex() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        nullPointer.setPropertyIndex(1);
        assertEquals(0, nullPointer.getPropertyIndex());
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertNull(nullPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertNull(nullPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertTrue(nullPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        NullPointer expectedPointer = new NullPointer(null, new QName("*"));
        assertEquals(expectedPointer, nullPointer.getValuePointer());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertFalse(nullPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertFalse(nullPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertTrue(nullPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        nullPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        NodePointer expectedPointer = new NullPointer(null, new QName("*"));
        assertEquals(expectedPointer, nullPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        Object value = "value";
        NodePointer expectedPointer = new NullPointer(null, new QName("*"));
        assertEquals(expectedPointer, nullPointer.createPath(context, value));
    }

    @Test
    public void testCreateChildWithNameAndIndex() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("name");
        int index = 1;
        expectedPointer = nullPointer.createPath(context).createChild(context, name, index);
        assertEquals(expectedPointer, nullPointer.createChild(context, name, index));
    }

    @Test
    public void testCreateChildWithNameIndexAndValue() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        JXPathContext context = JXPathContext.newContext(null);
        QName name = new QName("name");
        int index = 1;
        Object value = "value";
        expectedPointer = nullPointer.createPath(context).createChild(context, name, index, value);
        assertEquals(expectedPointer, nullPointer.createChild(context, name, index, value));
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        String propertyName = "name";
        nullPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        String attributeValue = "attribute";
        nullPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertFalse(nullPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        String[] propertyNames = new String[0];
        assertArrayEquals(propertyNames, nullPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        String path = "/*";
        assertEquals(path, nullPointer.asPath());
    }

    @Test
    public void testEscape() {
        NullPropertyPointer nullPointer = new NullPropertyPointer(null);
        String string = "'string'";
        String escapedString = "&apos;string&apos;";
        assertEquals(escapedString, nullPointer.escape(string));
    }
}
