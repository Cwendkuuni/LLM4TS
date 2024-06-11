import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstructor() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 Assert.assertNotNull(propertyPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer.setPropertyIndex(5);
 Assert.assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer.setPropertyIndex(10);
 propertyPointer.setPropertyIndex(5);
 Assert.assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexNegativeValue() {
 propertyPointer.setPropertyIndex(-5);
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer.setBean(bean);
 Assert.assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 Assert.assertEquals(new QName(null, propertyName), propertyPointer.getName());
 }

 @Test
 public void testSetPropertyName() {
 String newPropertyName = "newPropertyName";
 propertyPointer.setPropertyName(newPropertyName);
 Assert.assertEquals(newPropertyName, propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 Assert.assertThrows(UnsupportedOperationException.class, propertyPointer::getPropertyCount);
 }

 @Test
 public void testGetPropertyNames() {
 Assert.assertThrows(UnsupportedOperationException.class, propertyPointer::getPropertyNames);
 }

 @Test
 public void testIsActualProperty() {
 Assert.assertThrows(UnsupportedOperationException.class, propertyPointer::isActualProperty);
 }

 @Test
 public void testIsActual() {
 propertyPointer.setActual(true);
 Assert.assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActualFalse() {
 propertyPointer.setActual(false);
 Assert.assertFalse(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 Object testValue = new Object();
 propertyPointer.setValue(testValue);
 Assert.assertEquals(testValue, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 Object testCollection = new Object()[5];
 propertyPointer.setValue(testCollection);
 Assert.assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 Object testLeaf = new Object();
 propertyPointer.setValue(testLeaf);
 Assert.assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 Object testCollection = new Object[5];
 propertyPointer.setValue(testCollection);
 Assert.assertEquals(5, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 propertyPointer = (PropertyPointer) propertyPointer.createPath(context);
 Assert.assertNotNull(propertyPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathNullContext() {
 JXPathContext context = null;
 propertyPointer = (PropertyPointer) propertyPointer.createPath(context);
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = (PropertyPointer) propertyPointer.createPath(context);
 NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
 Assert.assertNotNull(valuePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext();
 propertyPointer.setValue(new Object());
 propertyPointer = (PropertyPointer) propertyPointer.createPath(context);
 }

 @Test
 public void testCreateChildNullContext() {
 JXPathContext context = null;
 propertyPointer = (PropertyPointer) propertyPointer.createChild(context, null, 0, null);
 Assert.assertNull(propertyPointer);
 }

 @Test
 public void testCreateChildNullName() {
 JXPathContext context = new JXPathContext();
 propertyPointer = (PropertyPointer) propertyPointer.createChild(context, null, 0, new Object());
 Assert.assertNotNull(propertyPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildNegativeIndex() {
 JXPathContext context = new JXPathContext();propertyPointer = (PropertyPointer) propertyPointer.createChild(context, null, -1, new Object());
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 propertyPointer = (PropertyPointer) propertyPointer.createChild(context, new QName("test"), 0, new Object());
 Assert.assertNotNull(propertyPointer);
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext();
 propertyPointer = (PropertyPointer) propertyPointer.createChild(context, new QName("test"), 0, new Object("testValue"));
 Assert.assertNotNull(propertyPointer);
 }

 @Test
 public void testHashCode() {
 NodePointer newParent = new NodePointer(null);
 PropertyPointer newPropertyPointer = new PropertyPointer(newParent);
 PropertyPointer newPropertyPointer2 = new PropertyPointer(newParent);
 PropertyPointer newPropertyPointer3 = new PropertyPointer(newParent);
 PropertyPointer newPropertyPointer4 = new PropertyPointer(newParent);
 PropertyPointer newPropertyPointer5 = new PropertyPointer(newParent);
 newPropertyPointer.setPropertyIndex(5);
 newPropertyPointer2.setPropertyIndex(6);
 newPropertyPointer3.setIndex(1);
 newPropertyPointer4.setIndex(2);
 newPropertyPointer5.setIndex(3);
 Assert.assertEquals(newPropertyPointer.hashCode(), newPropertyPointer2.hashCode());
 Assert.assertEquals(newPropertyPointer.hashCode(), newPropertyPointer3.hashCode());
 Assert.assertEquals(newPropertyPointer.hashCode(), newPropertyPointer4.hashCode());
 Assert.assertEquals(newPropertyPointer.hashCode(), newPropertyPointer5.hashCode());
 Assert.assertEquals(newPropertyPointer.hashCode(), newPropertyPointer.hashCode());
 Assert.assertNotEquals(newPropertyPointer.hashCode(), newPropertyPointer3.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer newPropertyPointer = new PropertyPointer(parentPointer);
 PropertyPointer newPropertyPointer2 = new PropertyPointer(parentPointer);
 PropertyPointer newPropertyPointer3 = new PropertyPointer(parentPointer);
 PropertyPointer newPropertyPointer4 = new PropertyPointer(parentPointer);
 PropertyPointer newPropertyPointer5 = new PropertyPointer(parentPointer);
 PropertyPointer newPropertyPointer6 = new PropertyPointer(new NodePointer(null));
 newPropertyPointer.setPropertyIndex(5);
 newPropertyPointer2.setPropertyIndex(6);
 newPropertyPointer3.setIndex(1);
 newPropertyPointer4.setIndex(2);
 newPropertyPointer5.setIndex(3);
 newPropertyPointer6.setPropertyIndex(4);
 Assert.assertTrue(newPropertyPointer.equals(newPropertyPointer));
 Assert.assertTrue(newPropertyPointer.equals(newPropertyPointer2));
 Assert.assertTrue(newPropertyPointer.equals(newPropertyPointer3));
 Assert.assertTrue(newPropertyPointer.equals(newPropertyPointer4));
 Assert.assertTrue(newPropertyPointer.equals(newPropertyPointer5));
 Assert.assertFalse(newPropertyPointer.equals(newPropertyPointer6));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = new NodePointer(null);
 NodePointer pointer2 = new NodePointer(null);
 Assert.assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
 }

}