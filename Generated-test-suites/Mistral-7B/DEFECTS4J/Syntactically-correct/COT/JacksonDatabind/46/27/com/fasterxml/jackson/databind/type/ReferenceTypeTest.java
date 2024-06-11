package com.example.junit4; // replace with your test package

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 private JavaType refType = JavaType.construct(String.class, Integer.class);
 private ReferenceType referenceType;

 @Test
 public void testConstructor() {
 referenceType = new ReferenceType(String.class, refType, null, null, false);
 assertNotNull(referenceType);
 }

 @Test
 public void testConstruct() {
 referenceType = ReferenceType.construct(String.class, refType, null, null);
 assertNotNull(referenceType);
 assertEquals(String.class, referenceType.getRawClass());
 assertEquals(Integer.class, referenceType.getContentType().getRawClass());
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 referenceType = referenceType.withTypeHandler(handler);
 assertNotSame(referenceType._typeHandler, handler);
 }

 @Test(expected = IllegalStateException.class)
 public void testWithTypeHandlerTwice() {
 referenceType = referenceType.withTypeHandler(new Object());
 try {
 referenceType = referenceType.withTypeHandler(new Object());
 } catch (IllegalStateException e) {
 assertEquals("Can't set type handler twice", e.getMessage());
 }
 }

 @Test
 public void testBuildCanonicalName() {
 assertEquals("java.lang.String[reference type, class com.fasterxml.jackson.databind.type.TypeReference<class java.lang.Integer>]",
 referenceType.buildCanonicalName());
 }

 @Test
 public void testNarrow() {
 JavaType narrowedType = referenceType._narrow(String[].class);
 assertNotNull(narrowedType);
 assertEquals(String[].class, narrowedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidClass() {
 JavaType narrowedType = referenceType._narrow(Object.class);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetReferencedType() {
 JavaType referencedType = referenceType.getReferencedType();
 assertEquals(refType, referencedType);
 }

 @Test
 public void testIsReferenceType() {
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testContainedTypeCount() {
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 JavaType containedType = referenceType.containedType(0);
 assertEquals(refType, containedType);
 }

 @Test
 public void testContainedTypeName() {
 String containedTypeName = referenceType.containedTypeName(0);
 assertEquals("T", containedTypeName);
 }

 @Test
 public void testGetParameterSource() {
 assertEquals(String.class, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 referenceType.getErasedSignature(sb);
 assertEquals("String", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 referenceType.getGenericSignature(sb);
 assertEquals("[Ljava/lang/String;<T:Ljava/lang/Integer;>", sb.toString());
 }

 @Test
 public void testToString() {
 assertEquals("[reference type, class java.lang.String<T:Ljava.lang.Integer>]",
 referenceType.toString());
 }

 @Test
 public void testEquals() {
 ReferenceType other = new ReferenceType(String.class, refType, null, null, false);
 assertTrue(referenceType.equals(other));
 }

 @Test
 public void testEqualsNull() {
 assertFalse(referenceType.equals(null));
 }

 @Test
 public void testEqualsSameInstance() {
 assertTrue(referenceType.equals(referenceType));
 }

 @Test
 public void testEqualsDifferentClass() {
 ReferenceType other = new ReferenceType(Integer.class, refType, null, null, false);
 assertFalse(referenceType.equals(other));
 }

 @Test
 public void testEqualsDifferentReferencedType() {
 ReferenceType other = new ReferenceType(String.class, JavaType.construct(String.class, String.class), null, null, false);
 assertFalse(referenceType.equals(other));
 }

 @Test
 public void testHashCode() {
 assertEquals(referenceType.hashCode(), referenceType.hashCode());
 JavaType refType2 = JavaType.construct(String.class, Integer.class);
 ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null, null, false);
 assertNotEquals(referenceType.hashCode(), referenceType2.hashCode());
 }
}