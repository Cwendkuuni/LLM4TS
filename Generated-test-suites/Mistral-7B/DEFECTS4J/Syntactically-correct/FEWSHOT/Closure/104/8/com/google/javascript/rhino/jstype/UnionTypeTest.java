package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ImmutableSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSetBuilder;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.TypePair;
import com.google.javascript.rhino.jstype.UnionType.UnionTypeBuilder;

public class UnionTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testCreateUnionType() {
    Set<JSType> alternates = new HashSet<>();
    alternates.add(registry.forValue("boolean"));
    alternates.add(registry.forValue("String"));

    UnionType type = new UnionType(registry, alternates);
    assertEquals("(boolean|String)", type.toString());
  }

  @Test
  public void testUnionTypeGetAlternates() {
    UnionType type = createUnionType(registry, "Object", "Boolean");
    assertEquals(Arrays.asList(registry.forValue("Boolean"), registry.forValue("Object")), type.getAlternates());
  }

  @Test
  public void testUnionTypeGetAlternatesEmpty() {
    UnionType type = new UnionType(registry, ImmutableSet.of());
    assertTrue(type.getAlternates().isEmpty());
  }

  @Test
  public void testUnionTypeMatchesNumberContext() {
    UnionType type = createUnionType(registry, "Number", "String");
    assertFalse(type.matchesNumberContext());
  }

  @Test
  public void testUnionTypeMatchesStringContext() {
    UnionType type = createUnionType(registry, "String", "Number");
    assertFalse(type.matchesStringContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContext() {
    UnionType type = createUnionType(registry, "Object", "Object");
    assertTrue(type.matchesObjectContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContextEmpty() {
    UnionType type = new UnionType(registry, ImmutableSet.of());
    assertTrue(type.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    UnionType type = createUnionType(registry, "Object", "String");
    assertEquals(registry.forProperty("length"), type.findPropertyType("length"));
  }

  @Test
  public void testUnionTypeFindPropertyTypeNull() {
    UnionType type = new UnionType(registry, ImmutableSet.of(registry.forNull()));
    assertNull(type.findPropertyType("length"));
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    UnionType typeA = createUnionType(registry, "Number", "String");
    UnionType typeB = createUnionType(registry, "String", "Number");

    assertTrue(typeA.canAssignTo(registry.forValue(1)));
    assertTrue(typeA.canAssignTo(registry.forValue("")));
    assertFalse(typeA.canAssignTo(registry.forValue("foo")));
    assertFalse(typeA.canAssignTo(typeB));

    assertTrue(typeB.canAssignTo(registry.forValue("")));
    assertTrue(typeB.canAssignTo(registry.forValue(1)));
    assertFalse(typeB.canAssignTo(registry.forValue(true)));
    assertFalse(typeB.canAssignTo(typeA));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    UnionType typeA = createUnionType(registry, "Function", "String");
    UnionType typeB = createUnionType(registry, "String", "Function");

    assertTrue(typeA.canBeCalled());
    assertTrue(typeB.canBeCalled());
    assertFalse(typeA.canBeCalled(registry.forValue(1)));
    assertFalse(typeB.canBeCalled(registry.forValue("")));
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    UnionType typeA = createUnionType(registry, "Object", "Null");
    UnionType typeB = createUnionType(registry, "Null", "Object");

    UnionType restrictedA = typeA.restrictByNotNullOrUndefined();
    UnionType restrictedB = typeB.restrictByNotNullOrUndefined();

    assertEquals(registry.forObject(), restrictedA);
    assertEquals(registry.forNull(), restrictedB);
  }

  @Test
  public void testUnionTypeTestForEquality() {
    UnionType typeA = createUnionType(registry, "String", "Number");
    UnionType typeB = createUnionType(registry, "Number", "String");

    assertEquals(typeA, typeB);
  }

  @Test
  public void testUnionTypeTestForEqualityNull() {
    UnionType type = new UnionType(registry, ImmutableSet.of(registry.forNull()));
    assertEquals(type, registry.forNull());
  }

  @Test
  public void testUnionTypeIsNullable() {
    UnionType typeA = createUnionType(registry, "Number", "Null");
    UnionType typeB = createUnionType(registry, "Null", "Number");

    assertFalse(typeA.isNullable());
    assertTrue(typeB.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    UnionType typeA = createUnionType(registry, "Number", registry.forUnknownType());
    UnionType typeB = createUnionType(registry, registry.forUnknownType(), "Number");

    assertFalse(typeA.isUnknownType());
    assertTrue(typeB.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    UnionType typeA = createUnionType(registry, "Number", "String");
    UnionType typeB = createUnionType(registry, "String", "Number");
    UnionType typeC = registry.forValue("Object");

    assertEquals(typeC, typeA.getLeastSupertype(typeC));
    assertEquals(typeC, typeB.getLeastSupertype(typeC));
    assertEquals(typeB, typeA.getLeastSupertype(typeB));
    assertEquals(typeA, typeB.getLeastSupertype(typeA));

    assertEquals(registry.forObject(), typeA.getLeastSupertype(registry.forObject()));
    assertEquals(registry.forObject(), typeB.getLeastSupertype(registry.forObject()));

    JSType unknown = registry.forUnknownType();
    assertEquals(unknown, typeA.getLeastSupertype(unknown));
    assertEquals(unknown, typeB.getLeastSupertype(unknown));
  }

  @Test
  public void testUnionTypeMeet() {
    UnionType typeA = createUnionType(registry, "String", "Number");
    UnionType typeB = createUnionType(registry, "Number", "String");

    UnionType result = typeA.meet(typeB);
    assertEquals(registry.forValue("String|Number"), result.toString());
  }

  @Test
  public void testUnionTypeToString() {
    UnionType type = createUnionType(registry, "Number", "String");
    assertEquals("(Number|String)", type.toString());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    UnionType typeA = createUnionType(registry, "Number", "String");
    UnionType typeB = createUnionType(registry, "String", "Number");
    UnionType typeC = registry.forValue("Object");

    assertTrue(typeA.isSubtype(typeB));
    assertTrue(typeB.isSubtype(typeA));
    assertTrue(typeC.isSubtype(typeA));
    assertTrue(typeC.isSubtype(typeB));

    assertFalse(registry.forValue("Boolean").isSubtype(typeA));
    assertFalse(registry.forValue("Boolean").isSubtype(typeB));

    assertFalse(typeA.isSubtype(registry.forValue("Boolean")));
    assertFalse(typeB.isSubtype(registry.forValue("Boolean")));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    UnionType typeA = createUnionType(registry, "Object", "Number");
    UnionType typeB = registry.forValue("Number");

    assertEquals(registry.forValue("Object"), typeA.getRestrictedUnion(typeB));
  }

  @Test
  public void testUnionTypeIsUnionType() {
    UnionType type = new UnionType(registry, ImmutableSet.of());
    assertTrue(type.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    UnionType type = createUnionType(registry, "Object", "String");
    assertTrue(type.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    UnionType type = createUnionType(registry, "Object", "String");

    assertTrue(type.contains(registry.forValue("Object")));
    assertTrue(type.contains(registry.forValue("String")));
    assertFalse(type.contains(registry.forValue("Number")));
  }

  @Test
  public void testUnionTypeEquals() {
    UnionType typeA = createUnionType(registry, "Object", "String");
    UnionType typeB = createUnionType(registry, "String", "Object");

    assertTrue(typeA.equals(typeB));
  }

  @Test
  public void testUnionTypeHashCode() {
    UnionType typeA = createUnionType(registry, "Object", "String");
    UnionType typeB = new UnionType(registry, ImmutableSet.of(registry.forValue("String"), registry.forValue("Object")));

    assertEquals(typeA.hashCode(), typeB.hashCode());
  }

  @Test
  public void testUnionTypeVisit() {
    UnionType type = createUnionType(registry, "Object", "String");

    TypePair expected = new TypePair(registry.forValue("Object"), registry.forValue("String"));
    TypePair result = type.visit(new UnionTypeVisitor<TypePair>() {
      @Override
      public TypePair caseUnionType(UnionType unionType) {
        return expected;
      }
    });

    assertSame(expected, result);
  }

  @Test
  public void testUnionTypeResolveInternal() {
    UnionType typeA = createUnionType(registry, "Object", "Object");
    JSType resolvedTypeA = typeA.resolve(registry.getErrorReporter(), registry.forStaticScope(registry));

    UnionType typeB = new UnionType(registry, ImmutableSet.of(registry.forValue("Function")));
    JSType resolvedTypeB = typeB.resolve(registry.getErrorReporter(), registry.forStaticScope(registry));

    assertEquals(typeA, resolvedTypeA);
    assertEquals(typeB, resolvedTypeB);
  }

  private UnionType createUnionType(JSTypeRegistry registry, String... types) {
    ImmutableSetBuilder<JSType> setBuilder = new ImmutableSetBuilder<JSType>();
    for (String typeName : types) {
      JSType type = registry.getOrCreate(typeName);
      setBuilder.add(type);
    }
    return new UnionType(registry, setBuilder.build());
  }

  private static class UnionTypeVisitor<T> {
    abstract T caseUnionType(UnionType unionType);
  }
}