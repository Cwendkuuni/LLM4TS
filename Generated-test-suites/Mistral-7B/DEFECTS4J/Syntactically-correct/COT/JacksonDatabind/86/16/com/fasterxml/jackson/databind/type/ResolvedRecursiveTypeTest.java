package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testResolvedRecursiveTypeConstructor() {
 ResolvedRecursiveType type = createTestType(String.class);
 assertNotNull(type);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = createTestType(Integer.class);
 type.setReference(ref);
 assertNotNull(type.getSelfReferencedType());
 assertEquals(ref.getClass(), type.getSelfReferencedType().getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = createTestType(Integer.class);
 type.setReference(ref);
 assertThrows(IllegalStateException.class, () -> type.setReference(ref));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = createTestType(String.class);
 assertNull(type.getSelfReferencedType());
 JavaType ref = createTestType(Integer.class);
 type.setReference(ref);
 assertNotNull(type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createTestType(String[].class);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String result = sb.toString();
 String expected = "[Ljava.lang.String;";
 assertEquals(expected, result);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createTestType(List.class);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 String result = sb.toString();
 String expected = "java.util.List";
 assertEquals(expected, result);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = createTestType(String[].class);
 JavaType contentType = createTestType(Integer.class);
 ResolvedRecursiveType result = type.withContentType(contentType);
 assertSame(type, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = createTestType(String[].class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withTypeHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = createTestType(String[].class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withContentTypeHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = createTestType(String[].class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withValueHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = createTestType(String[].class);
 Object handler = new Object();
 ResolvedRecursiveType result = type.withContentValueHandler(handler);
 assertSame(type, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = createTestType(String[].class);
 ResolvedRecursiveType result = type.withStaticTyping();
 assertSame(type, result);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = createTestType(String[].class);
 Class<?> subclass = Integer[].class;
 ResolvedRecursiveType result = type.narrow(subclass);
 assertNull(result);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = createTestType(List.class);
 Class<?> rawType = ArrayList.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = null;
 ResolvedRecursiveType result = (ResolvedRecursiveType) type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = createTestType(String[].class);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = createTestType(String[].class);
 String result = type.toString();
 String expected = "[recursive type; [Ljava.lang.String;]";
 assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = createTestType(String[].class);
 ResolvedRecursiveType type2 = createTestType(String[].class);
 assertFalse(type1.equals(type2));
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String[].class.getComponentType(), null);
 assertFalse(type1.equals(type3));
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(String[].class, null);
 assertSame(type1, type4);
 JavaType ref = createTestType(Integer.class);
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(String[].class, null);
 type5.setReference(ref);
 ResolvedRecursiveType type6 = new ResolvedRecursiveType(String[].class, null);
 type6.setReference(ref);
 assertTrue(type5.equals(type6));
 }
}