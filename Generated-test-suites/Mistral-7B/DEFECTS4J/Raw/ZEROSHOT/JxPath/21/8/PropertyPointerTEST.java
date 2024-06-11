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
 parentPointer = new NodePointer(null);
 propertyPointer = new MyPropertyPointer(parentPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
 int actual = propertyPointer.getPropertyIndex();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetPropertyIndex() {
 int index = 5;
 propertyPointer.setPropertyIndex(index);
 int actual = propertyPointer.getPropertyIndex();
 assertEquals(index, actual);
 }

 @Test
 public void testGetBean() {
 Object expected = new Object();
 propertyPointer.setBean(expected);
 Object actual = propertyPointer.getBean();
 assertEquals(expected, actual);
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 context.setContextNode(new Object());
 PropertyPointer property = propertyPointer.createPath(context);
 assertNotNull(property);
 assertEquals(propertyPointer.getClass(), property.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullContext() {
 JXPathContext context = null;
 propertyPointer.createPath(context);
 }

 @Test(expected = NullPointerException.class)
 public void testCreatePathWithNullValue() {
 Object value = null;
 propertyPointer.createPath(new JXPathContext(), value);
 }

 @Test
 public void testIsActual() {
 boolean actual = propertyPointer.isActual();
 assertTrue(actual);
 }

 @Test
 public void testGetImmediateNode() {
 Object expected = new Object();
 propertyPointer.setValue(expected);
 Object actual = propertyPointer.getImmediateNode();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCollection() {
 boolean actual = propertyPointer.isCollection();
 assertFalse(actual);
 }

 @Test
 public void testGetLength() {
 Object value = new Object()[5];
 propertyPointer.setValue(value);
 int actual = propertyPointer.getLength();
 assertEquals(5, actual);
 }

 @Test
 public void testGetImmediateValuePointer() {
 NodePointer expected = new NodePointer(null);
 NodePointer actual = propertyPointer.getImmediateValuePointer();
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 propertyPointer.createPath(context);
 String name = "testProperty";
 int index = 0;
 Object value = new Object();
 NodePointer child = propertyPointer.createChild(context, new QName(null, name), index, value);
 assertNotNull(child);
 assertEquals(PropertyPointer.class, child.getClass());
 }

 @Test
 public void testHashCode() {
 int expected = parentPointer.hashCode() + PropertyPointer.UNSPECIFIED_PROPERTY + 5;
 int actual = propertyPointer.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 PropertyPointer other = new MyPropertyPointer(parentPointer);
 assertTrue(propertyPointer.equals(other));
 }

 @Test
 public void testNotEquals() {
 Object other = new Object();
 assertFalse(propertyPointer.equals(other));
 }

 @Test
 public void testNotEqualsDifferentParent() {
 PropertyPointer other = new MyPropertyPointer(new NodePointer(null));
 assertFalse(propertyPointer.equals(other));
 }

 @Test
 public void testNotEqualsDifferentNames() {
 PropertyPointer other = new MyPropertyPointer(parentPointer);
 other.setPropertyName("differentName");
 assertFalse(propertyPointer.equals(other));
 }

 @Test
 public void testNotEqualsDifferentIndices() {
 PropertyPointer other = new MyPropertyPointer(parentPointer);
 other.setIndex(6);
 assertFalse(propertyPointer.equals(other));
 }
}

class MyPropertyPointer extends PropertyPointer {

 public MyPropertyPointer(NodePointer parent) {
 super(parent);
 }

 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 5;
 }

 @Override
 public String[] getPropertyNames() {
 String[] names = new String[5];
 for (int i = 0; i < 5; i++) {
 names[i] = "testProperty" + i;
 }
 return names;
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
}