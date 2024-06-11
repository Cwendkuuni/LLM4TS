package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.TernaryValue.UNKNOWN;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.ImmutableSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeWithTwoAlternates() {
    Set<JSType> alternates = ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE);
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);

    assertTrue(unionType.isUnionType());

    assertEquals(2, unionType.getAlternates().size());

    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertEquals(1, restrictedType.getAlternates().size());
    assertTrue(restictedType.isNullType());

    TypePair typePair = unionType.getTypesUnderEquality(StringType.INSTANCE);
    assertEquals(unionType, typePair.typeA);
    assertEquals(StringType.INSTANCE, typePair.typeB);

    typePair = unionType.getTypesUnderInequality(BooleanType.INSTANCE);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    typePair = unionType.getTypesUnderShallowInequality(NumberType.INSTANCE);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    assertEquals(unionType, unionType.visit(new Visitor<Void>() {}));

    UnionType resolvedType = new UnionType(JSTypeRegistry.INSTANCE, alternates).resolve(null, null);
    assertEquals(unionType, resolvedType);
  }

  @Test
  public void testUnionTypeWithThreeAlternates() {
    Set<JSType> alternates = ImmutableSet.of(
        StringType.INSTANCE,
        BooleanType.INSTANCE,
        NumberType.INSTANCE);
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);

    assertTrue(unionType.isUnionType());

    assertEquals(3, unionType.getAlternates().size());

    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertEquals(2, restrictedType.getAlternates().size());
    assertFalse(restictedType.isNullType());
    assertTrue(restictedType.getAlternates().contains(StringType.INSTANCE));
    assertTrue(restictedType.getAlternates().contains(BooleanType.INSTANCE));

    TypePair typePair = unionType.getTypesUnderEquality(NumberType.INSTANCE);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    typePair = unionType.getTypesUnderInequality(StringType.INSTANCE);
    assertEquals(unionType, typePair.typeA);
    assertEquals(StringType.INSTANCE, typePair.typeB);

    typePair = unionType.getTypesUnderShallowInequality(BooleanType.INSTANCE);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    assertEquals(unionType, unionType.visit(new Visitor<Void>() {}));

    UnionType resolvedType = new UnionType(JSTypeRegistry.INSTANCE, alternates).resolve(null, null);
    assertEquals(unionType, resolvedType);
  }

  @Test
  public void testUnionTypeWithNullAlternate() {
    Set<JSType> alternates = ImmutableSet.of(
        StringType.INSTANCE,
        null,
        NumberType.INSTANCE);
    try {
      new UnionType(JSTypeRegistry.INSTANCE, alternates);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("UnionType cannot contain null alternates", e.getMessage());
    }
  }

  @Test
  public void testUnionTypeWithEmptyAlternates() {
    Set<JSType> alternates = ImmutableSet.of();
    try {
      new UnionType(JSTypeRegistry.INSTANCE, alternates);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("UnionType cannot be empty", e.getMessage());
    }
  }

  @Test
  public void testUnionTypeWithSameAlternateTwice() {
    Set<JSType> alternates = ImmutableSet.of(
        StringType.INSTANCE,
        StringType.INSTANCE);
    try {
      new UnionType(JSTypeRegistry.INSTANCE, alternates);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("UnionType cannot contain duplicate alternates", e.getMessage());
    }
  }

  @Test
  public void testUnionTypeWithCircularDependencies() {
    Set<JSType> alternates = ImmutableSet.of(
        new MyCircularTypeA(),
        new MyCircularTypeB());
    try {
      new UnionType(JSTypeRegistry.INSTANCE, alternates);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("UnionType cannot have circular dependencies", e.getMessage());
    }
  }

  @Test
  public void testUnionTypeWithNestedUnionTypes() {
    Set<JSType> outerAlternates = ImmutableSet.of(
        StringType.INSTANCE,
        new UnionType(JSTypeRegistry.INSTANCE, ImmutableSet.of(
            BooleanType.INSTANCE,
            NumberType.INSTANCE)));
    UnionType outerUnionType = new UnionType(JSTypeRegistry.INSTANCE, outerAlternates);

    assertEquals(2, outerUnionType.getAlternates().size());

    UnionType innerUnionType = (UnionType) outerUnionType.getAlternates().toArray()[0];
    assertEquals(2, innerUnionType.getAlternates().size());

    assertTrue(outerUnionType.isSubtype(innerUnionType));
    assertTrue(innerUnionType.isSubtype(outerUnionType));
  }

  @Test
  public void testUnionTypeWithSubtypes() {
    Set<JSType> alternates = ImmutableSet.of(
        StringType.INSTANCE,
        new StringOrNumberType());
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);

    assertEquals(2, unionType.getAlternates().size());

    assertTrue(StringType.INSTANCE.isSubtype(unionType));
    assertTrue(StringOrNumberType.INSTANCE.isSubtype(unionType));

    assertFalse(NumberType.INSTANCE.isSubtype(unionType));
  }

  @Test
  public void testUnionTypeWithSupertypes() {
    Set<JSType> alternates = ImmutableSet.of(
        new StringOrNumberType(),
        StringType.INSTANCE);
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);

    assertEquals(2, unionType.getAlternates().size());

    assertTrue(StringOrNumberType.INSTANCE.isSupertype(unionType));
    assertTrue(StringType.INSTANCE.isSupertype(unionType));

    assertFalse(BooleanType.INSTANCE.isSupertype(unionType));
  }

  @Test
  public void testUnionTypeWithUnknownTypeAlternate() {
    Set<JSType> alternates = ImmutableSet.of(
        new UnionType(JSTypeRegistry.INSTANCE, ImmutableSet.of(UnknownType.INSTANCE)));
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);

    assertEquals(1, unionType.getAlternates().size());

    assertTrue(unionType.isUnknownType());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertTrue(restictedType.isNullType());
  }

  @Test
  public void testUnionTypeWithMixedAlternates() {
    Set<JSType> alternates = ImmutableSet.of(
        BooleanType.INSTANCE,
        StringType.INSTANCE,
        NumberType.INSTANCE);
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);

    assertEquals(3, unionType.getAlternates().size());

    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertEquals(2, restrictedType.getAlternates().size());
    assertFalse(restictedType.isNullType());
    assertTrue(restictedType.getAlternates().contains(BooleanType.INSTANCE));
    assertTrue(restictedType.getAlternates().contains(StringType.INSTANCE));

    TypePair typePair = unionType.getTypesUnderEquality(NumberType.INSTANCE);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    typePair = unionType.getTypesUnderInequality(BooleanType.INSTANCE);
    assertEquals(unionType, typePair.typeA);
    assertEquals(BooleanType.INSTANCE, typePair.typeB);

    typePair = unionType.getTypesUnderShallowInequality(StringType.INSTANCE);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    assertEquals(unionType, unionType.visit(new Visitor<Void>() {}));

    UnionType resolvedType = new UnionType(JSTypeRegistry.INSTANCE, alternates).resolve(null, null);
    assertEquals(unionType, resolvedType);
  }

  private static class MyCircularTypeA extends UnionType {
    MyCircularTypeA(JSTypeRegistry registry) {
      super(registry, ImmutableSet.of(MyCircularTypeB.INSTANCE, this));
    }
  }

  private static class MyCircularTypeB extends UnionType {
    MyCircularTypeB(JSTypeRegistry registry) {
      super(registry, ImmutableSet.of(MyCircularTypeA.INSTANCE, this));
    }
  }

  private static class StringOrNumberType extends UnionType {
    StringOrNumberType(JSTypeRegistry registry) {
      super(registry, ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
    }
  }
}