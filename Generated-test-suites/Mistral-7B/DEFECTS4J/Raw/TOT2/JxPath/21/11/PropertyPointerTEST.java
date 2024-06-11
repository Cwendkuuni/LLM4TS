import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.SimpleBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

 private SimpleBean simpleBean;
 private PropertyPointer propertyPointer;

 @Before
 public void setUp() {
 simpleBean = new SimpleBean();
 simpleBean.setName("John Doe");
 simpleBean.setAge(30);
 simpleBean.setList(Arrays.asList("apple", "banana", "orange"));
 propertyPointer = new PropertyPointer(new NodePointer(new SimpleBeanPointer(new NodePointer(null), simpleBean))) {
 @Override
 public String getPropertyName() {
 return "age";
 }
 };
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null), null, 0);
 Assert.assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 propertyPointer = new PropertyPointer(new NodePointer(null), null, 1);
 Assert.fail("should not be instantiated with an index");
 propertyPointer = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 0);
 Assert.assertEquals(0, propertyPointer.getPropertyIndex());
 propertyPointer = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 1);
 Assert.assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Assert.assertEquals(simpleBean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 Assert.assertEquals(new QName("", "age"), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyName() {
 Assert.assertEquals("age", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 Assert.assertNotNull(propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] expected = {"name", "age", "list"};
 Assert.assertArrayEquals(expected, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 // let subclass implement this method
 }

 @Test
 public void testIsActual() {
 Assert.assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 Assert.assertEquals(30, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 Assert.assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 Assert.assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 Assert.assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer valuePropertyPointer = new PropertyPointer(propertyPointer, "list", 0) {
 @Override
 public String getPropertyName() {
 return "apple";
 }
 };
 NodePointer expected = new NodePointer(new SimpleBeanPointer(new NodePointer(new SimpleBeanPointer(new NodePointer(null), simpleBean), propertyPointer), valuePropertyPointer), "apple");
 Assert.assertEquals(expected, propertyPointer.getImmediateValuePointer());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidFactory() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null), null, 0) {
 @Override
 protected AbstractFactory getAbstractFactory(JXPathContext context) {
 throw new RuntimeException("not an instance of AbstractFactory");
 }
 };
 propertyPointer.createPath(null);
 }

 @Test
 public void testCreatePathWithValidFactory() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null), null, 0);
 // let subclass implement this method
 }

 @Test
 public void testCreatePathWithValue() {
 propertyPointer.createPath(null, 31);
 Assert.assertEquals(31, propertyPointer.getImmediateNode());
 }

 @Test
 public void testCreateChild() {
 PropertyPointer listPropertyPointer = new PropertyPointer(propertyPointer, "list", 1) {
 @Override
 public String getPropertyName() {
 return "orange";
 }
 };
 PropertyPointer newPropertyPointer = propertyPointer.createChild(null, new QName("", "newProperty"), 0, null);
 Assert.assertNotNull(newPropertyPointer);
 newPropertyPointer = propertyPointer.createChild(null, new QName("", "newProperty"), 1, null);
 Assert.assertNotNull(newPropertyPointer);
 newPropertyPointer = propertyPointer.createChild(null, null, 0, "newValue");
 Assert.assertEquals("newValue", newPropertyPointer.getImmediateNode());
 newPropertyPointer = propertyPointer.createChild(null, new QName("", "newProperty"), 1, "newValue");
 Assert.assertEquals("newValue", newPropertyPointer.getImmediateNode());
 newPropertyPointer = propertyPointer.createChild(null, new QName("", "newProperty"), 2, "newValue");
 Assert.assertEquals("newValue", newPropertyPointer.getImmediateNode());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 0);
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 1);
 NodePointer nodePointer1 = new NodePointer(new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), "age");
 NodePointer nodePointer2 = new NodePointer(new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), "name");
 Assert.assertEquals(nodePointer1.hashCode(), propertyPointer1.hashCode());
 Assert.assertEquals(nodePointer2.hashCode(), propertyPointer2.hashCode());
 Assert.assertNotEquals(nodePointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 0);
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 0);
 PropertyPointer propertyPointer3 = new PropertyPointer(new NodePointer(null), new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), 1);
 NodePointer nodePointer1 = new NodePointer(new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), "age");
 NodePointer nodePointer2 = new NodePointer(new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), "name");
 Assert.assertEquals(propertyPointer1, propertyPointer1);
 Assert.assertEquals(propertyPointer1, propertyPointer1);
 Assert.assertNotEquals(propertyPointer1, propertyPointer2);
 Assert.assertNotEquals(propertyPointer1, propertyPointer3);
 Assert.assertNotEquals(propertyPointer1, nodePointer1);
 Assert.assertNotEquals(propertyPointer1, nodePointer2);
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = new NodePointer(new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), "age");
 NodePointer nodePointer2 = new NodePointer(new SimpleBeanPointer(new NodePointer(null), new SimpleBean()), "name");
 Assert.assertEquals(0, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer1));
 Assert.assertEquals(-1, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2));
 Assert.assertEquals(-1, propertyPointer.compareChildNodePointers(nodePointer2, nodePointer1));
 Assert.assertEquals(-1, propertyPointer.compareChildNodePointers(nodePointer2, nodePointer2));
 }

}