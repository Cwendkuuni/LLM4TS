import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 Assert.assertEquals("*", nullPropertyPointer.getPropertyName());
 Assert.assertFalse(nullPropertyPointer.isActual());
 Assert.assertFalse(nullPropertyPointer.isActualProperty());
 Assert.assertFalse(nullPropertyPointer.isContainer());
 Assert.assertFalse(nullPropertyPointer.isLeaf());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueToNullPropertyPointerThrowsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 }

 @Test
 public void testCreatingPathForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer path = nullPropertyPointer.createPath(context);
 Assert.assertNull(path.getNode());
 Assert.assertEquals("", path.asPath());
 }

 @Test
 public void testCreatingPathForNullPropertyPointerWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer path = nullPropertyPointer.createPath(context, new Object());
 Assert.assertNull(path.getNode());
 Assert.assertEquals("", path.asPath());
 }

 @Test
 public void testCreatingChildForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 QName name = new QName("test");
 NodePointer child = nullPropertyPointer.createChild(context, name, 0);
 Assert.assertNull(child.getNode());
 Assert.assertEquals("", child.asPath());
 }

 @Test
 public void testCreatingChildForNullPropertyPointerWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 QName name = new QName("test");
 Object value = new Object();
 NodePointer child = nullPropertyPointer.createChild(context, name, 0, value);
 Assert.assertNull(child.getNode());
 Assert.assertEquals("", child.asPath());
 }

 @Test
 public void testSettingPropertyNameForNullPropertyPointer() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("newPropertyName");
 Assert.assertEquals("newPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSettingNameAttributeValueForNullPropertyPointer() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue("newPropertyName");
 Assert.assertEquals("newPropertyName", nullPropertyPointer.getPropertyName());
 Assert.assertTrue(nullPropertyPointer.isByNameAttribute());
 }
}