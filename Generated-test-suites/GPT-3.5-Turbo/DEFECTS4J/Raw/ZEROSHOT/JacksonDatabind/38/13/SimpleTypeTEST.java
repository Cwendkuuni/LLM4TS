import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  // Test constructor
  @Test
  public void testConstructor() {
    SimpleType simpleType = new SimpleType(String.class);
    assertNotNull(simpleType);
  }

  // Test constructor with bindings, superclass and super interfaces
  @Test
  public void testConstructorWithBindings() {
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInterfaces = null;

    SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces);
    assertNotNull(simpleType);
  }

  // Test copy constructor
  @Test
  public void testCopyConstructor() {
    SimpleType base = new SimpleType(String.class);
    SimpleType simpleType = new SimpleType(base);
    assertNotNull(simpleType);
  }

  // Test full constructor with all parameters
  @Test
  public void testFullConstructor() {
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;

    SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
    assertNotNull(simpleType);
  }

  // Test full constructor with all parameters and extraHash
  @Test
  public void testFullConstructorWithExtraHash() {
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;
    int extraHash = 0;

    SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
    assertNotNull(simpleType);
  }

  // Test unsafe construction
  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertNotNull(simpleType);
  }

  // Test deprecated construct method
  @Test
  @Deprecated
  public void testConstruct() {
    try {
      SimpleType simpleType = SimpleType.construct(String.class);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Collection (class: " + String.class.getName() + ")", e.getMessage());
    }
  }

  // Test narrowing
  @Test
  @Deprecated
  public void testNarrow() {
    SimpleType simpleType = new SimpleType(String.class);
    JavaType narrow = simpleType._narrow(String.class);
    assertEquals(simpleType, narrow);
  }

  // Test withContentType
  @Test
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentType(simpleType);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
    }
  }

  // Test withTypeHandler
  @Test
  public void testWithTypeHandler() {
    Object typeHandler = new Object();
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
    assertEquals(newSimpleType._typeHandler, typeHandler);
    assertNotEquals(simpleType, newSimpleType);
  }

  // Test withContentTypeHandler
  @Test
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentTypeHandler(new Object());
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
    }
  }

  // Test withValueHandler
  @Test
  public void testWithValueHandler() {
    Object valueHandler = new Object();
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
    assertEquals(newSimpleType._valueHandler, valueHandler);
    assertNotEquals(simpleType, newSimpleType);
  }

  // Test withContentValueHandler
  @Test
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    try {
      simpleType.withContentValueHandler(new Object());
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
    }
  }

  // Test withStaticTyping
  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType newSimpleType = simpleType.withStaticTyping();
    assertTrue(newSimpleType._asStatic);
    assertNotEquals(simpleType, newSimpleType);
  }

  // Test refine
  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(String.class);
    JavaType rawType = new JavaType(String.class);
    JavaType refined = simpleType.refine(String.class, TypeBindings.emptyBindings(), rawType, null);
    assertNull(refined);
  }

  // Test buildCanonicalName
  @Test
  public void testBuildCanonicalName() {
    SimpleType simpleType = new SimpleType(String.class);
    String canonicalName = simpleType.buildCanonicalName();
    assertEquals(String.class.getName(), canonicalName);
  }

  // Test isContainerType
  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(String.class);
    assertFalse(simpleType.isContainerType());
  }

  // Test getErasedSignature
  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder result = simpleType.getErasedSignature(sb);
    assertEquals(sb, result);
  }

  // Test getGenericSignature
  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(String.class);
    StringBuilder sb = new StringBuilder();
    StringBuilder result = simpleType.getGenericSignature(sb);
    assertEquals(sb, result);
  }

  // Standard method - toString
  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(String.class);
    String result = simpleType.toString();
    assertEquals("[simple type, class " + String.class.getName() + "]", result);
  }

  // Standard method - equals
  @Test
  public void testEquals() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType sameSimpleType = new SimpleType(String.class);
    SimpleType differentSimpleType = new SimpleType(Integer.class);

    assertTrue(simpleType.equals(sameSimpleType));
    assertFalse(simpleType.equals(differentSimpleType));
  }

}