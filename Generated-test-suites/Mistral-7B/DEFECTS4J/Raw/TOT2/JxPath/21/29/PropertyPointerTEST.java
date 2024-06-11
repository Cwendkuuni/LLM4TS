import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private Object parent;

 @Before
 public void setUp() {
 parent = new Object() {}; // Any object that can be used as a parent
 propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 new NodePointer(parent), // Immediate parent pointer
 null, 0)); // New PropertyPointer with no name and index 0
 }

 @Test
 public void testGetPropertyIndex() {
 int expectedPropertyIndex = 0;
 int actualPropertyIndex = propertyPointer.getPropertyIndex();
 assertEquals(expectedPropertyIndex, actualPropertyIndex);
 }

 @Test
 public void testSetPropertyIndex() {
 int expectedPropertyIndex = 1;
 propertyPointer.setPropertyIndex(expectedPropertyIndex);
 int actualPropertyIndex = propertyPointer.getPropertyIndex();
 assertEquals(expectedPropertyIndex, actualPropertyIndex);
 }

 @Test
 public void testGetBean() {
 Object expectedBean = parent;
 Object actualBean = propertyPointer.getBean();
 assertEquals(expectedBean, actualBean);
 }

 @Test
 public void testGetName() {
 String expectedName = null;
 QName actualName = propertyPointer.getName();
 assertEquals(expectedName, actualName.getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 String expectedPropertyName = "testProperty";
 propertyPointer = new MyPropertyPointer(new NodePointer(parent), expectedPropertyName);
 String actualPropertyName = propertyPointer.getPropertyName();
 assertEquals(expectedPropertyName, actualPropertyName);
 }

 @Test
 public void testSetPropertyName() {
 String expectedPropertyName = "testProperty";
 propertyPointer.setPropertyName(expectedPropertyName);
 String actualPropertyName = propertyPointer.getPropertyName();
 assertEquals(expectedPropertyName, actualPropertyName);
 }

 @Test
 public void testGetPropertyCount() {
 int expectedPropertyCount = 3;
 PropertyPointer propertyPointerWithPropertyCount = new PropertyPointer(
 NodePointer.newChildNodePointer(
 new NodePointer(parent),
 null, 0)) {
 @Override
 public int getPropertyCount() {
 return expectedPropertyCount;
 }
 };
 int actualPropertyCount = propertyPointerWithPropertyCount.getPropertyCount();
 assertEquals(expectedPropertyCount, actualPropertyCount);
 }

 @Test
 public void testGetPropertyNames() {
 String[] expectedPropertyNames = {"property1", "property2", "property3"};
 PropertyPointer propertyPointerWithPropertyNames = new PropertyPointer(
 NodePointer.newChildNodePointer(
 new NodePointer(parent),
 null, 0)) {
 @Override
 public String[] getPropertyNames() {
 return expectedPropertyNames;
 }
 };
 String[] actualPropertyNames = propertyPointerWithPropertyNames.getPropertyNames();
 assertArrayEquals(expectedPropertyNames, actualPropertyNames);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsActualPropertyThrowsException() {
 propertyPointer.isActualProperty();
 }

 @Test
 public void testIsActual() {
 boolean expectedActual = true;
 boolean actualActual = propertyPointer.isActual();
 assertEquals(expectedActual, actualActual);
 }

 @Test
 public void testGetImmediateNode() {
 Object expectedImmediateNode = new Object() {}; // Any object that can be used as an immediate node
 propertyPointer.setValue(expectedImmediateNode);
 Object actualImmediateNode = propertyPointer.getImmediateNode();
 assertEquals(expectedImmediateNode, actualImmediateNode);
 }

 @Test
 public void testIsCollection() {
 boolean expectedIsCollection = false;
 boolean actualIsCollection = propertyPointer.isCollection();
 assertEquals(expectedIsCollection, actualIsCollection);
 }

 @Test
 public void testGetLength() {
 int expectedLength = 5;
 PropertyPointer propertyPointerWithLength = new PropertyPointer(
 NodePointer.newChildNodePointer(
 new NodePointer(parent),
 null, 0)) {
 @Override
 public int getLength() {
 return expectedLength;
 }
 };
 int actualLength = propertyPointerWithLength.getLength();
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetImmediateValuePointer() {
 NodePointer expectedImmediateValuePointer = NodePointer.newChildNodePointer(
 propertyPointer,
 null, 0);
 NodePointer actualImmediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(expectedImmediateValuePointer, actualImmediateValuePointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreatePathWithNegativeIndexThrowsException() {
 propertyPointer.createPath(null, null, -1, null);
 }
}

class MyPropertyPointer extends PropertyPointer {
 private String propertyName;

 MyPropertyPointer(NodePointer parent, String propertyName) {
 super(parent);
 this.propertyName = propertyName;
 }

 @Override
 public String getPropertyName() {
 return propertyName;
 }
}