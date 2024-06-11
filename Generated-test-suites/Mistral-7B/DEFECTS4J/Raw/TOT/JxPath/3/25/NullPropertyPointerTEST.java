package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NullPropertyPointerTest {

 private NodePointer root;
 private PropertyOwnerPointer propertyOwner;
 private NullPropertyPointer nullProperty;

 @Before
 public void setUp() {
 root = mock(NodePointer.class);
 propertyOwner = mock(PropertyOwnerPointer.class);
 when(propertyOwner.isDynamicPropertyDeclarationSupported()).thenReturn(true);
 when(propertyOwner.getPropertyPointer()).thenReturn(mock(PropertyPointer.class));
 nullProperty = new NullPropertyPointer(propertyOwner);
 }

 @Test
 public void testGetName() {
 QName name = nullProperty.getName();
 Assert.assertEquals(new QName("*"), name);
 }

 @Test
 public void testSetPropertyIndex() {
 nullProperty.setPropertyIndex(1);
 }

 @Test
 public void testGetLength() {
 Assert.assertEquals(0, nullProperty.getLength());
 }

 @Test
 public void testGetBaseValue() {
 Assert.assertNull(nullProperty.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 Assert.assertNull(nullProperty.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 Assert.assertTrue(nullProperty.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NodePointer valuePointer = nullProperty.getValuePointer();
 Assert.assertNotNull(valuePointer);
 Assert.assertInstanceOf(NullPointer.class, valuePointer);
 }

 @Test
 public void testIsActualProperty() {
 Assert.assertFalse(nullProperty.isActualProperty());
 }

 @Test
 public void testIsActual() {
 Assert.assertFalse(nullProperty.isActual());
 }

 @Test
 public void testIsContainer() {
 Assert.assertTrue(nullProperty.isContainer());
 }

 @Test
 public void testSetValue() {
 nullProperty.setValue(new Object());
 verify(propertyOwner).getPropertyPointer().setPropertyName(nullProperty.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
 nullProperty.setValue(new Object());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNotChangeable() {
 NullPropertyPointer notChangeableNullProperty = new NullPropertyPointer(mock(NullPointer.class));
 notChangeableNullProperty.setValue(new Object());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = mock(JXPathContext.class);
 NodePointer newParent = nullProperty.createPath(context);
 Assert.assertEquals(propertyOwner, newParent);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = mock(JXPathContext.class);
 NodePointer newParent = nullProperty.createPath(context, new Object());
 Assert.assertEquals(propertyOwner, newParent);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = mock(JXPathContext.class);
 QName name = new QName("test");
 NodePointer child = nullProperty.createPath(context).createChild(context, name);
 Assert.assertNotNull(child);
 Assert.assertEquals(name, child.getName());
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = mock(JXPathContext.class);
 QName name = new QName("test");
 Object value = new Object();
 NodePointer child = nullProperty.createPath(context).createChild(context, name, 1, value);
 Assert.assertNotNull(child);
 Assert.assertEquals(name, child.getName());
 Assert.assertEquals(value, child.getValue());
 }
}