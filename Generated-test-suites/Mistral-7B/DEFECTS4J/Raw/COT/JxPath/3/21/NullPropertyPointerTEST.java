import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NodePointer nodePointer;
 private JXPathContext context;

 @Before
 public void setUp() {
 nodePointer = new NodePointer(null);
 context = new JXPathContext(null);
 }

 @Test
 public void testNullPropertyPointerConstructor() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 assertNull(nullPropertyPointer.getValuePointer());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertTrue(nullPropertyPointer.isLeaf());
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 nullPropertyPointer.setValue("testValue");
 fail("Should throw JXPathInvalidAccessException with null parent");
 }

 @Test
 public void testCreatePathWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 assertEquals(NodePointer.class, path.getClass());
 assertFalse(path.isActualProperty());
 assertTrue(path.isLeaf());
 assertTrue(path.isContainer());
 }

 @Test
 public void testCreatePathWithEmptyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context, "");
 assertEquals(NodePointer.class, path.getClass());
 assertFalse(path.isActualProperty());
 assertTrue(path.isLeaf());
 assertTrue(path.isContainer());
 }

 @Test
 public void testCreatePathWithNameAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 nullPropertyPointer.setPropertyName("testName");
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 NodePointer path = nullPropertyPointer.createPath(context);
 assertEquals(NodePointer.class, path.getClass());
 assertEquals("testAttribute", path.getName().getLocalPart());
 assertFalse(path.isActualProperty());
 assertTrue(path.isLeaf());
 assertTrue(path.isContainer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNonContainerParent() {
 NodePointer nonContainerPointer = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonContainerPointer);
 nullPropertyPointer.setValue("testValue");
 fail("Should throw JXPathInvalidAccessException with non-container parent");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathWithNonContainerParent() {
 NodePointer nonContainerPointer = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonContainerPointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 fail("Should throw JXPathInvalidAccessException with non-container parent");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathWithNonChangeableLocation() {
 NodePointer nonChangeablePointer = new NodePointer(null, NodeType.ELEMENT_NODE);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonChangeablePointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 fail("Should throw JXPathInvalidAccessException with non-changeable location");
 }

 @Test
 public void testCreatePathWithPropertyOwnerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(nodePointer);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 assertEquals(propertyPointer.getClass(), path.getClass());
 assertEquals(propertyPointer.getName().getLocalPart(), path.getName().getLocalPart());
 assertFalse(path.isActualProperty());
 assertTrue(path.isLeaf());
 assertTrue(path.isContainer());
 }

 @Test
 public void testCreatePathWithNullValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context, null);
 assertEquals(NodePointer.class, path.getClass());
 assertFalse(path.isActualProperty());
 assertTrue(path.isLeaf());
 assertTrue(path.isContainer());
 }

 @Test
 public void testCreatePathWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context, "testValue");
 assertEquals("testValue", path.getValue().toString());
 assertEquals(NodePointer.class, path.getClass());
 assertFalse(path.isActualProperty());
 assertTrue(path.isLeaf());
 assertTrue(path.isContainer());
 }

 @Test
 public void testCreateChildWithEmptyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 NodePointer child = path.createChild(context, new QName(""));
 assertEquals(NodePointer.class, child.getClass());
 assertEquals(path, child.getParentPointer());
 assertEquals(0, child.getIndex());
 assertFalse(child.isActualProperty());
 assertTrue(child.isLeaf());
 assertTrue(child.isContainer());
 }

 @Test
 public void testCreateChildWithName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 NodePointer child = path.createChild(context, new QName("testName"));
 assertEquals("testName", child.getName().getLocalPart());
 assertEquals(NodePointer.class, child.getClass());
 assertEquals(path, child.getParentPointer());
 assertEquals(0, child.getIndex());
 assertFalse(child.isActualProperty());
 assertTrue(child.isLeaf());
 assertTrue(child.isContainer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreateChildWithNonContainerPath() {
 NodePointer nonContainerPath = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer child = nullPropertyPointer.createPath(context).createChild(context, new QName("testName"), 0);
 fail("Should throw JXPathInvalidAccessException with non-container path");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreateChildWithNegativeIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer path = nullPropertyPointer.createPath(context);
 try {
 path.createChild(context, new QName("testName"), -1);
 fail("Should throw JXPathInvalidAccessException with negative index");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}