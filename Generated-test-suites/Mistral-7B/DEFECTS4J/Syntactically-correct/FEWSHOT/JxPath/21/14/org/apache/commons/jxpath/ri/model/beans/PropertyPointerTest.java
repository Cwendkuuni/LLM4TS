package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl;
import org.apache.commons.jxpath.util.ValueUtils;

public class PropertyPointerTest {

    @Test
    public void testCreateAndGetPropertyPointer() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        assertEquals("propertyName", propertyPointer.getPropertyName());
        assertEquals("root", propertyPointer.getImmediateParentPointer().getNode().toString());
        assertNull(propertyPointer.getValue());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertEquals(Integer.MIN_VALUE, propertyPointer.getPropertyIndex());
        assertEquals("propertyName", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testSetAndGetPropertyIndex() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetAndGetBean() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        assertEquals("propertyName", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        assertEquals(-1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object value = new Object();
        propertyPointer.setValue(value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object value = new Object[0];
        propertyPointer.setValue(value);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        String value = "value";
        propertyPointer.setValue(value);
        assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object value = new Object[3];
        propertyPointer.setValue(value);
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object value = new Object();
        propertyPointer.setValue(value);
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(propertyPointer.getName(), immediateValuePointer.getName());
        assertEquals(value, immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "propertyName";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        Object value = new Object();
        NodePointer expectedNodePointer = NodePointer.newChildNodePointer(
                propertyPointer, null, propertyName, value);
        NodePointer actualNodePointer = propertyPointer.createPath(null, value);
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testCreatePathWithCollection() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "propertyName";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        Object[] values = new Object[2];
        NodePointer expectedNodePointer = NodePointer.newChildNodePointer(
                propertyPointer, null, propertyName, values);
        NodePointer actualNodePointer = propertyPointer.createPath(null, values);
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testClone() {
        NodePointer parentNodePointer = NodePointer.newChildNodePointer(
                null, null, "root", null);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parentNodePointer, "propertyName");
        PropertyPointer clone = propertyPointer.clone();
        assertEquals(propertyPointer.getName(), clone.getName());
        assertEquals(propertyPointer.getPropertyName(), clone.getPropertyName());
        assertEquals(propertyPointer.getPropertyIndex(), clone.getPropertyIndex());
        assertSame(propertyPointer.getParentPointer(), clone.getParentPointer());
        assertNull(propertyPointer.getValue());
        assertNull(clone.getValue());
    }

    @Test
    public void testHashCode() {
        NodePointer parentNodePointer1 = NodePointer.newChildNodePointer(
                null, null, "root1", null);
        NodePointer parentNodePointer2 = NodePointer.newChildNodePointer(
                null, null, "root2", null);
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(parentNodePointer1, "propertyName1");
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(parentNodePointer2, "propertyName2");
        PropertyPointer propertyPointer3 = new PropertyPointerImpl(parentNodePointer1, "propertyName1");
        assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        int expectedHashCode = parentNodePointer1.hashCode() + propertyPointer1.getPropertyIndex();
        int actualHashCode = propertyPointer1.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEquals() {
        NodePointer parentNodePointer1 = NodePointer.newChildNodePointer(
                null, null, "root1", null);
        NodePointer parentNodePointer2 = NodePointer.newChildNodePointer(
                null, null, "root2", null);
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(parentNodePointer1, "propertyName1");
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(parentNodePointer1, "propertyName2");
        PropertyPointer propertyPointer3 = new PropertyPointerImpl(parentNodePointer2, "propertyName1");
        PropertyPointer propertyPointer4 = new PropertyPointerImpl(parentNodePointer1, "propertyName1");
        PropertyPointer propertyPointer5 = new PropertyPointerImpl(parentNodePointer1, "propertyName2");
        PropertyPointer propertyPointer6 = new PropertyPointerImpl(parentNodePointer1, "propertyName1");
        PropertyPointer propertyPointer7 = new PropertyPointerImpl(parentNodePointer1, "propertyName3");
        PropertyPointer propertyPointer8 = new PropertyPointerImpl(parentNodePointer1, null);
        assertFalse(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(propertyPointer3));
        assertFalse(propertyPointer1.equals(propertyPointer4));
        assertFalse(propertyPointer1.equals(propertyPointer5));
        assertFalse(propertyPointer1.equals(propertyPointer6));
        assertFalse(propertyPointer1.equals(propertyPointer7));
        assertFalse(propertyPointer1.equals(propertyPointer8));
        assertFalse(propertyPointer1.equals(null));
        assertFalse(propertyPointer1.equals(""));
        assertTrue(propertyPointer1.equals(propertyPointer6));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer parentNodePointer1 = NodePointer.newChildNodePointer(
                null, null, "root1", null);
        NodePointer parentNodePointer2 = NodePointer.newChildNodePointer(
                null, null, "root2", null);
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(parentNodePointer1, "propertyName1");
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(parentNodePointer2, "propertyName2");
        NodePointer valuePointer1 = NodePointer.newChildNodePointer(
                propertyPointer1, null, "value1", new Object());
        NodePointer valuePointer2 = NodePointer.newChildNodePointer(
                propertyPointer1, null, "value2", new Object());
        int expectedResult = valuePointer1.compareChildNodePointers(valuePointer2, valuePointer2);
        int actualResult = propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer2);
        assertEquals(expectedResult, actualResult);
    }
}