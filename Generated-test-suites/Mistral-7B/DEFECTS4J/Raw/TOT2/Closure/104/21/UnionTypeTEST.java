import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertEquals("(String, Boolean)", stringBooleanUnionType.toString());
 }

 @Test
 public void testGetAlternates() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 Set<JSType> alternates = stringBooleanUnionType.getAlternates();
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getNativeStringType()));
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertFalse(alternates.contains(null));
 }

 @Test
 public void testFindPropertyType() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertEquals(JSTypeRegistry.getInstance().getNativeStringType(), stringBooleanUnionType.findPropertyType("length"));
 }

 @Test
 public void testCanAssignTo() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType numberUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeNumberType()));
 JSType stringVariable = JSTypeRegistry.getInstance().getNativeStringType();
 JSType numberVariable = JSTypeRegistry.getInstance().getNativeNumberType();

 assertTrue(stringBooleanUnionType.canAssignTo(stringVariable));
 assertTrue(stringBooleanUnionType.canAssignTo(numberVariable));
 assertFalse(numberUnionType.canAssignTo(stringVariable));
 }

 @Test
 public void testCanBeCalled() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertTrue(stringBooleanUnionType.canBeCalled());
 }

 @Test
 public void testIsNullable() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertFalse(stringBooleanUnionType.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertFalse(stringBooleanUnionType.isUnknownType());
 }

 @Test
 public void testEquals() {
 UnionType stringBooleanUnionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType stringBooleanUnionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType stringBooleanUnionType3 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeNumberType()));

 assertTrue(stringBooleanUnionType1.equals(stringBooleanUnionType2));
 assertFalse(stringBooleanUnionType1.equals(stringBooleanUnionType3));
 }

 @Test
 public void testMeet() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType numberStringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeNumberType(), JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType result = stringBooleanUnionType.meet(numberStringBooleanUnionType);
 UnionType expectedResult = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertEquals(expectedResult, result);
 }

 @Test
 public void testRestrictedUnion() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType stringUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType()));
 UnionType restrictedStringBooleanUnionType = stringBooleanUnionType.getRestrictedUnion(stringUnionType);
 assertEquals("String", restrictedStringBooleanUnionType.toString());
 }

 @Test
 public void testToString() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 assertEquals("(String, Boolean)", stringBooleanUnionType.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidUnionCreation() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeStringType()));
 }

 @Test
 public void testIsSubtype() {
 UnionType stringBooleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType stringUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType()));
 UnionType booleanUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeBooleanType()));
 UnionType stringNumberUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNativeStringType(), JSTypeRegistry.getInstance().getNativeNumberType()));

 assertTrue(stringBooleanUnionType.isSubtype(stringUnionType));
 assertTrue(stringBooleanUnionType.isSubtype(booleanUnionType));
 assertFalse(stringBooleanUnionType.isSubtype(numberStringBooleanUnionType));
 assertFalse(stringNumberUnionType.isSubtype(stringBooleanUnionType));
 }
}