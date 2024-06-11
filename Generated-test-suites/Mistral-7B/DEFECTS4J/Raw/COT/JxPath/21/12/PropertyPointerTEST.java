import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;

 @Test
 public void testConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(5);
 assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(5);
 propertyPointer.setPropertyIndex(10);
 assertEquals(10, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPropertyIndexNegative() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testSetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean);
 propertyPointer.setBean(new Object());
 assertNotSame(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 String propertyName = "testProperty";
 propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return propertyName;
 }
 };
 assertEquals(new QName(null, propertyName), propertyPointer.getName());
 }

 @Test
 public void testSetPropertyName() {
 String propertyName = "testProperty";
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName(propertyName);
 assertEquals(propertyName, propertyPointer.getPropertyName());
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
 Object testObj = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return testObj;
 }
 };
 Object immediateNode = propertyPointer.getImmediateNode();
 assertEquals(testObj, immediateNode);
 }

 @Test
 public void testIsCollection() {
 Object collection = new Object()[5];
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return collection;
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 Object leaf = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return leaf;
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 Object collection = new Object[5];
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return collection;
 }
 };
 assertEquals(5, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = null;
 Object testObj = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return testObj;
 }
 };
 PropertyPointer result = propertyPointer.createPath(context);
 assertEquals(propertyPointer, result);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathException() {
 JXPathContext context = null;
 Object testObj = new Object();propertyPointer = new PropertyPointer(null) { @Override public Object getBaseValue() { return testObj; } }; PropertyPointer result = propertyPointer.createPath(context, null); fail("Exception not thrown"); }

 @Test
 public void testClone() {
 PropertyPointer original = new PropertyPointer(null);
 PropertyPointer clone = original.clone();
 assertNotSame(original, clone);
 assertNotNull(clone.getValue());
 }

 @Test
 public void testHashCode() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 PropertyPointer pointer3 = new PropertyPointer(null);
 Pointer pointer4 = new NodePointer();
 int hashCode = pointer1.hashCode();
 assertEquals(hashCode, pointer1.hashCode());
 assertEquals(hashCode, pointer2.hashCode());
 assertEquals(hashCode, pointer3.hashCode());
 assertEquals(hashCode, pointer1.compareChildNodePointers(pointer1, pointer4));
 }

 @Test
 public void testEquals() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 PropertyPointer pointer3 = new PropertyPointer(null);
 NodePointer pointer4 = new NodePointer();
 Pointer pointer5 = new PropertyPointer(null);
 Pointer pointer6 = new PropertyPointer(null);
 PropertyPointer pointer7 = new PropertyPointer(null);
 PropertyPointer pointer8 = new PropertyPointer(null);
 // equals self
 assertEquals(pointer1, pointer1);
 // equals null
 assertNotEquals(pointer1, null);
 // equals same instance
 assertEquals(pointer1, pointer1);
 // equals same class
 assertEquals(pointer1, pointer2);
 // not equals different class
 assertNotEquals(pointer1, pointer4);
 // equals same index and propertyName
 pointer7.setPropertyIndex(0);
 pointer7.setPropertyName("testProperty");
 pointer8.setPropertyIndex(0);
 pointer8.setPropertyName("testProperty");
 assertEquals(pointer1, pointer7);
 assertEquals(pointer1, pointer8);
 // not equals different index
 pointer7.setPropertyIndex(1);
 assertNotEquals(pointer1, pointer7);
 // not equals different propertyName
 pointer8.setPropertyName("testProperty2");
 assertNotEquals(pointer1, pointer8);
 // not equals different parent
 pointer6.setParent(new NodePointer());
 pointer6.setPropertyIndex(0);
 pointer6.setPropertyName("testProperty");
 assertNotEquals(pointer1, pointer6);
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 NodePointer pointer3 = new NodePointer();
 int result = pointer1.compareChildNodePointers(pointer1, pointer1);
 assertEquals(0, result);
 result = pointer1.compareChildNodePointers(pointer1, pointer2);
 assertEquals(-1, result);
 result = pointer1.compareChildNodePointers(pointer2, pointer1);
 assertEquals(1, result);
 result = pointer1.compareChildNodePointers(pointer1, pointer3);
 assertEquals(Integer.MIN_VALUE, result);
 result = pointer1.compareChildNodePointers(pointer3, pointer1);
 assertEquals(Integer.MAX_VALUE, result);
 result = pointer2.compareChildNodePointers(pointer1, pointer1);
 assertEquals(1, result);
 result = pointer2.compareChildNodePointers(pointer1, pointer2);
 assertEquals(0, result);
 result = pointer2.compareChildNodePointers(pointer2, pointer1);
 assertEquals(-1, result);
 result = pointer2.compareChildNodePointers(pointer2, pointer3);
 assertEquals(Integer.MIN_VALUE, result);
 result = pointer2.compareChildNodePointers(pointer3, pointer2);
 assertEquals(Integer.MAX_VALUE, result);
 }
}