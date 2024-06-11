import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("*", nullPropertyPointer.getPropertyName());
 assertFalse(nullPropertyPointer.isActual());
 assertFalse(nullPropertyPointer.isContainer());
 assertNull(nullPropertyPointer.getBaseValue());
 assertNull(nullPropertyPointer.getImmediateNode());
 assertFalse(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testNullPropertyPointerGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 QName qname = nullPropertyPointer.getName();
 assertEquals("*", qname.getLocalPart());
 assertNull(qname.getNamespaceURI());
 }

 @Test
 public void testNullPropertyPointerSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer path = nullPropertyPointer.createPath(context);
 assertNull(path);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerSetValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("testValue");
 fail("Should throw JXPathInvalidAccessException");
 }

 @Test
 public void testNullPropertyPointerIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testNullPropertyPointerIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testNullPropertyPointerIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testNullPropertyPointerGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer valuePointer = nullPropertyPointer.getValuePointer();
 assertNotNull(valuePointer);
 assertTrue(valuePointer instanceof NullPointer);
 }
}