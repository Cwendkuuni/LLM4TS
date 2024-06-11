package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertSame(null, nullPropertyPointer.parent);
        assertNull(nullPropertyPointer.getValuePointer().getBaseValue());
        assertNull(nullPropertyPointer.getValuePointer().getImmediateNode());
        assertSame(nullPropertyPointer, nullPropertyPointer.getValuePointer().parent);
        assertEquals("", nullPropertyPointer.asPath());
        assertNull(nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isActual());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertTrue(nullPropertyPointer.isContainer());
        assertFalse(nullPropertyPointer.isLeaf());
        assertFalse(nullPropertyPointer.isCollection());
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("foo");
        assertEquals("foo", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("bar");
        assertEquals("bar", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertEquals(null, nodePointer.getBaseValue());
        assertEquals(null, nodePointer.getImmediateNode());
        assertEquals(nullPropertyPointer, nodePointer.parent);
        assertEquals("root/", nodePointer.asPath());
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer nodePointer = nullPropertyPointer.createPath(context, value);
        assertEquals(value, nodePointer.getValuePointer().getBaseValue());
        assertEquals(nullPropertyPointer, nodePointer.parent);
        assertEquals("root/", nodePointer.asPath());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("foo");
        NodePointer child = nullPropertyPointer.createChild(context, name);
        assertEquals(null, child.getBaseValue());
        assertEquals(null, child.getImmediateNode());
        assertEquals(nullPropertyPointer, child.parent);
        assertEquals("root/foo", child.asPath());
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        QName name = new QName("foo");
        NodePointer child = nullPropertyPointer.createChild(context, name, value);
        assertEquals(value, child.getValuePointer().getBaseValue());
        assertEquals(nullPropertyPointer, child.parent);
        assertEquals("root/foo", child.asPath());
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer(null, new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(value));
    }
}