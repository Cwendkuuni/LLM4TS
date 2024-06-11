package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = SimpleType.construct(String.class);
 JavaType expected = TypeFactory.defaultInstance().constructType(String.class);
 assertEquals(expected, simpleType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotConstructMapType() {
 SimpleType simpleType = SimpleType.construct(Map.class);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotConstructCollectionType() {
 SimpleType simpleType = SimpleType.construct(List.class);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotConstructArrayType() {
 SimpleType simpleType = SimpleType.construct(int[].class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testNarrowingType() {
 SimpleType simpleType = SimpleType.construct(String.class);
 JavaType narrowedType = simpleType.narrow(String[].class);
 JavaType expected = TypeFactory.defaultInstance().constructType(String[].class);
 assertEquals(expected, narrowedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowingDifferentType() {
 SimpleType simpleType = SimpleType.construct(String.class);
 JavaType narrowedType = simpleType.narrow(Integer.class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetCanonicalName() {
 SimpleType simpleType = SimpleType.construct(String.class);
 String expected = "class java.lang.String";
 String actual = simpleType.buildCanonicalName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = SimpleType.construct(String.class);
 StringBuilder expected = new StringBuilder("java.lang.String");
 StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = SimpleType.construct(String.class);
 StringBuilder expected = new StringBuilder("String");
 StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 SimpleType simpleType = SimpleType.construct(String.class);
 String expected = "[simple type, class [Ljava.lang.String;]";
 String actual = simpleType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 SimpleType simpleType1 = SimpleType.construct(String.class);
 SimpleType simpleType2 = SimpleType.construct(String.class);
 assertTrue(simpleType1.equals(simpleType2));
 }

 @Test
 public void testNotEqualsDifferentTypes() {
 SimpleType simpleType1 = SimpleType.construct(String.class);
 SimpleType simpleType2 = SimpleType.construct(Integer.class);
 assertFalse(simpleType1.equals(simpleType2));
 }
}