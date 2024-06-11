import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type1.setReference(ref);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 type2.setReference(type1);

 assertEquals("Unexpected self-referenced type", ref, type1.getSelfReferencedType());
 assertEquals("Unexpected self-referenced type", type1, type2.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type.setReference(ref);
 JavaType ref2 = new SimpleType(Integer.class);
 type.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type.setReference(ref);

 assertEquals("Self-referenced type should match set reference", ref, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, null);
 StringBuilder expected = new StringBuilder("[? extends java.util.List<?>; ");
 // [databind#1301]: Typically resolves to a loop so short-cut
 //   and only include type-erased class
 StringBuilder actual = new StringBuilder(50);
 type.getGenericSignature(actual);
 assertEquals("Unexpected generic signature", expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, null);
 StringBuilder expected = new StringBuilder("[java.util.List; ");
 StringBuilder actual = new StringBuilder(20);
 type.getErasedSignature(actual);
 assertEquals("Unexpected erased signature", expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = new SimpleType(String.class);
 JavaType result = type.withContentType(contentType);
 assertEquals("Unexpected result of withContentType", type, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withTypeHandler(handler);
 assertEquals("Unexpected result of withTypeHandler", type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withContentTypeHandler(handler);
 assertEquals("Unexpected result of withContentTypeHandler", type, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withValueHandler(handler);
 assertEquals("Unexpected result of withValueHandler", type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withContentValueHandler(handler);
 assertEquals("Unexpected result of withContentValueHandler", type, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withStaticTyping();
 assertEquals("Unexpected result of withStaticTyping", type, result);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> subclass = String.class;
 JavaType result = type.narrow(subclass);
 assertEquals("Unexpected result of narrow", type, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> rawType = List.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse("Unexpected result of isContainerType", type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type1.setReference(ref);
 type2.setReference(type1);
 assertEquals("Unexpected toString", "[recursive type; " + ref.getRawClass().getName() + "]", type1.toString());
 assertEquals("Unexpected toString", "[recursive type; " + type1.getClass().getName() + "]", type2.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type1.setReference(ref);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 type3.setReference(ref);

 assertTrue("Unexpected result of equals", type1.equals(type3));
 assertFalse("Unexpected result of equals", type1.equals(type2));
 }
}