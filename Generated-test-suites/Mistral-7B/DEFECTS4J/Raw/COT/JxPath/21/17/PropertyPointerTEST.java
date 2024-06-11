import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstructor() {
 propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getName());
 assertNull(propertyPointer.getValue());
 }

 @Test
 public void testSetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertNotNull(propertyPointer.getName());
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyNameWithNull() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testGetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(5);
 assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexWithNegativeValue() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(new Object());
 assertEquals(propertyPointer.getBean(), propertyPointer.getBean());
 }

 @Test
 public void testIsActualProperty() {
 propertyPointer = new AbstractPropertyPointer() {};
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 propertyPointer = new AbstractPropertyPointer() {};
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object get(int index) {
 return new Object();
 }
 };
 }
 };
 propertyPointer.setPropertyIndex(0);
 assertEquals(propertyPointer.getImmediateNode(), propertyPointer.getBaseValue().get(0));
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[] { new Object(), new Object() };
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new String("test");
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[] { new Object(), new Object(), new Object() };
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object get(int index) {
 return new Object();
 }
 };
 }
 };
 propertyPointer.setPropertyIndex(0);
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(nodePointer);
 }

 @Test
 public void testCreatePath() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object get(int index) {
 return new Object();
 }
 };
 }
 };
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertNotNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValue() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object get(int index) {
 return new Object();
 }
 };
 }
 };
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer.createPath(context, new Object());
 assertEquals(propertyPointer.getImmediateNode(), propertyPointer.getNode());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreatePathWithOutOfBoundsIndex() {
 propertyPointer = new AbstractPropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[] { new Object(), new Object() };
 }
 };
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer.createPath(context, 2);
 }

 @Test
 public void testClone() {
 PropertyPointer original = new AbstractPropertyPointer() {};
 PropertyPointer clone = (PropertyPointer) original.clone();
 assertNotNull(clone);
 assertNotSame(original, clone);
 assertNull(clone.getValue());
 assertEquals(original.getBean(), clone.getBean());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer2 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer3 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer4 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer5 = new AbstractPropertyPointer() {};
 assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
 assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertEquals(propertyPointer1.hashCode(), propertyPointer4.hashCode());
 assertEquals(propertyPointer1.hashCode(), propertyPointer5.hashCode());
 propertyPointer1.setPropertyName("testProperty");
 propertyPointer5.setPropertyName("testProperty");
 assertEquals(propertyPointer1.hashCode(), propertyPointer5.hashCode());
 propertyPointer1.setIndex(1);
 propertyPointer5.setIndex(2);
 assertNotEquals(propertyPointer1.hashCode(), propertyPointer5.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer2 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer3 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer4 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer5 = new AbstractPropertyPointer() {};
 assertEquals(propertyPointer1, propertyPointer1);
 assertEquals(propertyPointer1, propertyPointer2);
 assertFalse(propertyPointer1.equals(propertyPointer3));
 assertFalse(propertyPointer1.equals(propertyPointer4));
 assertFalse(propertyPointer1.equals(propertyPointer5));
 propertyPointer1.setPropertyName("testProperty");
 propertyPointer5.setPropertyName("testProperty");
 assertEquals(propertyPointer1, propertyPointer5);
 propertyPointer1.setIndex(1);
 propertyPointer5.setIndex(2);
 assertFalse(propertyPointer1.equals(propertyPointer5));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new AbstractPropertyPointer() {};
 PropertyPointer propertyPointer2 = new AbstractPropertyPointer() {};
 NodePointer nodePointer1 = new ChildNodePointer(null, null, 0, null);
 NodePointer nodePointer2 = new ChildNodePointer(null, null, 1, null);
 int comparisonResult = propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2);
 assertLessThan(0, comparisonResult);
 }

}