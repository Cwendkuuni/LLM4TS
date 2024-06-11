package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;

import java.util.List;

public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("String")).thenReturn(JSType.STRING);

    StaticScope<JSType> enclosing = mock(StaticScope.class);
    when(enclosing.getSlot("String")).thenReturn(new StaticSlot<>(null, JSType.STRING));

    NamedType namedType = new NamedType(registry, "String", "file.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(JSType.STRING, resolvedType);
    verify(errorReporter, never()).warning(anyString(), anyString(), anyInt(), anyInt());
  }

  @Test
  public void testResolveViaProperties() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("Object")).thenReturn(ObjectType.OBJECT);
    when(registry.getType("Object.String")).thenReturn(JSType.STRING);

    StaticScope<JSType> enclosing = mock(StaticScope.class);
    when(enclosing.getSlot("Object")).thenReturn(new StaticSlot<>(ObjectType.OBJECT, null));

    NamedType namedType = new NamedType(registry, "Object.String", "file.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(JSType.STRING, resolvedType);
    verify(errorReporter, never()).warning(anyString(), anyString(), anyInt(), anyInt());
  }

  @Test
  public void testResolveUnknownType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("UnknownType")).thenReturn(null);

    StaticScope<JSType> enclosing = mock(StaticScope.class);
    when(enclosing.getSlot("UnknownType")).thenReturn(new StaticSlot<>(null, null));

    NamedType namedType = new NamedType(registry, "UnknownType", "file.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(JSType.NO_RESOLVED_TYPE, resolvedType);
    verify(errorReporter).warning("Bad type annotation. Unknown type UnknownType", anyString(), eq(1), eq(1));
  }

  @Test
  public void testResolveCyclicType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("CycleType")).thenReturn(new NamedType(registry, "CycleType", "file.js", 1, 1));

    StaticScope<JSType> enclosing = mock(StaticScope.class);

    NamedType namedType = new NamedType(registry, "CycleType", "file.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    verify(errorReporter).warning("Cycle detected in inheritance chain of type CycleType", anyString(), eq("file.js"), anyInt(), anyInt());
  }

  @Test
  public void testResolveCyclicEnum() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("EnumType")).thenReturn(new EnumType("EnumType", new Node(), null));
    when(registry.getType("EnumType.Element"))
        .thenReturn(new EnumElementType("EnumType", "Element", null));

    StaticScope<JSType> enclosing = mock(StaticScope.class);

    NamedType namedType = new NamedType(registry, "EnumType.Element", "file.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    verify(errorReporter).warning("Cycle detected in inheritance chain of type EnumType.Element", anyString(), eq("file.js"), anyInt(), anyInt());
  }

  @Test
  public void testResolveInterfaceWithConstructor() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("InterfaceType"))
        .thenReturn(new FunctionType(null, Lists.newArrayList(), null, null, null));

    StaticScope<JSType> enclosing = mock(StaticScope.class);

    NamedType namedType = new NamedType(registry, "InterfaceType", "file.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    FunctionType functionType = FunctionType.INSTANCE;
    functionType.setInstanceType(JSType.OBJECT);
    assertEquals(functionType.getInstanceType(), resolvedType);
  }

  @Test
  public void testResolveNoObjectType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("NoObjectType"))
        .thenReturn(JSTypeNative.NO_OBJECT_TYPE);

    StaticScope<JSType> enclosing = mock(StaticScope.class);

    NamedType namedType = new NamedType(registry, "NoObjectType", "file.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(JSTypeNative.NO_OBJECT_TYPE.getInstanceType(), resolvedType);
  }

  @Test
  public void testResolveEnumType() {
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    when(registry.getType("EnumType"))
        .thenReturn(new EnumType("EnumType", new Node(), null));

    StaticScope<JSType> enclosing = mock(StaticScope.class);

    NamedType namedType = new NamedType(registry, "EnumType", "file.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(JSType.OBJECT, resolvedType.getBaseType());
  }
}