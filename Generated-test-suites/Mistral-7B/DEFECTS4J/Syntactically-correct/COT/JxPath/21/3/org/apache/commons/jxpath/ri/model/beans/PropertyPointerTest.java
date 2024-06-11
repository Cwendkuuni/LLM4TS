import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private AbstractPropertyPointer abstractPropertyPointer;

 @Test
 public void testPropertyPointerConstructor() {
 propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testSetGetPropertyIndex() {
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetGetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 String propertyName = "name";
 propertyPointer = new AbstractPropertyPointer(null, propertyName) {
 @Override
 public String getPropertyName() {
 return propertyName;
 }
 };
 assertEquals(new QName(null, propertyName), propertyPointer.getName());
 }

 @Test
 public void testSetgetPropertyName() {
 String oldPropertyName = "oldName";
 String newPropertyName = "newName";
 propertyPointer = new AbstractPropertyPointer(null, oldPropertyName) {
 @Override
 public void setPropertyName(String propertyName) {
 this.propertyName = propertyName;
 }
 };
 propertyPointer.setPropertyName(newPropertyName);
 assertEquals(new QName(null, newPropertyName), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyCount() {
 abstractPropertyPointer = new AbstractPropertyPointer(null, null) {
 @Override
 public int getPropertyCount() {
 return 5;
 }
 };
 assertEquals(5, abstractPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 abstractPropertyPointer = new AbstractPropertyPointer(null, null) {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"name1", "name2", "name3", "name4", "name5"};
 }
 };
 assertArrayEquals(new String[]{"name1", "name2", "name3", "name4", "name5"}, abstractPropertyPointer.getPropertyNames());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsActualPropertyUnsupported() {
 propertyPointer = new PropertyPointer(null);
 try {
 propertyPointer.isActualProperty();
 } catch (Error e) {
 throw new UnsupportedOperationException("Should not go through error path", e);
 }
 throw new UnsupportedOperationException("isActualProperty() should be unsupported");
 }

 @Test
 public void testIsActualPropertyTrue() {
 propertyPointer = new PropertyPointer(null) {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActualFalse() {
 propertyPointer = new PropertyPointer(null) {
 @Override
 protected boolean isActualProperty() {
 return false;
 }
 };
 assertFalse(propertyPointer.isActual());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetImmediateNodeUnsupported() {
 propertyPointer = new PropertyPointer(null);
 try {
 propertyPointer.getImmediateNode();
 } catch (Error e) {
 throw new UnsupportedOperationException("Should not go through error path", e);
 }
 throw new UnsupportedOperationException("getImmediateNode() should be unsupported");
 }

 @Test
 public void testGetBaseValue() {
 Object baseValue = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return baseValue;
 }
 };
 assertEquals(baseValue, propertyPointer.getBaseValue());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer(null);
 assertFalse(propertyPointer.isCollection());
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {return new Object()[0];}
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {return null;}
 };
 assertTrue(propertyPointer.isLeaf());
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {return new Object();}
 };
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 abstractPropertyPointer = new AbstractPropertyPointer(null, null) {
 @Override
 public int getPropertyCount() {return 3;}
 @Override
 public int getLength() {return 7;}
 };
 assertEquals(7, abstractPropertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new PropertyPointer(null);
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(nodePointer);
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 context.setAbstractFactory(new AbstractFactory());
 propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 String propertyName = "property";
 propertyPointer.setPropertyName(propertyName);
 propertyPointer.setIndex(1);
 propertyPointer.createPath(context);
 assertNotNull(propertyPointer.getNode());
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext();
 context.setAbstractFactory(new AbstractFactory());
 propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 String propertyName = "property";
 propertyPointer.setPropertyName(propertyName);
 propertyPointer.createPath(context, new Object());
 assertNotNull(propertyPointer.getNode());
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 context.setAbstractFactory(new AbstractFactory());
 propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 String propertyName = "property";
 propertyPointer.setPropertyName(propertyName);
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, null);
 assertNotNull(childNodePointer);
 }

 @Test
 public void testCreateChildWithName() {
 JXPathContext context = new JXPathContext();
 context.setAbstractFactory(new AbstractFactory());
 propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 String propertyName = "property";
 propertyPointer.setPropertyName(propertyName);
 String childPropertyName = "childProperty";
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName(null, childPropertyName), 1, null);
 assertNotNull(childNodePointer);
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 1;}
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 2;}
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 1;}
 };
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 1;}
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 2;}
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 1;}
 };
 assertEquals(propertyPointer1, propertyPointer3);
 assertNotEquals(propertyPointer1, propertyPointer2);
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 1;}
 };
 NodePointer nodePointer2 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 1;}
 };
 NodePointer nodePointer3 = new PropertyPointer(null) {
 @Override
 public int getPropertyIndex() {return 2;}
 };
 assertEquals(nodePointer1.compareChildNodePointers(nodePointer1, nodePointer2), 0);
 assertEquals(nodePointer1.compareChildNodePointers(nodePointer1, nodePointer3), -1);
 assertEquals(nodePointer3.compareChildNodePointers(nodePointer1, nodePointer3), 1);
 }

}