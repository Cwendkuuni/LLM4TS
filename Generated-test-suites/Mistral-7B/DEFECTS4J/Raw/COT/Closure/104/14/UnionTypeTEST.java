package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TernaryValue;

public class UnionTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();
  private static final ErrorReporter errorReporter = new ErrorReporter();
  private static final StaticScope<JSType> scope = new StaticScope<>();
  private static final Set<JSType> stringAlternates = ImmutableSet.of(JSTypeNative.STRING);
  private static final Set<JSType> booleanAlternates = ImmutableSet.of(JSTypeNative.BOOLEAN);
  private static final Set<JSType> nullAlternates = ImmutableSet.of(JSTypeNative.NULL);
  private static final Set<JSType> numberAlternates = ImmutableSet.of(JSTypeNative.NUMBER);
  private static final Set<JSType> objectAlternates = ImmutableSet.of(JSTypeNative.OBJECT);
  private static final Set<JSType> errorAlternates = ImmutableSet.of(JSTypeNative.ERROR);
  private static final Set<JSType> arrayAlternates = ImmutableSet.of(JSTypeNative.ARRAY);

  private JSType stringType;
  private JSType booleanType;
  private JSType nullType;
  private JSType numberType;
  private JSType objectType;
  private JSType errorType;
  private JSType arrayType;
  private JSType unionType;

  @Before
  public void setup() {
    this.stringType = registry.getElementById("String");
    this.booleanType = registry.getElementById("Boolean");
    this.nullType = registry.getElementById("Null");
    this.numberType = registry.getElementById("Number");
    this.objectType = registry.getElementById("Object");
    this.errorType = registry.getElementById("Error");
    this.arrayType = new ArrayType(registry, ImmutableSet.of(numberType));

    this.unionType = new UnionType(registry, stringAlternates);
  }

  @Test
  public void testGetAlternates() {
    assertEquals(stringAlternates, unionType.getAlternates());
  }

  @Test
  public void testForgiveUnknownNames() {
    unionType.forgiveUnknownNames();
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testMatchesNumberContext() {
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    assertSame(stringType, unionType.findPropertyType("toString"));
  }

  @Test
  public void testCanAssignTo() {
    JSType anotherUnionType = new UnionType(registry, booleanAlternates);
    assertTrue(unionType.canAssignTo(anotherUnionType));
  }

  @Test
  public void testCanBeCalled() {
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertEquals(Collections.singleton(stringType), restrictedUnionType.getAlternates());
  }

  @Test
  public void testTestForEquality() {
    JSType anotherUnionType = new UnionType(registry, booleanAlternates);
    TernaryValue result = unionType.testForEquality(anotherUnionType);
    assertEquals(TernaryValue.MAYBE, result);

    result = unionType.testForEquality(stringType);
    assertEquals(TernaryValue.MAYBE, result);

    result = stringType.testForEquality(unionType);
    assertEquals(TernaryValue.MAYBE, result);

    result = unionType.testForEquality(numberType);
    assertEquals(TernaryValue.NO, result);
  }

  @Test
  public void testIsNullable() {
    assertFalse(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType stringOrBooleanType = new UnionType(registry, stringAlternates);
    JSType booleanOrStringType = new UnionType(registry, booleanAlternates);
    JSType stringOrBooleanOrNumberType =
        new UnionType(registry, stringAlternates, numberAlternates);

    assertEquals(stringOrBooleanType, unionType.getLeastSupertype(booleanType));
    assertEquals(booleanOrStringType, booleanType.getLeastSupertype(unionType));
    assertEquals(stringOrBooleanOrNumberType, unionType.getLeastSupertype(numberType));
  }

  @Test
  public void testMeet() {
    JSType booleanOrStringType = new UnionType(registry, booleanAlternates, stringAlternates);
    JSType numberOrBooleanOrStringType =
        new UnionType(registry, numberAlternates, booleanAlternates, stringAlternates);

    JSType result = unionType.meet(booleanOrStringType);
    assertEquals(booleanOrStringType, result);

    result = booleanOrStringType.meet(unionType);
    assertEquals(booleanOrStringType, result);

    result = unionType.meet(numberOrBooleanOrStringType);
    assertEquals(numberOrBooleanOrStringType, result);
  }

  @Test
  public void testEquals() {
    JSType anotherUnionType = new UnionType(registry, booleanAlternates);
    assertFalse(unionType.equals(anotherUnionType));

    UnionType anotherUnionTypeWithSameAlternates = new UnionType(registry, stringAlternates);
    assertTrue(unionType.equals(anotherUnionTypeWithSameAlternates));
  }

  @Test
  public void testHashCode() {
    UnionType anotherUnionTypeWithSameAlternates = new UnionType(registry, stringAlternates);
    assertEquals(unionType.hashCode(), anotherUnionTypeWithSameAlternates.hashCode());
  }

  @Test
  public void testIsUnionType() {
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    assertTrue(unionType.contains(stringType));
    assertFalse(unionType.contains(numberType));

    UnionType unionTypeWithNumber = new UnionType(registry, stringAlternates, numberAlternates);
    assertTrue(unionTypeWithNumber.contains(numberType));
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType restrictedUnionType = unionType.getRestrictedUnion(numberType);
    assertNotNull(restrictedUnionType);
    assertEquals(Collections.singleton(stringType), restrictedUnionType.getAlternates());
  }

  @Test
  public void testToString() {
    assertEquals("(String)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    assertFalse(numberType.isSubtype(unionType));
    assertTrue(stringType.isSubtype(unionType));
    assertTrue(unionType.isSubtype(numberType));
    assertTrue(unionType.isSubtype(stringType));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    TernaryValue booleanOutcome = TernaryValue.TRUE;
    JSType restrictedType = unionType.getRestrictedTypeGivenToBooleanOutcome(booleanOutcome);
    assertEquals(stringType, restrictedType);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    BooleanLiteralSet outcomes = unionType.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, outcomes);
  }

  @Test
  public void testGetTypesUnderEquality() {
    JSType anotherUnionType = new UnionType(registry, booleanAlternates);
    TypePair typesUnderEquality = unionType.getTypesUnderEquality(anotherUnionType);
    assertNotNull(typesUnderEquality.typeA);
    assertNotNull(typesUnderEquality.typeB);
    assertSame(unionType, typesUnderEquality.typeA);
    assertSame(anotherUnionType, typesUnderEquality.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    JSType anotherUnionType = new UnionType(registry, booleanAlternates);
    TypePair typesUnderInequality = unionType.getTypesUnderInequality(anotherUnionType);
    assertNotNull(typesUnderInequality.typeA);
    assertNotNull(typesUnderInequality.typeB);
    assertSame(unionType, typesUnderInequality.typeA);
    assertSame(anotherUnionType, typesUnderInequality.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    JSType anotherUnionType = new UnionType(registry, booleanAlternates);
    TypePair typesUnderShallowInequality = unionType.getTypesUnderShallowInequality(anotherUnionType);
    assertNotNull(typesUnderShallowInequality.typeA);
    assertNotNull(typesUnderShallowInequality.typeB);
    assertSame(unionType, typesUnderShallowInequality.typeA);
    assertSame(anotherUnionType, typesUnderShallowInequality.typeB);
  }

  @Test
  public void testResolveInternal() {
    UnionType resolvedUnionType = (UnionType) unionType.resolveInternal(errorReporter, scope);
    assertEquals(stringAlternates, resolvedUnionType.getAlternates());
  }
}