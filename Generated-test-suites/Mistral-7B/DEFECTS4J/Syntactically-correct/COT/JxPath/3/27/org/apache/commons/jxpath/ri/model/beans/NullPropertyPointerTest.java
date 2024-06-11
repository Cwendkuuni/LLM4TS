import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class NullPropertyPointerTest {

 private NodePointer createNullPropertyPointer() {
 return new NullPropertyPointer(new NodePointer(null, null));
 }

 @Test
 public void testNullPropertyPointerConstructor() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertEquals("*", nullPropertyPointer.getPropertyName());
 assertFalse(nullPropertyPointer.isAttribute());
 assertNull(nullPropertyPointer.getValuePointer());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertFalse(nullPropertyPointer.isActual());
 assertFalse(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isAttribute());
 }

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 nullPropertyPointer.setPropertyName("testProperty");
 QName qname = nullPropertyPointer.getName();
 assertEquals("testProperty", qname.getLocalPart());
 }

 @Test
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 nullPropertyPointer.setPropertyIndex(1);
 assertEquals(1, nullPropertyPointer.getIndex());
 }

 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertEquals(0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertNull(nullPropertyPointer.getValuePointer());
 }

 @Test
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValueWithNullParent() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(new Object()));
 }

 @Test
 public void testSetValueWithPropertyOwnerParent() {
 // Assuming PropertyOwnerPointer has a method getPropertyPointer() that returns a PropertyPointer
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyPointer);
 nullPropertyPointer.setValue(new Object());
 assertEquals(nullPropertyPointer, propertyPointer.getPropertyPointer());
 }

 @Test
 public void testSetValueWithNonPropertyParent() {
 // Assuming NodePointer has a method createChild() that creates a PropertyPointer if the node is a PropertyOwner
 NodePointer nodePointer = new NodePointer(null, new QName("node"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 nullPropertyPointer.setValue(new Object());
 assertNotNull(nodePointer.getChild(nullPropertyPointer.asPath()));
 }

 @Test
 public void testCreatePathWithNullContext() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertNull(nullPropertyPointer.createPath(null));
 }

 @Test
 public void testCreatePathWithNonNullContext() {
 // Assuming NodePointer has a method createChild() that creates a PropertyPointer if the node is a PropertyOwner
 NodePointer nodePointer = new NodePointer(null, new QName("node"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 JXPathContext context = new JXPathContext();
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNotNull(result);
 assertEquals(nodePointer, result.getParent());
 assertEquals(nullPropertyPointer.getPropertyName(), result.getName().getLocalPart());
 }

 @Test
 public void testCreatePathWithNonNullContextAndValue() {
 // Assuming NodePointer has a method createChild() that creates a PropertyPointer if the node is a PropertyOwner
 NodePointer nodePointer = new NodePointer(null, new QName("node"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 JXPathContext context = new JXPathContext();
 NodePointer result = nullPropertyPointer.createPath(context, new Object());
 assertNotNull(result);
 assertEquals(nodePointer, result.getParent());
 assertEquals(nullPropertyPointer.getPropertyName(), result.getName().getLocalPart());
 }

 @Test
 public void testCreateChildWithNullContext() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 assertThrows(NullPointerException.class, () -> nullPropertyPointer.createChild(null, new QName("child"), 0));
 }

 @Test
 public void testCreateChildWithNonNullContext() {
 // Assuming NodePointer has a method createChild() that creates a PropertyPointer if the node is a PropertyOwner
 NodePointer nodePointer = new NodePointer(null, new QName("node"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 JXPathContext context = new JXPathContext();
 NodePointer result = nullPropertyPointer.createChild(context, new QName("child"), 0);
 assertNotNull(result);
 assertEquals(nodePointer, result.getParent());
 assertEquals("child", result.getName().getLocalPart());
 }

 @Test
 public void testCreateChildWithNonNullContextAndValue() {
 // Assuming NodePointer has a method createChild() that creates a PropertyPointer if the node is a PropertyOwner
 NodePointer nodePointer = new NodePointer(null, new QName("node"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 JXPathContext context = new JXPathContext();
 NodePointer result = nullPropertyPointer.createChild(context, new QName("child"), 0, new Object());
 assertNotNull(result);
 assertEquals(nodePointer, result.getParent());
 assertEquals("child", result.getName().getLocalPart());
 }

 @Test
 public void testAsPathWithByNameAttributeFalse() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 nullPropertyPointer.setPropertyName("testProperty");
 String asPath = nullPropertyPointer.asPath();
 assertEquals("/testProperty", asPath);
 }

 @Test
 public void testAsPathWithByNameAttributeTrue() {
 NullPropertyPointer nullPropertyPointer = createNullPropertyPointer();
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 nullPropertyPointer.setIndex(1);
 String asPath = nullPropertyPointer.asPath();
 assertEquals("/node[@name='testAttribute']/1", asPath);
 }
}