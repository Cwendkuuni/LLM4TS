import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testInstance() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNotNull(nullPropertyPointer);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 fail("Should have thrown JXPathInvalidAccessException");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNonContainerParent() {
 NodePointer nonContainerPointer = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonContainerPointer);
 nullPropertyPointer.setValue(new Object());
 fail("Should have thrown JXPathInvalidAccessException");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 try {
 nullPropertyPointer.createPath(new JXPathContext());
 fail("Should have thrown JXPathInvalidAccessException");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object[0]);
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(new Object[0]));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNotNull(result);
 assertEquals(propertyOwnerPointer, result.getParent());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 assertEquals(true, nullPropertyPointer.isByNameAttribute());
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
 String[] expected = new String[0];
 assertArrayEquals(expected, nullPropertyPointer.getPropertyNames());
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("", nullPropertyPointer.asPath());
 NullPropertyPointer nameAttributeNullPropertyPointer = new NullPropertyPointer(null);
 nameAttributeNullPropertyPointer.setNameAttributeValue("test");
 assertEquals("[@name='test']", nameAttributeNullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
 String string = "A'B\"C";
 String expected = "A&apos;B&quot;C";
 assertEquals(expected, NullPropertyPointer.escape(string));
 }
}