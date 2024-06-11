import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 TypeBindings bindings = new TypeBindings();
 return new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testSetReference() {
 JavaType referencedType = createResolvedRecursiveType(String.class);
 ResolvedRecursiveType selfRefType = createResolvedRecursiveType(ResolvedRecursiveType.class);

 selfRefType.setReference(referencedType);
 JavaType getSelfRefType = selfRefType.getSelfReferencedType();

 assertTrue(getSelfRefType instanceof ResolvedRecursiveType);
 assertEquals(referencedType, getSelfRefType);

 // Check that setting reference again throws an error
 try {
 selfRefType.setReference(referencedType);
 fail("Should have thrown IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 JavaType selfRefType = createResolvedRecursiveType(ResolvedRecursiveType.class);
 assertNull(selfRefType.getSelfReferencedType());

 JavaType referencedType = createResolvedRecursiveType(String.class);
 selfRefType.setReference(referencedType);
 JavaType getSelfRefType = selfRefType.getSelfReferencedType();

 assertSame(referencedType, getSelfRefType);
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType selfRefType = createResolvedRecursiveType(ResolvedRecursiveType.class);
 selfRefType.getGenericSignature(sb);

 String signature = sb.toString();
 assertTrue(signature.contains("ResolvedRecursiveType<"));
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType selfRefType = createResolvedRecursiveType(ResolvedRecursiveType.class);
 selfRefType.getErasedSignature(sb);

 String signature = sb.toString();
 assertFalse(signature.contains("<"));
 }

 @Test
 public void testWithContentType() {
 JavaType selfRefType = createResolvedRecursiveType(ResolvedRecursiveType.class);
 JavaType contentType = createResolvedRecursiveType(Integer.class);
 JavaType result = selfRefType.withContentType(contentType);

 assertEquals(selfRefType.getClass(), result.getClass());
 }

 // Add more test cases for other methods if necessary
}