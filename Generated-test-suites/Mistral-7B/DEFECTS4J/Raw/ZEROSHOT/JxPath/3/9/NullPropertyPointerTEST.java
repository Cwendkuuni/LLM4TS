import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class NullPropertyPointerTest {

 @Test
 public void testInstance() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected instance of NullPropertyPointer", nullPropertyPointer.getClass(), NullPropertyPointer.class);
 }

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 QName qname = nullPropertyPointer.getName();
 assertEquals("Expected instance of QName", "testProperty", qname.getLocalPart());
 }

 @Test
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertThrows("Expected JXPathInvalidAccessException", JXPathInvalidAccessException.class, () -> nullPropertyPointer.setPropertyIndex(1));
 }

 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected length to be zero", 0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull("Expected base value to be null", nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull("Expected immediate node to be null", nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue("Expected isLeaf to be true", nullPropertyPointer.isLeaf());
 }

 @Test
 public void testgetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NullPointer nullPointer = nullPropertyPointer.getValuePointer();
 assertEquals("Expected instance of NullPointer", nullPointer.getClass(), NullPointer.class);
 }

 @Test
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected isActualProperty to be false", nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected isActual to be false", nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue("Expected isContainer to be true", nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertThrows("Expected JXPathInvalidAccessException", JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue("testValue"));
 }

 @Test
 public void testCreatePath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull("Expected node pointer to be null", nodePointer);
 }

 @Test
 public void testCreatePathWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createPath(context, "testValue");
 assertNull("Expected node pointer to be null", nodePointer);
 }

 @Test
 public void testCreateChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"));
 assertNull("Expected node pointer to be null", nodePointer);
 }

 @Test
 public void testCreateChildWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 1, "testValue");
 assertNull("Expected node pointer to be null", nodePointer);
 }

 @Test
 public void testCreateChildWithIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer parentNodePointer = new PropertyOwnerPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 1, null);
 assertNull("Expected node pointer to be null", nodePointer);
 assertEquals("Expected parent node pointer to be the same", parentNodePointer, nullPropertyPointer.getParent());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("Expected property name to be testProperty", "testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("testName");
 assertEquals("Expected attribute value to be testName", "testName", nullPropertyPointer.getPropertyName());
 assertTrue("Expected byNameAttribute to be true", nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected isCollection to be false", nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected property count to be zero", 0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertEquals("Expected length of propertyNames array to be zero", 0, propertyNames.length);
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 nullPropertyPointer.setNameAttributeValue("testName");
 assertEquals("Expected asPath to be [@name='testName']", "[@name='testName']", nullPropertyPointer.asPath());
 }
}