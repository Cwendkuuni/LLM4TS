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
 public boolean isActualProperty() {
 return true;
 }
 }

 abstract static class TestCollectionPropertyPointer extends PropertyPointer {
 @Override
 public int getLength() {
 throw new UnsupportedOperationException();
 }
 }

 @Test
 public void testPropertyIndex() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setPropertyIndex(1);
 assertEquals(1, pointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 assertEquals(pointer.getBean(), pointer.getBean());
 }

 @Test
 public void testGetName() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setPropertyName("testProperty");
 assertEquals(new QName("testProperty"), pointer.getName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyNameUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getPropertyName();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyNameUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setPropertyName("testProperty");
 }

 @Test
 public void testGetPropertyCount() {
 TestCollectionPropertyPointer pointer = new TestCollectionPropertyPointer(null);
 pointer.setPropertyCount(3);
 assertEquals(3, pointer.getPropertyCount());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyCountUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getPropertyCount();
 }

 @Test
 public void testGetPropertyNames() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 String[] names = new String[]{"name1", "name2"};
 pointer.setPropertyNames(names);
 assertArrayEquals(names, pointer.getPropertyNames());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyNamesUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getPropertyNames();
 }

 @Test
 public void testIsActualProperty() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 assertTrue(pointer.isActual());
 }

 @Test
 public void testIsActualPropertyFalse() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 protected boolean isActualProperty() {
 return false;
 }
 };
 assertFalse(pointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setValue(new Object());
 assertEquals(pointer.getValue(), pointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 TestCollectionPropertyPointer pointer = new TestCollectionPropertyPointer(null) {
 @Override
 public int getLength() {
 return 3;
 }
 };
 assertTrue(pointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 public boolean isActualProperty() {
 return false;
 }
 };
 assertTrue(pointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 TestCollectionPropertyPointer pointer = new TestCollectionPropertyPointer(null) {
 @Override
 public int getLength() {
 return 3;
 }
 };
 assertEquals(3, pointer.getLength());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLengthUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getLength();
 }

 @Test
 public void testGetImmediateValuePointer() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 public Object getImmediateNode() {
 return new Object();
 }
 };
 NodePointer valuePointer = pointer.getImmediateValuePointer();
 assertEquals(pointer, valuePointer.getParent());
 assertEquals(pointer.getName(), valuePointer.getName());
 assertEquals(pointer.getImmediateNode(), valuePointer.getNode());
 }

 @Test
 public void testCreatePath() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object bean = new Object();
 pointer.setBean(bean);
 pointer.setPropertyName("testProperty");
 NodePointer result = pointer.createPath(context);
 assertEquals(pointer, result);
 }

 @Test
 public void testCreatePathWithValue() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 public int getLength() {
 return 3;
 }
 };
 JXPathContext context = new JXPathContext();
 Object value = new Object();
 NodePointer result = pointer.createPath(context, value);
 assertEquals(pointer, result);
 }

 @Test
 public void testCreateChild() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 NodePointer child = pointer.createChild(context, null, 0, null);
 assertEquals(pointer, child.getParent());
 assertEquals(pointer.getName(), child.getName());
 }

 @Test
 public void testCreateChildWithName() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 NodePointer child = pointer.createChild(context, new QName("testChild"), 0, null);
 assertEquals(pointer, child.getParent());
 assertEquals(new QName("testChild"), child.getName());
 }

 @Test
 public void testCreateChildWithNameAndValue() {
 TestPropertyPointer pointer = new TestPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 NodePointer child = pointer.createChild(context, new QName("testChild"), 0, new Object());
 assertEquals(pointer, child.getParent());
 assertEquals(new QName("testChild"), child.getName());
 }
}