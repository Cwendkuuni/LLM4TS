import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType, TypeBindings bindings) {
 return new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testConstructor() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 assertNotNull(actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 JavaType ref = new ClassType(Integer.class);
 actual.setReference(ref);
 actual.setReference(new ClassType(Long.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 JavaType expected = new ClassType(String.class);
 JavaType result = actual.getSelfReferencedType();
 assertEquals(expected, result);
 }

 @Test
 public void testGetGenericSignature() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder result = actual.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testGetErasedSignature() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder result = actual.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), result.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentType() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 JavaType contentType = new ClassType(Integer.class);
 JavaType result = actual.withContentType(contentType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithTypeHandler() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 Object handler = new Object();
 JavaType result = actual.withTypeHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentTypeHandler() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 Object handler = new Object();
 JavaType result = actual.withContentTypeHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithValueHandler() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 Object handler = new Object();
 JavaType result = actual.withValueHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentValueHandler() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 Object handler = new Object();
 JavaType result = actual.withContentValueHandler(handler);
 }

 @Test
 public void testWithStaticTyping() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 JavaType result = actual.withStaticTyping();
 }

 @Test
 public void testNarrow() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 Class<?> subclass = Integer.class;
 Class<?> result = actual._narrow(subclass);
 assertNull(result);
 }

 @Test
 public void testRefine() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 Class<?> rawType = Integer.class;
 TypeBindings newBindings = new TypeBindings();
 JavaType superClass = new ClassType(Long.class);
 JavaType[] superInterfaces = new JavaType[] { new ClassType(Comparable.class) };
 JavaType result = actual.refine(rawType, newBindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 boolean result = actual.isContainerType();
 assertFalse(result);
 }

 @Test
 public void testToString() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 String expected = "[recursive type; UNSOLVED]";
 String result = actual.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual1 = createTestType(erasedType, bindings);
 ResolvedRecursiveType actual2 = createTestType(erasedType, bindings);
 assertTrue(actual1.equals(actual2));
 }

 @Test
 public void testEqualsDifferentClasses() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual1 = createTestType(erasedType, bindings);
 ResolvedRecursiveType actual2 = createTestType(Integer.class, bindings);
 assertFalse(actual1.equals(actual2));
 }

 @Test
 public void testEqualsNull() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual = createTestType(erasedType, bindings);
 assertFalse(actual.equals(null));
 }

 @Test
 public void testEqualsSameReference() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual1 = createTestType(erasedType, bindings);
 ResolvedRecursiveType actual2 = actual1;
 assertTrue(actual1.equals(actual2));
 }

 @Test
 public void testEqualsDifferentReferences() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType actual1 = createTestType(erasedType, bindings);
 ResolvedRecursiveType actual2 = new ResolvedRecursiveType(erasedType, bindings);
 assertFalse(actual1.equals(actual2));
 }

 @Test
 public void testEqualsSelfReferencedTypes() {
 Class<String> erasedType1 = String.class;
 TypeBindings bindings1 = new TypeBindings();
 ResolvedRecursiveType actual1 = createTestType(erasedType1, bindings1);
 actual1.setReference(actual1);
 Class<String> erasedType2 = String.class;
 TypeBindings bindings2 = new TypeBindings();
 ResolvedRecursiveType actual2 = createTestType(erasedType2, bindings2);
 actual2.setReference(actual2);
 assertTrue(actual1.equals(actual2));
 }

}