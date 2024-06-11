package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings());
 }

 @Test
 public void testEmpty() {
 ResolvedRecursiveType emptyType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 assertEquals("java.lang.Object", emptyType.toString());
 assertNull(emptyType.getSelfReferencedType());
 }

 @Test
 public void testSettingReference() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 ResolvedRecursiveType resultType = (ResolvedRecursiveType) baseType.getSelfReferencedType();
 assertEquals("java.lang.String", resultType.toString());
 assertEquals(refType, resultType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingReferenceTwice() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 baseType.setReference(createTestType(Integer.class));
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 StringBuilder sb = new StringBuilder();
 baseType.getGenericSignature(sb);
 String signature = sb.toString();
 assertEquals("[Ljava.lang.String;", signature);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 StringBuilder sb = new StringBuilder();
 baseType.getErasedSignature(sb);
 String signature = sb.toString();
 assertEquals("Ljava/lang/String;", signature);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 JavaType resultType = baseType.withContentType(createTestType(java.util.Collection.class));
 assertEquals(baseType, resultType);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 Object handler = new Object();
 JavaType resultType = baseType.withTypeHandler(handler);
 assertEquals(baseType, resultType);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 Object handler = new Object();
 JavaType resultType = baseType.withContentTypeHandler(handler);
 assertEquals(baseType, resultType);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 Object handler = new Object();
 JavaType resultType = baseType.withValueHandler(handler);
 assertEquals(baseType, resultType);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 Object handler = new Object();
 JavaType resultType = baseType.withContentValueHandler(handler);
 assertEquals(baseType, resultType);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 JavaType resultType = baseType.withStaticTyping();
 assertEquals(baseType, resultType);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 Class<?> subclass = Integer.class;
 JavaType resultType = baseType._narrow(subclass);
 assertEquals(baseType, resultType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 Class<?> rawType = Integer.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = createTestType(java.util.List.class);
 JavaType[] superInterfaces = new JavaType[] { createTestType(java.io.Serializable.class) };
 JavaType resultType = baseType.refine(rawType, bindings, superClass, superInterfaces);
 fail("Refine method should not be called on ResolvedRecursiveType");
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType baseType = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = createTestType(String.class);
 baseType.setReference(refType);
 assertFalse(baseType.isContainerType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType baseType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType1 = createTestType(String.class);
 baseType1.setReference(refType1);
 ResolvedRecursiveType baseType2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType2 = createTestType(String.class);
 baseType2.setReference(refType2);
 assertTrue(baseType1.equals(baseType2));
 ResolvedRecursiveType baseType3 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType3 = createTestType(Integer.class);
 baseType3.setReference(refType3);
 assertFalse(baseType1.equals(baseType3));
 }
}