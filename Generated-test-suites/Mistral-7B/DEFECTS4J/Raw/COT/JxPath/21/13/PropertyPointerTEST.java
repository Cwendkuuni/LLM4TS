import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private NodePointer nodePointer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 PropertyPointerFactory factory = new PropertyPointerFactory();
 propertyPointer = factory.createPropertyPointer(null, "testProperty");
 nodePointer = propertyPointer.createPath(new JXPathContext());
 }

 @Test
 public void testGetPropertyIndex() {
 int propertyIndex = propertyPointer.getPropertyIndex();
 assertEquals(-1, propertyIndex);
 }

 @Test
 public void testSetPropertyIndexAndGetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 int propertyIndex = propertyPointer.getPropertyIndex();
 assertEquals(1, propertyIndex);
 }

 @Test
 public void testGetBean() {
 Object bean = propertyPointer.getBean();
 assertNotNull(bean);
 }

 @Test
 public void testGetName() {
 QName name = propertyPointer.getName();
 assertEquals("testProperty", name.getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 String propertyName = propertyPointer.getPropertyName();
 assertEquals("testProperty", propertyName);
 }

 @Test
 public void testSetPropertyName() {
 propertyPointer.setPropertyName("newPropertyName");
 String newPropertyName = propertyPointer.getPropertyName();
 assertEquals("newPropertyName", newPropertyName);
 }

 @Test
 public void testGetPropertyCount() {
 int propertyCount = propertyPointer.getPropertyCount();
 assertEquals(-1, propertyCount);
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = propertyPointer.getPropertyNames();
 assertArrayEquals(new String[]{}, propertyNames);
 }

 @Test
 public void testIsActualProperty() {
 boolean isActual = propertyPointer.isActualProperty();
 assertTrue(isActual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyNameUnexpectedly() {
 propertyPointer.setPropertyName("newPropertyName");
 propertyPointer.setPropertyName("newPropertyName2");
 }

 @Test
 public void testGetImmediateNode() {
 Object immediateNode = propertyPointer.getImmediateNode();
 assertNull(immediateNode);
 }

 @Test
 public void testIsCollection() {
 boolean isCollection = propertyPointer.isCollection();
 assertFalse(isCollection);
 }

 @Test
 public void testGetLength() {
 int length = propertyPointer.getLength();
 assertEquals(-1, length);
 }

 @Test
 public void testGetImmediateValuePointer() {
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(immediateValuePointer);
 }

 @Test
 public void testCreatePath() {
 NodePointer newNode = propertyPointer.createPath(new JXPathContext());
 assertEquals(propertyPointer, newNode);
 }

 @Test
 public void testCreatePathWithValue() {
 Object value = new Object();
 NodePointer newNode = propertyPointer.createPath(new JXPathContext(), value);
 assertNotNull(newNode);
 }

 @Test
 public void testCreateChild() {
 PropertyPointer child = (PropertyPointer) propertyPointer.createChild(
 new JXPathContext(), null, 0, null);
 assertEquals(propertyPointer, child.getImmediateParentPointer());
 }

 @Test
 public void testCreateChildWithName() {
 PropertyPointer child = (PropertyPointer) propertyPointer.createChild(
 new JXPathContext(), new QName("testChild"), 0, null);
 assertEquals("testChild", child.getPropertyName());
 }

 @Test
 public void testCreateChildWithValue() {
 Object value = new Object();
 PropertyPointer child = (PropertyPointer) propertyPointer.createChild(
 new JXPathContext(), null, 0, value);
 assertEquals(value, child.getNode());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer3 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer4 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer5 = new PropertyPointer(nodePointer.clone());
 int hashCode1 = propertyPointer.hashCode();
 int hashCode2 = propertyPointer1.hashCode();
 int hashCode3 = propertyPointer2.hashCode();
 int hashCode4 = propertyPointer3.hashCode();
 int hashCode5 = propertyPointer5.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertEquals(hashCode1, hashCode3);
 assertEquals(hashCode1, hashCode4);
 assertEquals(hashCode1, hashCode5);
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer3 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer4 = new PropertyPointer(nodePointer.clone());
 PropertyPointer propertyPointer5 = new PropertyPointer(nodePointer.clone());
 PropertyPointer propertyPointer6 = new PropertyPointer(nodePointer.clone());
 PropertyPointer propertyPointer7 = new PropertyPointer(new NodePointer(null));
 boolean equals1 = propertyPointer.equals(propertyPointer);
 boolean equals2 = propertyPointer.equals(propertyPointer1);
 boolean equals3 = propertyPointer.equals(propertyPointer2);
 boolean equals4 = propertyPointer.equals(propertyPointer3);
 boolean equals5 = propertyPointer.equals(propertyPointer4);
 boolean equals6 = propertyPointer.equals(propertyPointer5);
 boolean equals7 = propertyPointer.equals(propertyPointer6);
 boolean equals8 = propertyPointer.equals(propertyPointer7);
 assertTrue(equals1);
 assertTrue(equals2);
 assertTrue(equals3);
 assertTrue(equals4);
 assertTrue(equals5);
 assertTrue(equals6);
 assertFalse(equals7);
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = new NodePointer(nodePointer);
 NodePointer nodePointer2 = new NodePointer(nodePointer);
 int compareResult = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
 assertEquals(0, compareResult);
 }

 @Test
 public void testHashCodeWithDifferentParent() {
 PropertyPointer propertyPointer1 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(null));
 int hashCode1 = propertyPointer.hashCode();
 int hashCode2 = propertyPointer2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testEqualsWithDifferentParent() {
 PropertyPointer propertyPointer1 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(null));
 boolean equals = propertyPointer.equals(propertyPointer2);
 assertFalse(equals);
 }

 @Test
 public void testEqualsWithSameNameButDifferentParent() {
 PropertyPointer propertyPointer1 = new PropertyPointer(nodePointer);
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(null));
 propertyPointer2.setPropertyName("testProperty");
 boolean equals = propertyPointer.equals(propertyPointer2);
 assertFalse(equals);
 }

 @Test
 public void testGetImmediateNodeWhenValueIsSet() {
 Object value = new Object();
 propertyPointer.setValue(value);
 Object immediateNode = propertyPointer.getImmediateNode();
 assertEquals(value, immediateNode);
 }

 @Test
 public void testGetImmediateNodeWhenValueIsNull() {
 propertyPointer.setValue(null);
 Object immediateNode = propertyPointer.getImmediateNode();
 assertNull(immediateNode);
 }

 @Test
 public void testIsLeaf() {
 boolean isLeaf = propertyPointer.isLeaf();
 assertFalse(isLeaf);
 }

}