package com.example.junit; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

 private JavaType stringType;
 private JavaType referenceType;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 stringType = new SimpleType(String.class);
 referenceType = new ReferenceType(Integer.class, stringType, null, null, false);
 }

 // Basic test cases for each method

 @Test
 public void testConstruct() {
 ReferenceType refType = ReferenceType.construct(Integer.class, stringType, null, null);
 assertEquals(Integer.class, refType.getRawClass());
 assertEquals(stringType, refType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType typeHandler = new SimpleType(Double.class);
 ReferenceType refType = referenceType.withTypeHandler(typeHandler);
 assertSame(typeHandler, refType.getTypeHandler());
 }

 @Test
 public void testGetReferencedType() {
 JavaType refType = referenceType.getReferencedType();
 assertEquals(stringType, refType);
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
 assertSame(stringType, containedType);
 }

 @Test
 public void testContainedTypeName() {
 assertEquals("T", referenceType.containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 assertSame(Integer.class, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 referenceType.getErasedSignature(sb);
 assertEquals("[I", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 referenceType.getGenericSignature(sb);
 assertEquals("[I <T extends Object>]", sb.toString());
 }

 // Testing edge cases and exception handling

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullClass() {
 ReferenceType.construct(null, stringType, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullRefType() {
 ReferenceType.construct(Integer.class, null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testWithNullTypeHandler() {
 ReferenceType refType = referenceType.withTypeHandler(null);
 }

 @Test(expected = NullPointerException.class)
 public void testWithNullContentTypeHandler() {
 ReferenceType refType = referenceType.withContentTypeHandler(null);
 }

 @Test(expected = NullPointerException.class)
 public void testWithNullValueHandler() {
 ReferenceType refType = referenceType.withValueHandler(null);
 }

 @Test(expected = NullPointerException.class)
 public void testWithNullContentValueHandler() {
 ReferenceType refType = referenceType.withContentValueHandler(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithStaticTypingWithNonStaticType() {
 ReferenceType refType = referenceType.withStaticTyping();
 }

 @Test
 public void testNarrowWithInvalidSubclass() {
 Class<?> invalidSubclass = new Object() {}.getClass();
 JavaType narrowType = referenceType._narrow(invalidSubclass);
 assertNull(narrowType);
 }
}