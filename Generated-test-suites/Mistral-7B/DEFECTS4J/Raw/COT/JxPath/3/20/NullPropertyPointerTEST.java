import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

 private NodePointer nullPointer;
 private JXPathContext context;

 @Test
 public void testNullPropertyPointerConstruction() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 Assert.assertNull(nullPropertyPointer.getImmediateNode());
 Assert.assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetName() {
 nullPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 QName expectedName = new QName("testProperty");
 QName actualName = nullPropertyPointer.getName();
 Assert.assertEquals(expectedName, actualName);
 }

 @Test
 public void testSetPropertyIndex() {
 nullPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyIndex(1);
 int expectedIndex = 1;
 int actualIndex = nullPropertyPointer.getPropertyIndex();
 Assert.assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testGetLength() {
 nullPointer = new NullPropertyPointer(null);
 int expectedLength = 0;
 int actualLength = nullPointer.getLength();
 Assert.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetBaseValue() {
 nullPointer = new NullPropertyPointer(null);
 Object expectedBaseValue = null;
 Object actualBaseValue = nullPointer.getBaseValue();
 Assert.assertEquals(expectedBaseValue, actualBaseValue);
 }

 @Test
 public void testGetImmediateNode() {
 nullPointer = new NullPropertyPointer(null);
 Assert.assertNull(nullPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 nullPointer = new NullPropertyPointer(null);
 boolean expectedLeaf = true;
 boolean actualLeaf = nullPointer.isLeaf();
 Assert.assertEquals(expectedLeaf, actualLeaf);
 }

 @Test
 public void testCreatePathWithContext() {
 context = new JXPathContext(new Object());
 nullPointer = new NullPropertyPointer(null);
 NodePointer expectedPath = null;
 NodePointer actualPath = nullPointer.createPath(context);
 Assert.assertNull(actualPath);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNullParent() {
 nullPointer = new NullPropertyPointer(null);
 nullPointer.setValue("testValue");
 }

 @Test
 public void testSetPropertyName() {
 nullPointer = new NullPropertyPointer(null);
 nullPointer.setPropertyName("testProperty");
 String expectedPropertyName = "testProperty";
 String actualPropertyName = nullPointer.getPropertyName();
 Assert.assertEquals(expectedPropertyName, actualPropertyName);
 }

 @Test
 public void testSetNameAttributeValue() {
 nullPointer = new NullPropertyPointer(null);
 nullPointer.setNameAttributeValue("testProperty");
 String expectedPropertyName = "testProperty";
 String actualPropertyName = nullPointer.getPropertyName();
 Assert.assertEquals(expectedPropertyName, actualPropertyName);
 Assert.assertTrue(nullPointer.isByNameAttribute());
 }

 @Test
 public void testGetPropertyNameWithByNameAttribute() {
 nullPointer = new NullPropertyPointer(null);
 nullPointer.setNameAttributeValue("testProperty");
 String expectedPropertyName = "testProperty";
 String actualPropertyName = nullPointer.getPropertyName();
 Assert.assertEquals(expectedPropertyName, actualPropertyName);
 }

 @Test
 public void testIsCollection() {
 nullPointer = new NullPropertyPointer(null);
 boolean expectedCollection = false;
 boolean actualCollection = nullPointer.isCollection();
 Assert.assertEquals(expectedCollection, actualCollection);
 }

 @Test
 public void testGetPropertyNames() {
 nullPointer = new NullPropertyPointer(null);
 String[] expectedPropertyNames = new String[0];
 String[] actualPropertyNames = nullPointer.getPropertyNames();
 Assert.assertArrayEquals(expectedPropertyNames, actualPropertyNames);
 }

 @Test
 public void testGetPropertyCount() {
 nullPointer = new NullPropertyPointer(null);int expectedPropertyCount = 0;
 int actualPropertyCount = nullPointer.getPropertyCount();
 Assert.assertEquals(expectedPropertyCount, actualPropertyCount);
 }

 @Test
 public void testAsPathWithByNameAttribute() {
 nullPointer = new NullPropertyPointer(null);
 nullPointer.setNameAttributeValue("testProperty");
 nullPointer.setIndex(1);
 String expectedPath = "[@name='testProperty'][1]";
 String actualPath = nullPointer.asPath();
 Assert.assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testCreatePathWithValue() {
 context = new JXPathContext(new Object());
 nullPointer = new NullPropertyPointer(null);
 NodePointer expectedPath = null;
 NodePointer actualPath = nullPointer.createPath(context, "testValue");
 Assert.assertNull(actualPath);
 }

 @Test
 public void testCreateChildWithContextAndName() {
 context = new JXPathContext(new Object());
 nullPointer = new NullPropertyPointer(null);
 NodePointer expectedChild = null;
 NodePointer actualChild = nullPointer.createChild(context, new QName("testProperty"));
 Assert.assertNull(actualChild);
 }

 @Test
 public void testCreateChildWithContextAndNameAndValue() {
 context = new JXPathContext(new Object());
 nullPointer = new NullPropertyPointer(null);
 NodePointer expectedChild = null;
 NodePointer actualChild = nullPointer.createChild(context, new QName("testProperty"), 1, "testValue");
 Assert.assertNull(actualChild);
 }

 @Test
 public void testCreateChildWithContextAndNameAndIndex() {
 context = new JXPathContext(new Object());
 nullPointer = new NullPropertyPointer(null);
 NodePointer expectedChild = null;
 NodePointer actualChild = nullPointer.createChild(context, new QName("testProperty"), 1);
 Assert.assertNull(actualChild);
 }
}