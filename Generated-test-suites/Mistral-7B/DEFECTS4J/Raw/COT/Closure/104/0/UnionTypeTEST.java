package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {
  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = new HashSet<>(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE));
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), alternates);

    assertEquals("(String|Boolean)", unionType.toString());
    assertEquals(2, unionType.getAlternates().size());
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> alternates = new HashSet<>(Arrays.asList(StringType.INSTANCE, NumberType.INSTANCE));
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), alternates);

    assertFalse(unionType.canAssignTo(BooleanType.INSTANCE));
  }

  @Test
  public void testFindPropertyType() {
    Set<JSType> stringBooleanAlternates = new HashSet<>(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE));
    UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), stringBooleanAlternates);

    JSType stringPropertyType = stringBooleanUnionType.findPropertyType("length");
    assertNotNull(stringPropertyType);
    assertEquals(NumberType.INSTANCE, stringPropertyType);

    JSType booleanPropertyType = stringBooleanUnionType.findPropertyType("toString");
    assertNotNull(booleanPropertyType);
    assertEquals(FunctionType.create(Context.IDENTITY, StringType.INSTANCE, VoidType.INSTANCE), booleanPropertyType);
  }

  @Test(expected = AssertionError.class)
  public void testGetLeastSupertypeWithNullType() {
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), Arrays.asList(ObjectType.INSTANCE, NullType.INSTANCE));
    unionType.getLeastSupertype(NullType.INSTANCE);
  }

  @Test
  public void testIsSubtype() {
    Set<JSType> stringBooleanAlternates = new HashSet<>(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE));
    UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), stringBooleanAlternates);

    assertTrue(StringType.INSTANCE.isSubtype(stringBooleanUnionType));
    assertTrue(BooleanType.INSTANCE.isSubtype(stringBooleanUnionType));
    assertFalse(NumberType.INSTANCE.isSubtype(stringBooleanUnionType));
  }

  @Test
  public void testToString() {
    Set<JSType> alternates = new HashSet<>(Arrays.asList(StringType.INSTANCE, BooleanType.INSTANCE, NumberType.INSTANCE));
    UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), alternates);

    assertEquals("(String|Boolean|Number)", unionType.toString());
  }
}