package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class UnionTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeFactory typeFactory;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    typeFactory = new JSTypeFactory(errorReporter);
  }

  @Test
  public void testUnionTypeConstructor() {
    Set<JSType> alternates = new HashSet<>(Arrays.asList(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    UnionType unionType = new UnionType(typeFactory, alternates);
    assertEquals(ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE), unionType.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertFalse(stringBooleanUnion.matchesNumberContext());

    UnionType numberBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertTrue(numberBooleanUnion.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertTrue(stringBooleanUnion.matchesStringContext());

    UnionType numberBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertFalse(numberBooleanUnion.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertTrue(stringBooleanUnion.matchesObjectContext());

    UnionType voidUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.VOID_TYPE));
    assertFalse(voidUnion.matchesObjectContext());
  }

  @Test(expected = AssertionError.class)
  public void testFindPropertyTypeNull() {
    UnionType unionType = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NULL_TYPE, JSTypeNative.OBJECT_TYPE));
    unionType.findPropertyType(null);
  }

  @Test
  public void testCanAssignTo() {
    UnionType unionType = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    JSType stringType = JSTypeNative.STRING_TYPE;
    JSType booleanType = JSTypeNative.BOOLEAN_TYPE;
    JSType numberType = JSTypeNative.NUMBER_TYPE;

    assertTrue(unionType.canAssignTo(stringType));
    assertTrue(unionType.canAssignTo(booleanType));
    assertFalse(unionType.canAssignTo(numberType));
  }

  @Test
  public void testCanBeCalled() {
    UnionType unionType = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.NULL_TYPE));
    UnionType nullableNumber = unionType.restrictByNotNullOrUndefined();

    UnionType expectedNull = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NULL_TYPE));
    assertEquals(expectedNull, nullableNumber);
  }

  @Test
  public void testTestForEquality() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    JSType stringType = JSTypeNative.STRING_TYPE;
    JSType booleanType = JSTypeNative.BOOLEAN_TYPE;

    UnionType stringStringUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.STRING_TYPE));
    UnionType booleanBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.BOOLEAN_TYPE, JSTypeNative.BOOLEAN_TYPE));

    TernaryValue result = stringBooleanUnion.testForEquality(stringType);
    assertEquals(JSTypeNative.UNKNOWN, result);

    result = stringBooleanUnion.testForEquality(booleanType);
    assertEquals(JSTypeNative.UNKNOWN, result);

    result = stringStringUnion.testForEquality(stringType);
    assertEquals(JSTypeNative.TRUE, result);

    result = booleanBooleanUnion.testForEquality(booleanType);
    assertEquals(JSTypeNative.TRUE, result);
  }

  @Test
  public void testIsNullable() {
    UnionType unionType = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.NULL_TYPE));
    assertFalse(unionType.isNullable());

    UnionType nullableUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NULL_TYPE, JSTypeNative.STRING_TYPE));
    assertTrue(nullableUnion.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType unionType = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE));
    assertFalse(unionType.isUnknownType());

    UnionType unknownUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.UNKNOWN_TYPE, JSTypeNative.STRING_TYPE));
    assertTrue(unknownUnion.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    UnionType stringStringUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.STRING_TYPE));

    JSType result = stringBooleanUnion.getLeastSupertype(stringStringUnion);
    assertEquals(stringStringUnion, result);

    JSType result2 = stringStringUnion.getLeastSupertype(stringBooleanUnion);
    assertEquals(stringBooleanUnion, result2);
  }

  @Test
  public void testMeet() {
    UnionType numberBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE));
    UnionType booleanNumberUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.BOOLEAN_TYPE, JSTypeNative.NUMBER_TYPE));

    JSType result = numberBooleanUnion.meet(booleanNumberUnion);
    assertNotNull(result);
    assertEquals(JSTypeNative.VOID_TYPE, result);
  }

  @Test
  public void testEquals() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    UnionType stringStringUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.STRING_TYPE));

    assertTrue(stringBooleanUnion.equals(stringBooleanUnion));
    assertFalse(stringBooleanUnion.equals(stringStringUnion));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType numberBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE));
    JSType numberType = JSTypeNative.NUMBER_TYPE;

    UnionType result = numberBooleanUnion.getRestrictedUnion(numberType);
    assertEquals(numberType, result);
  }

  @Test
  public void testToString() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    String result = stringBooleanUnion.toString();
    assertEquals("(String|Boolean)", result);
  }

  @Test
  public void testIsSubtype() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    UnionType booleanNumberUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.BOOLEAN_TYPE, JSTypeNative.NUMBER_TYPE));
    JSType stringType = JSTypeNative.STRING_TYPE;

    assertTrue(stringBooleanUnion.isSubtype(stringType));
    assertTrue(stringBooleanUnion.isSubtype(booleanNumberUnion));
    assertFalse(booleanNumberUnion.isSubtype(stringBooleanUnion));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType stringBooleanUnion = new UnionType(typeFactory, ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    BooleanLiteralSet result = stringBooleanUnion.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, result);
  }
}