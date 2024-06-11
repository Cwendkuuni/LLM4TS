package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;

import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link NamedType} class.
 */
public class NamedTypeTest {
  @Test
  public void testNamedTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    // Test empty constructor
    NamedType namedType1 = new NamedType(registry, "", "", 0, 0);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType1.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType1.getResolvedType());

    // Test constructor with reference and source name
    String reference = "FooType";
    NamedType namedType2 = new NamedType(registry, reference, " SourceName", 1, 1);
    assertEquals(reference, namedType2.getReferenceName());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType2.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType2.getResolvedType());

    // Test constructor with validate
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        assertEquals(JSTypeNative.NUMBER_TYPE, input);
        return true;
      }
    };
    namedType2.setValidator(validator);
    JSType referencedType = new FunctionType(registry, null, new ObjectType(registry), null);
    JSType resolvedType = new ObjectType(registry, referencedType);
    namedType2.setReferencedAndResolvedType(resolvedType, errorReporter, null);
    assertTrue(namedType2.isResolved());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference = "FooType";
    NamedType namedType = new NamedType(registry, reference, "", 0, 0);

    namedType.defineProperty("propertyName", JSType.NULL, false, null);
    assertNull(namedType.getPropertyType("propertyName"));
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference1 = "FooType";
    String reference2 = "BarType";
    registry.registerType(reference1, new FunctionType(registry, null, null, null));
    registry.registerType(reference2, new FunctionType(registry, null, null, null));

    NamedType namedType1 = new NamedType(registry, reference1, "", 0, 0);
    JSType referencedType1 = new FunctionType(registry, null, null, null);
    namedType1.setReferencedAndResolvedType(referencedType1, errorReporter, null);
    assertEquals(referencedType1, namedType1.getReferencedType());
    assertEquals(referencedType1, namedType1.getResolvedType());

    NamedType namedType2 = new NamedType(registry, reference2, "", 0, 0);
    assertNull(namedType2.getReferencedType());
    assertNull(namedType2.getResolvedType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference1 = "FooType.BarType";
    String reference2 = "FooType";

    StaticScope<JSType> enclosing = new StaticScope<>(registry);
    StaticSlot<JSType> slot1 = enclosing.put("FooType", new ObjectType(registry));

    NamedType namedType1 = new NamedType(registry, reference1, "", 0, 0);
    namedType1.resolveInternal(errorReporter, enclosing);
    JSType referencedType1 = slot1.getType().getPropertyType("BarType");
    namedType1.setReferencedAndResolvedType(referencedType1, errorReporter, enclosing);
    assertEquals(referencedType1, namedType1.getReferencedType());
    assertEquals(referencedType1, namedType1.getResolvedType());

    NamedType namedType2 = new NamedType(registry, reference2, "", 0, 0);
    namedType2.resolveInternal(errorReporter, enclosing);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType2.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType2.getResolvedType());
  }

  @Test
  public void testResolveTypeWithCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference = "FooType";
    registry.registerType(reference, new ObjectType(registry));

    NamedType namedType = new NamedType(registry, reference, "", 0, 0);
    namedType.resolveInternal(errorReporter, null);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getResolvedType());

    errorReporter.expectWarning(1);
    namedType.resolveInternal(errorReporter, null);
  }

  @Test
  public void testResolveTypeWithEnumCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference = "EnumType";
    registry.registerType(reference, new EnumType(registry, "EnumType", null));

    NamedType namedType = new NamedType(registry, reference, "", 0, 0);
    namedType.resolveInternal(errorReporter, null);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getResolvedType());

    EnumElementType enumElementType = new EnumElementType(registry, "EnumType", null, null);
    namedType.setReferencedAndResolvedType(enumElementType, errorReporter, null);

    errorReporter.expectWarning(1);
    namedType.resolveInternal(errorReporter, null);
  }

  @Test
  public void testResolveTypeWithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference = "UnresolvedType";

    NamedType namedType = new NamedType(registry, reference, "", 0, 0);
    namedType.resolveInternal(errorReporter, null);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getResolvedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    String reference = "FooType";
    NamedType namedType = new NamedType(registry, reference, "", 0, 0);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        assertEquals(JSTypeNative.NUMBER_TYPE, input);
        return true;
      }
    };

    namedType.setValidator(validator);
    JSType referencedType = new FunctionType(registry, null, null, null);
    JSType resolvedType = new ObjectType(registry, referencedType);
    namedType.setReferencedAndResolvedType(resolvedType, errorReporter, null);
    assertEquals(JSTypeNative.NUMBER_TYPE, namedType.getReferencedType());
    assertEquals(JSTypeNative.NUMBER_TYPE, namedType.getResolvedType());
  }
}