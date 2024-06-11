import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstruction() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 assertNull(propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testSetPropertyIndex() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(new Object());
 assertEquals(propertyPointer.getBean(), new Object());
 }

 @Test
 public void testGetName() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyName("testProperty");
 propertyPointer.setPropertyName("newPropertyName");
 assertEquals("newPropertyName", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyCount() {
 return 3;
 }
 };
 assertEquals(3, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"name1", "name2", "name3"};
 }
 };
 assertArrayEquals(new String[]{"name1", "name2", "name3"}, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(new Object());
 propertyPointer.setPropertyIndex(0);
 assertEquals(ValueUtils.getValue(new Object(), 0), propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public boolean isCollection() {
 return true;
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public boolean isLeaf() {
 return true;
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getLength() {
 return 4;
 }
 };
 assertEquals(4, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public NodePointer getValuePointer() {
 return new NodePointer(null);
 }
 };
 NodePointer childPointer = propertyPointer.getImmediateValuePointer();
 assertNull(childPointer.getNode());
 }

 @Test
 public void testCreatePath() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(new Object());
 propertyPointer.setPropertyName("testProperty");
 JXPathContext context = new JXPathContext();
 PropertyPointer result = propertyPointer.createPath(context);
 assertEquals(propertyPointer.getName(), result.getName());
 assertEquals(propertyPointer.getPropertyIndex(), result.getPropertyIndex());
 assertEquals(propertyPointer.getBean(), result.getNode());
 }

 @Test
 public void testCreatePathWithValue() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public NodePointer getValuePointer() {
 return new NodePointer(null);
 }
 };
 propertyPointer.setBean(new Object());
 propertyPointer.setPropertyName("testProperty");
 JXPathContext context = new JXPathContext();
 Object value = new Object();
 propertyPointer.createPath(context, value);
 assertEquals(propertyPointer.getName(), ((PropertyPointer)result).getPropertyName());
 assertEquals(propertyPointer.getPropertyIndex(), ((PropertyPointer)result).getPropertyIndex());
 assertEquals(propertyPointer.getBean(), ((PropertyPointer)result).getBean());
 assertEquals(value, ((PropertyPointer)result).getValue());
 }

 @Test
 public void testCreateChild() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return new PropertyPointer(this) {
 @Override
 public NodePointer getValuePointer() {
 return new NodePointer(null);
 }
 };
 }
 };
 PropertyPointer childPointer = propertyPointer.createChild(new JXPathContext(), null, 0, null);
 assertNotNull(childPointer);
 assertNull(childPointer.getNode());
 }
}