import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected null parent for NullPropertyPointer", null, nullPropertyPointer.getParent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPropertyPointerParent() {
 new NullPropertyPointer(null);
 }

 @Test
 public void testNullPropertyPointerName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected asterisk as default property name", "*", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testNullPropertyPointerByNameAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected false byNameAttribute property", nullPropertyPointer.isByNameAttribute());

 NullPropertyPointer nullPropertyPointerByNameAttribute = new NullPropertyPointer(null);
 nullPropertyPointerByNameAttribute.setNameAttributeValue("testName");
 assertTrue("Expected true byNameAttribute property", nullPropertyPointerByNameAttribute.isByNameAttribute());
 }

 @Test
 public void testNullPropertyPointerIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue("Expected true isContainer property", nullPropertyPointer.isContainer());
 }

 @Test
 public void testNullPropertyPointerIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue("Expected true isLeaf property", nullPropertyPointer.isLeaf());
 }

 @Test
 public void testNullPropertyPointerIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected false isActual property", nullPropertyPointer.isActual());
 }

 @Test
 public void testNullPropertyPointerIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected false isActualProperty property", nullPropertyPointer.isActualProperty());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerSetValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull("Expected null NodePointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context, "testValue");
 assertNull("Expected null NodePointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testName"), 0);
 assertNull("Expected null NodePointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testName"), 0, "testValue");
 assertNull("Expected null NodePointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithIndex() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer parentPointer = new NodePointer(context, new Object());
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testName"), 0, null, parentPointer);
 assertNull("Expected null NodePointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValueAndIndex() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer parentPointer = new NodePointer(context, new Object());
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testName"), 0, "testValue", parentPointer);
 assertNull("Expected null NodePointer", nodePointer);
 }
}