import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 assertEquals("(String, Boolean)", stringBoolUnion.toString());
 }

 @Test
 public void testGetAlternates() {
 UnionType stringBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 Iterable<JSType> alternates = stringBoolUnion.getAlternates();
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getStringType()));
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getBooleanType()));
 }

 @Test
 public void testMatchesNumberContext() {
 UnionType numBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNumberType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 assertFalse(numBoolUnion.matchesNumberContext());
 }

 @Test
 public void testMatchesStringContext() {
 UnionType stringNumUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getNumberType()
 ));
 assertTrue(stringNumUnion.matchesStringContext());
 }

 @Test
 public void testMatchesObjectContext() {
 UnionType nullBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNullType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 assertFalse(nullBoolUnion.matchesObjectContext());
 }

 @Test(expected = AssertionError.class)
 public void testFindPropertyTypeWithInvalidPropertyName() {
 UnionType stringBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 String invalidPropertyName = "invalidPropertyName";
 assertNull(stringBoolUnion.findPropertyType(invalidPropertyName));
 }

 @Test
 public void testCanAssignTo() {
 UnionType numStringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNumberType(),
 JSTypeRegistry.getInstance().getStringType()
 ));
 JSType stringType = JSTypeRegistry.getInstance().getStringType();
 JSType numberType = JSTypeRegistry.getInstance().getNumberType();
 assertTrue(numberType.canAssignTo(numStringUnion));
 assertTrue(numberType.canAssignTo(numStringUnion));
 assertFalse(stringType.canAssignTo(numStringUnion));
 }

 @Test(expected = RuntimeException.class)
 public void testCannotCallUnionType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 unionType.canBeCalled();
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType nullableUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNumberType(),
 JSTypeRegistry.getInstance().getNullType()
 ));
 UnionType restrictedUnion = nullableUnion.restrictByNotNullOrUndefined();
 assertNotNull(restrictedUnion);
 assertTrue(restrictedUnion.isSubtype(JSTypeRegistry.getInstance().getNumberType()));
 assertFalse(restrictedUnion.isSubtype(JSTypeRegistry.getInstance().getNullType()));
 }

 @Test
 public void testTestForEquality() {
 UnionType stringNumUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getNumberType()
 ));
 UnionType numStringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNumberType(),
 JSTypeRegistry.getInstance().getStringType()
 ));
 TernaryValue result = stringNumUnion.testForEquality(numStringUnion);
 assertEquals(TernaryValue.MAYBE, result);
 }

 @Test
 public void testIsNullable() {
 UnionType nullBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNullType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 assertTrue(nullBoolUnion.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType stringBoolUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 assertFalse(stringBoolUnion.isUnknownType());
 }

 @Test
 public void testGetLeastSupertype() {
 UnionType numStringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getNumberType(),
 JSTypeRegistry.getInstance().getStringType()
 ));
 UnionType numberType = JSTypeRegistry.getInstance().getNumberType();
 UnionType stringType = JSTypeRegistry.getInstance().getStringType();
 UnionType leastSupertype = numStringUnion.getLeastSupertype(numberType);
 assertEquals(numberType, leastSupertype);
 leastSupertype = numStringUnion.getLeastSupertype(stringType);
 assertEquals(stringType, leastSupertype);
 }

 @Test
 public void testMeet() {
 UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 ));
 UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 JSTypeRegistry.getInstance().getBooleanType(),
 JSTypeRegistry.getInstance().getNumberType()
 ));
 UnionType meetType = unionTypeA.meet(unionTypeB);
 assertEquals(unionTypeA, meetType);
 meetType = unionTypeB.meet(unionTypeA);
 assertEquals(unionTypeB, meetType);
 }
}