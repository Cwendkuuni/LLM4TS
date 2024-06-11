import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstructor() {
 propertyPointer = new PropertyPointer(null);
 Assert.assertNull(propertyPointer.getBean());
 Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(5);
 Assert.assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexNegativeNumber() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setBean(new Object());
 Assert.assertEquals(new Object(), propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 Assert.assertEquals(new QName(null, "testProperty"), propertyPointer.getName());
 }

 @Test
 public void testIsActualProperty() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 Assert.assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActual() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 protected boolean isActualProperty() {
 return false;
 }
 };
 Assert.assertFalse(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
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
 Assert.assertNotNull(propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public Object getBaseValue() {
 return new Object()[5];
 }
 };
 Assert.assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public Object getBaseValue() {
 return "testString";
 }
 };
 Assert.assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3, 4, 5};
 }
 };
 Assert.assertEquals(5, propertyPointer.getLength());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLengthNegativeIndex() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3, 4, 5};
 }
 };
 propertyPointer.setIndex(-1);
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
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
 Assert.assertNotNull(nodePointer);
 }

 @Test
 public void testCreatePath() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
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
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nodePointer = propertyPointer.createPath(context);
 Assert.assertNotNull(nodePointer);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathFailed() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public Object getBaseValue() {
 return null;
 }
 };
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nodePointer = propertyPointer.createPath(context);
 }

 @Test
 public void testCreateChild() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
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
 JXPathContext context = new JXPathContext(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("testChild"), 0, new Object());
 Assert.assertNotNull(childNodePointer);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreateChildFailed() {
 propertyPointer = new PropertyPointer(new NodePointer(null)) {
 @Override
 public Object getBaseValue() {
 return null;
 }
 };
 propertyPointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("testChild"), 0, new Object());
 }
}