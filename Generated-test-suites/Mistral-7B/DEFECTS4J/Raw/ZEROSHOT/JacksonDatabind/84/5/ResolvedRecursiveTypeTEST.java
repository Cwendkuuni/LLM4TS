package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> clazz) {
 return new ResolvedRecursiveType(clazz, new TypeBindings());
 }

 @Test
 public void testNull() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(null, new TypeBindings());
 assertEquals("null", type.toString());
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testSelfReference() {
 Class<?> clazz = ResolvedRecursiveType.class;
 ResolvedRecursiveType type = createTestType(clazz);
 type.setReference(type);
 assertEquals("[recursive type; "+clazz.getName()+"]", type.toString());
 JavaType ref = type.getSelfReferencedType();
 assertEquals(clazz, ref.getRawClass());
 }

 @Test
 public void testGetSignatures() {
 Class<?> clazz = ResolvedRecursiveType.class;
 ResolvedRecursiveType type = createTestType(clazz);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String signature = sb.toString();
 assertEquals("java.lang.Class<com.fasterxml.jackson.databind.type.ResolvedRecursiveType<T>>", signature);
 type.getErasedSignature(sb);
 signature = sb.toString();
 assertEquals("java.lang.Class<com.fasterxml.jackson.databind.type.ResolvedRecursiveType>", signature);
 }

 @Test
 public void testSetters() {
 Class<?> clazz = ResolvedRecursiveType.class;
 ResolvedRecursiveType type = createTestType(clazz);
 JavaType ref = new TypeReference<ResolvedRecursiveType>() {}.getType();
 type.setReference(ref);
 assertEquals(ref, type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<?> clazz = ResolvedRecursiveType.class;
 ResolvedRecursiveType type = createTestType(clazz);
 JavaType ref = new TypeReference<ResolvedRecursiveType>() {}.getType();
 type.setReference(ref);
 type.setReference(ref);
 }

 @Test
 public void testEquals() {
 Class<?> clazz = ResolvedRecursiveType.class;
 ResolvedRecursiveType type1 = createTestType(clazz);
 ResolvedRecursiveType type2 = createTestType(clazz);
 ResolvedRecursiveType type3 = createTestType(String.class);
 assertFalse(type1.equals(type3));
 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type2));
 }

 @Test
 public void testHashCode() {
 Class<?> clazz = ResolvedRecursiveType.class;
 ResolvedRecursiveType type1 = createTestType(clazz);
 ResolvedRecursiveType type2 = createTestType(clazz);
 ResolvedRecursiveType type3 = createTestType(String.class);
 assertNotSame(type1.hashCode(), type3.hashCode());
 assertSame(type1.hashCode(), type1.hashCode());
 assertSame(type1.hashCode(), type2.hashCode());
 }
}