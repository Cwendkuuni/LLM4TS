import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class NullPropertyPointerTest {

 @Test
 public void testNewInstance() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertEquals(NullPropertyPointer.class, pointer.getClass());
 assertNull(pointer.getParent());
 assertNull(pointer.getName());
 assertNull(pointer.getValuePointer());
 }

 @Test
 public void testGetName() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 pointer.setPropertyName("testProperty");
 assertEquals("testProperty", pointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 pointer.setPropertyName("testProperty");
 assertEquals("testProperty", pointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 pointer.setNameAttributeValue("testProperty");
 assertEquals("testProperty", pointer.getPropertyName());
 assertEquals(true, pointer.isByNameAttribute());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertTrue(pointer.isContainer());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertTrue(pointer.isLeaf());
 }

 @Test
 public void testIsActual() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertFalse(pointer.isActual());
 }

 @Test
 public void testIsActualProperty() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertFalse(pointer.isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertNull(pointer.getImmediateNode());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 NodePointer result = pointer.createPath(context);
 assertNull(result);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 pointer.setValue(new Object());
 NodePointer result = pointer.createPath(context);
 assertNull(result);
 }

 @Test
 public void testCreatePathWithValueAndAttribute() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 pointer.setValue(new Object());
 pointer.setPropertyName("testProperty");
 pointer.setNameAttributeValue("testAttribute");
 NodePointer result = pointer.createPath(context);
 NodePointer expected = new NodePointer() {
 @Override
 public String asPath() {
 return "[@testAttribute='']";
 }
 };
 assertSame(expected.getClass(), result.getClass());
 assertSame(expected, result);
 }

 @Test
 public void testCreatePathWithAttribute() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 pointer.setPropertyName("testProperty");
 pointer.setNameAttributeValue("testAttribute");
 NodePointer result = pointer.createPath(context);
 NodePointer expected = new NodePointer() {
 @Override
 public String asPath() {
 return "[@testAttribute='']";
 }
 };
 assertSame(expected.getClass(), result.getClass());
 assertSame(expected, result);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 pointer.setPropertyName("testProperty");
 NodePointer result = pointer.createChild(context, new QName("testChild"), 0);
 assertNull(result);
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object());NullPropertyPointer parentPointer = new NullPropertyPointer(null);
NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
pointer.setPropertyName("testProperty");
NodePointer result = pointer.createChild(context, new QName("testChild"), 0, new Object());
assertNull(result);
}

 @Test
 public void testCreateChildWithAttribute() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 pointer.setPropertyName("testProperty");
 NodePointer result = pointer.createChild(context, new QName("testChild"), 0, "testValue");
 assertNull(result);
 }

 @Test
 public void testCreateChildWithValueAndAttribute() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer parentPointer = new NullPropertyPointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parentPointer);
 pointer.setPropertyName("testProperty");
 pointer.setNameAttributeValue("testAttribute");
 NodePointer result = pointer.createChild(context, new QName("testChild"), 0, "testValue");
 NodePointer expected = new NodePointer() {
 @Override
 public String asPath() {
 return "[@testAttribute='']/testChild[1]";
 }
 };
 assertSame(expected.getClass(), result.getClass());
 assertSame(expected, result);
 }

 @Test
 public void testSetValue() {
 NullPropertyPointer pointer = new NullPropertyPointer(null);
 assertThrows(JXPathInvalidAccessException.class, () -> pointer.setValue(new Object()));
 }
}