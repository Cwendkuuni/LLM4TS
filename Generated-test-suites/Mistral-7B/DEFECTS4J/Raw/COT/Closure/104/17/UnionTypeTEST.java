package com.google.javascript.rhino.jstype;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class UnionTypeTest {

  @Test
  public void testCreateUnionType() {
    Set<JSType> set1 = ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER));
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), set1);
    assertEquals("(String|Number)", unionType1.toString());

    Set<JSType> set2 = ImmutableSet.of(new NumberType(Type.NUMBER), new BooleanType(Type.BOOLEAN));
    UnionType unionType2 = new UnionType(new JSTypeRegistry(), set2);
    assertEquals("(Number|Boolean)", unionType2.toString());
  }

  @Test
  public void testGetAlternates() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    Set<JSType> alternates = unionType.getAlternates();
    assertEquals(2, alternates.size());
    assertTrue(alternates.contains(new StringType(Type.STRING)));
    assertTrue(alternates.contains(new NumberType(Type.NUMBER)));
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertTrue(unionType1.matchesNumberContext());

    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new BooleanType(Type.BOOLEAN)));
    assertFalse(unionType2.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertTrue(unionType1.matchesStringContext());

    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new BooleanType(Type.BOOLEAN)));
    assertTrue(unionType2.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertTrue(unionType1.matchesObjectContext());

    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new BooleanType(Type.BOOLEAN)));
    assertTrue(unionType2.matchesObjectContext());
  }

  @Test(expected = AssertionError.class)
  public void testFindPropertyTypeUnknownProperty() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    String propertyName = "unknownProperty";
    JSType result = unionType.findPropertyType(propertyName);
    assertNull(result);
  }

  @Test
  public void testFindPropertyTypePropertyInFirstAlternate() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new StringType(Type.STRING)));
    String propertyName = "property";
    JSType result = unionType.findPropertyType(propertyName);
    assertEquals(new StringType(Type.STRING), result);
  }

  @Test
  public void testFindPropertyTypePropertyInSecondAlternate() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NumberType(Type.NUMBER), new StringType(Type.STRING)));
    String propertyName = "property";
    JSType result = unionType.findPropertyType(propertyName);
    assertEquals(new StringType(Type.STRING), result);
  }

  @Test
  public void testCanAssignTo() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    JSType that = new NumberType(Type.NUMBER);
    assertTrue(unionType1.canAssignTo(that));

    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new BooleanType(Type.BOOLEAN)));
    assertFalse(unionType2.canAssignTo(that));
  }

  @Test
  public void testCanBeCalled() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new FunctionType(ImmutableSet.of(), new VoidType(Type.VOID))));
    assertTrue(unionType.canBeCalled());
  }

  @Test(expected = AssertionError.class)
  public void testCanBeCalledFunctionType() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new FunctionType(ImmutableSet.of(), new VoidType(Type.VOID)), new StringType(Type.STRING)));
    assertFalse(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NullType(Type.NULL), new NumberType(Type.NUMBER)));
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals(ImmutableSet.of(new NumberType(Type.NUMBER)), restricted.getAlternates());
  }

  @Test
  public void testTestForEquality() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NumberType(Type.NUMBER), new StringType(Type.STRING)));
    TernaryValue result = unionType1.testForEquality(unionType2);
    assertEquals(TernaryValue.UNKNOWN, result);

    UnionType unionType3 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NumberType(Type.NUMBER), new NumberType(Type.NUMBER)));
    result = unionType1.testForEquality(unionType3);
    assertEquals(TernaryValue.TRUE, result);
  }

  @Test
  public void testIsNullable() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NullType(Type.NULL), new NumberType(Type.NUMBER)));
    assertTrue(unionType1.isNullable());

    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new BooleanType(Type.BOOLEAN)));
    assertFalse(unionType2.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NumberType(Type.NUMBER), new StringType(Type.STRING)));
    UnionType result = unionType1.getLeastSupertype(unionType2);
    assertEquals(unionType1, result);

    UnionType superType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER), new BooleanType(Type.BOOLEAN)));
    result = unionType1.getLeastSupertype(superType);
    assertEquals(superType, result);
  }

  @Test
  public void testMeet() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NumberType(Type.NUMBER), new BooleanType(Type.BOOLEAN)));
    UnionType result = unionType1.meet(unionType2);
    assertEquals(new UnionType(new JSTypeRegistry(), ImmutableSet.of(new NumberType(Type.NUMBER))), result);
  }

  @Test
  public void testEquals() {
    UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertTrue(unionType1.equals(unionType2));
    assertTrue(unionType1.hashCode() == unionType2.hashCode());

    UnionType unionType3 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new BooleanType(Type.BOOLEAN)));
    assertFalse(unionType1.equals(unionType3));
  }

  @Test
  public void testIsUnionType() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    JSType alternate = new StringType(Type.STRING);
    assertTrue(unionType.contains(alternate));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(new StringType(Type.STRING), new NumberType(Type.NUMBER)));
    JSType type = new StringType(Type.STRING);
    UnionType result = unionType.getRestrictedUnion(type);
    assertEquals(new StringType(Type.STRING), result);
  }
}