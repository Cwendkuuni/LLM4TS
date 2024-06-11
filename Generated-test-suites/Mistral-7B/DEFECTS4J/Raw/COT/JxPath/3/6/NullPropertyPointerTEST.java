import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;

public class NullPropertyPointerTest {

 private static final String TEST_NAME = "test";
 private static final String TEST_Attribute_NAME = "attributeName";
 private static final int TEST_INDEX = 0;
 private NodePointer parent;
 private PropertyOwnerPointer propertyOwner;
 private NullPropertyPointer nullProperty;

 @Test
 public void testNullPropertyPointerConstructor() {
     parent = new PropertyOwnerPointer(null);
     nullProperty = new NullPropertyPointer(parent);
     Assert.assertNotNull(nullProperty);
     Assert.assertNotNull(nullProperty.getValuePointer());
     Assert.assertEquals("", nullProperty.getPropertyName());
     Assert.assertFalse(nullProperty.isActualProperty());
     Assert.assertFalse(nullProperty.isActual());
     Assert.assertTrue(nullProperty.isContainer());
     Assert.assertNull(nullProperty.getBaseValue());
     Assert.assertNull(nullProperty.getImmediateNode());
     Assert.assertTrue(nullProperty.isLeaf());
 }

 @Test
 public void testSetPropertyName() {
     parent = new PropertyOwnerPointer(null);
     nullProperty = new NullPropertyPointer(parent);
     nullProperty.setPropertyName(TEST_NAME);
     Assert.assertEquals(TEST_NAME, nullProperty.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameNullParent() {
     nullProperty = new NullPropertyPointer(null);
     nullProperty.setPropertyName(TEST_NAME);
 }

 @Test
 public void testIsCollection() {
     nullProperty = new NullPropertyPointer(null);
     Assert.assertFalse(nullProperty.isCollection());
 }

 @Test
 public void testCreatePath() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     JXPathContext context = new JXPathContext(new Object());
     NodePointer path = nullProperty.createPath(context);
     Assert.assertNotNull(path);
     Assert.assertEquals(propertyOwner, path.getParent());
 }

 @Test
 public void testCreatePathWithValue() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     JXPathContext context = new JXPathContext(new Object());
     Object value = new Object();
     NodePointer path = nullProperty.createPath(context, value);
     Assert.assertNotNull(path);
     Assert.assertEquals(propertyOwner, path.getParent());
     Assert.assertEquals(value, path.getValue());
 }

 @Test
 public void testCreateChild() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     JXPathContext context = new JXPathContext(new Object());
     NodePointer child = nullProperty.createChild(context, new QName("name"));
     Assert.assertNotNull(child);
     Assert.assertEquals(propertyOwner, child.getParent());
 }

 @Test
 public void testCreateChildWithValue() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     JXPathContext context = new JXPathContext(new Object());
     NodePointer child = nullProperty.createChild(context, new QName("name"), TEST_INDEX, null);
     Assert.assertNotNull(child);
     Assert.assertEquals(propertyOwner, child.getParent());
 }

 @Test
 public void testCreateChildWithAttributeValue() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     JXPathContext context = new JXPathContext(new Object());
     nullProperty.setNameAttributeValue("attributeName");
     NodePointer child = nullProperty.createChild(context, new QName("name"), TEST_INDEX, null);
     Assert.assertNotNull(child);
     Assert.assertEquals("attributeName", child.getName().getLocalPart());
 }

 @Test
 public void testAsPath() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer path = nullProperty.createPath(new JXPathContext(new Object()));
     String expectedPath = "[@name='']";
     String actualPath = nullProperty.asPath();
     Assert.assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testAsPathWithNameAttribute() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     nullProperty.setNameAttributeValue("attributeName");
     NodePointer path = nullProperty.createPath(new JXPathContext(new Object()));
     String expectedPath = "[@name='attributeName']";
     String actualPath = nullProperty.asPath();
     Assert.assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testAsPathWithIndex() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer path = nullProperty.createPath(new JXPathContext(new Object()));
     nullProperty.setIndex(TEST_INDEX);
     String expectedPath = "[1]";
     String actualPath = nullProperty.asPath();
     Assert.assertEquals(expectedPath, actualPath);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
     NullPropertyPointer nullProperty = new NullPropertyPointer(null);
     Object value = new Object();
     nullProperty.setValue(value);
 }

 @Test
 public void testSetValueWithParentPropertyOwner() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     Object value = new Object();
     nullProperty.setValue(value);
     PropertyPointer propertyPointer = propertyOwner.getPropertyPointer();
     Assert.assertNotNull(propertyPointer.getValue());
     Assert.assertEquals(value, propertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithParentNullPointer() {
     parent = new NullPointer(null, new QName(""));
     nullProperty = new NullPropertyPointer(parent);
     Object value = new Object();
     nullProperty.setValue(value);
 }

 @Test
 public void testSetValueWithParentNonPropertyPointer() {
     parent = new DOMNodePointer(null, null);
     nullProperty = new NullPropertyPointer(parent);
     Object value = new Object();
     nullProperty.setValue(value);
     Assert.assertEquals(value, parent.getValue());
 }

 @Test
 public void testCreatePathWithParentNullPointer() {
     parent = new NullPointer(null, new QName(""));
     nullProperty = new NullPropertyPointer(parent);
     NodePointer newParent = nullProperty.createPath(new JXPathContext(new Object()));
     Assert.assertNotNull(newParent);
     Assert.assertEquals(parent, newParent.getParent());
 }

 @Test
 public void testCreatePathWithParentPropertyOwner() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer newParent = nullProperty.createPath(new JXPathContext(new Object()));
     Assert.assertNotNull(newParent);
     Assert.assertEquals(propertyOwner, newParent.getParent());
 }

 @Test
 public void testCreatePathWithParentDOMNodePointer() {
     parent = new DOMNodePointer(null, null);
     nullProperty = new NullPropertyPointer(parent);
     NodePointer newParent = nullProperty.createPath(new JXPathContext(new Object()));
     Assert.assertNotNull(newParent);
     Assert.assertEquals(parent, newParent.getParent());
 }

 @Test
 public void testCreatePathWithAttributeValue() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer newParent = nullProperty.createPath(new JXPathContext(new Object()), "attributeName");
     Assert.assertNotNull(newParent);
     Assert.assertEquals(propertyOwner, newParent.getParent());
 }

 @Test
 public void testCreatePathWithIndex() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer newParent = nullProperty.createPath(new JXPathContext(new Object()), TEST_INDEX);
     Assert.assertNotNull(newParent);
     Assert.assertEquals(propertyOwner, newParent.getParent());
 }

 @Test
 public void testCreatePathWithValueAndIndex() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer newParent = nullProperty.createPath(new JXPathContext(new Object()), TEST_INDEX, new Object());
     Assert.assertNotNull(newParent);
     Assert.assertEquals(propertyOwner, newParent.getParent());
     Assert.assertEquals(newObject(), newParent.getValue());
 }

 @Test
 public void testCreateChildWithAttributeValueAndIndex() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer child = nullProperty.createChild(new JXPathContext(new Object()), new QName("name"), TEST_INDEX, "attributeName");
     Assert.assertNotNull(child);
     Assert.assertEquals("attributeName", child.getName().getLocalPart());
     Assert.assertEquals(propertyOwner, child.getParent());
 }

 @Test
 public void testCreateChildWithValueAndIndex() {
     parent = new PropertyOwnerPointer(null);
     propertyOwner = new PropertyOwnerPointer(parent);
     nullProperty = new NullPropertyPointer(propertyOwner);
     NodePointer child = nullProperty.createChild(new JXPathContext(new Object()), new QName("name"), TEST_INDEX, new Object());
     Assert.assertNotNull(child);
     Assert.assertEquals(propertyOwner, child.getParent());
     Assert.assertEquals(newObject(), child.getValue());
 }

}