package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testIsNullPropertyPointer() {
        NullPropertyPointer npp = new NullPropertyPointer(null);
        assertNull(npp.getBaseValue());
        assertNull(npp.getImmediateNode());
        assertFalse(npp.isLeaf());
        assertFalse(npp.isContainer());
        assertFalse(npp.isActual());
        assertFalse(npp.isActualProperty());
        assertEquals(0, npp.getLength());
        assertEquals("", npp.asPath());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer npp = new NullPropertyPointer(new NodePointer(null));
        assertTrue(npp.isContainer());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer npp = new NullPropertyPointer(new NodePointer(null));
        npp.setPropertyName("foo");
        assertEquals("foo", npp.getPropertyName());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer npp = new NullPropertyPointer(new NodePointer(null));
        npp.setValue("foo");
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer npp = new NullPropertyPointer(parent);
        NodePointer result = npp.createPath(null);
        assertNull(result);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer npp = new NullPropertyPointer(parent);
        NodePointer result = npp.createPath(null, "foo");
        assertNull(result);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer npp = new NullPropertyPointer(parent);
        NodePointer result = npp.createChild(null, new QName("foo"), 0);
        assertNull(result);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer npp = new NullPropertyPointer(parent);
        NodePointer result = npp.createChild(null, new QName("foo"), 0, "bar");
        assertNull(result);
    }
}