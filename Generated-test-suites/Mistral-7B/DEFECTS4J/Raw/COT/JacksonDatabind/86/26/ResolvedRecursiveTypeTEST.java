import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertNotNull(type);
 assertEquals(String.class, type.getErasedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 type.setReference(new JavaType(Double.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 type.setReference(new JavaType(Integer.class));
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertEquals(Integer.class, selfReferencedType.getRawClass());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String expected = "[recursive type; UNRESOLVED]";
 assertEquals(expected, sb.toString());

 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 sb = new StringBuilder();
 type.getGenericSignature(sb);
 expected = "[recursive type; " + ref.getRawClass().getName() + "]";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 String expected = "[Ljava/lang/String;";
 assertEquals(expected, sb.toString());

 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 sb = new StringBuilder();
 type.getErasedSignature(sb);
 expected = ref.getRawClass().getName();
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new JavaType(Integer.class);
 ResolvedRecursiveType result = type.withContentType(contentType);
 assertSame(type, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withTypeHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withContentTypeHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withValueHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withContentValueHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType result = type.withStaticTyping();
 assertSame(type, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 type.narrow(Integer.class);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Class<?> rawType = Object.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 ResolvedRecursiveType result = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 String expected = "[recursive type; UNRESOLVED]";
 assertEquals(expected, type.toString());

 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 expected = "[recursive type; " + ref.getRawClass().getName() + "]";
 assertEquals(expected, type.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 assertFalse(type1.equals(type2));

 JavaType ref1 = new JavaType(Integer.class);
 type1.setReference(ref1);
 JavaType ref2 = new JavaType(Long.class);
 type2.setReference(ref2);
 assertFalse(type1.equals(type2));

 JavaType sameRef = ref1;
 type1.setReference(sameRef);
 type2.setReference(sameRef);
 assertTrue(type1.equals(type2));
 }
}