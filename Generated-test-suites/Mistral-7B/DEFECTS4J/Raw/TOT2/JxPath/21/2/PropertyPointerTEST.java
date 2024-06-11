import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 assertEquals(0, propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyIndex(2);
 assertEquals(2, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testSetBeanNull() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setBean(null);
 }

 @Test
 public void testGetName() {
 String propertyName = "testProperty";
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(null), propertyName);
 assertEquals(new QName(null, propertyName), propertyPointer.getName());
 }

 @Test
 public void testSetPropertyName() {
 String propertyName = "testProperty";
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(null), null);
 propertyPointer.setPropertyName(propertyName);
 assertEquals(propertyName, propertyPointer.getPropertyName());
 }

 @Test(expected = java.lang.NullPointerException.class)
 public void testSetPropertyNameNull() {
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(null), null);
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(null), null);
 assertEquals(-1, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = new String[]{"testProperty1", "testProperty2"};
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(null), null);
 propertyPointer.setPropertyNames(propertyNames);
 assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 boolean isActual = true;
 PropertyPointer propertyPointer = new TestActualPropertyPointer(new NodePointer(null), isActual);
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualPropertyFalse() {
 boolean isActual = false;
 PropertyPointer propertyPointer = new TestActualPropertyPointer(new NodePointer(null), isActual);
 assertFalse(propertyPointer.isActualProperty());
 }

 @Test(expected = java.lang.AbstractMethodError.class)
 public void testAbstractMethodGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.getImmediateNode();
 }

 @Test
 public void testIsCollection() {
 Object collection = new Object()[3];
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setBaseValue(collection);
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 Object leaf = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setNode(leaf);
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 Object collection = new Object[3];
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setBaseValue(collection);
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 Object value = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setValue(value);
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(propertyPointer, immediateValuePointer.getParent());
 assertEquals(new QName(null, propertyPointer.getPropertyName()), immediateValuePointer.getName());
 assertEquals(value, immediateValuePointer.getNode());
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testCreatePathWithInvalidContext() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 AbstractFactory factory = new AbstractFactory() {};
 assertNull(propertyPointer.createPath(null, factory));
 }

 @Test
 public void testCreatePath() {
 Object bean = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 AbstractFactory factory = new AbstractFactory() {
 @Override public Object createObject(
 JXPathContext context,
 NodePointer pointer,
 Object parent,
 String name,
 int index) {
 return parent;
 }
 };
 propertyPointer.setBean(bean);
 PropertyPointer result = propertyPointer.createPath(new JXPathContext() {});
 assertEquals(bean, result.getBean());
 }

 @Test
 public void testGetAbstractFactory() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 assertEquals(AbstractFactory.getDefaultInstance(), propertyPointer.getAbstractFactory());
 }

 @Test
 public void testCreatePathWithValue() {
 Object value = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.createPath(new JXPathContext() {});
 propertyPointer.setValue(value);
 assertEquals(value, propertyPointer.getNode());
 }

 @Test
 public void testCreateChild() {
 Object childValue = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.createPath(new JXPathContext() {});
 NodePointer child = propertyPointer.createChild(new JXPathContext() {}, new QName(null, "testChild"), 0, childValue);
 assertEquals(propertyPointer, child.getParent());
 assertEquals("testChild", child.getName().getLocalPart());
 assertEquals(childValue, child.getNode());
 }

 @Test
 public void testCreateChildWithName() {
 String childName = "testChild";
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.createPath(new JXPathContext() {});
 NodePointer child = propertyPointer.createChild(new JXPathContext() {}, new QName(null, childName), 0);
 assertEquals(propertyPointer, child.getParent());
 assertEquals(childName, child.getName().getLocalPart());
 assertEquals(-1, child.getLength());
 }

 @Test(expected = java.lang.IndexOutOfBoundsException.class)
 public void testCreateChildWithInvalidIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.createPath(new JXPathContext() {});
 NodePointer child = propertyPointer.createChild(new JXPathContext() {}, null, 1);
 }
}

class TestPropertyPointer extends PropertyPointer {
 public TestPropertyPointer(NodePointer parent, String propertyName) {
 super(parent);
 setPropertyName(propertyName);
 }
}

class TestActualPropertyPointer extends PropertyPointer {
 public TestActualPropertyPointer(NodePointer parent, boolean isActual) {
 super(parent);
 setActualProperty(isActual);
 }
}