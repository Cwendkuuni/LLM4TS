import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NullPropertyPointerTest {

 private NodePointer parent;

 @Before
 public void setUp() {
 parent = new NodePointer(new Object());
 }

 @Test
 public void testNullPropertyPointerConstructor() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getValuePointer());
 }

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyIndex(1);
 assertEquals(1, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals(0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NullPointer nullPointer = nullPropertyPointer.getValuePointer();
 assertSame(nullPropertyPointer, nullPointer.getParent());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValueWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertThrows(JXPathInvalidAccessException.class, () -> {
 nullPropertyPointer.setValue(new Object());
 });
 }

 @Test
 public void testSetValueWithPropertyOwnerPointerParent() {
 JXPathContext context = new JXPathContext(new Object[]{new Object(), new Object()});
 PropertyOwnerPointer propertyOwnerPointer =
 new PropertyOwnerPointer(context, new NodePointer(new Object()), "testProperty");
 NullPropertyPointer nullPropertyPointer =
 new NullPropertyPointer(propertyOwnerPointer);
 nullPropertyPointer.setValue(new Object());
 assertEquals(propertyOwnerPointer.getName().getLocalPart(),
 nullPropertyPointer.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNonPropertyParent() {
 NodePointer nonPropertyParent = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyParent);
 assertThrows(JXPathInvalidAccessException.class, () -> {
 nullPropertyPointer.setValue(new Object());
 });
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object[]{new Object(), new Object()});
 PropertyOwnerPointer propertyOwnerPointer =
 new PropertyOwnerPointer(context, new NodePointer(new Object()), "testProperty");
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer result = nullPropertyPointer.createPath(context);
 assertSame(propertyOwnerPointer, result.getParent());
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object[]{new Object(), new Object()});
 PropertyOwnerPointer propertyOwnerPointer =
 new PropertyOwnerPointer(context, new NodePointer(new Object()), "testProperty");
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer result = nullPropertyPointer.createPath(context, new Object());
 assertEquals(propertyOwnerPointer.getName().getLocalPart(),
 result.getParent().getName().getLocalPart());
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object[]{new Object(), new Object()});
 PropertyOwnerPointer propertyOwnerPointer =
 new PropertyOwnerPointer(context, new NodePointer(new Object()), "testProperty");
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer result = nullPropertyPointer.createChild(context, new QName("testChild"));
 assertSame(nullPropertyPointer, result.getParent());
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object[]{new Object(), new Object()});
 PropertyOwnerPointer propertyOwnerPointer =
 new PropertyOwnerPointer(context, new NodePointer(new Object()), "testProperty");
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer result = nullPropertyPointer.createChild(context, new QName("testChild"), 1, new Object());
 assertEquals("testChild", result.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 String[] result = nullPropertyPointer.getPropertyNames();
 assertEquals(0, result.length);
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 nullPropertyPointer.setIndex(1);
 assertEquals("/testAttribute[1]", nullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
 String test = "test'test\"test";
 assertEquals("test&apos;test&quot;test", NullPropertyPointer.escape(test));
 }
}