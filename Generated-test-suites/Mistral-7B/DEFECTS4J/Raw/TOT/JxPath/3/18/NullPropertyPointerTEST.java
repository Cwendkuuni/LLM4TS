import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(new Object()));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("*", nullPropertyPointer.getPropertyName());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertFalse(nullPropertyPointer.isActual());
 assertTrue(nullPropertyPointer.isContainer());
 assertNull(nullPropertyPointer.getValuePointer().getNode());
 }

 @Test
 public void testNullPropertyPointerValueSetting() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DOMNodePointer(new Object()));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(new Object()));
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DOMNodePointer(new Object()));
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.createPath(context).getNode());
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DOMNodePointer(new Object()));
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer createdPath = nullPropertyPointer.createPath(context, "testValue");
 assertNotNull(createdPath);
 assertSame(createdPath.getNode(), parent.getNode());
 }

 @Test
 public void testNullPropertyPointerSetPropertyName() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DOMNodePointer(new Object()));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testNullPropertyPointerSetNameAttributeValue() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new DOMNodePointer(new Object()));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }
}