package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.ImmutableSet;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.testtype.MyCustomType;
import com.google.javascript.rhino.jstype.testtype.MyCustomTypeA;
import com.google.javascript.rhino.jstype.testtype.MyCustomTypeB;
import com.google.javascript.rhino.testing.AssertTypes;

public class UnionTypeTest {
  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(TYPE_REGISTRY.getNullType(), TYPE_REGISTRY.getStringType());
    UnionType unionType = new UnionType(TYPE_REGISTRY, alternates);
    assertEquals("(null, String)", unionType.toString());
  }

  @Test
  public void testGetAlternates() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    Iterable<JSType> alternates = unionType.getAlternates();
    assertEquals(2, alternates.spliterator().getExactSizeIfKnown());
    assertTrue(alternates.contains(TYPE_REGISTRY.getBooleanType()));
    assertTrue(alternates.contains(TYPE_REGISTRY.getStringType()));
  }

  @Test
  public void testForgiveUnknownNames() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getUnknownType(), TYPE_REGISTRY.getStringType()));
    unionType.forgiveUnknownNames();
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNumberType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNumberType(), TYPE_REGISTRY.getStringType()));
    assertFalse(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNullType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(new MyCustomTypeA(), new MyCustomTypeB()));
    JSType propertyType = unionType.findPropertyType("property");
    assertNull(propertyType);
  }

  @Test
  public void testCanAssignTo() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNumberType(), TYPE_REGISTRY.getStringType()));
    JSType that = TYPE_REGISTRY.getBooleanType();
    assertFalse(unionType.canAssignTo(that));
  }

  @Test
  public void testCanBeCalled() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNullType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNullType(), TYPE_REGISTRY.getStringType()));
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals("String", restricted.toString());
  }

  @Test
  public void testTestForEquality() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    TernaryValue result = unionTypeA.testForEquality(unionTypeB);
    assertEquals(UNKNOWN, result);

    UnionType unionTypeC = createUnionType(TYPE_REGISTRY, Arrays.asList(unionTypeA, unionTypeB));
    result = unionTypeC.testForEquality(unionTypeA);
    assertEquals(TRUE, result);

    UnionType unionTypeD = createUnionType(TYPE_REGISTRY, Arrays.asList(unionTypeA, unionTypeB));
    result = unionTypeD.testForEquality(unionTypeB);
    assertEquals(TRUE, result);
  }

  @Test
  public void testIsNullable() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNullType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNullType(), TYPE_REGISTRY.getStringType()));
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    EqualityType leastSupertype = (EqualityType) unionTypeA.getLeastSupertype(unionTypeB);
    assertEquals("String|Boolean", leastSupertype.toString());
  }

  @Test
  public void testMeet() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    UnionType result = (UnionType) unionTypeA.meet(unionTypeB);
    assertEquals("String|Boolean", result.toString());
  }

  @Test
  public void testEquals() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    assertFalse(unionTypeA.equals(unionTypeB));

    UnionType unionTypeC = createUnionType(TYPE_REGISTRY, Arrays.asList(unionTypeA, unionTypeB));
    assertTrue(unionTypeC.equals(unionTypeC));
  }

  @Test
  public void testHashCode() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    assertNotSame(unionTypeA.hashCode(), unionTypeB.hashCode());
  }

  @Test
  public void testIsUnionType() {
    UnionType unionType = new UnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    JSType booleanType = TYPE_REGISTRY.getBooleanType();
    assertTrue(unionType.contains(booleanType));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNumberType(), TYPE_REGISTRY.getStringType()));
    JSType numberType = TYPE_REGISTRY.getNumberType();
    UnionType restricted = unionType.getRestrictedUnion(numberType);
    assertEquals("String", restricted.toString());
  }

  @Test
  public void testToString() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    assertEquals("(Boolean, String)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    assertTrue(unionTypeA.isSubtype(unionTypeB));

    UnionType unionTypeC = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getNumberType(), TYPE_REGISTRY.getStringType()));
    assertFalse(unionTypeA.isSubtype(unionTypeC));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    assertTrue(unionType.getRestrictedTypeGivenToBooleanOutcome(true).isBooleanType());
    assertTrue(unionType.getRestrictedTypeGivenToBooleanOutcome(false).isStringType());
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    BooleanLiteralSet possibleToBooleanOutcomes = unionType.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, possibleToBooleanOutcomes);
  }

  @Test
  public void testGetTypesUnderEquality() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    TypePair typesUnderEquality = unionTypeA.getTypesUnderEquality(unionTypeB);
    assertSame(typesUnderEquality.typeA, unionTypeB);
    assertSame(typesUnderEquality.typeB, unionTypeA);
  }

  @Test
  public void testGetTypesUnderInequality() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    TypePair typesUnderInequality = unionTypeA.getTypesUnderInequality(unionTypeB);
    assertSame(typesUnderInequality.typeA, unionTypeB);
    assertNull(typesUnderInequality.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    UnionType unionTypeA = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    UnionType unionTypeB = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getStringType(), TYPE_REGISTRY.getBooleanType()));
    TypePair typesUnderShallowInequality = unionTypeA.getTypesUnderShallowInequality(unionTypeB);
    assertNull(typesUnderShallowInequality.typeA);
    assertSame(typesUnderShallowInequality.typeB, unionTypeB);
  }

  @Test
  public void testVisit() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(TYPE_REGISTRY.getBooleanType(), TYPE_REGISTRY.getStringType()));
    Visitor<Object> visitor = new Visitor<Object>() {
      @Override
      public Object caseUnionType(UnionType unionType) {
        return unionType.toString();
      }
    };
    Object result = unionType.visit(visitor);
    assertEquals("(Boolean, String)", result);
  }

  @Test
  public void testResolveInternal() {
    UnionType unionType = createUnionType(TYPE_REGISTRY, Arrays.asList(new MyCustomTypeA(), new MyCustomTypeB()));
    unionType.resolveInternal(TYPE_REGISTRY.getErrorReporter(), new StaticScope<JSType>(TYPE_REGISTRY));
    assertSame(unionType, TYPE_REGISTRY.getMyCustomTypeA());
  }

  private UnionType createUnionType(JSTypeRegistry typeRegistry, List<JSType> types) {
    return new UnionType(typeRegistry, ImmutableSet.copyOf(types));
  }
}