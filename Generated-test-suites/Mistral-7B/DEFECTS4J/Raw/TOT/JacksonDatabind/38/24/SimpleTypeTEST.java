package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util. Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 // Test constructor with a simple class
 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType simpleType = new SimpleType(intType);
 JavaType expected = TypeFactory.defaultInstance().constructType(intType);
 assertEquals(expected.getRawClass(), simpleType.getClass());
 assertEquals(expected, simpleType);
 }

 // Test constructor with bindings, superClass, and superInterfaces
 @Test
 public void testSimpleTypeConstructionWithBindingsAndInterfaces() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
 JavaType javaIntType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
 SimpleType simpleType = new SimpleType(listIntType, TypeBindings.emptyBindings(), javaIntType, null);
 JavaType expected = TypeFactory.defaultInstance().constructType(listIntType);
 assertEquals(expected.getRawClass(), simpleType.getClass());
 assertEquals(expected, simpleType);
 }

 // Test Copy constructor
 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType simpleType1 = new SimpleType(intType);
 SimpleType simpleType2 = new SimpleType(simpleType1);
 JavaType expected = TypeFactory.defaultInstance().constructType(intType);
 assertEquals(expected, simpleType1);
 assertEquals(expected, simpleType2);
 }

 // Test constructUnsafe method
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructUnsafeException() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType simpleType = SimpleType.constructUnsafe(intType);
 fail("Expected IllegalArgumentException");
 }

 // Test construct method (deprecated)
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructDeprecatedException() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType simpleType = SimpleType.construct(intType);
 fail("Expected IllegalArgumentException");
 }

 // Test _narrow method
 @Test
 public void testSimpleTypeNarrow() {
 Class<Integer> intType = Integer.TYPE;
 Class<Integer> intSubClass = Integer.class;
 SimpleType simpleType = new SimpleType(intType);
 SimpleType narrowedType = simpleType._narrow(intSubClass);
 assertEquals(intSubClass, narrowedType.getClass());
 }

 // Test isContainerType method
 @Test
 public void testSimpleTypeIsContainerType() {
 SimpleType simpleType = new SimpleType(Integer.TYPE);
 assertFalse(simpleType.isContainerType());
 }

 // Test getErasedSignature and getGenericSignature methods
 @Test
 public void testSimpleTypeSignatures() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
 SimpleType simpleType = new SimpleType(listIntType);
 StringBuilder erasedSignature = new StringBuilder();
 StringBuilder genericSignature = new StringBuilder();
 simpleType.getErasedSignature(erasedSignature);
 simpleType.getGenericSignature(genericSignature);
 String expectedErased = "[Ljava.lang.Integer;";
 String expectedGeneric = "<Ljava.lang.Integer;>";
 assertEquals(expectedErased, erasedSignature.toString());
 assertEquals(expectedGeneric, genericSignature.toString());
 }

 // Test equals and hashCode methods
 @Test
 public void testSimpleTypeEqualsAndHashCode() {
 Class<Integer> intType = Integer.TYPE;
 SimpleType simpleType1 = new SimpleType(intType);
 SimpleType simpleType2 = new SimpleType(intType);
 SimpleType simpleType3 = new SimpleType(String.class);
 assertEquals(simpleType1, simpleType1);
 assertEquals(simpleType1, simpleType2);
 assertNotEquals(simpleType1, simpleType3);
 }

 // Test toString method
 @Test
 public void testSimpleTypeToString() {
 Class<Integer> intType = Integer.TYPE;SimpleType simpleType = new SimpleType(intType);
 String expected = "[simple type, class [I]";
 assertEquals(expected, simpleType.toString());
 }
}