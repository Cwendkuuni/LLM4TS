import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("*", nullPropertyPointer.getPropertyName());
 assertFalse(nullPropertyPointer.isActual());
 assertTrue(nullPropertyPointer.isContainer());
 assertFalse(nullPropertyPointer.isLeaf());
 assertEquals(0, nullPropertyPointer.getLength());
 assertNull(nullPropertyPointer.getBaseValue());
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testNullPropertyPointerGetName() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 QName name = nullPropertyPointer.getName();
 assertEquals("*", name.getLocalPart());
 }

 @Test
 public void testNullPropertyPointerSetPropertyName() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("newName");
 assertEquals("newName", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPropertyPointerInvalidParent() {
 NodePointer parent = null;
 try {
 new NullPropertyPointer(parent);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNullPropertyPointerValueSetting() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("value");
 fail("Cannot set value on a NullPropertyPointer");
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNull(result);
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 Object value = new Object();
 NodePointer result = nullPropertyPointer.createPath(context, value);
 assertNull(result);
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 QName name = new QName("child");
 NodePointer result = nullPropertyPointer.createChild(context, name);
 assertNull(result);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 QName name = new QName("child");
 Object value = new Object();
 NodePointer result = nullPropertyPointer.createChild(context, name, 0, value);
 assertNull(result);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithIndex() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 QName name = new QName("child");
 NodePointer result = nullPropertyPointer.createChild(context, name, 0);
 assertNull(result);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithIndexAndValue() {
 NodePointer parent = new NodePointer(null, new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 QName name = new QName("child");
 Object value = new Object();
 NodePointer result = nullPropertyPointer.createChild(context, name, 0, value);
 assertNull(result);
 }
}