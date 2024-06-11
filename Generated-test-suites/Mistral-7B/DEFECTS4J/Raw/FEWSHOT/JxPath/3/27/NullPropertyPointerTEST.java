package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNewInstance() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("NullPropertyPointer", nullPropertyPointer.getClass().getName());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getName().getLocalPart());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertEquals("NullPointer", valuePointer.getClass().getName());
        assertEquals(nullPropertyPointer.getName(), valuePointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertThrows(UnsupportedOperationException.class, () -> nullPropertyPointer.setPropertyIndex(1));
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedPath = new NodePointer(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(null);
        assertSame(expectedPath, actualPath);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedPath = new NodePointer(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(null, new Object());
        assertSame(expectedPath, actualPath);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("test");
        NodePointer expectedChild = new NodePointer(new Object());
        NodePointer actualChild = nullPropertyPointer.createChild(null, name);
        assertSame(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("test");
        Object value = new Object();
        NodePointer expectedChild = new NodePointer(value);
        NodePointer actualChild = nullPropertyPointer.createChild(null, name, value);
        assertSame(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildWithIndex() {
        NodePointer parent = new NodePointer(new Object());
        NodePointer child0 = new NodePointer(new Object());
        NodePointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("test");
        NodePointer expectedChild = child0.createChild(null, name);
        NodePointer actualChild = nullPropertyPointer.createChild(null, name, 0);
        assertSame(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildWithValueAndIndex() {
        NodePointer parent = new NodePointer(new Object());
        NodePointer child0 = new NodePointer(new Object());
        NodePointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("test");
        Object value = new Object();
        NodePointer expectedChild = child0.createChild(null, name, value);
        NodePointer actualChild = nullPropertyPointer.createChild(null, name, 0, value);
        assertSame(expectedChild, actualChild);
    }

    @Test
    public void testCreatePathWithParentAsPropertyOwner() {
        NodePointer parent1 = new PropertyOwnerPointer(new Object());
        NodePointer child1 = new NodePointer(new Object());
        NodePointer parent2 = new NodePointer(parent1);
        NodePointer nullPropertyPointer = new NullPropertyPointer(parent2);
        NodePointer expectedPath = child1;
        NodePointer actualPath = nullPropertyPointer.createPath(null);
        assertSame(expectedPath, actualPath);
    }

    @Test
    public void testCreatePathWithValueAndParentAsPropertyOwner() {
        NodePointer parent1 = new PropertyOwnerPointer(new Object());
        NodePointer child1 = new NodePointer(new Object());
        NodePointer parent2 = new NodePointer(parent1);
        NodePointer nullPropertyPointer = new NullPropertyPointer(parent2);
        Object value = new Object();
        NodePointer expectedPath = child1.setPropertyValue(value);
        NodePointer actualPath = nullPropertyPointer.createPath(null, value);
        assertSame(expectedPath, actualPath);
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(value));
    }
}