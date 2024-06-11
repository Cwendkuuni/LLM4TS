package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createPath(context));

        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(new NodePointer(new Object()));
        assertNotNull(nullPropertyPointerWithParent.createPath(context));
    }

    @Test
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        try {
            nullPropertyPointer.setValue(new Object());
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }

        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(parent);
        nullPropertyPointerWithParent.setValue(new Object());
        assertNotNull(parent.getValue());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(context, new QName("name"), 1));

        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(new NodePointer(new Object()));
        assertNotNull(nullPropertyPointerWithParent.createChild(context, new QName("name"), 1));
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createChild(context, new QName("name"), 1, new Object()));

        NullPropertyPointer nullPropertyPointerWithParent = new NullPropertyPointer(new NodePointer(new Object()));
        NodePointer child = nullPropertyPointerWithParent.createChild(context, new QName("name"), 1, new Object());
        assertNotNull(child);
        assertEquals("name", child.getName().getLocalPart());
        assertEquals(1, child.getIndex());
        assertEquals(new Object(), child.getValue());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());

        NullPropertyPointer nullPropertyPointerWithName = new NullPropertyPointer(null);
        nullPropertyPointerWithName.setPropertyName("name");
        assertEquals("name", nullPropertyPointerWithName.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());

        NullPropertyPointer nullPropertyPointerWithIndex = new NullPropertyPointer(null);
        nullPropertyPointerWithIndex.setIndex(1);
        assertTrue(nullPropertyPointerWithIndex.isCollection());
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

        NullPropertyPointer nullPropertyPointerWithName = new NullPropertyPointer(null);
        nullPropertyPointerWithName.setNameAttributeValue("name");
        nullPropertyPointerWithName.setIndex(1);
        assertEquals("[@name='name']", nullPropertyPointerWithName.asPath());
    }

    @Test
    public void testEscape() {
        assertEquals("", NullPropertyPointer.escape(""));
        assertEquals("\'", NullPropertyPointer.escape("\'"));
        assertEquals("\"", NullPropertyPointer.escape("\""));
        assertEquals("foo\'s\"bar", NullPropertyPointer.escape("foo\'s\"bar"));
    }
}