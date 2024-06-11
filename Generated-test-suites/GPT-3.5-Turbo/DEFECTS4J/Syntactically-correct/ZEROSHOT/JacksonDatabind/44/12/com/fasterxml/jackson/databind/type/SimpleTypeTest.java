import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {
  
  // Test constructors
  
  @Test
  public void testConstructor1() {
    SimpleType st = new SimpleType(Integer.class);
    assertEquals(Integer.class, st.getRawClass());
    assertFalse(st.hasGenericTypes());
  }
  
  @Test
  public void testConstructor2() {
    TypeBindings tb = new TypeBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;
    
    SimpleType st = new SimpleType(Integer.class, tb, superClass, superInts, valueHandler, typeHandler, asStatic);
    assertEquals(Integer.class, st.getRawClass());
    assertFalse(st.hasGenericTypes());
  }
  
  @Test
  public void testConstructor3() {
    TypeBase base = new TypeBase();
    
    SimpleType st = new SimpleType(base);
    assertSame(base, st);
  }
  
  @Test
  public void testConstructor4() {
    TypeBindings tb = new TypeBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    int extraHash = 0;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;
    
    SimpleType st = new SimpleType(Integer.class, tb, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
    assertEquals(Integer.class, st.getRawClass());
    assertFalse(st.hasGenericTypes());
  }
  
  @Test
  public void testConstructor5() {
    TypeBindings tb = new TypeBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    int extraHash = 0;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;
    
    SimpleType st = new SimpleType(Integer.class, tb, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
    assertEquals(Integer.class, st.getRawClass());
    assertFalse(st.hasGenericTypes());
  }
  
  // Test constructUnsafe method
  
  @Test
  public void testConstructUnsafe() {
    SimpleType st = SimpleType.constructUnsafe(Integer.class);
    assertEquals(Integer.class, st.getRawClass());
    assertFalse(st.hasGenericTypes());
  }
  
  // Test construct method
  
  @Test
  public void testConstruct() {
    try {
      SimpleType.construct(Map.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
    try {
      SimpleType.construct(Collection.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
    try {
      SimpleType.construct(Integer[].class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
    
    SimpleType st = SimpleType.construct(Integer.class);
    assertEquals(Integer.class, st.getRawClass());
    assertFalse(st.hasGenericTypes());
  }
  
  // Test narrowing method
  
  @Test
  public void testNarrow() {
    SimpleType st = new SimpleType(Integer.class);
    
    JavaType narrowedType = st.narrow(Integer.class);
    assertSame(st, narrowedType);
    
    narrowedType = st.narrow(Double.class);
    assertEquals(Double.class, narrowedType.getRawClass());
    assertFalse(narrowedType.hasGenericTypes());
  }
  
  // Test withContentType method
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType st = new SimpleType(Integer.class);
    st.withContentType(null);
  }
  
  // Test withTypeHandler method
  
  @Test
  public void testWithTypeHandler() {
    SimpleType st = new SimpleType(Integer.class);
    Object handler = new Object();
    
    SimpleType newType = st.withTypeHandler(handler);
    
    assertNotSame(st, newType);
    assertEquals(handler, newType.getTypeHandler());
  }
  
  // Test withContentTypeHandler method
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType st = new SimpleType(Integer.class);
    st.withContentTypeHandler(null);
  }
  
  // Test withValueHandler method
  
  @Test
  public void testWithValueHandler() {
    SimpleType st = new SimpleType(Integer.class);
    Object handler = new Object();
    
    SimpleType newType = st.withValueHandler(handler);
    
    assertNotSame(st, newType);
    assertEquals(handler, newType.getValueHandler());
  }
  
  // Test withContentValueHandler method
  
  @Test(expected = IllegalArgumentException.class)
  public void testWithContentValueHandler() {
    SimpleType st = new SimpleType(Integer.class);
    st.withContentValueHandler(null);
  }
  
  // Test withStaticTyping method
  
  @Test
  public void testWithStaticTyping() {
    SimpleType st = new SimpleType(Integer.class);
    
    SimpleType newType = st.withStaticTyping();
    
    assertNotSame(st, newType);
    assertTrue(newType.isStatic());
  }
  
  // Test refine method
  
  @Test
  public void testRefine() {
    SimpleType st = new SimpleType(Integer.class);
    
    JavaType newType = st.refine(Integer.class, null, null, null);
    
    assertNull(newType);
  }
  
  // Test isContainerType method
  
  @Test
  public void testIsContainerType() {
    SimpleType st = new SimpleType(Integer.class);
    
    assertFalse(st.isContainerType());
  }
  
  // Test getErasedSignature method
  
  @Test
  public void testGetErasedSignature() {
    SimpleType st = new SimpleType(Integer.class);
    
    StringBuilder sb = new StringBuilder();
    StringBuilder result = st.getErasedSignature(sb);
    
    assertSame(sb, result);
    
    assertEquals("Ljava/lang/Integer;", result.toString());
  }
  
  // Test getGenericSignature method
  
  @Test
  public void testGetGenericSignature() {
    SimpleType st = new SimpleType(Integer.class);
    
    StringBuilder sb = new StringBuilder();
    StringBuilder result = st.getGenericSignature(sb);
    
    assertSame(sb, result);
    
    assertEquals("Ljava/lang/Integer;", result.toString());
  }
  
  // Test toString method
  
  @Test
  public void testToString() {
    SimpleType st = new SimpleType(Integer.class);
    
    String result = st.toString();
    
    assertEquals("[simple type, class java.lang.Integer]", result);
  }
  
  // Test equals method
  
  @Test
  public void testEquals() {
    SimpleType st1 = new SimpleType(Integer.class);
    SimpleType st2 = new SimpleType(Integer.class);
    SimpleType st3 = new SimpleType(Double.class);
    
    assertTrue(st1.equals(st1));
    assertTrue(st1.equals(st2));
    assertFalse(st1.equals(null));
    assertFalse(st1.equals(new Object()));
    assertFalse(st1.equals(st3));
  }
  
}