package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for ResolvedRecursiveType
 *
 * @author Johnson.D'Cruz
 */
public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType, TypeBindings bindings) {
 return new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testResolvedRecursiveTypeConstructor() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 assertEquals(stringClass, type.getRawClass());
 assertEquals(bindings, type.getTypeBindings());
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testSetReference() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type1 = createTestType(stringClass, bindings);
 JavaType intType = createTestType(intClass, bindings);
 type1.setReference(intType);
 ResolvedRecursiveType type2 = (ResolvedRecursiveType) type1;
 JavaType selfReferencedType = type2.getSelfReferencedType();
 assertEquals(intType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type1 = createTestType(stringClass, bindings);
 JavaType intType = createTestType(intClass, bindings);
 type1.setReference(intType);
 type1.setReference(intType);
 }

 @Test
 public void testGetSelfReferencedType() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type1 = createTestType(stringClass, bindings);
 JavaType intType = createTestType(intClass, bindings);
 type1.setReference(intType);
 ResolvedRecursiveType type2 = (ResolvedRecursiveType) type1;
 assertEquals(intType, type2.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;<TT:Ljava/lang/Integer;>");
 StringBuilder actual = new StringBuilder();
 type.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder actual = new StringBuilder();
 type.getErasedSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 JavaType javaType = createTestType(intClass, bindings);
 ResolvedRecursiveType expected = createTestType(stringClass, bindings);
 ResolvedRecursiveType actual = (ResolvedRecursiveType) type.withContentType(javaType);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 Object handler = new Object();
 ResolvedRecursiveType expected = createTestType(stringClass, bindings);
 ResolvedRecursiveType actual = (ResolvedRecursiveType) type.withTypeHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 Object handler = new Object();
 ResolvedRecursiveType expected = createTestType(stringClass, bindings);
 ResolvedRecursiveType actual = (ResolvedRecursiveType) type.withContentTypeHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 Object handler = new Object();
 ResolvedRecursiveType expected = createTestType(stringClass, bindings);
 ResolvedRecursiveType actual = (ResolvedRecursiveType) type.withValueHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 Object handler = new Object();
 ResolvedRecursiveType expected = createTestType(stringClass, bindings);
 ResolvedRecursiveType actual = (ResolvedRecursiveType) type.withContentValueHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 ResolvedRecursiveType expected = createTestType(stringClass, bindings);
 ResolvedRecursiveType actual = (ResolvedRecursiveType) type.withStaticTyping();
 assertEquals(expected, actual);
 }

 @Test
 public void testNarrow() {
 fail("_narrow method is deprecated and should not be called");
 }

 @Test
 public void testRefine() {
 fail("refine method returns null");
 }

 @Test
 public void testIsContainerType() {
 assertTrue(false);
 }

 @Test
 public void testToString() {
 Class<String> stringClass = String.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = createTestType(stringClass, bindings);
 JavaType intType = createTestType(intClass, bindings);
 type.setReference(intType);
 String expected = "[recursive type; " + intType.getRawClass().getName() + "]";
 String actual = type.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 Class<String> stringClass = String.class;
 Class<Integer> intClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type1 = createTestType(stringClass, bindings);
 ResolvedRecursiveType type2 = createTestType(stringClass, bindings);
 ResolvedRecursiveType type3 = createTestType(intClass, bindings);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(stringClass, bindings, null, null, 0, null, null, false);
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(stringClass, bindings, null, null, 0, null, null, false);
 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type5));
 assertFalse(type1.equals(type2));
 assertFalse(type1.equals(type3));
 assertFalse(type1.equals(type4));
 }

}