package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.ProxiedType;
import com.google.javascript.rhino.jstype.ReferenceType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.UnknownType;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link NamedType} class.
 *
 */
public class NamedTypeTest {

  private static final String REFERENCE_NAME = "MyType";
  private static final String SOURCE_NAME = "NamedTypeTest";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private StaticScope<JSType> scope;
  private NamedType namedType;

  @Before
  public void setUp() {
    errorReporter = Mockito.mock(ErrorReporter.class);
    registry = new JSTypeRegistry();
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testCreateNamedType() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(ReferenceType.class, namedType.getClass());
    assertEquals(REFERENCE_NAME, namedType.getReferenceName());
    assertNull(namedType.getReferencedType());
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testDefineProperty() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    PropertyContinuation propertyContinuation =
        new PropertyContinuation("propertyName", null, false, null);

    namedType.defineProperty("propertyName", null, false, null);
    assertSame(propertyContinuation, namedType.getPropertyContinuation("propertyName"));

    namedType.finishPropertyContinuations();
    assertNull(namedType.getPropertyContinuation("propertyName"));
  }

  @Test
  public void testGetReferencedTypeInternal() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertNull(namedType.getReferencedTypeInternal());
  }

  @Test
  public void testIsNamedType() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testToStringHelper() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertEquals(REFERENCE_NAME, namedType.toStringHelper(false));
  }

  @Test
  public void testHashCode() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    int referenceHashCode = REFERENCE_NAME.hashCode();
    assertEquals(referenceHashCode, namedType.hashCode());
  }

  @Test
  public void testIsResolved() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testResolveViaRegistry() {
    JSType referencedType = Mockito.mock(JSType.class);
    registry.registerType(REFERENCE_NAME, referencedType);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(errorReporter, scope);
    assertEquals(referencedType, resolvedType);
  }

  @Test
  public void testResolveViaProperties_NullType() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    JSType type1 = Mockito.mock(ObjectType.class);
    JSType type2 = Mockito.mock(JSType.class);
    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);

    Mockito.when(slot.getType()).thenReturn(type1);
    Mockito.when(type1.getPropertyType("propertyName")).thenReturn(type2);
    StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
    Mockito.when(staticScope.getSlot(REFERENCE_NAME)).thenReturn(slot);

    namedType.resolveInternal(errorReporter, staticScope);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties_Success() {
    JSType referencedType = Mockito.mock(ObjectType.class);
    JSType propertyType = Mockito.mock(JSType.class);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);
    Mockito.when(slot.getType()).thenReturn(Mockito.mock(FunctionType.class));
    Mockito.when(slot.getSlot("propertyName")).thenReturn(propertyType);
    StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
    Mockito.when(staticScope.getSlot(REFERENCE_NAME)).thenReturn(slot);

    Mockito.when(propertyType.isFunctionType()).thenReturn(true);
    Mockito.when(propertyType.isInterface()).thenReturn(false);
    Mockito.when(propertyType.getInstanceType()).thenReturn(referencedType);

    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties_NullType2() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    JSType type1 = Mockito.mock(ObjectType.class);
    JSType type2 = Mockito.mock(JSType.class);
    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);

    Mockito.when(slot.getType()).thenReturn(type1);
    Mockito.when(type1.getPropertyType("propertyName")).thenReturn(type2);
    StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
    Mockito.when(staticScope.getSlot(REFERENCE_NAME)).thenReturn(slot);

    Mockito.when(type1.isInterface()).thenReturn(false);
    Mockito.when(type2.isAllType()).thenReturn(true);

    namedType.resolveInternal(errorReporter, staticScope);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties_EnumType() {
    JSType referencedType = Mockito.mock(EnumType.class);
    EnumElementType enumElementType = Mockito.mock(EnumElementType.class);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);
    Mockito.when(slot.getType()).thenReturn(Mockito.mock(ObjectType.class));
    Mockito.when(slot.getSlot("propertyName")).thenReturn(enumElementType);
    StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
    Mockito.when(staticScope.getSlot(REFERENCE_NAME)).thenReturn(slot);

    Mockito.when(enumElementType.getPrimitiveType()).thenReturn(namedType);

    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties_FunctionType() {
    JSType referencedType = Mockito.mock(FunctionType.class);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);
    Mockito.when(slot.getType()).thenReturn(Mockito.mock(ObjectType.class));
    Mockito.when(slot.getSlot("propertyName")).thenReturn(referencedType);
    StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
    Mockito.when(staticScope.getSlot(REFERENCE_NAME)).thenReturn(slot);

    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties_NoObjectType() {
    JSType referencedType = Mockito.mock(JSTypeNative.class);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    StaticSlot<JSType> slot = Mockito.mock(StaticSlot.class);
    Mockito.when(slot.getType()).thenReturn(Mockito.mock(ObjectType.class));
    Mockito.when(slot.getSlot("propertyName")).thenReturn(referencedType);
    StaticScope<JSType> staticScope = Mockito.mock(StaticScope.class);
    Mockito.when(staticScope.getSlot(REFERENCE_NAME)).thenReturn(slot);

    Mockito.when(referencedType.isNoObjectType()).thenReturn(true);

    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testSetReferencedAndResolvedType() {
    JSType referencedType = Mockito.mock(FunctionType.class);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    namedType.setReferencedAndResolvedType(referencedType, errorReporter, scope);
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testHandleTypeCycle() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    JSType referencedType = Mockito.mock(NamedType.class);
    Mockito.when(referencedType.getReferencedType()).thenReturn(namedType);

    namedType.handleTypeCycle(errorReporter);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testCheckEnumElementCycle() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    EnumElementType enumElementType = Mockito.mock(EnumElementType.class);
    Mockito.when(enumElementType.getPrimitiveType()).thenReturn(namedType);

    namedType.checkEnumElementCycle(errorReporter);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testHandleUnresolvedType() {
    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    namedType.handleUnresolvedType(errorReporter, false);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());

    namedType.handleUnresolvedType(errorReporter, true);
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = Mockito.mock(Predicate.class);

    namedType = new NamedType(registry, REFERENCE_NAME, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);

    namedType.setValidator(validator);
    assertTrue(namedType.isValidatorSet());
  }
}