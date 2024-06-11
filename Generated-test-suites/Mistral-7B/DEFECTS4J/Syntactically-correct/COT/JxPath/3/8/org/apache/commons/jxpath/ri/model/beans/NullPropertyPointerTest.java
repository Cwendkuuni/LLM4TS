import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NullPropertyPointer nullPropertyPointer;
 private NodePointer nodePointer;
 private JXPathContext jxPathContext;

 @Test
 public void testNullPropertyPointerConstructor() {
     nullPropertyPointer = new NullPropertyPointer(null);
     assertNull(nullPropertyPointer.getValuePointer());
     assertFalse(nullPropertyPointer.isActualProperty());
     assertFalse(nullPropertyPointer.isActual());
     assertTrue(nullPropertyPointer.isContainer());
     assertEquals("", nullPropertyPointer.getPropertyName());
     assertEquals(0, nullPropertyPointer.getLength());
     assertNull(nullPropertyPointer.getBaseValue());
     assertNull(nullPropertyPointer.getImmediateNode());
     assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testSetPropertyName() {
     nullPropertyPointer = new NullPropertyPointer(null);
     nullPropertyPointer.setPropertyName("testProperty");
     assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameWithNullParent() {
     nullPropertyPointer = new NullPropertyPointer(null);
     nullPropertyPointer.setPropertyName("testProperty");
 }

 @Test
 public void testCreatePath() {
     jxPathContext = new JXPathContext();
     nodePointer = jxPathContext.getDocument();
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     NodePointer path = nullPropertyPointer.createPath(jxPathContext);
     assertNull(path);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNullParent() {
     nullPropertyPointer = new NullPropertyPointer(null);
     nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testCreatePathWithParentPropertyOwner() {
     jxPathContext = new JXPathContext();
     nodePointer = jxPathContext.getDocument();
     NullPropertyPointer parentPropertyPointer = new NullPropertyPointer(nodePointer);
     PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(nodePointer);
     nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
     NodePointer path = nullPropertyPointer.createPath(jxPathContext);
     assertTrue(path instanceof PropertyPointer);
     assertEquals("testProperty", path.getName().getLocalPart());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNonPropertyParent() {
     jxPathContext = new JXPathContext();
     NodePointer parentPointer = jxPathContext.getDocument();
     nullPropertyPointer = new NullPropertyPointer(parentPointer);
     nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testSetValueWithPropertyOwnerParent() {
     jxPathContext = new JXPathContext();
     NodePointer parentPointer = jxPathContext.getDocument();
     PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parentPointer);
     nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
     nullPropertyPointer.setValue("testValue");
     PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
     assertEquals("testProperty", propertyPointer.getPropertyName().getLocalPart());
     assertEquals("testValue", propertyPointer.getValue());
 }

 @Test
 public void testCreatePathWithNonPropertyParent() {
     jxPathContext = new JXPathContext();
     NodePointer parentPointer = jxPathContext.getDocument();
     nullPropertyPointer = new NullPropertyPointer(parentPointer);
     NodePointer path = nullPropertyPointer.createPath(jxPathContext);
     assertNotNull(path);
     assertTrue(path instanceof PropertyOwnerPointer);
 }

 @Test
 public void testCreateChild() {
     jxPathContext = new JXPathContext();
     NodePointer parentPointer = jxPathContext.getDocument();
     nullPropertyPointer = new NullPropertyPointer(parentPointer);
     NodePointer child = nullPropertyPointer.createChild(jxPathContext, new QName("testChild"));
     assertNotNull(child);
     assertTrue(child instanceof PropertyPointer);
 }

 @Test
 public void testCreateChildWithValue() {
     jxPathContext = new JXPathContext();
     NodePointer parentPointer = jxPathContext.getDocument();
     nullPropertyPointer = new NullPropertyPointer(parentPointer);
     NodePointer child = nullPropertyPointer.createChild(jxPathContext, new QName("testChild"), "testValue");
     assertNotNull(child);
     assertTrue(child instanceof PropertyPointer);
     assertEquals("testValue", child.getValue());
 }
}