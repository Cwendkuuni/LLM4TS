package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testGetAlternates() {
    UnionType type = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertEquals(ImmutableSet.of(registry.getStringType(), registry.getBooleanType()), type.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType type = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertFalse(type.matchesNumberContext());

    UnionType numberType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    assertTrue(numberType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType type = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(type.matchesStringContext());

    UnionType numberType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    assertFalse(numberType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType type = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(type.matchesObjectContext());

    UnionType voidType = new UnionType(registry, ImmutableSet.of(JSTypeNative.VOID_TYPE));
    assertFalse(voidType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertEquals(registry.getStringType(), stringBooleanType.findPropertyType("toString"));
    assertEquals(registry.getBooleanType(), stringBooleanType.findPropertyType("length"));
    assertNull(stringBooleanType.findPropertyType("toUpperCase"));
  }

  @Test
  public void testCanAssignTo() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType numberType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    assertTrue(stringBooleanType.canAssignTo(registry.getStringType()));
    assertTrue(stringBooleanType.canAssignTo(registry.getBooleanType()));
    assertFalse(stringBooleanType.canAssignTo(registry.getNumberType()));
    assertTrue(numberType.canAssignTo(registry.getNumberType()));
    assertTrue(numberType.canAssignTo(registry.getStringType()));
    assertFalse(numberType.canAssignTo(registry.getBooleanType()));
  }

  @Test
  public void testCanBeCalled() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(stringBooleanType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType nullableNumberBooleanType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getBooleanType(), registry.getNullType()));
    UnionType restrictedType = nullableNumberBooleanType.restrictByNotNullOrUndefined();
    assertTrue(restrictedType.isNullable());
    assertFalse(restrictedType.isUnknownType());
    assertFalse(restrictedType.contains(registry.getNullType()));
  }

  @Test
  public void testTestForEquality() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType anotherStringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertEquals(stringBooleanType, anotherStringBooleanType);

    UnionType numberStringType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    UnionType anotherNumberStringType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    assertEquals(numberStringType, anotherNumberStringType);

    UnionType stringType = new UnionType(registry, ImmutableSet.of(registry.getStringType()));
    UnionType numberType = new UnionType(registry, ImmutableSet.of(registry.getNumberType()));
    assertNotEquals(stringType, numberType);
  }

  @Test
  public void testIsNullable() {
    UnionType nullableNumberBooleanType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getBooleanType(), registry.getNullType()));
    assertTrue(nullableNumberBooleanType.isNullable());

    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertFalse(stringBooleanType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertFalse(stringBooleanType.isUnknownType());

    UnionType nullableNumberBooleanType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getBooleanType(), registry.getNullType()));
    assertFalse(nullableNumberBooleanType.isUnknownType());

    UnionType unknownType = new UnionType(registry, ImmutableSet.of(registry.getUnknownType()));
    assertTrue(unknownType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType numberStringType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    UnionType objectType = new UnionType(registry, ImmutableSet.of(registry.getObjectType()));

    assertEquals(objectType, stringBooleanType.getLeastSupertype(objectType));
    assertEquals(numberStringType, stringBooleanType.getLeastSupertype(numberStringType));
    assertEquals(numberStringType, registry.getObjectType().getLeastSupertype(numberStringType));
    assertEquals(registry.getObjectType(), stringBooleanType.getLeastSupertype(registry.getObjectType()));
    assertNull(stringBooleanType.getLeastSupertype(registry.getNumberType()));
  }

  @Test
  public void testMeet() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType numberStringType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    UnionType numberBooleanType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getBooleanType()));

    UnionType result = stringBooleanType.meet(numberStringType);
    assertEquals(numberStringType, result);

    result = numberStringType.meet(numberBooleanType);
    assertEquals(numberBooleanType, result);

    result = numberBooleanType.meet(stringBooleanType);
    assertEquals(numberBooleanType, result);

    result = stringBooleanType.meet(registry.getObjectType());
    assertEquals(registry.getObjectType(), result);

    result = registry.getObjectType().meet(numberBooleanType);
    assertEquals(registry.getObjectType(), result);

    result = registry.getObjectType().meet(registry.getObjectType());
    assertEquals(registry.getObjectType(), result);
  }

  @Test
  public void testEquals() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType anotherStringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(stringBooleanType.equals(anotherStringBooleanType));

    UnionType numberStringType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    assertNotEquals(stringBooleanType, numberStringType);
  }

  @Test
  public void testHashCode() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType anotherStringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertEquals(stringBooleanType.hashCode(), anotherStringBooleanType.hashCode());

    UnionType numberStringType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getStringType()));
    assertNotEquals(stringBooleanType.hashCode(), numberStringType.hashCode());
  }

  @Test
  public void testIsUnionType() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(stringBooleanType.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(stringBooleanType.isObject());
  }

  @Test
  public void testContains() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType stringType = registry.getStringType();
    UnionType booleanType = registry.getBooleanType();

    assertTrue(stringBooleanType.contains(stringType));
    assertTrue(stringBooleanType.contains(booleanType));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType nullableNumberBooleanType = new UnionType(registry, ImmutableSet.of(registry.getNumberType(), registry.getBooleanType(), registry.getNullType()));
    UnionType restrictedType = nullableNumberBooleanType.getRestrictedUnion(registry.getNumberType());
    assertEquals(registry.getBooleanType(), restrictedType);
  }

  @Test
  public void testToString() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertEquals("(String|Boolean)", stringBooleanType.toString());
  }

  @Test
  public void testIsSubtype() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType stringType = registry.getStringType();
    UnionType numberType = new UnionType(registry, ImmutableSet.of(registry.getNumberType()));

    assertTrue(stringBooleanType.isSubtype(stringType));
    assertTrue(stringBooleanType.isSubtype(registry.getBooleanType()));
    assertFalse(stringBooleanType.isSubtype(numberType));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    assertTrue(stringBooleanType.getRestrictedTypeGivenToBooleanOutcome(true).isBooleanType());
    assertTrue(stringBooleanType.getRestrictedTypeGivenToBooleanOutcome(false).isStringType());
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    BooleanLiteralSet literals = stringBooleanType.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, literals);
  }

  @Test
  public void testGetTypesUnderEquality() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType anotherStringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    TypePair result = stringBooleanType.getTypesUnderEquality(anotherStringBooleanType);
    assertEquals(registry.getObjectType(), result.typeA);
    assertEquals(registry.getObjectType(), result.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType anotherStringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    TypePair result = stringBooleanType.getTypesUnderInequality(anotherStringBooleanType);
    assertEquals(registry.getObjectType(), result.typeA);
    assertEquals(registry.getObjectType(), result.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    UnionType stringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));
    UnionType anotherStringBooleanType = new UnionType(registry, ImmutableSet.of(registry.getStringType(), registry.getBooleanType()));

    TypePair result = stringBooleanType.getTypesUnderShallowInequality(anotherStringBooleanType);
    assertEquals(registry.getObjectType(), result.typeA);
    assertEquals(registry.getObjectType(), result.typeB);
  }
}