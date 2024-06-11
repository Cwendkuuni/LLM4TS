import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.TernaryValue.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertEquals(2, unionType.getAlternates().size());
  }

  @Test
  public void testGetAlternates() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(IntType.instance(), StringType.instance()));
    assertSame(IntType.instance(), unionType.getAlternates().iterator().next());
    assertSame(StringType.instance(), unionType.getAlternates().iterator().next());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testModifyAlternates() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(IntType.instance(), StringType.instance()));
    unionType.getAlternates().clear();
  }

  @Test
  public void testForgiveUnknownNames() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(IntType.instance(), StringType.instance()));
    unionType.forgiveUnknownNames();
    // No specific assertions for this method as it only calls forgiveUnknownNames() on each alternate type
  }

  @Test
  public void testMatchesNumberContext() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertFalse(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
        new ObjectType(new JSTypeRegistry(), "MyObject", ImmutableSet.of("prop", new NumberType(new JSTypeRegistry(), "Number"))),
        new ObjectType(new JSTypeRegistry(), "MyOtherObject", ImmutableSet.of("prop", new StringType(new JSTypeRegistry(), "String")))));
    assertEquals(new NumberType(new JSTypeRegistry(), "Number"), unionType.findPropertyType("prop"));
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertFalse(unionType.canAssignTo(new NullType(new JSTypeRegistry())));
  }

  @Test
  public void testCanBeCalled() {
    Set<JSType> alternates = ImmutableSet.of(
        new FunctionType(
            new JSTypeRegistry(),
            ImmutableSet.of("arg1"),
            new VoidType(new JSTypeRegistry())),
        new FunctionType(
            new JSTypeRegistry(),
            ImmutableSet.of("arg1"),
            new IntType(new JSTypeRegistry())));
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertTrue(restricted.isNullType());
  }

  @Test
  public void testTestForEquality() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    assertEquals(UNKNOWN, unionTypeA.testForEquality(unionTypeB));
  }

  @Test
  public void testIsNullable() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertFalse(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    assertEquals(ObjectType.instance(new JSTypeRegistry(), "Object"), unionTypeA.getLeastSupertype(unionTypeB));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMeetNullThat() {
    UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(IntType.instance()));
    unionType.meet(null);
  }

  @Test
  public void testEquals() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    assertFalse(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testHashCode() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    Set<JSType> alternatesDifferentOrder = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeDifferentOrder = new UnionType(new JSTypeRegistry(), alternatesDifferentOrder);
    assertNotSame(unionType.hashCode(), unionTypeDifferentOrder.hashCode());
  }

  @Test
  public void testIsUnionType() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), ObjectType.instance(new JSTypeRegistry(), "MyObject"));
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertFalse(unionType.contains(BooleanType.instance()));
    assertTrue(unionType.contains(IntType.instance()));
  }

  @Test
  public void testGetRestrictedUnion() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    UnionType restricted = unionType.getRestrictedUnion(IntType.instance());
    assertEquals(StringType.instance(), restricted);
  }

  @Test
  public void testToString() {
    Set<JSType> alternates = ImmutableSet.of(IntType.instance(), StringType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertEquals("(Int|String)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    assertTrue(unionTypeA.isSubtype(unionTypeB));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> alternates = ImmutableSet.of(BooleanType.instance(), IntType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertEquals(IntType.instance(), unionType.getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(BooleanType.instance(), unionType.getRestrictedTypeGivenToBooleanOutcome(false));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    Set<JSType> alternates = ImmutableSet.of(BooleanType.instance(), IntType.instance());
    UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);
    assertEquals(BooleanLiteralSet.BOTH, unionType.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testGetTypesUnderEquality() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    TypePair result = unionTypeA.getTypesUnderEquality(unionTypeB);
    assertEquals(ObjectType.instance(new JSTypeRegistry(), "Object"), result.typeA);
    assertEquals(ObjectType.instance(new JSTypeRegistry(), "Object"), result.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    TypePair result = unionTypeA.getTypesUnderInequality(unionTypeB);
    assertEquals(IntType.instance(), result.typeA);
    assertEquals(StringType.instance(), result.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    Set<JSType> alternatesA = ImmutableSet.of(IntType.instance(), StringType.instance());
    Set<JSType> alternatesB = ImmutableSet.of(StringType.instance(), IntType.instance());
    UnionType unionTypeA = new UnionType(new JSTypeRegistry(), alternatesA);
    UnionType unionTypeB = new UnionType(new JSTypeRegistry(), alternatesB);
    TypePair result = unionTypeA.getTypesUnderShallowInequality(unionTypeB);
    assertEquals(IntType.instance(), result.typeA);
    assertEquals(StringType.instance(), result.typeB);
  }
}