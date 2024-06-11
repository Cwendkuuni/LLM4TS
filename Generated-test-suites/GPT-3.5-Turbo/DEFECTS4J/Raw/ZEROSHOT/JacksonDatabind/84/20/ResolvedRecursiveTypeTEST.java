import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

   @Test
   public void testSetReference() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      JavaType javaType = new JavaType();
      resolvedRecursiveType.setReference(javaType);
      assertEquals(resolvedRecursiveType.getSelfReferencedType(), javaType);
   }

   @Test
   public void testGetGenericSignature() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      StringBuilder sb = new StringBuilder();
      StringBuilder expectedSb = resolvedRecursiveType.getGenericSignature(sb);
      assertEquals(expectedSb, sb);
   }

   @Test
   public void testGetErasedSignature() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      StringBuilder sb = new StringBuilder();
      StringBuilder expectedSb = resolvedRecursiveType.getErasedSignature(sb);
      assertEquals(expectedSb, sb);
   }

   @Test
   public void testWithContentType() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      JavaType javaType = new JavaType();
      JavaType expectedJavaType = resolvedRecursiveType.withContentType(javaType);
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testWithTypeHandler() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      Object handler = new Object();
      JavaType expectedJavaType = resolvedRecursiveType.withTypeHandler(handler);
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testWithContentTypeHandler() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      Object handler = new Object();
      JavaType expectedJavaType = resolvedRecursiveType.withContentTypeHandler(handler);
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testWithValueHandler() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      Object handler = new Object();
      JavaType expectedJavaType = resolvedRecursiveType.withValueHandler(handler);
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testWithContentValueHandler() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      Object handler = new Object();
      JavaType expectedJavaType = resolvedRecursiveType.withContentValueHandler(handler);
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testWithStaticTyping() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      JavaType expectedJavaType = resolvedRecursiveType.withStaticTyping();
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testNarrow() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      Class<?> subclass = new Class<?>();
      JavaType expectedJavaType = resolvedRecursiveType._narrow(subclass);
      assertEquals(expectedJavaType, resolvedRecursiveType);
   }

   @Test
   public void testRefine() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      Class<?> rawType = new Class<?>();
      TypeBindings bindings = new TypeBindings();
      JavaType superClass = new JavaType();
      JavaType[] superInterfaces = new JavaType[0];
      JavaType expectedJavaType = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
      assertEquals(expectedJavaType, null);
   }

   @Test
   public void testIsContainerType() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      boolean isContainerType = resolvedRecursiveType.isContainerType();
      assertFalse(isContainerType);
   }

   @Test
   public void testToString() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      String expectedString = resolvedRecursiveType.toString();
      assertTrue(expectedString.contains("recursive type"));
   }

   @Test
   public void testEquals() {
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
      ResolvedRecursiveType otherResolvedRecursiveType = new ResolvedRecursiveType(null, null);
      boolean isEqual = resolvedRecursiveType.equals(otherResolvedRecursiveType);
      assertFalse(isEqual);
   }
}