package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.NativeObjectType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.PropertyContinuation;
import com.google.javascript.rhino.PropertyType;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.TypeError;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

/**
 * Tests for {@link com.google.javascript.rhino.jstype.NamedType}.
 */
public class NamedTypeTest {

  private NamedType namedType;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private StaticSlot<JSType> staticSlot;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    staticScope = mock(StaticScope.class);
    staticSlot = mock(StaticSlot.class);

    when(staticSlot.getType()).thenReturn(null);
    when(staticScope.getSlot(eq("Foo"))).thenReturn(staticSlot);

    namedType = new NamedType(
        mock(JSTypeRegistry.class),
        "Foo",
        "Test",
        1,
        1);
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testDefinePropertyWithUnresolvedType() {
    namedType.defineProperty("bar", mock(JSType.class), false, null);

    ArgumentCaptor<PropertyContinuation> propertyContinuationCaptor =
        ArgumentCaptor.forClass(PropertyContinuation.class);
    verify(staticScope).getSlot("Foo");
    verify(staticScope).defineProperty("Foo$bar", argThat(isNull()), eq(false), any());
    verify(staticScope).getSlot("Foo$bar").getType();
    verify(staticScope).defineProperty("Foo$bar", eq(null), eq(false), any());
    verify(namedType).finishPropertyContinuations();
  }

  @Test
  public void testGetReferencedTypeWithUnresolvedType() {
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testToStringHelper() {
    assertEquals("Foo", namedType.toStringHelper(false));
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("Foo", namedType.getReferenceName());
  }

  @Test
  public void testResolveViaRegistryWithResolvedType() {
    JSType resolvedType = mock(JSType.class);
    when(mock(JSTypeRegistry.class).getType("Foo")).thenReturn(resolvedType);

    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(resolvedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistryWithUnresolvedType() {
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertNotNull(namedType.getReferencedType());
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
  }

  @Test(expected = TypeError.class)
  public void testResolveViaPropertiesWithUnresolvedType() {
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);
    when(staticSlot.getType()).thenReturn(mock(FunctionType.class));

    namedType.resolveInternal(errorReporter, staticScope);
  }

  @Test
  public void testSetReferencedAndResolvedType() {
    JSType referencedType = mock(JSType.class);
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, staticScope);

    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveWithCycle() {
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);
    when(staticSlot.getType()).thenReturn(namedType);

    namedType.resolveInternal(errorReporter, staticScope);
  }

  @Test
  public void testGetReferencedObjTypeInternalWithUnknownType() {
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testGetReferencedObjTypeInternalWithFunctionType() {
    FunctionType functionType = mock(FunctionType.class);
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);
    when(staticSlot.getType()).thenReturn(functionType);

    assertEquals(functionType.getInstanceType(), namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testGetReferencedObjTypeInternalWithNoObjectType() {
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);
    when(staticSlot.getType()).thenReturn(mock(NoObjectType.class));

    assertEquals(NativeObjectType.getObjectType(), namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testGetReferencedObjTypeInternalWithEnumType() {
    EnumType enumType = mock(EnumType.class);
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);
    when(staticSlot.getType()).thenReturn(enumType);

    assertEquals(enumType.getElementsType(), namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testResolveInternalWithUnresolvedType() {
    namedType.resolveViaRegistry(errorReporter, staticScope);
    namedType.resolveViaProperties(errorReporter, staticScope);

    assertEquals(NativeObjectType.getObjectType(), namedType.getReferencedType());
  }

  @Test
  public void testResolveInternalWithResolvedType() {
    JSType resolvedType = mock(JSType.class);
    when(mock(JSTypeRegistry.class).getType("Foo")).thenReturn(resolvedType);

    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(resolvedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveInternalWithEnumElementType() {
    EnumType enumType = mock(EnumType.class);
    EnumElementType enumElementType = mock(EnumElementType.class);
    when(enumType.getElementsType()).thenReturn(enumElementType);
    when(enumElementType.getPrimitiveType()).thenReturn(namedType);

    when(mock(JSTypeRegistry.class).getType("Foo")).thenReturn(enumType);

    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(enumType, namedType.getReferencedType());
  }

  @Test
  public void testResolveInternalWithFunctionType() {
    FunctionType functionType = mock(FunctionType.class);
    FunctionType instanceType = mock(FunctionType.class);

    when(functionType.getInstanceType()).thenReturn(instanceType);
    when(instanceType.isFunctionType()).thenReturn(true);
    when(instanceType.isConstructor()).thenReturn(true);

    when(mock(JSTypeRegistry.class).getType("Foo"))
        .thenReturn(functionType);

    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(instanceType, namedType.getReferencedType());
  }

  @Test
  public void testResolveInternalWithNoObjectType() {
    when(mock(JSTypeRegistry.class).getType("Foo"))
        .thenReturn(mock(NoObjectType.class));

    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(NativeObjectType.getObjectType(), namedType.getReferencedType());
  }

  @Test
  public void testResolveInternalWithUnknownType() {
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testSetReferencedType() {
    JSType referencedType = mock(JSType.class);

    namedType.setReferencedType(referencedType);

    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testSetResolvedTypeInternalWithUnresolvedType() {
    namedType.setResolvedTypeInternal(null);

    assertNull(namedType.getResolvedType());
  }

  @Test
  public void testSetResolvedTypeInternalWithResolvedType() {
    when(staticScope.getSlot("Foo")).thenReturn(staticSlot);

    namedType.setReferencedAndResolvedType(mock(JSType.class), errorReporter, staticScope);

    assertEquals(mock(JSType.class), namedType.getResolvedType());
  }

  @Test
  public void testIsResolved() {
    assertFalse(namedType.isResolved());

    namedType.setReferencedAndResolvedType(mock(JSType.class), errorReporter, staticScope);

    assertTrue(namedType.isResolved());
  }

  @Test
  public void testHasReferenceName() {
    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testHashCode() {
    assertEquals(namedType.hashCode(), "Foo".hashCode());
  }

  @Test
  public void testIsNamedTypeWithUnknownType() {
    assertFalse(new NamedType(null, "Foo", null, 1, 1).isNamedType());
  }

  @Test
  public void testIsNamedTypeWithFunctionType() {
    FunctionType functionType = mock(FunctionType.class);

    assertTrue(new NamedType(null, "Foo", null, 1, 1, functionType).isNamedType());
  }

  @Test
  public void testIsNamedTypeWithEnumType() {
    EnumType enumType = mock(EnumType.class);

    assertTrue(new NamedType(null, "Foo", null, 1, 1, enumType).isNamedType());
  }

  @Test
  public void testIsNamedTypeWithObjectType() {
    ObjectType objectType = mock(ObjectType.class);

    assertFalse(new NamedType(null, "Foo", null, 1, 1, objectType).isNamedType());
  }

  @Test
  public void testIsNamedTypeWithNullType() {
    assertFalse(new NamedType(null, "Foo", null, 1, 1, null).isNamedType());
  }
}