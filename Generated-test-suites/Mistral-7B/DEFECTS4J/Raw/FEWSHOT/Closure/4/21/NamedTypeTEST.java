package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Symbol;
import com.google.javascript.rhino.SymbolTable;
import com.google.javascript.rhino.StylusObject;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TypeError;
import com.google.javascript.rhino.jstype.TypeRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class NamedTypeTest {

  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private TypeRegistry typeRegistry;
  private NamedType namedType;
  private StylusObject enclosing;

  @org.junit.Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    staticScope = mock(StaticScope.class);
    typeRegistry = mock(TypeRegistry.class);
    when(staticScope.getTypeRegistry()).thenReturn(typeRegistry);
    when(typeRegistry.getType(anyString())).thenReturn(null);

    enclosing = mock(StylusObject.class);

    namedType = new NamedType(typeRegistry, "Foo", "test", 1, 1);
  }

  @org.junit.Test
  public void testDefineProperty() {
    final String propertyName = "myProperty";
    final JSType propertyType = new ObjectType(typeRegistry, "String");

    namedType.defineProperty(propertyName, propertyType, false, null);

    verify(staticScope, times(1)).defineProperty(
        propertyName, eq(propertyType), anyBoolean(), any(Node.class));
  }

  @org.junit.Test
  public void testResolveViaRegistry() {
    final String reference = "Foo";
    JSType resolvedType = new ObjectType(typeRegistry, "Foo");

    when(typeRegistry.getType(reference)).thenReturn(resolvedType);

    namedType = new NamedType(typeRegistry, reference, "test", 1, 1);

    JSType referencedType = namedType.resolveInternal(errorReporter, staticScope);

    assertEquals(resolvedType, referencedType);
  }

  @org.junit.Test
  public void testResolveViaRegistry_UnresolvedType() {
    namedType = new NamedType(typeRegistry, "Foo", "test", 1, 1);

    JSType referencedType = namedType.resolveInternal(errorReporter, staticScope);

    assertNull(referencedType);
  }

  @org.junit.Test
  public void testResolveViaProperties() throws NoSuchMethodException,
      InstantiationException, InvocationTargetException {
    final String reference = "Foo.Bar.Baz";
    final String firstComponent = "Foo";

    staticScope.putSlot(new Symbol(firstComponent), new ObjectType(typeRegistry, firstComponent));

    ObjectType firstComponentType = (ObjectType) staticScope.getSlot(new Symbol(firstComponent));
    firstComponentType.defineProperty(firstComponent, new ObjectType(typeRegistry, "Bar"), false, null);
    ObjectType secondComponentType = (ObjectType) firstComponentType.getPropertyType(firstComponent);
    secondComponentType.defineProperty("Baz", new ObjectType(typeRegistry, "Baz"), false, null);

    namedType = new NamedType(typeRegistry, reference, "test", 1, 1);

    JSType referencedType = namedType.resolveInternal(errorReporter, staticScope);

    ObjectType resolvedObjectType = (ObjectType) referencedType;
    assertEquals(secondComponentType, resolvedObjectType.getPropertyType("Baz"));
  }

  @org.junit.Test
  public void testResolveViaPropertiesWithInvalidType() {
    final String reference = "Foo.Bar.Baz";
    final String firstComponent = "Foo";

    staticScope.putSlot(new Symbol(firstComponent), new ObjectType(typeRegistry, firstComponent));

    ObjectType firstComponentType = (ObjectType) staticScope.getSlot(new Symbol(firstComponent));
    firstComponentType.defineProperty(firstComponent, new ObjectType(typeRegistry, "Bar"), false, null);

    namedType = new NamedType(typeRegistry, reference, "test", 1, 1);

    namedType.resolveInternal(errorReporter, staticScope);

    verify(errorReporter, times(1)).warning(anyString(), eq("test"), anyInt(), anyInt());
  }

  @org.junit.Test
  public void testSetReferencedAndResolvedType() {
    final String reference = "Foo";
    final JSType resolvedType = new ObjectType(typeRegistry, reference);

    namedType = new NamedType(typeRegistry, reference, "test", 1, 1);

    namedType.setReferencedAndResolvedType(resolvedType, errorReporter, staticScope);

    assertEquals(resolvedType, namedType.getReferencedType());
    assertEquals(resolvedType, namedType.getResolvedType());
  }

  @org.junit.Test
  public void testSetReferencedAndResolvedType_NullType() {
    namedType = new NamedType(typeRegistry, "Foo", "test", 1, 1);

    namedType.setReferencedAndResolvedType(null, errorReporter, staticScope);

    assertNull(namedType.getReferencedType());
    assertNull(namedType.getResolvedType());
  }

  @org.junit.Test
  public void testSetReferencedAndResolvedType_NullErrorReporter() {
    final JSType resolvedType = new ObjectType(typeRegistry, "Foo");

    namedType = new NamedType(typeRegistry, "Foo", "test", 1, 1);

    namedType.setReferencedAndResolvedType(resolvedType, null, staticScope);

    assertEquals(resolvedType, namedType.getReferencedType());
    assertEquals(resolvedType, namedType.getResolvedType());
  }

  @org.junit.Test
  public void testSetReferencedAndResolvedType_NullStaticScope() {
    final JSType resolvedType = new ObjectType(typeRegistry, "Foo");

    namedType = new NamedType(typeRegistry, "Foo", "test", 1, 1);

    namedType.setReferencedAndResolvedType(resolvedType, errorReporter, null);

    assertEquals(resolvedType, namedType.getReferencedType());
    assertEquals(resolvedType, namedType.getResolvedType());
  }

  @org.junit.Test
  public void testHandleTypeCycle() {
    namedType = new NamedType(typeRegistry, "Foo", "test", 1, 1);

    namedType.resolveInternal(errorReporter, staticScope);

    verify(errorReporter, times(1)).warning(anyString(), eq("test"), anyInt(), anyInt());
  }
}