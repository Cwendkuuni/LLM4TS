package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

  @Test
  public void testConstructor1() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals(String.class, simpleType.getRawClass());
    assertEquals(null, simpleType.getBindings());
    assertEquals(null, simpleType.getSuperClass());
    assertEquals(null, simpleType.getSuperInterfaces());
    assertEquals(null, simpleType.getValueHandler());
    assertEquals(null, simpleType.getTypeHandler());
    assertFalse(simpleType.isStaticTyping());
  }

  @Test
  public void testConstructor2() {
    SimpleType superClass = new SimpleType(Object.class);
    SimpleType[] superInterfaces = new SimpleType[0];
    SimpleType simpleType = new SimpleType(
        String.class, 
        new TypeBindings(), 
        superClass, 
        superInterfaces
    );
    assertEquals(String.class, simpleType.getRawClass());
    assertEquals(new TypeBindings(), simpleType.getBindings());
    assertEquals(superClass, simpleType.getSuperClass());
    assertEquals(superInterfaces, simpleType.getSuperInterfaces());
    assertEquals(null, simpleType.getValueHandler());
    assertEquals(null, simpleType.getTypeHandler());
    assertFalse(simpleType.isStaticTyping());
  }

  @Test
  public void testConstructor3() {
    SimpleType superClass = new SimpleType(Object.class);
    SimpleType[] superInterfaces = new SimpleType[0];
    Object valueHandler = new Object();
    Object typeHandler = new Object();
    SimpleType simpleType = new SimpleType(
        Integer.class, 
        new TypeBindings(), 
        superClass, 
        superInterfaces, 
        valueHandler, 
        typeHandler, 
        true
    );
    assertEquals(Integer.class, simpleType.getRawClass());
    assertEquals(new TypeBindings(), simpleType.getBindings());
    assertEquals(superClass, simpleType.getSuperClass());
    assertEquals(superInterfaces, simpleType.getSuperInterfaces());
    assertEquals(valueHandler, simpleType.getValueHandler());
    assertEquals(typeHandler, simpleType.getTypeHandler());
    assertTrue(simpleType.isStaticTyping());
  }

  @Test
  public void test_narrow() {
    SimpleType simpleType = new SimpleType(Object.class);
    SimpleType narrowedType = simpleType._narrow(String.class);
    assertEquals(String.class, narrowedType.getRawClass());
    assertEquals(simpleType.getBindings(), narrowedType.getBindings());
    assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
    assertEquals(simpleType.getSuperInterfaces(), narrowedType.getSuperInterfaces());
    assertEquals(simpleType.getValueHandler(), narrowedType.getValueHandler());
    assertEquals(simpleType.getTypeHandler(), narrowedType.getTypeHandler());
    assertEquals(simpleType.isStaticTyping(), narrowedType.isStaticTyping());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_withContentType() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType.withContentType(new SimpleType(Object.class));
  }

  @Test
  public void test_withTypeHandler() {
    SimpleType simpleType = new SimpleType(Object.class);
    Object typeHandler = new Object();
    SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
    assertEquals(Object.class, withTypeHandler.getRawClass());
    assertEquals(simpleType.getBindings(), withTypeHandler.getBindings());
    assertEquals(simpleType.getSuperClass(), withTypeHandler.getSuperClass());
    assertEquals(simpleType.getSuperInterfaces(), withTypeHandler.getSuperInterfaces());
    assertEquals(simpleType.getValueHandler(), withTypeHandler.getValueHandler());
    assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    assertEquals(simpleType.isStaticTyping(), withTypeHandler.isStaticTyping());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_withContentTypeHandler() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType.withContentTypeHandler(new Object());
  }

  @Test
  public void test_withValueHandler() {
    SimpleType simpleType = new SimpleType(Object.class);
    Object valueHandler = new Object();
    SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
    assertEquals(Object.class, withValueHandler.getRawClass());
    assertEquals(simpleType.getBindings(), withValueHandler.getBindings());
    assertEquals(simpleType.getSuperClass(), withValueHandler.getSuperClass());
    assertEquals(simpleType.getSuperInterfaces(), withValueHandler.getSuperInterfaces());
    assertEquals(valueHandler, withValueHandler.getValueHandler());
    assertEquals(simpleType.getTypeHandler(), withValueHandler.getTypeHandler());
    assertEquals(simpleType.isStaticTyping(), withValueHandler.isStaticTyping());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_withContentValueHandler() {
    SimpleType simpleType = new SimpleType(Object.class);
    simpleType.withContentValueHandler(new Object());
  }

  @Test
  public void test_withStaticTyping() {
    SimpleType simpleType = new SimpleType(Object.class);
    SimpleType withStaticTyping = simpleType.withStaticTyping();
    assertEquals(Object.class, withStaticTyping.getRawClass());
    assertEquals(simpleType.getBindings(), withStaticTyping.getBindings());
    assertEquals(simpleType.getSuperClass(), withStaticTyping.getSuperClass());
    assertEquals(simpleType.getSuperInterfaces(), withStaticTyping.getSuperInterfaces());
    assertEquals(simpleType.getValueHandler(), withStaticTyping.getValueHandler());
    assertEquals(simpleType.getTypeHandler(), withStaticTyping.getTypeHandler());
    assertTrue(withStaticTyping.isStaticTyping());
  }

  @Test
  public void testRefine() {
    SimpleType simpleType = new SimpleType(Object.class);
    JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), null, new SimpleType[] {});
    assertNull(refinedType);
  }

  @Test
  public void testBuildCanonicalName() {
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder();
    sb.append(Object.class.getName());
    assertEquals(sb.toString(), simpleType.buildCanonicalName());
  }

  @Test
  public void testIsContainerType() {
    SimpleType simpleType = new SimpleType(Object.class);
    assertFalse(simpleType.isContainerType());
  }

  @Test
  public void testGetErasedSignature() {
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder();
    sb.append("L").append(Object.class.getName().replace(".", "/")).append(";");
    assertEquals(sb.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
  }

  @Test
  public void testGetGenericSignature() {
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder();
    sb.append("L").append(Object.class.getName().replace(".", "/")).append(";").append(";");
    assertEquals(sb.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());
  }

  @Test
  public void testToString() {
    SimpleType simpleType = new SimpleType(Object.class);
    StringBuilder sb = new StringBuilder(40);
    sb.append("[simple type, class ").append(simpleType.buildCanonicalName()).append(']');
    assertEquals(sb.toString(), simpleType.toString());
  }

  @Test
  public void testEquals() {
    SimpleType simpleType1 = new SimpleType(Object.class);
    SimpleType simpleType2 = new SimpleType(Object.class);
    assertTrue(simpleType1.equals(simpleType1));
    assertFalse(simpleType1.equals(null));
    assertFalse(simpleType1.equals(new Object()));
    assertTrue(simpleType1.equals(simpleType2));
  }
}