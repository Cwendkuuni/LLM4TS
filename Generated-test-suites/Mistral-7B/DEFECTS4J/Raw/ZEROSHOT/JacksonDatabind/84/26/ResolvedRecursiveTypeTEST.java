package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings() {}) {
 // override to avoid creating infinite recursion when testing getSelfReferencedType()
 @Override
 public JavaType getSelfReferencedType() { return null; }
 };
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type.setReference(refType);
 JavaType selfRefType = type.getSelfReferencedType();
 assertNull(selfRefType); // initial state
 refType.setSelfReferencedType(type);
 type.setReference(refType);
 selfRefType = type.getSelfReferencedType();
 assertSame(refType, selfRefType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type.setReference(refType);
 type.setReference(refType); // exception should be thrown
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 assertNull(type.getSelfReferencedType());
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type.setReference(refType);
 JavaType selfRefType = type.getSelfReferencedType();
 assertSame(refType, selfRefType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String signature = sb.toString();
 assertTrue(signature.contains("[recursive type; "));
 assertFalse(signature.contains("UNRESOLVED"));
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type.setReference(refType);
 sb = new StringBuilder();
 type.getGenericSignature(sb);
 signature = sb.toString();
 assertTrue(signature.contains("[recursive type; "));
 assertTrue(signature.contains(refType.getRawClass().getName()));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 String signature = sb.toString();
 assertTrue(signature.contains("[recursive type; "));
 assertFalse(signature.contains("UNRESOLVED"));
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type.setReference(refType);
 sb = new StringBuilder();
 type.getErasedSignature(sb);
 signature = sb.toString();
 assertTrue(signature.contains("[recursive type; "));
 assertTrue(signature.contains(refType.getRawClass().getName()));
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType contentType = new TypeReference<List<String>>(){}.getType();
 JavaType resultType = type.withContentType(contentType);
 assertSame(type, resultType);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 Object handler = new Object();
 JavaType resultType = type.withTypeHandler(handler);
 assertSame(type, resultType);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 Object handler = new Object();
 JavaType resultType = type.withContentTypeHandler(handler);
 assertSame(type, resultType);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 Object handler = new Object();
 JavaType resultType = type.withValueHandler(handler);
 assertSame(type, resultType);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 Object handler = new Object();
 JavaType resultType = type.withContentValueHandler(handler);
 assertSame(type, resultType);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 JavaType resultType = type.withStaticTyping();
 assertSame(type, resultType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 type._narrow(String.class);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 Class<?> rawType = String[].class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = Object.class;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType resultType = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(resultType);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 String toString = type.toString();
 assertTrue(toString.contains("[recursive type; "));
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type.setReference(refType);
 toString = type.toString();
 assertTrue(toString.contains("[recursive type; "));
 assertTrue(toString.contains(refType.getRawClass().getName()));
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
 assertFalse(type1.equals(type2));
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 type1.setReference(refType);
 type2.setReference(refType);
 assertTrue(type1.equals(type2));
 }
}