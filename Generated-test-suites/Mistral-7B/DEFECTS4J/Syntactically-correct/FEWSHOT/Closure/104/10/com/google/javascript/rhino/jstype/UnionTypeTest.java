package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testEquals() {
    UnionType t1 = createUnionType(Collections.singleton(StringType.INSTANCE));
    UnionType t2 = createUnionType(Collections.singleton(StringType.INSTANCE));
    UnionType t3 = createUnionType(Collections.singleton(NumberType.INSTANCE));

    assertEquals(t1, t1);
    assertEquals(t1, t2);

    assertFalse(t1.equals(t3));
    assertFalse(t3.equals(t1));

    UnionType t4 = createUnionType(Collections.singleton(BooleanType.INSTANCE));
    assertFalse(t1.equals(t4));
    assertFalse(t4.equals(t1));
  }

  @Test
  public void testHashCode() {
    UnionType t1 = createUnionType(Collections.singleton(StringType.INSTANCE));
    UnionType t2 = createUnionType(Collections.singleton(StringType.INSTANCE));
    UnionType t3 = createUnionType(Collections.singleton(NumberType.INSTANCE));

    int h1 = t1.hashCode();
    int h2 = t2.hashCode();
    int h3 = t3.hashCode();

    assertEquals(h1, h1);
    assertEquals(h1, h2); // equals should imply same hashcode
    assertNotSame(h1, h3);
  }

  @Test
  public void testIsSubtype() {
    UnionType t1 = createUnionType(Collections.singleton(StringType.INSTANCE));
    UnionType t2 = createUnionType(Collections.singleton(NumberType.INSTANCE));
    UnionType t3 = createUnionType(Collections.singleton(BooleanType.INSTANCE));

    assertFalse(t2.isSubtype(t1));
    assertFalse(t1.isSubtype(t2));
    assertTrue(t1.isSubtype(t1));
    assertTrue(t3.isSubtype(t3));
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(NumberType.INSTANCE, StringType.INSTANCE));
    UnionType t3 = createUnionType(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE));
    UnionType t4 = createUnionType(Arrays.asList(NumberType.INSTANCE, BooleanType.INSTANCE));

    assertEquals(StringType.INSTANCE, t1.getLeastSupertype(StringType.INSTANCE));
    assertEquals(NumberType.INSTANCE, t1.getLeastSupertype(NumberType.INSTANCE));
    assertEquals(StringType.INSTANCE, t1.getLeastSupertype(BooleanType.INSTANCE));
    assertSame(t1, t1.getLeastSupertype(t1));

    assertEquals(StringType.INSTANCE, t2.getLeastSupertype(StringType.INSTANCE));
    assertEquals(NumberType.INSTANCE, t2.getLeastSupertype(NumberType.INSTANCE));
    assertEquals(ObjectType.INSTANCE, t2.getLeastSupertype(BooleanType.INSTANCE));
    assertNull(t2.getLeastSupertype(VoidType.INSTANCE));

    assertEquals(StringType.INSTANCE, t3.getLeastSupertype(StringType.INSTANCE));
    assertEquals(ObjectType.INSTANCE, t3.getLeastSupertype(BooleanType.INSTANCE));
    assertEquals(ObjectType.INSTANCE, t3.getLeastSupertype(StringType.INSTANCE));
    assertSame(t3, t3.getLeastSupertype(t3));

    assertEquals(NumberType.INSTANCE, t4.getLeastSupertype(NumberType.INSTANCE));
    assertEquals(ObjectType.INSTANCE, t4.getLeastSupertype(BooleanType.INSTANCE));
    assertEquals(ObjectType.INSTANCE, t4.getLeastSupertype(NumberType.INSTANCE));
    assertSame(t4, t4.getLeastSupertype(t4));
  }

  @Test
  public void testFindPropertyType() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));

    assertNull(t1.findPropertyType("length"));
    assertNull(t1.findPropertyType("toString"));

    StringType stringAlternate = (StringType) t1.getAlternates().iterator().next();
    assertEquals(StringType.INSTANCE, stringAlternate.findPropertyType("length"));
    assertEquals(StringType.INSTANCE, stringAlternate.findPropertyType("toString"));
  }

  @Test
  public void testCanAssignTo() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(NumberType.INSTANCE, StringType.INSTANCE));
    UnionType t3 = createUnionType(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE));

    assertTrue(t1.canAssignTo(StringType.INSTANCE));
    assertTrue(t1.canAssignTo(NumberType.INSTANCE));
    assertFalse(t1.canAssignTo(BooleanType.INSTANCE));

    assertTrue(t2.canAssignTo(StringType.INSTANCE));
    assertTrue(t2.canAssignTo(NumberType.INSTANCE));
    assertTrue(t2.canAssignTo(BooleanType.INSTANCE));

    assertFalse(t3.canAssignTo(StringType.INSTANCE));
    assertFalse(t3.canAssignTo(NumberType.INSTANCE));
    assertFalse(t3.canAssignTo(BooleanType.INSTANCE));
  }

  @Test
  public void testCanBeCalled() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(StringType.INSTANCE, FunctionType.INSTANCE));

    assertTrue(t1.canBeCalled());
    assertTrue(t2.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));

    UnionType t2 = t1.restrictByNotNullOrUndefined();
    assertTrue(t2.isSubtype(StringType.INSTANCE));
    assertTrue(t2.isSubtype(NumberType.INSTANCE));
    assertFalse(t2.isSubtype(VoidType.INSTANCE));
    assertFalse(t2.isSubtype(NullType.INSTANCE));

    UnionType t3 = t2.restrictByNotNullOrUndefined();
    assertTrue(t3.isSubtype(StringType.INSTANCE));
    assertTrue(t3.isSubtype(NumberType.INSTANCE));
    assertTrue(t3.isSubtype(VoidType.INSTANCE));
    assertTrue(t3.isSubtype(NullType.INSTANCE));

    assertEquals(t3, t1.restrictByNotNullOrUndefined());
  }

  @Test
  public void testMeet() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(NumberType.INSTANCE, StringType.INSTANCE));

    UnionType t3 = t1.meet(t2);
    assertTrue(t3.isSubtype(StringType.INSTANCE));
    assertTrue(t3.isSubtype(NumberType.INSTANCE));

    UnionType t4 = createUnionType(Arrays.asList(ObjectType.INSTANCE, StringType.INSTANCE));
    UnionType t5 = createUnionType(Arrays.asList(StringType.INSTANCE, ObjectType.INSTANCE));

    UnionType t6 = t4.meet(t5);
    assertTrue(t6.isSubtype(ObjectType.INSTANCE));
    assertTrue(t6.isSubtype(StringType.INSTANCE));

    UnionType t7 = createUnionType(Arrays.asList(VoidType.INSTANCE, ObjectType.INSTANCE));
    UnionType t8 = createUnionType(Arrays.asList(ObjectType.INSTANCE, VoidType.INSTANCE));

    UnionType t9 = t7.meet(t8);
    assertNull(t9);
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE));

    UnionType t3 = t1.getRestrictedUnion(StringType.INSTANCE);
    assertEquals(StringType.INSTANCE, t3);

    UnionType t4 = t1.getRestrictedUnion(NumberType.INSTANCE);
    assertEquals(NumberType.INSTANCE, t4);

    UnionType t5 = t2.getRestrictedUnion(StringType.INSTANCE);
    assertEquals(StringType.INSTANCE, t5);

    UnionType t6 = t2.getRestrictedUnion(BooleanType.INSTANCE);
    assertEquals(BooleanType.INSTANCE, t6);

    UnionType t7 = t2.getRestrictedUnion(NumberType.INSTANCE);
    assertNull(t7);
  }

  @Test
  public void testToString() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(BooleanType.INSTANCE, NumberType.INSTANCE));
    UnionType t3 = createUnionType(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE, NumberType.INSTANCE));

    assertEquals("(String|Number)", t1.toString());
    assertEquals("(Boolean|Number)", t2.toString());
    assertEquals("(String|Boolean|Number)", t3.toString());
  }

  @Test
  public void testIsObject() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType t2 = createUnionType(Arrays.asList(BooleanType.INSTANCE, NumberType.INSTANCE));
    UnionType t3 = createUnionType(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE, NumberType.INSTANCE));

    assertTrue(t1.isObject());
    assertTrue(t2.isObject());
    assertTrue(t3.isObject());
  }

  @Test
  public void testIsUnionType() {
    UnionType t1 = createUnionType(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));

    assertTrue(t1.isUnionType());
  }

  private static UnionType createUnionType(Set<JSType> alternates) {
    return new UnionType(JSTypeRegistry.INSTANCE, new HashSet<>(alternates));
  }
}