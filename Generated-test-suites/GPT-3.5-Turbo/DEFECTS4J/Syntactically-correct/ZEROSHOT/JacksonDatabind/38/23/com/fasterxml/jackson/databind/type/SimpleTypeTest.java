package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  @Test
  public void testConstructorWithClass() {
    Class<?> cls = Object.class;
    SimpleType simpleType = new SimpleType(cls);
    assertEquals(cls, simpleType.getRawClass());
    assertEquals(TypeBase.DEFAULT_OBJECT_TYPE, simpleType);
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
  }

  @Test
  public void testConstructorWithParams() {
    Class<?> cls = Object.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = TypeBase.DEFAULT_OBJECT_TYPE;
    JavaType[] superInts = null;
    Object valueHandler = null;
    Object typeHandler = null;
    boolean asStatic = false;

    SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
    assertEquals(cls, simpleType.getRawClass());
    assertEquals(bindings, simpleType.getBindings());
    assertEquals(superClass, simpleType.getSuperClass());
    assertArrayEquals(superInts, simpleType.getInterfaces());
    assertEquals(valueHandler, simpleType.getValueHandler());
    assertEquals(typeHandler, simpleType.getTypeHandler());
  }

  @Test
  public void testCopyConstructor() {
    SimpleType base = new SimpleType(Object.class);
    SimpleType simpleType = new SimpleType(base);
    assertEquals(base.getRawClass(), simpleType.getRawClass());
    assertEquals(base.getBindings(), simpleType.getBindings());
    assertEquals(base.getSuperClass(), simpleType.getSuperClass());
    assertArrayEquals(base.getInterfaces(), simpleType.getInterfaces());
    assertEquals(base.getValueHandler(), simpleType.getValueHandler());
    assertEquals(base.getTypeHandler(), simpleType.getTypeHandler());
  }

  @Test
  public void testConstructUnsafe() {
    Class<?> raw = Object.class;
    SimpleType simpleType = SimpleType.constructUnsafe(raw);
    assertEquals(raw, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertNull(simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
  }

  @Test
  public void testConstruct() {
    Class<?> cls = Object.class;

    try {
      SimpleType.construct(Map.class);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
    }

    try {
      SimpleType.construct(Collection.class);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
    }

    try {
      SimpleType.construct(Object[].class);
      fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
    }

    SimpleType simpleType = SimpleType.construct(cls);
    assertEquals(cls, simpleType.getRawClass());
    assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    assertEquals(TypeBase._bogusSuperClass(cls), simpleType.getSuperClass());
    assertNull(simpleType.getInterfaces());
    assertNull(simpleType.getValueHandler());
    assertNull(simpleType.getTypeHandler());
  }

  @Test
  public void testNarrow() {
    Class<?> subclass = Object.class;
    SimpleType simpleType = new SimpleType(Object.class);
    assertEquals(simpleType, simpleType._narrow(simpleType.getRawClass()));
    assertEquals(new SimpleType(subclass, simpleType.getBindings(), simpleType.getSuperClass(), simpleType.getInterfaces(), simpleType.getValueHandler(), simpleType.getTypeHandler(), simpleType.isStatic()), simpleType._narrow(subclass));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testWithContentType() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType.withContentType(TypeBase.DEFAULT_OBJECT_TYPE);
  }

  @Test
  public void testWithTypeHandler() {
    Object h = null;
    SimpleType simpleType = new SimpleType(Object.class);
    assertEquals(simpleType, simpleType.withTypeHandler(simpleType.getTypeHandler()));
    assertEquals(new SimpleType(simpleType.getRawClass(), simpleType.getBindings(), simpleType.getSuperClass(), simpleType.getInterfaces(), simpleType.getValueHandler(), h, simpleType.isStatic()), simpleType.withTypeHandler(h));
  }

  @Test(expected=IllegalArgumentException.class)
  public void testWithContentTypeHandler() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType.withContentTypeHandler(simpleType.getTypeHandler());
  }

  @Test
  public void testWithContentValueHandler() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType.withContentValueHandler(simpleType.getValueHandler());
  }

  @Test
  public void testWithStaticTyping() {
    SimpleType simpleType = new SimpleType(Object.class);
    assertEquals(simpleType, simpleType.withStaticTyping());
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(Object.class);
    assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), TypeBase.DEFAULT_OBJECT_TYPE, null));
  }

  @Test
  public void testBuildCanonicalName() {
    String clsName = "java.lang.Object";
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder();
    sb.append(clsName);
    assertEquals(sb.toString(), simpleType.buildCanonicalName());

    JavaType[] types = new JavaType[] {TypeBase.DEFAULT_OBJECT_TYPE};
    simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, types);
    sb = new StringBuilder();
    sb.append(clsName + "<" + clsName + ">");
    assertEquals(sb.toString(), simpleType.buildCanonicalName());
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(Object.class);
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    String expected = "Ljava/lang/Object";
    SimpleType simpleType = new SimpleType(Object.class);
    assertEquals(expected, simpleType.getErasedSignature(sb).toString());
  }

  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    String clsName = "java.lang.Object";
    String expected = "L" + clsName;

    SimpleType simpleType = new SimpleType(Object.class);
    assertEquals(expected + ";", simpleType.getGenericSignature(sb).toString());

    JavaType[] types = new JavaType[] {TypeBase.DEFAULT_OBJECT_TYPE};
    simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, types);
    sb = new StringBuilder();
    sb.append(expected + "<" + expected + ">");
    assertEquals(sb.toString() + ";", simpleType.getGenericSignature(sb).toString());
  }

  @Test
  public void testToString() {
    String clsName = "java.lang.Object";
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder(40);
    sb.append("[simple type, class " + clsName + "]");
    assertEquals(sb.toString(), simpleType.toString());
  }

  @Test
  public void testEquals() {
    Class<?> cls = Object.class;
    SimpleType type1 = new SimpleType(cls);
    SimpleType type2 = new SimpleType(cls);
    TypeBindings bindings = TypeBindings.emptyBindings();

    assertTrue(type1.equals(type1));
    assertTrue(type1.equals(type2));
    assertFalse(type1.equals(null));
    assertFalse(type1.equals(bindings));
  }

}