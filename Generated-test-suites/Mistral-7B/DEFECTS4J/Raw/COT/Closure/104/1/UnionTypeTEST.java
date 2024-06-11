package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> stringSet = ImmutableSet.of(JSTypeRegistry.STRING);
    UnionType stringUnion = new UnionType(JSTypeRegistry.instance(), stringSet);

    Set<JSType> booleanSet = ImmutableSet.of(JSTypeRegistry.BOOLEAN);
    UnionType booleanUnion = new UnionType(JSTypeRegistry.instance(), booleanSet);

    Set<JSType> stringBooleanSet = ImmutableSet.of(JSTypeRegistry.STRING, JSTypeRegistry.BOOLEAN);
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.instance(), stringBooleanSet);

    assertEquals(stringSet, stringUnion.getAlternates());
    assertEquals(booleanSet, booleanUnion.getAlternates());
    assertEquals(stringBooleanSet, stringBooleanUnion.getAlternates());
  }

  @Test
  public void testMatchesContext() {
    Set<JSType> numberSet = ImmutableSet.of(JSTypeRegistry.NUMBER);
    UnionType numberUnion = new UnionType(JSTypeRegistry.instance(), numberSet);

    assertFalse(numberUnion.matchesNumberContext());

    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    assertTrue(numberStringUnion.matchesNumberContext());
  }

  @Test
  public void testFindPropertyType() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    JSType propertyType = numberStringUnion.findPropertyType("toString");
    assertNotNull(propertyType);
    assertTrue(propertyType instanceof JSTypeNative);
    assertEquals(JSTypeRegistry.STRING, propertyType);
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> numberSet = ImmutableSet.of(JSTypeRegistry.NUMBER);
    UnionType numberUnion = new UnionType(JSTypeRegistry.instance(), numberSet);

    Set<JSType> numberBooleanSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.BOOLEAN);
    UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.instance(), numberBooleanSet);

    JSType number = JSTypeRegistry.NUMBER;
    JSType booleanType = JSTypeRegistry.BOOLEAN;

    assertTrue(numberUnion.canAssignTo(number));
    assertTrue(numberBooleanUnion.canAssignTo(number));

    assertFalse(numberBooleanUnion.canAssignTo(booleanType));
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> numberSet = ImmutableSet.of(JSTypeRegistry.NUMBER);
    UnionType numberUnion = new UnionType(JSTypeRegistry.instance(), numberSet);

    UnionType numberNullUnion = numberUnion.restrictByNotNullOrUndefined();

    Set<JSType> expectedAlternates = new HashSet<>();
    expectedAlternates.add(JSTypeRegistry.NULL);
    expectedAlternates.add(JSTypeRegistry.UNDEFINED);

    assertEquals(expectedAlternates, numberNullUnion.getAlternates());
  }

  @Test
  public void testTestForEquality() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    Set<JSType> stringNumberSet = ImmutableSet.of(JSTypeRegistry.STRING, JSTypeRegistry.NUMBER);
    UnionType stringNumberUnion = new UnionType(JSTypeRegistry.instance(), stringNumberSet);

    TernaryValue result = numberStringUnion.testForEquality(stringNumberUnion);
    assertEquals(TernaryValue.TRUE, result);

    result = numberStringUnion.testForEquality(numberStringUnion);
    assertEquals(TernaryValue.TRUE, result);

    result = numberStringUnion.testForEquality(JSTypeRegistry.NUMBER);
    assertEquals(TernaryValue.MAYBE, result);

    result = numberStringUnion.testForEquality(JSTypeRegistry.STRING);
    assertEquals(TernaryValue.MAYBE, result);

    result = numberStringUnion.testForEquality(JSTypeRegistry.BOOLEAN);
    assertEquals(TernaryValue.FALSE, result);
  }

  @Test
  public void testIsNullable() {
    Set<JSType> nullUnionSet = ImmutableSet.of(JSTypeRegistry.NULL);
    UnionType nullUnion = new UnionType(JSTypeRegistry.instance(), nullUnionSet);

    Set<JSType> numberUnionSet = ImmutableSet.of(JSTypeRegistry.NUMBER);
    UnionType numberUnion = new UnionType(JSTypeRegistry.instance(), numberUnionSet);

    assertTrue(nullUnion.isNullable());
    assertFalse(numberUnion.isNullable());
  }

  @Test
  public void testGetLeastSupertype() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    Set<JSType> stringNumberSet = ImmutableSet.of(JSTypeRegistry.STRING, JSTypeRegistry.NUMBER);
    UnionType stringNumberUnion = new UnionType(JSTypeRegistry.instance(), stringNumberSet);

    JSType leastSupertype = numberStringUnion.getLeastSupertype(stringNumberUnion);
    assertEquals(JSTypeRegistry.OBJECT, leastSupertype);

    leastSupertype = numberStringUnion.getLeastSupertype(JSTypeRegistry.NUMBER);
    assertEquals(JSTypeRegistry.NUMBER, leastSupertype);

    leastSupertype = numberStringUnion.getLeastSupertype(JSTypeRegistry.STRING);
    assertEquals(JSTypeRegistry.STRING, leastSupertype);

    leastSupertype = numberStringUnion.getLeastSupertype(JSTypeRegistry.BOOLEAN);
    assertNull(leastSupertype);
  }

  @Test
  public void testMeet() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    Set<JSType> stringNumberSet = ImmutableSet.of(JSTypeRegistry.STRING, JSTypeRegistry.NUMBER);
    UnionType stringNumberUnion = new UnionType(JSTypeRegistry.instance(), stringNumberSet);

    UnionType result = numberStringUnion.meet(stringNumberUnion);
    assertEquals(numberStringUnion, result);

    result = numberStringUnion.meet(JSTypeRegistry.NUMBER);
    assertEquals(numberStringUnion, result);

    result = numberStringUnion.meet(JSTypeRegistry.STRING);
    assertEquals(numberStringUnion, result);

    result = numberStringUnion.meet(JSTypeRegistry.BOOLEAN);
    assertNull(result);
  }

  @Test
  public void testToString() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    String expectedResult = "(|Number|String)";
    assertEquals(expectedResult, numberStringUnion.toString());
  }

  @Test
  public void testIsSubtype() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    Set<JSType> stringNumberSet = ImmutableSet.of(JSTypeRegistry.STRING, JSTypeRegistry.NUMBER);
    UnionType stringNumberUnion = new UnionType(JSTypeRegistry.instance(), stringNumberSet);

    assertTrue(numberStringUnion.isSubtype(stringNumberUnion));
    assertTrue(stringNumberUnion.isSubtype(numberStringUnion));

    Set<JSType> numberBooleanSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.BOOLEAN);
    UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.instance(), numberBooleanSet);

    assertFalse(numberStringUnion.isSubtype(numberBooleanUnion));
    assertFalse(numberBooleanUnion.isSubtype(numberStringUnion));

    assertFalse(numberStringUnion.isSubtype(JSTypeRegistry.NUMBER));
    assertFalse(numberStringUnion.isSubtype(JSTypeRegistry.STRING));
    assertFalse(numberStringUnion.isSubtype(JSTypeRegistry.BOOLEAN));
  }

  @Test
  public void testGetRestrictedUnion() {
    Set<JSType> numberStringSet = ImmutableSet.of(JSTypeRegistry.NUMBER, JSTypeRegistry.STRING);
    UnionType numberStringUnion = new UnionType(JSTypeRegistry.instance(), numberStringSet);

    JSType number = JSTypeRegistry.NUMBER;
    JSType string = JSTypeRegistry.STRING;

    UnionType restrictedNumberUnion = numberStringUnion.getRestrictedUnion(number);
    assertEquals(ImmutableSet.of(JSTypeRegistry.NUMBER), restrictedNumberUnion.getAlternates());

    UnionType restrictedStringUnion = numberStringUnion.getRestrictedUnion(string);
    assertEquals(ImmutableSet.of(JSTypeRegistry.STRING), restrictedStringUnion.getAlternates());
  }
}