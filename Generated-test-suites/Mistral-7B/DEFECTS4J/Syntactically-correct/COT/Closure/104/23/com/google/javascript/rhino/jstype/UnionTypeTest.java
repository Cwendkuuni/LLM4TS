import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.testing.TypeTester;
import com.google.common.collect.ImmutableSet;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testUnionTypeBasicFunctionality() {
    Set<JSType> alternates = ImmutableSet.of(registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(String|Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeGetAlternates() {
    Set<JSType> alternates = ImmutableSet.of(registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testUnionTypeMatchesNumberContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testUnionTypeMatchesStringContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContext() {
    Set<JSType> alternates = ImmutableSet.of(registry.findBuiltins().ObjectType(), registry.findBuiltins().BooleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType stringBooleanUnion = new UnionType(registry, stringBooleanAlternates);

    JSType propertyType = stringBooleanUnion.findPropertyType("length");
    assertEquals(registry.findBuiltins().NumberType(), propertyType);

    propertyType = stringBooleanUnion.findPropertyType("toString");
    assertEquals(registry.findBuiltins().StringType(), propertyType);
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    JSType that = registry.findBuiltins().StringType();

    assertTrue(numberBooleanUnion.canAssignTo(registry.findBuiltins().NumberType()));
    assertTrue(numberBooleanUnion.canAssignTo(numberBooleanUnion));
    assertFalse(numberBooleanUnion.canAssignTo(that));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> errorErrorAlternates = ImmutableSet.of(registry.findBuiltins().ErrorType(), registry.findBuiltins().ErrorType());
    UnionType errorErrorUnion = new UnionType(registry, errorErrorAlternates);

    assertTrue(errorErrorUnion.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> nullableBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NullType(), registry.findBuiltins().BooleanType());
    UnionType nullableBooleanUnion = new UnionType(registry, nullableBooleanAlternates);

    UnionType restrictedUnion = nullableBooleanUnion.restrictByNotNullOrUndefined();

    assertFalse(restrictedUnion.isNullable());
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    JSType that = registry.findBuiltins().StringType();

    TernaryValue result = numberBooleanUnion.testForEquality(registry.findBuiltins().NumberType());
    assertEquals(TernaryValue.TRUE, result);

    result = numberBooleanUnion.testForEquality(that);
    assertEquals(TernaryValue.FALSE, result);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> nullableNumberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NullType(), registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType nullableNumberBooleanUnion = new UnionType(registry, nullableNumberBooleanAlternates);

    assertFalse(nullableNumberBooleanUnion.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> unknownNumberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().UnknownType(), registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType unknownNumberBooleanUnion = new UnionType(registry, unknownNumberBooleanAlternates);

    assertTrue(unknownNumberBooleanUnion.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> numberStringBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType numberStringBooleanUnion = new UnionType(registry, numberStringBooleanAlternates);

    JSType that = registry.findBuiltins().ObjectType();

    JSType leastSupertype = numberStringBooleanUnion.getLeastSupertype(that);
    assertEquals(registry.findBuiltins().ObjectType(), leastSupertype);
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> numberBooleanObjectAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType(), registry.findBuiltins().ObjectType());
    UnionType numberBooleanObjectUnion = new UnionType(registry, numberBooleanObjectAlternates);

    Set<JSType> stringBooleanObjectAlternates = ImmutableSet.of(registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType(), registry.findBuiltins().ObjectType());
    UnionType stringBooleanObjectUnion = new UnionType(registry, stringBooleanObjectAlternates);

    UnionType result = numberBooleanObjectUnion.meet(stringBooleanObjectUnion);

    assertEquals(numberBooleanObjectAlternates, result.getAlternates());
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion1 = new UnionType(registry, numberBooleanAlternates);

    Set<JSType> numberStringBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType numberStringBooleanUnion = new UnionType(registry, numberStringBooleanAlternates);

    UnionType numberBooleanUnion2 = new UnionType(registry, numberBooleanAlternates);

    assertTrue(numberBooleanUnion1.equals(numberBooleanUnion2));

    assertFalse(numberBooleanUnion1.equals(numberStringBooleanUnion));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    UnionType numberBooleanUnion2 = new UnionType(registry, numberBooleanAlternates);

    assertEquals(numberBooleanUnion.hashCode(), numberBooleanUnion2.hashCode());

    Set<JSType> numberStringBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType numberStringBooleanUnion = new UnionType(registry, numberStringBooleanAlternates);

    assertNotEquals(numberBooleanUnion.hashCode(), numberStringBooleanUnion.hashCode());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    assertTrue(numberBooleanUnion.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    Set<JSType> objectBooleanAlternates = ImmutableSet.of(registry.findBuiltins().ObjectType(), registry.findBuiltins().BooleanType());
    UnionType objectBooleanUnion = new UnionType(registry, objectBooleanAlternates);

    assertTrue(objectBooleanUnion.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    JSType numberType = registry.findBuiltins().NumberType();

    assertTrue(numberBooleanUnion.contains(numberType));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    JSType stringType = registry.findBuiltins().StringType();

    UnionType restrictedUnion = numberBooleanUnion.getRestrictedUnion(stringType);

    ImmutableSet<JSType> restrictedAlternates = ImmutableSet.of(registry.findBuiltins().NumberType());
    UnionType expectedRestrictedUnion = new UnionType(registry, restrictedAlternates);

    assertEquals(expectedRestrictedUnion, restrictedUnion);
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> numberStringBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().StringType(), registry.findBuiltins().BooleanType());
    UnionType numberStringBooleanUnion = new UnionType(registry, numberStringBooleanAlternates);

    assertEquals("(Number|String|Boolean)", numberStringBooleanUnion.toString());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> numberBooleanAlternates = ImmutableSet.of(registry.findBuiltins().NumberType(), registry.findBuiltins().BooleanType());
    UnionType numberBooleanUnion = new UnionType(registry, numberBooleanAlternates);

    JSType numberType = registry.findBuiltins().NumberType();

    assertTrue(numberBooleanUnion.isSubtype(numberType));
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> booleanNumberAlternates = ImmutableSet.of(registry.findBuiltins().BooleanType(), registry.findBuiltins().NumberType());
    UnionType booleanNumberUnion = new UnionType(registry, booleanNumberAlternates);

    BooleanLiteralSet booleanOutcomes = BooleanLiteralSet.fromInts(1, 0);
    UnionType booleanOutcomeUnion = booleanNumberUnion.getRestrictedTypeGivenToBooleanOutcome(booleanOutcomes.getBooleanValue(0));

    ImmutableSet<JSType> booleanAlternates = ImmutableSet.of(registry.findBuiltins().BooleanType());
    UnionType expectedBooleanOutcomeUnion = new UnionType(registry, booleanAlternates);

    assertEquals(expectedBooleanOutcomeUnion, booleanOutcomeUnion);

    booleanOutcomes = BooleanLiteralSet.fromInts(0, 1);
    booleanOutcomeUnion = booleanNumberUnion.getRestrictedTypeGivenToBooleanOutcome(booleanOutcomes.getBooleanValue(1));

    ImmutableSet<JSType> numberAlternates = ImmutableSet.of(registry.findBuiltins().NumberType());
    UnionType expectedNumberOutcomeUnion = new UnionType(registry, numberAlternates);

    assertEquals(expectedNumberOutcomeUnion, booleanOutcomeUnion);
  }

  @Test
  public void testUnionTypeGetPossibleToBooleanOutcomes() {
    Set<JSType> booleanNumberAlternates = ImmutableSet.of(registry.findBuiltins().BooleanType(), registry.findBuiltins().NumberType());
    UnionType booleanNumberUnion = new UnionType(registry, booleanNumberAlternates);

    BooleanLiteralSet booleanOutcomes = booleanNumberUnion.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, booleanOutcomes);
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> booleanNumberAlternates = ImmutableSet.of(registry.findBuiltins().BooleanType(), registry.findBuiltins().NumberType());
    UnionType booleanNumberUnion = new UnionType(registry, booleanNumberAlternates);

    JSType that = registry.findBuiltins().StringType();

    TypePair result = booleanNumberUnion.getTypesUnderEquality(that);

    UnionType emptyUnion = new UnionType(registry, ImmutableSet.of());
    UnionType expectedEmptyUnion = new UnionType(registry, ImmutableSet.of());

    assertEquals(expectedEmptyUnion, result.typeA);
    assertEquals(that, result.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> booleanNumberAlternates = ImmutableSet.of(registry.findBuiltins().BooleanType(), registry.findBuiltins().NumberType());
    UnionType booleanNumberUnion = new UnionType(registry, booleanNumberAlternates);

    JSType that = registry.findBuiltins().StringType();

    TypePair result = booleanNumberUnion.getTypesUnderInequality(that);

    UnionType emptyUnion = new UnionType(registry, ImmutableSet.of());
    UnionType expectedEmptyUnion = new UnionType(registry, ImmutableSet.of());

    assertEquals(booleanNumberUnion, result.typeA);
    assertEquals(expectedEmptyUnion, result.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> booleanNumberAlternates = ImmutableSet.of(registry.findBuiltins().BooleanType(), registry.findBuiltins().NumberType());
    UnionType booleanNumberUnion = new UnionType(registry, booleanNumberAlternates);

    JSType that = registry.findBuiltins().StringType();

    TypePair result = booleanNumberUnion.getTypesUnderShallowInequality(that);

    UnionType emptyUnion = new UnionType(registry, ImmutableSet.of());
    UnionType expectedEmptyUnion = new UnionType(registry, ImmutableSet.of());

    assertEquals(emptyUnion, result.typeA);
    assertEquals(booleanNumberUnion, result.typeB);
  }
}