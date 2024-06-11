package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl;
import org.apache.commons.jxpath.util.ValueUtils;

public class PropertyPointerTest {

 private static final Object UNINITIALIZED = new Object();
 private static final int PROPERTY_COUNT = 3;
 private static final String[] PROPERTY_NAMES = {"property1", "property2", "property3"};

 @Test
 public void testPropertyPointer() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null);
 assertNull(propertyPointer.getPropertyName());
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 assertEquals(-1, propertyPointer.getLength());
 assertFalse(propertyPointer.isActual());
 assertFalse(propertyPointer.isCollection());
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerWithPropertyNameAndBean() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 assertEquals("propertyName", propertyPointer.getPropertyName());
 assertEquals(new Object(), propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 assertEquals(-1, propertyPointer.getLength());
 assertFalse(propertyPointer.isActual());
 assertFalse(propertyPointer.isCollection());
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerWithPropertyNameAndValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertEquals("propertyName", propertyPointer.getPropertyName());
 assertEquals(new Object(), propertyPointer.getBean());
 assertEquals(ValueUtils.createList("value1", "value2", "value3"), propertyPointer.getValue());
 assertEquals(PROPERTY_COUNT, propertyPointer.getLength());
 assertFalse(propertyPointer.isActual());
 assertFalse(propertyPointer.isCollection());
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerWithPropertyIndexAndBean() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object());
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 assertEquals(new Object(), propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 assertEquals(-1, propertyPointer.getLength());
 assertFalse(propertyPointer.isActual());
 assertFalse(propertyPointer.isCollection());
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerWithPropertyIndexAndValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertEquals(0, propertyPointer.getPropertyIndex());
 assertEquals(new Object(), propertyPointer.getBean());
 assertEquals("value1", propertyPointer.getValue());
 assertEquals(1, propertyPointer.getLength());
 assertFalse(propertyPointer.isActual());
 assertFalse(propertyPointer.isCollection());
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 propertyPointer.setPropertyIndex(2);
 assertEquals(2, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 propertyPointer.setPropertyIndex(2);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexNegativeValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 propertyPointer.setPropertyIndex(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexHigherThanPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 propertyPointer.setPropertyIndex(4);
 }

 @Test
 public void testGetBean() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 Object bean = new Object();
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualPropertyWithValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualPropertyWithIndex() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertFalse(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActualWithValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActualWithIndex() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertFalse(propertyPointer.isActual());
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 assertEquals(new QName(null, "propertyName"), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("property1", "property2", "property3"));
 assertEquals("property2", propertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 propertyPointer.setPropertyName("newPropertyName");
 assertEquals("newPropertyName", propertyPointer.getPropertyName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyNameNull() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertEquals(PROPERTY_COUNT, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("property1", "property2", "property3"));
 assertEquals(PROPERTY_NAMES, propertyPointer.getPropertyNames());
 }

 @Test
 public void testClone() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 PropertyPointer clone = propertyPointer.clone();
 assertEquals(propertyPointer, clone);
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertEquals(ValueUtils.getValue(ValueUtils.getList("value1", "value2", "value3"), 0), propertyPointer.getImmediateNode());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 NodePointer childNodePointer = new PropertyPointerImpl("childParentPointer", "childPropertyName", propertyPointer.getImmediateNode());
 assertEquals(propertyPointer, propertyPointer.getImmediateValuePointer().getParentPointer());
 assertEquals("childPropertyName", propertyPointer.getImmediateValuePointer().getPropertyName());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 JXPathContext context = null;
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals(propertyPointer, nodePointer);
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 Object value = new Object();
 JXPathContext context = null;
 NodePointer nodePointer = propertyPointer.createPath(context, value);
 assertEquals(propertyPointer, nodePointer);
 assertEquals(value, propertyPointer.getValue());
 }

 @Test
 public void testCreateChild() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 QName name = new QName("namespaceURI", "localPart");
 NodePointer childNodePointer = propertyPointer.createChild(new JXPathContext(), name, 0, new Object());
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals(name, childNodePointer.getName());
 }

 @Test
 public void testCreateChildWithName() {
 PropertyPointer propertyPointer = new PropertyPointerImpl("parentPointer", "propertyName", new Object());
 QName name = new QName("namespaceURI", "localPart");
 NodePointer childNodePointer = propertyPointer.createChild(new JXPathContext(), name);
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals(name, childNodePointer.getName());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointerImpl("parentPointer1", "propertyName1", new Object());
 PropertyPointer propertyPointer2 = new PropertyPointerImpl("parentPointer2", "propertyName2", new Object());
 PropertyPointer propertyPointer3 = new PropertyPointerImpl("parentPointer1", "propertyName1", new Object());
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointerImpl("parentPointer1", "propertyName1", new Object());
 PropertyPointer propertyPointer2 = new PropertyPointerImpl("parentPointer2", "propertyName2", new Object());
 PropertyPointer propertyPointer3 = new PropertyPointerImpl("parentPointer1", "propertyName1", new Object());
 PropertyPointer propertyPointer4 = new PropertyPointerImpl("parentPointer1", "propertyName1", new Object(), ValueUtils.createList("value1", "value2", "value3"));
 PropertyPointer propertyPointer5 = new PropertyPointerImpl("parentPointer1", "propertyName1", new Object(), ValueUtils.createList("value1", new Object(), "value3"));
 assertEquals(propertyPointer1, propertyPointer3);
 assertFalse(propertyPointer1.equals(propertyPointer2));
 assertFalse(propertyPointer1.equals(propertyPointer4));
 assertFalse(propertyPointer1.equals(propertyPointer5));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new PropertyPointerImpl("parentPointer1", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 PropertyPointer propertyPointer2 = new PropertyPointerImpl("parentPointer1", 1, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 PropertyPointer propertyPointer3 = new PropertyPointerImpl("parentPointer1", 0, new Object(), ValueUtils.createList("value1", "value2", "value3"));
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer3));
 assertEquals(-1, propertyPointer3.compareChildNodePointers(propertyPointer1, propertyPointer3));
 assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer3));
 }

}