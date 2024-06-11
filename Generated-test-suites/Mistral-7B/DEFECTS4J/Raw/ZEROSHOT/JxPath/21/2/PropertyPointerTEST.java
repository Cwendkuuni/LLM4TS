import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstruction() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(new Object());
 assertEquals(propertyPointer.getBean(), propertyPointer.getBean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 assertEquals(new QName("testProperty"), propertyPointer.getName());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public int getPropertyCount() {
 return 3;
 }
 };
 assertEquals(3, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2", "testProperty3"};
 }
 };
 assertEquals(new String[]{"testProperty1", "testProperty2", "testProperty3"}, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object getProperty(String name) {
 switch (name) {
 case "testProperty":
 return "testValue";
 default:
 return null;
 }
 }
 };
 }
 };
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals("testValue", nodePointer.getNode().toString());
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object getProperty(String name) {
 switch (name) {
 case "testProperty":
 return new Object();
 default:
 return null;
 }
 }
 };
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(null, new Object());
 assertEquals(propertyPointer, nodePointer);
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object getProperty(String name) {
 switch (name) {
 case "testProperty":
 return new Object[]{"testValue1", "testValue2"};
 default:
 return null;
 }
 }
 };
 }
 };
 Object node = propertyPointer.getImmediateNode();
 assertEquals(new Object[]{"testValue1", "testValue2"}, node);
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public Object getProperty(String name) {
 switch (name) {
 case "testProperty":
 return new Object[]{1, 2, 3};
 default:
 return null;
 }
 }
 };
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new String("testString");
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {return new Object[]{1, 2, 3};}
 };
 assertEquals(3, propertyPointer.getLength());
 }
}