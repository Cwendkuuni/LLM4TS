package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testCreateNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertSame(parent, nullPropertyPointer.getParent());
        assertSame(parent, nullPropertyPointer.getValuePointer().getParent());
        assertEquals(0, nullPropertyPointer.getLength());
        assertEquals(true, nullPropertyPointer.isLeaf());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValueNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue("value"));
    }

    @Test
    public void testCreatePathNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer nodePointer = new NodePointer(new Object());
        JXPathContext context = new JXPathContext(nodePointer);
        NodePointer createdPath = nullPropertyPointer.createPath(context);
        assertNull(createdPath.getBaseValue());
        assertNull(createdPath.getImmediateNode());
        assertSame(nodePointer, createdPath.getParent());
    }

    @Test
    public void testCreatePathNullPropertyPointerWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer nodePointer = new NodePointer(new Object());
        JXPathContext context = new JXPathContext(nodePointer);
        NodePointer createdPath = nullPropertyPointer.createPath(context, "value");
        assertNull(createdPath.getBaseValue());
        assertNull(createdPath.getImmediateNode());
        assertSame(nodePointer, createdPath.getParent());
    }

    @Test
    public void testCreateChildNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer createdChild = nullPropertyPointer.createChild(context, new QName("name"), 0);
        assertNull(createdChild.getBaseValue());
        assertNull(createdChild.getImmediateNode());
        assertSame(parent, createdChild.getParent());
    }

    @Test
    public void testCreateChildNullPropertyPointerWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer createdChild = nullPropertyPointer.createChild(context, new QName("name"), 0, "value");
        assertNull(createdChild.getBaseValue());
        assertNull(createdChild.getImmediateNode());
        assertSame(parent, createdChild.getParent());
    }

    @Test
    public void testGetNameNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.getLocalPart());
    }

    @Test
    public void testSetPropertyNameNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValueNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollectionNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCountNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNamesNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPathNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscapeNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String string = "foo'bar\"qux";
        String escapedString = nullPropertyPointer.escape(string);
        assertEquals("foo&apos;bar&quot;qux", escapedString);
    }
}