package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.TernaryValue.UNKNOWN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypePair;
import com.google.javascript.rhino.UnionType;
import com.google.javascript.rhino.UnionTypeBuilder;

import java.util.SortedSet;
import java.util.TreeSet;

public class UnionTypeTest {
  private static ErrorReporter REPORTER = new ErrorReporter() {
    @Override
    public void error(String message) {
      throw new RuntimeException(message);
    }

    @Override
    public void fatal(String message) {
      throw new RuntimeException(message);
    }
  };

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> typesA = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    Set<JSType> typesB = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType unionA = new UnionType(new JSTypeRegistry(), typesA);
    UnionType unionB = new UnionType(new JSTypeRegistry(), typesB);

    assertEquals(unionA, unionB);
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> typesA = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    Set<JSType> typesB = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType unionA = new UnionType(new JSTypeRegistry(), typesA);
    UnionType unionB = new UnionType(new JSTypeRegistry(), typesB);

    assertEquals(unionA.hashCode(), unionB.hashCode());
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> types = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType union = new UnionType(new JSTypeRegistry(), types);

    assertEquals("(number, string)", union.toString());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE));
    UnionType numberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.NUMBER_TYPE));

    assertTrue(stringUnion.isSubtype(stringUnion));
    assertTrue(numberUnion.isSubtype(numberUnion));
    assertTrue(stringUnion.isSubtype(numberUnion)); // string is a subtype of number
    assertFalse(numberUnion.isSubtype(stringUnion)); // number is not a subtype of string

    UnionType stringBooleanUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));
    UnionType booleanStringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.BOOLEAN_TYPE, JSTypeNative.STRING_TYPE));

    assertTrue(stringBooleanUnion.isSubtype(stringBooleanUnion));
    assertTrue(booleanStringUnion.isSubtype(booleanStringUnion));
    assertTrue(stringBooleanUnion.isSubtype(booleanStringUnion)); // string is a subtype of boolean
    assertTrue(booleanStringUnion.isSubtype(stringBooleanUnion)); // boolean is a subtype of string
  }

  @Test
  public void testUnionTypeGetAlternates() {
    UnionType union = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    assertEquals(2, union.getAlternates().size());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE));
    UnionType objectUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.OBJECT_TYPE));

    assertEquals(objectUnion, stringUnion.getLeastSupertype(objectUnion));
  }

  @Test
  public void testUnionTypeMeet() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE));
    UnionType numberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.NUMBER_TYPE));
    UnionType objectUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.OBJECT_TYPE));

    UnionType result = stringUnion.meet(numberUnion);
    assertEquals(objectUnion, result);

    result = numberUnion.meet(stringUnion);
    assertEquals(objectUnion, result);

    result = stringUnion.meet(objectUnion);
    assertEquals(stringUnion, result);

    result = objectUnion.meet(stringUnion);
    assertEquals(objectUnion, result);
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE));
    UnionType numberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.NUMBER_TYPE));
    UnionType objectUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.OBJECT_TYPE));

    assertTrue(stringUnion.canAssignTo(stringUnion));
    assertTrue(numberUnion.canAssignTo(numberUnion));
    assertTrue(stringUnion.canAssignTo(numberUnion)); // string can be assigned to number (implicit conversion)
    assertFalse(numberUnion.canAssignTo(stringUnion)); // number can't be assigned to string
    assertTrue(stringUnion.canAssignTo(objectUnion)); // string can be assigned to object
    assertTrue(objectUnion.canAssignTo(stringUnion)); // object can be assigned to string (implicit conversion)
    assertFalse(objectUnion.canAssignTo(numberUnion)); // object can't be assigned to number
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE));
    UnionType functionUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.FUNCTION_TYPE));

    assertTrue(stringUnion.canBeCalled());
    assertFalse(functionUnion.canBeCalled());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    UnionType union = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    assertTrue(union.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    UnionType union = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    assertTrue(union.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));
    UnionType stringNumberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    assertTrue(stringUnion.contains(JSTypeNative.STRING_TYPE));
    assertTrue(stringUnion.contains(JSTypeNative.NUMBER_TYPE));
    assertTrue(stringUnion.contains(stringNumberUnion));
    assertFalse(stringUnion.contains(JSTypeNative.BOOLEAN_TYPE));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));
    UnionType numberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.NUMBER_TYPE));

    UnionType result = stringUnion.getRestrictedUnion(numberUnion);
    assertEquals(numberUnion, result);

    result = numberUnion.getRestrictedUnion(stringUnion);
    assertEquals(stringUnion, result);
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    UnionType stringNumberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    JSType result = stringNumberUnion.findPropertyType("length");
    assertEquals(JSTypeNative.NUMBER_TYPE, result);
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));
    UnionType stringBooleanUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    TypePair result = stringUnion.getTypesUnderEquality(stringBooleanUnion);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.typeA);
    assertNull(result.typeB);

    result = stringBooleanUnion.getTypesUnderEquality(stringUnion);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.typeB);
    assertNull(result.typeA);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));
    UnionType stringBooleanUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    TypePair result = stringUnion.getTypesUnderInequality(stringBooleanUnion);
    assertEquals(JSTypeNative.VOID_TYPE, result.typeA);
    assertNull(result.typeB);

    result = stringBooleanUnion.getTypesUnderInequality(stringUnion);
    assertEquals(JSTypeNative.VOID_TYPE, result.typeB);
    assertNull(result.typeA);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));
    UnionType stringBooleanUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    TypePair result = stringUnion.getTypesUnderShallowInequality(stringBooleanUnion);
    assertEquals(JSTypeNative.NUMBER_TYPE, result.typeA);
    assertEquals(JSTypeNative.BOOLEAN_TYPE, result.typeB);

    result = stringBooleanUnion.getTypesUnderShallowInequality(stringUnion);
    assertEquals(JSTypeNative.STRING_TYPE, result.typeB);
    assertEquals(JSTypeNative.NUMBER_TYPE, result.typeA);
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    assertEquals(JSTypeNative.STRING_TYPE, stringUnion.getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(JSTypeNative.NUMBER_TYPE, stringUnion.getRestrictedTypeGivenToBooleanOutcome(false));
  }

  @Test
  public void testUnionTypePossibleToBooleanOutcomes() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    BooleanLiteralSet result = stringUnion.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, result);
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    UnionType stringUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));

    assertEquals(JSTypeNative.VOID_TYPE, stringUnion.restrictByNotNullOrUndefined().getType());
  }

  @Test
  public void testUnionTypeResolveInternal() {
    UnionType stringNumberUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE));
    UnionType stringBooleanUnion = new UnionType(new JSTypeRegistry(), ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.BOOLEAN_TYPE));

    UnionType stringNumberResolved = stringNumberUnion.resolveInternal(REPORTER, new StaticScope<JSType>(null));
    UnionType stringBooleanResolved = stringBooleanUnion.resolveInternal(REPORTER, new StaticScope<JSType>(null));

    assertEquals(JSTypeNative.OBJECT_TYPE, stringNumberResolved.getType());
    assertEquals(JSTypeNative.OBJECT_TYPE, stringBooleanResolved.getType());
  }
}