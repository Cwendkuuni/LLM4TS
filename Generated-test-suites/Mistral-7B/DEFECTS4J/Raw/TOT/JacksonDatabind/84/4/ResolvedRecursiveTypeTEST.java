package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Test cases for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

 // Test case for setReference method
 @Test
 public void testSetReference() {
  Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
  TypeBindings bindings = new TypeBindings();
  JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
  JavaType javaType2 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);

  ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
  ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);

  type1.setReference(javaType1);
  type2.setReference(javaType2);

  // Test setting reference to self
  try {
   type1.setReference(type1);
   fail("Should not be able to set reference to self.");
  } catch (IllegalStateException e) {
   // Expected exception
  }

  // Test setting reference to different type
  type1.setReference(javaType2);
  ResolvedRecursiveType expectedType = new ResolvedRecursiveType(erasedType, bindings);
  expectedType.setReference(javaType1);

  assertEquals(expectedType, type1);
  assertEquals(javaType1, type1.getSelfReferencedType());
 }

 // Test case for getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
  Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
  TypeBindings bindings = new TypeBindings();
  JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
  JavaType javaType2 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);

  ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
  ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);

  type1.setReference(javaType1);
  type2.setReference(javaType2);

  // Test getSelfReferencedType when not set
  assertNull(type1.getSelfReferencedType());

  // Test getSelfReferencedType when set to different type
  type1.setReference(javaType2);
  assertNotSame(type1, type1.getSelfReferencedType());

  // Test getSelfReferencedType when set to self
  type1.setReference(type1);
  assertEquals(type1, type1.getSelfReferencedType());
 }

 // Test case for getGenericSignature and getErasedSignature methods
 @Test
 public void testGetGenericSignatureAndGetErasedSignature() {
  Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
  TypeBindings bindings = new TypeBindings();
  JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
  JavaType javaType2 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);

  ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
  StringBuilder sb = new StringBuilder(100);

  // Test getGenericSignature when not set
  assertNull(type1.getSelfReferencedType());
  type1.getGenericSignature(sb);
  assertEquals("", sb.toString());

  // Test getGenericSignature when set to different type
  type1.setReference(javaType2);
  type2.getGenericSignature(sb);
  assertEquals(javaType2.getGenericSignature().toString(), sb.toString());

  // Test getGenericSignature when set to self
  type1.setReference(type1);
  type1.getGenericSignature(sb);
  assertEquals(type1.getGenericSignature().toString(), sb.toString());

  // Test getErasedSignature when not set
  assertNull(type1.getSelfReferencedType());
  type1.getErasedSignature(sb);
  assertEquals("", sb.toString());

  // Test getErasedSignature when set to different type
  type1.setReference(javaType2);
  javaType2.getErasedSignature(sb);
  assertEquals(javaType2.getErasedSignature().toString(), sb.toString());

  // Test getErasedSignature when set to self
  type1.setReference(type1);
  type1.getErasedSignature(sb);
  assertEquals(type1.getErasedSignature().toString(), sb.toString());
 }

 // Test case for equals method
 @Test
 public void testEquals() {
  Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
  TypeBindings bindings = new TypeBindings();
  JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
  JavaType javaType2 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
  JavaType javaType3 = TypeFactory.defaultInstance().constructType(Integer.class);

  ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
  ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
  ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType, bindings);

  // Test equality with self
  assertTrue(type1.equals(type1));
  assertTrue(type1.equals(type1));

  // Test equality with same instance
  assertTrue(type1.equals(type1));

  // Test equality with different instance but same type and self-reference
  type1.setReference(javaType1);
  type2.setReference(javaType1);
  assertTrue(type1.equals(type2));

  // Test inequality with different instance and different self-reference
  type1.setReference(javaType1);
  type2.setReference(javaType2);
  assertFalse(type1.equals(type2));

  // Test inequality with different type
  type1.setReference(javaType1);
  JavaType differentType = TypeFactory.defaultInstance().constructType(javaType3.getRawClass());
  assertFalse(type1.equals(differentType));
 }
}