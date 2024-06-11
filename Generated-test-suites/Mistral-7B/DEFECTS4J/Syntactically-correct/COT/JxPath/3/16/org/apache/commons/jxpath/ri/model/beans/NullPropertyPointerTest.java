import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerConstructor() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals(0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNotNull(nullPropertyPointer.getValuePointer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createPath(context);
 }

 @Test
 public void testCreatePath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(new QName("parent"));
 nullPropertyPointer.setParent(parent);
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNotNull(result);
 }

 @Test
 public void testCreatePathWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(new QName("parent"));
 nullPropertyPointer.setParent(parent);
 Object value = "testValue";
 NodePointer result = nullPropertyPointer.createPath(context, value);
 assertNotNull(result);
 }

 @Test
 public void testCreateChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(new QName("parent"));
 nullPropertyPointer.setParent(parent);
 QName name = new QName("child");
 int index = 0;
 NodePointer result = nullPropertyPointer.createChild(context, name, index);
 assertNotNull(result);
 }

 @Test
 public void testCreateChildWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(new QName("parent"));
 nullPropertyPointer.setParent(parent);
 QName name = new QName("child");
 int index = 0;
 Object value = "testValue";
 NodePointer result = nullPropertyPointer.createChild(context, name, index, value);
 assertNotNull(result);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 }

 @Test
 public void testSetPropertyNameWithParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer parent = new NodePointer(new QName("parent"));
 nullPropertyPointer.setParent(parent);
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("testAttributeName");
 assertEquals("testAttributeName", nullPropertyPointer.getPropertyName());
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

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildWithIndexOutOfRange() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(new QName("parent"));
 nullPropertyPointer.setParent(parent);
 QName name = new QName("child");
 int index = Integer.MAX_VALUE;
 nullPropertyPointer.createChild(context, name, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyNameWithEmptyString() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("");
 }

}