import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 }

 @Test
 public void testGetPropertyIndex() {
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 fail("Method not yet implemented");
 }

 @Test
 public void testGetName() {
 assertEquals(new QName(null, "testProperty"), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyName() {
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 propertyPointer.setPropertyName("newTestProperty");
 assertEquals("newTestProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 assertEquals(1, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = propertyPointer.getPropertyNames();
 assertEquals(1, propertyNames.length);
 assertEquals("testProperty", propertyNames[0]);
 }

 @Test
 public void testIsActualProperty() {
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 fail("Method not yet implemented");
 }

 @Test
 public void testIsCollection() {
 fail("Method not yet implemented");
 }

 @Test
 public void testGetLength() {
 fail("Method not yet implemented");
 }

 @Test
 public void testGetImmediateValuePointer() {
 fail("Method not yet implemented");
 }

 @Test
 public void testCreatePath() {
 fail("Method not yet implemented");
 }

 @Test
 public void testCreatePathWithValue() {
 fail("Method not yet implemented");
 }

 @Test
 public void testCreateChild() {
 fail("Method not yet implemented");
 }

 @Test
 public void testCreateChildWithName() {
 fail("Method not yet implemented");
 }

 @Test
 public void testCreateChildWithNameAndIndex() {
 fail("Method not yet implemented");
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 assertEquals(propertyPointer.hashCode(), propertyPointer.hashCode());
 assertNotEquals(propertyPointer.hashCode(), propertyPointer1.hashCode());
 assertNotEquals(propertyPointer.hashCode(), propertyPointer2.hashCode());
 assertNotEquals(propertyPointer.hashCode(), propertyPointer3.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 assertTrue(propertyPointer.equals(propertyPointer));
 assertTrue(propertyPointer1.equals(propertyPointer));
 assertFalse(propertyPointer.equals(propertyPointer2));
 assertFalse(propertyPointer.equals(propertyPointer3));
 }

 @Test
 public void testCompareChildNodePointers() {
 fail("Method not yet implemented");
 }
}