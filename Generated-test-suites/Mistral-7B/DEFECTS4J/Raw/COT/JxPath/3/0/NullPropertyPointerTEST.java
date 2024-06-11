import static org.junit.Assert.*;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;

public class NullPropertyPointerTest {

 private NodePointer createNodePointer() {
 return new NodePointer(null);
 }

 @Test
 public void testNullPropertyPointerConstructor() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 assertNull(nullPropertyPointer.getPropertyName());
 assertFalse(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testGetNameMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 nullPropertyPointer.setPropertyName("testProperty");
 QName expectedName = new QName("testProperty");
 QName actualName = nullPropertyPointer.getName();
 assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
 assertEquals(expectedName.getNamespaceURI(), actualName.getNamespaceURI());
 }

 @Test
 public void testSetPropertyIndexMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 int index = 1;
 nullPropertyPointer.setPropertyIndex(index);
 assertEquals(index, nullPropertyPointer.getPropertyIndex());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLengthMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 int length = nullPropertyPointer.getLength();
 fail("Expected UnsupportedOperationException not thrown");
 }

 @Test
 public void testGetBaseValueMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 Object baseValue = null;
 assertEquals(baseValue, nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNodeMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 NodePointer expectedNodePointer = createNodePointer();
 NodePointer actualNodePointer = nullPropertyPointer.getImmediateNode();
 assertSame(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testIsLeafMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testgetValuePointerMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName("testProperty"));
 NodePointer expectedNodePointer = new NullPointer(nullPropertyPointer, new QName("testProperty"));
 NodePointer actualNodePointer = nullPropertyPointer.getValuePointer();
 assertSame(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testIsActualPropertyMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainerMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValueMethodWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 try {
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException not thrown");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testSetValueMethodWithPropertyOwnerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 nullPropertyPointer.setValue(new Object());
 PropertyPointer testPropertyPointer = propertyOwnerPointer.getPropertyPointer();
 assertNotNull(testPropertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueMethodWithNonPropertyParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 NodePointer nonPropertyParent = new DOMNodePointerAdapter();
 try {
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException not thrown");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePathMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 NodePointer expectedNewParent = createNodePointer();
 NodePointer actualNewParent = nullPropertyPointer.createPath(context);
 assertSame(expectedNewParent, actualNewParent);
 }

 @Test
 public void testCreatePathMethodWithAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 NodePointer expectedNewParent = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer actualNewParent = nullPropertyPointer.createPath(context);
 assertSame(expectedNewParent, actualNewParent);
 NodePointer attributeNodePointer = expectedNewParent.createAttribute(context, new QName("testAttribute"));
 assertNotNull(attributeNodePointer);
 }

 @Test
 public void testCreatePathMethodWithCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 NodePointer expectedNewParent = new NullPropertyPointer(newParent);
 NodePointer actualNewParent = nullPropertyPointer.createPath(context);
 assertSame(expectedNewParent, actualNewParent);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathMethodWithNonPropertyParentCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 NodePointer nonPropertyParent = new DOMNodePointerAdapter();
 try {
 nullPropertyPointer.createPath(context);
 fail("Expected JXPathInvalidAccessException not thrown");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePathMethodWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 NodePointer expectedNewParent = new NullPropertyPointer(newParent);
 NodePointer actualNewParent = nullPropertyPointer.createPath(context, new Object());
 assertSame(expectedNewParent, actualNewParent);
 NodePointer childNodePointer = newParent.createChild(context, new QName("testChild"), 0, new Object());
 assertNotNull(childNodePointer);
 }

 @Test
 public void testCreateChildMethodWithNullContext() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 QName name = new QName("testChild");
 int index = 1;
 NodePointer expectedNewChild = null;
 NodePointer actualNewChild = nullPropertyPointer.createChild(null, name, index);
 assertSame(expectedNewChild, actualNewChild);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildMethodWithNullName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 NodePointer newParent = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 int index = 1;
 try {
 nullPropertyPointer.createChild(null, null, index);
 fail("Expected NullPointerException not thrown");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testCreateChildMethodWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 NodePointer expectedNewChild = new NullPropertyPointer(newParent);
 NodePointer actualNewChild = nullPropertyPointer.createChild(context, null, 1);
 assertSame(expectedNewChild, actualNewChild);
 }

 @Test
 public void testCreateChildMethodWithEmptyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 NodePointer expectedNewChild = new NullPropertyPointer(newParent);
 NodePointer actualNewChild = nullPropertyPointer.createChild(context, new QName(""), 1);
 assertSame(expectedNewChild, actualNewChild);
 }

 @Test
 public void testCreateChildMethodWithInvalidName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 NodePointer expectedNewChild = new NullPropertyPointer(newParent);
 NodePointer actualNewChild = nullPropertyPointer.createChild(context, new QName("testCollection@"), 1);
 assertSame(expectedNewChild, actualNewChild);
 }

 @Test
 public void testCreateChildMethodWithNameAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 NodePointer expectedNewChild = new NullPropertyPointer(newParent);
 NodePointer actualNewChild = nullPropertyPointer.createChild(context, new QName("testCollection"), 1);
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 NodePointer attributeNodePointer = expectedNewChild.createAttribute(context, new QName("testAttribute"));
 assertNotNull(attributeNodePointer);
 assertSame(expectedNewChild, actualNewChild);
 }

 @Test
 public void testCreateChildMethodWithIndexOutOfBoundsException() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 JXPathContext context = new JXPathContext();
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testProperty"});
 NodePointer newParent = propertyOwnerPointer.createChild(context, new QName("testCollection"), 0);
 try {
 nullPropertyPointer.createChild(context, new QName("testCollection"), Integer.MAX_VALUE);
 fail("Expected IndexOutOfBoundsException not thrown");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testSetPropertyNameMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 String propertyName = "testProperty";
 nullPropertyPointer.setPropertyName(propertyName);
 assertEquals(propertyName, nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValueMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 String attributeValue = "testAttribute";
 nullPropertyPointer.setNameAttributeValue(attributeValue);
 assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollectionMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCountMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 int propertyCount = 0;
 assertEquals(propertyCount, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNamesMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 String[] propertyNames = new String[0];
 assertArrayEquals(propertyNames, nullPropertyPointer.getPropertyNames());
 }

 @Test
 public void testAsPathMethodWithByNameAttributeFalse() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 String expectedPath = "/";
 String actualPath = nullPropertyPointer.asPath();
 assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testAsPathMethodWithByNameAttributeTrue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(createNodePointer());
 nullPropertyPointer.setNameAttributeValue("testProperty");
 NodePointer parentPointer = new PropertyOwnerPointerAdapter(createNodePointer(), new String[]{"testParent"});
 NodePointer childPointer = parentPointer.createChild(null, new QName("testProperty"), 0);
 nullPropertyPointer.setValue(childPointer);
 String expectedPath = "/testParent[@name='testParent']/@testProperty";
 String actualPath = nullPropertyPointer.asPath();
 assertEquals(expectedPath, actualPath);
 }
}