package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyPointerTest {

 @Mock
 private NodePointer parent;
 @Mock
 private JXPathContext context;
 @Mock
 private AbstractFactory factory;
 @InjectMocks
 private PropertyPointer propertyPointer;

 @Before
 public void setup() {
 }

 @Test
 public void testConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(parent);
 Assert.assertNotNull(propertyPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 Assert.assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyIndex(2);
 Assert.assertEquals(2, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object bean = Mockito.mock(Object.class);
 Mockito.when(parent.getNode()).thenReturn(bean);
 Assert.assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 QName name = propertyPointer.getName();
 Assert.assertEquals(new QName(null, propertyName), name);
 }

 @Test
 public void testGetPropertyName() {
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 Assert.assertEquals(propertyName, propertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 String oldPropertyName = "oldPropertyName";
 propertyPointer.setPropertyName(oldPropertyName);
 String newPropertyName = "newPropertyName";
 propertyPointer.setPropertyName(newPropertyName);
 Assert.assertEquals(newPropertyName, propertyPointer.getPropertyName());
 Assert.assertNotEquals(oldPropertyName, propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 Mockito.when(factory.createObject(context, propertyPointer, Mockito.any(), Mockito.anyString(), Mockito.anyInt()))
 .thenReturn(Mockito.mock(Object.class));
 Mockito.when(ValueUtils.getLength(Mockito.any()))
 .thenReturn(1);
 Assert.assertEquals(1, propertyPointer.getPropertyCount());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyCountUnsupported() {
 propertyPointer = new PropertyPointer(parent) {
 @Override
 public int getPropertyCount() {
 throw new UnsupportedOperationException();
 }
 };
 propertyPointer.getPropertyCount();
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = {"property1", "property2"};
 Mockito.when(factory.createObject(context, propertyPointer, Mockito.any(), Mockito.anyString(), Mockito.anyInt()))
 .thenReturn(Mockito.mock(Object.class));
 Mockito.when(ValueUtils.getLength(Mockito.any()))
 .thenReturn(2);
 Mockito.when(ValueUtils.getPropertyNames(Mockito.any()))
 .thenReturn(propertyNames);
 Assert.assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyNamesUnsupported() {
 propertyPointer = new PropertyPointer(parent) {
 @Override
 public String[] getPropertyNames() {
 throw new UnsupportedOperationException();
 }
 };
 propertyPointer.getPropertyNames();
 }

 @Test
 public void testIsActualProperty() {
 Mockito.when(factory.isActualProperty(Mockito.any(), Mockito.anyString()))
 .thenReturn(true);
 Assert.assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualPropertyFalse() {
 Mockito.when(factory.isActualProperty(Mockito.any(), Mockito.anyString()))
 .thenReturn(false);
 Assert.assertFalse(propertyPointer.isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 Object immediateNode = Mockito.mock(Object.class);
 propertyPointer.setValue(immediateNode);
 Assert.assertEquals(immediateNode, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 Object collection = new Object[5];
 propertyPointer.setValue(collection);
 Assert.assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 String leafValue = "testValue";
 propertyPointer.setValue(leafValue);
 Assert.assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 Object lengthObject = new Object[5];
 propertyPointer.setValue(lengthObject);
 Assert.assertEquals(5, propertyPointer.getLength());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLengthUnsupported() {
 propertyPointer = new PropertyPointer(parent) {
 @Override
 public int getLength() {
 throw new UnsupportedOperationException();
 }
 };
 propertyPointer.getLength();
 }

 @Test
 public void testGetImmediateValuePointer() {
 Object immediateNode = Mockito.mock(Object.class);
 propertyPointer.setValue(immediateNode);
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 Assert.assertNotNull(immediateValuePointer);
 Assert.assertEquals(propertyPointer, immediateValuePointer.getParent());
 Assert.assertEquals(propertyPointer.getName(), immediateValuePointer.getName());
 Assert.assertEquals(immediateNode, immediateValuePointer.getNode());
 }

 @Test
 public void testCreatePath() {
 Object bean = Mockito.mock(Object.class);
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 Mockito.when(parent.getNode()).thenReturn(bean);
 propertyPointer.createPath(context);
 }

 @Test
 public void testCreatePathWithValue() {
 Object bean = Mockito.mock(Object.class);
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 Mockito.when(parent.getNode()).thenReturn(bean);
 Object value = Mockito.mock(Object.class);
 propertyPointer.createPath(context, value);
 Mockito.verify(factory, Mockito.times(1))
 .createObject(context, propertyPointer, bean, propertyName, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullContext() {
 propertyPointer.createPath(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCreatePathWithNullParent() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.createPath(context);
 }

 @Test(expected = NullPointerException.class)
 public void testCreatePathWithNullBean() {
 propertyPointer.setPropertyName("testProperty");
 Mockito.when(parent.getNode()).thenReturn(null);
 propertyPointer.createPath(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullPropertyName() {
 propertyPointer.createPath(context, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreatePathWithIndexOutOfBounds() {
 Object bean = Mockito.mock(Object.class);
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 Mockito.when(parent.getNode()).thenReturn(bean);
 propertyPointer.createPath(context, 10);
 }

 @Test
 public void testCreateChild() {
 Object childBean = Mockito.mock(Object.class);
 String childPropertyName = "testChildProperty";
 propertyPointer.setPropertyName("testProperty");
 NodePointer childPropertyPointer = Mockito.mock(NodePointer.class);
 propertyPointer.createChild(context, new QName(null, childPropertyName), 0, childBean);
 Mockito.verify(factory, Mockito.times(1))
 .createObject(context, propertyPointer, childBean, childPropertyName, 0);
 Assert.assertEquals(new QName(null, childPropertyName), propertyPointer.getName());
 Assert.assertEquals(childBean, propertyPointer.getBean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithNullContext() {
 propertyPointer.createChild(null, new QName(null, "testChildProperty"), 0, Mockito.mock(Object.class));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWithNullParent() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.createChild(context, new QName(null, "testChildProperty"), 0, Mockito.mock(Object.class));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWithNullChildBean() {
 propertyPointer.createChild(context, new QName(null, "testChildProperty"), 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithNullQName() {
 propertyPointer.createChild(context, null, 0, Mockito.mock(Object.class));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreateChildWithIndexOutOfBounds() {
 Object bean = Mockito.mock(Object.class);
 String propertyName = "testProperty";
 String childPropertyName = "testChildProperty";
 propertyPointer.setPropertyName(propertyName);
 Mockito.when(parent.getNode()).thenReturn(bean);
 propertyPointer.createChild(context, new QName(null, childPropertyName), 5, Mockito.mock(Object.class));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = Mockito.mock(NodePointer.class);
 NodePointer nodePointer2 = Mockito.mock(NodePointer.class);
 Mockito.when(propertyPointer.getValuePointer()).thenReturn(nodePointer1);
 propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
 }
}