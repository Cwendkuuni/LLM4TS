package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * JUnit 4 test file for ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

 private JavaType javaType1;
 private JavaType javaType2;
 private JavaType javaType3;
 private ResolvedRecursiveType resolvedRecursiveType1;
 private ResolvedRecursiveType resolvedRecursiveType2;

 @Before
 public void setUp() {
 javaType1 = TypeFactory.defaultInstance().constructType(String.class);
 javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
 javaType3 = TypeFactory.defaultInstance().constructType(ResolvedRecursiveType.class);
 }

 @Test
 public void testResolverecursivetypeConstructor() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNotNull(resolvedRecursiveType1);
 }

 @Test
 public void testSetReference() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 resolvedRecursiveType2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 resolvedRecursiveType1.setReference(resolvedRecursiveType2);
 assertNotNull(resolvedRecursiveType1.getSelfReferencedType());
 assertEquals(resolvedRecursiveType2, resolvedRecursiveType1.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceIllegalStateException() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 resolvedRecursiveType2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 resolvedRecursiveType1.setReference(resolvedRecursiveType2);
 resolvedRecursiveType1.setReference(javaType3);
 }

 @Test
 public void testGetSelfReferencedType() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNull(resolvedRecursiveType1.getSelfReferencedType());
 }

 @Test
 public void testGetterasString() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 String stringRepresentation = resolvedRecursiveType1.toString();
 assertNotNull(stringRepresentation);
 }

 @Test
 public void testEquals() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 resolvedRecursiveType2 = new ResolvedRecursiveType(javaType1, new TypeBindings());
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 resolvedRecursiveType2 = new ResolvedRecursiveType(javaType1, new TypeBindings());
 resolvedRecursiveType2.setReference(resolvedRecursiveType1);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 String stringRepresentation = resolvedRecursiveType1.getGenericSignature(sb).toString();
 assertEquals(expectedType.getGenericSignature().toString(), stringRepresentation);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentType() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = resolvedRecursiveType1.withContentType(javaType3);
 assertNull(javaType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithTypeHandler() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType javaType = resolvedRecursiveType1.withTypeHandler(handler);
 assertNull(javaType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentTypeHandler() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType javaType = resolvedRecursiveType1.withContentTypeHandler(handler);
 assertNull(javaType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithValueHandler() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType javaType = resolvedRecursiveType1.withValueHandler(handler);
 assertNull(javaType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentValueHandler() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType javaType = resolvedRecursiveType1.withContentValueHandler(handler);
 assertNull(javaType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithStaticTyping() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = resolvedRecursiveType1.withStaticTyping();
 assertNull(javaType);
 }

 @Test(expected = DeprecatedException.class)
 public void test_Narrow() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> subclass = String[].class;
 JavaType javaType = resolvedRecursiveType1._narrow(subclass);
 assertNull(javaType);
 }

 @Test
 public void testRefine() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = resolvedRecursiveType1.refine(String[].class, new TypeBindings(), javaType2, new JavaType[0]);
 assertNull(javaType);
 }

 @Test
 public void testIsContainerType() {
 resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType1.isContainerType());
 }
}