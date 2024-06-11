
import org.junit.*;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

  @Test
  public void testSetReference() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a reference to a JavaType object
      JavaType reference = new JavaType();
      
      // Set the reference in the ResolvedRecursiveType object
      resolvedRecursiveType.setReference(reference);
      
      // Verify that the reference is set correctly
      Assert.assertEquals(reference, resolvedRecursiveType.getSelfReferencedType());
  }
  
  @Test
  public void testGetGenericSignature() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a StringBuilder object
      StringBuilder sb = new StringBuilder();
      
      // Get the generic signature and append it to the StringBuilder
      resolvedRecursiveType.getGenericSignature(sb);
      
      // Verify that the StringBuilder contains the expected value
      Assert.assertEquals("", sb.toString());
  }
  
  @Test
  public void testGetErasedSignature() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a StringBuilder object
      StringBuilder sb = new StringBuilder();
      
      // Get the erased signature and append it to the StringBuilder
      resolvedRecursiveType.getErasedSignature(sb);
      
      // Verify that the StringBuilder contains the expected value
      Assert.assertEquals("", sb.toString());
  }
  
  @Test
  public void testWithContentType() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a JavaType object
      JavaType contentType = new JavaType();
      
      // Call the withContentType method
      JavaType result = resolvedRecursiveType.withContentType(contentType);
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void testWithTypeHandler() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a type handler object
      Object handler = new Object();
      
      // Call the withTypeHandler method
      JavaType result = resolvedRecursiveType.withTypeHandler(handler);
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void testWithContentTypeHandler() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a type handler object
      Object handler = new Object();
      
      // Call the withContentTypeHandler method
      JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void testWithValueHandler() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a value handler object
      Object handler = new Object();
      
      // Call the withValueHandler method
      JavaType result = resolvedRecursiveType.withValueHandler(handler);
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void testWithContentValueHandler() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a value handler object
      Object handler = new Object();
      
      // Call the withContentValueHandler method
      JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void testWithStaticTyping() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Call the withStaticTyping method
      JavaType result = resolvedRecursiveType.withStaticTyping();
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void test_narrow() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a subclass
      Class<?> subclass = String.class;
      
      // Call the _narrow method
      JavaType result = resolvedRecursiveType._narrow(subclass);
      
      // Verify that the result is the same as the original ResolvedRecursiveType object
      Assert.assertSame(resolvedRecursiveType, result);
  }
  
  @Test
  public void testRefine() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Create a rawType
      Class<?> rawType = String.class;
      
      // Create TypeBindings
      TypeBindings bindings = null;
      
      // Create superClass and superInterfaces
      JavaType superClass = null;
      JavaType[] superInterfaces = null;
      
      // Call the refine method
      JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
      
      // Verify that the result is null
      Assert.assertNull(result);
  }
  
  @Test
  public void testIsContainerType() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Call the isContainerType method
      boolean result = resolvedRecursiveType.isContainerType();
      
      // Verify that the result is false
      Assert.assertFalse(result);
  }
  
  @Test
  public void testToString() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
      
      // Call the toString method
      String result = resolvedRecursiveType.toString();
      
      // Verify that the result is as expected
      Assert.assertEquals("[recursive type; UNRESOLVED]", result);
  }
  
  @Test
  public void testEquals() {
      // Create an instance of ResolvedRecursiveType
      ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
      
      // Create another instance of ResolvedRecursiveType with the same referenced type
      ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
      
      // Call the equals method on the first instance passing the second instance
      boolean result = resolvedRecursiveType1.equals(resolvedRecursiveType2);
      
      // Verify that the result is true
      Assert.assertTrue(result);
  }

}
