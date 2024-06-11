package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.StaticScope;

import java.util.Set;

public class UnionTypeTest {
  @Test
  public void testUnionTypeCreation() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    assertEquals(typeA.getClass(), UnionType.class);
    assertEquals(typeA.getAlternates().size(), 2);
    assertTrue(typeA.isUnionType());
    assertTrue(typeA.isObject());

    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);
    assertEquals(typeB, typeA);
  }

  @Test
  public void testUnionTypeStringRepresentation() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertEquals("(number, string)", type.toString());
  }

  @Test
  public void testUnionTypeContains() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertTrue(type.contains(JSTypeNative.NUMBER_TYPE));
    assertTrue(type.contains(JSTypeNative.STRING_TYPE));
    assertFalse(type.contains(JSTypeNative.BOOLEAN_TYPE));
  }

  @Test
  public void testUnionTypeSubtype() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);

    assertTrue(typeA.isSubtype(typeB));
  }

  @Test
  public void testUnionTypeRestrictedByType() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.NUMBER_TYPE);
    UnionType typeB = typeA.getRestrictedUnion(JSTypeNative.NUMBER_TYPE);

    assertEquals(typeB, JSTypeNative.NUMBER_TYPE);
  }

  @Test
  public void testUnionTypeForgiveUnknownNames() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    type.forgiveUnknownNames();
  }

  @Test
  public void testUnionTypeMatchesNumberContext() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertTrue(type.matchesNumberContext());
  }

  @Test
  public void testUnionTypeMatchesStringContext() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertTrue(type.matchesStringContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContext() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NULL_TYPE, JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertTrue(type.matchesObjectContext());
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);
    Set<JSType> setC = ImmutableSet.of(JSTypeNative.STRING_TYPE);
    UnionType typeC = new UnionType(JSTypeRegistry.instance(), setC);

    assertTrue(typeA.canAssignTo(typeB));
    assertTrue(typeA.canAssignTo(typeC));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.FUNCTION_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertTrue(type.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNullOrUndefined() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NULL_TYPE, JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    UnionType restricted = type.restrictByNotNullOrUndefined();
    assertNull(restricted);
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);

    TypePair pair = typeA.getTypesUnderEquality(typeB);
    assertTrue(pair.typeA != null);
    assertTrue(pair.typeB != null);
    assertEquals(typeB, pair.typeA);
    assertEquals(typeA, pair.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);

    TypePair pair = typeA.getTypesUnderInequality(typeB);
    assertTrue(pair.typeA != null);
    assertTrue(pair.typeB != null);
    assertFalse(pair.typeA.equals(pair.typeB));
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);

    TypePair pair = typeA.getTypesUnderShallowInequality(typeB);
    assertTrue(pair.typeA != null);
    assertTrue(pair.typeB != null);
    assertFalse(pair.typeA.equals(pair.typeB));
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);

    assertEquals(typeA, typeB);

    Set<JSType> setC = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE);
    UnionType typeC = new UnionType(JSTypeRegistry.instance(), setC);

    assertFalse(typeA.equals(typeC));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);

    assertEquals(typeA.hashCode(), typeB.hashCode());

    Set<JSType> setC = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE);
    UnionType typeC = new UnionType(JSTypeRegistry.instance(), setC);

    assertNotEquals(typeA.hashCode(), typeC.hashCode());
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);
    Set<JSType> setC = ImmutableSet.of(JSTypeNative.STRING_TYPE);
    UnionType typeC = new UnionType(JSTypeRegistry.instance(), setC);

    assertEquals(typeA.meet(typeB), typeC);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NULL_TYPE, JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertFalse(type.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.UNKNOWN_TYPE, JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);
    assertTrue(type.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);
    Set<JSType> setC = ImmutableSet.of(JSTypeNative.OBJECT_TYPE);
    UnionType typeC = new UnionType(JSTypeRegistry.instance(), setC);

    assertEquals(typeA.getLeastSupertype(typeB), typeC);
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> setA = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType typeA = new UnionType(JSTypeRegistry.instance(), setA);
    Set<JSType> setB = ImmutableSet.of(JSTypeNative.STRING_TYPE, JSTypeNative.NUMBER_TYPE);
    UnionType typeB = new UnionType(JSTypeRegistry.instance(), setB);
    Set<JSType> setC = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.BOOLEAN_TYPE);
    UnionType typeC = new UnionType(JSTypeRegistry.instance(), setC);

    TernaryValue result = typeA.testForEquality(typeB);
    assertEquals(TernaryValue.UNDEFINED, result);

    result = typeA.testForEquality(typeC);
    assertEquals(TernaryValue.FALSE, result);

    result = typeB.testForEquality(typeC);
    assertEquals(TernaryValue.FALSE, result);

    result = typeA.testForEquality(typeA);
    assertEquals(TernaryValue.TRUE, result);
  }

  @Test
  public void testUnionTypeResolve() {
    Set<JSType> set = ImmutableSet.of(JSTypeNative.NUMBER_TYPE, JSTypeNative.STRING_TYPE);
    UnionType type = new UnionType(JSTypeRegistry.instance(), set);

    ErrorReporter errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int pc) {
        fail("Unexpected error: " + message);
      }
    };
    StaticScope<JSType> scope = new StaticScope<JSType>(JSTypeRegistry.instance());

    type.resolve(errorReporter, scope);
  }
}