package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link NamedType} class.
 */
public class NamedTypeTest {

  private static final String CLASS_NAME = "NamedTypeTest";

  private ErrorReporter errorReporter;
  private StaticScope<JSType> staticScope;
  private JSTypeRegistry jstypeRegistry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {};
    jstypeRegistry = new JSTypeRegistry();
    staticScope = new StaticScope<>(jstypeRegistry, null, null);
  }

  @Test
  public void testCreateNamedType() {
    String reference = "String";
    int lineno = 1;
    int charno = 1;
    NamedType namedType = new NamedType(jstypeRegistry, reference, CLASS_NAME, lineno, charno);
    assertEquals("String", namedType.getReferenceName());
    assertFalse(namedType.isResolved());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getBaseType());
  }

  @Test
  public void testResolveNamedTypeViaRegistry() {
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    JSType stringType = jstypeRegistry.getType(reference);
    Preconditions.checkNotNull(stringType);

    // When
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // Then
    assertEquals(stringType, namedType.resolveInternal(errorReporter, staticScope));
    assertTrue(namedType.isResolved());
  }

  @Test(expected = TypeError.class)
  public void testResolveNamedTypeViaRegistryWithUnknownType() {
    String reference = "UnknownType";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given

    // When
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // Then
    namedType.resolveInternal(errorReporter, staticScope);
  }

  @Test
  public void testResolveNamedTypeViaProperties() {
    String reference = "String.prototype.toString";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    ObjectType stringType = new ObjectType(jstypeRegistry, "String");
    StaticSlot<JSType> slot = new StaticSlot<>(null, "prototype");
    StaticSlot<PropertyType> toStringSlot = new StaticSlot<>(stringType, "toString");
    staticScope.setSlot(slot, stringType);
    staticScope.setSlot(toStringSlot, toStringSlot.getType());

    // When
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // Then
    JSType toStringType = namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(JSTypeNative.STRING_TYPE, toStringType);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testResolveNamedTypeWithFunctionType() {
    String reference = "Function";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    FunctionType functionType = new FunctionType(
        jstypeRegistry, null, new ObjectType(jstypeRegistry, "Object"),
        new ObjectType(jstypeRegistry, "Object"), null);

    // When
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // Then
    FunctionType functionTypeInstance = (FunctionType) namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(functionType, functionTypeInstance);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testResolveNamedTypeWithInterfaceType() {
    String reference = "EventEmitter";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    InterfaceType interfaceType = new InterfaceType(
        jstypeRegistry, "EventEmitter", null, new List<JSTypeType>() {});

    // When
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // Then
    InterfaceType interfaceTypeInstance = (InterfaceType) namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(interfaceType, interfaceTypeInstance);
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testResolveNamedTypeWithCycle() {
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    NamedType namedType1 = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When
    NamedType namedType2 = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // Then
    try {
      namedType2.resolveInternal(errorReporter, staticScope);
      fail("Expected TypeError for cycle");
    } catch (TypeError e) {
      // Expected
    }

    assertSame(namedType1, namedType2.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isStringType();
      }
    };
    namedType.setValidator(validator);

    // Then
    JSType stringType = jstypeRegistry.getType(reference);
    assertEquals(stringType, namedType.resolveInternal(errorReporter, staticScope));
    assertTrue(namedType.isResolved());
    assertTrue(validator.apply(namedType.getReferencedType()));
  }

  @Test
  public void testDefineProperties() {
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When
    namedType.defineProperty("prop1", JSTypeNative.STRING_TYPE, true, null);
    namedType.defineProperty("prop2", JSTypeNative.NUMBER_TYPE, true, null);

    // Then
    ObjectType namedTypeInstance = (ObjectType) namedType.resolveInternal(errorReporter, staticScope);
    List<PropertyType> properties = namedTypeInstance.getProperties();

    assertEquals(2, properties.size());

    PropertyType prop1 = properties.get(0);
    assertEquals("prop1", prop1.getName());
    assertEquals(JSTypeNative.STRING_TYPE, prop1.getType());

    PropertyType prop2 = properties.get(1);
    assertEquals("prop2", prop2.getName());
    assertEquals(JSTypeNative.NUMBER_TYPE, prop2.getType());
  }

  @Test
  public void testPropertyContinuations() {
    String reference = "NamedTypeTest";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When
    namedType.defineProperty("prop1", JSTypeNative.STRING_TYPE, true, null);
    namedType.finishPropertyContinuations();

    // Then
    ObjectType namedTypeInstance = (ObjectType) namedType.resolveInternal(errorReporter, staticScope);
    List<PropertyType> properties = namedTypeInstance.getProperties();

    assertEquals(1, properties.size());

    PropertyType prop1 = properties.get(0);
    assertEquals("prop1", prop1.getName());
    assertEquals(JSTypeNative.STRING_TYPE, prop1.getType());
  }

  @Test
  public void testGetReferencedType() {
    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    JSType stringType = jstypeRegistry.getType(reference);
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When
    namedType.setReferencedTypeInternal(stringType);

    // Then
    assertEquals(stringType, namedType.getReferencedType());
  }

  @Test
  public void testGetReferencedObjTypeInternal() {
    String reference = "Object";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    ObjectType objectType = new ObjectType(jstypeRegistry, "Object");
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When
    namedType.setReferencedObjTypeInternal(objectType);

    // Then
    assertEquals(objectType, namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testIsResolved() {
    String reference = "UnknownType";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Given
    NamedType namedType = new NamedType(jstypeRegistry, reference, sourceName, lineno, charno);

    // When

    // Then
    assertFalse(namedType.isResolved());
  }
}