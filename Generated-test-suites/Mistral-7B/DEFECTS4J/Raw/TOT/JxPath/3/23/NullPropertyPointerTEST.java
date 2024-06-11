import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(new Object()));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getBaseValue());
 assertNull(nullPropertyPointer.getImmediateNode());
 assertFalse(nullPropertyPointer.isLeaf());
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("test");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNullParentPropertyPointer() {
 PropertyPointer parent = null;
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("test");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNonChangeableLocation() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer pointer = context.getDocument();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(pointer);
 nullPropertyPointer.setValue("test");
 }

 @Test
 public void testSettingValueOnChangeablePropertyOwner() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(context.getDocument());
 parent.setProperty("test", "value");
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("test2");
 assertEquals("test2", parent.getPropertyValue("test"));
 }

 @Test
 public void testCreatingPathWithNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNull(result);
 }

 @Test
 public void testCreatingPathWithAttributeName() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("test");
 nullPropertyPointer.setNameAttributeValue("attribute");
 NodePointer result = nullPropertyPointer.createPath(context);
 NodePointer expected = context.getDocument().createAttribute("attribute");
 assertEquals(expected, result);
 }

 @Test
 public void testCreatingPathWithIndex() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("test");
 NodePointer result = nullPropertyPointer.createPath(context, 1);
 assertNull(result);
 }

 @Test
 public void testCreatingPathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("test");
 NodePointer result = nullPropertyPointer.createPath(context, "value");
 assertNull(result);
 }

 @Test
 public void testCreatingChildWithNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer result = nullPropertyPointer.createChild(context, new QName("test"), 1);
 assertNull(result);
 }

 @Test
 public void testCreatingChildWithAttributeName() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("test");
 nullPropertyPointer.setNameAttributeValue("attribute");
 NodePointer result = nullPropertyPointer.createChild(context, new QName("test2"), 1);
 NodePointer expected = context.getDocument().createAttribute("attribute");
 assertEquals(expected, result);
 }

 @Test
 public void testCreatingChildWithIndex() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer result = nullPropertyPointer.createChild(context, new QName("test"), 1, "value");
 assertNull(result);
 }

 @Test
 public void testCreatingChildWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyPointer parent = new PropertyPointer(context.getDocument());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer result = nullPropertyPointer.createChild(context, new QName("test"), 1, 1);
 assertNull(result);
 }

}