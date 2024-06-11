package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);

        assertNull(np.getBaseValue());
        assertNull(np.getImmediateNode());
        assertFalse(np.isLeaf());
        assertTrue(np.isContainer());
        assertEquals(new QName("*"), np.getName());
        assertEquals(0, np.getLength());

        assertNull(np.getPropertyName());
        np.setPropertyName("test");
        assertEquals("test", np.getPropertyName());

        assertEquals(0, np.getPropertyCount());
        String[] names = np.getPropertyNames();
        assertEquals(0, names.length);

        assertEquals("", np.asPath());

        NodePointer np1 = np.getValuePointer();
        assertSame(np, np1.getParent());
        assertEquals(np.getName(), np1.getName());
        assertNull(np1.getBaseValue());
        assertNull(np1.getImmediateNode());

        assertFalse(np.isActualProperty());
        assertFalse(np.isActual());

        JXPathContext context = new JXPathContext(new Object());
        NodePointer path = np.createPath(context);
        assertNull(path.getBaseValue());
        assertNull(path.getImmediateNode());
        assertFalse(path.isLeaf());
        assertTrue(path.isContainer());
        assertEquals(np.getName(), path.getName());

        np.setValue("test");
        assertEquals("test", parent.getValuePointer().getValue(context).getValue());

        NodePointer child = np.createChild(context, new QName("child"), 1);
        assertSame(np, child.getParent());
        assertEquals("child", child.getName().getLocalPart());
        assertEquals(1, child.getIndex());
        assertNull(child.getBaseValue());
        assertNull(child.getImmediateNode());
        assertFalse(child.isLeaf());
        assertTrue(child.isContainer());
        assertEquals(np.getName(), child.getParent().getName());

        np.setValue(new String[]{"test1", "test2"});
        Object value = parent.getValuePointer().getValue(context).getValue();
        assertNotNull(value);
        assertTrue(value instanceof String[]);
        assertEquals(2, ((String[]) value).length);
        assertEquals("test1", ((String[]) value)[0]);
        assertEquals("test2", ((String[]) value)[1]);

        NodePointer child2 = np.createChild(context, new QName("child"), 0, "test3");
        assertSame(np, child2.getParent());
        assertEquals("child", child2.getName().getLocalPart());
        assertEquals(0, child2.getIndex());
        assertEquals("test3", child2.getValue(context).getValue());
        assertNull(child2.getImmediateNode());
    }

    @Test
    public void testNullPropertyPointerWithParentPropertyOwner() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer np = new NullPropertyPointer(parent);

        assertNull(np.getBaseValue());
        assertNull(np.getImmediateNode());
        assertFalse(np.isLeaf());
        assertTrue(np.isContainer());
        assertEquals(new QName("*"), np.getName());
        assertEquals(0, np.getLength());

        assertNull(np.getPropertyName());
        np.setPropertyName("test");
        assertEquals("test", np.getPropertyName());

        assertEquals(0, np.getPropertyCount());
        String[] names = np.getPropertyNames();
        assertEquals(0, names.length);

        assertEquals("", np.asPath());

        NodePointer np1 = np.getValuePointer();
        assertSame(np, np1.getParent());
        assertEquals(np.getName(), np1.getName());
        assertNull(np1.getBaseValue());
        assertNull(np1.getImmediateNode());

        assertFalse(np.isActualProperty());
        assertFalse(np.isActual());

        JXPathContext context = new JXPathContext(new Object());
        NodePointer path = np.createPath(context);
        assertNull(path.getBaseValue());
        assertNull(path.getImmediateNode());
        assertFalse(path.isLeaf());
        assertTrue(path.isContainer());
        assertEquals(np.getName(), path.getName());

        np.setValue("test");
        assertEquals("test", parent.getValuePointer().getValue(context).getValue());

        NodePointer child = np.createChild(context, new QName("child"), 1);
        assertSame(np, child.getParent());
        assertEquals("child", child.getName().getLocalPart());
        assertEquals(1, child.getIndex());
        assertNull(child.getBaseValue());
        assertNull(child.getImmediateNode());
        assertFalse(child.isLeaf());
        assertTrue(child.isContainer());
        assertEquals(np.getName(), child.getParent().getName());

        PropertyPointer propertyPointer = parent.getPropertyPointer();
        propertyPointer.setPropertyName("test");
        propertyPointer.setValue("test");

        assertEquals("test", np.getValue(context).getValue());

        NodePointer child2 = np.createChild(context, new QName("child"), 0, "test3");
        assertSame(np, child2.getParent());
        assertEquals("child", child2.getName().getLocalPart());
        assertEquals(0, child2.getIndex());
        assertEquals("test3", child2.getValue(context).getValue());
        assertNull(child2.getImmediateNode());
    }
}