import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyPointer(new NodePointer("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("root", nullPropertyPointer.getImmediateParentPointer().asPath());
 assertEquals("*", nullPropertyPointer.getPropertyName());
 assertNull(nullPropertyPointer.getValuePointer().getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCannotSetValueOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 JXPathContext context = new JXPathContext(new NodePointer("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(
 new PropertyPointer(new NodePointer("root")));
 NodePointer createdPath = nullPropertyPointer.createPath(context);
 assertEquals("root/null()", createdPath.asPath());
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new NodePointer("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(
 new PropertyPointer(new NodePointer("root")));
 NodePointer createdPath = nullPropertyPointer.createPath(context, "testValue");
 assertEquals("root/null()[. = 'testValue']", createdPath.asPath());
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 JXPathContext context = new JXPathContext(new NodePointer("root"));
 PropertyPointer parent = new PropertyPointer(new NodePointer("root"));
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer createdChild = nullPropertyPointer.createChild(context, new QName("test"), 0, "testValue");
 assertEquals("root/test[1]", createdChild.asPath());
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new NodePointer("root"));
 PropertyPointer parent = new PropertyPointer(new NodePointer("root"));
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer createdChild = nullPropertyPointer.createChild(context, new QName("test"), 0, new NodePointer("testValue"));
 assertEquals("root/test[1]", createdChild.asPath());
 }
}