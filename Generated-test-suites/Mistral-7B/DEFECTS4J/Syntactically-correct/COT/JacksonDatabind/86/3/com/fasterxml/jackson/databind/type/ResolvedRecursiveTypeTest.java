package com.yourpackage.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 // Test constructor
 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNotNull(type);
 }

 // Test setReference method
 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = new JavaType(String.class, null);
 type.setReference(refType);
 assertSame(refType, type.getSelfReferencedType());
 }

 // Test getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = createTestType(Object.class);
 type.setReference(new JavaType(Integer.class, null));
 JavaType expected = new JavaType(Integer.class, null);
 JavaType actual = type.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createTestType(Object.class);
 StringBuilder expected = new StringBuilder("[recursive type; <T>");
 StringBuilder actual = new StringBuilder();
 type.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createTestType(Object.class);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 type.getErasedSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test methods that return this instance
 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = createTestType(Object.class);
 JavaType contentType = new JavaType(Integer.class, null);
 ResolvedRecursiveType result = type.withContentType(contentType);
 assertSame(type, result);
 }

 // Test methods that return this instance
 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = createTestType(Object.class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withTypeHandler(handler);
 assertSame(type, result);
 }

 // Test methods that return this instance
 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = createTestType(Object.class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withContentTypeHandler(handler);
 assertSame(type, result);
 }

 // Test methods that return this instance
 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = createTestType(Object.class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withValueHandler(handler);
 assertSame(type, result);
 }

 // Test methods that return this instance
 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = createTestType(Object.class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withContentValueHandler(handler);
 assertSame(type, result);
 }

 // Test methods that return this instance
 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = createTestType(Object.class);
 ResolvedRecursiveType result = type.withStaticTyping();
 assertSame(type, result);
 }

 // Test deprecated method
 @Test(expected = DeprecationException.class)
 public void testNarrow() {
 ResolvedRecursiveType type = createTestType(Object.class);
 type._narrow(String.class);
 fail("Method should not be callable");
 }

 // Test refine method
 @Test
 public void testRefine() {
 ResolvedRecursiveType type = createTestType(Object.class);
 Class<?> rawType = Object.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = null;
 ResolvedRecursiveType result = (ResolvedRecursiveType) type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 // Test isContainerType method
 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = createTestType(Object.class);
 boolean actual = type.isContainerType();
 assertFalse(actual);
 }

 // Test toString method
 @Test
 public void testToString() {
 ResolvedRecursivetype type = createTestType(Object.class);
 type.setReference(new JavaType(String.class, null));
 String expected = "[recursive type; [Ljava.lang.String;]";
 String actual = type.toString();
 assertEquals(expected, actual);
 }

 // Test equals method
 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type1.equals(type2));
 type2.setReference(new JavaType(Integer.class, null));
 assertFalse(type1.equals(type2));
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 type3.setReference(type1.getSelfReferencedType());
 assertTrue(type1.equals(type3));
 }
}