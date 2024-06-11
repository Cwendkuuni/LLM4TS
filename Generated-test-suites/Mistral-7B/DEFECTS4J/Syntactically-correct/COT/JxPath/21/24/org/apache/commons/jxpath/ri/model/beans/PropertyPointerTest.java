import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private NodePointer parentPointer;

 @Test
 public void testPropertyPointerConstructor() {
 propertyPointer = new PropertyPointer(parentPointer);
 Assert.assertNotNull(propertyPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 Assert.assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer.setPropertyIndex(2);
 propertyPointer.setPropertyIndex(1);
 Assert.assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(bean);
 Assert.assertEquals(bean, propertyPointer.getBean());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBeanWhenNullBean() {
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(null);
 propertyPointer.getBean();
 }

 @Test
 public void testGetName() {
 String propertyName = "testProperty";
 propertyPointer.setPropertyName(propertyName);
 Assert.assertEquals(new QName(null, propertyName), propertyPointer.getName());
 }

 @Test(expected = NullPointerException.class)
 public void testSetPropertyNameWhenNullPropertyName() {
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testGetPropertyCount() throws Exception {
 int propertyCount = 3;
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyCount() {
 return propertyCount;
 }
 };
 Assert.assertEquals(propertyCount, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() throws Exception {
 String[] propertyNames = new String[]{"testProperty1", "testProperty2", "testProperty3"};
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public String[] getPropertyNames() {
 return propertyNames;
 }
 };
 List<String> expectedList = new ArrayList<>(3);
 for (String name : propertyNames) {
 expectedList.add(name);
 }
 Assert.assertArrayEquals(expectedList.toArray(new String[0]), propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() throws Exception {
 boolean isActual = true;
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 protected boolean isActualProperty() {
 return isActual;
 }
 };
 Assert.assertEquals(isActual, propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() throws Exception {
 boolean isActual = true;
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 protected boolean isActualProperty() {
 return isActual;
 }
 };
 Assert.assertEquals(isActual, propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 Object immediateNode = new Object();
 propertyPointer.setValue(immediateNode);
 Assert.assertEquals(immediateNode, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 Object collection = new Object()[3];
 propertyPointer.setValue(collection);
 Assert.assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 Object leaf = 10;
 propertyPointer.setValue(leaf);
 Assert.assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() throws Exception {
 int length = 2;
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getLength() {
 return length;
 }
 };
 Assert.assertEquals(length, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 Object value = new Object();
 propertyPointer.setValue(value);
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 Assert.assertEquals(propertyPointer, immediateValuePointer.getParentPointer());
 Assert.assertEquals(new QName(null, propertyPointer.getPropertyName()), immediateValuePointer.getName());
 Assert.assertEquals(value, immediateValuePointer.getNode());
 }

 @Test
 public void testCreatePath() throws Exception {
 Object bean = new Object();
 JXPathContext context = new JXPathContext(bean);
 PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public NodePointer createPath(JXPathContext context, Object value) {
 return null;
 }
 };
 Assert.assertNull(propertyPointer.createPath(context, new Object()));
 }

 @Test
 public void testCreatePathWithValue() throws Exception {
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer = new PropertyPointer(parentPointer);
 Object value = new Object();
 NodePointer nodePointer = propertyPointer.createPath(context, value);
 Assert.assertEquals(propertyPointer, nodePointer.getParentPointer());
 Assert.assertEquals(new QName(null, propertyPointer.getPropertyName()), nodePointer.getName());
 Assert.assertEquals(value, nodePointer.getNode());
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
 return null;
 }
 };
 Assert.assertNull(propertyPointer.createChild(context, null, 0, new Object()));
 }

 @Test
 public void testCreateChildWithName() throws Exception {
 JXPathContext context = new JXPathContext(new Object());
 String childPropertyName = "child";
 PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
 @Override
 public String getPropertyName() {
 return childPropertyName;
 }
 };
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, new Object());
 Assert.assertNotNull(childNodePointer);
 Assert.assertEquals(childPropertyPointer, childNodePointer.getParentPointer());
 Assert.assertEquals(new QName(null, childPropertyName), childNodePointer.getName());
 }

 @Test
 public void testCreateChildWithIndex() throws Exception {
 JXPathContext context = new JXPathContext(new Object());
 String childPropertyName = "child";
 PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
 @Override
 public String getPropertyName() {
 return childPropertyName;
 }
 };
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, new Object());
 Assert.assertNull(childNodePointer);
 }

 @Test
 public void testCreateChildWithIndexAndName() throws Exception {
 JXPathContext context = new JXPathContext(new Object());
 String childPropertyName = "child";
 PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
 @Override
 public String getPropertyName() {
 return childPropertyName;
 }
 };
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName(null, childPropertyName), 0, new Object());
 Assert.assertNotNull(childNodePointer);
 Assert.assertEquals(childPropertyPointer, childNodePointer.getParentPointer());
 Assert.assertEquals(new QName(null, childPropertyName), childNodePointer.getName());
 }

}