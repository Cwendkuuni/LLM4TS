import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NodePointer nodePointer;
 private JXPathContext context;
 private NullPropertyPointer nullPropertyPointer;

 @Test
 public void testNullPropertyPointerConstruction() {
     nodePointer = new NodePointer(null);
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     assertNull(nullPropertyPointer.getImmediateNode());
     assertFalse(nullPropertyPointer.isLeaf());
     assertTrue(nullPropertyPointer.isContainer());
     assertEquals(0, nullPropertyPointer.getLength());
 }

 @Test
 public void testSetPropertyName() {
     nodePointer = new NodePointer(null);
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setPropertyName("testProperty");
     assertEquals("testProperty", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameNullPointerParent() {
     nodePointer = null;
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setPropertyName("testProperty");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameNonContainerParent() {
     nodePointer = new NodePointer(new Object());
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setPropertyName("testProperty");
 }

 @Test
 public void testSetNameAttributeValue() {
     nodePointer = new NodePointer(null);
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setNameAttributeValue("testAttribute");
     assertEquals("testAttribute", nullPropertyPointer.getPropertyName().getLocalPart());
     assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetNameAttributeValueNullPointerParent() {
     nodePointer = null;
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setNameAttributeValue("testAttribute");
 }

 @Test
 public void testIsCollection() {
     nodePointer = new NodePointer(null);
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
     nodePointer = new NodePointer(null);
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
     nodePointer = new NodePointer(null);
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     String[] propertyNames = nullPropertyPointer.getPropertyNames();
     assertArrayEquals(new String[0], propertyNames);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
     nodePointer = null;
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setValue(new Object());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNonContainerParent() {
     nodePointer = new NodePointer(new Object());
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setValue(new Object());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathNullParent() {
     nodePointer = null;
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.createPath(context);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathNonContainerParent() {
     nodePointer = new NodePointer(new Object());
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.createPath(context);
 }

 @Test
 public void testCreatePath() {
     context = new JXPathContext();
     nodePointer = new NodePointer(context.setRoot(new Object()));
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     NodePointer result = nullPropertyPointer.createPath(context);
     assertTrue(result instanceof PropertyOwnerPointer);
 }

 @Test
 public void testCreatePathWithValue() {
     context = new JXPathContext();
     nodePointer = new NodePointer(context.setRoot(new Object()));
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     NodePointer result = nullPropertyPointer.createPath(context, new Object());
     assertTrue(result instanceof NodePointer);
 }

 @Test
 public void testCreateChild() {
     context = new JXPathContext();
     nodePointer = new NodePointer(context.setRoot(new Object()));
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     QName name = new QName("testChild");
     NodePointer result = nullPropertyPointer.createChild(context, name, 0);
     assertTrue(result instanceof NodePointer);
 }

 @Test
 public void testCreateChildWithValue() {
     context = new JXPathContext();
     nodePointer = new NodePointer(context.setRoot(new Object()));
     nullPropertyPointer = new NullPropertyPointer(nodePointer);
     QName name = new QName("testChild");
     Object value = new Object();
     NodePointer result = nullPropertyPointer.createChild(context, name, 0, value);
     assertTrue(result instanceof NodePointer);
 }

 @Test
 public void testAsPath() {
     context = new JXPathContext();
     nodePointer = new NodePointer(context.setRoot(new Object()));
     NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
     nullPropertyPointer.setPropertyName("testProperty");
     assertEquals("null[testProperty]", nullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
     String string = "test'string\"";
     String escapedString = nullPropertyPointer.escape(string);
     assertEquals("test&apos;string&quot;", escapedString);
 }
}