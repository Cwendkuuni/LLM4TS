package com.yourpackage.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test cases for ResolvedRecursiveType class.
 *
 * @author John Doe
 */
public class ResolvedRecursiveTypeTest {

 private JavaType testType;
 private JavaType referencedType;

 @Test
 public void testConstructor() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 assertNotNull(resolvedRecursiveType);
 }

 @Test
 public void testSetReference() {
 JavaType javaType = new SimpleType(int[].class);
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.setReference(javaType);
 assertSame(javaType, resolvedRecursiveType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType javaType = new SimpleType(int[].class);
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.setReference(javaType);
 resolvedRecursiveType.setReference(javaType);
 }

 @Test
 public void testGetSelfReferencedType() {
 JavaType javaType = new SimpleType(int[].class);
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.setReference(javaType);
 assertSame(javaType, resolvedRecursiveType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder expectedSig = new StringBuilder();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType javaType = new SimpleType(int[].class);
 resolvedRecursiveType.setReference(javaType);
 String result = resolvedRecursiveType.getGenericSignature(expectedSig).toString();
 String expected = "<T extends java.lang.Object>";
 assertSame(expected, result);
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder expectedSig = new StringBuilder();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType javaType = new SimpleType(int[].class);
 resolvedRecursiveType.setReference(javaType);
 String result = resolvedRecursiveType.getErasedSignature(expectedSig).toString();
 String expected = "[Ljava.lang.Object;";
 assertSame(expected, result);
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = new SimpleType(int.class);
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType result = resolvedRecursiveType.withContentType(contentType);
 assertSame(resolvedRecursiveType, result);
 }

 // Add test cases for other methods with similar structure

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwiceWithException() {
 JavaType javaType = new SimpleType(int[].class);
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.setReference(javaType);
 resolvedRecursiveType.setReference(javaType);
 }
}