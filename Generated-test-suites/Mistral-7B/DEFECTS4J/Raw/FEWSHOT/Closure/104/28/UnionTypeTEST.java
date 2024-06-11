package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.UnionType;
import com.google.javascript.rhino.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.TernaryValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Tests for {@link UnionType}.
 */
public class UnionTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;
  private UnionType typeA;
  private UnionType typeB;
  private UnionType typeC;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = mock(ErrorReporter.class);
    scope = new StaticScope<>(registry);
    typeA = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    typeB = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    typeC = new UnionType(registry, ImmutableSet.of(registry.getObjectType(), registry.getNullType()));
  }

  @Test
  public void testGetAlternates() {
    assertEquals(ImmutableSet.of(registry.getStringType(), registry.getBooleanType()), typeA.getAlternates());
    assertEquals(ImmutableSet.of(registry.getNumberType(), registry.getStringType()), typeB.getAlternates());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType typeD = new UnionType(registry, ImmutableSet.of(registry.getBooleanType(), registry.getStringType()));
    JSType typeE = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    assertEquals(typeA, typeA.getLeastSupertype(typeA));
    assertEquals(typeB, typeB.getLeastSupertype(typeB));
    assertEquals(typeA, typeA.getLeastSupertype(typeB));
    assertEquals(typeB, typeB.getLeastSupertype(typeA));
    assertEquals(typeD, typeD.getLeastSupertype(typeE));
    assertEquals(typeE, typeE.getLeastSupertype(typeD));
  }

  @Test
  public void testCanAssignTo() {
    JSType typeX = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getBooleanType()));

    assertTrue(typeA.canAssignTo(typeA));
    assertTrue(typeA.canAssignTo(typeB));
    assertTrue(typeB.canAssignTo(typeA));
    assertTrue(typeB.canAssignTo(typeB));
    assertFalse(typeA.canAssignTo(typeX));
    assertFalse(typeB.canAssignTo(typeX));
    assertFalse(typeX.canAssignTo(typeA));
    assertFalse(typeX.canAssignTo(typeB));
  }

  @Test
  public void testCanBeCalled() {
    assertTrue(typeA.canBeCalled());
    assertTrue(typeB.canBeCalled());
    assertFalse(typeC.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType typeANull = typeA.restrictByNotNullOrUndefined();
    JSType typeBNull = typeB.restrictByNotNullOrUndefined();
    JSType typeCNull = typeC.restrictByNotNullOrUndefined();

    assertEquals(registry.getNullType(), typeANull);
    assertEquals(registry.getNullType(), typeBNull);
    assertNull(typeCNull);
  }

  @Test
  public void testTestForEquality() {
    UnionType typeF = new UnionType(registry, ImmutableSet.of(registry.getBooleanType(), registry.getStringType()));
    UnionType typeG = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    assertEquals(typeA, typeA);
    assertEquals(typeA, typeA);
    assertEquals(typeA, typeB);
    assertEquals(typeB, typeA);
    assertNotEquals(typeA, typeC);
    assertNotEquals(typeC, typeA);
    assertEquals(typeA, typeF);
    assertEquals(typeF, typeA);
    assertEquals(typeB, typeG);
    assertEquals(typeG, typeB);
    assertNotEquals(typeA, typeG);
    assertNotEquals(typeG, typeA);
    assertNotEquals(typeA, typeF);
    assertNotEquals(typeF, typeA);
  }

  @Test
  public void testIsNullable() {
    assertFalse(typeA.isNullable());
    assertFalse(typeB.isNullable());
    assertTrue(typeC.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    assertFalse(typeA.isUnknownType());
    assertFalse(typeB.isUnknownType());
    assertFalse(typeC.isUnknownType());
  }

  @Test
  public void testIsSubtype() {
    assertTrue(typeA.isSubtype(typeA));
    assertTrue(typeB.isSubtype(typeB));
    assertTrue(typeA.isSubtype(typeB));
    assertTrue(typeB.isSubtype(typeA));
    assertFalse(typeA.isSubtype(typeC));
    assertFalse(typeB.isSubtype(typeC));
    assertFalse(typeC.isSubtype(typeA));
    assertFalse(typeC.isSubtype(typeB));
  }

  @Test
  public void testToString() {
    assertEquals("(string, boolean)", typeA.toString());
    assertEquals("(number, string)", typeB.toString());
    assertEquals("(object, null)", typeC.toString());
  }

  @Test
  public void testEquals() {
    UnionType typeH = new UnionType(registry, ImmutableSet.of(registry.getBooleanType(), registry.getStringType()));

    assertTrue(typeA.equals(typeA));
    assertTrue(typeA.equals(typeA));
    assertTrue(typeA.equals(typeB));
    assertTrue(typeB.equals(typeA));
    assertFalse(typeA.equals(typeC));
    assertFalse(typeC.equals(typeA));
    assertTrue(typeA.equals(typeH));
    assertTrue(typeH.equals(typeA));
    assertFalse(typeA.equals(null));
    assertFalse(typeA.equals(new Object()));
  }

  @Test
  public void testHashCode() {
    UnionType typeI = new UnionType(registry, ImmutableSet.of(registry.getBooleanType(), registry.getStringType()));

    assertEquals(typeA.hashCode(), typeA.hashCode());
    assertEquals(typeA.hashCode(), typeA.hashCode());
    assertEquals(typeA.hashCode(), typeB.hashCode());
    assertEquals(typeB.hashCode(), typeA.hashCode());
    assertNotEquals(typeA.hashCode(), typeC.hashCode());
    assertNotEquals(typeC.hashCode(), typeA.hashCode());
    assertEquals(typeA.hashCode(), typeI.hashCode());
    assertEquals(typeI.hashCode(), typeA.hashCode());
  }

  @Test
  public void testIsUnionType() {
    assertTrue(typeA.isUnionType());
    assertTrue(typeB.isUnionType());
    assertFalse(registry.getStringType().isUnionType());
  }

  @Test
  public void testIsObject() {
    assertTrue(typeA.isObject());
    assertTrue(typeB.isObject());
    assertTrue(typeC.isObject());
  }

  @Test
  public void testContains() {
    assertTrue(typeA.contains(registry.getStringType()));
    assertTrue(typeA.contains(registry.getBooleanType()));
    assertFalse(typeA.contains(registry.getNumberType()));
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType typeJ = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    JSType typeK = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType(), registry.getNumberType()));

    assertEquals(typeA, typeA.getRestrictedUnion(registry.getStringType()));
    assertEquals(typeB, typeA.getRestrictedUnion(registry.getBooleanType()));
    assertEquals(registry.getStringType(), typeA.getRestrictedUnion(registry.getNumberType()));
    assertEquals(typeJ, typeA.getRestrictedUnion(registry.getNumberType()));
    assertEquals(typeJ, typeJ.getRestrictedUnion(registry.getNumberType()));
    assertEquals(registry.getBooleanType(), typeB.getRestrictedUnion(registry.getNumberType()));
    assertEquals(registry.getStringType(), typeK.getRestrictedUnion(registry.getNumberType()));
  }

  @Test
  public void testGetResolvedTypeInternal() {
    JSType typeL = new UnionType(registry, ImmutableSet.of(registry.getObjectType(), registry.getNullType()));

    when(scope.get("typeL")).thenReturn(typeL);
    typeA.resolveInternal(errorReporter, scope);

    ArgumentCaptor<JSType> captor = ArgumentCaptor.forClass(JSType.class);
    verify(scope).put(captor.capture(), eq(typeA));
    assertEquals(typeA, captor.getValue());
  }

  @Test
  public void testMeet() {
    UnionType typeM = new UnionType(registry, ImmutableSet.of(registry.getBooleanType(), registry.getStringType()));
    UnionType typeN = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    UnionType result = typeA.meet(typeB);
    assertEquals(typeA, result);

    result = typeB.meet(typeA);
    assertEquals(typeB, result);

    result = typeA.meet(typeC);
    assertNull(result);

    result = typeC.meet(typeA);
    assertNull(result);

    result = typeA.meet(typeM);
    assertEquals(typeA, result);

    result = typeM.meet(typeA);
    assertEquals(typeA, result);

    result = typeB.meet(typeN);
    assertEquals(typeB, result);

    result = typeN.meet(typeB);
    assertEquals(typeB, result);

    result = typeB.meet(typeM);
    assertEquals(typeB, result);

    result = typeM.meet(typeB);
    assertEquals(typeB, result);

    result = typeB.meet(typeN);
    assertEquals(typeB, result);

    result = typeN.meet(typeB);
    assertEquals(typeB, result);
  }
}