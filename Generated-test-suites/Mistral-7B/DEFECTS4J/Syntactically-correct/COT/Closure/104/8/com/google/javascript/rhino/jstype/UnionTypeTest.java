import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.TestingUtilities;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = new HashSet<JSType>();
    alternates.add(TestingUtilities.intType);
    alternates.add(TestingUtilities.stringType);

    UnionType unionType = new UnionType(TestingUtilities.registry, alternates);
    assertEquals("(int, string)", unionType.toString());
  }

  @Test
  public void testGetAlternates() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    assertEquals(Arrays.asList(TestingUtilities.intType,
        TestingUtilities.stringType), Arrays.asList(unionType.getAlternates()));
  }

  @Test
  public void testFindPropertyType() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    assertNull(unionType.findPropertyType("length"));
    assertEquals(TestingUtilities.intType, unionType.findPropertyType("length"));
  }

  @Test
  public void testCanAssignTo() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    assertTrue(unionType.canAssignTo(TestingUtilities.intType));
    assertTrue(unionType.canAssignTo(TestingUtilities.stringType));
    assertFalse(unionType.canAssignTo(TestingUtilities.booleanType));
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = createUnionType(TestingUtilities.nullType,
        TestingUtilities.stringType);
    assertEquals(TestingUtilities.stringType, unionType.restrictByNotNullOrUndefined());
  }

  @Test
  public void testTestForEquality() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType otherUnionType = createUnionType(TestingUtilities.stringType,
        TestingUtilities.intType);

    assertEquals(UNKNOWN, unionType.testForEquality(otherUnionType));
  }

  @Test
  public void testIsNullable() {
    UnionType unionType = createUnionType(TestingUtilities.nullType,
        TestingUtilities.stringType);
    assertTrue(unionType.isNullable());

    UnionType nonNullableUnionType = createUnionType(
        TestingUtilities.intType, TestingUtilities.stringType);
    assertFalse(nonNullableUnionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    assertFalse(unionType.isUnknownType());

    UnionType unknownUnionType = new UnionType(TestingUtilities.registry,
        new HashSet<JSType>());
    assertTrue(unknownUnionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType intOrString = new UnionType(TestingUtilities.registry,
        Arrays.asList(TestingUtilities.intType, TestingUtilities.stringType));

    assertEquals(intOrString, unionType.getLeastSupertype(intOrString));

    JSType otherType = TestingUtilities.objectType;
    assertEquals(TestingUtilities.objectType, unionType.getLeastSupertype(otherType));
  }

  @Test
  public void testMeet() {
    UnionType unionTypeA = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType unionTypeB = createUnionType(TestingUtilities.booleanType,
        TestingUtilities.stringType);

    UnionType result = unionTypeA.meet(unionTypeB);

    assertEquals(TestingUtilities.stringType, result.getAlternates().iterator()
        .next());
  }

  @Test
  public void testEqualsAndHashCode() {
    UnionType unionTypeA = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    UnionType unionTypeB = createUnionType(TestingUtilities.stringType,
        TestingUtilities.intType);

    assertTrue(unionTypeA.equals(unionTypeB));
    assertEquals(unionTypeA.hashCode(), unionTypeB.hashCode());
  }

  @Test
  public void testIsUnionType() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    assertTrue(unionType.contains(TestingUtilities.intType));
    assertTrue(unionType.contains(TestingUtilities.stringType));
    assertFalse(unionType.contains(TestingUtilities.booleanType));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    assertEquals(TestingUtilities.stringType, unionType.getRestrictedUnion(
        TestingUtilities.intType));
  }

  @Test
  public void testToString() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);
    assertEquals("(int, string)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    UnionType unionType = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType intType = TestingUtilities.intType;
    UnionType stringType = TestingUtilities.stringType;
    UnionType booleanType = TestingUtilities.booleanType;

    assertTrue(unionType.isSubtype(intType));
    assertTrue(unionType.isSubtype(stringType));
    assertFalse(unionType.isSubtype(booleanType));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType unionType = createUnionType(TestingUtilities.booleanType,
        TestingUtilities.numberType);

    assertEquals(TestingUtilities.booleanType, unionType
        .getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(TestingUtilities.numberType, unionType
        .getRestrictedTypeGivenToBooleanOutcome(false));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    UnionType unionType = createUnionType(TestingUtilities.booleanType,
        TestingUtilities.numberType);

    assertEquals(BooleanLiteralSet.BOTH, unionType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testGetTypesUnderEquality() {
    UnionType unionTypeA = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType unionTypeB = createUnionType(TestingUtilities.stringType,
        TestingUtilities.intType);

    TypePair result = unionTypeA.getTypesUnderEquality(unionTypeB);

    JSType typeA = unionTypeA.getAlternates().iterator().next();
    JSType typeB = unionTypeB.getAlternates().iterator().next();

    UnionType typeAPrimary = (UnionType) typeA;
    UnionType typeBPrimary = (UnionType) typeB;

    assertEquals(typeAPrimary, result.typeA);
    assertEquals(typeBPrimary, result.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    UnionType unionTypeA = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType unionTypeB = createUnionType(TestingUtilities.stringType,
        TestingUtilities.intType);

    TypePair result = unionTypeA.getTypesUnderInequality(unionTypeB);

    JSType typeA = unionTypeA.getAlternates().iterator().next();
    JSType typeB = unionTypeB.getAlternates().iterator().next();

    UnionType typeAPrimary = (UnionType) typeA;
    UnionType typeBPrimary = (UnionType) typeB;

    assertEquals(typeBPrimary, result.typeA);
    assertEquals(typeAPrimary, result.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    UnionType unionTypeA = createUnionType(TestingUtilities.intType,
        TestingUtilities.stringType);

    UnionType unionTypeB = createUnionType(TestingUtilities.stringType,
        TestingUtilities.intType);

    TypePair result = unionTypeA.getTypesUnderShallowInequality(unionTypeB);

    JSType typeA = unionTypeA.getAlternates().iterator().next();
    JSType typeB = unionTypeB.getAlternates().iterator().next();

    UnionType typeAPrimary = (UnionType) typeA;
    UnionType typeBPrimary = (UnionType) typeB;

    assertEquals(typeAPrimary, result.typeA);
    assertEquals(typeBPrimary, result.typeB);
  }

  private UnionType createUnionType(JSType... alternates) {
    UnionTypeBuilder builder = new UnionTypeBuilder(TestingUtilities.registry);
    for (JSType alternate : alternates) {
      builder.addAlternate(alternate);
    }
    return builder.build();
  }
}