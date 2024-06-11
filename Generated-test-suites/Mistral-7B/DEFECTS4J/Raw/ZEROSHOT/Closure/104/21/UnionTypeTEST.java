import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import java.util.Set;

public class UnionTypeTest {

  @org.junit.Test
  public void testUnionTypeCreation() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertEquals(stringBooleanAlternates, stringBooleanUnion.getAlternates());
  }

  @org.junit.Test
  public void testUnionTypeEquality() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    Set<JSType> booleanStringAlternates = ImmutableSet.of(booleanType(), stringType());

    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);
    UnionType booleanStringUnion = new UnionType(JSTypeRegistry.create(), booleanStringAlternates);

    assertTrue(stringBooleanUnion.equals(stringBooleanUnion));
    assertTrue(stringBooleanUnion.equals(stringBooleanUnion));

    assertFalse(stringBooleanUnion.equals(booleanStringUnion));
  }

  @org.junit.Test
  public void testUnionTypeHashCode() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());

    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);
    UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertEquals(stringBooleanUnion.hashCode(), stringBooleanUnion2.hashCode());
  }

  @org.junit.Test
  public void testUnionTypeMatchesContext() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertTrue(stringBooleanUnion.matchesNumberContext());
    assertTrue(stringBooleanUnion.matchesStringContext());
    assertTrue(stringBooleanUnion.matchesObjectContext());
  }

  @org.junit.Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    // Test finding a property on an alternate type
    JSType stringPropertyType = propertyType(stringType(), "length");
    assertEquals(stringPropertyType, stringBooleanUnion.findPropertyType("length"));

    // Test finding a property on the union type itself
    JSType unionLengthPropertyType = propertyType(stringBooleanUnion, "length");
    assertEquals(stringPropertyType, unionLengthPropertyType);

    // Test finding a property on an alternate that doesn't have it
    JSType booleanPropertyType = propertyType(booleanType(), "length");
    assertNull(stringBooleanUnion.findPropertyType("toUpperCase"));
  }

  @org.junit.Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    JSType numberType = numberType();

    assertTrue(stringBooleanUnion.canAssignTo(numberType));
    assertTrue(numberType.canAssignTo(stringBooleanUnion));
  }

  @org.junit.Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertTrue(stringBooleanUnion.canBeCalled());
  }

  @org.junit.Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    UnionType stringBooleanNullsafe = stringBooleanUnion.restrictByNotNullOrUndefined();

    assertFalse(stringBooleanNullsafe.canAssignTo(nullType()));
    assertFalse(stringBooleanNullsafe.canAssignTo(undefinedType()));
  }

  @org.junit.Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    JSType stringType2 = stringType();
    JSType booleanType2 = booleanType();

    assertEquals(stringBooleanUnion, stringBooleanUnion);
    assertEquals(stringBooleanUnion, stringBooleanUnion);

    assertNotEquals(stringBooleanUnion, stringType2);
    assertNotEquals(stringBooleanUnion, booleanType2);
  }

  @org.junit.Test
  public void testUnionTypeIsNullable() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertFalse(stringBooleanUnion.isNullable());
  }

  @org.junit.Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertFalse(stringBooleanUnion.isUnknownType());
  }

  @org.junit.Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    JSType anyType = anyType();

    assertEquals(anyType, stringBooleanUnion.getLeastSupertype(anyType));
  }

  @org.junit.Test
  public void testUnionTypeMeet() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    Set<JSType> numberBooleanAlternates = ImmutableSet.of(numberType(), booleanType());
    UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.create(), numberBooleanAlternates);

    assertEquals(stringBooleanUnion, stringBooleanUnion.meet(numberBooleanUnion));
  }

  @org.junit.Test
  public void testUnionTypeRestrictedUnion() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    JSType numberType = numberType();

    UnionType restrictedToNumber = stringBooleanUnion.getRestrictedUnion(numberType);

    assertTrue(numberType.isSubtype(restrictedToNumber));
    assertFalse(stringType.isSubtype(restrictedToNumber));
  }

  @org.junit.Test
  public void testUnionTypeToString() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertEquals("(string|boolean)", stringBooleanUnion.toString());
  }

  @org.junit.Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    JSType numberType = numberType();

    assertTrue(stringBooleanUnion.isSubtype(numberType));
  }

  @org.junit.Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    assertTrue(stringBooleanUnion.getRestrictedTypeGivenToBooleanOutcome(true).isInstanceOf(stringType()));
    assertTrue(stringBooleanUnion.getRestrictedTypeGivenToBooleanOutcome(false).isInstanceOf(booleanType()));
  }

  @org.junit.Test
  public void testUnionTypeGetPossibleToBooleanOutcomes() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    BooleanLiteralSet booleanLiterals = BooleanLiteralSet.valueOf(true, false);
    assertEquals(booleanLiterals, stringBooleanUnion.getPossibleToBooleanOutcomes());
  }

  @org.junit.Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    Set<JSType> numberBooleanAlternates = ImmutableSet.of(numberType(), booleanType());
    UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.create(), numberBooleanAlternates);

    TypePair stringBooleanTypesUnderEquality = stringBooleanUnion.getTypesUnderEquality(numberBooleanUnion);
    TypePair numberBooleanTypesUnderEquality = numberBooleanUnion.getTypesUnderEquality(stringBooleanUnion);

    assertNotNull(stringBooleanTypesUnderEquality.typeA);
    assertNotNull(stringBooleanTypesUnderEquality.typeB);
    assertNotNull(numberBooleanTypesUnderEquality.typeA);
    assertNotNull(numberBooleanTypesUnderEquality.typeB);

    assertTrue(stringBooleanTypesUnderEquality.typeA.isSubtype(stringBooleanUnion));
    assertTrue(stringBooleanTypesUnderEquality.typeB.isSubtype(numberBooleanUnion));
    assertTrue(numberBooleanTypesUnderEquality.typeA.isSubtype(numberBooleanUnion));
    assertTrue(numberBooleanTypesUnderEquality.typeB.isSubtype(stringBooleanUnion));
  }

  @org.junit.Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    Set<JSType> numberBooleanAlternates = ImmutableSet.of(numberType(), booleanType());
    UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.create(), numberBooleanAlternates);

    TypePair stringBooleanTypesUnderInequality = stringBooleanUnion.getTypesUnderInequality(numberBooleanUnion);
    TypePair numberBooleanTypesUnderInequality = numberBooleanUnion.getTypesUnderInequality(stringBooleanUnion);

    assertNotNull(stringBooleanTypesUnderInequality.typeA);
    assertNotNull(stringBooleanTypesUnderInequality.typeB);
    assertNotNull(numberBooleanTypesUnderInequality.typeA);
    assertNotNull(numberBooleanTypesUnderInequality.typeB);

    assertNull(stringBooleanTypesUnderInequality.typeA.getLeastSupertype(numberBooleanUnion));
    assertNull(numberBooleanTypesUnderInequality.typeB.getLeastSupertype(stringBooleanUnion));
  }

  @org.junit.Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> stringBooleanAlternates = ImmutableSet.of(stringType(), booleanType());
    UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.create(), stringBooleanAlternates);

    Set<JSType> numberBooleanAlternates = ImmutableSet.of(numberType(), booleanType());
    UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.create(), numberBooleanAlternates);

    TypePair stringBooleanTypesUnderShallowInequality = stringBooleanUnion.getTypesUnderShallowInequality(numberBooleanUnion);
    TypePair numberBooleanTypesUnderShallowInequality = numberBooleanUnion.getTypesUnderShallowInequality(stringBooleanUnion);

    assertNotNull(stringBooleanTypesUnderShallowInequality.typeA);
    assertNotNull(stringBooleanTypesUnderShallowInequality.typeB);
    assertNotNull(numberBooleanTypesUnderShallowInequality.typeA);
    assertNotNull(numberBooleanTypesUnderShallowInequality.typeB);

    assertFalse(stringBooleanTypesUnderShallowInequality.typeA.isSubtype(numberBooleanUnion));
    assertFalse(numberBooleanTypesUnderShallowInequality.typeB.isSubtype(stringBooleanUnion));
  }
}