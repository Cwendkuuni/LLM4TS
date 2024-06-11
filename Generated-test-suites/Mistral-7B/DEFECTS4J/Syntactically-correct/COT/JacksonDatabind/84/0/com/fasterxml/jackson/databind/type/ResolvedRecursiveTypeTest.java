package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testResolvedRecursiveTypeConstructor() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(intType, null);
 assertNotNull(resolvedType);
 assertEquals(intType, resolvedType.getRawClass());
 assertNull(resolvedType.getSelfReferencedType());
 }

 @Test
 public void testSetReference() {
 Class<Integer> intType = Integer.TYPE;
 Class<String> strType = String.class;
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(intType, null);
 ResolvedRecursiveType resolvedStrType = new ResolvedRecursiveType(strType, null);

 resolvedIntType.setReference(resolvedStrType);
 JavaType expected = resolvedStrType;
 JavaType actual = resolvedIntType.getSelfReferencedType();
 assertEquals("Incorrect self-referenced type set", expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<Integer> intType = Integer.TYPE;
 Class<String> strType = String.class;
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(intType, null);
 ResolvedRecursiveType resolvedStrType = new ResolvedRecursiveType(strType, null);

 resolvedIntType.setReference(resolvedStrType);
 resolvedIntType.setReference(resolvedStrType); // this should fail
 }

 @Test
 public void testGetGenericSignature() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType resolvedIntType = createTestType(intType);
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = new StringBuilder(40);
 resolvedIntType.getGenericSignature(actual);
 assertEquals("Incorrect generic signature", expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType resolvedIntType = createTestType(intType);
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = new StringBuilder(40);
 resolvedIntType.getErasedSignature(actual);
 assertEquals("Incorrect erased signature", expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 Class<Integer> intType = Integer.TYPE;
 JavaType contentType = JavaType.construct(int[].class);
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(intType, null);
 JavaType actual = resolvedIntType.withContentType(contentType);
 assertSame("WithContentType method should return the same instance", resolvedIntType, actual);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType actual = resolvedIntType.withTypeHandler(handler);
 assertSame("WithTypeHandler method should return the same instance", resolvedIntType, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType actual = resolvedIntType.withContentTypeHandler(handler);
 assertSame("WithContentTypeHandler method should return the same instance", resolvedIntType, actual);
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object();
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType actual = resolvedIntType.withValueHandler(handler);
 assertSame("WithValueHandler method should return the same instance", resolvedIntType, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object();
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType actual = resolvedIntType.withContentValueHandler(handler);
 assertSame("WithContentValueHandler method should return the same instance", resolvedIntType, actual);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(Integer.TYPE, null);
 JavaType actual = resolvedIntType.withStaticTyping();
 assertSame("WithStaticTyping method should return the same instance", resolvedIntType, actual);
 }

 @Test
 public void testNarrow() {
 // Deprecated method, no test case required
 }

 @Test
 public void testRefine() {
 Class<?> rawType = Integer.TYPE;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(rawType, bindings);
 JavaType actual = resolvedIntType.refine(rawType, bindings, superClass, superInterfaces);
 assertNull("Refine method should return null for ResolvedRecursiveType", actual);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(Integer.TYPE, null);
 assertFalse("ResolvedRecursiveType should not be a container type", resolvedIntType.isContainerType());
 }

 @Test
 public void testToString() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType resolvedIntType = new ResolvedRecursiveType(intType, null);
 String expected = "[recursive type; UNRESOLVED]";
 String actual = resolvedIntType.toString();
 assertEquals("Incorrect toString output", expected, actual);

 resolvedIntType.setReference(intType);
 expected = "[recursive type; " + intType.getName() + "]";
 actual = resolvedIntType.toString();
 assertEquals("Incorrect toString output", expected, actual);
 }

 @Test
 public void testEquals() {
 Class<Integer> intType = Integer.TYPE;
 ResolvedRecursiveType resolvedIntType1 = new ResolvedRecursiveType(intType, null);
 ResolvedRecursiveType resolvedIntType2 = new ResolvedRecursiveType(intType, null);
 assertTrue("Unequal instances of same type should be considered equal", resolvedIntType1.equals(resolvedIntType2));

 resolvedIntType1.setReference(intType);
 resolvedIntType2.setReference(intType);
 assertTrue("Equal instances with same self-referenced type should be considered equal", resolvedIntType1.equals(resolvedIntType2));

 ResolvedRecursiveType resolvedStrType = new ResolvedRecursiveType(String.class, null);
 assertFalse("Unequal instances of different types should be considered unequal", resolvedIntType1.equals(resolvedStrType));
 }
}