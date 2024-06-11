import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.ChildPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointer() {
 PropertyPointer propertyPointer = new ChildPropertyPointer(null, new Object(), "testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
 assertNull(propertyPointer.getBean());
 assertFalse(propertyPointer.isActual());
 assertEquals(-1, propertyPointer.getLength());
 assertFalse(propertyPointer.isCollection());
 assertFalse(propertyPointer.isLeaf());

 PropertyPointer parentPropertyPointer = new ChildPropertyPointer(null, new Object(), "parentProperty");
 PropertyPointer propertyPointerWithParent = new ChildPropertyPointer(parentPropertyPointer, new Object(), "testProperty");
 assertEquals("parentProperty.testProperty", propertyPointerWithParent.asPath());
 assertEquals("testProperty", propertyPointerWithParent.getPropertyName());
 assertSame(parentPropertyPointer, propertyPointerWithParent.getImmediateParentPointer());
 assertSame(new Object(), propertyPointerWithParent.getBean());
 assertTrue(propertyPointerWithParent.isActual());
 assertEquals(0, propertyPointerWithParent.getLength());
 assertFalse(propertyPointerWithParent.isCollection());
 assertFalse(propertyPointerWithParent.isLeaf());
 }

 @Test
 public void testPropertyPointerWithCollection() {
 Object collection = new String[]{"test1", "test2"};
 PropertyPointer collectionPropertyPointer = new ArrayPropertyPointer(null, new Object(), "testProperty", 0);
 assertEquals("testProperty[0]", collectionPropertyPointer.getPropertyName());
 assertNull(collectionPropertyPointer.getBean());
 assertFalse(collectionPropertyPointer.isActual());
 assertEquals(2, collectionPropertyPointer.getLength());
 assertTrue(collectionPropertyPointer.isCollection());
 assertFalse(collectionPropertyPointer.isLeaf());

 PropertyPointer parentPropertyPointer = new ChildPropertyPointer(null, new Object(), "testProperty");
 PropertyPointer collectionPropertyPointerWithParent = new ArrayPropertyPointer(parentPropertyPointer, new Object(), "testProperty", 0);
 assertEquals("testProperty[0]", collectionPropertyPointerWithParent.getPropertyName());
 assertEquals(collection, collectionPropertyPointerWithParent.getNode());
 assertTrue(collectionPropertyPointerWithParent.isActual());
 assertEquals("test1", collectionPropertyPointerWithParent.getImmediateNode().toString());
 assertEquals(1, collectionPropertyPointerWithParent.getLength());
 assertTrue(collectionPropertyPointerWithParent.isCollection());
 assertFalse(collectionPropertyPointerWithParent.isLeaf());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPropertyPointerNegativeIndex() {
 Object collection = new String[]{"test1", "test2"};
 PropertyPointer collectionPropertyPointer = new ArrayPropertyPointer(null, new Object(), "testProperty", 1);
 assertEquals("testProperty[1]", collectionPropertyPointer.getPropertyName());
 assertNull(collectionPropertyPointer.getBean());
 assertFalse(collectionPropertyPointer.isActual());
 assertEquals(2, collectionPropertyPointer.getLength());
 assertTrue(collectionPropertyPointer.isCollection());
 assertFalse(collectionPropertyPointer.isLeaf());

 PropertyPointer parentPropertyPointer = new ChildPropertyPointer(null, new Object(), "testProperty");
 PropertyPointer collectionPropertyPointerWithParent = new ArrayPropertyPointer(parentPropertyPointer, new Object(), "testProperty", -1);
 assertEquals("testProperty[-1]", collectionPropertyPointerWithParent.getPropertyName());
 assertSame(parentPropertyPointer, collectionPropertyPointerWithParent.getImmediateParentPointer());
 assertSame(new Object(), collectionPropertyPointerWithParent.getBean());
 assertFalse(collectionPropertyPointerWithParent.isActual());
 assertEquals(-1, collectionPropertyPointerWithParent.getLength());
 assertFalse(collectionPropertyPointerWithParent.isCollection());
 assertFalse(collectionPropertyPointerWithParent.isLeaf());
 }

 @Test
 public void testPropertyPointerWithValues() {
 Object bean = new Object() {{
 @SuppressWarnings("unchecked")
 int testProperty = 1;
 String testString = "test";
 }};
 PropertyPointer testPropertyPropertyPointer = new ScalarPropertyPointer(null, bean, "testProperty");
 assertEquals("testProperty", testPropertyPropertyPointer.getPropertyName());
 assertSame(bean, testPropertyPropertyPointer.getBean());
 assertTrue(testPropertyPropertyPointer.isActual());
 assertEquals(1, testPropertyPropertyPointer.getLength());
 assertFalse(testPropertyPropertyPointer.isCollection());
 assertFalse(testPropertyPropertyPointer.isLeaf());
 assertEquals(1, ((Number)testPropertyPropertyPointer.getImmediateNode()).intValue());

 PropertyPointer testStringPropertyPointer = new ScalarPropertyPointer(null, bean, "testString");
 assertEquals("testString", testStringPropertyPointer.getPropertyName());
 assertSame(bean, testStringPropertyPointer.getBean());
 assertTrue(testStringPropertyPointer.isActual());
 assertEquals(1, testStringPropertyPointer.getLength());
 assertFalse(testStringPropertyPointer.isCollection());
 assertFalse(testStringPropertyPointer.isLeaf());
 assertEquals("test", testStringPropertyPointer.getImmediateNode().toString());
 }

 @Test(expected = NoSuchMethodException.class)
 public void testInvalidPropertyPointer() {
 Object bean = new Object() {{
 @SuppressWarnings("unchecked")
 int testProperty = 1;
 String testString = "test";
 int invalidProperty = new Object();
 }};
 PropertyPointer invalidPropertyPointer = new ScalarPropertyPointer(null, bean, "invalidProperty");
 }
}