import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private static final Object TEST_BEAN = new TestBean();

 @Test
 public void testPropertyPointerCreation() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new Object()));
 assertNull(propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerIndex() {
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(TEST_BEAN), "testProperty");
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testPropertyPointerBean() {
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(TEST_BEAN), "testProperty");
 propertyPointer.setPropertyIndex(0);
 assertEquals(TEST_BEAN, propertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerGetName() {
 PropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(TEST_BEAN), "testProperty");
 propertyPointer.setPropertyIndex(0);
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testPropertyPointerGetPropertyCount() {
 TestBean testBean = new TestBean();
 testBean.setProperty1("value1");
 testBean.setProperty2("value2");
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property");
 assertEquals(2, propertyPointer.getPropertyCount());
 }

 @Test
 public void testPropertyPointerGetPropertyNames() {
 TestBean testBean = new TestBean();
 testBean.setProperty1("value1");
 testBean.setProperty2("value2");
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property");
 String[] propertyNames = propertyPointer.getPropertyNames();
 assertEquals(2, propertyNames.length);
 assertEquals("property1", propertyNames[0]);
 assertEquals("property2", propertyNames[1]);
 }

 @Test
 public void testPropertyPointerIsActualProperty() {
 TestBean testBean = new TestBean();
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property");
 testBean.setProperty1("value1");
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testPropertyPointerGetImmediateNode() {
 TestBean testBean = new TestBean();
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");
 testBean.setProperty1("value1");
 assertEquals("value1", propertyPointer.getImmediateNode());
 }

 @Test
 public void testPropertyPointerIsCollection() {
 TestBean testBean = new TestBean();
 testBean.setProperty1(new String[]{"value1", "value2"});
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testPropertyPointerIsLeaf() {
 TestBean testBean = new TestBean();
 testBean.setProperty1("value1");
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerGetLength() {
 TestBean testBean = new TestBean();
 testBean.setProperty1(new String[]{"value1", "value2"});
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");
 assertEquals(2, propertyPointer.getLength());
 }

 @Test
 public void testPropertyPointerCreatePath() {
 TestBean testBean = new TestBean();
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");
 JXPathContext context = new JXPathContext(new Object());
 NodePointer result = propertyPointer.createPath(context);
 assertNotNull(result);
 }

 @Test
 public void testPropertyPointerCreatePathWithValue() {
 TestBean testBean = new TestBean();
 TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer.createPath(context, "value1");
 assertEquals("value1", propertyPointer.getNode().toString());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testPropertyPointerCreatePathException() {
 TestBean testBean = new TestBean();TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");JXPathContext context = new JXPathContext(new Object());propertyPointer.createPath(context, null);}

 @Test
 public void testPropertyPointerClone() {
 TestBean testBean = new TestBean();TestPropertyPointer propertyPointer = new TestPropertyPointer(new NodePointer(testBean), "property1");NodePointer clonedPointer = propertyPointer.clone();assertEquals(propertyPointer, clonedPointer);}

 @Test
 public void testPropertyPointerEquals() {
 TestBean testBean1 = new TestBean();TestBean testBean2 = new TestBean();TestPropertyPointer propertyPointer1 = new TestPropertyPointer(new NodePointer(testBean1), "property1");TestPropertyPointer propertyPointer2 = new TestPropertyPointer(new NodePointer(testBean2), "property1");assertEquals(propertyPointer1, propertyPointer1);assertEquals(propertyPointer1, propertyPointer1.clone());assertEquals(propertyPointer1, propertyPointer2);assertEquals(propertyPointer2, propertyPointer1.clone());}

 @Test
 public void testPropertyPointerHashCode() {
 TestBean testBean = new TestBean();TestPropertyPointer propertyPointer1 = new TestPropertyPointer(new NodePointer(testBean), "property1");TestPropertyPointer propertyPointer2 = new TestPropertyPointer(new NodePointer(testBean), "property2");int hashCode1 = propertyPointer1.hashCode();int hashCode2 = propertyPointer2.hashCode();assertNotEquals(hashCode1, hashCode2);}

 @Test
 public void testPropertyPointerCompareChildNodePointers() {
 TestBean testBean = new TestBean();TestPropertyPointer propertyPointer1 = new TestPropertyPointer(new NodePointer(testBean), "property1");TestPropertyPointer propertyPointer2 = new TestPropertyPointer(new NodePointer(testBean), "property1");NodePointer childNodePointer1 = propertyPointer1.createChild(new JXPathContext(new Object()), null, 0, "child1");NodePointer childNodePointer2 = propertyPointer2.createChild(new JXPathContext(new Object()), null, 0, "child1");assertEquals(0, propertyPointer1.compareChildNodePointers(childNodePointer1, childNodePointer2));}

 @Test
 public void testPropertyPointerCompareChildNodePointersDifferent() {
 TestBean testBean = new TestBean();TestPropertyPointer propertyPointer1 = new TestPropertyPointer(new NodePointer(testBean), "property1");TestPropertyPointer propertyPointer2 = new TestPropertyPointer(new NodePointer(testBean), "property2");NodePointer childNodePointer1 = propertyPointer1.createChild(new JXPathContext(new Object()), null, 0, "child1");NodePointer childNodePointer2 = propertyPointer2.createChild(new JXPathContext(new Object()), null, 0, "child2");assertEquals(-1, propertyPointer1.compareChildNodePointers(childNodePointer1, childNodePointer2));}

}

class TestBean {
 private String property1;
 private String[] property2;

 public String getProperty1() {
 return property1;
 }

 public void setProperty1(String property1) {
 this.property1 = property1;
 }

 public String[] getProperty2() {
 return property2;
 }

 public void setProperty2(String[] property2) {
 this.property2 = property2;
 }

 @Override
 public String toString() {
 return "TestBean{" +
 "property1='" + property1 + '\'' +
 ", property2=" + (property2 == null ? "null" : property2.toString()) +
 '}';
 }
}

class TestPropertyPointer extends PropertyPointer {

 public TestPropertyPointer(NodePointer parent, String propertyName) {
 super(parent);
 setPropertyName(propertyName);
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"property1"};
 }

 @Override
 public String getPropertyName() {
 return "property";
 }

 @Override
 public void setPropertyName(String propertyName) {
 super.setPropertyName(propertyName);
}

}