import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("PropertyName should be set to *", "*", nullPropertyPointer.getPropertyName());
 assertFalse("By name attribute should be false", nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testGetNameMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 QName qname = nullPropertyPointer.getName();
 assertEquals("Expected QName to be equal to propertyName", "*", qname.getLocalPart());
 }

 @Test
 public void testSetPropertyNameMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("Expected propertyName to be set to testProperty", "testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValueMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 assertEquals("Expected propertyName to be set to testAttribute", "testAttribute", nullPropertyPointer.getPropertyName());
 assertTrue("Expected byNameAttribute to be true", nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollectionMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected isCollection to be false", nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCountMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected propertyCount to be 0", 0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNamesMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals("Expected propertyNames to be empty", new String[0], propertyNames);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreatePathMethodWithContextThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createPath(context);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreatePathMethodWithContextValueThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createPath(context, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildMethodWithContextThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("test"), 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildMethodWithContextValueThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("test"), 0, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildMethodWithContextNameThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("test"), 0, "");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildMethodWithContextIndexThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("test"), -1, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildMethodWithContextNameValueThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("test"), 0, "value");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetValueMethodThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 }
}