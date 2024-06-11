package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testConstruct() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNotNull(resolvedRecursiveType);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 ResolvedRecursiveType expected = new ResolvedRecursiveType(String.class, new TypeBindings()) {{
 setReference(javaType);
 }};
 assertEquals(expected, resolvedRecursiveType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 resolvedRecursiveType.setReference(javaType);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 JavaType actual = resolvedRecursiveType.getSelfReferencedType();
 JavaType expected = javaType;
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
 StringBuilder actual = resolvedRecursiveType.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 expected = new StringBuilder("[recursive type; " + javaType.getRawClass().getName() + "]");
 actual = resolvedRecursiveType.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder actual = resolvedRecursiveType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 expected = new StringBuilder("L" + javaType.getRawClass().getName() + ";");
 actual = resolvedRecursiveType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 JavaType actual = resolvedRecursiveType.withContentType(javaType);
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType actual = resolvedRecursiveType.withTypeHandler(handler);
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType actual = resolvedRecursiveType.withContentTypeHandler(handler);
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType actual = resolvedRecursiveType.withValueHandler(handler);
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 JavaType actual = resolvedRecursiveType.withContentValueHandler(handler);
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType actual = resolvedRecursiveType.withStaticTyping();
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> subclass = Integer.class;
 JavaType actual = resolvedRecursiveType.withContentType(JavaType.construct(subclass));
 JavaType expected = resolvedRecursiveType;
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> rawType = Integer.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType actual = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 boolean actual = resolvedRecursiveType.isContainerType();
 boolean expected = false;
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
 StringBuilder actual = new StringBuilder(resolvedRecursiveType.toString());
 assertEquals(expected.toString(), actual.toString());
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType.setReference(javaType);
 expected = new StringBuilder("[recursive type; " + javaType.getRawClass().getName() + "]");
 actual = new StringBuilder(resolvedRecursiveType.toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 JavaType javaType = new SimpleType(Integer.class);
 resolvedRecursiveType1.setReference(javaType);
 resolvedRecursiveType2.setReference(javaType);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }
}