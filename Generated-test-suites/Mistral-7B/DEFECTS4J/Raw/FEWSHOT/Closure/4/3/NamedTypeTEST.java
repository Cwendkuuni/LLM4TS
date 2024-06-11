package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

import java.util.List;

public class NamedTypeTest {

  @Test
  public void testNewNamedType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    String reference = "MyType";
    String sourceName = "MyFile.js";
    int lineno = 5;
    int charno = 10;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(registry, namedType.registry);
    assertEquals(reference, namedType.reference);
    assertEquals(sourceName, namedType.sourceName);
    assertEquals(lineno, namedType.lineno);
    assertEquals(charno, namedType.charno);
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    String reference = "MyType";
    String sourceName = "MyFile.js";
    int lineno = 5;
    int charno = 10;

    JSType type = mock(JSType.class);

    when(registry.getType(reference)).thenReturn(type);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertTrue(namedType.resolveInternal(errorReporter, enclosing));

    assertEquals(type, namedType.getReferencedType());
    assertEquals(type, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    String reference = "MyType.InnerType.InnerInnerType";
    String sourceName = "MyFile.js";
    int lineno = 5;
    int charno = 10;

    StaticSlot<ObjectType> slot1 = mock(StaticSlot.class);
    ObjectType parentClass1 = mock(ObjectType.class);
    ObjectType parentClass2 = mock(ObjectType.class);
    ObjectType innerInnerType = mock(ObjectType.class);

    when(enclosing.getSlot(reference.split("\\.")[0])).thenReturn(slot1);
    when(slot1.getType()).thenReturn(parentClass1);
    when(parentClass1.getPropertyType(reference.split("\\.")[1])).thenReturn(parentClass2);
    when(parentClass2.getPropertyType(reference.split("\\.")[2])).thenReturn(innerInnerType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertTrue(namedType.resolveInternal(errorReporter, enclosing));

    assertEquals(innerInnerType, namedType.getReferencedType());
    assertEquals(innerInnerType, namedType.getResolvedType());
  }

  @Test
  public void testUnresolvedType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    String reference = "MyType";
    String sourceName = "MyFile.js";
    int lineno = 5;
    int charno = 10;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
    assertNull(namedType.getResolvedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    String reference = "MyType";
    String sourceName = "MyFile.js";
    int lineno = 5;
    int charno = 10;
    Predicate<JSType> validator = mock(Predicate.class);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Unresolved type, validator is not called
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
    assertNull(namedType.getResolvedType());
    assertFalse(namedType.setValidator(validator));

    // Resolved type, validator is called
    JSType referencedType = mock(JSType.class);
    when(registry.getType(reference)).thenReturn(referencedType);
    namedType.resolveInternal(errorReporter, enclosing);
    assertTrue(namedType.isResolved());
    assertEquals(referencedType, namedType.getReferencedType());
    assertEquals(referencedType, namedType.getResolvedType());
    assertTrue(namedType.setValidator(validator));
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = mock(StaticScope.class);
    String reference = "MyType";
    String sourceName = "MyFile.js";
    int lineno = 5;
    int charno = 10;

    ObjectType parentType = mock(ObjectType.class);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.defineProperty("prop", JSType.NULL, false, null);

    assertEquals(null, namedType.propertyContinuations);

    JSType resolvedType = mock(JSType.class);
    when(registry.getType(reference)).thenReturn(resolvedType);
    namedType.resolveInternal(errorReporter, enclosing);

    List<PropertyContinuation> propertyContinuations = namedType.propertyContinuations;
    assertEquals(1, propertyContinuations.size());
    PropertyContinuation propertyContinuation = propertyContinuations.get(0);
    assertEquals("prop", propertyContinuation.propertyName);
    assertEquals(JSType.NULL, propertyContinuation.type);
    assertEquals(false, propertyContinuation.inferred);
    assertEquals(null, propertyContinuation.propertyNode);

    namedType.finishPropertyContinuations();

    assertEquals(null, namedType.propertyContinuations);
  }
}