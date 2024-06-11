package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testIsNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isContainer());
        assertNull(nullPropertyPointer.getValuePointer());
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(1));
        assertNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testNullPropertyPointerWithParent() {
        NodePointer parentPointer = new NodePointer(new QName("root"), null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        assertEquals("root", nullPropertyPointer.getImmediateParentPointer().asPath());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertTrue(nullPropertyPointer.isContainer());
        assertNull(nullPropertyPointer.getValuePointer());
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(1));
        assertNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
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
        assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(new NodePointer(new QName("root"), null));
        assertEquals("root", nullPropertyPointerWithParent.asPath());

        NullPropertyPointer nullPropertyPointerWithNameAndIndex = new NullPropertyPointer(new NodePointer(new QName("root"), null));
        nullPropertyPointerWithNameAndIndex.setPropertyName("test");
        nullPropertyPointerWithNameAndIndex.setIndex(1);
        assertEquals("root[1]", nullPropertyPointerWithNameAndIndex.asPath());
    }

    @Test
    public void testEscape() {
        assertEquals("", NullPropertyPointer.escape(""));
        assertEquals("&apos;test&apos;", NullPropertyPointer.escape("test"));
        assertEquals("&quot;test&quot;", NullPropertyPointer.escape("test"));
        assertEquals("&apos;test&apos;&quot;another test&quot;", NullPropertyPointer.escape("test'another test"));
    }
}