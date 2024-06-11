package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        assertNull(pointer.createPath(context));

        NodePointer parent = new NodePointer(new Object());
        pointer = new NullPropertyPointer(parent);

        NodePointer result = pointer.createPath(context);
        assertSame(parent, result.getParent());
        assertFalse(result.isLeaf());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        pointer.setPropertyName("foo");
        assertEquals("foo", pointer.getPropertyName());

        pointer.setNameAttributeValue("bar");
        assertEquals("bar", pointer.getPropertyName());
        assertTrue(pointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isCollection());

        pointer.setPropertyName("foo[0]");
        assertTrue(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getPropertyCount());

        pointer.setPropertyName("foo[0]");
        assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertArrayEquals(new String[0], pointer.getPropertyNames());

        pointer.setPropertyName("foo[0]");
        assertArrayEquals(new String[0], pointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.asPath());

        pointer.setPropertyName("foo");
        assertSame(pointer, pointer.asPath());

        pointer.setNameAttributeValue("bar");
        String path = pointer.asPath();
        String expected = "[@name='bar']";
        assertTrue(path.startsWith(expected));
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        assertNull(pointer.createChild(context, new QName("foo"), 0));
        assertNull(pointer.createChild(context, new QName("foo"), 0, null));
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer pointer = new NullPropertyPointer(null);

        NodePointer result = pointer.createChild(context, new QName("foo"), 0, "value");
        assertSame(pointer, result.getParent());
        assertFalse(result.isLeaf());
        assertEquals("value", result.getValue());
    }
}