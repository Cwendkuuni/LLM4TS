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
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 assertNotNull(type);
 assertEquals(Integer.TYPE, type.getRawClass());
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType ref = new JavaType(String.class);
 type.setReference(ref);
 JavaType expected = ref;
 JavaType actual = type.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType ref1 = new JavaType(String.class);
 type.setReference(ref1);
 JavaType ref2 = new JavaType(Integer.TYPE);
 type.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType ref = new JavaType(String.class);
 type.setReference(ref);
 JavaType expected = ref;
 JavaType actual = type.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String expected = "<T>";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 String expected = "[I";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType contentType = new JavaType(String.class);
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withContentType(contentType);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 Object h = new Object();
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withTypeHandler(h);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 Object h = new Object();
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withContentTypeHandler(h);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 Object h = new Object();
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withValueHandler(h);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 Object h = new Object();
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withContentValueHandler(h);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withStaticTyping();
 assertEquals(expected, actual);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 Class<?> subclass = String.class;
 ResolvedRecursiveType expected = type;
 ResolvedRecursiveType actual = type.withStaticTyping()._narrow(subclass);
 assertEquals(expected, actual);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 Class<?> rawType = Integer.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = null;
 ResolvedRecursiveType expected = null;
 ResolvedRecursiveType actual = type.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 boolean expected = false;
 boolean actual = type.isContainerType();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Integer.TYPE, null);
 String expected = "[recursive type; UNRESOLVED]";
 String actual = type.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.TYPE, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.TYPE, null);
 boolean expected = true;
 boolean actual = type1.equals(type2);
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsDifferentClasses() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.TYPE, null);
 Object obj = new Object();
 boolean expected = false;
 boolean actual = type1.equals(obj);
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsDifferentReferencedTypes() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType ref1 = new JavaType(String.class);
 type1.setReference(ref1);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType ref2 = new JavaType(Long.TYPE);
 type2.setReference(ref2);
 boolean expected = false;
 boolean actual = type1.equals(type2);
 assertEquals(expected, actual);
 }
}