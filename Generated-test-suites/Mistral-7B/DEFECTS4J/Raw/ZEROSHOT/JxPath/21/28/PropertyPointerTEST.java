import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstruction() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 assertEquals(1, propertyPointer.getPropertyCount());
 assertArrayEquals(new String[]{"testProperty"}, propertyPointer.getPropertyNames());
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testPropertyPointerGetBean() {
 Object testObject = new Object() {};
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBean() {
 return testObject;
 }
 };
 assertEquals(testObject, propertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerSetBean() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 propertyPointer.setBean(new Object() {});
 assertEquals(new Object(), propertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerGetPropertyIndex() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 assertSame(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testPropertyPointerSetPropertyIndex() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 propertyPointer.setPropertyIndex(42);
 assertSame(42, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testPropertyPointerGetPropertyIndexWhenUninitialized() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 assertSame(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testPropertyPointerIsActualProperty() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 assertFalse(propertyPointer.isActualProperty());
 }

 @Test
 public void testPropertyPointerIsActualPropertyWhenInitialized() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testPropertyPointerGetImmediateNode() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public int hashCode() {
 return 42;
 }
 };
 }
 };
 assertSame(new Object(), propertyPointer.getImmediateNode());
 }

 @Test
 public void testPropertyPointerIsCollection() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object()[3];
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testPropertyPointerIsLeaf() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new String("test");
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerGetLength() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[3];
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testPropertyPointerGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {};
 }
 };
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(immediateValuePointer);
 }

 @Test
 public void testPropertyPointerCreatePath() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {};
 }
 };
 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals(propertyPointer, nodePointer);
 }

 @Test
 public void testPropertyPointerCreatePathWithValue() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {};
 }
 };
 JXPathContext context = JXPathContext.newContext(new Object());
 PropertyPointer nodePointer = (PropertyPointer) propertyPointer.createPath(context, new Object());
 assertNotNull(nodePointer);
 }

 @Test
 public void testPropertyPointerCreateChild() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {};
 }
 };
 JXPathContext context = JXPathContext.newContext(new Object());
 PropertyPointer childNodePointer1 = propertyPointer.createChild(context, null, 0, null);
 PropertyPointer childNodePointer2 = propertyPointer.createChild(context, null, 1, null);
 assertNotNull(childNodePointer1);
 assertNotNull(childNodePointer2);
 }

 @Test
 public void testPropertyPointerCreateChildWithName() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object() {};
 }
 };
 JXPathContext context = JXPathContext.newContext(new Object());
 PropertyPointer childNodePointer = propertyPointer.createChild(context, new QName("testName"), 0, null);
 assertNotNull(childNodePointer);
 assertEquals("testName", childNodePointer.getName().getLocalPart());
 }

 @Test
 public void testPropertyPointerCreateChildWithIndexAndName() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object()[2];
 }
 };
 JXPathContext context = JXPathContext.newContext(new Object());
 PropertyPointer childNodePointer = propertyPointer.createChild(context, new QName("testName"), 1, null);
 assertNotNull(childNodePointer);
 assertEquals("testName", childNodePointer.getName().getLocalPart());
 assertEquals(1, childNodePointer.getIndex());
 }

 @Test
 public void testPropertyPointerHashCode() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 PropertyPointer propertyPointer2 = new AbstractPropertyPointer(null) {};
 propertyPointer.setIndex(1);
 propertyPointer2.setIndex(1);
 PropertyPointer propertyPointer3 = new AbstractPropertyPointer(null) {};
 PropertyPointer propertyPointer4 = new AbstractPropertyPointer(null) {};
 propertyPointer3.setParent(new NodePointer(null));
 propertyPointer4.setParent(new NodePointer(null));
 int hashCode = propertyPointer.hashCode();
 assertEquals(hashCode, propertyPointer2.hashCode());
 assertNotEquals(hashCode, propertyPointer3.hashCode());
 assertNotEquals(hashCode, propertyPointer4.hashCode());
 }

 @Test
 public void testPropertyPointerEquals() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 PropertyPointer propertyPointer2 = new AbstractPropertyPointer(null) {};
 PropertyPointer propertyPointer3 = new AbstractPropertyPointer(null) {};
 PropertyPointer propertyPointer4 = new AbstractPropertyPointer(null) {};
 propertyPointer.setParent(new NodePointer(null));
 propertyPointer2.setParent(new NodePointer(null));
 propertyPointer3.setParent(new NodePointer(null));
 propertyPointer4.setParent(new NodePointer(null));
 propertyPointer.setIndex(1);
 propertyPointer2.setIndex(1);
 propertyPointer3.setIndex(2);
 propertyPointer4.setIndex(2);
 assertFalse(propertyPointer.equals(propertyPointer2));
 assertTrue(propertyPointer.equals(propertyPointer));
 assertFalse(propertyPointer.equals(propertyPointer3));
 assertFalse(propertyPointer.equals(propertyPointer4));
 }

 @Test
 public void testPropertyPointerCompareChildNodePointers() {
 PropertyPointer propertyPointer = new AbstractPropertyPointer(null) {};
 NodePointer nodePointer1 = new NodePointer(null);
 NodePointer nodePointer2 = new NodePointer(null);
 int result1 = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
 int result2 = propertyPointer.compareChildNodePointers(nodePointer2, nodePointer1);
 int result3 = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer1);
 int result4 = propertyPointer.compareChildNodePointers(nodePointer2, nodePointer2);
 int result5 = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
 int result6 = propertyPointer.compareChildNodePointers(nodePointer2, nodePointer1);

 assertEquals(0, result3);
 assertEquals(-1, result5);
 assertEquals(1, result5);
 assertEquals(-1, result6);
 assertEquals(1, result6);
 }
}