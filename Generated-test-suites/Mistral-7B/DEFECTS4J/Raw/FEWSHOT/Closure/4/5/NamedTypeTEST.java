package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;

public class NamedTypeTest {

  @Test
  public void testNewNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertEquals("Test", namedType.sourceName);
    assertEquals(1, namedType.lineno);
    assertEquals(1, namedType.charno);
    assertEquals("String", namedType.toString());
  }

  @Test
  public void testNewNamedTypeWithNullReference() {
    JSTypeRegistry registry = new JSTypeRegistry();
    try {
      new NamedType(registry, null, "Test", 1, 1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    List<NamedType.PropertyContinuation> propertyContinuations = Lists.newArrayList();
    assertTrue(namedType.defineProperty("prop", null, false, null));
    assertSame(namedType, namedType.getReferencedType());
    assertNull(namedType.getPropertyContinuations());

    namedType.defineProperty("prop", new JSTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), false, null);
    assertSame(new JSTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getProperty("prop"));
    assertNull(namedType.getPropertyContinuations());

    namedType.defineProperty("prop", null, false, null);
    List<NamedType.PropertyContinuation> propertyContinuations2 = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations2.size());
    NamedType.PropertyContinuation propertyContinuation = propertyContinuations2.get(0);
    assertEquals("prop", propertyContinuation.propertyName);
    assertEquals(null, propertyContinuation.type);
    assertEquals(false, propertyContinuation.inferred);
    assertEquals(null, propertyContinuation.propertyNode);

    namedType.finishPropertyContinuations();
    assertNull(namedType.getPropertyContinuations());
    assertSame(new JSTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getProperty("prop"));
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.putType("String", new StringType());
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertEquals("String", namedType.getReferencedType().getName());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.putType("Object", new ObjectType(registry, "Object", null, 0));
    registry.putProperty("Object.prop", new NumberType(registry, "Number", null, 0));

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    JSType objType = new ObjectType(registry, "Object", null, 0);
    enclosing.define(JSTypeNative.OBJECT_TYPE.getName(), objType);

    NamedType namedType = new NamedType(registry, "Object.prop", "Test", 1, 1);
    assertEquals("Object.prop", namedType.getReferenceName());
    assertEquals(NumberType.class, namedType.getReferencedType().getClass());
  }

  @Test
  public void testResolveViaPropertiesWithNullValue() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSType objType = new ObjectType(registry, "Object", null, 0);
    enclosing.define(JSTypeNative.OBJECT_TYPE.getName(), objType);

    NamedType namedType = new NamedType(registry, "Object.prop", "Test", 1, 1);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithNonObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSType numberType = new NumberType(registry, "Number", null, 0);
    enclosing.define(JSTypeNative.NUMBER_TYPE.getName(), numberType);

    NamedType namedType = new NamedType(registry, "Number", "Test", 1, 1);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.putType("Enum", new EnumType(registry, "Enum", null, 0));
    registry.putProperty("Enum.FOO", new EnumElementType(registry, "Enum", "FOO", null, 0));

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    JSType enumType = new EnumType(registry, "Enum", null, 0);
    enclosing.define(JSTypeNative.ENUM_TYPE.getName(), enumType);

    NamedType namedType = new NamedType(registry, "Enum.FOO", "Test", 1, 1);
    assertEquals("Enum.FOO", namedType.getReferenceName());
    assertEquals(EnumElementType.class, namedType.getReferencedType().getClass());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType functionType = new FunctionType(registry, null, null, null, null, null);
    registry.define(JSTypeNative.FUNCTION_TYPE.getName(), functionType);

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    JSType objectType = new ObjectType(registry, "Object", null, 0);
    enclosing.define(JSTypeNative.OBJECT_TYPE.getName(), objectType);

    NamedType namedType = new NamedType(registry, "Object.method", "Test", 1, 1);
    assertEquals("Object.method", namedType.getReferenceName());
    assertEquals(FunctionType.class, namedType.getReferencedType().getClass());
  }

  @Test
  public void testResolveViaPropertiesWithInterfaceType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType interfaceType = new InterfaceType(registry, null, null);
    registry.define(JSTypeNative.INTERFACE_TYPE.getName(), interfaceType);

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    JSType objectType = new ObjectType(registry, "Object", null, 0);
    enclosing.define(JSTypeNative.OBJECT_TYPE.getName(), objectType);

    NamedType namedType = new NamedType(registry, "Object.interface", "Test", 1, 1);
    assertEquals("Object.interface", namedType.getReferenceName());
    assertEquals(InterfaceType.class, namedType.getReferencedType().getClass());
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};
    JSType objectType = new ObjectType(registry, "Object", null, 0);
    enclosing.define(JSTypeNative.OBJECT_TYPE.getName(), objectType);

    NamedType namedType = new NamedType(registry, "Unknown", "Test", 1, 1);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertFalse(namedType.isResolvedType());
  }

  @Test
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testIsUnnamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType unnamedType = new ObjectType(registry, null, null, 0);
    assertFalse(unnamedType.isNamedType());
  }
}