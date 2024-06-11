
package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        NodePointer parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        // No assertion as the method has a void return type
    }

    @Test
    public void testGetLength() {
        int length = nullPropertyPointer.getLength();
        assertEquals(0, length);
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
        boolean isLeaf = nullPropertyPointer.isLeaf();
        assertTrue(isLeaf);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertTrue(valuePointer instanceof NullPointer);
    }

    @Test
    public void testIsActualProperty() {
        boolean isActualProperty = nullPropertyPointer.isActualProperty();
        assertFalse(isActualProperty);
    }

    @Test
    public void testIsActual() {
        boolean isActual = nullPropertyPointer.isActual();
        assertFalse(isActual);
    }

    @Test
    public void testIsContainer() {
        boolean isContainer = nullPropertyPointer.isContainer();
        assertTrue(isContainer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_WithNullParent() {
        nullPropertyPointer.setValue("testValue");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_WithNonNullParentAndDynamicPropertyDeclarationNotSupported() {
        // Create a non-null parent
        NodePointer parent = new NodePointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("testValue");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer path = nullPropertyPointer.createPath(context);
        assertNotNull(path);
        assertTrue(path instanceof NullPointer);
    }

    @Test
    public void testCreatePath_WithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer path = nullPropertyPointer.createPath(context, "testValue");
        assertNotNull(path);
        assertTrue(path instanceof NullPointer);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        int index = 0;
        NodePointer child = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(child);
        assertTrue(child instanceof NullPointer);
    }

    @Test
    public void testCreateChild_WithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("testName");
        int index = 0;
        NodePointer child = nullPropertyPointer.createChild(context, name, index, "testValue");
        assertNotNull(child);
        assertTrue(child instanceof NullPointer);
    }

    @Test
    public void testIsCollection() {
        boolean isCollection = nullPropertyPointer.isCollection();
        assertFalse(isCollection);
    }

    @Test
    public void testGetPropertyCount() {
        int propertyCount = nullPropertyPointer.getPropertyCount();
        assertEquals(0, propertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        nullPropertyPointer.setPropertyName("testName");
        String path = nullPropertyPointer.asPath();
        assertEquals("*/[@name='testName']", path);
    }

    @Test
    public void testEscape() {
        String escapedString = nullPropertyPointer.escape("test's string");
        assertEquals("test&apos;s string", escapedString);
    }
}
