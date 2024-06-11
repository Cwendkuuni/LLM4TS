import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testGetAlternates() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 Set<JSType> alternates = stringBooleanUnion.getAlternates();
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getStringType()));
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getBooleanType()));
 }

 @Test
 public void testMatchesNumberContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertFalse(stringBooleanUnion.matchesNumberContext());
 }

 @Test
 public void testMatchesStringContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertTrue(stringBooleanUnion.matchesStringContext());
 }

 @Test
 public void testMatchesObjectContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertTrue(stringBooleanUnion.matchesObjectContext());
 }

 @Test
 public void testFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertEquals(JSTypeRegistry.getInstance().getStringType(), stringBooleanUnion.findPropertyType("length"));
 }

 @Test
 public void testCanAssignTo() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringType = JSTypeRegistry.getInstance().getStringType();
 UnionType booleanType = JSTypeRegistry.getInstance().getBooleanType();
 UnionType numberType = JSTypeRegistry.getInstance().getNumberType();

 assertTrue(stringBooleanUnion.canAssignTo(stringType));
 assertTrue(stringBooleanUnion.canAssignTo(booleanType));
 assertFalse(stringBooleanUnion.canAssignTo(numberType));
 }

 @Test
 public void testCanBeCalled() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertTrue(stringBooleanUnion.canBeCalled());
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanNotNullUnion = stringBooleanUnion.restrictByNotNullOrUndefined();
 assertEquals("(String, Boolean)", stringBooleanNotNullUnion.toString());
 JSTypeRegistry registry = JSTypeRegistry.getInstance();
 UnionType stringNotNullType = registry.getStringType().restrictByNotNullOrUndefined();
 UnionType booleanNotNullType = registry.getBooleanType().restrictByNotNullOrUndefined();
 Set<JSType> stringBooleanNullAlternates = stringBooleanUnion.getAlternates();
 Set<JSType> stringBooleanNotNullAlternates = stringBooleanNotNullUnion.getAlternates();
 assertTrue(stringBooleanNullAlternates.contains(registry.getNullType()));
 assertTrue(stringBooleanNullAlternates.contains(registry.getUndefinedType()));
 assertFalse(stringBooleanNotNullAlternates.contains(registry.getNullType()));
 assertFalse(stringBooleanNotNullAlternates.contains(registry.getUndefinedType()));
 assertTrue(stringBooleanNotNullAlternates.contains(stringNotNullType));
 assertTrue(stringBooleanNotNullAlternates.contains(booleanNotNullType));
 }

 @Test
 public void testTestForEquality() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType booleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getBooleanType()));
 TernaryValue result = stringBooleanUnion.testForEquality(booleanUnion);
 assertEquals(TernaryValue.FALSE, result);
 result = stringBooleanUnion.testForEquality(stringBooleanUnion2);
 assertEquals(TernaryValue.TRUE, result);
 }

 @Test
 public void testIsNullable() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertFalse(stringBooleanUnion.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertFalse(stringBooleanUnion.isUnknownType());
 }

 @Test
 public void testGetLeastSupertype() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType objectType = JSTypeRegistry.getInstance().getObjectType();
 JSType leastSupertype = stringBooleanUnion.getLeastSupertype(objectType);
 assertEquals(objectType, leastSupertype);
 }

 @Test
 public void testMeet() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType result = stringBooleanUnion.meet(numberBooleanUnion);
 UnionType expected = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType(), JSTypeRegistry.getInstance().getNumberType()));
 assertEquals(expected, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testEquals() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion3 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getStringType()));
 assertEquals(stringBooleanUnion, stringBooleanUnion2);
 assertNotEquals(stringBooleanUnion, stringBooleanUnion3);
 }

 @Test
 public void testHashCode() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringStringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getStringType()));
 assertEquals(stringBooleanUnion.hashCode(), stringBooleanUnion2.hashCode());
 assertNotEquals(stringBooleanUnion.hashCode(), stringStringUnion.hashCode());
 }

 @Test
 public void testIsUnionType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertTrue(stringBooleanUnion.isUnionType());
 }

 @Test
 public void testIsObject() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertTrue(stringBooleanUnion.isObject());
 }

 @Test
 public void testContains() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringType = JSTypeRegistry.getInstance().getStringType();
 UnionType booleanType = JSTypeRegistry.getInstance().getBooleanType();
 assertTrue(stringBooleanUnion.contains(stringType));
 assertTrue(stringBooleanUnion.contains(booleanType));
 }

 @Test
 public void testGetRestrictedUnion() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberType = JSTypeRegistry.getInstance().getNumberType();
 UnionType restrictedUnion = stringBooleanUnion.getRestrictedUnion(numberType);
 assertEquals(stringBooleanUnion, restrictedUnion);
 }
}