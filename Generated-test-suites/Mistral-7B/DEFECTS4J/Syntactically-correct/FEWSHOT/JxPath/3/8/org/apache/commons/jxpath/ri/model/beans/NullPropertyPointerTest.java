package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertTrue(nullPropertyPointer.isLeaf());
        assertNull(nullPropertyPointer.getValuePointer());
        assertNull(nullPropertyPointer.createPath(null));
        assertNull(nullPropertyPointer.createPath(null, null));
        assertNull(nullPropertyPointer.createChild(null, null, 0));
        assertNull(nullPropertyPointer.createChild(null, null, 0, null));
    }

    @Test
    public void testNullPropertyPointerWithParent() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parentPointer = new NodePointer(context, new QName("parent"), null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertTrue(nullPropertyPointer.isLeaf());
        assertNull(nullPropertyPointer.getValuePointer());

        NodePointer path = nullPropertyPointer.createPath(context);
        assertNull(path);

        NodePointer child = nullPropertyPointer.createChild(context, new QName("child"), 0);
        assertNull(child);

        child = nullPropertyPointer.createChild(context, new QName("child"), 0, "value");
        assertNull(child);
    }

    @Test
    public void testNullPropertyPointerName() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parentPointer = new NodePointer(context, new QName("parent"), null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        nullPropertyPointer.setPropertyName("property");

        NodePointer path = nullPropertyPointer.createPath(context);
        NodePointer child = path.createChild(context, new QName("property"), 0);
        assertNull(child);

        child = path.createChild(context, new QName("property"), 0, null);
        assertNull(child);

        child = path.createChild(context, new QName("property"), 0, "value");
        assertNull(child);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullPropertyPointerSetValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parentPointer = new NodePointer(context, new QName("parent"), null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parentPointer);
        nullPropertyPointer.setValue(new Object());
    }
}