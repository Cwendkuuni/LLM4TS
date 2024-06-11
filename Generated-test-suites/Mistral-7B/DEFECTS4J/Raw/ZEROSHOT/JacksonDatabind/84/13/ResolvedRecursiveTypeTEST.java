package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveType_Null_ReferencedType() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Object.class, null);
 assertNull(testType.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveType_Set_ReferencedType() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = new SimpleType(int[].class);
 testType.setReference(refType);
 JavaType selfReferencedType = testType.getSelfReferencedType();
 assertEquals(refType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveType_Set_ReferencedType_Twice() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = new SimpleType(int[].class);
 testType.setReference(refType);
 try {
 testType.setReference(new SimpleType(double.class));
 } catch (IllegalStateException e) {
 assertEquals("Trying to re-set self reference; old value = " + refType + ", new = " + new SimpleType(double.class), e.getMessage());
 }
 }

 @Test
 public void testResolvedRecursiveType_GetSignatures() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 JavaType refType = new SimpleType(int[].class);
 testType.setReference(refType);
 StringBuilder sb = new StringBuilder();
 JavaType selfReferencedTypeSignature = testType.getGenericSignature(sb);
 JavaType erasedSignature = testType.getErasedSignature(sb);
 assertEquals("com.fasterxml.jackson.core.type.ResolvedRecursiveType<[Ljava.lang.String;>", selfReferencedTypeSignature.toString());
 assertEquals("[Ljava.lang.Object;", erasedSignature.toString());
 }

 @Test
 public void testResolvedRecursiveType_WithContentType() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 JavaType contentType = new SimpleType(int[].class);
 JavaType result = testType.withContentType(contentType);
 assertEquals(testType, result);
 }

 @Test
 public void testResolvedRecursiveType_WithTypeHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 Object handler = new Object();
 JavaType result = testType.withTypeHandler(handler);
 assertEquals(testType, result);
 }

 @Test
 public void testResolvedRecursiveType_WithContentTypeHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 Object handler = new Object();
 JavaType result = testType.withContentTypeHandler(handler);
 assertEquals(testType, result);
 }

 @Test
 public void testResolvedRecursiveType_WithValueHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 Object handler = new Object();
 JavaType result = testType.withValueHandler(handler);
 assertEquals(testType, result);
 }

 @Test
 public void testResolvedRecursiveType_WithContentValueHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 Object handler = new Object();
 JavaType result = testType.withContentValueHandler(handler);
 assertEquals(testType, result);
 }

 @Test
 public void testResolvedRecursiveType_WithStaticTyping() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 JavaType result = testType.withStaticTyping();
 assertEquals(testType, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testResolvedRecursiveType_Narrow() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 Class<?> subclass = String[].class.getComponentType().getComponentType();
 JavaType result = testType._narrow(subclass);
 fail("Should throw UnsupportedOperationException.");
 }

 @Test
 public void testResolvedRecursiveType_Refine() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 Class<?> rawType = String[].class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = testType.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveType_IsContainerType() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 boolean result = testType.isContainerType();
 assertFalse(result);
 }

 @Test
 public void testResolvedRecursiveType_ToString() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(String[].class, null);
 String result = testType.toString();
 assertTrue(result.contains("recursive type; [UNRESOLVED]"));
 }

 @Test
 public void testResolvedRecursiveType_Equals() {
 ResolvedRecursiveType testType1 = new ResolvedRecursiveType(String[].class, null);
 ResolvedRecursiveType testType2 = new ResolvedRecursiveType(String[].class, null);
 assertFalse(testType1.equals(testType2));

 JavaType refType = new SimpleType(int[].class);
 ResolvedRecursiveType testType3 = new ResolvedRecursiveType(String[].class, null);
 testType3.setReference(refType);
 ResolvedRecursiveType testType4 = new ResolvedRecursiveType(String[].class, null);
 testType4.setReference(refType);
 assertTrue(testType3.equals(testType4));
 }
}