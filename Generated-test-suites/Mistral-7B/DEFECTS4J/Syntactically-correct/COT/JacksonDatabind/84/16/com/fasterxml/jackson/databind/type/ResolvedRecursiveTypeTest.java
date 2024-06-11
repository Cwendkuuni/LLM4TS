import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertNotNull(type);
 assertEquals(String.class, type.getRawClass());
 assertNull(type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceIllegalStateException() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertNotNull(selfReferencedType);
 assertEquals(ref.getClass(), selfReferencedType.getClass());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String expected = "[recursive type; ";
 String result = sb.toString();
 assertTrue(result.startsWith(expected));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 String result = sb.toString();
 assertEquals("", result);
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
 Class<?> subclass = Object.class;
 JavaType result = type._narrow(subclass);
 fail("UnsupportedOperationException not thrown");
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Class<?> rawType = Object.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
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
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 String expected = "[recursive type; [Ljava.lang.Class;@368f0872]";
 String result = type.toString();
 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 assertFalse(type1.equals(type2));

 JavaType ref1 = new JavaType(Integer.class);
 JavaType ref2 = new JavaType(Integer.class);

 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);
 type3.setReference(ref1);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, null);
 type4.setReference(ref2);

 assertFalse(type1.equals(type3));
 assertFalse(type1.equals(type4));

 assertEquals(type3, type4);
 }
}