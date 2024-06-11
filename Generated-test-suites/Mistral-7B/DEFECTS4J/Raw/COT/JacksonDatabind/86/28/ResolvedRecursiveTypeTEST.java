package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test cases for ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

 private JavaType createTestType() {
 return new ResolvedRecursiveType(Object.class, null);
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNotNull(type);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = new JavaType(String.class);
 type.setReference(refType);
 type.setReference(new JavaType(Integer.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.getSelfReferencedType());

 JavaType refType = new JavaType(String.class);
 type.setReference(refType);
 assertSame(refType, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertNull(sb.toString());

 JavaType refType = new JavaType(String.class);
 type.setReference(refType);
 type.getGenericSignature(sb);
 assertTrue(sb.toString().contains("String"));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertNull(sb.toString());

 JavaType refType = new JavaType(String.class);
 type.setReference(refType);
 type.getErasedSignature(sb);
 assertTrue(sb.toString().contains("Object"));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type1.equals(type2));

 JavaType refType1 = new JavaType(String.class);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 type3.setReference(refType1);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, null);
 type4.setReference(refType1);
 assertTrue(type3.equals(type4));

 JavaType refType2 = new JavaType(Integer.class);
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(Object.class, null);
 type5.setReference(refType2);
 assertFalse(type3.equals(type5));
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertTrue(type.toString().contains("UNRESOLVED"));

 JavaType refType = new JavaType(String.class);
 type.setReference(refType);
 assertTrue(type.toString().contains("String"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = new JavaType(Integer.class);
 type.withContentType(contentType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 type.withTypeHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 type.withContentTypeHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 type.withValueHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 type.withContentValueHandler(handler);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.withStaticTyping();
 }

 @Test(expected = Deprecated.class)
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type._narrow(String.class);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.refine(Object.class, null, null, new JavaType[0]));
 }
}