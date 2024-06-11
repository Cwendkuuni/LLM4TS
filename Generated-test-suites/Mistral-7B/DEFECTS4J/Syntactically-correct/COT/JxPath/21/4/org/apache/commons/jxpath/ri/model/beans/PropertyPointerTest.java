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
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = AssertionError.class)
 public void testSetPropertyIndexNegativeNumber() {
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
 propertyPointer.setPropertyName("propertyName");
 assertEquals("propertyName", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("propertyName");
 assertEquals("propertyName", propertyPointer.getPropertyName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyName() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("propertyName");
 try {
 propertyPointer.setPropertyName("newPropertyName");
 } catch (UnsupportedOperationException e) {
 throw new AssertionError("Should throw UnsupportedOperationException", e);
 }
 }

 @Test
 public void testGetPropertyCount() {
 throw new UnsupportedOperationException("Method getPropertyCount() is not supported.");
 }

 @Test
 public void testGetPropertyNames() {
 throw new UnsupportedOperationException("Method getPropertyNames() is not supported.");
 }

 @Test
 public void testIsActualProperty() {
 boolean result = propertyPointer.isActualProperty();
 assertFalse(result);
 }

 @Test
 public void testIsActual() {
 propertyPointer = new PropertyPointer(null);
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setImmediateNode(new Object());
 assertEquals(propertyPointer.getImmediateNode(), new Object());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setImmediateNode(new Object()[1]);
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setImmediateNode(new String());
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setImmediateNode(new Object[3]);
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setImmediateNode(new Object());
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(nodePointer);
 }

 @Test
 public void testCreatePath() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 propertyPointer.createPath(context);
 assertEquals(object, propertyPointer.getBean());
 assertEquals("propertyName", propertyPointer.getPropertyName());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidFactory() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 AbstractFactory factory = null;
 propertyPointer.createPath(context, factory);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullValue() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 propertyPointer.createPath(context, null);
 }

 @Test
 public void testCreatePathWithValidValue() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 Object value = new Object();
 propertyPointer.createPath(context, value);
 assertEquals(value, propertyPointer.getImmediateNode());
 }

 @Test
 public void testCreateChild() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 NodePointer child = propertyPointer.createChild(context, null, 0, new Object());
 assertNotNull(child);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithNullName() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 propertyPointer.createChild(context, null, 0, new Object());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreateChildWithIndexOutOfBounds() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 propertyPointer.createPath(context);
 propertyPointer.setIndex(3);
 propertyPointer.createChild(context, null, 3, new Object());
 }

 @Test
 public void testCreateChildWithValidValues() {
 propertyPointer = new PropertyPointer(null);
 JXPathContext context = new JXPathContext();
 Object object = new Object();
 propertyPointer.setBean(object);
 propertyPointer.setPropertyName("propertyName");
 NodePointer child1 = propertyPointer.createChild(context, null, 0, new Object());
 NodePointer child2 = propertyPointer.createChild(context, null, 1, new Object());
 assertEquals(2, propertyPointer.getLength());
 }

}