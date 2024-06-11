import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @Before
 public void setup() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 }

 @Test
 public void testConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
 Assert.assertNotNull(propertyPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 int index = propertyPointer.getPropertyIndex();
 Assert.assertEquals(1, index);
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyIndex(2);
 int index = propertyPointer.getPropertyIndex();
 Assert.assertEquals(2, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexNegative() {
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer.setBean(bean);
 Object beanRetrieved = propertyPointer.getBean();
 Assert.assertEquals(bean, beanRetrieved);
 }

 @Test
 public void testGetName() {
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 QName name = propertyPointer.getName();
 Assert.assertEquals(new QName(null, propertyName), name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyNameNull() {
 propertyPointer.setPropertyName(null);
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
 Assert.assertFalse(propertyPointer.isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 Object value = new Object();
 propertyPointer.setValue(value);
 Object nodeRetrieved = propertyPointer.getImmediateNode();
 Assert.assertEquals(value, nodeRetrieved);
 }

 @Test
 public void testIsCollection() {
 Object collection = new Object()[1];
 propertyPointer.setValue(collection);
 boolean isCollection = propertyPointer.isCollection();
 Assert.assertTrue(isCollection);
 }

 @Test
 public void testIsLeaf() {
 Object leaf = new Object();
 propertyPointer.setValue(leaf);
 boolean isLeaf = propertyPointer.isLeaf();
 Assert.assertTrue(isLeaf);
 }

 @Test
 public void testGetLength() {
 Object collection = new Object[3];
 propertyPointer.setValue(collection);
 int length = propertyPointer.getLength();
 Assert.assertEquals(3, length);
 }

 @Test
 public void testGetImmediateValuePointer() {
 Object value = new Object();
 propertyPointer.setValue(value);
 NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
 Assert.assertNotNull(valuePointer);
 }

 @Test
 public void testCreatePath() {
 Object bean = new Object();
 propertyPointer.setBean(bean);
 propertyPointer.createPath(null);
 Assert.assertNotNull(propertyPointer.getNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathNullBean() {
 propertyPointer.createPath(null);
 }

 @Test
 public void testCreatePathWithValue() {
 Object bean = new Object();
 propertyPointer.setBean(bean);
 Object value = new Object();
 propertyPointer.createPath(null, value);
 Assert.assertEquals(value, propertyPointer.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWithNullName() {
 propertyPointer.createChild(null, null, 0, null);
 }

 @Test
 public void testCreateChild() {
 PropertyPointer childPointer = new PropertyPointer(propertyPointer);
 childPointer.setPropertyName("childProperty");
 propertyPointer.createChild(null, childPointer.getName(), 0, null);
 String childPropertyName = propertyPointer.getPropertyName();
 Assert.assertEquals("childProperty", childPropertyName);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreateChildWithIndexOutOfBounds() {
 propertyPointer.createChild(null, null, getLength() + 1, null);
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer);
 propertyPointer3.setIndex(1);
 int hashCode1 = propertyPointer1.hashCode();
 int hashCode2 = propertyPointer2.hashCode();
 int hashCode3 = propertyPointer3.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 Assert.assertEquals(hashCode1, hashCode3);
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer);
 propertyPointer1.setIndex(0);
 propertyPointer2.setIndex(1);
 boolean equals12 = propertyPointer1.equals(propertyPointer2);
 boolean equals13 = propertyPointer1.equals(propertyPointer3);
 boolean equals14 = propertyPointer1.equals(propertyPointer4);
 boolean equals23 = propertyPointer2.equals(propertyPointer3);
 boolean equals24 = propertyPointer2.equals(propertyPointer4);
 boolean equals34 = propertyPointer3.equals(propertyPointer4);
 Assert.assertFalse(equals12);
 Assert.assertFalse(equals13);
 Assert.assertTrue(equals14);
 Assert.assertFalse(equals23);
 Assert.assertFalse(equals24);
 Assert.assertTrue(equals34);
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = new NodePointer(null);
 NodePointer nodePointer2 = new NodePointer(null);
 int comparisonResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
 Assert.assertEquals(0, comparisonResult);
 }

}