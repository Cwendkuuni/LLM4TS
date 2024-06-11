import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertEquals("(String, boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testUnionTypeMatchesNumberContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertFalse(stringBooleanUnion.matchesNumberContext());
 }

 @Test
 public void testUnionTypeMatchesStringContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertTrue(stringBooleanUnion.matchesStringContext());
 }

 @Test
 public void testUnionTypeMatchesObjectContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertTrue(stringBooleanUnion.matchesObjectContext());
 }

 @Test
 public void testUnionTypeFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertEquals(StringType.INSTANCE, stringBooleanUnion.findPropertyType("toString"));
 }

 @Test
 public void testUnionTypeCanAssignTo() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 JSType numberType = NumberType.INSTANCE;
 assertTrue(stringBooleanUnion.canAssignTo(numberType));
 }

 @Test
 public void testUnionTypeCanBeCalled() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertTrue(stringBooleanUnion.canBeCalled());
 }

 @Test
 public void testUnionTypeRestrictByNotNullOrUndefined() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringBooleanNullUnion = stringBooleanUnion.restrictByNotNullOrUndefined();
 assertTrue(stringBooleanNullUnion.isSubtype(stringBooleanUnion));
 assertFalse(stringBooleanNullUnion.isSubtype(BooleanType.INSTANCE));
 assertFalse(stringBooleanNullUnion.isSubtype(StringType.INSTANCE));
 }

 @Test
 public void testUnionTypeTestForEquality() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertEquals(UNKNOWN, stringBooleanUnion.testForEquality(stringBooleanUnion2));
 }

 @Test
 public void testUnionTypeIsNullable() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertFalse(stringBooleanUnion.isNullable());
 }

 @Test
 public void testUnionTypeIsUnknownType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertFalse(stringBooleanUnion.isUnknownType());
 }

 @Test
 public void testUnionTypeGetLeastSupertype() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 JSType objectType = ObjectType.INSTANCE;
 assertEquals(objectType, stringBooleanUnion.getLeastSupertype(objectType));
 }

 @Test
 public void testUnionTypeMeet() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(NumberType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringNumberBooleanUnion = stringBooleanUnion.meet(numberBooleanUnion);
 assertEquals("(String, Number, boolean)", stringNumberBooleanUnion.toString());
 }

 @Test(expected = ClassCastException.class)
 public void testUnionTypeGetLeastSupertypeWithDifferentType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 JSType numberType = NumberType.INSTANCE;
 // This should throw a ClassCastException because NumberType is not a subtype of UnionType
 assertEquals(numberType, stringBooleanUnion.getLeastSupertype(numberType));
 }

 @Test
 public void testUnionTypeEquals() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertTrue(stringBooleanUnion1.equals(stringBooleanUnion2));
 }

 @Test
 public void testUnionTypeHashCode() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertEquals(stringBooleanUnion1.hashCode(), stringBooleanUnion2.hashCode());
 }

 @Test
 public void testUnionTypeIsUnionType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertTrue(stringBooleanUnion.isUnionType());
 }

 @Test
 public void testUnionTypeIsObject() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertTrue(stringBooleanUnion.isObject());
 }

 @Test
 public void testUnionTypeToString() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 assertEquals("(String, boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testUnionTypeIsSubtype() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringType = StringType.INSTANCE;
 assertTrue(stringBooleanUnion.isSubtype(stringType));
 }

 @Test
 public void testUnionTypeGetRestrictedUnion() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 JSType numberType = NumberType.INSTANCE;
 UnionType restrictedUnion = stringBooleanUnion.getRestrictedUnion(numberType);
 assertNull(restrictedUnion);
 }

 @Test
 public void testUnionTypeGetRestrictedUnionWithSubtype() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 UnionType stringNumberUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, NumberType.INSTANCE));
 UnionType restrictedUnion = stringBooleanUnion.getRestrictedUnion(stringNumberUnion);
 assertNotNull(restrictedUnion);
 assertEquals("String", restrictedUnion.toString());
 }

 @Test
 public void testUnionTypeGetRestrictedUnionWithNonSubtype() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.DEFAULT, ImmutableSet.of(StringType.INSTANCE, BooleanType.INSTANCE));
 JSType booleanType = BooleanType.INSTANCE;
 UnionType restrictedUnion = stringBooleanUnion.getRestrictedUnion(booleanType);
 assertNull(restrictedUnion);
 }
}