package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionType() {
    // Test creating a union type from two types
    UnionType numberOrString = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.NUMBER, JSType.STRING));

    // Test getting alternates
    assertEquals(ImmutableSet.of(JSType.NUMBER, JSType.STRING),
        numberOrString.getAlternates());

    // Test matchesNumberContext
    assertFalse(numberOrString.matchesNumberContext());

    // Test matchesStringContext
    assertTrue(numberOrString.matchesStringContext());

    // Test matchesObjectContext
    assertTrue(numberOrString.matchesObjectContext());

    // Test findPropertyType
    assertNull(numberOrString.findPropertyType("toString"));

    // Test canAssignTo
    Set<JSType> assignableTypes = new HashSet<JSType>();
    assignableTypes.add(JSType.NUMBER);
    assignableTypes.add(JSType.STRING);
    assertTrue(numberOrString.canAssignTo(JSType.NUMBER));
    assertTrue(numberOrString.canAssignTo(JSType.STRING));
    assertFalse(numberOrString.canAssignTo(JSType.BOOLEAN));

    // Test canBeCalled
    assertFalse(numberOrString.canBeCalled());

    // Test restrictByNotNullOrUndefined
    UnionType numberOrStringNotNull = numberOrString.restrictByNotNullOrUndefined();
    assertEquals(numberOrString, numberOrStringNotNull);

    // Test testForEquality
    UnionType stringOrNumber = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.STRING, JSType.NUMBER));
    assertEquals(numberOrString, numberOrString);
    assertEquals(numberOrString, stringOrNumber);
    assertNotSame(numberOrString, JSType.NUMBER);
    assertNotSame(numberOrString, JSType.STRING);
    assertFalse(numberOrString.testForEquality(JSType.BOOLEAN));

    // Test isNullable
    assertFalse(numberOrString.isNullable());

    // Test isUnknownType
    assertFalse(numberOrString.isUnknownType());

    // Test getLeastSupertype
    UnionType numberOrStringOrObject = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.NUMBER, JSType.STRING, JSType.OBJECT));
    assertEquals(numberOrStringOrObject, numberOrString.getLeastSupertype(
        numberOrStringOrObject));

    // Test meet
    UnionType numberOrStringMeetBoolean = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.NUMBER, JSType.STRING, JSType.BOOLEAN));
    UnionType booleanOrNumberOrString = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.BOOLEAN, JSType.NUMBER, JSType.STRING));
    assertEquals(numberOrStringMeetBoolean, booleanOrNumberOrString.meet(
        numberOrStringMeetBoolean));

    // Test equals
    UnionType numberOrString2 = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.NUMBER, JSType.STRING));
    assertTrue(numberOrString.equals(numberOrString2));

    // Test hashCode
    UnionType numberOrString3 = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.NUMBER, JSType.STRING));
    assertEquals(numberOrString.hashCode(), numberOrString3.hashCode());

    // Test isUnionType
    assertTrue(numberOrString.isUnionType());

    // Test isObject
    assertTrue(numberOrString.isObject());

    // Test contains
    UnionType numberStringOrNumber = new UnionType(new JSTypeRegistry(),
        ImmutableSet.of(JSType.NUMBER, JSType.STRING, JSType.NUMBER));
    assertTrue(numberOrString.contains(JSType.NUMBER));
    assertTrue(numberOrString.contains(JSType.STRING));
    assertFalse(numberOrString.contains(JSType.BOOLEAN));

    // Test getRestrictedUnion
    UnionType numberOrStringRestrictedByNumber = numberOrString.getRestrictedUnion(
        JSType.NUMBER);
    assertEquals(JSType.STRING, numberOrStringRestrictedByNumber);

    // Test toString
    assertEquals("(Number|String)", numberOrString.toString());

    // Test isSubtype
    assertTrue(numberOrString.isSubtype(JSType.STRING));
    assertFalse(JSType.NUMBER.isSubtype(numberOrString));

    // Test getRestrictedTypeGivenToBooleanOutcome
    UnionType booleanOrNumberOrStringBooleanOutcomeTrue = new UnionType(
        new JSTypeRegistry(),
        ImmutableSet.of(JSType.BOOLEAN, JSType.NUMBER, JSType.STRING));
    UnionType booleanOrNumberOrStringBooleanOutcomeFalse = new UnionType(
        new JSTypeRegistry(),
        ImmutableSet.of(JSType.BOOLEAN, JSType.NUMBER, JSType.STRING));
    UnionType numberOrStringBooleanOutcomeTrue = numberOrString.getRestrictedTypeGivenToBooleanOutcome(
        true);
    UnionType numberOrStringBooleanOutcomeFalse = numberOrString.getRestrictedTypeGivenToBooleanOutcome(
        false);
    assertEquals(booleanOrNumberOrStringBooleanOutcomeTrue,
        booleanOrNumberOrStringBooleanOutcomeFalse);
    assertEquals(booleanOrNumberOrStringBooleanOutcomeTrue,
        booleanOrNumberOrStringBooleanOutcomeTrue);
    assertNotSame(booleanOrNumberOrStringBooleanOutcomeTrue,
        booleanOrNumberOrStringBooleanOutcomeFalse);

    // Test getPossibleToBooleanOutcomes
    BooleanLiteralSet numberOrStringPossibleToBooleanOutcomes = numberOrString.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, numberOrStringPossibleToBooleanOutcomes);

    // Test getTypesUnderEquality
    TypePair numberOrStringTypesUnderEqualityNumber = numberOrString.getTypesUnderEquality(
        JSType.NUMBER);
    TypePair numberOrStringTypesUnderEqualityString = numberOrString.getTypesUnderEquality(JSType.STRING);
    assertEquals(numberOrStringTypesUnderEqualityNumber,
        new TypePair(JSType.NUMBER, numberOrString));
    assertEquals(numberOrStringTypesUnderEqualityString,
        new TypePair(JSType.STRING, numberOrString));

    // Test getTypesUnderInequality
    TypePair numberOrStringTypesUnderInequalityNumber = numberOrString.getTypesUnderInequality(
        JSType.NUMBER);
    TypePair numberOrStringTypesUnderInequalityString = numberOrString.getTypesUnderInequality(JSType.STRING);
    assertEquals(numberOrStringTypesUnderInequalityNumber,
        new TypePair(JSType.NUMBER, JSType.BOOLEAN));
    assertEquals(numberOrStringTypesUnderInequalityString,
        new TypePair(JSType.STRING, JSType.BOOLEAN));

    // Test getTypesUnderShallowInequality
    TypePair numberOrStringTypesUnderShallowInequalityNumber = numberOrString.getTypesUnderShallowInequality(
        JSType.NUMBER);
    TypePair numberOrStringTypesUnderShallowInequalityString = numberOrString.getTypesUnderShallowInequality(JSType.STRING);
    assertEquals(numberOrStringTypesUnderShallowInequalityNumber,
        new TypePair(JSType.NUMBER, JSType.NUMBER));
    assertEquals(numberOrStringTypesUnderShallowInequalityString,
        new TypePair(JSType.STRING, JSType.STRING));
  }
}