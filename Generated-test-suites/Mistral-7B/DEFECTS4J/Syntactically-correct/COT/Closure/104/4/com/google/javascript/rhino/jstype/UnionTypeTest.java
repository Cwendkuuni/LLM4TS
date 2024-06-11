package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {}; // An empty error reporter for testing
    typeRegistry = new JSTypeRegistry();
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> set1 = ImmutableSet.of(typeRegistry.stringType(), typeRegistry.booleanType());
    UnionType unionType1 = new UnionType(typeRegistry, set1);

    Set<JSType> set2 = ImmutableSet.of(typeRegistry.booleanType(), typeRegistry.numberType());
    UnionType unionType2 = new UnionType(typeRegistry, set2);

    assertEquals(unionType1, unionType1);
    assertEquals(unionType1.hashCode(), unionType1.hashCode());

    Set<JSType> set3 = ImmutableSet.of(typeRegistry.stringType(), typeRegistry.numberType());
    UnionType unionType3 = new UnionType(typeRegistry, set3);

    assertFalse(unionType1.equals(unionType2));
    assertFalse(unionType1.equals(unionType3));

    assertNotNull(unionType1.getAlternates());
    assertEquals(2, unionType1.getAlternates().size());

    assertEquals(unionType1, unionType1.meet(unionType1));
    assertEquals(unionType1, unionType1.meet(unionType2));
    assertEquals(unionType2, unionType1.meet(unionType2));
  }

  @Test
  public void testUnionTypeMatchesContexts() {
    UnionType stringBooleanUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.booleanType()));
    UnionType booleanNumberUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.booleanType(), typeRegistry.numberType()));

    assertTrue(stringBooleanUnion.matchesNumberContext());
    assertTrue(booleanNumberUnion.matchesNumberContext());

    assertTrue(stringBooleanUnion.matchesStringContext());
    assertTrue(booleanNumberUnion.matchesStringContext());

    assertTrue(stringBooleanUnion.matchesObjectContext());
    assertTrue(booleanNumberUnion.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.numberType()));

    String propertyName = "length";

    JSType stringPropertyType = typeRegistry.stringType();
    JSType numberPropertyType = typeRegistry.numberType();

    assertEquals(stringPropertyType, unionType.findPropertyType(propertyName));

    propertyName = "toExponential";
    assertNull(unionType.findPropertyType(propertyName));
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    UnionType stringBooleanUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.booleanType()));
    UnionType booleanStringUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.booleanType(), typeRegistry.stringType()));

    JSType stringType = typeRegistry.stringType();
    JSType booleanType = typeRegistry.booleanType();
    JSType numberType = typeRegistry.numberType();

    assertTrue(stringBooleanUnion.canAssignTo(stringType));
    assertTrue(stringBooleanUnion.canAssignTo(booleanType));
    assertTrue(stringBooleanUnion.canAssignTo(numberType));

    assertFalse(booleanStringUnion.canAssignTo(stringType));
    assertTrue(booleanStringUnion.canAssignTo(booleanType));
    assertTrue(booleanStringUnion.canAssignTo(numberType));

    assertFalse(stringBooleanUnion.canAssignTo(booleanStringUnion));
    assertFalse(booleanStringUnion.canAssignTo(stringBooleanUnion));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    UnionType anyFunctionType = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.objectFunctionType(), typeRegistry.voidFunctionType()));

    assertFalse(anyFunctionType.canBeCalled());
  }

  @Test(expected = AssertionError.class)
  public void testUnionTypeNegativeTestCanBeCalled() {
    UnionType booleanType = typeRegistry.booleanType();

    booleanType.canBeCalled();
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.numberType()));

    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();

    assertEquals(ImmutableSet.of(), restrictedUnionType.getAlternates());
  }

  @Test
  public void testUnionTypeTestForEquality() {
    UnionType stringBooleanUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.booleanType()));
    UnionType booleanStringUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.booleanType(), typeRegistry.stringType()));

    TernaryValue stringBooleanEquality = stringBooleanUnion.testForEquality(booleanStringUnion);
    TernaryValue booleanStringEquality = booleanStringUnion.testForEquality(stringBooleanUnion);

    assertEquals(UNKNOWN, stringBooleanEquality);
    assertEquals(UNKNOWN, booleanStringEquality);

    assertEquals(typeRegistry.stringType(), stringBooleanUnion.testForEquality(typeRegistry.stringType()));
    assertEquals(typeRegistry.booleanType(), stringBooleanUnion.testForEquality(typeRegistry.booleanType()));
    assertEquals(typeRegistry.stringType(), booleanStringUnion.testForEquality(typeRegistry.stringType()));
    assertEquals(typeRegistry.booleanType(), booleanStringUnion.testForEquality(typeRegistry.booleanType()));
  }

  @Test
  public void testUnionTypeIsNullable() {
    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.numberType()));

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.numberType()));

    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    UnionType stringBooleanUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.booleanType()));
    UnionType booleanStringUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.booleanType(), typeRegistry.stringType()));

    JSType stringType = typeRegistry.stringType();
    JSType booleanType = typeRegistry.booleanType();
    JSType numberType = typeRegistry.numberType();
    JSType anyFunctionType = typeRegistry.objectFunctionType();

    assertEquals(typeRegistry.objectType(), stringBooleanUnion.getLeastSupertype(booleanStringUnion));
    assertEquals(stringType, stringBooleanUnion.getLeastSupertype(stringType));
    assertEquals(booleanType, stringBooleanUnion.getLeastSupertype(booleanType));
    assertEquals(numberType, stringBooleanUnion.getLeastSupertype(numberType));
    assertEquals(anyFunctionType, stringBooleanUnion.getLeastSupertype(anyFunctionType));

    assertEquals(booleanType, booleanStringUnion.getLeastSupertype(stringBooleanUnion));
    assertEquals(booleanType, booleanStringUnion.getLeastSupertype(booleanType));
    assertEquals(stringType, booleanStringUnion.getLeastSupertype(stringType));
    assertEquals(numberType, booleanStringUnion.getLeastSupertype(numberType));
    assertEquals(anyFunctionType, booleanStringUnion.getLeastSupertype(anyFunctionType));

    assertEquals(typeRegistry.unionType(ImmutableSet.of(stringType, booleanType, numberType), registry), stringBooleanUnion.getLeastSupertype(numberType));
  }

  @Test
  public void testUnionTypeMeet() {
    UnionType stringBooleanUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.stringType(), typeRegistry.booleanType()));
    UnionType booleanStringUnion = new UnionType(typeRegistry, ImmutableSet.of(typeRegistry.booleanType(), typeRegistry.stringType()));

    UnionType stringBooleanMeet = stringBooleanUnion.meet(booleanStringUnion);
    UnionType booleanStringMeet = booleanStringUnion.meet(stringBooleanUnion);

    assertEquals(typeRegistry.objectType(), stringBooleanMeet);
    assertEquals(typeRegistry.objectType(), booleanStringMeet);
  }
}