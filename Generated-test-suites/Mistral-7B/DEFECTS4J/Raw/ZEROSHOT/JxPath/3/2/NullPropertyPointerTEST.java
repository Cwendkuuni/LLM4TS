import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NullPropertyPointerTest {

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 QName name = nullPropertyPointer.getName();
 assertEquals("*", name.getLocalPart());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.setPropertyIndex(0);
 }

 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertEquals(0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testgetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 NodePointer nodePointer = nullPropertyPointer.getValuePointer();
 assertNotNull(nodePointer);
 assertTrue(nodePointer instanceof NullPointer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.isActualProperty();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.isActual();
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.setValue(1);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreatePath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.createPath(new JXPathContext());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreatePathWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.createPath(new JXPathContext(), 1);
 }

 @Test
 public void testCreateChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 JXPathContext jxPathContext = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createPath(jxPathContext);
 NodePointer childNodePointer = nodePointer.createChild(jxPathContext, new QName("test"), 0);
 assertNotNull(childNodePointer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCreateChildWithNameAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 JXPathContext jxPathContext = new JXPathContext();
 nullPropertyPointer.setNameAttributeValue("test");
 nullPropertyPointer.createChild(jxPathContext, new QName("test"), 0);
 }

 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.setPropertyName("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.setPropertyName("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(new String[0], propertyNames);
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.setPropertyName("test");
 String path = nullPropertyPointer.asPath();
 assertEquals("/test", path);
 }
}