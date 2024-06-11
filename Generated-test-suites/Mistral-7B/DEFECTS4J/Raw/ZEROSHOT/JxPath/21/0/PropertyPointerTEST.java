import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private NodePointer parentPointer;

 @Before
 public void setUp() {
 parentPointer = new NodePointer(new Object());
 propertyPointer = new TestPropertyPointer(parentPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 int expected = 0;
 int actual = propertyPointer.getPropertyIndex();
 assertEquals("Unexpected property index", expected, actual);
 }

 @Test
 public void testSetPropertyIndex() {
 int expected = 1;
 propertyPointer.setPropertyIndex(expected);
 int actual = propertyPointer.getPropertyIndex();
 assertEquals("Unexpected property index", expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPropertyIndexWithInvalidIndex() {
 propertyPointer.setPropertyIndex(-1);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testGetBean() {
 Object expected = new Object();
 Object actual = propertyPointer.getBean();
 assertEquals("Unexpected bean", expected, actual);
 }

 @Test
 public void testGetName() {
 String expected = "testProperty";
 propertyPointer.setPropertyName(expected);
 QName actual = propertyPointer.getName();
 assertEquals("Unexpected name", expected, actual.getName());
 }

 @Test
 public void testGetPropertyName() {
 String expected = "testProperty";
 String actual = propertyPointer.getPropertyName();
 assertEquals("Unexpected property name", expected, actual);
 }

 @Test
 public void testSetPropertyName() {
 String expected = "testProperty2";
 propertyPointer.setPropertyName(expected);
 String actual = propertyPointer.getPropertyName();
 assertEquals("Unexpected property name", expected, actual);
 }

 @Test
 public void testGetPropertyCount() {
 int expected = 1;
 int actual = propertyPointer.getPropertyCount();
 assertEquals("Unexpected property count", expected, actual);
 }

 @Test
 public void testGetPropertyNames() {
 String[] expected = new String[]{"testProperty"};
 String[] actual = propertyPointer.getPropertyNames();
 assertArrayEquals("Unexpected property names", expected, actual);
 }

 @Test
 public void testIsActualProperty() {
 boolean expected = true;
 boolean actual = propertyPointer.isActualProperty();
 assertEquals("Unexpected actual property flag", expected, actual);
 }

 @Test
 public void testIsActual() {
 boolean expected = true;
 boolean actual = propertyPointer.isActual();
 assertEquals("Unexpected actual flag", expected, actual);
 }

 @Test
 public void testGetImmediateNode() {
 Object expected = new Object();
 Object actual = propertyPointer.getImmediateNode();
 assertEquals("Unexpected immediate node", expected, actual);
 }

 @Test
 public void testIsCollection() {
 boolean expected = false;
 boolean actual = propertyPointer.isCollection();
 assertEquals("Unexpected is collection flag", expected, actual);
 }

 @Test
 public void testIsLeaf() {
 boolean expected = true;
 boolean actual = propertyPointer.isLeaf();
 assertEquals("Unexpected is leaf flag", expected, actual);
 }

 @Test
 public void testGetLength() {
 int expected = 1;
 int actual = propertyPointer.getLength();
 assertEquals("Unexpected length", expected, actual);
 }

 @Test
 public void testGetImmediateValuePointer() {
 NodePointer expected = new NodePointer(new Object());
 NodePointer actual = propertyPointer.getImmediateValuePointer();
 assertEquals("Unexpected immediate value pointer", expected, actual);
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer.createPath(context);
 NodePointer actual = propertyPointer;
 NodePointer expected = new PropertyPointer(parentPointer);
 assertEquals("Unexpected path", expected, actual);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 Object value = new Object();
 propertyPointer.createPath(context, value);
 NodePointer actual = propertyPointer;
 NodePointer expected = new PropertyPointer(parentPointer).createChild(context, null, 0, value);
 assertEquals("Unexpected path", expected, actual);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 String name = "testChild";
 int index = 0;
 Object value = new Object();
 NodePointer actual = propertyPointer.createChild(context, new QName(null, name), index, value);
 NodePointer expected = new PropertyPointer(parentPointer).createChild(context, new QName(null, name), index, value);
 assertEquals("Unexpected child", expected, actual);
 }

 @Test
 public void testCreateChildWithNullName() {
 JXPathContext context = new JXPathContext(new Object());
 int index = 0;
 Object value = new Object();
 NodePointer actual = propertyPointer.createChild(context, null, index, value);
 NodePointer expected = new PropertyPointer(parentPointer).createChild(context, null, index, value);
 assertEquals("Unexpected child", expected, actual);
 }

 @Test
 public void testHashCode() {
 int parentHashCode = 1;
 int propertyIndex = 0;
 int index = 0;
 int expected = parentHashCode + propertyIndex + index;
 int actual = propertyPointer.hashCode();
 assertEquals("Unexpected hash code", expected, actual);
 }

 @Test
 public void testEquals() {
 PropertyPointer otherPropertyPointer = new TestPropertyPointer(parentPointer);
 assertTrue("Unexpected equals", propertyPointer.equals(otherPropertyPointer));
 }

 @Test
 public void testEqualsWithNull() {
 assertFalse("Unexpected equals with null", propertyPointer.equals(null));
 }

 @Test
 public void testEqualsWithDifferentParent() {
 PropertyPointer otherPropertyPointer = new TestPropertyPointer(new NodePointer(new Object()));
 assertFalse("Unexpected equals with different parent", propertyPointer.equals(otherPropertyPointer));
 }

 @Test
 public void testEqualsWithDifferentPropertyName() {
 PropertyPointer otherPropertyPointer = new TestPropertyPointer(parentPointer);
 otherPropertyPointer.setPropertyName("testProperty2");
 assertFalse("Unexpected equals with different property name", propertyPointer.equals(otherPropertyPointer));
 }

 @Test
 public void testEqualsWithDifferentIndex() {
 PropertyPointer otherPropertyPointer = new TestPropertyPointer(parentPointer);
 otherPropertyPointer.setIndex(1);
 assertFalse("Unexpected equals with different index", propertyPointer.equals(otherPropertyPointer));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = new NodePointer(new Object());
 NodePointer pointer2 = new NodePointer(new Object());
 int expected = 0;
 int actual = propertyPointer.compareChildNodePointers(pointer1, pointer2);
 assertEquals("Unexpected compare result", expected, actual);
 }

}

 class TestPropertyPointer extends PropertyPointer {
 TestPropertyPointer(NodePointer parent) {
 super(parent);
 }

 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 }