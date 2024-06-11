package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = createTestType(String.class);
 Assert.assertNotNull(type);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType refType = createTestType(Integer.class);
 type.setReference(refType);
 type.setReference(refType);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = createTestType(String.class);
 type.setReference(createTestType(Integer.class));
 JavaType refType = type.getSelfReferencedType();
 Assert.assertEquals(createTestType(Integer.class), refType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createTestType(String.class);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 type.getGenericSignature(actual);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createTestType(String.class);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 type.getErasedSignature(actual);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType contentType = createTestType(Integer.class);
 JavaType result = type.withContentType(contentType);
 Assert.assertEquals(type, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = createTestType(String.class);
 Object handler = new Object();
 JavaType result = type.withTypeHandler(handler);
 Assert.assertEquals(type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedReccursiveType type = createTestType(String.class);
 Object handler = new Object();
 JavaType result = type.withContentTypeHandler(handler);
 Assert.assertEquals(type, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = createTestType(String.class);
 Object handler = new Object();
 JavaType result = type.withValueHandler(handler);
 Assert.assertEquals(type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = createTestType(String.class);
 Object handler = new Object();
 JavaType result = type.withContentValueHandler(handler);
 Assert.assertEquals(type, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType result = type.withStaticTyping();
 Assert.assertEquals(type, result);
 }

 @Test
 public void testDeprecatedNarrow() {
 ResolvedRecursiveType type = createTestType(String.class);
 Class<?> subclass = Integer.class;
 JavaType result = type._narrow(subclass);
 Assert.assertNull(result);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = createTestType(String.class);
 Class<?> rawType = Integer.class;
 TypeBindings bindings = null;
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 Assert.assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = createTestType(String.class);
 Assert.assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = createTestType(String.class);
 Assert.assertEquals("[recursive type; UNRESOLVED]", type.toString());
 type.setReference(createTestType(Integer.class));
 Assert.assertEquals("[recursive type; [class java.lang.Integer]", type.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = createTestType(String.class);
 ResolvedRecursiveType type2 = createTestType(String.class);
 ResolvedRecursiveType type3 = createTestType(String.class);
 JavaType refType = createTestType(Integer.class);
 type1.setReference(refType);
 type2.setReference(refType);
 Assert.assertFalse(type1.equals(type2));
 Assert.assertFalse(type1.equals(type3));
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Integer.class, null);
 Assert.assertFalse(type1.equals(type4));
 }
}