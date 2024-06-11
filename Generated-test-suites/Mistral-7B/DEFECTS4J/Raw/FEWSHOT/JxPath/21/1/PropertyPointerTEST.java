package org.apache.commons.cli;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl; // Assuming PropertyPointerImpl is a concrete implementation of PropertyPointer
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getBean());
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getPropertyName());
        Assert.assertEquals(0, propertyPointer.getPropertyCount());
        Assert.assertNull(propertyPointer.getPropertyNames());
        Assert.assertFalse(propertyPointer.isActualProperty());
        Assert.assertEquals(false, propertyPointer.isActual());
        Assert.assertNull(propertyPointer.getValue());
        Assert.assertEquals(false, propertyPointer.isCollection());
        Assert.assertEquals(false, propertyPointer.isLeaf());
        Assert.assertEquals(-1, propertyPointer.getLength());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        Assert.assertSame(parentPointer, propertyPointer.getImmediateParentPointer());
        Assert.assertNull(propertyPointer.getNode());
        Assert.assertEquals(UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getPropertyName());
        Assert.assertEquals(0, propertyPointer.getPropertyCount());
        Assert.assertNull(propertyPointer.getPropertyNames());
        Assert.assertFalse(propertyPointer.isActualProperty());
        Assert.assertEquals(false, propertyPointer.isActual());
        Assert.assertEquals(UNINITIALIZED, propertyPointer.getValue());
        Assert.assertEquals(false, propertyPointer.isCollection());
        Assert.assertEquals(false, propertyPointer.isLeaf());
        Assert.assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getBean());

        Object bean = new Object();
        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setBean(bean);
        Assert.assertSame(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        Assert.assertEquals(2, propertyPointer.getPropertyIndex());

        PropertyPointer propertyPointerWithActualProperty = new PropertyPointerImpl(null);
        propertyPointerWithActualProperty.setPropertyIndex(1);
        propertyPointerWithActualProperty.setPropertyName("name");
        Assert.assertEquals("name", propertyPointerWithActualProperty.getPropertyName());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getPropertyName());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        Assert.assertEquals("name", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getPropertyName());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyName("newName");
        Assert.assertEquals("newName", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertEquals(0, propertyPointer.getPropertyCount());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyCount(5);
        Assert.assertEquals(5, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getPropertyNames());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        String[] propertyNames = new String[]{"name1", "name2", "name3"};
        propertyPointer.setPropertyNames(propertyNames);
        Assert.assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertFalse(propertyPointer.isActualProperty());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setActualProperty(true);
        Assert.assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertFalse(propertyPointer.isActual());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setActualProperty(true);
        Assert.assertTrue(propertyPointer.isActual());

        Assert.assertFalse(propertyPointer.isActual());
        Assert.assertFalse(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getImmediateNode());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setValue("value");
        Assert.assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertEquals(false, propertyPointer.isCollection());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setValue(new String[]{"value1", "value2"});
        Assert.assertEquals(true, propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertEquals(false, propertyPointer.isLeaf());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setValue("value");
        Assert.assertEquals(true, propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertEquals(-1, propertyPointer.getLength());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setValue(new String[]{"value1", "value2"});
        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getImmediateValuePointer());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setValue("value");
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        Assert.assertNotNull(immediateValuePointer);
        Assert.assertEquals(immediateValuePointer.getNode(), "value");
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getNode());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        propertyPointer.setValue("value");
        NodePointer nodePointer = propertyPointer.createPath(null);
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(nodePointer.getName(), "name");
        Assert.assertEquals(nodePointer.getNode(), "value");
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getNode());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        NodePointer nodePointer = propertyPointer.createPath(null, "value");
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(nodePointer.getName(), "name");
        Assert.assertEquals(nodePointer.getNode(), "value");
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getNode());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        NodePointer nodePointer = propertyPointer.createChild(null, null, 0, "value");
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(nodePointer.getName(), "value");
        Assert.assertEquals(nodePointer.getNode(), "value");
    }

    @Test
    public void testCreateChildWithName() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getNode());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        NodePointer nodePointer = propertyPointer.createChild(null, new QName("name", "namespace"), 0, "value");
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(nodePointer.getName(), new QName("name", "namespace"));
        Assert.assertEquals(nodePointer.getNode(), "value");
    }

    @Test
    public void testCreateChildWithIndex() {
        PropertyPointer propertyPointer = new PropertyPointerImpl(null);
        Assert.assertNull(propertyPointer.getNode());

        PropertyPointer parentPointer = new PropertyPointerImpl(null);
        propertyPointer = new PropertyPointerImpl(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        NodePointer nodePointer = propertyPointer.createChild(null, null, 1, "value");
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(nodePointer.getName(), "name[1]");
        Assert.assertEquals(nodePointer.getNode(), "value");
    }
}