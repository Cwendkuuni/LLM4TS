import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getBaseValue());
 assertNull(nullPropertyPointer.getImmediateNode());
 assertEquals(0, nullPropertyPointer.getLength());
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testNullPropertyPointerSetValue() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 try {
 nullPropertyPointer.setValue("testValue");
 fail();
 } catch (JXPathInvalidAccessException e) {
 // expected
 }
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer path = nullPropertyPointer.createPath(context);
 assertNull(path);
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer path = nullPropertyPointer.createPath(context, "testValue");
 assertNull(path);
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer child = nullPropertyPointer.createChild(context, new QName("testName"));
 assertNull(child);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer child = nullPropertyPointer.createChild(context, new QName("testName"), 0, "testValue");
 assertNull(child);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithIndex() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer child = nullPropertyPointer.createChild(context, new QName("testName"), 1);
 assertNull(child);
 }

 @Test
 public void testNullPropertyPointerIsCollection() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testNullPropertyPointerPropertyCount() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testNullPropertyPointerPropertyNames() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(new String[0], propertyNames);
 }

 @Test
 public void testNullPropertyPointerWithParentPropertyOwner() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer1.isActual());
 PropertyOwnerPointer parent2 = new PropertyOwnerPointer(new NodePointer(null));
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent2);
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(propertyPointer);
 assertTrue(nullPropertyPointer2.isActual());
 }

 @Test
 public void testNullPropertyPointerWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActual());
 }
}