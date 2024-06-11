package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Symbol;

public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof JSTypeNative);
    assertEquals(JSTypeNative.STRING_TYPE, resolvedType);
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    objectType.defineProperty("myProperty", registry.getNativeStringType(), true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<>(objectType));
    String reference = "MyObjectType.myProperty";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof JSTypeNative);
    assertEquals(registry.getNativeStringType(), resolvedType);
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    FunctionType functionType = new FunctionType(
        registry, null, null, null, null, null, null, null, null, null);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    objectType.defineProperty("myFunction", functionType, true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<>(objectType));
    String reference = "MyObjectType.myFunction";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof FunctionType);
    FunctionType functionTypeInstance = (FunctionType) resolvedType;
    assertEquals(functionType, functionTypeInstance.getPrototype());
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    String reference = "MyEnum.MY_ENUM_VALUE";
    EnumType enumType = new EnumType(
        registry, "MyEnum", null, null, null, null, null, null, null, null);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    PropertyType propertyType = new PropertyType(
        registry, null, null, null, null, null, null, null, null, null);
    enumType.defineElement("MY_ENUM_VALUE", propertyType, null, null, null);
    objectType.defineProperty("myEnumProperty", enumType.getInstanceType(), true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<>(objectType));
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof EnumElementType);
    EnumElementType enumElementTypeInstance = (EnumElementType) resolvedType;
    assertEquals(enumType.getElement("MY_ENUM_VALUE"), enumElementTypeInstance.getPrimitiveType());
  }

  @Test
  public void testResolveViaRegistryAndProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    objectType.defineProperty("myProperty", registry.getNativeStringType(), true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<JSType>(objectType));
    String reference = "MyObjectType.myProperty.myNestedProperty";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof JSTypeNative);
    assertEquals(registry.getNativeStringType(), resolvedType);
  }

  @Test
  public void testResolveViaRegistryAndPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    FunctionType functionType = new FunctionType(
        registry, null, null, null, null, null, null, null, null, null);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    objectType.defineProperty("myFunction", functionType, true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<JSType>(objectType));
    String reference = "MyObjectType.myFunction.myNestedFunction";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertTrue(resolvedType instanceof FunctionType);
    FunctionType functionTypeInstance = (FunctionType) resolvedType;
    assertEquals(functionType, functionTypeInstance.getPrototype());
  }

  @Test
  public void testResolveViaRegistryAndPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    String reference = "MyEnum.MY_ENUM_VALUE.myNestedProperty";
    EnumType enumType = new EnumType(
        registry, "MyEnum", null, null, null, null, null, null, null, null);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    PropertyType propertyType = new PropertyType(
        registry, null, null, null, null, null, null, null, null, null);
    enumType.defineElement("MY_ENUM_VALUE", propertyType, null, null, null);
    objectType.defineProperty("myEnumProperty", enumType.getInstanceType(), true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<JSType>(objectType));
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertNull(resolvedType);
  }

  @Test
  public void testUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    String reference = "MyUnknownType";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertNull(resolvedType);
  }

  @Test
  public void testUnresolvedTypeWithValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof FunctionType;
      }
    };
    String reference = "MyUnknownType";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    namedType.setValidator(validator);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertNull(resolvedType);
  }

  @Test(expected = IllegalStateException.class)
  public void testCycleDetection() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType rootType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    EnumType enumType = new EnumType(
        registry, "MyEnum", null, null, null, null, null, null, null, null);
    PropertyType propertyType = new PropertyType(
        registry, null, null, null, null, null, null, null, null, null);
    enumType.defineElement("MY_ENUM_VALUE", propertyType, null, null, null);
    ObjectType objectType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    PropertyType propertyType2 = new PropertyType(
        registry, null, null, null, null, null, null, null, null, null);
    enumType.defineElement("MY_ENUM_VALUE2", propertyType2, null, null, null);
    objectType.defineProperty("myEnumProperty", enumType.getInstanceType(), true, null);
    objectType.defineProperty("myEnumProperty2", enumType.getInstanceType(), true, null);
    enclosing.setSlot("MyObjectType", new StaticSlot<JSType>(objectType));
    String reference = "MyObjectType.myEnumProperty.MY_ENUM_VALUE.myEnumProperty2";
    NamedType namedType = new NamedType(registry, reference, null, 1, 1);
    namedType.resolveInternal(t, enclosing);
  }
}