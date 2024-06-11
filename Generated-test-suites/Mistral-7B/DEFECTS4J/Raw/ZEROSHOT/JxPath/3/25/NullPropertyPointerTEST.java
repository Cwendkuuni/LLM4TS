package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.QName;

/**
 * Test class for {@link NullPropertyPointer} class.
 */
public class NullPropertyPointerTest {

    @SuppressWarnings("unchecked")
    private static final NodePointer NULL_POINTER = new NullPointer(null, new QName(""));

    @org.junit.Test
    public void testNewNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertEquals(NULL_POINTER, nullPropertyPointer.getParent());
        assertEquals("", nullPropertyPointer.getPropertyName());
        assertNull(nullPropertyPointer.getValuePointer());
        assertTrue(nullPropertyPointer.isContainer());
        assertFalse(nullPropertyPointer.isLeaf());
    }

    @org.junit.Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        QName name = new QName("test");
        nullPropertyPointer.setPropertyName(name.getLocalPart());
        assertEquals(name, nullPropertyPointer.getName());
    }

    @org.junit.Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @org.junit.Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        nullPropertyPointer.setPropertyIndex(1);
    }

    @org.junit.Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @org.junit.Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @org.junit.Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @org.junit.Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertFalse(nullPropertyPointer.isLeaf());
    }

    @org.junit.Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @org.junit.Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertFalse(nullPropertyPointer.isActual());
    }

    @org.junit.Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @org.junit.Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        nullPropertyPointer.setValue("value");
    }

    @org.junit.Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object[0]);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        NodePointer result = nullPropertyPointer.createPath(context);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(new Object[0]);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createPath(context, value);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object[0]);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        QName name = new QName("child");
        NodePointer result = nullPropertyPointer.createChild(context, name);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new Object[0]);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        Object value = new Object();
        NodePointer result = nullPropertyPointer.createChild(context, new QName("child"), 1, value);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreatePathWithParent() {
        JXPathContext context = new JXPathContext(new Object[0]);
        NullPropertyPointer parentPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        NullPropertyPointer childPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        NodePointer result = childPropertyPointer.createPath(context);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreatePathWithParentAndValue() {
        JXPathContext context = new JXPathContext(new Object[0]);
        NullPropertyPointer parentPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        Object value = new Object();
        NullPropertyPointer childPropertyPointer = new NullPropertyPointer(parentPropertyPointer);
        NodePointer result = childPropertyPointer.createPath(context, value);
        assertNull(result);
    }

    @org.junit.Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        assertEquals("", nullPropertyPointer.asPath());
    }

    @org.junit.Test
    public void testAsPathWithAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(NULL_POINTER);
        nullPropertyPointer.setNameAttributeValue("test");
        int index = 1;
        assertEquals("[@name='test']" + index, nullPropertyPointer.asPath());
    }
}