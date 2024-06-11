package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getName().getLocalPart());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(null));
        assertEquals("*", nullPropertyPointer1.getName().getLocalPart());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(null);
        assertSame(parent, result);

        NodePointer parent1 = new NodePointer(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent1);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer result1 = nullPropertyPointer1.createPath(null);
        assertSame(propertyOwnerPointer.getPropertyPointer(), result1);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(null, new Object());
        assertNull(result);

        NodePointer parent1 = new NodePointer(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent1);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer result1 = nullPropertyPointer1.createPath(null, new Object());
        assertSame(propertyOwnerPointer.getPropertyPointer(), result1);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(null, new QName("test"), 0);
        assertNull(result);

        NodePointer parent1 = new NodePointer(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent1);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer result1 = nullPropertyPointer1.createChild(null, new QName("test"), 0, new Object());
        assertSame(propertyOwnerPointer.getPropertyPointer(), result1);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(null, new QName("test"), 0, new Object());
        assertNull(result);

        NodePointer parent1 = new NodePointer(new Object());
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent1);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(propertyOwnerPointer);
        NodePointer result1 = nullPropertyPointer1.createChild(null, new QName("test"), 0, new Object());
        assertSame(propertyOwnerPointer.getPropertyPointer(), result1);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
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
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(new NodePointer(new Object()));
        assertEquals("[@name='*']", nullPropertyPointer1.asPath());

        NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer2.setNameAttributeValue("test");
        assertEquals("[@name='test']", nullPropertyPointer2.asPath());

        NullPropertyPointer nullPropertyPointer3 = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer3.setNameAttributeValue("test");
        nullPropertyPointer3.setIndex(1);
        assertEquals("[@name='test'][1]", nullPropertyPointer3.asPath());
    }

    @Test
    public void testEscape() {
        String string = "'test'\"string";
        String result = new NullPropertyPointer().escape(string);
        assertEquals("&apos;test&quot;string", result);
    }
}