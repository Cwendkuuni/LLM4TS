import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
  @Test
  public void testConstructor1() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructor2() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructor3() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, false);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructor4() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, false);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructor5() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, 0, null, null, false);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructor6() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, 0, null, null, false);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructor7() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, 0, null, null, false);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void testConstructUnsafe() {
    SimpleType simpleType = SimpleType.constructUnsafe(Class.class);

    assertEquals(Class.class, simpleType.getRawClass());
    assertNull(simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Deprecated
  @Test
  public void testConstruct() {
    try {
      SimpleType.construct(Map.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
    }

    try {
      SimpleType.construct(Collection.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
    }

    try {
      SimpleType.construct(Class[].class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Class;)", e.getMessage());
    }

    SimpleType simpleType = SimpleType.construct(Class.class);

    assertEquals(Class.class, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertEquals(
        SimpleType.construct(Object.class),
        simpleType.getSuperClass()
    );
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
    assertFalse(simpleType.isStatic());
  }

  @Test
  public void test_narrow() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertSame(simpleType, simpleType._narrow(Class.class));
    assertNotSame(simpleType, simpleType._narrow(Object.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(Class.class);
    simpleType.withContentType(new SimpleType(Class.class));
  }

  @Test
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, false);

    assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(null));
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertSame(simpleType, simpleType.withStaticTyping());
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertNull(simpleType.refine(Class.class, TypeBindings.emptyBindings(),
        null, null));
  }

  @Test
  public void testBuildCanonicalName() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertEquals("java.lang.Class", simpleType.buildCanonicalName());
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertEquals("Ljava/lang/Class;", simpleType.getErasedSignature(new StringBuilder()).toString());
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertEquals(
        "Ljava/lang/Class;",
        simpleType.getGenericSignature(new StringBuilder()).toString()
    );
  }

  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(Class.class);

    assertEquals("[simple type, class java.lang.Class]", simpleType.toString());
  }

  @Test
  public void testEquals() {
    SimpleType simpleType = new SimpleType(Class.class);
    assertTrue(simpleType.equals(simpleType));
    assertFalse(simpleType.equals(null));
    assertFalse(simpleType.equals(new Object()));
    assertFalse(simpleType.equals(new SimpleType(Class[].class)));
    assertTrue(simpleType.equals(new SimpleType(Class.class)));

    SimpleType simpleType2 = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, false);
    assertTrue(simpleType.equals(simpleType2));

    SimpleType simpleType3 = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        new SimpleType(Class.class), null, null, null, false);
    assertFalse(simpleType.equals(simpleType3));

    SimpleType simpleType4 = new SimpleType(Class.class, TypeBindings.emptyBindings(),
        null, null, null, null, true);
    assertFalse(simpleType.equals(simpleType4));
  }
}