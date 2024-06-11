import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NullPropertyPointerTest {

 private NodePointer parent;
 private NullPropertyPointer nullPropertyPointer;

 @Before
 public void setup() {
 parent = new StaticNodePointer("root");
 nullPropertyPointer = new NullPropertyPointer(parent);
 }

 @Test
 public void testGetName() {
 assertEquals("*", nullPropertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyIndex() {
 nullPropertyPointer.setPropertyIndex(1);
 assertTrue(false); // should not be called
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLength() {
 nullPropertyPointer.getLength();
 }

 @Test
 public void testGetBaseValue() {
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testgetValuePointer() {
 NodePointer valuePointer = nullPropertyPointer.getValuePointer();
 assertNotNull(valuePointer);
 assertInstanceOf(NullPointer.class, valuePointer);
 }

 @Test
 public void testIsActualProperty() {
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue() {
 try {
 nullPropertyPointer.setValue("some value");
 assertTrue(false); // should not be called
 } catch (JXPathInvalidAccessException e) {
 // expected
 }
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object[]{});
 NodePointer newPath = nullPropertyPointer.createPath(context);
 assertNull(newPath);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object[]{});
 nullPropertyPointer.setValue("some value");
 NodePointer newPath = nullPropertyPointer.createPath(context);
 assertNotNull(newPath);
 assertInstanceOf(NullPointer.class, newPath);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object[]{});
 try {
 nullPropertyPointer.createChild(context, new QName("test"), 1);
 assertTrue(false); // should not be called
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testCreateChildWithNameAttribute() {
 JXPathContext context = new JXPathContext(new Object[]{});
 nullPropertyPointer.setNameAttributeValue("test");
 NodePointer newPath = nullPropertyPointer.createChild(context, new QName("test"), 1);
 assertNotNull(newPath);
 assertInstanceOf(NullPointer.class, newPath);
 }
}