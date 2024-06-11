package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.StaticType;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.StaticTypeRegistry;
import com.google.javascript.rhino.jstype.TypeConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Properties;

public class NamedTypeTest {
  private static final String CLASS_NAME = "NamedTypeTest";
  private static final String REFERENCE = "MyType";
  private static final String SOURCE_NAME = "MyTestSource";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  private StaticTypeRegistry registry;
  private ErrorReporter errorReporter;
  private NamedType namedType;
  private StaticScope<JSType> enclosingScope;
  private StaticSlot<JSType> slot;
  private ObjectType parentClass;

  @Before
  public void setUp() {
    registry = Mockito.mock(StaticTypeRegistry.class);
    errorReporter = Mockito.mock(ErrorReporter.class);

    namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    enclosingScope = Mockito.mock(StaticScope.class);
  }

  @Test
  public void testBasicCreate() {
    namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertNotNull(namedType);
    assertEquals(REFERENCE, namedType.getReferenceName());
  }

  @Test
  public void testDefineProperty() {
    PropertyContinuation propertyContinuation =  new PropertyContinuation(
        "testProperty", JSType.STRING, true, Mockito.mock(Node.class));

    namedType.defineProperty("testProperty", JSType.STRING, true, Mockito.mock(Node.class));

    assertEquals(propertyContinuation.propertyName, namedType.getPropertyNames()[0]);
    assertEquals(JSType.STRING, namedType.getPropertyType("testProperty"));
  }

  @Test(expected = AssertionError.class)
  public void testDefineProperty_UnresolvedType() {
    when(registry.getType(REFERENCE)).thenReturn(null);

    namedType.defineProperty("testProperty", JSType.STRING, true, Mockito.mock(Node.class));
  }

  @Test
  public void testFinishPropertyContinuations() {
    parentClass = Mockito.mock(ObjectType.class);
    slot = Mockito.mock(StaticSlot.class);
    when(enclosingScope.getSlot(REFERENCE)).thenReturn(slot);

    namedType.finishPropertyContinuations();

    verify(parentClass).defineProperty(eq(REFERENCE), isA(String.class), anyBoolean(), any(Node.class));
  }

  @Test
  public void testGetReferencedType_RegisteredType() {
    JSType registeredType = Mockito.mock(JSType.class);
    when(registry.getType(REFERENCE)).thenReturn(registeredType);

    namedType.setReferencedType(registeredType);
    assertEquals(registeredType, namedType.getReferencedType());
  }

  @Test
  public void testGetReferencedType_UnknownType() {
    when(registry.getType(REFERENCE)).thenReturn(null);

    namedType.setReferencedType(null);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testGetReferenceName() {
    assertEquals(REFERENCE, namedType.getReferenceName());
  }

  @Test
  public void testHashCode() {
    int expectedHashCode = REFERENCE.hashCode();

    assertEquals(expectedHashCode, namedType.hashCode());
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
  public void testResolve_RegisteredType() {
    JSType registeredType = Mockito.mock(JSType.class);
    when(registry.getType(REFERENCE)).thenReturn(registeredType);

    JSType resolvedType = namedType.resolve(errorReporter, enclosingScope);
    assertEquals(registeredType, resolvedType);
  }

  @Test(expected = AssertionError.class)
  public void testResolve_UnresolvedType() {
    when(registry.getType(REFERENCE)).thenReturn(null);

    namedType.resolve(errorReporter, enclosingScope);
  }

  @Test
  public void testResolve_RegisteredType_Resolved() {
    JSType registeredType = Mockito.mock(JSType.class);
    when(registry.getType(REFERENCE)).thenReturn(registeredType);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
    assertEquals(registeredType, resolvedType);
  }

  @Test(expected = AssertionError.class)
  public void testResolve_UnregisteredType() {
    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosingScope);
  }

  @Test
  public void testToStringHelper() {
    assertEquals(REFERENCE, namedType.toStringHelper(false));
  }

  @Test
  public void testToStringHelper_ForAnnotations() {
    assertEquals(REFERENCE, namedType.toStringHelper(true));
  }

  @Test
  public void testTypeOf() {
    when(registry.getType(REFERENCE)).thenReturn(TypeConstants.STRING);

    assertEquals(TypeConstants.STRING, namedType.getTypeOf(new Properties()));
  }

  @Test
  public void testTypeOf_UnknownType() {
    when(registry.getType(REFERENCE)).thenReturn(null);

    assertEquals(JSType.NULL, namedType.getTypeOf(new Properties()));
  }

  @Test
  public void testIsResolved() {
    assertFalse(namedType.isResolved());

    when(registry.getType(REFERENCE)).thenReturn(TypeConstants.STRING);

    namedType.resolveInternal(errorReporter, enclosingScope);
    assertTrue(namedType.isResolved());
  }
}