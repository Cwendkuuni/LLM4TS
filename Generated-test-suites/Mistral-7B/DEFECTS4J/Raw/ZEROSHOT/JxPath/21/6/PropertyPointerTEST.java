import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstruction() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 assertEquals(-1, propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testSettingPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(null));
 propertyPointer.setPropertyIndex(5);
 assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGettingBean() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new Object()));
 propertyPointer.setBean(new Object());
 assertEquals(propertyPointer.getBean(), new Object());
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingBeanTwice() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new Object()));
 propertyPointer.setBean(new Object());
 propertyPointer.setBean(new Object());
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 assertEquals(new QName("testProperty"), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public int getPropertyCount() {
 return 5;
 }
 };
 assertEquals(5, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"name1", "name2", "name3"};
 }
 };
 assertArrayEquals(new String[]{"name1", "name2", "name3"}, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){
 @Override
 public String toString() {
 return "{name: 'testName', value: 'testValue'}";
 }
 };
 }
 @Override
 public Object getImmediateNode() {
 return "testValue";
 }
 };
 assertEquals("testValue", propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return "testValue";
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2", "testValue3"};
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){
 @Override
 public String toString() {
 return "{name: 'testName', value: [{}, {}]}";
 }
 };
 }
 @Override
 public NodePointer createPath(JXPathContext context) {
 return new NodePointer(new Object[]{
 new Object(){
 @Override
 public String toString() {
 return "{name: 'testName1', value: 'testValue1'}";
 }
 },
 new Object(){
 @Override
 public String toString() {
 return "{name: 'testName2', value: 'testValue2'}";
 }
 }
 });
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(new JXPathContext());
 assertEquals(2, nodePointer.getLength());
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object(){
 @Override
 public String toString() {
 return "{name: 'testName', value: [{}, {}]}";
 }
 };
 }
 @Override
 public NodePointer createPath(JXPathContext context, Object value) {
 return new NodePointer(new Object[]{value});
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(new JXPathContext(), "testValue");
 assertEquals("testValue", nodePointer.getNode());
 }

 @Test
 public void testCreateChildNodePointer() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object(), new Object()};
 }
 };
 NodePointer childNodePointer1 = propertyPointer.createChild(new JXPathContext(), null, 0, null);
 NodePointer childNodePointer2 = propertyPointer.createChild(new JXPathContext(), null, 1, null);
 assertEquals(2, propertyPointer.getLength());
 assertEquals("testProperty[0]", childNodePointer1.asPath().toString());
 assertEquals("testProperty[1]", childNodePointer2.asPath().toString());
 }

 @Test
 public void testCreateChildNodePointerWithName() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object(), new Object()};
 }
 };
 NodePointer childNodePointer1 = propertyPointer.createChild(new JXPathContext(), new QName("childName"), 0, null);
 NodePointer childNodePointer2 = propertyPointer.createChild(new JXPathContext(), new QName("childName"), 1, null);
 assertEquals(2, propertyPointer.getLength());
 assertEquals("testProperty/childName[0]", childNodePointer1.asPath().toString());
 assertEquals("testProperty/childName[1]", childNodePointer2.asPath().toString());
 }

 @Test
 public void testCreateChildNodePointerWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object(), new Object()};
 }
 };
 NodePointer childNodePointer1 = propertyPointer.createChild(new JXPathContext(), null, 0, "testValue");
 NodePointer childNodePointer2 = propertyPointer.createChild(new JXPathContext(), null, 1, "testValue");
 assertEquals(2, propertyPointer.getLength());
 assertEquals("testProperty[0]", childNodePointer1.asPath().toString());
 assertEquals("testProperty[1]", childNodePointer2.asPath().toString());
 assertEquals("testValue", childNodePointer1.getNode().toString());
 assertEquals("testValue", childNodePointer2.getNode().toString());
 }

 @Test
 public void testCreateChildNodePointerWithNameAndValue() {
 PropertyPointer propertyPointer = new PropertyPointer("testProperty") {
 @Override
 public Object getBaseValue() {
 return new Object[]{new Object(), new Object()};
 }
 };
 NodePointer childNodePointer1 = propertyPointer.createChild(new JXPathContext(), new QName("childName"), 0, "testValue");
 NodePointer childNodePointer2 = propertyPointer.createChild(new JXPathContext(), new QName("childName"), 1, "testValue");
 assertEquals(2, propertyPointer.getLength());
 assertEquals("testProperty/childName[0]", childNodePointer1.asPath().toString());
 assertEquals("testProperty/childName[1]", childNodePointer2.asPath().toString());
 assertEquals("testValue", childNodePointer1.getNode().toString());
 assertEquals("testValue", childNodePointer2.getNode().toString());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1") {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer("testProperty2") {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer("testProperty1") {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1") {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer("testProperty2") {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer("testProperty1") {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer4 = new PropertyPointer("testProperty1") {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 assertTrue(propertyPointer1.equals(propertyPointer3));
 assertFalse(propertyPointer1.equals(propertyPointer2));
 assertFalse(propertyPointer1.equals(propertyPointer4));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new PropertyPointer("testProperty1") {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer("testProperty2") {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 NodePointer nodePointer1 = new NodePointer("node1");
 NodePointer nodePointer2 = new NodePointer("node2");
 assertEquals(propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2), -1);
 }

}