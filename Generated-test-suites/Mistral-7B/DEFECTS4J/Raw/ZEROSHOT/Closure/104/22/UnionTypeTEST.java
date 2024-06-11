import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals("(string, boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeGetAlternates() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);
    ImmutableSet<JSType> actualAlternates = unionType.getAlternates();
    assertEquals(alternates, actualAlternates);
  }

  @Test
  public void testUnionTypeMatchesContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    // String context test
    assertTrue(unionType.matchesStringContext());

    // Number context test
    assertFalse(unionType.matchesNumberContext());

    // Object context test
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> alternates = ImmutableSet.of(
        registry.getObjectType(),
        new UnionTypeBuilder(registry)
            .addAlternate(registry.getStringType())
            .addAlternate(registry.getBooleanType())
            .build()
    );
    UnionType unionType = new UnionType(registry, alternates);

    assertNull(unionType.findPropertyType("notExistentProperty"));
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    Set<JSType> assignableTypes = ImmutableSet.of(
        registry.getStringType(),
        registry.getBooleanType(),
        registry.getNumberType(),
        registry.getObjectType()
    );

    for (JSType that : assignableTypes) {
      assertTrue(unionType.canAssignTo(that));
    }

    assertFalse(registry.getNullType().canAssignTo(unionType));
    assertFalse(registry.getVoidType().canAssignTo(unionType));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertTrue(restrictedUnionType.isNullType());
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> stringAlternates = ImmutableSet.of(registry.getStringType());
    UnionType stringUnionType = new UnionType(registry, stringAlternates);

    Set<JSType> booleanAlternates = ImmutableSet.of(registry.getBooleanType());
    UnionType booleanUnionType = new UnionType(registry, booleanAlternates);

    Set<JSType> stringBooleanAlternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType stringBooleanUnionType = new UnionType(registry, stringBooleanAlternates);

    TernaryValue stringStringResult = stringUnionType.testForEquality(stringUnionType);
    assertEquals(TernaryValue.TRUE, stringStringResult);

    TernaryValue stringBooleanResult = stringUnionType.testForEquality(booleanUnionType);
    assertEquals(TernaryValue.UNKNOWN, stringBooleanResult);

    TernaryValue stringBooleanStringResult = stringBooleanUnionType.testForEquality(stringBooleanUnionType);
    assertEquals(TernaryValue.TRUE, stringBooleanStringResult);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> stringAlternates = ImmutableSet.of(registry.getStringType());
    UnionType stringUnionType = new UnionType(registry, stringAlternates);

    Set<JSType> numberAlternates = ImmutableSet.of(registry.getNumberType());
    UnionType numberUnionType = new UnionType(registry, numberAlternates);

    assertEquals(numberUnionType, stringUnionType.getLeastSupertype(numberUnionType));
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> stringAlternates = ImmutableSet.of(registry.getStringType());
    UnionType stringUnionType = new UnionType(registry, stringAlternates);

    Set<JSType> numberAlternates = ImmutableSet.of(registry.getNumberType());
    UnionType numberUnionType = new UnionType(registry, numberAlternates);

    UnionType result = stringUnionType.meet(numberUnionType);

    assertNotNull(result);
    assertTrue(result.isObject());
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> stringAlternates = ImmutableSet.of(registry.getStringType());
    UnionType stringUnionType = new UnionType(registry, stringAlternates);

    Set<JSType> booleanAlternates = ImmutableSet.of(registry.getBooleanType());
    UnionType booleanUnionType = new UnionType(registry, booleanAlternates);

    Set<JSType> stringBooleanAlternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType stringBooleanUnionType = new UnionType(registry, stringBooleanAlternates);

    assertTrue(stringUnionType.equals(stringUnionType));

    assertFalse(stringUnionType.equals(booleanUnionType));

    assertTrue(stringUnionType.equals(stringBooleanUnionType));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    int hashCode = unionType.hashCode();

    UnionType unionTypeWithSameAlternates = new UnionType(registry, alternates);

    assertEquals(hashCode, unionTypeWithSameAlternates.hashCode());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    UnionType alternate = registry.getStringType();

    assertTrue(unionType.contains(alternate));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> alternates = ImmutableSet.of(registry.getStringType(), registry.getBooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    UnionType restrictedUnionType = unionType.getRestrictedUnion(registry.getStringType());

    assertNotNull(restrictedUnionType);
    assertEquals(registry.getStringType(), restrictedUnionType);
  }
}