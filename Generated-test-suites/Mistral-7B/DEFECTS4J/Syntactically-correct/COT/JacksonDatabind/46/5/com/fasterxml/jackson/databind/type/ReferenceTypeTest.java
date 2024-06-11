package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

 private JavaType referenceType;
 private JavaType referencedType;

 @Test
 public void testConstructor() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 assertNotNull(referenceType);
 assertSame(String.class, referenceType.getClass());
 assertNotNull(referenceType.getContentType());
 assertSame(IntegerType.getInstance().getClass(), referenceType.getContentType().getClass());
 }

 @Test
 public void testGetReferencedType() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 referencedType = referenceType.getReferencedType();
 assertNotNull(referencedType);
 assertSame(IntegerType.getInstance(), referencedType);
 }

 @Test
 public void testIsReferenceType() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testContainedTypeCount() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 assertNotNull(referenceType.containedType(0));
 assertSame(IntegerType.getInstance(), referenceType.containedType(0));
 }

 @Test
 public void testContainedTypeName() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 assertNotNull(referenceType.containedTypeName(0));
 assertEquals("T", referenceType.containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 assertSame(String.class, referenceType.getParameterSource());
 }

 @Test
 public void testNarrow() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType narrowedType = referenceType._narrow(Double.TYPE);
 assertNotNull(narrowedType);
 assertSame(new ReferenceType(Double.TYPE, IntegerType.getInstance(), null, null, false).getClass(), narrowedType.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowInvalidSubclass() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType narrowedType = referenceType._narrow(Float.class);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testGetErasedSignature() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;<T>Lcom/fasterxml/jackson/databind/JavaType;>");
 StringBuilder actual = new StringBuilder();
 referenceType.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testToString() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 String expected = "[reference type, class [Ljava.lang.String;<T>Lcom.fasterxml.jackson.databind.JavaType;>]";
 String actual = referenceType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 ReferenceType refType1 = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 ReferenceType refType2 = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 ReferenceType refType3 = new ReferenceType(String.class, IntegerType.getInstance(), null, null, true);
 assertTrue(refType1.equals(refType1));
 assertTrue(refType1.equals(refType2));
 assertFalse(refType1.equals(refType3));
 }

 // Edge cases and exception handling tests

 @Test(expected = NullPointerException.class)
 public void testNullJavaTypeConstructor() {
 referenceType = new ReferenceType(String.class, null, null, null, false);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullReferencedTypeWithTypeHandler() {
 referenceType = new ReferenceType(String.class, null, null, new Object(){}, false);
 JavaType refTypeWithTypeHandler = referenceType.withTypeHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullReferencedTypeWithContentTypeHandler() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithContentTypeHandler = referenceType.withContentTypeHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullReferencedTypeWithValueHandler() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithValueHandler = referenceType.withValueHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullReferencedTypeWithContentValueHandler() {
 referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithContentValueHandler = referenceType.withContentValueHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidClassType() {
 JavaType refType = JavaType.construct(int[].class, String.class);
 ReferenceType referenceType = new ReferenceType(null, refType, null, null, false);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullReferencedType() {
 ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValueHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithValueHandler = referenceType.withValueHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTypeHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithTypeHandler = referenceType.withTypeHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullContentTypeHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithContentTypeHandler = referenceType.withContentTypeHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullContentValueHandler() {
 ReferenceType referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithContentValueHandler = referenceType.withContentValueHandler(null);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBooleanValue() {
 ReferenceType referenceType = new ReferenceType(String.class, IntegerType.getInstance(), null, null, false);
 JavaType refTypeWithStaticTyping = referenceType.withStaticTyping();
 fail("Should have thrown IllegalArgumentException");
 }
}