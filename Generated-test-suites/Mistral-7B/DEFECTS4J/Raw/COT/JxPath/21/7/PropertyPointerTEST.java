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
 assertEquals(-1, propertyPointer.getPropertyIndex());
 assertEquals(-1, propertyPointer.getIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPropertyIndexWithInvalidIndex() {
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
 public void testSetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
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
 return new String[]{"name1", "name2", "name3"};
 }
 };
 assertEquals(3, propertyPointer.getPropertyNames().length);
 assertEquals("name1", propertyPointer.getPropertyNames()[0]);
 assertEquals("name2", propertyPointer.getPropertyNames()[1]);
 assertEquals("name3", propertyPointer.getPropertyNames()[2]);
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
 propertyPointer = new PropertyPointer() {
 @Override
 protected boolean isActualProperty() {
 return false;
 }
 };
 assertFalse(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object()[1];
 }
 };
 assertEquals(1, propertyPointer.getImmediateNode().hashCode());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[3];
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new String("test");
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

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetImmediateValuePointerWithInvalidIndex() {
 propertyPointer = new PropertyPointer() {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 propertyPointer.setIndex(4);
 propertyPointer.getImmediateValuePointer();
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 PropertyPointer result = (PropertyPointer) propertyPointer.createPath(context);
 assertEquals(3, result.getPropertyCount());
 assertEquals("test[1]", result.asPath());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithException() {
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return null;
 }
 };
 propertyPointer.createPath(context);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 PropertyPointer child1 = (PropertyPointer) propertyPointer.createChild(context, null, 1, null);
 PropertyPointer child2 = (PropertyPointer) propertyPointer.createChild(context, null, 2, null);
 PropertyPointer child3 = (PropertyPointer) propertyPointer.createChild(context, null, 3, null);
 assertEquals(4, propertyPointer.getPropertyCount());
 assertEquals("test[1][1]", child1.asPath());
 assertEquals("test[1][2]", child2.asPath());
 assertEquals("test[1][3]", child3.asPath());
 }
}