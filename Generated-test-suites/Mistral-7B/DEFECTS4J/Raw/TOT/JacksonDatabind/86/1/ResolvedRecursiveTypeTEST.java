import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 type2.setReference(type1);

 assertEquals(type1, type2.getSelfReferencedType());
 assertEquals(type2, type1.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 type1.setReference(refType); // second call should throw exception
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertNull(type.getSelfReferencedType());

 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 type.setReference(refType);
 assertEquals(refType, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb1 = new StringBuilder(50);
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 type1.getGenericSignature(sb1);
 String signature1 = sb1.toString();

 StringBuilder sb2 = new StringBuilder(50);
 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 type2.setReference(refType);
 type2.getGenericSignature(sb2);
 String signature2 = sb2.toString();

 assertEquals(signature1, signature2);
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb1 = new StringBuilder(50);
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 type1.getErasedSignature(sb1);
 String signature1 = sb1.toString();

 StringBuilder sb2 = new StringBuilder(50);
 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 type2.setReference(refType);
 type2.getErasedSignature(sb2);
 String signature2 = sb2.toString();

 assertEquals(signature1, signature2);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new TypeReference<List<String>>() {}.getType();
 JavaType result = type.withContentType(contentType);
 assertEquals(type.getClass(), result.getClass());
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType result = type.withTypeHandler(handler);
 assertEquals(type.getClass(), result.getClass());
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType result = type.withContentTypeHandler(handler);
 assertEquals(type.getClass(), result.getClass());
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType result = type.withValueHandler(handler);
 assertEquals(type.getClass(), result.getClass());
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 JavaType result = type.withContentValueHandler(handler);
 assertEquals(type.getClass(), result.getClass());
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 JavaType result = type.withStaticTyping();
 assertEquals(type.getClass(), result.getClass());
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertNull(type._narrow(String[].class));
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertNull(type.refine(String[].class, null, null, null));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 assertEquals("[[recursive type; UNRESOLVED]]", type1.toString());

 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 type2.setReference(refType);
 assertEquals("[[recursive type; "+ refType.getClass().getName() +"]]", type2.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
 assertFalse(type1.equals(type2));

 JavaType refType = new TypeReference<ResolvedRecursiveType>() {}.getType();
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);
 type3.setReference(refType);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, null);
 type4.setReference(refType);
 assertTrue(type3.equals(type4));
 }
}