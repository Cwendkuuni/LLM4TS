package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

 private JavaType refType = new JavaType(String.class);
 private JavaType anchorType = new JavaType(Integer.class);

 @Test
 public void testConstructorWithClassRefType() {
 ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refType);
 assertEquals("java.lang.String", referenceType.getCanonicalName());
 assertEquals(refType, referenceType.getReferencedType());
 }

 @Test
 public void testConstructorWithClassAnchorType() {
 ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, null, anchorType);
 assertEquals("java.lang.Integer", referenceType.getCanonicalName());
 assertEquals(null, referenceType.getReferencedType());
 assertEquals(anchorType, referenceType.getAnchorType());
 }

 @Test
 public void testUpgradeFromSimpleType() {
 JavaType simpleType = new JavaType(Integer.class);
 ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
 assertEquals("java.lang.Integer", referenceType.getCanonicalName());
 assertEquals(refType, referenceType.getReferencedType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromNonType() {
 JavaType simpleType = new JavaType(String.class);
 ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, new Object());
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testIsReferenceType() {
 assertTrue(new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false).isReferenceType());
 }

 @Test
 public void testHasContentType() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertTrue(referenceType.hasContentType());
 }

 @Test
 public void testGetContentType() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertEquals(refType, referenceType.getContentType());
 }

 @Test
 public void testGetReferencedType() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertEquals(refType, referenceType.getReferencedType());
 }

 @Test
 public void testGetAnchorType() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertNull(referenceType.getAnchorType());
 }

 @Test
 public void testIsAnchorType() {
 ReferenceType referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, null, anchorType, null, null, false);
 assertTrue(referenceType.isAnchorType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromNullReferenceType() {
 JavaType simpleType = new JavaType(Integer.class);
 ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, null);
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testConstructorWithTypeBindingsRefType() {
 ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertEquals("java.lang.String", referenceType.getCanonicalName());
 assertEquals(refType, referenceType.getReferencedType());
 }

 @Test
 public void testConstructorWithTypeBindingsAnchorType() {
 ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, null, anchorType, null, null, false);
 assertEquals("java.lang.Integer", referenceType.getCanonicalName());
 assertEquals(null, referenceType.getReferencedType());
 assertEquals(anchorType, referenceType.getAnchorType());
 }

 @Test
 public void testConstructorWithTypeBindingsRefTypeAndAnchorType() {
 ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refType, anchorType, null, null, false);
 assertEquals("java.lang.String", referenceType.getCanonicalName());
 assertEquals(refType, referenceType.getReferencedType());
 assertEquals(anchorType, referenceType.getAnchorType());
 }

 @Test
 public void testGetErasedSignature() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;<T:Ljava/lang/Object;>");
 StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = new JavaType(Double.class);
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentType(contentType);
 assertEquals(contentType, newReferenceType.getReferencedType());
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = () -> "Handler";
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
 assertEquals(handler, newReferenceType._typeHandler);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = () -> "Handler";
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
 assertEquals(handler, newReferenceType._referencedType.<Object>getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Object handler = () -> "Handler";
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(handler);
 assertEquals(handler, newReferenceType._valueHandler);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = () -> "Handler";
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
 assertEquals(handler, newReferenceType._referencedType.<Object>getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertTrue(newReferenceType._asStatic);
 }

 @Test
 public void testRefine() {
 Class<?> rawType = String[].class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = Object[].class;
 JavaType[] superInterfaces = new JavaType[] { List.class };
 JavaType refinedType = referenceType.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(new JavaType(List.class, TypeParameterList.empty(), new JavaType[] { refType }), refinedType);
 }

 @Test
 public void testToString() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertEquals("[reference type, class java.lang.String<T:Ljava/lang/Object;>]", referenceType.toString());
 }

 @Test
 public void testEquals() {
 ReferenceType referenceType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertTrue(referenceType1.equals(referenceType2));
 }

 @Test
 public void testEqualsDifferentClasses() {
 ReferenceType referenceType1 = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertFalse(referenceType1.equals(referenceType2));
 }

 @Test
 public void testEqualsNull() {
 ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 assertFalse(referenceType.equals(null));
 }
}