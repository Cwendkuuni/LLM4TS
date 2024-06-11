package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {

 private static JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testUnionTypeCreation() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertEquals("(String, Boolean)", stringOrBoolean.toString());
 }

 @Test
 public void testUnionTypeGetAlternates() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 Set<JSType> alternates = stringOrBoolean.getAlternates();
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getStringType()));
 assertTrue(alternates.contains(JSTypeRegistry.getInstance().getBooleanType()));
 }

 @Test
 public void testUnionTypeMatchesContexts() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 // Test that the union type matches contexts for each of its alternates
 assertTrue(stringOrBoolean.matchesStringContext());
 assertTrue(stringOrBoolean.matchesNumberContext());
 assertTrue(stringOrBoolean.matchesObjectContext());

 // Test that the union type fails to match contexts for an unrelated type
 assertFalse(stringOrBoolean.matchesNumberContext(JSTypeRegistry.getInstance().getArrayType(JSTypeRegistry.getInstance().getIntType())));
 }

 @Test
 public void testUnionTypeFindPropertyType() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertEquals(JSTypeRegistry.getInstance().getStringType(), stringOrBoolean.findPropertyType("toString"));
 }

 @Test
 public void testUnionTypeCanAssignTo() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 JSType numberType = JSTypeRegistry.getInstance().getIntType();
 assertTrue(stringOrBoolean.canAssignTo(numberType));
 }

 @Test
 public void testUnionTypeCanBeCalled() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertTrue(stringOrBoolean.canBeCalled());
 }

 @Test
 public void testUnionTypeRestrictByNotNullOrUndefined() {
 UnionType nullableStringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType().restrictByNotNullOrUndefined(),
 JSTypeRegistry.getInstance().getBooleanType().restrictByNotNullOrUndefined()
 )));

 assertEquals("(String|null, Boolean|null)", nullableStringOrBoolean.toString());
 }

 @Test
 public void testUnionTypeTestForEquality() {
 UnionType stringOrBooleanA = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType stringOrBooleanB = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType booleanOrString = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getBooleanType(),
 JSTypeRegistry.getInstance().getStringType()
 )));

 assertEquals(stringOrBooleanA, stringOrBooleanB);
 assertFalse(stringOrBooleanA.equals(booleanOrString));
 }

 @Test
 public void testUnionTypeIsNullable() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertFalse(stringOrBoolean.isNullable());
 }

 @Test
 public void testUnionTypeIsUnknownType() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertFalse(stringOrBoolean.isUnknownType());
 }

 @Test
 public void testUnionTypeGetLeastSupertype() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType anyType = JSTypeRegistry.getInstance().getAnyType();
 JSType leastSupertype = stringOrBoolean.getLeastSupertype(anyType);
 assertEquals(anyType, leastSupertype);
 }

 @Test
 public void testUnionTypeMeet() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType numberOrString = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getIntType(),
 JSTypeRegistry.getInstance().getStringType()
 )));

 UnionType stringOrBooleanOrNumber = stringOrBoolean.meet(numberOrString);
 assertEquals("(String, Boolean, Int)", stringOrBooleanOrNumber.toString());
 }

 @Test
 public void testUnionTypeEquals() {
 UnionType stringOrBooleanA = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType stringOrBooleanB = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType booleanOrString = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getBooleanType(),
 JSTypeRegistry.getInstance().getStringType()
 )));

 assertTrue(stringOrBooleanA.equals(stringOrBooleanB));
 assertFalse(stringOrBooleanA.equals(booleanOrString));
 }

 @Test
 public void testUnionTypeHashCode() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType stringOrBoolean2 = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 UnionType booleanOrString = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getBooleanType(),
 JSTypeRegistry.getInstance().getStringType()
 )));

 assertEquals(stringOrBoolean.hashCode(), stringOrBoolean2.hashCode());
 assertNotSame(stringOrBoolean.hashCode(), booleanOrString.hashCode());
 }

 @Test
 public void testUnionTypeIsUnionType() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertTrue(stringOrBoolean.isUnionType());
 }

 @Test
 public void testUnionTypeIsObject() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));

 assertTrue(stringOrBoolean.isObject());
 }

 @Test
 public void testUnionTypeContains() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 JSType stringType = JSTypeRegistry.getInstance().getStringType();

 assertTrue(stringOrBoolean.contains(stringType));
 }

 @Test
 public void testUnionTypeGetRestrictedUnion() {
 UnionType stringOrBoolean = new UnionType(registry, new HashSet<>(Arrays.asList(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()
 )));
 JSType numberType = JSTypeRegistry.getInstance().getIntType();

 UnionType restricted = stringOrBoolean.getRestrictedUnion(numberType);
 assertTrue(restricted instanceof VoidType);
 }
}