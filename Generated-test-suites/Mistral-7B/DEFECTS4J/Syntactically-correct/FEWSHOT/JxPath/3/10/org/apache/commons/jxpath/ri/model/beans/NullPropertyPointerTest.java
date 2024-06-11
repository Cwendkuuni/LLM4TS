package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.JXPathContext;
import org.apache.commons.cli.NodePointer;
import org.apache.commons.cli.QName;
import org.apache.commons.cli.PropertyOwnerPointer;
import org.apache.commons.cli.NullPointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getBaseValue());
        assertNull(pointer.getImmediateNode());
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testSetProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.setValue(new Object());
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        NodePointer newParent = context.getDocument();

        try {
            pointer.createPath(context);
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }

        PropertyOwnerPointer pop = new PropertyOwnerPointer(newParent);
        pointer = new NullPropertyPointer(pop);

        NodePointer newParent2 = pointer.createPath(context);
        assertNull(newParent2);
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        NodePointer newParent = context.getDocument();

        try {
            pointer.createPath(context, new Object());
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }

        PropertyOwnerPointer pop = new PropertyOwnerPointer(newParent);
        pointer = new NullPropertyPointer(pop);

        NodePointer newParent2 = pointer.createPath(context, new Object());
        assertNull(newParent2);
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        NodePointer newParent = context.getDocument();
        QName name = new QName("test");

        try {
            pointer.createChild(context, name, 0);
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }

        PropertyOwnerPointer pop = new PropertyOwnerPointer(newParent);
        pointer = new NullPropertyPointer(pop);

        NodePointer newParent2 = pointer.createPath(context);
        NodePointer child = newParent2.createChild(context, name, 0);
        assertNull(child);
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext(null);
        NodePointer newParent = context.getDocument();
        QName name = new QName("test");
        Object value = new Object();

        try {
            pointer.createChild(context, name, 0, value);
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }

        PropertyOwnerPointer pop = new PropertyOwnerPointer(newParent);
        pointer = new NullPropertyPointer(pop);

        NodePointer newParent2 = pointer.createPath(context);
        NodePointer child = newParent2.createChild(context, name, 0, value);
        assertNull(child);
    }
}