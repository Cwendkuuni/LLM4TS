import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 QName name = nullPropertyPointer.getName();
 assertEquals("*", name.getLocalPart());
 }

 @Test
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyIndex(1);
 assertEquals(1, nullPropertyPointer.getPropertyIndex());
 nullPropertyPointer.setPropertyIndex(-1);
 assertEquals(-1, nullPropertyPointer.getPropertyIndex());
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
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testgetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.getValuePointer();
 assertNotNull(nodePointer);
 assertInstanceOf(NullPointer.class, nodePointer);
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
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue() {
 // Test with null parent
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 try {
 nullPropertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }

 // Test with non-PropertyOwnerPointer parent
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 try {
 nullPropertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }

 // Test with PropertyOwnerPointer parent that supports dynamic property declaration
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null) {
 @Override
 public PropertyPointer getPropertyPointer() {
 return new PropertyPointer(this) {
 @Override
 public int getLength() {
 return 1;
 }

 @Override
 public Object getBaseValue() {
 return "testValue";
 }

 @Override
 public NodePointer getValuePointer() {
 return new NodePointer() {
 @Override
 public QName getName() {
 return new QName("testProperty");
 }
 };
 }
 };
 }};
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 assertEquals("testValue", propertyOwnerPointer.getPropertyPointer().getValue());
 nullPropertyPointer.setValue("testValue2");
 assertEquals("testValue2", propertyOwnerPointer.getPropertyPointer().getValue());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context, "testValue");
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithParentPropertyOwner() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertInstanceOf(PropertyOwnerPointer.class, nodePointer);
 }

 @Test
 public void testCreatePathWithParentNullPointer() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(null);
 NullPropertyPointer nullPropertyPointer2 = new NullPropertyPointer(nullProperty1);
 NodePointer nodePointer = nullPropertyPointer2.createPath(context);
 assertInstanceOf(NullPointer.class, nodePointer);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testChild"));
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testChild"), 1, "testValue");
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithParentPropertyOwner() {
 JXPathContext context = new JXPathContext(new Object());
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testChild"), 1);
 assertInstanceOf(PropertyPointer.class.getClass(), nodePointer.getClass());
 }
}