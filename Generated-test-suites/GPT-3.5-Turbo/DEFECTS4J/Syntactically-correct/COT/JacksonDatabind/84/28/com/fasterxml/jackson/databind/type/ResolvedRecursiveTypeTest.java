
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
  
  @Test
  public void testSetReference() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    JavaType ref = new JavaType();
    type.setReference(ref);
    
    assertEquals(ref, type.getSelfReferencedType());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSetReferenceMultipleTimes() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    JavaType ref1 = new JavaType();
    JavaType ref2 = new JavaType();
    
    type.setReference(ref1);
    type.setReference(ref2); // IllegalStateException should be thrown
  }
  
  @Test
  public void testGetGenericSignature() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    StringBuilder sb = new StringBuilder();
    StringBuilder result = type.getGenericSignature(sb);
    
    assertEquals(sb, result);
  }
  
  @Test
  public void testGetErasedSignature() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    StringBuilder sb = new StringBuilder();
    StringBuilder result = type.getErasedSignature(sb);
    
    assertEquals(sb, result);
  }
  
  @Test
  public void testWithContentType() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    JavaType contentType = new JavaType();
    JavaType result = type.withContentType(contentType);
    
    assertEquals(type, result);
  }
  
  @Test
  public void testWithTypeHandler() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    Object h = new Object();
    JavaType result = type.withTypeHandler(h);
    
    assertEquals(type, result);
  }
  
  @Test
  public void testWithContentTypeHandler() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    Object h = new Object();
    JavaType result = type.withContentTypeHandler(h);
    
    assertEquals(type, result);
  }
  
  @Test
  public void testWithValueHandler() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    Object h = new Object();
    JavaType result = type.withValueHandler(h);
    
    assertEquals(type, result);
  }
  
  @Test
  public void testWithContentValueHandler() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    Object h = new Object();
    JavaType result = type.withContentValueHandler(h);
    
    assertEquals(type, result);
  }
  
  @Test
  public void testWithStaticTyping() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    JavaType result = type.withStaticTyping();
    
    assertEquals(type, result);
  }
  
  @Test
  public void testNarrow() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    Class<?> subclass = new Class<?>();
    JavaType result = type._narrow(subclass);
    
    assertEquals(type, result);
  }
  
  @Test
  public void testRefine() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    Class<?> rawType = new Class<?>();
    TypeBindings bindings = new TypeBindings();
    JavaType superClass = new JavaType();
    JavaType[] superInterfaces = new JavaType[0];
    
    JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
    
    assertNull(result);
  }
  
  @Test
  public void testIsContainerType() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    boolean result = type.isContainerType();
    
    assertFalse(result);
  }
  
  @Test
  public void testToStringNullReference() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    String result = type.toString();
    
    assertEquals("[recursive type; UNRESOLVED", result);
  }
  
  @Test
  public void testToStringNonNullReference() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    JavaType ref = new JavaType(Class.forName("com.fasterxml.jackson.databind.JavaType"));
    type.setReference(ref);
    
    String result = type.toString();
    
    assertEquals("[recursive type; com.fasterxml.jackson.databind.JavaType]", result);
  }
  
  @Test
  public void testEqualsSameObject() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    boolean result = type.equals(type);
    
    assertTrue(result);
  }
  
  @Test
  public void testEqualsNullObject() {
    ResolvedRecursiveType type = new ResolvedRecursiveType();
    
    boolean result = type.equals(null);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualsDifferentClass() {
    ResolvedRecursiveType type1 = new ResolvedRecursiveType();
    Long type2 = new Long();
    
    boolean result = type1.equals(type2);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualsDifferentReferencedType() {
    ResolvedRecursiveType type1 = new ResolvedRecursiveType();
    ResolvedRecursiveType type2 = new ResolvedRecursiveType();
    
    JavaType ref1 = new JavaType();
    JavaType ref2 = new JavaType();
    
    type1.setReference(ref1);
    type2.setReference(ref2);
    
    boolean result = type1.equals(type2);
    
    assertFalse(result);
  }
  
  @Test
  public void testEqualsSameReferencedType() {
    ResolvedRecursiveType type1 = new ResolvedRecursiveType();
    ResolvedRecursiveType type2 = new ResolvedRecursiveType();
    
    JavaType ref = new JavaType();
    
    type1.setReference(ref);
    type2.setReference(ref);
    
    boolean result = type1.equals(type2);
    
    assertTrue(result);
  }
}
