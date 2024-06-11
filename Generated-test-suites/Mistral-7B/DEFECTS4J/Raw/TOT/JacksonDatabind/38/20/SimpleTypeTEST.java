package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass);
 assertEquals(String.class, simpleType.getType());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<String> stringClass = String.class;
 SimpleType original = new SimpleType(stringClass);
 SimpleType copy = new SimpleType(original);
 assertEquals(original, copy);
 }

 @Test
 public void testInvalidTypeConstruction() {
 try {
 Class<List<Integer>> listClass = List.class.getComponentType();
 SimpleType invalidType = new SimpleType(listClass);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStackOverflowErrorWithRecursiveType() {
 Class<SimpleType> simpleTypeClass = SimpleType.class;
 try {
 SimpleType recursiveType = new SimpleType(simpleTypeClass);
 fail("Expected StackOverflowError");
 } catch (StackOverflowError e) {
 // Expected
 }
 }

 @Test
 public void testHashCodeConsistency() {
 Class<String> stringClass = String.class;
 SimpleType simpleType1 = new SimpleType(stringClass);
 SimpleType simpleType2 = new SimpleType(stringClass);
 assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
 }

 @Test
 public void testEquals() {
 Class<String> stringClass = String.class;
 SimpleType simpleType1 = new SimpleType(stringClass);
 SimpleType simpleType2 = new SimpleType(stringClass);
 assertTrue(simpleType1.equals(simpleType2));
 }

 @Test
 public void testNotEqualsDifferentTypes() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 SimpleType stringType = new SimpleType(stringClass);
 SimpleType intType = new SimpleType(intClass);
 assertFalse(stringType.equals(intType));
 }

 @Test
 public void testToString() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass);
 assertEquals("[simple type, class java.lang.String]", simpleType.toString());
 }
}