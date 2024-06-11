package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;
  private UnionType stringUnion;
  private UnionType booleanUnion;
  private UnionType numberUnion;
  private UnionType stringBooleanUnion;
  private UnionType nullUnion;
  private UnionType voidUnion;
  private UnionType objectUnion;
  private UnionType stringNumberUnion;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    stringUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN));
    booleanUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.BOOLEAN));
    numberUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.NUMBER));
    stringBooleanUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN));
    nullUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.NULL));
    voidUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.VOID));
    objectUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.OBJECT));
    stringNumberUnion = new UnionType(registry, ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.NUMBER));
  }

  @Test
  public void testGetAlternates() {
    assertEquals(ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN), stringUnion.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    assertTrue(numberUnion.matchesNumberContext());
    assertFalse(stringUnion.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    assertTrue(stringUnion.matchesStringContext());
    assertTrue(booleanUnion.matchesStringContext());
    assertTrue(numberUnion.matchesStringContext());
    assertFalse(nullUnion.matchesStringContext());
    assertFalse(voidUnion.matchesStringContext());
    assertFalse(objectUnion.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    assertTrue(objectUnion.matchesObjectContext());
    assertFalse(stringUnion.matchesObjectContext());
    assertFalse(booleanUnion.matchesObjectContext());
    assertFalse(numberUnion.matchesObjectContext());
    assertFalse(nullUnion.matchesObjectContext());
    assertFalse(voidUnion.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    JSType propertyType = stringUnion.findPropertyType("length");
    assertEquals(JSTypeNative.NUMBER, propertyType);

    propertyType = stringUnion.findPropertyType("toString");
    assertEquals(JSTypeNative.STRING, propertyType);

    propertyType = booleanUnion.findPropertyType("toString");
    assertNull(propertyType);
  }

  @Test
  public void testCanAssignTo() {
    assertTrue(stringUnion.canAssignTo(booleanUnion));
    assertTrue(booleanUnion.canAssignTo(stringUnion));
    assertTrue(numberUnion.canAssignTo(stringUnion));
    assertTrue(numberUnion.canAssignTo(booleanUnion));
    assertFalse(stringUnion.canAssignTo(numberUnion));
    assertFalse(stringUnion.canAssignTo(booleanUnion));
    assertFalse(booleanUnion.canAssignTo(numberUnion));
    assertFalse(numberUnion.canAssignTo(numberUnion));
    assertFalse(stringUnion.canAssignTo(nullUnion));
    assertFalse(booleanUnion.canAssignTo(nullUnion));
    assertFalse(numberUnion.canAssignTo(nullUnion));
    assertFalse(nullUnion.canAssignTo(stringUnion));
    assertFalse(nullUnion.canAssignTo(booleanUnion));
    assertFalse(nullUnion.canAssignTo(numberUnion));
    assertFalse(nullUnion.canAssignTo(nullUnion));
    assertFalse(voidUnion.canAssignTo(anyType()));
  }

  @Test
  public void testCanBeCalled() {
    assertTrue(booleanUnion.canBeCalled());
    assertTrue(stringUnion.canBeCalled());
    assertTrue(numberUnion.canBeCalled());
    assertFalse(nullUnion.canBeCalled());
    assertFalse(voidUnion.canBeCalled());
    assertFalse(objectUnion.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType restrictedStringUnion = stringUnion.restrictByNotNullOrUndefined();
    UnionType restrictedBooleanUnion = booleanUnion.restrictByNotNullOrUndefined();
    UnionType restrictedNumberUnion = numberUnion.restrictByNotNullOrUndefined();

    assertFalse(restrictedStringUnion.canAssignTo(booleanUnion));
    assertFalse(restrictedBooleanUnion.canAssignTo(stringUnion));
    assertFalse(restrictedNumberUnion.canAssignTo(stringUnion));
    assertFalse(restrictedNumberUnion.canAssignTo(booleanUnion));
    assertFalse(stringUnion.canAssignTo(restrictedStringUnion));
    assertFalse(booleanUnion.canAssignTo(restrictedBooleanUnion));
    assertFalse(numberUnion.canAssignTo(restrictedNumberUnion));

    // null and void are already restricted by definition
    assertEquals(nullUnion, restrictedNullUnion());
    assertEquals(voidUnion, restrictedVoidUnion());
  }

  @Test
  public void testTestForEquality() {
    TernaryValue result = stringUnion.testForEquality(booleanUnion);
    assertEquals(UnknownType.UNKNOWN, result);

    result = booleanUnion.testForEquality(stringUnion);
    assertEquals(UnknownType.UNKNOWN, result);

    result = stringUnion.testForEquality(stringBooleanUnion);
    assertEquals(TrueType.TRUE, result);

    result = stringBooleanUnion.testForEquality(stringUnion);
    assertEquals(TrueType.TRUE, result);

    result = stringBooleanUnion.testForEquality(booleanUnion);
    assertEquals(TrueType.TRUE, result);

    result = nullUnion.testForEquality(numberUnion);
    assertEquals(UnknownType.UNKNOWN, result);

    result = numberUnion.testForEquality(nullUnion);
    assertEquals(UnknownType.UNKNOWN, result);

    result = nullUnion.testForEquality(nullUnion);
    assertEquals(TrueType.TRUE, result);

    result = voidUnion.testForEquality(voidUnion);
    assertEquals(TrueType.TRUE, result);
  }

  @Test
  public void testIsNullable() {
    assertFalse(stringUnion.isNullable());
    assertFalse(booleanUnion.isNullable());
    assertFalse(numberUnion.isNullable());
    assertTrue(nullUnion.isNullable());
    assertFalse(voidUnion.isNullable());
    assertTrue(objectUnion.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    assertFalse(stringUnion.isUnknownType());
    assertFalse(booleanUnion.isUnknownType());
    assertFalse(numberUnion.isUnknownType());
    assertFalse(nullUnion.isUnknownType());
    assertFalse(voidUnion.isUnknownType());
    assertFalse(objectUnion.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType leastSupertype = stringUnion.getLeastSupertype(numberUnion);
    assertEquals(stringNumberUnion, leastSupertype);

    leastSupertype = booleanUnion.getLeastSupertype(stringUnion);
    assertEquals(stringBooleanUnion, leastSupertype);

    leastSupertype = numberUnion.getLeastSupertype(booleanUnion);
    assertEquals(stringNumberUnion, leastSupertype);

    leastSupertype = numberUnion.getLeastSupertype(numberUnion);
    assertEquals(numberUnion, leastSupertype);

    leastSupertype = nullUnion.getLeastSupertype(anyType());
    assertEquals(nullUnion, leastSupertype);

    leastSupertype = voidUnion.getLeastSupertype(anyType());
    assertEquals(voidUnion, leastSupertype);

    leastSupertype = objectUnion.getLeastSupertype(anyType());
    assertEquals(objectUnion, leastSupertype);

    leastSupertype = anyType().getLeastSupertype(voidUnion);
    assertEquals(voidUnion, leastSupertype);

    leastSupertype = anyType().getLeastSupertype(nullUnion);
    assertEquals(nullUnion, leastSupertype);

    leastSupertype = anyType().getLeastSupertype(anyType());
    assertEquals(anyType(), leastSupertype);
  }

  @Test
  public void testMeet() {
    UnionType result = stringUnion.meet(booleanUnion);
    assertEquals(stringBooleanUnion, result);

    result = booleanUnion.meet(stringUnion);
    assertEquals(stringBooleanUnion, result);

    result = nullUnion.meet(numberUnion);
    assertNull(result);

    result = numberUnion.meet(nullUnion);
    assertNull(result);

    result = voidUnion.meet(objectUnion);
    assertNull(result);

    result = objectUnion.meet(voidUnion);
    assertNull(result);

    result = anyType().meet(anyType());
    assertEquals(anyType(), result);
  }

  @Test
  public void testToString() {
    assertEquals("(String|Boolean)", stringUnion.toString());
    assertEquals("(Boolean)", booleanUnion.toString());
    assertEquals("(Number)", numberUnion.toString());
    assertEquals("(String|Boolean)", stringBooleanUnion.toString());
    assertEquals("(Null)", nullUnion.toString());
    assertEquals("(Void)", voidUnion.toString());
    assertEquals("(Object)", objectUnion.toString());
    assertEquals("(String|Number)", stringNumberUnion.toString());
  }

  @Test
  public void testIsSubtype() {
    assertTrue(stringUnion.isSubtype(stringBooleanUnion));
    assertTrue(stringBooleanUnion.isSubtype(stringUnion));
    assertTrue(booleanUnion.isSubtype(stringBooleanUnion));
    assertTrue(stringBooleanUnion.isSubtype(booleanUnion));
    assertTrue(numberUnion.isSubtype(stringNumberUnion));
    assertTrue(stringNumberUnion.isSubtype(numberUnion));
    assertTrue(nullUnion.isSubtype(anyType()));
    assertTrue(anyType().isSubtype(nullUnion));
    assertTrue(voidUnion.isSubtype(anyType()));
    assertTrue(anyType().isSubtype(voidUnion));
    assertTrue(objectUnion.isSubtype(anyType()));
    assertTrue(anyType().isSubtype(objectUnion));

    assertFalse(stringUnion.isSubtype(numberUnion));
    assertFalse(numberUnion.isSubtype(stringUnion));
    assertFalse(booleanUnion.isSubtype(numberUnion));
    assertFalse(numberUnion.isSubtype(booleanUnion));
    assertFalse(nullUnion.isSubtype(numberUnion));
    assertFalse(numberUnion.isSubtype(nullUnion));
    assertFalse(voidUnion.isSubtype(numberUnion));
    assertFalse(numberUnion.isSubtype(voidUnion));
    assertFalse(objectUnion.isSubtype(numberUnion));
    assertFalse(numberUnion.isSubtype(objectUnion));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType restrictedNumberUnion = stringNumberUnion.getRestrictedUnion(JSTypeNative.STRING);
    assertEquals(numberUnion, restrictedNumberUnion);

    restrictedNumberUnion = stringNumberUnion.getRestrictedUnion(JSTypeNative.NUMBER);
    assertEquals(numberUnion, restrictedNumberUnion);

    restrictedNumberUnion = stringNumberUnion.getRestrictedUnion(JSTypeNative.BOOLEAN);
    assertEquals(stringUnion, restrictedNumberUnion);
  }

  private UnionType restrictedNullUnion() {
    return new UnionType(registry, ImmutableSet.of(JSTypeNative.NULL));
  }

  private UnionType restrictedVoidUnion() {
    return new UnionType(registry, ImmutableSet.of(JSTypeNative.VOID));
  }

  private JSType anyType() {
    return JSTypeNative.ANY;
  }
}