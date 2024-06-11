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
import com.google.javascript.rhino.UnionTypeBuilder;
import com.google.javascript.rhino.visitor.Visitor;

import java.util.Set;

public class UnionTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testCreateUnionType() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    Set<JSType> alternatesB = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("String"));

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    assertTrue(unionTypeA.equals(unionTypeA));
    assertTrue(unionTypeA.equals(unionTypeA));
    assertFalse(unionTypeA.equals(null));
    assertFalse(unionTypeA.equals(registry.findTypeByName("Boolean")));
    assertFalse(unionTypeA.equals(registry.findTypeByName("CustomType")));
    assertTrue(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    UnionType sameUnionType = new UnionType(registry, alternates);
    UnionType differentUnionType = new UnionType(registry, ImmutableSet.of(registry.findTypeByName("Boolean")));

    assertEquals(unionType.hashCode(), sameUnionType.hashCode());
    assertNotEquals(unionType.hashCode(), differentUnionType.hashCode());
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(String|Number)", unionType.toString());
  }

  @Test
  public void testUnionTypeMatchesNumberContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("String"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testUnionTypeMatchesStringContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals(registry.findTypeByName("String"), unionType.findPropertyType("property"));
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.canAssignTo(registry.findTypeByName("String")));
    assertTrue(unionType.canAssignTo(registry.findTypeByName("Number")));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Function"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Null"));
    UnionType unionType = new UnionType(registry, alternates);

    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();

    assertNotNull(restrictedUnionType);
    assertTrue(restrictedUnionType.isSubtype(registry.findTypeByName("String")));
    assertFalse(restrictedUnionType.isSubtype(registry.findTypeByName("Null")));
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    Set<JSType> alternatesB = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("String"));

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    assertEquals(unionTypeA, unionTypeB);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals(registry.findTypeByName("Object"), unionType.getLeastSupertype(registry.findTypeByName("Object")));
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    Set<JSType> alternatesB = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("Boolean"));

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    UnionType meetType = unionTypeA.meet(unionTypeB);

    assertNotNull(meetType);
    assertTrue(meetType.isSubtype(registry.findTypeByName("Number")));
    assertFalse(meetType.isSubtype(registry.findTypeByName("Boolean")));
  }

  @Test
  public void testUnionTypeEqualsGivenNull() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.equals(null));
  }

  @Test
  public void testUnionTypeEqualsGivenCustomType() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.equals(new Object()));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"), registry.findTypeByName("Boolean"));
    UnionType unionType = new UnionType(registry, alternates);

    UnionType restrictedUnionType = unionType.getRestrictedUnion(registry.findTypeByName("Number"));

    assertNotNull(restrictedUnionType);
    assertTrue(restrictedUnionType.isSubtype(registry.findTypeByName("String")));
    assertFalse(restrictedUnionType.isSubtype(registry.findTypeByName("Boolean")));
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Boolean"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.getRestrictedTypeGivenToBooleanOutcome(true).isSubtype(registry.findTypeByName("Boolean")));
    assertTrue(unionType.getRestrictedTypeGivenToBooleanOutcome(false).isSubtype(registry.findTypeByName("String")));
  }

  @Test
  public void testUnionTypeGetPossibleToBooleanOutcomes() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Boolean"));
    UnionType unionType = new UnionType(registry, alternates);

    BooleanLiteralSet outcomes = unionType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, outcomes);
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    Set<JSType> alternatesB = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("String"));

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    TypePair pair = unionTypeA.getTypesUnderEquality(unionTypeB);

    assertSame(unionTypeB, pair.typeB);
    assertSame(unionTypeA, pair.typeA);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    Set<JSType> alternatesB = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("String"));

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    TypePair pair = unionTypeA.getTypesUnderInequality(unionTypeB);

    assertNull(pair.typeA);
    assertSame(registry.findTypeByName("String"), pair.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    Set<JSType> alternatesB = ImmutableSet.of(registry.findTypeByName("Number"), registry.findTypeByName("String"));

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    TypePair pair = unionTypeA.getTypesUnderShallowInequality(unionTypeB);

    assertNull(pair.typeA);
    assertSame(registry.findTypeByName("String"), pair.typeB);
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isSubtype(registry.findTypeByName("String")));
    assertTrue(unionType.isSubtype(registry.findTypeByName("Number")));
    assertFalse(unionType.isSubtype(registry.findTypeByName("Boolean")));
  }

  @Test
  public void testUnionTypeIsSubtypeGivenUnknownType() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    JSType unknownType = registry.findTypeByName("UnknownType");

    assertFalse(unionType.isSubtype(unknownType));
  }

  @Test
  public void testUnionTypeVisit() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Number"));
    UnionType unionType = new UnionType(registry, alternates);

    Visitor visitor = new Visitor() {
      @Override
      public <T> T visitUnionType(UnionType type) {
        assertEquals(alternates, type.getAlternates());
        return null;
      }
    };

    visitor.visit(unionType);
  }

  @Test
  public void testUnionTypeResolveInternal() {
    Set<JSType> alternates = ImmutableSet.of(registry.findTypeByName("String"), registry.findTypeByName("Function"));
    UnionType unionType = new UnionType(registry, alternates);

    ErrorReporter reporter = new ErrorReporter() {};
    StaticScope<JSType> scope = new StaticScope<>();

    UnionType resolvedType = unionType.resolveInternal(reporter, scope);

    assertSame(unionType, resolvedType);
  }
}