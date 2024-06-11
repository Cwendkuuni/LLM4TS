import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 // Test case for the constructor that takes a Class argument
 @Test
 public void testSimpleTypeConstruct() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 assertEquals("Expected class 'String' for SimpleType instance", simpleClass, simpleType.getRawClass());
 }

 // Test case for the constructor that takes a Class and TypeBindings arguments
 @Test
 public void testSimpleTypeConstructWithBindings() {
 Class<?> simpleClass = String[].class;
 JavaType superClass = JavaType.constructType(List.class);
 TypeBindings bindings = new TypeBindings();
 bindings.put(0, Integer.class);
 SimpleType simpleType = new SimpleType(simpleClass, bindings, superClass, null);
 assertEquals("Expected class 'String[]' for SimpleType instance", simpleClass, simpleType.getRawClass());
 assertEquals("Expected binding for Integer at index 0", Integer.class, simpleType.containedType(0).getRawClass());
 }

 // Test case for the copy constructor
 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<?> simpleClass = String.class;
 SimpleType originalSimpleType = new SimpleType(simpleClass);
 SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
 assertEquals("Expected copied SimpleType to have same class as original", simpleClass, copiedSimpleType.getRawClass());
 }

 // Test case for the constructUnsafe method
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructUnsafeWithInvalidClass() {
 Class<?> invalidClass = List.class;
 SimpleType simpleType = SimpleType.constructUnsafe(invalidClass);
 fail("Expected IllegalArgumentException when constructing SimpleType with invalid class");
 }

 // Test case for the construct method (deprecated)
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructWithMapClass() {
 Class<?> mapClass = HashMap.class;
 SimpleType simpleType = SimpleType.construct(mapClass);
 fail("Expected IllegalArgumentException when constructing SimpleType with Map class");
 }

 // Test case for the _narrow method
 @Test
 public void testSimpleTypeNarrow() {
 Class<?> stringClass = String.class;
 Class<?> stringSubclass = StringBuffer.class;
 SimpleType simpleType = new SimpleType(stringClass);
 SimpleType narrowedType = simpleType._narrow(stringSubclass);
 assertEquals("Expected narrowed SimpleType to have same class as subclass", stringSubclass, narrowedType.getRawClass());
 }

 // Test case for the equals method
 @Test
 public void testSimpleTypeEquals() {
 Class<?> simpleClass1 = Integer.class;
 Class<?> simpleClass2 = Integer.class;
 SimpleType simpleType1 = new SimpleType(simpleClass1);
 SimpleType simpleType2 = new SimpleType(simpleClass2);
 assertTrue("Expected SimpleTypes with same classes to be equal", simpleType1.equals(simpleType2));
 }

 // Test case for the getErasedSignature method
 @Test
 public void testSimpleTypeGetErasedSignature() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 StringBuilder expectedSignature = new StringBuilder("Ljava/lang/String;");
 StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
 assertEquals("Expected erased signature to match", expectedSignature.toString(), actualSignature.toString());
 }

 // Test case for the getGenericSignature method
 @Test
 public void testSimpleTypeGetGenericSignature() {
 Class<?> listClass = List.class;
 SimpleType simpleType = new SimpleType(listClass);
 StringBuilder expectedSignature = new StringBuilder("[Ljava/lang/Object;");
 StringBuilder actualSignature = simpleType.getGenericSignature(new StringBuilder());
 assertEquals("Expected generic signature to match", expectedSignature.toString(), actualSignature.toString());
 }

 // Test case for the isContainerType method
 @Test
 public void testSimpleTypeIsContainerType() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 assertFalse("Expected SimpleType to not be a container type", simpleType.isContainerType());
 }

}