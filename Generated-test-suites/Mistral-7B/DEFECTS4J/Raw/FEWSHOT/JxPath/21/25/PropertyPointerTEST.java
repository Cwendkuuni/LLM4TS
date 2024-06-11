package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyPointerTest {

 abstract class TestPropertyPointer extends PropertyPointer {
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

 @Test
 public void testGetImmediateNode() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 pointer.setIndex(0);
 assertEquals(pointer.getBean(), pointer.getImmediateNode());
}

 @Test
 public void testIsCollection() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object()[3]);
 assertTrue(pointer.isCollection());

 pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 assertFalse(pointer.isCollection());
}

 @Test
 public void testIsLeaf() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new String());
 assertTrue(pointer.isLeaf());

 pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 assertFalse(pointer.isLeaf());
}

 @Test
 public void testGetLength() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object[3]);
 assertEquals(3, pointer.getLength());
}

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyNameUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getPropertyName();
}

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyNameUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setPropertyName("name");
}

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyCountUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getPropertyCount();
}

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyNamesUnsupported() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.getPropertyNames();
}

 @Test
 public void testCreatePath() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 pointer.setIndex(0);

 JXPathContext context = JXPathContext.newContext(pointer.getNode());
 NodePointer result = pointer.createPath(context);
 assertEquals(pointer.getBean(), result.getNode());
}

 @Test
 public void testCreatePathWithValue() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 pointer.setIndex(0);

 JXPathContext context = JXPathContext.newContext(pointer.getNode());
 Object value = new Object();
 NodePointer result = pointer.createPath(context, value);
 assertEquals(value, result.getNode());
}

 @Test
 public void testCreateChild() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 pointer.setIndex(0);
 pointer.setPropertyName("property");

 JXPathContext context = JXPathContext.newContext(pointer.getNode());
 Object value = new Object();
 NodePointer child = pointer.createChild(context, null, 0, value);
 assertNotSame(pointer, child);
 assertEquals(pointer.getBean(), child.getImmediateParentPointer().getNode());
 assertEquals("property", child.getName().getLocalPart());
 assertEquals(value, child.getNode());
}

 @Test
 public void testCreateChildWithName() {
 TestPropertyPointer pointer = new TestPropertyPointer(null);
 pointer.setBean(new Object());
 pointer.setIndex(0);
 pointer.setPropertyName("property");

 JXPathContext context = JXPathContext.newContext(pointer.getNode());
 NodePointer child = pointer.createChild(context, new QName("namespace:child"), 0);
 assertNotSame(pointer, child);
 assertEquals(pointer.getBean(), child.getImmediateParentPointer().getNode());
 assertEquals("child", child.getName().getLocalPart());
}

 @Test
 public void testHashCodeEquals() {
 TestPropertyPointer pointer1 = new TestPropertyPointer(null);
 TestPropertyPointer pointer2 = new TestPropertyPointer(null);
 Object bean = new Object();
 assertNotSame(pointer1, pointer2);
 assertNotEquals(pointer1.hashCode(), pointer2.hashCode());

 pointer1.setBean(bean);
 pointer1.setIndex(0);
 pointer2.setBean(bean);
 pointer2.setIndex(0);
 assertEquals(pointer1.hashCode(), pointer2.hashCode());

 pointer1.setIndex(1);
 assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
}

 @Test
 public void testCompareChildNodePointers() {
 TestPropertyPointer pointer1 = new TestPropertyPointer(null);
 TestPropertyPointer pointer2 = new TestPropertyPointer(null);
 Object bean = new Object();
 NodePointer child1 = new ChildNodePointer(pointer1, new QName("child1"), new Object());
 NodePointer child2 = new ChildNodePointer(pointer2, new QName("child2"), new Object());

 int result = pointer1.compareChildNodePointers(child1, child2);
 assertEquals(0, result);

 result = pointer1.compareChildNodePointers(child1, pointer2);
 assertLessThan(0, result);

 result = pointer1.compareChildNodePointers(pointer1, child2);
 assertGreaterThan(0, result);
}
}