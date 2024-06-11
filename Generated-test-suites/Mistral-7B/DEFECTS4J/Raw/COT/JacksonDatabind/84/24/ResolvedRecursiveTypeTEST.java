package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test case for constructor
 @Test
 public void testResolvedRecursiveTypeConstructor() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
 assertNotNull(resolvedRecursiveType);
 }

 // Test case for setReference method
 @Test
 public void testSetReferenceMethod() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
 JavaType ref = new TypeReference<Object>() {}.getType();
 resolvedRecursiveType.setReference(ref);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertNotNull(selfReferencedType);
 assertSame(ref, selfReferencedType);
 }

 // Test case for getSelfReferencedType method
 @Test
 public void testGetSelfReferencedTypeMethod() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedReccursiveType(erasedType, bindings);
 JavaType ref = new TypeReference<Object>() {}.getType();
 resolvedRecursiveType.setReference(ref);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertNotNull(selfReferencedType);
 assertSame(ref, selfReferencedType);
 }

 // Test case for getGenericSignature and getErasedSignature methods
 @Test
 public void testGetGenericSignatureAndGetErasedSignatureMethods() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
 StringBuilder sb = new StringBuilder();
 JavaType ref = new TypeReference<Object>() {}.getType();
 resolvedRecursiveType.setReference(ref);
 resolvedRecursiveType.getGenericSignature(sb);
 resolvedRecursiveType.getErasedSignature(sb);
 String expected = "[recursive type; <T: java.lang.Object>]";
 String actual = sb.toString();
 assertTrue(expected.equals(actual));
 }

 // Test case for equals method with same instance
 @Test
 public void testEqualsSameInstance() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType resolvedRecursiveType2 = resolvedRecursiveType1;
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }

 // Test case for equals with different instance
 @Test
 public void testEqualsDifferentInstance() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }

 // Test case for equals with null
 @Test(expected = NullPointerException.class)
 public void testEqualsNull() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType.equals(null));
 }

 // Test case for equals with different types
 @Test
 public void testEqualsDifferentTypes() {
 Class<String> erasedType1 = String.class;
 Class<Integer> erasedType2 = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings);
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings);
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }

 // Test case for equals with unresolved reference
 @Test
 public void testEqualsUnresolvedReference() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
 resolvedRecursiveType1.setReference(null);
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }
}