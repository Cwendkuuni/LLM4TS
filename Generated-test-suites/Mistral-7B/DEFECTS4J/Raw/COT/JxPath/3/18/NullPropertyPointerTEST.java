import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NodePointer parent;

 @Before
 public void setUp() {
 parent = new NodePointer(null);
 }

 // Testing constructor
 @Test
 public void testNullPropertyPointerConstructor() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected NullPropertyPointer", null, nullPropertyPointer.getImmediateNode());
 assertEquals("Expected NullPropertyPointer", "*", nullPropertyPointer.getPropertyName());
 assertFalse("Expected false for isActualProperty", nullPropertyPointer.isActualProperty());
 }

 // Testing getName method
 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 QName expectedName = new QName("testProperty");
 QName actualName = nullPropertyPointer.getName();
 assertEquals("Expected QName", expectedName, actualName);
 }

 // Testing setPropertyIndex method
 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyIndex(1);
 }

 // Testing getLength method
 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected 0", 0, nullPropertyPointer.getLength());
 }

 // Testing getBaseValue method
 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null", null, nullPropertyPointer.getBaseValue());
 }

 // Testing getImmediateNode method
 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null", null, nullPropertyPointer.getImmediateNode());
 }

 // Testing isLeaf method
 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertTrue("Expected true for isLeaf", nullPropertyPointer.isLeaf());
 }

 // Testing getValuePointer method
 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NullPointer nullPointer = nullPropertyPointer.getValuePointer();
 assertNotNull("Expected NonNullPointer", nullPointer);
 }

 // Testing isActualProperty method
 @Test
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse("Expected false for isActualProperty", nullPropertyPointer.isActualProperty());
 }

 // Testing isActual method
 @Test
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse("Expected false for isActual", nullPropertyPointer.isActual());
 }

 // Testing isContainer method
 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertTrue("Expected true for isContainer", nullPropertyPointer.isContainer());
 }

 // Testing setValue method
 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("testValue");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParentPropertyOwner() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testSetValue() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 String value = "testValue";
 nullPropertyPointer.setValue(value);
 assertEquals("Expected testValue", value, propertyOwnerPointer.getValue());
 }

 // Testing createPath method with null parent
 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createPath(context);
 }

 // Testing createPath method with non-PropertyOwnerPointer parent
 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathNonPropertyOwnerPointerParent() {
 NodePointer nonPropertyOwnerPointer = new DOMNodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerPointer);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createPath(context);
 }

 // Testing createPath method with PropertyOwnerPointer parent
 @Test
 public void testCreatePathPropertyOwnerPointerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 JXPathContext context = new JXPathContext();
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 NodePointer expectedPath = propertyPointer.createChild(context, new QName("testProperty"), 0);
 NodePointer actualPath = nullPropertyPointer.createPath(context);
 assertEquals("Expected testProperty PropertyPointer", expectedPath, actualPath);
 }

 // Testing createPath method with value
 @Test
 public void testCreatePathValue() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 JXPathContext context = new JXPathContext();
 Object value = "testValue";
 NodePointer expectedPath = propertyOwnerPointer.getPropertyPointer().createChild(context, new QName("testProperty"), 0, value);
 NodePointer actualPath = nullPropertyPointer.createPath(context, value);
 assertEquals("Expected testProperty PropertyPointer", expectedPath, actualPath);
 }

 // Testing createChild method with null parent
 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreateChildNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("testProperty"), 0);
 }

 // Testing createChild method with non-PropertyOwnerPointer parent
 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreateChildNonPropertyOwnerPointerParent() {
 NodePointer nonPropertyOwnerPointer = new DOMNodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerPointer);
 JXPathContext context = new JXPathContext();
 nullPropertyPointer.createChild(context, new QName("testProperty"), 0);
 }

 // Testing createChild method with PropertyOwnerPointer parent
 @Test
 public void testCreateChildPropertyOwnerPointerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 JXPathContext context = new JXPathContext();
 NodePointer expectedPath = propertyOwnerPointer.getPropertyPointer().createChild(context, new QName("testProperty"), 0);
 NodePointer actualPath = nullPropertyPointer.createChild(context, new QName("testProperty"), 0);
 assertEquals("Expected testProperty PropertyPointer", expectedPath, actualPath);
 }

 // Testing createChild method with value
 @Test
 public void testCreateChildValue() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 JXPathContext context = new JXPathContext();
 Object value = "testValue";
 NodePointer expectedPath = propertyOwnerPointer.getPropertyPointer().createChild(context, new QName("testProperty"), 0, value);
 NodePointer actualPath = nullPropertyPointer.createChild(context, new QName("testProperty"), 0, value);
 assertEquals("Expected testProperty PropertyPointer", expectedPath, actualPath);
 }

 // Testing getPropertyName method
 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 String expectedName = "testProperty";
 String actualName = nullPropertyPointer.getPropertyName();
 assertEquals("Expected testProperty", expectedName, actualName);
 }

 // Testing setNameAttributeValue method
 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue("testAttributeValue");
 String expectedName = "@name='testAttributeValue'";
 String actualPath = nullPropertyPointer.asPath();
 assertEquals("Expected testAttributeValue in asPath", expectedName, actualPath.substring(0, actualPath.indexOf('[')));
 }

 // Testing isCollection method
 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse("Expected false for isCollection", nullPropertyPointer.isCollection());
 }

 // Testing getPropertyCount method
 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected 0", 0, nullPropertyPointer.getPropertyCount());
 }

 // Testing getPropertyNames method
 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertArrayEquals("Expected empty String array", new String[0], nullPropertyPointer.getPropertyNames());
 }

 // Testing asPath method with byNameAttribute false
 @Test
 public void testAsPathByNameAttributeFalse() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 String expectedPath = "testProperty";
 String actualPath = nullPropertyPointer.asPath();
 assertEquals("Expected testProperty in asPath", expectedPath, actualPath);
 }

 // Testing asPath method with byNameAttribute true
 @Test
 public void testAsPathByNameAttributeTrue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 nullPropertyPointer.setNameAttributeValue("testAttributeValue");
 String expectedPath = "@name='testAttributeValue']";
 String actualPath = nullPropertyPointer.asPath();
 assertEquals("Expected testAttributeValue in asPath", expectedPath, actualPath.substring(0, actualPath.indexOf('[')));
 }

 // Testing escape method
 @Test
 public void testEscapeMethod() {
 String input = "test'string with 'quote 'in it";
 String expectedOutput = "test&apos;string with &apos;quote &apos;in it";
 String actualOutput = NullPropertyPointer.escape(input);
 assertEquals("Expected escaped string", expectedOutput, actualOutput);
 }
}