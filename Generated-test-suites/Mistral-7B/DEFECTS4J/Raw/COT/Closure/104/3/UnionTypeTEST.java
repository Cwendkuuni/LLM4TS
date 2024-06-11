import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.*;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals("(boolean|string)", unionType.toString());
  }

  @Test
  public void testGetAlternates() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testForgiveUnknownNames() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    unionType.forgiveUnknownNames();

    for (JSType alternate : alternates) {
      alternate.forgiveUnknownNames();
    }
  }

  @Test
  public void testMatchesNumberContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNumberType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    Set<JSType> alternates = ImmutableSet.of(
        registry.getObjectType().getMemberType("foo"),
        registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    JSType propertyType = unionType.findPropertyType("foo");
    assertNotNull(propertyType);
    assertEquals(registry.getObjectType().getMemberType("foo"), propertyType);
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);

    JSType that = registry.getStringType();
    assertTrue(unionType.canAssignTo(that));
  }

  @Test
  public void testCanBeCalled() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = ImmutableSet.of(registry.getObjectType(), registry.getNullType());
    UnionType unionType = new UnionType(registry, alternates);

    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals("(null|Object)", restricted.toString());
  }

  @Test
  public void testTestForEquality() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    Set<JSType> alternatesB = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    TernaryValue result = unionTypeA.testForEquality(unionTypeB);
    assertEquals(UNKNOWN, result);

    // Test reflexivity
    result = unionTypeA.testForEquality(unionTypeA);
    assertEquals(UNKNOWN, result);

    result = unionTypeB.testForEquality(unionTypeB);
    assertEquals(UNKNOWN, result);

    result = registry.getBooleanType().testForEquality(unionTypeA);
    assertEquals(UNKNOWN, result);

    result = registry.getStringType().testForEquality(unionTypeA);
    assertEquals(UNKNOWN, result);
  }

  @Test
  public void testIsNullable() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertFalse(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    Set<JSType> alternates = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    UnionType unionType = new UnionType(registry, alternates);
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    Set<JSType> alternatesB = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    JSType leastSupertype = unionTypeA.getLeastSupertype(unionTypeB);
    assertEquals(unionTypeB, leastSupertype);

    leastSupertype = unionTypeB.getLeastSupertype(unionTypeA);
    assertEquals(unionTypeA, leastSupertype);

    leastSupertype = unionTypeA.getLeastSupertype(registry.getBooleanType());
    assertEquals(registry.getBooleanType(), leastSupertype);

    leastSupertype = unionTypeB.getLeastSupertype(registry.getBooleanType());
    assertEquals(registry.getBooleanType(), leastSupertype);
  }

  @Test
  public void testMeet() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    Set<JSType> alternatesB = ImmutableSet.of(registry.getStringType(), registry.getNumberType());

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    JSType result = unionTypeA.meet(unionTypeB);
    assertNull(result);
  }

  @Test
  public void testEquals() {
    Set<JSType> alternatesA = ImmutableSet.of(registry.getBooleanType(), registry.getStringType());
    Set<JSType> alternatesB = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());

    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    assertFalse(unionTypeA.equals(unionTypeB));
  }

  // Add more test cases here for other methods and edge cases
}