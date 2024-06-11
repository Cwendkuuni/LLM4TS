package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.TypePair;
import com.google.javascript.rhino.UnionType;
import com.google.javascript.rhino.UnionTypeBuilder;
import com.google.javascript.rhino.Visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class UnionTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testEquals() {
    JSType typeA = registry.getBooleanType();
    JSType typeB = registry.getNumberType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(typeA));
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(typeB));

    assertFalse(unionTypeA.equals(unionTypeB));

    UnionType unionTypeC = new UnionType(registry, ImmutableSet.of(typeA, typeB));
    assertTrue(unionTypeA.equals(unionTypeC));
    assertTrue(unionTypeC.equals(unionTypeA));
  }

  @Test
  public void testHashCode() {
    JSType typeA = registry.getBooleanType();
    JSType typeB = registry.getNumberType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(typeA));
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(typeB));
    UnionType unionTypeC = new UnionType(registry, ImmutableSet.of(typeA, typeB));

    assertEquals(unionTypeA.hashCode(), unionTypeC.hashCode());
    assertNotEquals(unionTypeA.hashCode(), unionTypeB.hashCode());
  }

  @Test
  public void testCanAssignTo() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();

    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    JSType intValue = registry.getIntValue(42);
    JSType stringValue = registry.getStringValue("foo");

    assertTrue(unionType.canAssignTo(intValue));
    assertTrue(unionType.canAssignTo(stringValue));

    JSType nullType = registry.getNullType();
    assertFalse(unionType.canAssignTo(nullType));

    JSType voidType = registry.getVoidType();
    assertFalse(unionType.canAssignTo(voidType));
  }

  @Test
  public void testCanBeCalled() {
    JSType functionType = registry.getFunctionType(registry.getContext(), new ArrayList<>());
    UnionType unionType = new UnionType(registry, ImmutableSet.of(functionType));

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType nullType = registry.getNullType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(nullType, stringType));

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();

    assertTrue(restictedType.isNullType());
  }

  @Test
  public void testTestForEquality() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();

    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    JSType intValue1 = registry.getIntValue(42);
    JSType intValue2 = registry.getIntValue(42);
    JSType stringValue = registry.getStringValue("foo");

    assertEquals(unionType.testForEquality(intValue1), unionType.testForEquality(intValue2));
    assertEquals(unionType.testForEquality(intValue1), UNKNOWN);
    assertEquals(unionType.testForEquality(stringValue), UNKNOWN);
  }

  @Test
  public void testIsNullable() {
    JSType nullType = registry.getNullType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(nullType));

    assertTrue(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    JSType unknownType = registry.getUnknownType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(unknownType));

    assertTrue(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();

    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    JSType objectType = registry.getObjectType();
    assertEquals(objectType, unionType.getLeastSupertype(objectType));

    JSType numberType = registry.getNumberType();
    assertEquals(numberType, unionType.getLeastSupertype(intType));

    JSType stringType2 = registry.getStringType();
    assertEquals(stringType2, unionType.getLeastSupertype(stringType));

    JSType booleanType = registry.getBooleanType();
    assertEquals(booleanType, unionType.getLeastSupertype(booleanType));
  }

  @Test
  public void testMeet() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(intType));
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(stringType));

    UnionType meetType = unionTypeA.meet(unionTypeB);

    assertTrue(meetType.isObject());
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    UnionType restrictedTypeInt = unionType.getRestrictedUnion(intType);
    UnionType restrictedTypeString = unionType.getRestrictedUnion(stringType);

    assertTrue(intType.isSubtype(restrictedTypeInt));
    assertTrue(stringType.isSubtype(restrictedTypeString));
    assertFalse(restrictedTypeInt.isSubtype(stringType));
    assertFalse(restrictedTypeString.isSubtype(intType));
  }

  @Test
  public void testToString() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    assertEquals("(number, string)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    assertTrue(intType.isSubtype(unionType));
    assertTrue(stringType.isSubtype(unionType));

    JSType objectType = registry.getObjectType();
    assertFalse(objectType.isSubtype(unionType));

    JSType booleanType = registry.getBooleanType();
    assertFalse(booleanType.isSubtype(unionType));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(booleanType));

    TypePair typePairTrue = unionType.getTypesUnderEquality(registry.getBooleanValue(true));
    TypePair typePairFalse = unionType.getTypesUnderEquality(registry.getBooleanValue(false));

    assertEquals(booleanType, typePairTrue.typeA);
    assertEquals(booleanType, typePairFalse.typeA);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSType intType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType));

    BooleanLiteralSet literals = unionType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.EMPTY, literals);
  }

  @Test
  public void testGetTypesUnderEquality() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    TypePair typePairInt = unionType.getTypesUnderEquality(registry.getIntValue(42));
    TypePair typePairString = unionType.getTypesUnderEquality(registry.getStringValue("foo"));

    assertNull(typePairInt.typeB);
    assertNull(typePairString.typeB);

    assertEquals(intType, typePairInt.typeA);
    assertEquals(intType, typePairInt.typeB);

    assertEquals(stringType, typePairString.typeA);
    assertEquals(stringType, typePairString.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType));

    TypePair typePairIntString = unionType.getTypesUnderInequality(registry.getStringValue("foo"));

    assertNull(typePairIntString.typeA);
    assertNull(typePairIntString.typeB);

    assertEquals(intType, typePairIntString.typeB);
    assertEquals(stringType, typePairIntString.typeA);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    JSType intType = registry.getNumberType();
    JSType stringType = registry.getStringType();
    JSType functionType = registry.getFunctionType(registry.getContext(), new ArrayList<>());
    UnionType unionType = new UnionType(registry, ImmutableSet.of(intType, stringType, functionType));

    TypePair typePairIntString = unionType.getTypesUnderShallowInequality(registry.getStringValue("foo"));

    assertNull(typePairIntString.typeA);
    assertNull(typePairIntString.typeB);

    assertEquals(intType, typePairIntString.typeB);
    assertEquals(stringType, typePairIntString.typeA);
  }

  @Test
  public void testVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(registry.getNumberType()));

    Visitor visitor = new Visitor() {
      @Override
      public JSType visitBooleanType(JSType booleanType) {
        return booleanType;
      }

      @Override
      public JSType visitNumberType(JSType numberType) {
        return numberType;
      }

      @Override
      public JSType visitUnionType(UnionType unionType) {
        fail();
      }

      @Override
      public JSType visitVoidType(JSType voidType) {
        return voidType;
      }
    };

    JSType result = unionType.visit(visitor);

    assertEquals(registry.getNumberType(), result);
  }

  @Test
  public void testResolveInternal() {
    JSType nullType = registry.getNullType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(nullType, stringType));

    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> scope = new StaticScope<JSType>() {};
    UnionType resolvedType = unionType.resolve(errorReporter, scope);

    assertTrue(resolvedType.isObject());
  }
}