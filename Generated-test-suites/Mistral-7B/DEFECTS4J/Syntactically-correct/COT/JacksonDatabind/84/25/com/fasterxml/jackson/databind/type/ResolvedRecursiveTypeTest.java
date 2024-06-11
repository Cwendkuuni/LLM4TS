package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 assertNotNull(resolvedRecursiveType);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType refType = new JavaType(Integer.class, null);

 resolvedRecursiveType.setReference(refType);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertEquals(refType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType refType = new JavaType(Integer.class, null);

 resolvedRecursiveType.setReference(refType);
 resolvedRecursiveType.setReference(new JavaType(Double.class, null));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType refType = new JavaType(Integer.class, null);
 resolvedRecursiveType.setReference(refType);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertEquals(refType, selfReferencedType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder();
 resolvedRecursiveType.getGenericSignature(sb);
 String genericSignature = sb.toString();
 assertTrue(genericSignature.startsWith("[recursive type; "));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 StringBuilder sb = new StringBuilder();
 resolvedRecursiveType.getErasedSignature(sb);
 String erasedSignature = sb.toString();
 fail("Method getErasedSignature is not implemented in ResolvedRecursiveType");
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new JavaType(Integer.class, null);
 ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);
 assertEquals(resolvedRecursiveType, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(handler);
 assertEquals(resolvedRecursiveType, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(handler);
 assertEquals(resolvedRecursiveType, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(handler);
 assertEquals(resolvedRecursiveType, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 Object handler = new Object();
 ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(handler);
 assertEquals(resolvedRecursiveType, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();
 assertEquals(resolvedRecursiveType, result);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 Class<?> subclass = Integer.class;
 ResolvedRecursiveType result = resolvedRecursiveType._narrow(subclass);
 assertNull(result);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 Class<?> rawType = String.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = null;
 ResolvedRecursiveType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 assertFalse(resolvedRecursiveType.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 String toString = resolvedRecursiveType.toString();
 assertTrue(toString.startsWith("[recursive type; "));
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

 JavaType refType1 = new JavaType(Integer.class, null);
 JavaType refType2 = new JavaType(Integer.class, null);

 ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType3.setReference(refType1);
 ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType4.setReference(refType2);

 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
 assertFalse(resolvedRecursiveType3.equals(resolvedRecursiveType4));

 resolvedRecursiveType3.setReference(refType1);
 resolvedRecursiveType4.setReference(refType1);
 assertTrue(resolvedRecursiveType3.equals(resolvedRecursiveType4));
 }
}