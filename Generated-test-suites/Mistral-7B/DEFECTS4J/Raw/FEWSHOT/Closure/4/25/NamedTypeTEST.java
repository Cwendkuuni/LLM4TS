package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", null, 0, 0);
    JSType stringType = registry.getType("String");
    namedType.setReferencedAndResolvedType(stringType, null, null);
    assertEquals("String", namedType.getReferenceName());
    assertEquals(stringType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    ObjectType parentClass = new ObjectType(registry, "Parent", null, 0, 0);
    parentClass.defineProperty("child", objType, false, null);

    NamedType namedType = new NamedType(registry, "Parent.child", null, 0, 0);
    StaticScope<JSType> enclosing = new StaticScope<>(registry);
    enclosing.putSlot("Parent", parentClass);

    namedType.resolveInternal(null, enclosing);
    assertEquals("Object", namedType.getReferenceName());
    assertEquals(objType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    FunctionType funcType = new FunctionType(registry, JSType.VOID, null, null);
    funcType.setInstanceType(objType);

    ObjectType parentClass = new ObjectType(registry, "Parent", null, 0, 0);
    parentClass.defineProperty("child", funcType, false, null);

    NamedType namedType = new NamedType(registry, "Parent.child", null, 0, 0);
    StaticScope<JSType> enclosing = new StaticScope<>(registry);
    enclosing.putSlot("Parent", parentClass);

    namedType.resolveInternal(null, enclosing);
    assertEquals("Function", namedType.getReferenceName());
    assertEquals(funcType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    EnumType enumType = new EnumType(registry, "MyEnum", null, 0, 0);
    enumType.defineElement("ONE", registry.getNativeNumberType(1));
    ObjectType parentClass = new ObjectType(registry, "Parent", null, 0, 0);
    parentClass.defineProperty("myEnum", enumType, false, null);

    NamedType namedType = new NamedType(registry, "Parent.myEnum.ONE", null, 0, 0);
    StaticScope<JSType> enclosing = new StaticScope<>(registry);
    enclosing.putSlot("Parent", parentClass);

    namedType.resolveInternal(null, enclosing);
    assertEquals("Number", namedType.getReferenceName());
    assertEquals(registry.getNativeNumberType(1), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType parentClass = new ObjectType(registry, "Parent", null, 0, 0);
    parentClass.defineProperty("child", null, false, null);

    NamedType namedType = new NamedType(registry, "Parent.child", null, 0, 0);
    StaticScope<JSType> enclosing = new StaticScope<>(registry);
    enclosing.putSlot("Parent", parentClass);

    namedType.resolveInternal(null, enclosing);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", null, 0, 0);
    Predicate<JSType> validator = type -> type.isStringType();
    namedType.setValidator(validator);

    JSType stringType = registry.getType("String");
    namedType.setReferencedAndResolvedType(stringType, null, null);
    assertTrue(validator.apply(namedType.getReferencedType()));
  }
}