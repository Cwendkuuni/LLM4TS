import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private Object bean;

 @Before
 public void setUp() {
 bean = new Object() {
 public String property1 = "test";
 public String property2 = "test2";
 public String[] propertyArray = {"test1", "test2", "test3"};
 };
 propertyPointer = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 propertyPointer1.setPropertyIndex(0);
 Assert.assertEquals(-1, propertyPointer1.getPropertyIndex());
 propertyPointer1.setPropertyIndex(Integer.MIN_VALUE);
 Assert.assertEquals(Integer.MIN_VALUE, propertyPointer1.getPropertyIndex());
 propertyPointer1.setPropertyIndex(1);
 Assert.assertEquals(0, propertyPointer1.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Assert.assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 Assert.assertEquals(new QName(null, "property1"), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyName() {
 Assert.assertEquals("property1", propertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 propertyPointer.setPropertyName("property2");
 Assert.assertEquals("property2", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property2";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "propertyArray";
 }
 };
 PropertyPointer propertyPointer4 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return null;
 }
 };
 PropertyPointer[] propertyPointers = {propertyPointer1, propertyPointer2, propertyPointer3, propertyPointer4};
 PropertyPointerFactory propertyPointerFactory = new PropertyPointerFactory();
 List<NodePointer> nodePointers = Arrays.asList(propertyPointer1, propertyPointer2, propertyPointer3, propertyPointer4);
 List<NodePointer> actualNodePointers = propertyPointerFactory.createPropertyNodePointers(new JXPathContext(new Object()), nodePointers);
 for (int i = 0; i < propertyPointers.length; i++) {
 Assert.assertEquals(nodePointers.get(i), actualNodePointers.get(i));
 }
 Assert.assertEquals(4, propertyPointer3.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 Assert.assertArrayEquals(new String[]{"property1", "property2"}, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 Assert.assertTrue(propertyPointer.isActualProperty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetActualPropertyFail() {
 propertyPointer.setActualProperty(false);
 }

 @Test
 public void testGetImmediateNode() {
 Assert.assertEquals("test", propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 Assert.assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "propertyArray";
 }
 };
 Assert.assertEquals(3, propertyPointer1.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 NodePointer immediateValuePointer = propertyPointer1.getImmediateValuePointer();
 Assert.assertNotNull(immediateValuePointer);
 Assert.assertEquals("test", immediateValuePointer.getNode());
 }

 @Test
 public void testCreatePath() {
 JXPathContext jxpathContext = new JXPathContext(bean);
 NodePointer propertyPointerNode = propertyPointer.createPath(jxpathContext);
 Assert.assertEquals(propertyPointer, propertyPointerNode);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext jxpathContext = new JXPathContext(bean);
 propertyPointer.createPath(jxpathContext, "test3");
 Assert.assertEquals("test3", propertyPointer.getImmediateNode());
 }

 @Test
 public void testCreateChild() {
 JXPathContext jxpathContext = new JXPathContext(bean);
 PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 NodePointer childNodePointer = childPropertyPointer.createChild(jxpathContext, null, 1, "test4");
 Assert.assertEquals("test4", childNodePointer.getNode());
 }

 @Test
 public void testCreateChildWithName() {
 JXPathContext jxpathContext = new JXPathContext(bean);
 PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
 @Override
 public String getPropertyName() {
 return "property2";
 }
 };
 NodePointer childNodePointer = childPropertyPointer.createChild(jxpathContext, new QName("test"), 1, "test4");
 Assert.assertEquals("test4", childNodePointer.getNode());
 }

 @Test
 public void testClone() {
 NodePointer clonedNodePointer = propertyPointer.clone();
 Assert.assertEquals(propertyPointer, clonedNodePointer);
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 NodePointer nodePointer1 = new NodePointer(new Object()) {
 @Override
 public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
 return 0;
 }
 };
 NodePointer nodePointer2 = new NodePointer(new Object()) {
 @Override
 public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
 return 1;
 }
 };
 Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2));
 Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));
 Assert.assertEquals(1, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2));
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property2";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
 Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property2";
 }
 };
 PropertyPointer propertyPointer4 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 Assert.assertTrue(propertyPointer1.equals(propertyPointer1));
 Assert.assertTrue(propertyPointer1.equals(propertyPointer4));
 Assert.assertFalse(propertyPointer1.equals(propertyPointer2));
 Assert.assertFalse(propertyPointer1.equals(propertyPointer3));
 }

 @Test
 public void testCompareChildNodePointersNull() {
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer(new Object())) {
 @Override
 public String getPropertyName() {
 return "property1";
 }
 };
 NodePointer nodePointer1 = null;
 Assert.assertNull(propertyPointer1.compareChildNodePointers(propertyPointer1, nodePointer1));
 }
}