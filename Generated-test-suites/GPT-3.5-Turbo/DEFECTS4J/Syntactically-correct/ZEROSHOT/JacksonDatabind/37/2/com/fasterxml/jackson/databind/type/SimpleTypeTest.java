import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertNotNull(simpleType);
    assertEquals(String.class, simpleType.getRawClass());
  }
  
  @Test
  public void testConstruct() {
    try {
      SimpleType.construct(Map.class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
    }
    try {
      SimpleType.construct(Collection.class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
    }
    try {
      SimpleType.construct(int[].class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
    }
    SimpleType simpleType = SimpleType.construct(String.class);
    assertNotNull(simpleType);
    assertEquals(String.class, simpleType.getRawClass());
  }
  
  @Test
  public void test_narrow() {
    SimpleType simpleType = SimpleType.construct(String.class);
    SimpleType narrowedType = (SimpleType) simpleType._narrow(CharSequence.class);
    assertEquals(CharSequence.class, narrowedType.getRawClass());
  }
  
  @Test
  public void test_withTypeHandler() {
    SimpleType simpleType = SimpleType.construct(String.class);
    SimpleType modifiedType = simpleType.withTypeHandler(null);
    assertNull(modifiedType.getTypeHandler());
  }
  
  @Test
  public void test_withValueHandler() {
    SimpleType simpleType = SimpleType.construct(String.class);
    SimpleType modifiedType = simpleType.withValueHandler(null);
    assertNull(modifiedType.getValueHandler());
  }
  
  @Test
  public void test_withStaticTyping() {
    SimpleType simpleType = SimpleType.construct(String.class);
    SimpleType modifiedType = simpleType.withStaticTyping();
    assertTrue(modifiedType.isStatic());
  }
  
  @Test
  public void test_refine() {
    SimpleType simpleType = SimpleType.construct(String.class);
    JavaType refinedType = simpleType.refine(StringBuilder.class, null, null, null);
    assertNull(refinedType);
  }
  
  @Test
  public void test_isContainerType() {
    SimpleType simpleType = SimpleType.construct(String.class);
    assertFalse(simpleType.isContainerType());
  }
  
  @Test
  public void test_getErasedSignature() {
    SimpleType simpleType = SimpleType.construct(String.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder signature = simpleType.getErasedSignature(sb);
    assertNotNull(signature);
    assertEquals("Ljava/lang/String;", signature.toString());
  }
  
  @Test
  public void test_getGenericSignature() {
    SimpleType simpleType = SimpleType.construct(String.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder signature = simpleType.getGenericSignature(sb);
    assertNotNull(signature);
    assertEquals("Ljava/lang/String;", signature.toString());
  }
  
  @Test
  public void test_equals() {
    SimpleType simpleType1 = SimpleType.construct(String.class);
    SimpleType simpleType2 = SimpleType.construct(String.class);
    assertTrue(simpleType1.equals(simpleType2));
    
    SimpleType simpleType3 = SimpleType.construct(StringBuilder.class);
    assertFalse(simpleType1.equals(simpleType3));
    
    SimpleType simpleType4 = SimpleType.construct(String.class);
    simpleType4 = simpleType4.withContentValueHandler(new Object());
    assertFalse(simpleType1.equals(simpleType4));
  }
}