import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
  
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
    assertNotNull(simpleType);
    assertEquals(Integer.class, simpleType.getRawClass());
  }
  
  @Test
  public void testConstructWithMapType() {
    try {
      SimpleType.construct(Map.class);
      fail("Expected IllegalArgumentException for constructing SimpleType with Map type");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testConstructWithCollectionType() {
    try {
      SimpleType.construct(Collection.class);
      fail("Expected IllegalArgumentException for constructing SimpleType with Collection type");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testConstructWithArrayType() {
    try {
      SimpleType.construct(String[].class);
      fail("Expected IllegalArgumentException for constructing SimpleType with Array type");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testNarrow() {
    SimpleType simpleType = SimpleType.constructUnsafe(Number.class);
    SimpleType narrowedType = (SimpleType) simpleType._narrow(Long.class);
    assertNotNull(narrowedType);
    assertEquals(Long.class, narrowedType.getRawClass());
  }
  
  @Test
  public void testContentType() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    try {
      simpleType.withContentType(Boolean.class);
      fail("Expected IllegalArgumentException for calling withContentType on SimpleType");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testTypeHandler() {
    SimpleType simpleType = SimpleType.constructUnsafe(Double.class);
    SimpleType withTypeHandler = simpleType.withTypeHandler("handler");
    assertNotNull(withTypeHandler);
    assertEquals("handler", withTypeHandler.getTypeHandler());
  }

  @Test
  public void testContentValueHandler() {
    SimpleType simpleType = SimpleType.constructUnsafe(Float.class);
    try {
      simpleType.withContentValueHandler("contentHandler");
      fail("Expected IllegalArgumentException for calling withContentValueHandler on SimpleType");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testStaticTyping() {
    SimpleType simpleType = SimpleType.constructUnsafe(Character.class);
    SimpleType withStaticTyping = simpleType.withStaticTyping();
    assertTrue(withStaticTyping.isStaticTyping());
  }
  
  @Test
  public void testRefine() {
    SimpleType simpleType = SimpleType.constructUnsafe(Boolean.class);
    JavaType refinedType = simpleType.refine(Boolean.class, TypeBindings.emptyBindings(), null, null);
    assertNull(refinedType);
  }
  
  @Test
  public void testIsContainerType() {
    SimpleType simpleType = SimpleType.constructUnsafe(Byte.class);
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    SimpleType simpleType = SimpleType.constructUnsafe(Short.class);
    StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
    assertNotNull(erasedSignature);
    assertEquals(sb, erasedSignature);
  }
  
  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    SimpleType simpleType = SimpleType.constructUnsafe(Void.class);
    StringBuilder genericSignature = simpleType.getGenericSignature(sb);
    assertNotNull(genericSignature);
    assertEquals(sb, genericSignature);
  }
  
}