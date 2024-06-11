import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyOwnerPointer(new DomNodePointer(1));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getValuePointer().getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueOnNullParent() {
 PropertyPointer parent = null;
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("some value");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueOnNonPropertyParent() {
 PropertyPointer parent = new DomNodePointer(1);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("some value");
 }

 @Test
 public void testSetValueOnPropertyOwnerParent() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DomNodePointer(1));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 PropertyPointer propertyPointer = parent.getPropertyPointer();
 propertyPointer.setPropertyName("testProperty");
 nullPropertyPointer.setValue("some value");
 assertSame("some value", propertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathOnNonPropertyParent() {
 PropertyPointer parent = new DomNodePointer(1);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer createdPath = nullPropertyPointer.createPath(new JXPathContext());
 }

 @Test
 public void testCreatePathOnPropertyOwnerParent() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DomNodePointer(1));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer createdPath = nullPropertyPointer.createPath(new JXPathContext());
 assertSame(parent.getNode(), createdPath.getNode());
 }

 @Test
 public void testCreateChildOnPropertyOwnerParent() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DomNodePointer(1));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer createdChild = nullPropertyPointer.createChild(
 new JXPathContext(), new QName("testChild"));
 assertSame(parent.getNode(), createdChild.getNode());
 }

 @Test
 public void testCreateChildWithValueOnPropertyOwnerParent() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DomNodePointer(1));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer createdChild = nullPropertyPointer.createChild(
 new JXPathContext(), new QName("testChild"), "some value");
 assertSame(parent.getNode(), createdChild.getNode());
 assertSame("some value", createdChild.getValue());
 }

 @Test
 public void testSetPropertyName() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DomNodePointer(1));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertSame("testProperty", nullPropertyPointer.getPropertyName());
 }
}