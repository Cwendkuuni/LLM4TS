package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeConstruction() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = ReferenceType.construct(intType, javaType, null, null);
 assertNotNull(referenceType);
 assertEquals(intType, referenceType.getRawClass());
 assertEquals(javaType.getClass(), referenceType.getContentType().getClass());
 }

 @Test
 public void testGetReferencedType() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = ReferenceType.construct(intType, javaType, null, null);
 assertEquals(javaType, referenceType.getReferencedType());
 }

 @Test
 public void testIsReferenceType() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testContainedTypeCount() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 assertEquals(javaType, referenceType.containedType(0));
 }

 @Test
 public void testContainedTypeName() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 assertEquals("T", referenceType.containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 assertEquals(intType, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 StringBuilder expected = new StringBuilder("I");
 StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 StringBuilder expected = new StringBuilder("I<T>");
 StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testToString() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 String expected = "[reference type, class [I<java.lang.String>]";
 String actual = referenceType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 Class<Integer> intType1 = Integer.TYPE;
 Class<Integer> intType2 = Integer.TYPE;
 JavaType javaType1 = TypeReference.getType(String.class);
 JavaType javaType2 = TypeReference.getType(String.class);
 ReferenceType referenceType1 = new ReferenceType(intType1, javaType1, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(intType2, javaType2, null, null, false);
 ReferenceType referenceType3 = new ReferenceType(intType1, javaType1, null, null, false);
 assertTrue(referenceType1.equals(referenceType1));
 assertTrue(referenceType1.equals(referenceType3));
 assertFalse(referenceType1.equals(referenceType2));
 }

 @Test
 public void testNarrow() {
 Class<Integer> intType = Integer.TYPE;
 Class<Long> longType = Long.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 JavaType narrowedType = referenceType.narrow(longType);
 assertEquals(new ReferenceType(longType, javaType, null, null, false), narrowedType);
 }

 @Test
 public void testWithTypeHandler() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 Object typeHandler = new Object();
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 JavaType withTypeHandler = referenceType.withTypeHandler(typeHandler);
 assertNotSame(referenceType, withTypeHandler);
 assertEquals(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType1 = TypeReference.getType(String.class);
 JavaType javaType2 = TypeReference.getType(StringBuilder.class);
 Object contentTypeHandler = new Object();
 ReferenceType referenceType = new ReferenceType(intType, javaType1, null, null, false);
 JavaType withContentTypeHandler = referenceType.withContentTypeHandler(contentTypeHandler);
 assertNotSame(referenceType, withContentTypeHandler);
 assertEquals(contentTypeHandler, withContentTypeHandler.getContentType().getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 Object valueHandler = new Object();
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 JavaType withValueHandler = referenceType.withValueHandler(valueHandler);
 assertNotSame(referenceType, withValueHandler);
 assertEquals(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType1 = TypeReference.getType(String.class);
 JavaType javaType2 = TypeReference.getType(StringBuilder.class);
 Object contentValueHandler = new Object();
 ReferenceType referenceType = new ReferenceType(intType, javaType1, null, null, false);
 JavaType withContentValueHandler = referenceType.withContentValueHandler(contentValueHandler);
 assertNotSame(referenceType, withContentValueHandler);
 assertEquals(contentValueHandler, withContentValueHandler.getContentType().getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<Integer> intType = Integer.TYPE;
 JavaType javaType = TypeReference.getType(String.class);
 ReferenceType referenceType = new ReferenceType(intType, javaType, null, null, false);
 JavaType withStaticTyping = referenceType.withStaticTyping();
 assertNotSame(referenceType, withStaticTyping);
 assertTrue(withStaticTyping.isStatic());
 }
}