import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer createPropertyPointer(String propertyName) {
 AbstractPropertyPointer pointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return propertyName;
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"propertyName"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 return (PropertyPointer) pointer;
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 assertEquals(-1, pointer.getPropertyIndex());
 pointer.setPropertyIndex(0);
 assertEquals(0, pointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 PropertyPointer pointer = createPropertyPointer("propertyName");
 pointer.setBean(bean);
 assertEquals(bean, pointer.getBean());
 }

 @Test
 public void testGetName() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 QName expectedName = new QName("", "propertyName");
 assertEquals(expectedName, pointer.getName());
 }

 @Test
 public void testGetPropertyName() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 assertEquals("propertyName", pointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 pointer.setPropertyName("newPropertyName");
 assertEquals("newPropertyName", pointer.getPropertyName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyCountUnsupported() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 pointer.getPropertyCount();
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 String[] expectedPropertyNames = {"propertyName"};
 assertArrayEquals(expectedPropertyNames, pointer.getPropertyNames());
 }

 @Test
 public void testIsActual() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 assertTrue(pointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 Object expectedValue = new Object();
 pointer.setValue(expectedValue);
 Object actualValue = pointer.getImmediateNode();
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testIsCollection() {
 PropertyPointer pointer = new AbstractPropertyPointer(null) {
 @Override
 public boolean isActualProperty() {
 return false;
 }
 };
 assertFalse(pointer.isCollection());

 PropertyPointer pointerWithCollection = createPropertyPointer("propertyName");
 Object collection = new Object[3];
 pointerWithCollection.setValue(collection);
 assertTrue(pointerWithCollection.isCollection());
 }

 @Test
 public void testGetLength() {
 PropertyPointer pointerWithCollection = createPropertyPointer("propertyName");
 Object collection = new Object[3];
 pointerWithCollection.setValue(collection);
 int expectedLength = 3;
 assertEquals(expectedLength, pointerWithCollection.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer pointer = createPropertyPointer("propertyName");
 Object expectedValue = new Object();
 pointer.setValue(expectedValue);
 NodePointer expectedValuePointer = NodePointer.newChildNodePointer(
 pointer,
 "propertyName",
 expectedValue);
 NodePointer actualValuePointer = pointer.getImmediateValuePointer();
 assertEquals(expectedValuePointer, actualValuePointer);
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 PropertyPointer pointer = createPropertyPointer("propertyName");
 Object expectedBean = new Object();
 pointer.setBean(expectedBean);
 NodePointer expectedValuePointer = NodePointer.newChildNodePointer(
 pointer,
 "propertyName",
 new Object());
 NodePointer actualValuePointer = pointer.createPath(context).getNode();
 assertEquals(expectedBean, actualValuePointer.getNode());
 assertEquals(expectedValuePointer, actualValuePointer.getImmediateChild());
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext();
 PropertyPointer pointer = createPropertyPointer("propertyName");
 Object expectedValue = new Object();
 NodePointer expectedValuePointer = NodePointer.newChildNodePointer(
 pointer,
 "propertyName",
 expectedValue);
 pointer.createPath(context, expectedValue);
 NodePointer actualValuePointer = pointer.getNode();
 assertEquals(expectedValuePointer, actualValuePointer);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 PropertyPointer parentPointer = createPropertyPointer("parentPropertyName");
 PropertyPointer childPointer = parentPointer.createChild(context,
 "childPropertyName",
 0);
 assertEquals(childPointer.getClass(), PropertyPointer.class);
 assertEquals(parentPointer.getBean(), childPointer.getBean());
 assertEquals("childPropertyName", childPointer.getPropertyName());
 assertEquals(0, childPointer.getPropertyIndex());
 }

 @Test
 public void testCreateChildWithIndexOutOfBounds() {
 JXPathContext context = new JXPathContext();PropertyPointer parentPointer = createPropertyPointer("parentPropertyName");
 PropertyPointer childPointer = parentPointer.createChild(context,
 "childPropertyName",
 1);
 assertEquals(childPointer.getClass(), PropertyPointer.class);
 assertEquals(parentPointer.getBean(), childPointer.getBean());
 assertEquals("childPropertyName", childPointer.getPropertyName());
 assertEquals(-1, childPointer.getPropertyIndex());
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = NodePointer.newChildNodePointer(null, "", new Object());
 NodePointer pointer2 = NodePointer.newChildNodePointer(null, "", new Object());
 NodePointer pointer3 = NodePointer.newChildNodePointer(null, "", new Object());
 NodePointer pointer4 = NodePointer.newChildNodePointer(null, "", new Object());
 PropertyPointer propertyPointer1 = createPropertyPointer("propertyName");
 PropertyPointer propertyPointer2 = createPropertyPointer("propertyName");
 int result = propertyPointer1.compareChildNodePointers(pointer1, pointer2);
 assertEquals(0, result);
 result = propertyPointer1.compareChildNodePointers(pointer1, propertyPointer2);
 assertEquals(-1, result);
 result = propertyPointer1.compareChildNodePointers(propertyPointer2, pointer1);
 assertEquals(1, result);
 result = propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer2);
 assertEquals(0, result);
 result = propertyPointer1.compareChildNodePointers(pointer3, pointer4);
 assertEquals(0, result);
 }

 @Test
 public void testHashCode() {
 PropertyPointer pointer1 = createPropertyPointer("propertyName1");
 PropertyPointer pointer2 = createPropertyPointer("propertyName2");
 PropertyPointer pointer3 = createPropertyPointer("propertyName1");
 int hashCode1 = pointer1.hashCode();
 int hashCode2 = pointer2.hashCode();
 int hashCode3 = pointer3.hashCode();
 assertEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testEquals() {
 PropertyPointer pointer1 = createPropertyPointer("propertyName1");
 PropertyPointer pointer2 = createPropertyPointer("propertyName2");
 PropertyPointer pointer3 = createPropertyPointer("propertyName1");
 PropertyPointer pointer4 = createPropertyPointer("propertyName1");
 PropertyPointer pointer5 = createPropertyPointer("propertyName2");
 assertTrue(pointer1.equals(pointer1));
 assertFalse(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 assertFalse(pointer1.equals(pointer4));
 assertFalse(pointer1.equals(pointer5));
 }
}