package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.SymbolTable;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Symbol;

import java.util.List;

/**
 * Test cases for {@code NamedType} class.
 */
public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    SymbolTable symbolTable = mock(SymbolTable.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    when(registry.getType("String")).thenReturn(JSTypeNative.STRING_TYPE);

    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(
        namedType.getReferencedType().equals(JSTypeNative.STRING_TYPE));
  }

  @Test
  public void testResolveViaProperties() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    SymbolTable symbolTable = mock(SymbolTable.class);

    ObjectType parentObjectType = mock(ObjectType.class);
    when(parentObjectType.getPropertyType("foo")).thenReturn(
        JSTypeNative.NUMBER_TYPE);

    ObjectType parentObjectType2 = mock(ObjectType.class);
    when(parentObjectType2.getPropertyType("bar")).thenReturn(
        JSTypeNative.STRING_TYPE);

    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(slot.getType()).thenReturn(parentObjectType);

    when(enclosingScope.getSlot("Object")).thenReturn(slot);

    String reference = "Object.foo.bar";
    NamedType namedType = new NamedType(null, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(
        namedType.getReferencedType().equals(JSTypeNative.STRING_TYPE));
  }

  @Test
  public void testResolveViaPropertiesWithNoObjectType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    SymbolTable symbolTable = mock(SymbolTable.class);

    JSType noObjectType = JSTypeNative.NO_OBJECT_TYPE;
    JSType instanceType = noObjectType.getInstanceType();

    when(enclosingScope.getSlot("Object")).thenReturn(null);

    String reference = "Object";
    NamedType namedType = new NamedType(null, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(
        namedType.getReferencedType().equals(instanceType));
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    SymbolTable symbolTable = mock(SymbolTable.class);

    EnumType enumType = mock(EnumType.class);
    when(enumType.getElementsType()).thenReturn(JSTypeNative.STRING_TYPE);

    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(slot.getType()).thenReturn(enumType);

    when(enclosingScope.getSlot("Object")).thenReturn(slot);

    String reference = "Object.Enum.FOO";
    NamedType namedType = new NamedType(null, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(
        namedType.getReferencedType().equals(JSTypeNative.STRING_TYPE));
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    SymbolTable symbolTable = mock(SymbolTable.class);

    FunctionType functionType = mock(FunctionType.class);
    when(functionType.getInstanceType()).thenReturn(
        JSTypeNative.FUNCTION_TYPE);

    StaticSlot<JSType> slot = mock(StaticSlot.class);
    when(slot.getType()).thenReturn(functionType);

    when(enclosingScope.getSlot("Object")).thenReturn(slot);

    String reference = "Object.Function";
    NamedType namedType = new NamedType(null, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(
        namedType.getReferencedType().equals(JSTypeNative.FUNCTION_TYPE));
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    SymbolTable symbolTable = mock(SymbolTable.class);

    when(enclosingScope.getSlot("Object")).thenReturn(null);

    String reference = "Object";
    NamedType namedType = new NamedType(null, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(
        namedType.getReferencedType().equals(JSTypeNative.NO_RESOLVED_TYPE));
  }

  @Test
  public void testDefineProperty() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.defineProperty("foo", JSTypeNative.STRING_TYPE, false, null))
        .thenReturn(true);

    when(enclosingScope.getType()).thenReturn(objectType);

    String reference = "Object";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.defineProperty("foo", JSTypeNative.STRING_TYPE, false, null);

    Preconditions.checkState(namedType.isResolved());
    Preconditions.checkState(
        namedType.getPropertyType("foo").equals(JSTypeNative.STRING_TYPE));
  }

  @Test
  public void testFinishPropertyContinuations() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.defineProperty("foo", JSTypeNative.STRING_TYPE, false, null))
        .thenReturn(true);

    when(enclosingScope.getType()).thenReturn(objectType);

    String reference = "Object";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.defineProperty("foo", JSTypeNative.STRING_TYPE, false, null);
    namedType.finishPropertyContinuations();

    Preconditions.checkState(namedType.isResolved());
    Preconditions.checkState(
        namedType.getPropertyType("foo").equals(JSTypeNative.STRING_TYPE));
  }

  @Test
  public void testHandleUnresolvedType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    when(enclosingScope.getType()).thenReturn(null);

    String reference = "Object";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(namedType.getReferencedType().equals(
        JSTypeNative.NO_RESOLVED_TYPE));

    // No warnings logged when it's a forward-declared type name.
    String forwardDeclaredReference = "Object";
    NamedType forwardDeclaredNamedType = new NamedType(registry,
        forwardDeclaredReference, null, 0, 0);
    forwardDeclaredNamedType.resolveInternal(errorReporter, enclosingScope);
    Preconditions.checkState(
        namedType.getReferencedType().equals(JSTypeNative.NO_RESOLVED_TYPE));
    assertNull(errorReporter.getWarnings());
  }

  @Test
  public void testHandleUnresolvedTypeWithWarning() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosingScope = mock(StaticScope.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);

    when(enclosingScope.getType()).thenReturn(null);

    String reference = "Unknown";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);
    namedType.resolveInternal(errorReporter, enclosingScope);

    Preconditions.checkState(namedType.getReferencedType().equals(
        JSTypeNative.NO_RESOLVED_TYPE));

    // Warnings logged when it's not a forward-declared type name.
    String unknownReference = "Unknown";
    NamedType unknownNamedType = new NamedType(registry, unknownReference, null, 0, 0);
    unknownNamedType.resolveInternal(errorReporter, enclosingScope);
    List<String> warnings = Lists.newArrayList(errorReporter.getWarnings());
    assertThat(warnings).hasSize(1);
    assertThat(warnings.get(0)).contains("Unknown type ");
    assertThat(warnings.get(0)).contains(unknownReference);
  }
}