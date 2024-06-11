import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstructor() {
 propertyPointer = new PropertyPointer(null);
 Assert.assertNull(propertyPointer.getBean());
 Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 Assert.assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexNegativeValue() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean);
 Assert.assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 Assert.assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyNameNull() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testGetPropertyCount() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testGetPropertyNames() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testIsActualProperty() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 Assert.assertNull(propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 Object collection = new Object[] { 1, 2, 3 };
 propertyPointer.setValue(collection);
 Assert.assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeafForCollection() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 Object collection = new Object[] { 1, 2, 3 };
 propertyPointer.setValue(collection);
 Assert.assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testIsLeafForScalar() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setValue(1);
 Assert.assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLengthForCollection() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 Object collection = new Object[] { 1, 2, 3 };
 propertyPointer.setValue(collection);
 Assert.assertEquals(3, propertyPointer.getLength());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLengthForScalar() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setValue(1);
 propertyPointer.getLength();
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setValue(1);
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 Assert.assertNotNull(nodePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreatePathForNullContext() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = null;
 propertyPointer.createPath(context);
 }

 @Test
 public void testCreatePath() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext();
 propertyPointer.createPath(context);
 Assert.assertEquals(propertyPointer, context.getCurrentNode());
 }

 @Test
 public void testCreatePathWithValue() {
 propertyPointer = new PropertyPointer(null);propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext();
 propertyPointer.setValue(1);
 propertyPointer.createPath(context);
 Assert.assertEquals(propertyPointer, context.getCurrentNode());
 }

 @Test
 public void testCreateChildForNullContext() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = null;
 propertyPointer.createChild(context, null, 0, 1);
 }

 @Test
 public void testCreateChild() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext();
 PropertyPointer childPointer = new PropertyPointer(propertyPointer);
 childPointer.setPropertyName("testChildProperty");
 childPointer.setValue(1);
 PropertyPointer result = propertyPointer.createChild(context, null, 0, childPointer);
 Assert.assertEquals(childPointer, result);
 }

 @Test
 public void testHashCode() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 NodePointer nodePointer = new NodePointer(null);
 propertyPointer.setImmediateParentPointer(nodePointer);
 PropertyPointer pointer3 = new PropertyPointer(nodePointer);
 pointer3.setPropertyName("testProperty");
 pointer3.setPropertyIndex(0);
 Assert.assertEquals(propertyPointer.hashCode(), pointer3.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 Assert.assertFalse(pointer1.equals(pointer2));
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 PropertyPointer pointer3 = new PropertyPointer(propertyPointer);
 PropertyPointer pointer4 = new PropertyPointer(null);
 propertyPointer.setImmediateParentPointer(pointer4);
 PropertyPointer pointer5 = new PropertyPointer(propertyPointer);
 Assert.assertTrue(pointer1.equals(pointer1));
 Assert.assertTrue(pointer1.equals(pointer3));
 Assert.assertTrue(pointer1.equals(pointer5));
 Assert.assertFalse(pointer1.equals(pointer2));
 Assert.assertFalse(pointer1.equals(pointer4));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = new NodePointer(null);
 NodePointer pointer2 = new NodePointer(null);
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyIndex(0);
 Assert.assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
 propertyPointer.setValue(1);
 NodePointer valuePointer = propertyPointer.getValuePointer();
 Assert.assertEquals(-1, propertyPointer.compareChildNodePointers(pointer1, valuePointer));
 Assert.assertEquals(1, propertyPointer.compareChildNodePointers(valuePointer, pointer1));
 }
}