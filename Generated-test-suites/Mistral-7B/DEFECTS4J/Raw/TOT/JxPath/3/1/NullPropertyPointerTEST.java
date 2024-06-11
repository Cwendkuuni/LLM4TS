import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testCreateNullPropertyPointer() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null property pointer", null, nullPropertyPointer.getImmediateNode());
 assertEquals("Expected length 0", 0, nullPropertyPointer.getLength());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueOnNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 Object value = new Object();
 nullPropertyPointer.setValue(value);
 fail("Should have thrown JXPathInvalidAccessException");
 }

 @Test
 public void testSetValueOnContainerParent() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 Object value = new Object();
 nullPropertyPointer.setValue(value);
 // Test that the value is actually set on the parent
 assertEquals("Expected parent to have the new value", value, parent.getNodeValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueOnNonContainerParent() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 Object value = new Object();
 nullPropertyPointer.setValue(value);
 fail("Should have thrown JXPathInvalidAccessException");
 }

 @Test
 public void testCreatePath() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext(new Object());
 NodePointer path = nullPropertyPointer.createPath(context);
 assertEquals("Expected null pointer", null, path);
 }

 @Test
 public void testCreatePathWithValue() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext(new Object());
 Object value = new Object();
 NodePointer path = nullPropertyPointer.createPath(context, value);
 assertNotNull("Expected non-null pointer", path);
 }
}