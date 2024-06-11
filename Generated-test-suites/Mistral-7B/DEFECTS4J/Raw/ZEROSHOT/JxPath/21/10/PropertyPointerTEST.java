import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstruction() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 assertNull(propertyPointer.getPropertyName());
 assertEquals(-1, propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testSetPropertyName() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test(expected = AssertionError.class)
 public void testSetPropertyNameNull() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testSetBean() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(new Object());
 assertEquals(new Object(), propertyPointer.getBean());
 }

 @Test
 public void testSetPropertyIndex() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndexNegative() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyIndex(-1);
 assertEquals(-1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndexSameAsCurrent() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
 propertyPointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetPropertyCount() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyCount(5);
 assertEquals(5, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 parentPointer = new NodePointer(null);
 String[] propertyNames = new String[]{"name1", "name2", "name3"};
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public String[] getPropertyNames() {
 return propertyNames;
 }
 };
 assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 assertFalse(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualPropertyWithImplementation() {
 parentPointer = new NodePointer(null);
 PropertyPointer propertyPointerWithImplementation = new PropertyPointerWithValue(parentPointer, "testProperty") {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointerWithImplementation.isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 Object expectedValue = new Object();
 propertyPointer.setValue(expectedValue);
 assertEquals(expectedValue, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setValue(new Object[5]);
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setValue(new Integer(5));
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setValue(new Object[5]);
 assertEquals(5, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 Object expectedValue = new Object();
 propertyPointer.setValue(expectedValue);
 NodePointer expectedValuePointer = NodePointer.newChildNodePointer(propertyPointer, new QName(""), expectedValue);
 assertEquals(expectedValuePointer, propertyPointer.getImmediateValuePointer());
 }

 @Test
 public void testCreatePath() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 Object expectedValue = new Object();
 propertyPointer.setValue(expectedValue);
 JXPathContext context = new JXPathContext();
 NodePointer expectedPath = NodePointer.newChildNodePointer(parentPointer, new QName(""), expectedValue);
 assertEquals(expectedPath, propertyPointer.createPath(context));
 }

 @Test
 public void testCreatePathWithValue() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 Object expectedValue = new Object();
 JXPathContext context = new JXPathContext();
 propertyPointer.createPath(context, expectedValue);
 NodePointer expectedPath = NodePointer.newChildNodePointer(parentPointer, new QName(""), expectedValue);
 assertEquals(expectedPath, propertyPointer);
 }

 @Test
 public void testCreateChild() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 String childPropertyName = "childProperty";
 NodePointer childProperty = NodePointer.newChildNodePointer(propertyPointer, new QName(childPropertyName), new Object());
 assertEquals(childPropertyName, childProperty.getPropertyName());
 assertEquals(propertyPointer, childProperty.getParentPointer());
 assertEquals(propertyPointer.getBean(), childProperty.getBean());
 }

 @Test
 public void testHashCode() {
 parentPointer = new NodePointer(null);
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer);
 propertyPointer3.setPropertyIndex(1);
 int expectedHashCode = parentPointer.hashCode() + propertyPointer1.getPropertyIndex() + 0;
 assertEquals(expectedHashCode, propertyPointer1.hashCode());
 assertEquals(expectedHashCode, propertyPointer2.hashCode());
 assertEquals(expectedHashCode, propertyPointer3.hashCode());
 }

 @Test
 public void testEquals() {
 parentPointer = new NodePointer(null);
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer);
 PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer);
 propertyPointer3.setPropertyIndex(1);
 assertTrue(propertyPointer1.equals(propertyPointer1));
 assertTrue(propertyPointer1.equals(propertyPointer2));
 assertFalse(propertyPointer1.equals(propertyPointer3));
 assertFalse(propertyPointer1.equals(propertyPointer4));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = NodePointer.newChildNodePointer(null, new QName(""), new Object());
 NodePointer pointer2 = NodePointer.newChildNodePointer(null, new QName(""), new Object());
 NodePointer pointer3 = NodePointer.newChildNodePointer(null, new QName(""), new Object());
 NodePointer pointer4 = NodePointer.newChildNodePointer(null, new QName(""), new Object());
 NodePointer pointer5 = NodePointer.newChildNodePointer(null, new QName(""), new Object());
 int expectedResult = 0;
 assertEquals(expectedResult, propertyPointer.compareChildNodePointers(pointer1, pointer2));
 assertEquals(expectedResult, propertyPointer.compareChildNodePointers(pointer2, pointer1));
 assertEquals(-1, propertyPointer.compareChildNodePointers(pointer3, pointer2));
 assertEquals(1, propertyPointer.compareChildNodePointers(pointer2, pointer3));
 }
}