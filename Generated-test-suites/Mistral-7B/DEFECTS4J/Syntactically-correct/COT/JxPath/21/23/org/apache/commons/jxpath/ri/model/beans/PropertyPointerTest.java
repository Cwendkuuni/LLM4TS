import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstructor() {
 propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyIndex(2);
 assertEquals(2, propertyPointer.getPropertyIndex());
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
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyName("newTestProperty");
 }

 @Test
 public void testGetPropertyCount() {
 propertyPointer = new PropertyPointer() {
 @Override
 public int getPropertyCount() {
 return 3;
 }
 };
 assertEquals(3, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 propertyPointer = new PropertyPointer() {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2", "testProperty3"};
 }
 };
 assertEquals("testProperty1", propertyPointer.getPropertyNames()[0]);
 assertEquals("testProperty2", propertyPointer.getPropertyNames()[1]);
 assertEquals("testProperty3", propertyPointer.getPropertyNames()[2]);
 }

 @Test
 public void testIsActualProperty() {
 propertyPointer = new PropertyPointer() {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 @Override
 public Object getValue() {
 return 5;
 }
 };
 assertEquals(5, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getNode() {
 return 5;
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 @Override
 public Object getValue() {
 return 5;
 }
 };
 NodePointer expected = NodePointer.newChildNodePointer(
 propertyPointer,
 "testProperty",
 5);
 NodePointer actual = propertyPointer.getImmediateValuePointer();
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetLengthOutOfBounds() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 propertyPointer.setIndex(4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullContext() {
 propertyPointer.createPath(null);
 }

 @Test
 public void testCreatePath() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = null;
 propertyPointer.createPath(context);
 assertEquals(propertyPointer, propertyPointer.getImmediateParentPointer().getChildByIndex(0));
 }

 @Test
 public void testCreatePathWithValue() {
 Object testObject = new Object();
 Object testValue = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = null;
 propertyPointer.createPath(context, testValue);
 assertEquals(testValue, propertyPointer.getValue());
 }

 @Test
 public void testCreateChild() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 NodePointer childPointer = NodePointer.newChildNodePointer(propertyPointer, "testChildProperty", 1, new Object());
 assertEquals(1, childPointer.getIndex());
 assertEquals("testChildProperty", childPointer.getName().getLocalPart());
 assertEquals(new Object(), childPointer.getNode());
 }

 @Test
 public void testCreateChildWithNullContext() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 QName name = new QName(null, "testChildProperty");
 NodePointer expected = NodePointer.newChildNodePointer(
 propertyPointer,
 name,
 1,
 new Object());
 NodePointer actual = propertyPointer.createChild(null, name, 1, new Object());
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateChildWithNullName() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 NodePointer actual = propertyPointer.createChild(null, null, 0, null);
 assertNull(actual);
 }

 @Test
 public void testCreateChildWithNegativeIndex() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 QName name = new QName(null, "testChildProperty");
 try {
 propertyPointer.createChild(null, name, -1, null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreateChildWithNullValue() {
 Object testObject = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 QName name = new QName(null, "testChildProperty");
 NodePointer actual = propertyPointer.createChild(null, name, 0, null);
 assertNull(actual);
 }

 @Test
 public void testHashCode() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 assertEquals(pointer1.hashCode(), pointer2.hashCode());
 propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setIndex(1);
 PropertyPointer pointer3 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 pointer3.setIndex(2);
 assertEquals(propertyPointer.hashCode(), pointer3.hashCode());
 propertyPointer.setIndex(3);
 pointer3.setIndex(1);
 assertNotEquals(propertyPointer.hashCode(), pointer3.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 assertFalse(pointer1.equals(pointer2));
 PropertyPointer pointer3 = new PropertyPointer(null);
 pointer1.setIndex(1);
 pointer3.setIndex(1);
 assertTrue(pointer1.equals(pointer3));
 PropertyPointer pointer4 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 pointer1.setPropertyName("testProperty");
 pointer4.setPropertyName("testProperty2");
 assertFalse(pointer1.equals(pointer4));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 assertEquals(0, pointer1.compareChildNodePointers(pointer1, pointer2));
 assertEquals(0, pointer1.compareChildNodePointers(pointer2, pointer1));
 PropertyPointer pointer3 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 PropertyPointer pointer4 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 pointer3.setIndex(1);
 pointer4.setIndex(2);
 assertEquals(-1, pointer3.compareChildNodePointers(pointer3, pointer4));
 assertEquals(1, pointer3.compareChildNodePointers(pointer4, pointer3));
 }

}