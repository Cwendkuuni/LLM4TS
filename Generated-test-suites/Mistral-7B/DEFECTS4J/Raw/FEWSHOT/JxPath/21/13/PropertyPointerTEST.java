package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyPointerTest {

 abstract static class TestPropertyPointer extends PropertyPointer {
 @Override
 public String getPropertyName() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setPropertyName(String propertyName) {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getPropertyCount() {
 throw new UnsupportedOperationException();
 }

 @Override
 public String[] getPropertyNames() {
 throw new UnsupportedOperationException();
 }

 @Override
 protected boolean isActualProperty() {
 return true;
 }
 }

 @Test
 public void testGetPropertyIndex() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertEquals(-1, pointer.getPropertyIndex());
 pointer.setPropertyIndex(1);
 assertEquals(1, pointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertNull(pointer.getBean());
 pointer.setBean(new Object());
 assertEquals(pointer.getBean(), pointer.getBean());
 }

 @Test
 public void testGetName() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertNull(pointer.getName());
 String propertyName = "testProperty";
 pointer.setPropertyName(propertyName);
 assertEquals(new QName(null, propertyName), pointer.getName());
 }

 @Test
 public void testIsActual() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertTrue(pointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertNull(pointer.getImmediateNode());
 pointer.setPropertyIndex(1);
 Object expected = new Object();
 pointer.setValue(expected);
 assertEquals(expected, pointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertFalse(pointer.isCollection());
 pointer.setPropertyIndex(0);
 Object collection = new Object[] { 1, 2, 3 };
 pointer.setValue(collection);
 assertTrue(pointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertTrue(pointer.isLeaf());
 pointer.setPropertyIndex(1);
 Object value = new Object();
 pointer.setValue(value);
 assertFalse(pointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setValue(new Object[0]);
 assertEquals(-1, pointer.getLength());
 pointer.setValue(new Object[] { 1, 2, 3 });
 assertEquals(3, pointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 Object value = new Object();
 pointer.setPropertyIndex(0);
 pointer.setValue(value);
 NodePointer expected = NodePointer.newChildNodePointer(
 pointer, getName(), value);
 NodePointer actual = pointer.getImmediateValuePointer();
 assertEquals(expected, actual);
 }

 @Test
 public void testCreatePath() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 Object parent = new Object();
 pointer.setBean(parent);
 pointer.setPropertyName("testProperty");
 pointer.setPropertyIndex(0);
 JXPathContext context = null;
 NodePointer result = pointer.createPath(context);
 assertNotNull(result);
 assertEquals(pointer, result);
 }

 @Test
 public void testCreatePathWithValue() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 Object parent = new Object();
 pointer.setBean(parent);
 pointer.setPropertyName("testProperty");
 pointer.setPropertyIndex(0);
 JXPathContext context = null;
 Object value = new Object();
 NodePointer result = pointer.createPath(context, value);
 assertNotNull(result);
 assertEquals(pointer, result);
 assertEquals(value, result.getNode());
 }

 @Test
 public void testCreateChild() {
 TestPropertyPointer parentPointer = new TestPropertyPointer(null);
 TestPropertyPointer childPointer1 = new TestPropertyPointer(parentPointer);
 childPointer1.setPropertyName("child1");
 childPointer1.setPropertyIndex(0);
 TestPropertyPointer childPointer2 = new TestPropertyPointer(parentPointer);
 childPointer2.setPropertyName("child2");
 childPointer2.setPropertyIndex(1);
 NodePointer result1 = childPointer1.createChild(null, null, 0, null);
 NodePointer result2 = childPointer1.createChild(null, null, 1, null);
 NodePointer expected1 = NodePointer.newChildNodePointer(childPointer1, null, null);
 NodePointer expected2 = NodePointer.newChildNodePointer(childPointer1, null, null);
 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 }

 @Test
 public void testCreateChildWithValue() {
 TestPropertyPointer parentPointer = new TestPropertyPointer(null);
 TestPropertyPointer childPointer1 = new TestPropertyPointer(parentPointer);
 childPointer1.setPropertyName("child1");
 childPointer1.setPropertyIndex(0);
 TestPropertyPointer childPointer2 = new TestPropertyPointer(parentPointer);
 childPointer2.setPropertyName("child2");
 childPointer2.setPropertyIndex(1);
 Object value1 = new Object();
 Object value2 = new Object();
 NodePointer result1 = childPointer1.createChild(null, null, 0, value1);
 NodePointer result2 = childPointer1.createChild(null, null, 1, value2);
 NodePointer expected1 = NodePointer.newChildNodePointer(childPointer1, null, value1);
 NodePointer expected2 = NodePointer.newChildNodePointer(childPointer1, null, value2);
 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 }

}