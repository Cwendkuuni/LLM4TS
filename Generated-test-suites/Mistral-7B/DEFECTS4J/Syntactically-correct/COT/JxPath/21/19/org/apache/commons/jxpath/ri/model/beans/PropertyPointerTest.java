package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstructor() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Assert.assertNull(propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        Assert.assertEquals(2, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexNegativeNumber() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testSetBean() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
        propertyPointer.setBean(new Object());
        Assert.assertNotSame(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName(null, "testProperty");
        Assert.assertEquals(expectedName, propertyPointer.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetName() {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyName("testProperty2");
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyCount(3);
        Assert.assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyCount() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyCount(3);
        propertyPointer.setPropertyCount(4);
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String[] expectedPropertyNames = {"testProperty1", "testProperty2"};
        propertyPointer.setPropertyNames(expectedPropertyNames);
        Assert.assertArrayEquals(expectedPropertyNames, propertyPointer.getPropertyNames());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyNames() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String[] expectedPropertyNames = {"testProperty1", "testProperty2"};
        propertyPointer.setPropertyNames(expectedPropertyNames);
        propertyPointer.setPropertyNames(new String[]{"testProperty3"});
    }

    @Test
    public void testIsActualProperty() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object expectedValue = new Object();
        propertyPointer.setValue(expectedValue);
        Object actualValue = propertyPointer.getImmediateNode();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsCollection() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object collection = new Object[3];
        propertyPointer.setValue(collection);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object leaf = new Object();
        propertyPointer.setValue(leaf);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object collection = new Object[3];
        propertyPointer.setValue(collection);
        int expectedLength = 3;
        Assert.assertEquals(expectedLength, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setValue(bean);
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertEquals(propertyPointer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathWithInvalidContext() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = null;
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreatePathWithNullBean() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        propertyPointer.setValue(null);
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertNull(result);
    }

    @Test
    public void testCreatePathWithValue() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        propertyPointer.setValue(value);
        NodePointer result = propertyPointer.createPath(context, value);
        Assert.assertEquals(propertyPointer, result);
    }

    @Test
    public void testCreateChild() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        String propertyName = "testProperty";
        int index = 1;
        Object value = new Object();
        NodePointer childPointer = propertyPointer.createChild(context, new QName(null, propertyName), index, value);
        PropertyPointer expectedChildPointer = new PropertyPointer(propertyPointer);
        expectedChildPointer.setPropertyName(propertyName);
        expectedChildPointer.setIndex(index);
        expectedChildPointer.setValue(value);
        Assert.assertEquals(expectedChildPointer, childPointer);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateChildWithNullContext() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        int index = 1;
        Object value = new Object();
        propertyPointer.createChild(null, new QName(null, propertyName), index, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNullPropertyName() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object value = new Object();
        propertyPointer.createChild(new JXPathContext(), null, 1, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithIndexOutOfBounds() throws Exception {
        NodePointer parent = NodePointer.newChildNodePointer(null, null, 0);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        int index = 5;
        Object value = new Object();
        propertyPointer.createChild(new JXPathContext(), new QName(null, propertyName), index, value);
    }
}