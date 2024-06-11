import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testUnionTypeGetAlternates() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 Iterable<JSType> alternates = stringBooleanUnion.getAlternates();
 assertEquals(2, alternates.size());
 }

 @Test
 public void testUnionTypeFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 String propertyName = "length";
 JSType stringPropertyType = JSType.getStringType();
 JSType booleanPropertyType = JSType.getBooleanType();
 JSType stringBooleanPropertyType = stringBooleanUnion.findPropertyType(propertyName);
 assertEquals(stringPropertyType, stringBooleanPropertyType);
 }

 @Test
 public void testUnionTypeCanAssignTo() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 UnionType stringUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType()));
 UnionType booleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getBooleanType()));
 JSType numberType = JSType.getNumberType();

 assertTrue(stringBooleanUnion.canAssignTo(stringUnion));
 assertTrue(stringBooleanUnion.canAssignTo(booleanUnion));
 assertTrue(stringBooleanUnion.canAssignTo(numberType));
 assertFalse(numberType.canAssignTo(stringBooleanUnion));
 }

 @Test
 public void testUnionTypeRestrictByNotNullOrUndefined() {
 UnionType nullableUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getNullType()));
 UnionType restrictedNullableUnion = nullableUnion.restrictByNotNullOrUndefined();
 assertEquals("String", restrictedNullableUnion.getAlternates().iterator().next().getName());
 assertFalse(restrictedNullableUnion.isNullable());
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeEqualsNullObject() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertEquals(stringBooleanUnion, null); // expected AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeHashCodeEqualsNullObject() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertEquals(stringBooleanUnion.hashCode(), 0); // expected AssertionError
 }

 @Test
 public void testUnionTypeIsUnionType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertTrue(stringBooleanUnion.isUnionType());
 }

 @Test
 public void testUnionTypeIsObject() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertTrue(stringBooleanUnion.isObject());
 }

 @Test
 public void testUnionTypeToString() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testUnionTypeEquals() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 assertTrue(stringBooleanUnion1.equals(stringBooleanUnion2));
 }

 @Test
 public void testUnionTypeMeet() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getBooleanType()));
 UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getNumberType(), JSType.getBooleanType()));
 UnionType stringNumberUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSType.getStringType(), JSType.getNumberType()));
 UnionType meetResult = stringBooleanUnion.meet(numberBooleanUnion);
 assertEquals(stringNumberUnion, meetResult);
 }
}