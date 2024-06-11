package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private Object bean;

 @Before
 public void setUp() {
 bean = new DummyBean();
 propertyPointer = new DummyPropertyPointer(new DummyParentPointer(bean));
 }

 @Test
 public void testGetPropertyIndex() {
 assertEquals(-1, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 assertEquals("dummyProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 assertEquals("dummyProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 assertEquals(1, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = propertyPointer.getPropertyNames();
 assertEquals(1, propertyNames.length);
 assertEquals("dummyProperty", propertyNames[0]);
 }

 @Test
 public void testIsActualProperty() {
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 JXPathContext context = new JXPathContext();
 Object dummyValue = new DummyValue("testValue");
 propertyPointer.createPath(context, dummyValue);
 assertEquals(dummyValue, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 // This test case assumes DummyBean has a getDummyProperty() method that returns an array
 Object dummyArray = propertyPointer.getBaseValue();
 int length = propertyPointer.getLength();
 assertEquals(dummyArray.length, length);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyIndexWhenPropertyIsLeaf() {
 propertyPointer.setPropertyIndex(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetImmediateNodeWithNegativeIndex() {
 propertyPointer.setIndex(-1);
 propertyPointer.getImmediateNode();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreatePathWithNegativeIndex() {
 propertyPointer.setIndex(-1);
 propertyPointer.createPath(new JXPathContext(), new DummyValue("testValue"));
 }

 @Test(expected = NullPointerException.class)
 public void testCreatePathWithNullContext() {
 propertyPointer.createPath(null, new DummyValue("testValue"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullValue() {
 propertyPointer.createPath(new JXPathContext(), null);
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 Object dummyValue = new DummyValue("testValue");
 propertyPointer.createPath(context, dummyValue);
 assertEquals(dummyValue, propertyPointer.getNode());
 }

 @Test
 public void testCreateChildNodePointer() {
 JXPathContext context = new JXPathContext();
 String newPropertyName = "newProperty";
 NodePointer newChildPointer = propertyPointer.createChild(context, new QName("", newPropertyName), 0);
 assertEquals(newPropertyName, newChildPointer.getName().getLocalPart());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildNodePointerWithNullContext() {
 propertyPointer.createChild(null, new QName("", "newProperty"), 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildNodePointerWithNullName() {
 propertyPointer.createChild(new JXPathContext(), null, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreateChildNodePointerWithInvalidIndex() {
 propertyPointer.createChild(new JXPathContext(), new QName("", "newProperty"), 2);
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyParentPointer(bean));
 PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyParentPointer(bean));
 assertEquals(propertyPointer.hashCode(), propertyPointer1.hashCode());
 PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyParentPointer(new Object()));
 assertNotEquals(propertyPointer.hashCode(), propertyPointer3.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new DummyPropertyPointer(new DummyParentPointer(bean));
 PropertyPointer propertyPointer2 = new DummyPropertyPointer(new DummyParentPointer(bean));
 assertTrue(propertyPointer.equals(propertyPointer1));
 PropertyPointer propertyPointer3 = new DummyPropertyPointer(new DummyParentPointer(new Object()));
 assertFalse(propertyPointer.equals(propertyPointer3));
 PropertyPointer propertyPointer4 = new DummyPropertyPointer(new DummyParentPointer(bean), 1);
 assertFalse(propertyPointer.equals(propertyPointer4));
 PropertyPointer propertyPointer5 = new DummyPropertyPointer(new DummyParentPointer(bean), 0);
 assertTrue(propertyPointer.equals(propertyPointer5));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = new DummyPropertyPointer(new DummyParentPointer(bean), 0);
 NodePointer nodePointer2 = new DummyPropertyPointer(new DummyParentPointer(bean), 0);
 assertEquals(0, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2));
 NodePointer nodePointer3 = new DummyPropertyPointer(new DummyParentPointer(bean), 1);
 assertTrue(propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3) < 0);
 assertTrue(propertyPointer.compareChildNodePointers(nodePointer3, nodePointer1) > 0);
 }

}