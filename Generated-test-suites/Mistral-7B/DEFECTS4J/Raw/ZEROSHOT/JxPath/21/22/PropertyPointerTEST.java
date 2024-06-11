import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

 @Test
 public void testBasicConstruction() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertEquals(Integer.MIN_VALUE, propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testPropertyIndexSetterAndGetter() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyIndexNegativeValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public int getPropertyCount() {
 return 1;
 }
 };
 assertEquals(1, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2"};
 }
 };
 assertArrayEquals(new String[]{"testProperty1", "testProperty2"}, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){};
 }
 };
 propertyPointer.setPropertyIndex(0);
 Object value = new Object(){};
 propertyPointer.setValue(value);
 assertEquals(value, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object(), new Object()};
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){};
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object(), new Object(), new Object()};
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){};
 }
 };
 PropertyPointer valuePropertyPointer = new PropertyPointer("testValue") {
 @Override
 public Object getBaseValue() {
 return new Object(){};
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setValue(valuePropertyPointer);
 assertEquals(valuePropertyPointer, propertyPointer.getImmediateValuePointer());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){};
 }
 };
 Object bean = new Object(){};
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.createPath(null).getNode());
 }

 @Test
 public void testCreatePathValue() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object()};
 }
 };
 Object[] values = new Object[]{new Object(), new Object()};
 propertyPointer.setIndex(1);
 propertyPointer.createPath(null, values[1]);
 assertSame(values[1], propertyPointer.getNode());
 }

 @Test
 public void testCreateChild() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object()};
 }
 };
 Object bean = new Object(){};
 propertyPointer.setBean(bean);
 PropertyPointer childPropertyPointer = new PropertyPointer("testChildProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object()};
 }
 };
 propertyPointer.createChild(null, null, 0, childPropertyPointer);
 assertEquals(childPropertyPointer, propertyPointer.getChild(0));
 }

 @Test
 public void testCreateChildWithName() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object()};
 }
 };
 Object bean = new Object(){};
 PropertyPointer childPropertyPointer = new PropertyPointer("testChildProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object()};
 }
 };
 propertyPointer.setBean(bean);
 propertyPointer.createChild(null, new QName("testName"), 0, childPropertyPointer);
 assertEquals("testName", propertyPointer.getChild(0).getName().getLocalPart());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer("testProperty2") {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertFalse(propertyPointer1.hashCode() == propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer("testProperty2") {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 PropertyPointer propertyPointer4 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer5 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer6 = new PropertyPointer("testProperty2") {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 assertTrue(propertyPointer1.equals(propertyPointer4));
 assertFalse(propertyPointer1.equals(propertyPointer2));
 assertFalse(propertyPointer1.equals(propertyPointer3));
 assertFalse(propertyPointer1.equals(propertyPointer5));
 assertFalse(propertyPointer1.equals(propertyPointer6));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer("testProperty2") {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 NodePointer nodePointer1 = new NodePointer("testNodePointer1") {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 NodePointer nodePointer2 = new NodePointer("testNodePointer2") {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer1));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2));
 assertEquals(1, propertyPointer1.compareChildNodePointers(nodePointer2, nodePointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer2));
 }
}