import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNonPropertyOwnerParent() {
 NodePointer nonPropertyOwnerPointer = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerPointer);
 nullPropertyPointer.setValue(new Object());
 }

 @Test
 public void testSetValuePropertyOwnerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 nullPropertyPointer.setValue(new Object());
 assertNotNull(propertyOwnerPointer.getPropertyPointer());
 assertEquals(propertyOwnerPointer.getPropertyName(), nullPropertyPointer.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.createPath(new JXPathContext());
 }

 @Test
 public void testCreatePathNonPropertyOwnerParent() {
 NodePointer nonPropertyOwnerPointer = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerPointer);
 nullPropertyPointer.createPath(new JXPathContext());
 }

 @Test
 public void testCreatePathPropertyOwnerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer nodePointer = nullPropertyPointer.createPath(new JXPathContext());
 assertNotNull(nodePointer);
 }

 @Test
 public void testCreateChildNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
 }

 @Test
 public void testCreateChildPropertyOwnerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer nodePointer = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
 assertNotNull(nodePointer);
 }

 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("test");
 assertEquals("test", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("test", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertEquals(0, propertyNames.length);
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("test");
 assertEquals("/test", nullPropertyPointer.asPath());
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("/..[@name='test']", nullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
 String string1 = "hello'world";
 String string2 = NullPropertyPointer.escape(string1);
 assertEquals("hello&#039;world", string2);
 String string3 = "hello\"world";
 String string4 = NullPropertyPointer.escape(string3);
 assertEquals("hello&quot;world", string4);
 }
}