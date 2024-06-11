package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

 private JavaType referenceType;
 private ResolvedRecursiveType recursiveType;

 @Before
 public void setup() {
 referenceType = new SimpleType(int.class);
 recursiveType = new ResolvedRecursiveType(int[].class, null);
 }

 @Test
 public void testSetReference() {
 recursiveType.setReference(referenceType);
 JavaType gotReference = recursiveType.getSelfReferencedType();
 assertEquals(referenceType, gotReference);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 recursiveType.setReference(referenceType);
 recursiveType.setReference(referenceType);
 fail("Should have thrown an exception");
 }

 @Test
 public void testGetSelfReferencedType() {
 assertNull(recursiveType.getSelfReferencedType());
 recursiveType.setReference(referenceType);
 JavaType gotReference = recursiveType.getSelfReferencedType();
 assertEquals(referenceType, gotReference);
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder expected = new StringBuilder("[recursive type; ");
 expected.append(referenceType.getGenericSignature(new StringBuilder()).toString());
 expected.append("]");
 StringBuilder actual = new StringBuilder(50);
 recursiveType.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder expected = new StringBuilder("[I");
 StringBuilder actual = new StringBuilder(5);
 recursiveType.getErasedSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 JavaType result = recursiveType.withContentType(referenceType);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithTypeHandler() {
 JavaType result = recursiveType.withTypeHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType result = recursiveType.withContentTypeHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithValueHandler() {
 JavaType result = recursiveType.withValueHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType result = recursiveType.withContentValueHandler(null);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testWithStaticTyping() {
 JavaType result = recursiveType.withStaticTyping();
 assertEquals(recursiveType, result);
 }

 @Test
 @Deprecated
 public void testNarrow() {
 try {
 recursiveType._narrow(String.class);
 fail("Should have thrown an exception");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testRefine() {
 try {
 recursiveType.refine(int[].class, null, null, null);
 fail("Should have thrown an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testIsContainerType() {
 assertFalse(recursiveType.isContainerType());
 }

 @Test
 public void testToString() {
 StringBuilder expected = new StringBuilder("[recursive type; ");
 expected.append(referenceType.getRawClass().getName());
 expected.append("]");
 String actual = recursiveType.toString();
 assertEquals(expected.toString(), actual);
 }

 @Test
 public void testEquals() {
 assertFalse(recursiveType.equals(new Object()));
 ResolvedRecursiveType other = new ResolvedRecursiveType(int[].class, null);
 other.setReference(referenceType);
 assertTrue(recursiveType.equals(other));
 }
}