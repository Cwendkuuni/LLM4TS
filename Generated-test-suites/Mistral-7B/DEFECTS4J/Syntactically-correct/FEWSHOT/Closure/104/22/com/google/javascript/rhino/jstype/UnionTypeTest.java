package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.TernaryValue.UNKNOWN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class UnionTypeTest {

  private static final UnionType INT_OR_STRING = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.STRING));
  private static final UnionType INT_OR_BOOL = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
  private static final UnionType BOOL_OR_INT = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.BOOLEAN, JSType.INT));

  @Test
  public void testGetAlternates() {
    assertEquals(ImmutableSet.of(JSType.INT, JSType.STRING), INT_OR_STRING.getAlternates());
    assertEquals(ImmutableSet.of(JSType.INT, JSType.BOOLEAN), INT_OR_BOOL.getAlternates());
    assertEquals(ImmutableSet.of(JSType.BOOLEAN, JSType.INT), BOOL_OR_INT.getAlternates());
  }

  @Test
  public void testCanAssignTo() {
    assertTrue(INT_OR_STRING.canAssignTo(JSType.INT));
    assertTrue(INT_OR_STRING.canAssignTo(JSType.STRING));
    assertTrue(INT_OR_BOOL.canAssignTo(JSType.INT));
    assertTrue(INT_OR_BOOL.canAssignTo(JSType.BOOLEAN));
    assertFalse(JSType.STRING.canAssignTo(INT_OR_STRING));
    assertFalse(JSType.BOOLEAN.canAssignTo(INT_OR_BOOL));
  }

  @Test
  public void testMatchesNumberContext() {
    assertTrue(INT_OR_STRING.matchesNumberContext());
    assertTrue(INT_OR_BOOL.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    assertTrue(INT_OR_STRING.matchesStringContext());
    assertTrue(INT_OR_BOOL.matchesStringContext());
    assertTrue(BOOL_OR_INT.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    assertTrue(INT_OR_STRING.matchesObjectContext());
    assertTrue(INT_OR_BOOL.matchesObjectContext());
    assertTrue(BOOL_OR_INT.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    JSType intProperty = JSType.INT.asInstance();
    JSType stringProperty = JSType.STRING.asInstance();
    JSType booleanProperty = JSType.BOOLEAN.asInstance();

    assertEquals(intProperty, INT_OR_STRING.findPropertyType("length"));
    assertNull(INT_OR_STRING.findPropertyType("toString"));
    assertEquals(stringProperty, INT_OR_STRING.findPropertyType("toString"));
    assertEquals(booleanProperty, INT_OR_BOOL.findPropertyType("toString"));
    assertNull(INT_OR_BOOL.findPropertyType("length"));
  }

  @Test
  public void testIsNullable() {
    assertFalse(INT_OR_STRING.isNullable());
    assertFalse(INT_OR_BOOL.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    assertFalse(INT_OR_STRING.isUnknownType());
    assertFalse(INT_OR_BOOL.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    assertEquals(INT_OR_STRING, INT_OR_STRING.getLeastSupertype(INT_OR_STRING));
    assertEquals(INT_OR_BOOL, INT_OR_BOOL.getLeastSupertype(INT_OR_BOOL));
    assertEquals(INT_OR_STRING, INT_OR_STRING.getLeastSupertype(INT_OR_BOOL));
    assertEquals(INT_OR_BOOL, INT_OR_STRING.getLeastSupertype(INT_OR_BOOL));
    assertSame(JSType.OBJECT, INT_OR_STRING.getLeastSupertype(JSType.OBJECT));
  }

  @Test
  public void testMeet() {
    JSType intOrBoolOrNull = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN, JSType.NULL));
    assertEquals(intOrBoolOrNull, INT_OR_BOOL.meet(JSType.NULL));
    assertEquals(INT_OR_BOOL, INT_OR_BOOL.meet(INT_OR_BOOL));
    assertSame(JSType.OBJECT, INT_OR_BOOL.meet(JSType.OBJECT));
  }

  @Test
  public void testEqualsHashcode() {
    UnionType intOrString1 = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intOrString2 = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.STRING));
    UnionType intOrBool = createUnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));

    assertTrue(intOrString1.equals(intOrString2));
    assertTrue(intOrString1.hashCode() == intOrString2.hashCode());
    assertFalse(intOrString1.equals(intOrBool));
    assertFalse(intOrString1.hashCode() == intOrBool.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("(int|string)", INT_OR_STRING.toString());
    assertEquals("(int|boolean)", INT_OR_BOOL.toString());
    assertEquals("(boolean|int)", BOOL_OR_INT.toString());
  }

  @Test
  public void testIsSubtype() {
    assertTrue(INT_OR_STRING.isSubtype(INT_OR_STRING));
    assertTrue(INT_OR_STRING.isSubtype(INT_OR_BOOL));
    assertTrue(INT_OR_BOOL.isSubtype(INT_OR_STRING));
    assertTrue(INT_OR_BOOL.isSubtype(BOOL_OR_INT));
    assertTrue(BOOL_OR_INT.isSubtype(INT_OR_BOOL));
    assertTrue(BOOL_OR_INT.isSubtype(BOOL_OR_INT));
    assertFalse(INT_OR_STRING.isSubtype(JSType.OBJECT));
  }

  @Test
  public void testGetRestrictedUnion() {
    assertEquals(JSType.STRING, INT_OR_STRING.getRestrictedUnion(JSType.STRING));
    assertEquals(JSType.INT, INT_OR_STRING.getRestrictedUnion(JSType.INT));
    assertSame(JSType.OBJECT, INT_OR_STRING.getRestrictedUnion(JSType.OBJECT));

    assertEquals(JSType.BOOLEAN, INT_OR_BOOL.getRestrictedUnion(JSType.BOOLEAN));
    assertEquals(JSType.INT, INT_OR_BOOL.getRestrictedUnion(JSType.INT));
    assertSame(JSType.OBJECT, INT_OR_BOOL.getRestrictedUnion(JSType.OBJECT));
  }

  private static UnionType createUnionType(JSTypeRegistry registry, ImmutableSet<JSType> alternates) {
    return new UnionType(registry, alternates);
  }
}