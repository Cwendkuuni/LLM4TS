package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.parse.Scope;
import com.google.javascript.jscomp.parse.types.JSType;
import com.google.javascript.jscomp.parse.types.JSTypeRegistry;
import com.google.javascript.jscomp.parse.types.ReferenceType;
import com.google.javascript.jscomp.parse.types.ReferenceType.TypeReference;
import com.google.javascript.jscomp.scope.StaticScope;

import java.util.List;

/**
 * Test cases for {@link NamedType} class.
 */
public class NamedTypeTest {

  @Test
  public void testNamedTypeCreation() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("String")).thenReturn(mock(JSType.class));

    NamedType namedType = new NamedType(registry, "String", null, 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        namedType.getBaseType());
  }

  @Test
  public void testDefinePropertyWhenUnresolved() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("String")).thenReturn(mock(JSType.class));

    NamedType namedType = new NamedType(registry, "String", null, 1, 1);
    namedType.defineProperty("propertyName", mock(JSType.class), false, null);

    assertNull(namedType.getProperties().get("propertyName"));
  }

  @Test
  public void testDefinePropertyWhenResolved() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    when(registry.getType("String")).thenReturn(stringType);

    NamedType namedType = new NamedType(registry, "String", null, 1, 1);
    namedType.setReferencedType(stringType);

    namedType.defineProperty("propertyName", mock(JSType.class), false, null);

    assertNotNull(namedType.getProperties().get("propertyName"));
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    JSType numberType = mock(JSType.class);
    JSType unknownType = mock(JSType.class);

    when(registry.getType("String")).thenReturn(stringType);
    when(registry.getType("Number")).thenReturn(numberType);
    when(registry.getType("Unknown")).thenReturn(unknownType);

    NamedType namedType = new NamedType(registry, "String", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), mock(StaticScope.class));

    assertEquals(stringType, namedType.getReferencedType());
    assertEquals(stringType, namedType.getResolvedType());

    namedType = new NamedType(registry, "Number", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), mock(StaticScope.class));

    assertEquals(numberType, namedType.getReferencedType());
    assertEquals(numberType, namedType.getResolvedType());

    namedType = new NamedType(registry, "Unknown", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), mock(StaticScope.class));

    assertEquals(unknownType, namedType.getReferencedType());
    assertEquals(unknownType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    JSType numberType = mock(JSType.class);
    JSType unknownType = mock(JSType.class);
    JSType functionType = mock(FunctionType.class);
    JSType enumType = mock(EnumType.class);

    when(registry.getType("String")).thenReturn(stringType);
    when(registry.getType("Number")).thenReturn(numberType);
    when(registry.getType("Unknown")).thenReturn(unknownType);

    StaticScope<JSType> staticScope = mock(StaticScope.class);
    when(staticScope.getSlot("String")).thenReturn(new StaticSlot<>(stringType));
    when(stringType.isFunctionType()).thenReturn(false);
    when(stringType.isInterface()).thenReturn(false);
    when(staticScope.getSlot("Number")).thenReturn(new StaticSlot<>(numberType));
    when(numberType.isFunctionType()).thenReturn(false);
    when(numberType.isInterface()).thenReturn(false);
    when(staticScope.getSlot("EnumType")).thenReturn(new StaticSlot<>(enumType));
    when(enumType.getElementsType()).thenReturn(numberType);

    NamedType namedType = new NamedType(registry, "String.prototype.method", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(stringType, namedType.getReferencedType());
    assertEquals(functionType, namedType.getResolvedType());

    namedType = new NamedType(registry, "Number.prototype.method", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(numberType, namedType.getReferencedType());
    assertEquals(functionType, namedType.getResolvedType());

    namedType = new NamedType(registry, "EnumType.Element", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(numberType, namedType.getReferencedType());
    assertEquals(numberType, namedType.getResolvedType());

    namedType = new NamedType(registry, "Function", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(registry.getNativeFunctionType(JSTypeNative.NO_OBJECT_TYPE)
        .getInstanceType(), namedType.getReferencedType());
    assertEquals(functionType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    JSType numberType = mock(JSType.class);
    JSType unknownType = mock(JSType.class);
    JSType functionType = mock(FunctionType.class);
    JSType constructorType = mock(FunctionType.class);

    when(registry.getType("String")).thenReturn(stringType);
    when(registry.getType("Number")).thenReturn(numberType);
    when(registry.getType("Unknown")).thenReturn(unknownType);

    StaticScope<JSType> staticScope = mock(StaticScope.class);
    when(staticScope.getSlot("Function")).thenReturn(new StaticSlot<>(functionType));
    when(functionType.isConstructor()).thenReturn(true);
    when(functionType.isInterface()).thenReturn(false);
    when(functionType.getInstanceType()).thenReturn(constructorType);

    NamedType namedType = new NamedType(registry, "Function", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(constructorType, namedType.getReferencedType());
    assertEquals(constructorType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    JSType numberType = mock(JSType.class);
    JSType unknownType = mock(JSType.class);
    JSType enumType = mock(EnumType.class);
    JSType elementsType = mock(JSType.class);

    when(registry.getType("String")).thenReturn(stringType);
    when(registry.getType("Number")).thenReturn(numberType);
    when(registry.getType("Unknown")).thenReturn(unknownType);

    StaticScope<JSType> staticScope = mock(StaticScope.class);
    when(staticScope.getSlot("EnumType")).thenReturn(new StaticSlot<>(enumType));
    when(enumType.getElementsType()).thenReturn(elementsType);

    NamedType namedType = new NamedType(registry, "EnumType.Element", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(elementsType, namedType.getReferencedType());
    assertEquals(elementsType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaPropertiesWithUnresolvedType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    JSType numberType = mock(JSType.class);
    JSType unknownType = mock(JSType.class);
    JSType unresolvedType = mock(JSType.class);

    when(registry.getType("String")).thenReturn(stringType);
    when(registry.getType("Number")).thenReturn(numberType);
    when(registry.getType("Unknown")).thenReturn(unknownType);
    when(registry.getType("UnresolvedType")).thenReturn(unresolvedType);

    StaticScope<JSType> staticScope = mock(StaticScope.class);
    when(staticScope.getSlot("UnresolvedType")).thenReturn(new StaticSlot<>(unresolvedType));

    NamedType namedType = new NamedType(registry, "UnresolvedType", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(unresolvedType, namedType.getReferencedType());
    assertEquals(unresolvedType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaPropertiesWithTypeReference() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType stringType = mock(JSType.class);
    JSType numberType = mock(JSType.class);
    JSType unknownType = mock(JSType.class);
    JSType typeReferenceType = mock(JSType.class);
    JSType typeReferenceTypeElement = mock(JSType.class);

    when(registry.getType("String")).thenReturn(stringType);
    when(registry.getType("Number")).thenReturn(numberType);
    when(registry.getType("Unknown")).thenReturn(unknownType);

    when(registry.getType("TypeReference")).thenReturn(typeReferenceType);
    when(typeReferenceType.isTypeReference()).thenReturn(true);
    when(typeReferenceType.getElementType()).thenReturn(typeReferenceTypeElement);

    StaticScope<JSType> staticScope = mock(StaticScope.class);
    when(staticScope.getSlot("TypeReference")).thenReturn(new StaticSlot<>(typeReferenceType));

    NamedType namedType = new NamedType(registry, "TypeReference", null, 1, 1);
    namedType.resolveInternal(mock(ErrorReporter.class), staticScope);

    assertEquals(typeReferenceTypeElement, namedType.getReferencedType());
    assertEquals(typeReferenceTypeElement, namedType.getResolvedType());
  }

  @Test
  public void testResolveWithImplicitPrototypeCycle() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType objectType = mock(JSType.class);
    JSType namedType = mock(NamedType.class);

    when(registry.getType("Object")).thenReturn(objectType);
    when(registry.getType(NamedType.class.getName())).thenReturn(namedType);

    NamedType namedTypeInstance = new NamedType(registry, NamedType.class.getName(), null, 1, 1);
    namedTypeInstance.resolveInternal(mock(ErrorReporter.class), mock(StaticScope.class));

    assertEquals(namedType, namedTypeInstance.getReferencedType());
    assertEquals(objectType, namedTypeInstance.getResolvedType());

    verify(registry, times(1)).handleTypeCycle(mock(ErrorReporter.class));
  }

  @Test
  public void testResolveWithEnumElementCycle() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    JSType enumType = mock(EnumType.class);
    JSType enumElementType = mock(EnumElementType.class);
    JSType namedType = mock(NamedType.class);

    when(registry.getType("EnumType")).thenReturn(enumType);
    when(registry.getType("NamedType")).thenReturn(namedType);

    when(enumType.getElementsType()).thenReturn(enumElementType);
    when(enumElementType.getPrimitiveType()).thenReturn(namedType);

    NamedType namedTypeInstance = new NamedType(registry, "NamedType", null, 1, 1);
    namedTypeInstance.resolveInternal(mock(ErrorReporter.class), mock(StaticScope.class));

    assertEquals(namedType, namedTypeInstance.getReferencedType());
    assertEquals(enumElementType, namedTypeInstance.getResolvedType());

    verify(registry, times(1)).checkEnumElementCycle(mock(ErrorReporter.class));
  }
}