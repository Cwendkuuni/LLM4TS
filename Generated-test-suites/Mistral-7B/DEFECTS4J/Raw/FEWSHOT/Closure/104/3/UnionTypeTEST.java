package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.TernaryValue.UNKNOWN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.UnionType;
import com.google.javascript.rhino.UnionTypeBuilder;
import com.google.javascript.rhino.Visitor;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
    scope = new StaticScope<>(registry, errorReporter);
  }

  @Test
  public void testEquals() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeC = registry.newNullType();

    assertTrue(typeA.equals(typeB));
    assertFalse(typeA.equals(typeC));
  }

  @Test
  public void testHashCode() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeC = registry.newNullType();

    int hashCodeA = typeA.hashCode();
    int hashCodeB = typeB.hashCode();
    int hashCodeC = typeC.hashCode();

    assertTrue(hashCodeA == hashCodeB);
    assertFalse(hashCodeA == hashCodeC);
  }

  @Test
  public void testIsUnionType() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.getStringType();

    assertTrue(typeA.isUnionType());
    assertFalse(typeB.isUnionType());
  }

  @Test
  public void testIsObject() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));

    assertTrue(typeA.isObject());
  }

  @Test
  public void testGetAlternates() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));

    ImmutableSet<JSType> alternates = typeA.getAlternates();
    assertTrue(alternates.size() == 2);
    assertTrue(alternates.contains(registry.getStringType()));
    assertTrue(alternates.contains(registry.getNumberType()));
  }

  @Test
  public void testGetLeastSupertype() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newStringType();

    JSType leastSupertypeA = typeA.getLeastSupertype(typeB);
    assertEquals(typeC, leastSupertypeA);

    JSType leastSupertypeB = typeB.getLeastSupertype(typeA);
    assertEquals(typeC, leastSupertypeB);

    JSType leastSupertypeSelf = typeA.getLeastSupertype(typeA);
    assertEquals(typeA, leastSupertypeSelf);
  }

  @Test
  public void testMeet() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    UnionType result = (UnionType) typeA.meet(typeB);
    assertEquals(typeC, result);

    result = (UnionType) typeB.meet(typeA);
    assertEquals(typeC, result);

    result = (UnionType) typeA.meet(typeC);
    assertEquals(typeC, result);

    result = (UnionType) typeB.meet(typeC);
    assertEquals(typeC, result);
  }

  @Test
  public void testCanAssignTo() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newStringType();
    JSType typeD = registry.newBooleanType();

    assertTrue(typeA.canAssignTo(typeB));
    assertTrue(typeB.canAssignTo(typeA));
    assertFalse(typeA.canAssignTo(typeC));
    assertFalse(typeA.canAssignTo(typeD));
  }

  @Test
  public void testCanBeCalled() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newNullType();

    assertTrue(typeA.canBeCalled());
    assertFalse(typeB.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getNullType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getBooleanType(), registry.getNumberType()));

    UnionType result = (UnionType) typeA.restrictByNotNullOrUndefined();
    assertTrue(result.contains(registry.getNumberType()));
    assertFalse(result.contains(registry.getNullType()));

    result = (UnionType) typeB.restrictByNotNullOrUndefined();
    assertTrue(result.contains(registry.getBooleanType()));
    assertTrue(result.contains(registry.getNumberType()));
    assertFalse(result.contains(registry.getNullType()));
  }

  @Test
  public void testToString() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));

    assertEquals("(String|Number)", typeA.toString());
  }

  @Test
  public void testGetTypesUnderEquality() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newBooleanType();

    TypePair result = typeA.getTypesUnderEquality(typeB);
    assertEquals(typeC, result.typeA);
    assertEquals(typeC, result.typeB);

    result = typeB.getTypesUnderEquality(typeA);
    assertEquals(typeC, result.typeA);
    assertEquals(typeC, result.typeB);

    result = typeA.getTypesUnderEquality(typeC);
    assertNull(result.typeA);
    assertNull(result.typeB);

    result = typeB.getTypesUnderEquality(typeC);
    assertNull(result.typeA);
    assertNull(result.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newBooleanType();

    TypePair result = typeA.getTypesUnderInequality(typeB);
    assertNull(result.typeA);
    assertEquals(typeC, result.typeB);

    result = typeB.getTypesUnderInequality(typeA);
    assertNull(result.typeA);
    assertEquals(typeC, result.typeB);

    result = typeA.getTypesUnderInequality(typeC);
    assertNull(result.typeA);
    assertNull(result.typeB);

    result = typeB.getTypesUnderInequality(typeC);
    assertNull(result.typeA);
    assertNull(result.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newBooleanType();

    TypePair result = typeA.getTypesUnderShallowInequality(typeB);
    assertNull(result.typeA);
    assertEquals(typeC, result.typeB);

    result = typeB.getTypesUnderShallowInequality(typeA);
    assertNull(result.typeA);
    assertEquals(typeC, result.typeB);

    result = typeA.getTypesUnderShallowInequality(typeC);
    assertNull(result.typeA);
    assertNull(result.typeB);

    result = typeB.getTypesUnderShallowInequality(typeC);
    assertNull(result.typeA);
    assertNull(result.typeB);
  }

  @Test
  public void testToStringWithCircularDependencies() {
    HashSet<JSType> circularTypes = new HashSet<>();
    circularTypes.add(registry.newUnionType(ImmutableSet.of(registry.newUnionType(ImmutableSet.of(registry.getStringType(), circularTypes.add(registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()))))))));

    for (JSType type : circularTypes) {
      assertEquals("Circular dependency", type.toString());
    }
  }

  @Test
  public void testToStringWithCycleDetected() {
    JSType typeA = registry.newUnionType(ImmutableSet.of(registry.getStringType(), registry.getNumberType()));
    JSType typeB = registry.newUnionType(ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    JSType typeC = registry.newUnionType(ImmutableSet.of(registry.getStringType(), typeB));

    UnionType result = (UnionType) typeA.getLeastSupertype(typeC);
    assertEquals(typeB, result);

    assertEquals("(String|Number|String|...|Number)", typeC.toString());
  }
}