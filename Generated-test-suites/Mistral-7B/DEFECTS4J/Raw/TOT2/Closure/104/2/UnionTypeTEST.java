import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testGetAlternates() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 Set<JSType> alternates = stringBooleanUnion.getAlternates();
 assertTrue(alternates.contains(JSTypeRegistry.instance().getStringType()));
 assertTrue(alternates.contains(JSTypeRegistry.instance().getBooleanType()));
 }

 @Test
 public void testMatchesNumberContext() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertFalse(stringBooleanUnion.matchesNumberContext());
 }

 @Test
 public void testMatchesStringContext() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertTrue(stringBooleanUnion.matchesStringContext());
 }

 @Test
 public void testMatchesObjectContext() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertTrue(stringBooleanUnion.matchesObjectContext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionWithSameTypeAlternate() {
 UnionTypeRegistry registry = UnionTypeRegistry.instance();
 UnionType stringStringUnion = new UnionType(registry, ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getStringType()
 ));
 }

 @Test
 public void testUnionWithNullAlternate() {
 UnionTypeRegistry registry = UnionTypeRegistry.instance();
 Set<JSType> alternates = ImmutableSet.of(JSTypeRegistry.instance().getNullType());
 UnionType unionType = new UnionType(registry, alternates);
 assertTrue(unionType.isNullType());
 }

 @Test
 public void testUnionWithVoidAlternate() {
 UnionTypeRegistry registry = UnionTypeRegistry.instance();
 Set<JSType> alternates = ImmutableSet.of(JSTypeRegistry.instance().getVoidType());
 UnionType unionType = new UnionType(registry, alternates);
 assertTrue(unionType.isVoidType());
 }

 @Test
 public void testUnionWithMixedTypes() {
 UnionTypeRegistry registry = UnionTypeRegistry.instance();
 Set<JSType> alternates = ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType(),
 JSTypeRegistry.instance().getNumberType()
 );
 UnionType unionType = new UnionType(registry, alternates);
 assertFalse(unionType.matchesNumberContext());
 assertFalse(unionType.matchesStringContext());
 assertTrue(unionType.matchesObjectContext());
 }

 @Test
 public void testFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertEquals(JSTypeRegistry.instance().getStringType(), stringBooleanUnion.findPropertyType("toString"));
 }

 @Test
 public void testCanAssignTo() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 UnionType stringType = JSTypeRegistry.instance().getStringType();
 UnionType booleanType = JSTypeRegistry.instance().getBooleanType();
 assertTrue(stringBooleanUnion.canAssignTo(stringType));
 assertTrue(stringBooleanUnion.canAssignTo(booleanType));
 }

 @Test
 public void testCanBeCalled() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertTrue(stringBooleanUnion.canBeCalled());
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 UnionType restrictedUnion = stringBooleanUnion.restrictByNotNullOrUndefined();
 assertTrue(restrictedUnion.getAlternates().isEmpty());
 }

 @Test
 public void testTestForEquality() {
 UnionType stringBooleanUnion1 = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 UnionType stringBooleanUnion2 = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertEquals(stringBooleanUnion1, stringBooleanUnion2);
 }

 @Test
 public void testIsNullable() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertFalse(stringBooleanUnion.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 assertFalse(stringBooleanUnion.isUnknownType());
 }

 @Test
 public void testGetLeastSupertype() {
 UnionType stringBooleanUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 UnionType numberUnion = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getNumberType()
 ));
 UnionType commonSupertype = UnionType.getLeastSupertype(stringBooleanUnion, numberUnion);
 assertTrue(commonSupertype.matchesNumberContext());
 assertTrue(commonSupertype.matchesObjectContext());
 }

 @Test
 public void testMeet() {
 UnionType stringBooleanUnion1 = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType()
 ));
 UnionType numberStringUnion1 = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getNumberType(),
 JSTypeRegistry.instance().getStringType()
 ));
 UnionType meetType1 = stringBooleanUnion1.meet(numberStringUnion1);
 UnionType stringBooleanUnion2 = new UnionType(UnionTypeRegistry.instance(), ImmutableSet.of(
 JSTypeRegistry.instance().getStringType(),
 JSTypeRegistry.instance().getBooleanType(),
 JSTypeRegistry.instance().getNumberType()
 ));
 UnionType meetType2 = stringBooleanUnion2.meet(numberStringUnion1);
 assertEquals(meetType1, meetType2);
 }
}