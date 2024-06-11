package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testCreatePathNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathPropertyOwnerPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new DOMNodePointer(null));
        NodePointer nodePointer = propertyOwnerPointer.createChild(null, new QName("test"), 0);
        NodePointer newParent = nullPropertyPointer.createPath(null);
        assertSame(propertyOwnerPointer, newParent);
        PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
        NodePointer child = propertyPointer.createChild(null, new QName("test"), 0);
        assertSame(nodePointer, child);
    }

    @Test
    public void testCreatePathNullPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NullPointer(null, new QName("test")));
        NodePointer newParent = nullPropertyPointer.createPath(null);
        assertNull(newParent);
    }

    @Test
    public void testSetValueNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue("test"));
    }

    @Test
    public void testSetValuePropertyOwnerPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new DOMNodePointer(null));
        NodePointer nodePointer = propertyOwnerPointer.createChild(null, new QName("test"), 0);
        PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
        propertyPointer.setValue("test");
        nullPropertyPointer.setValue("test");
        assertEquals("test", nodePointer.getValue());
    }

    @Test
    public void testSetValueNullPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NullPointer(null, new QName("test")));
        nullPropertyPointer.setValue("test");
        assertNull(nullPropertyPointer.getValuePointer().getValue());
    }

    @Test
    public void testCreatePathContextNullValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = nullPropertyPointer.createPath(new JXPathContext(null));
        assertNull(newParent);
    }

    @Test
    public void testCreatePathContextValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = nullPropertyPointer.createPath(new JXPathContext(null), "test");
        assertNull(newParent);
    }

    @Test
    public void testCreatePathContextValueAndIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = nullPropertyPointer.createPath(new JXPathContext(null), "test", 0);
        assertNull(newParent);
    }

    @Test
    public void testCreateChildContextNullValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newChild = nullPropertyPointer.createChild(new JXPathContext(null), new QName("test"), 0);
        assertNull(newChild);
    }

    @Test
    public void testCreateChildContextValueAndIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = new JXPathContext(null).createPath();
        NodePointer newChild = nullPropertyPointer.createChild(new JXPathContext(null), new QName("test"), 0, "test");
        assertNull(newChild);
    }

    @Test
    public void testCreateChildContextValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = new JXPathContext(null).createPath();
        NodePointer newChild = nullPropertyPointer.createChild(new JXPathContext(null), new QName("test"), 0);
        assertNull(newChild);
    }

    @Test
    public void testCreateChildContextValueAndIndexNullValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = new JXPathContext(null).createPath();
        NodePointer newChild = nullPropertyPointer.createChild(new JXPathContext(null), new QName("test"), 0, null);
        assertNull(newChild);
    }

    @Test
    public void testCreateChildContextValueIndexGreaterThanZero() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer newParent = new JXPathContext(null).createPath();
        NodePointer newChild = nullPropertyPointer.createChild(new JXPathContext(null), new QName("test"), 1, "test");
        assertNull(newChild);
    }
}