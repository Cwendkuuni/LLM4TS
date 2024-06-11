package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerCreation() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(new QName(""), pointer.getName());
        assertFalse(pointer.isActualProperty());
        assertFalse(pointer.isActual());
        assertTrue(pointer.isContainer());
        assertNull(pointer.getValuePointer());
        assertNull(pointer.getBaseValue());
        assertEquals(0, pointer.getLength());
        assertFalse(pointer.isLeaf());
    }

    @Test
    public void testNullPropertyPointerName() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyName("propertyName");
        assertEquals("propertyName", pointer.getPropertyName().getLocalPart());
    }

    @Test
    public void testNullPropertyPointerIndex() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyIndex(123);
        assertEquals(123, pointer.getPropertyIndex());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCannotSetValueOnNullPropertyPointer() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue(new Object());
    }

    @Test
    public void testNullPropertyPointerCreatePath() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newPath = pointer.createPath(new JXPathContext());
        assertNull(newPath);
    }

    @Test
    public void testNullPropertyPointerCreatePathWithValue() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newPath = pointer.createPath(new JXPathContext(), new Object());
        assertNull(newPath);
    }

    @Test
    public void testNullPropertyPointerCreateChild() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer newChild = pointer.createChild(context, new QName("child"), 123);
        assertNull(newChild);
    }

    @Test
    public void testNullPropertyPointerCreateChildWithValue() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer newChild = pointer.createChild(context, new QName("child"), 123, new Object());
        assertNull(newChild);
    }

    @Test
    public void testNullPropertyPointerCreateChildWithSameName() {
        NodePointer parent = new NullPropertyPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer newChild = pointer.createChild(context, new QName("child"), 123);
        NodePointer newChild2 = pointer.createChild(context, new QName("child"), 123);
        assertSame(newChild, newChild2);
    }

    @Test
    public void testNullPropertyPointerCreateChildWithDifferentNames() {
        NodePointer parent = new NullPropertyPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(new Object());
        NodePointer newChild1 = pointer.createChild(context, new QName("child1"), 123);
        NodePointer newChild2 = pointer.createChild(context, new QName("child2"), 123);
        assertNull(newChild1);
        assertNull(newChild2);
    }

    @Test
    public void testNullPropertyPointerAsPath() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals("", pointer.asPath());
    }

    @Test
    public void testNullPropertyPointerAsPathWithNameAttribute() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("myAttribute");
        assertEquals("[@name='myAttribute']", pointer.asPath());
    }

    @Test
    public void testNullPropertyPointerAsPathWithIndex() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyIndex(123);
        assertEquals("[123]", pointer.asPath());
    }

    @Test
    public void testNullPropertyPointerAsPathWithNameAttributeAndIndex() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setNameAttributeValue("myAttribute");
        pointer.setPropertyIndex(123);
        assertEquals("[@name='myAttribute'][123]", pointer.asPath());
    }

    @Test
    public void testNullPropertyPointerValuePointer() {
        NodePointer parent = new NullPointer(null, new QName(""));
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer valuePointer = pointer.getValuePointer();
        assertInstanceOf(NullPointer.class, valuePointer);
        assertEquals(new QName(""), valuePointer.getName());
    }
}