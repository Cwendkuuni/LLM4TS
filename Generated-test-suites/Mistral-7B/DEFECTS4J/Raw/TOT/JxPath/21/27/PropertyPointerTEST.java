package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerCreation() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertNull(propertyPointer.getPropertyName());
        assertNull(propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertEquals(-1, propertyPointer.getIndex());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerWithNameAndBean() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", parent);
        assertNotNull(propertyPointer.getPropertyName());
        assertNull(propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertEquals(-1, propertyPointer.getIndex());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertTrue(propertyPointer.isActualProperty());
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testPropertyPointerWithNameBeanAndIndex() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, parent);
        assertNotNull(propertyPointer.getPropertyName());
        assertNull(propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertEquals(0, propertyPointer.getIndex());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertTrue(propertyPointer.isActualProperty());
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyPointerWithNullName() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(null, parent);
    }

    @Test
    public void testPropertyPointerWithBean() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(bean, parent);
        assertEquals(bean, propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertNull(propertyPointer.getPropertyName());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerWithBeanAndName() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", bean, parent);
        assertEquals(bean, propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertNotNull(propertyPointer.getPropertyName());
        assertEquals("testProperty", propertyPointer.getPropertyName());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerWithBeanNameAndIndex() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        assertEquals(bean, propertyPointer.getBean());
        assertNull(propertyPointer.getNode());
        assertNotNull(propertyPointer.getPropertyName());
        assertEquals("testProperty", propertyPointer.getPropertyName());
        assertEquals(0, propertyPointer.getIndex());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getPropertyIndex());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerGetValue() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        propertyPointer.setValue(new Object());
        assertEquals(new Object(), propertyPointer.getValue());
    }

    @Test
    public void testPropertyPointerGetImmediateNode() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        propertyPointer.setValue(new Object());
        assertEquals(new Object(), propertyPointer.getImmediateNode());
    }

    @Test
    public void testPropertyPointerIsCollection() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        propertyPointer.setValue(new Object[5]);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testPropertyPointerIsLeaf() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        propertyPointer.setValue(new String("test"));
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testPropertyPointerLength() {
        Object bean = new Object[5];
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        propertyPointer.setValue(bean);
        assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testPropertyPointerCreatePath() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        JXPathContext context = new JXPathContext();
        NodePointer result = propertyPointer.createPath(context);
        assertEquals(propertyPointer, result);
    }

    @Test
    public void testPropertyPointerCreatePathWithValue() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        JXPathContext context = new JXPathContext();
        propertyPointer.setValue("test");
        NodePointer result = propertyPointer.createPath(context, "test");
        assertEquals(propertyPointer, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPropertyPointerCreatePathWithInvalidIndex() {
        Object bean = new Object[5];
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        JXPathContext context = new JXPathContext();
        propertyPointer.setValue(bean);
        propertyPointer.createPath(context, 5);
    }

    @Test
    public void testPropertyPointerClone() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer("testProperty", 0, bean, parent);
        PropertyPointer cloned = propertyPointer.clone();
        assertEquals(propertyPointer, cloned);
        assertNotSame(propertyPointer, cloned);
    }

    @Test
    public void testPropertyPointerCompareChildNodePointers() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1", 0, bean, parent);
        PropertyPointer propertyPointer2 = new PropertyPointer("testProperty2", 0, bean, parent);
        assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2) == 0);
        assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1) > 0);
        assertTrue(propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1) < 0);
    }
}