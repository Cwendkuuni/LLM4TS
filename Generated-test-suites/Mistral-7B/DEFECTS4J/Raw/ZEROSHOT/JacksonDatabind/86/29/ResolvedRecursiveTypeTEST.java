package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveType_constructor() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveType_setReference_calledMultipleTimes() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type.setReference(ref);
 type.setReference(ref);
 }

 @Test
 public void testResolvedRecursiveType_getSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type.setReference(ref);
 assertEquals(ref, type.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveType_getGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type.setReference(ref);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertEquals("[recursive type; java.lang.String]", sb.toString());
 }

 @Test
 public void testResolvedRecursiveType_getErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type.setReference(ref);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertEquals("[Ljava.lang.Object;", sb.toString());
 }

 @Test
 public void testResolvedRecursiveType_withContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = new SimpleType(Integer.class);
 JavaType result = type.withContentType(contentType);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveType_withTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveType_withContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withContentTypeHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveType_withValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveType_withContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 JavaType result = type.withContentValueHandler(handler);
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveType_withStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType result = type.withStaticTyping();
 assertEquals(type, result);
 }

 @Test
 public void testResolvedRecursiveType_narrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> subclass = String.class;
 JavaType result = type.narrow(subclass);
 assertNotNull(result);
 assertEquals(type.getClass(), result.getClass());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testResolvedRecursiveType_refine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> rawType = String.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 }

 @Test
 public void testResolvedRecursiveType_isContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testResolvedRecursiveType_toString() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = new SimpleType(String.class);
 type1.setReference(ref);
 assertEquals("[recursive type; java.lang.String]", type1.toString());
 assertEquals("[recursive type; UNRESOLVED]", type2.toString());
 }

 @Test
 public void testResolvedRecursiveType_equals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = new SimpleType(String.class);
 JavaType ref2 = new SimpleType(Integer.class);
 type1.setReference(ref1);
 type2.setReference(ref2);
 assertFalse(type1.equals(type2));
 JavaType ref3 = new SimpleType(String.class);
 type1.setReference(ref3);
 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type1));
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref4 = ref1;
 type3.setReference(ref4);
 assertTrue(type1.equals(type3));
 assertTrue(type3.equals(type1));
 }
}