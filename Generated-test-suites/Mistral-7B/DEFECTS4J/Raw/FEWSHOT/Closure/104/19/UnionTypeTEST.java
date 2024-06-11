package com.google.javascript.rhino.jstype;

import static com.google.junit.testing.JSTestCase.assertUnionEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.TypePair;
import com.google.javascript.rhino.jstype.UnionType.UnionTypeBuilder;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionB = new UnionType(registry, setB);

    Set<JSType> setC = ImmutableSet.of(registry.getNumberType(), registry.getStringType());
    UnionType unionC = new UnionType(registry, setC);

    assertUnionEquals(unionA, unionB);
    assertFalse(unionA.equals(unionC));
  }

  @Test
  public void testUnionTypeSubtype() {
    Set<JSType> setA = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionB = new UnionType(registry, setB);

    Set<JSType> setC = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionC = new UnionType(registry, setC);

    assertTrue(unionA.isSubtype(unionB));
    assertTrue(unionB.isSubtype(unionA));
    assertFalse(unionA.isSubtype(unionC));
    assertFalse(unionC.isSubtype(unionA));
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getNullType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getBooleanType(), registry.getObjectType());
    UnionType unionB = new UnionType(registry, setB);

    Set<JSType> setC = ImmutableSet.of(registry.getNullType(), registry.getObjectType());
    UnionType unionC = new UnionType(registry, setC);

    UnionType restrictA = unionA.restrictByNotNullOrUndefined();
    UnionType restrictB = unionB.restrictByNotNullOrUndefined();
    UnionType restrictC = unionC.restrictByNotNullOrUndefined();

    assertSame(restrictA, restrictB);
    assertFalse(restrictA.equals(restrictC));
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionB = new UnionType(registry, setB);

    UnionType meetAB = unionA.meet(unionB);
    UnionType meetBA = unionB.meet(unionA);

    assertTrue(meetAB.equals(meetBA));
    assertFalse(meetAB.equals(unionA));
    assertFalse(meetAB.equals(unionB));

    Set<JSType> setC = ImmutableSet.of(registry.getObjectType(), registry.getNumberType());
    UnionType unionC = new UnionType(registry, setC);

    UnionType meetAC = unionA.meet(unionC);
    UnionType meetCA = unionC.meet(unionA);

    assertNull(meetAC);
    assertNull(meetCA);
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getNumberType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionB = new UnionType(registry, setB);

    Set<JSType> setC = ImmutableSet.of(registry.getNumberType(), registry.getBooleanType());
    UnionType unionC = new UnionType(registry, setC);

    assertTrue(unionA.canAssignTo(unionB));
    assertTrue(unionB.canAssignTo(unionA));
    assertTrue(unionA.canAssignTo(unionC));
    assertTrue(unionC.canAssignTo(unionA));

    Set<JSType> setD = ImmutableSet.of(registry.getNumberType(), registry.getNullType());
    UnionType unionD = new UnionType(registry, setD);

    assertFalse(unionA.canAssignTo(unionD));
    assertFalse(unionD.canAssignTo(unionA));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getFunctionType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getFunctionType());
    UnionType unionB = new UnionType(registry, setB);

    Set<JSType> setC = ImmutableSet.of(registry.getNumberType(), registry.getFunctionType());
    UnionType unionC = new UnionType(registry, setC);

    assertTrue(unionA.canBeCalled());
    assertTrue(unionB.canBeCalled());
    assertTrue(unionC.canBeCalled());
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionB = new UnionType(registry, setB);

    assertEquals("(boolean|string)", unionA.toString());
    assertEquals("(string|boolean)", unionB.toString());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getNumberType(), registry.getStringType());
    UnionType unionB = new UnionType(registry, setB);

    JSType leastSupertypeAB = unionA.getLeastSupertype(unionB);
    JSType leastSupertypeBA = unionB.getLeastSupertype(unionA);

    assertEquals(unionB, leastSupertypeAB);
    assertEquals(unionA, leastSupertypeBA);

    Set<JSType> setC = ImmutableSet.of(registry.getNumberType(), registry.getNumberType());
    UnionType unionC = new UnionType(registry, setC);

    JSType leastSupertypeAC = unionA.getLeastSupertype(unionC);
    JSType leastSupertypeCA = unionC.getLeastSupertype(unionA);

    assertSame(registry.getObjectType(), leastSupertypeAC);
    assertSame(registry.getObjectType(), leastSupertypeCA);
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionB = new UnionType(registry, setB);

    JSType restrictedUnionAB = unionA.getRestrictedUnion(unionB);
    JSType restrictedUnionBA = unionB.getRestrictedUnion(unionA);

    assertNull(restrictedUnionAB);
    assertNull(restrictedUnionBA);

    Set<JSType> setC = ImmutableSet.of(registry.getBooleanType(), registry.getBooleanType());
    UnionType unionC = new UnionType(registry, setC);

    UnionType restrictedUnionCC = unionC.getRestrictedUnion(unionC);
    assertEquals(registry.getBooleanType(), restrictedUnionCC);
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionB = new UnionType(registry, setB);

    Set<JSType> setC = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionC = new UnionType(registry, setC);

    assertTrue(unionA.isSubtype(unionB));
    assertTrue(unionB.isSubtype(unionA));
    assertTrue(unionA.isSubtype(unionC));
    assertTrue(unionC.isSubtype(unionA));

    Set<JSType> setD = ImmutableSet.of(registry.getNumberType(), registry.getNumberType());
    UnionType unionD = new UnionType(registry, setD);

    assertTrue(unionA.isSubtype(unionD));
    assertTrue(unionD.isSubtype(unionA));

    Set<JSType> setE = ImmutableSet.of(registry.getNullType(), registry.getObjectType());
    UnionType unionE = new UnionType(registry, setE);

    assertFalse(unionA.isSubtype(unionE));
    assertFalse(unionE.isSubtype(unionA));
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionB = new UnionType(registry, setB);

    TypePair pairAB = unionA.getTypesUnderEquality(unionB);
    TypePair pairBA = unionB.getTypesUnderEquality(unionA);

    assertNull(pairAB.typeA);
    assertNull(pairBA.typeB);

    Set<JSType> setC = ImmutableSet.of(registry.getStringType(), registry.getStringType());
    UnionType unionC = new UnionType(registry, setC);

    TypePair pairCC = unionC.getTypesUnderEquality(unionC);

    assertEquals(registry.getStringType(), pairCC.typeA);
    assertEquals(registry.getStringType(), pairCC.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionB = new UnionType(registry, setB);

    TypePair pairAB = unionA.getTypesUnderInequality(unionB);
    TypePair pairBA = unionB.getTypesUnderInequality(unionA);

    assertNull(pairAB.typeA);
    assertNull(pairBA.typeB);

    Set<JSType> setC = ImmutableSet.of(registry.getStringType(), registry.getStringType());
    UnionType unionC = new UnionType(registry, setC);

    TypePair pairCC = unionC.getTypesUnderInequality(unionC);

    assertNull(pairCC.typeA);
    assertNull(pairCC.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());
    UnionType unionB = new UnionType(registry, setB);

    TypePair pairAB = unionA.getTypesUnderShallowInequality(unionB);
    TypePair pairBA = unionB.getTypesUnderShallowInequality(unionA);

    assertNull(pairAB.typeA);
    assertNull(pairBA.typeB);

    Set<JSType> setC = ImmutableSet.of(registry.getStringType(), registry.getStringType());
    UnionType unionC = new UnionType(registry, setC);

    TypePair pairCC = unionC.getTypesUnderShallowInequality(unionC);

    assertEquals(registry.getStringType(), pairCC.typeA);
    assertEquals(registry.getStringType(), pairCC.typeB);

    Set<JSType> setD = ImmutableSet.of(registry.getBooleanType(), registry.getBooleanType());
    UnionType unionD = new UnionType(registry, setD);

    TypePair pairDD = unionD.getTypesUnderShallowInequality(unionD);

    assertSame(registry.getBooleanType(), pairDD.typeA);
    assertSame(registry.getBooleanType(), pairDD.typeB);
  }

  @Test
  public void testUnionTypeVisit() {
    UnionTypeVisitor visitor = new UnionTypeVisitor();

    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    unionA.visit(visitor);

    assertTrue(visitor.boolUnionVisited);
    assertTrue(visitor.stringUnionVisited);
  }

  @Test
  public void testUnionTypeResolve() {
    Set<JSType> setA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionA = new UnionType(registry, setA);

    Set<JSType> setB = ImmutableSet.of(registry.getStringType(), registry.getFunctionType());
    UnionType unionB = new UnionType(registry, setB);

    UnionType resolvedA = unionA.resolve(null, null);
    UnionType resolvedB = unionB.resolve(null, null);

    assertEquals(registry.getObjectType(), resolvedA);
    assertEquals(registry.getFunctionType(), resolvedB);
  }
}

class UnionTypeVisitor implements Visitor<VoidType> {
  boolean boolUnionVisited = false;
  boolean stringUnionVisited = false;

  @Override
  public VoidType caseUnionType(UnionType unionType) {
    for (JSType alternate : unionType.getAlternates()) {
      alternate.visit(this);
      if (alternate instanceof BooleanType) {
        boolUnionVisited = true;
      } else if (alternate instanceof StringType) {
        stringUnionVisited = true;
      }
    }
    return null;
  }
}