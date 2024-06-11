package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.Symbol;
import com.google.javascript.rhino.TypeEquivalence;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyDescriptor;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.TypeOf;
import com.google.javascript.rhino.jstype.TypeOf.JSTypeType;

import java.util.List;
import java.util.function.Function;

public class NamedTypeTest {
  private static final ErrorReporter errorReporter = mock(ErrorReporter.class);
  private static final StaticScope<JSType> enclosingScope = mock(StaticScope.class);
  private static final JSTypeRegistry registry = mock(JSTypeRegistry.class);
  private static final ObjectType referencedObjType = mock(ObjectType.class);
  private static final StaticSlot<JSType> slot = mock(StaticSlot.class);
  private static final Symbol symbol = new Symbol("testSymbol");
  private static final String reference = "testReference";
  private static final String sourceName = "testSourceName";
  private static final int lineno = 1;
  private static final int charno = 1;

  @SuppressWarnings("unchecked")
  private static <T> T createMockList(Class<T> clazz, T... values) {
    return (T) mock(List.class, values);
  }

  @SuppressWarnings("unchecked")
  private static <T> T createMockFunction(Class<T> clazz, Function<Object, T> function) {
    return (T) mock(clazz, function);
  }

  @Test
  public void testDefineProperty() {
    when(enclosingScope.getSlot(anyString())).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "testPropertyName",
        TypeFactory.createStringType(),
        false,
        mock(Node.class));

    namedType.defineProperty("testPropertyName", null, false, null);
    verify(namedType).defineProperty(eq("testPropertyName"), any(JSType.class), eq(false), any(Node.class));

    namedType.finishPropertyContinuations();

    verify(referencedObjType, never()).defineProperty(anyString(), any(JSType.class), eq(false), any(Node.class));

    verify(namedType).finishPropertyContinuations();
  }

  @Test
  public void testResolveViaRegistry() {
    JSType type = TypeFactory.createNumberType();

    when(registry.getType(reference)).thenReturn(type);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertEquals(type, resolvedType);
  }

  @Test
  public void testResolveViaProperties() {
    JSType referencedType = TypeFactory.createObjectType();
    JSType propertyType = TypeFactory.createBooleanType();

    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(referencedType);
    when(referencedType.getPropertyType("testPropertyName")).thenReturn(propertyType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertEquals(propertyType, resolvedType);
  }

  @Test
  public void testResolveWithForwardDeclaration() {
    JSType referencedType = TypeFactory.createObjectType();
    JSType forwardDeclaredType = TypeFactory.createStringType();

    when(registry.getType(reference)).thenReturn(null);
    when(registry.isForwardDeclaredType(reference)).thenReturn(true);

    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(null);
    when(registry.getType(eq("String"))).thenReturn(forwardDeclaredType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertEquals(forwardDeclaredType, resolvedType);
  }

  @Test
  public void testResolveWithUnknownType() {
    JSType referencedType = TypeFactory.createObjectType();

    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertNull(resolvedType);
  }

  @Test
  public void testResolveWithUnknownTypeAndValidator() {
    JSType referencedType = TypeFactory.createObjectType();

    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = jstype -> {
      assertEquals(JSTypeType.OBJECT, TypeEquivalence.getType(jstype).getType());
      return true;
    };

    namedType.setValidator(validator);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertNull(resolvedType);
  }

  @Test
  public void testResolveWithUnknownTypeAndValidatorFail() {
    JSType referencedType = TypeFactory.createObjectType();

    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = jstype -> {
      fail("Validator should not be called if type is unknown");
      return true;
    };

    namedType.setValidator(validator);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertNull(resolvedType);
  }

  @Test
  public void testResolveWithUnknownTypeAndValidatorPass() {
    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = jstype -> {
      assertEquals(JSTypeType.OBJECT, TypeEquivalence.getType(jstype).getType());
      return true;
    };

    namedType.setValidator(validator);

    JSType referencedType = TypeFactory.createFunctionType(null, null);
    when(registry.getType(reference)).thenReturn(referencedType);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);

    assertEquals(referencedType, resolvedType);
  }

  @Test
  public void testHandleTypeCycle() {
    JSType referencedType = TypeFactory.createObjectType();

    when(enclosingScope.getSlot(reference)).thenReturn(slot);
    when(slot.getType()).thenReturn(referencedType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    namedType.resolveInternal(errorReporter, enclosingScope);

    verify(errorReporter).warning(
        eq("Cycle detected in inheritance chain of type " + reference),
        eq(sourceName), eq(lineno), eq(charno));
  }
}