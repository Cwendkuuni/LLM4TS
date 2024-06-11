package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class NullPropertyPointerTest {

    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isContainer());
    }

    @Test
    public void testIsNullPropertyPointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getBaseValue());
        assertNull(pointer.getImmediateNode());
        assertEquals("", pointer.asPath());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        QName qname = pointer.getName();
        assertEquals("*", qname.getNamespace());
        assertEquals("*", qname.getLocalPart());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("foo");
        QName qname = pointer.getName();
        assertEquals("foo", qname.getLocalPart());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("name='foo'");
        String expectedPath = "[@name='foo']";
        assertEquals(expectedPath, pointer.asPath());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActual());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNotContainerParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(new NodePointer(new Object()));
        pointer.setValue(new Object());
    }

    @Test
    public void testSetValue() {
        JXPathContext context = new TestJXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(context.createNode("root"));
        pointer.setValue(new Object());
        NodePointer nodePointer = pointer.getValuePointer();
        assertEquals(nodePointer.getClass(), NullPointer.class);
        assertEquals("root/" + pointer.asPath(), nodePointer.asPath());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new TestJXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(context.createNode("root"));
        NodePointer nodePointer = pointer.createPath(context);
        assertEquals(nodePointer.getClass(), PropertyOwnerPointer.class);
        assertEquals("root", nodePointer.asPath());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new TestJXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(context.createNode("root"));
        NodePointer nodePointer = pointer.createPath(context, new Object());
        assertEquals(nodePointer.getClass(), PropertyPointer.class);
        assertEquals("root/" + pointer.asPath(), nodePointer.asPath());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new TestJXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(context.createNode("root"));
        NodePointer childNodePointer = pointer.createChild(context, new QName("foo"), 1);
        NodePointer parentNodePointer = childNodePointer.getParent();
        assertEquals(parentNodePointer.getClass(), PropertyOwnerPointer.class);
        assertEquals("root/foo", childNodePointer.asPath());
        assertEquals("root", parentNodePointer.asPath());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new TestJXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(context.createNode("root"));
        NodePointer childNodePointer = pointer.createChild(context, new QName("foo"), 1, new Object());
        NodePointer parentNodePointer = childNodePointer.getParent();
        assertEquals(parentNodePointer.getClass(), PropertyOwnerPointer.class);
        assertEquals("root/foo[1]", childNodePointer.asPath());
        assertEquals("root", parentNodePointer.asPath());
    }
}

class TestJXPathContext extends JXPathContext {
    @Override
    public NodePointer createNode(Object node) {
        return new PropertyOwnerPointer(null, new NodePointer(node), null);
    }
}