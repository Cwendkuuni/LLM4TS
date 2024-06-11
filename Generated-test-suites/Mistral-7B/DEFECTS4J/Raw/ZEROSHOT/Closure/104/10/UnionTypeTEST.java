import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import static com.google.javascript.rhino.jstype.UnionType.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertEquals("(String, Boolean)", unionType.toString());
    assertEquals(2, unionType.getAlternates().size());
  }

  @Test
  public void testUnionTypeGetAlternates() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    Iterable<JSType> alternates = unionType.getAlternates();
    assertTrue(alternates instanceof Iterable);
    assertFalse(alternates instanceof JSType);

    Set<JSType> actualAlternates = new HashSet<JSType>();
    for (JSType alternate : alternates) {
      actualAlternates.add(alternate);
    }

    assertEquals(alternateTypes, actualAlternates);
  }

  @Test
  public void testUnionTypeMatchesNumberContext() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(numberType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testUnionTypeMatchesStringContext() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContext() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(objectType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(numberType);

    UnionType unionType = new UnionType(null, alternateTypes);

    JSType propertyType = unionType.findPropertyType("length");
    assertEquals(numberType, propertyType);
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(numberType);

    UnionType unionType = new UnionType(null, alternateTypes);

    JSType that = numberType;

    assertTrue(unionType.canAssignTo(that));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(functionType(Arrays.asList(contextType, stringType),
        voidType));
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(nullType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();

    assertTrue(restrictedUnionType.isNullType());
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(stringType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(stringType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    TernaryValue ternaryValue = unionTypeA.testForEquality(unionTypeB);

    assertEquals(UNKNOWN, ternaryValue);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(numberType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(numberType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(stringType);
    alternateTypesA.add(numberType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(numberType);
    alternateTypesB.add(booleanType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    JSType leastSupertype = unionTypeA.getLeastSupertype(unionTypeB);

    assertEquals(numberType, leastSupertype);
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(numberType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(numberType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    UnionType meet = unionTypeA.meet(unionTypeB);

    assertEquals(numberType, meet);
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(stringType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(stringType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    assertFalse(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionTypeA = new UnionType(null, alternateTypes);
    UnionType unionTypeB = new UnionType(null, alternateTypes);

    assertEquals(hashCode(unionTypeA), hashCode(unionTypeB));
  }

  @Test
  public void testUnionTypeIsUnionType() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertTrue(unionType.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    JSType alternateType = booleanType;

    assertTrue(unionType.contains(alternateType));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(numberType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    JSType restrictedUnionType = unionType.getRestrictedUnion(stringType);

    assertNull(restrictedUnionType);
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(stringType);
    alternateTypes.add(booleanType);

    UnionType unionType = new UnionType(null, alternateTypes);

    assertEquals("(String, Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(numberType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(numberType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    assertTrue(unionTypeA.isSubtype(unionTypeB));
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> alternateTypes = new HashSet<JSType>();
    alternateTypes.add(booleanType);
    alternateTypes.add(stringType);

    UnionType unionType = new UnionType(null, alternateTypes);

    BooleanLiteralSet possibleToBooleanOutcomes = unionType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, possibleToBooleanOutcomes);
  }

  @Test
  public void testUnionTypeTypesUnderEquality() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(numberType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(numberType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    TypePair typePair = unionTypeA.getTypesUnderEquality(unionTypeB);

    assertNull(typePair.typeA);
    assertNull(typePair.typeB);
  }

  @Test
  public void testUnionTypeTypesUnderInequality() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(numberType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(numberType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    TypePair typePair = unionTypeA.getTypesUnderInequality(unionTypeB);

    assertNull(typePair.typeA);
    assertNull(typePair.typeB);
  }

  @Test
  public void testUnionTypeTypesUnderShallowInequality() {
    Set<JSType> alternateTypesA = new HashSet<JSType>();
    alternateTypesA.add(numberType);
    alternateTypesA.add(booleanType);

    Set<JSType> alternateTypesB = new HashSet<JSType>();
    alternateTypesB.add(booleanType);
    alternateTypesB.add(numberType);

    UnionType unionTypeA = new UnionType(null, alternateTypesA);
    UnionType unionTypeB = new UnionType(null, alternateTypesB);

    TypePair typePair = unionTypeA.getTypesUnderShallowInequality(unionTypeB);

    assertNull(typePair.typeA);
    assertNull(typePair.typeB);
  }
}