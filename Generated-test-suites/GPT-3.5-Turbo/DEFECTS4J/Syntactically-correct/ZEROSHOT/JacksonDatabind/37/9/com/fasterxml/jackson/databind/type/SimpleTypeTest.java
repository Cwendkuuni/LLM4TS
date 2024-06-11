import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {
  
  // Test cases for constructor SimpleType(Class<?> cls)
  @Test
  public void testSimpleTypeConstructor1() {
    SimpleType type = new SimpleType(String.class);
    assertEquals(String.class, type.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test cases for constructor SimpleType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts)
  @Test
  public void testSimpleTypeConstructor2() {
    SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, new JavaType[]{});
    assertEquals(String.class, type.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test cases for copy constructor SimpleType(TypeBase base)
  @Test
  public void testSimpleTypeConstructor3() {
    SimpleType base = new SimpleType(String.class);
    SimpleType type = new SimpleType(base);
    assertEquals(String.class, type.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test cases for constructor SimpleType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts, Object valueHandler, Object typeHandler, boolean asStatic)
  @Test
  public void testSimpleTypeConstructor4() {
    SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, new JavaType[]{}, null, null, false);
    assertEquals(String.class, type.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test cases for constructor SimpleType(Class<?> cls, TypeBindings bindings, JavaType superClass, JavaType[] superInts, int extraHash, Object valueHandler, Object typeHandler, boolean asStatic)
  @Test
  public void testSimpleTypeConstructor5() {
    SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, new JavaType[]{}, 0, null, null, false);
    assertEquals(String.class, type.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test case for method constructUnsafe(Class<?> raw)
  @Test
  public void testConstructUnsafe() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertEquals(String.class, type.getRawClass());
    assertEquals(null, type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test case for deprecated method construct(Class<?> cls)
  @Test(expected = IllegalArgumentException.class)
  public void testConstruct() {
    SimpleType.construct(String.class);
  }
  
  // Test case for method _narrow(Class<?> subclass)
  @Test
  public void testNarrow() {
    SimpleType base = new SimpleType(Number.class);
    SimpleType type = base._narrow(Integer.class);
    assertEquals(Integer.class, type.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    assertNull(type.getSuperClass());
    assertArrayEquals(new JavaType[]{}, type.getInterfaces());
    assertNull(type.getValueHandler());
    assertNull(type.getTypeHandler());
    assertFalse(type.isStatic());
  }
  
  // Test case for method withContentType(JavaType contentType)
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType type = new SimpleType(String.class);
    type.withContentType(null);
  }
  
  // Test case for method withTypeHandler(Object h)
  @Test
  public void testWithTypeHandler() {
    SimpleType type = new SimpleType(String.class);
    Object handler = new Object();
    SimpleType newType = type.withTypeHandler(handler);
    assertEquals(String.class, newType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), newType.getBindings());
    assertNull(newType.getSuperClass());
    assertArrayEquals(new JavaType[]{}, newType.getInterfaces());
    assertNull(newType.getValueHandler());
    assertEquals(handler, newType.getTypeHandler());
    assertFalse(newType.isStatic());
  }
  
  // Test case for method withContentTypeHandler(Object h)
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType type = new SimpleType(String.class);
    type.withContentTypeHandler(null);
  }
  
  // Test case for method withValueHandler(Object h)
  @Test
  public void testWithValueHandler() {
    SimpleType type = new SimpleType(String.class);
    Object handler = new Object();
    SimpleType newType = type.withValueHandler(handler);
    assertEquals(String.class, newType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), newType.getBindings());
    assertNull(newType.getSuperClass());
    assertArrayEquals(new JavaType[]{}, newType.getInterfaces());
    assertEquals(handler, newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertFalse(newType.isStatic());
  }
  
  // Test case for method withContentValueHandler(Object h)
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType type = new SimpleType(String.class);
    type.withContentValueHandler(null);
  }
  
  // Test case for method withStaticTyping()
  @Test
  public void testWithStaticTyping() {
    SimpleType type = new SimpleType(String.class);
    SimpleType newType = type.withStaticTyping();
    assertEquals(String.class, newType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), newType.getBindings());
    assertNull(newType.getSuperClass());
    assertArrayEquals(new JavaType[]{}, newType.getInterfaces());
    assertNull(newType.getValueHandler());
    assertNull(newType.getTypeHandler());
    assertTrue(newType.isStatic());
  }
  
  // Test case for method refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
  @Test
  public void testRefine() {
    SimpleType base = new SimpleType(Number.class);
    JavaType refinedType = base.refine(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[]{});
    assertNull(refinedType);
  }
  
  // Test case for method buildCanonicalName()
  @Test
  public void testBuildCanonicalName() {
    SimpleType type = new SimpleType(String.class);
    assertEquals("java.lang.String", type.buildCanonicalName());
  }
  
  // Test case for method isContainerType()
  @Test
  public void testIsContainerType() {
    SimpleType type = new SimpleType(String.class);
    assertFalse(type.isContainerType());
  }
  
  // Test case for method getErasedSignature(StringBuilder sb)
  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    SimpleType type = new SimpleType(String.class);
    StringBuilder result = type.getErasedSignature(sb);
    assertEquals("Ljava/lang/String;", result.toString());
  }
  
  // Test case for method getGenericSignature(StringBuilder sb)
  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    SimpleType type = new SimpleType(String.class);
    StringBuilder result = type.getGenericSignature(sb);
    assertEquals("Ljava/lang/String;", result.toString());
  }
  
  // Test case for method toString()
  @Test
  public void testToString() {
    SimpleType type = new SimpleType(String.class);
    assertEquals("[simple type, class java.lang.String]", type.toString());
  }
  
  // Test case for method equals(Object o)
  @Test
  public void testEquals() {
    SimpleType type1 = new SimpleType(String.class);
    SimpleType type2 = new SimpleType(String.class);
    assertTrue(type1.equals(type2));
    
    SimpleType type3 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, new JavaType[]{});
    assertFalse(type1.equals(type3));
  }
}