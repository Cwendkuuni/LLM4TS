package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType refTypeMock;

 @Before
 public void setUp() {
 refTypeMock = mock(JavaType.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.setReference(refTypeMock);
 assertSame(refTypeMock, type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType refTypeMock1 = mock(JavaType.class);
 type.setReference(refTypeMock1);
 try {
 type.setReference(refTypeMock);
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.getSelfReferencedType());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 assertSame(refType, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertNull(sb.toString());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 String expected = "[recursive type; " + refType.getRawClass().getName() + "]";
 StringBuilder sb2 = new StringBuilder(expected.length());
 type.getGenericSignature(sb2);
 assertSame(expected, sb2.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertNull(sb.toString());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 String expected = refType.getRawClass().getName();
 StringBuilder sb2 = new StringBuilder(expected.length());
 type.getErasedSignature(sb2);
 assertSame(expected, sb2.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mock(JavaType.class);
 assertSame(type, type.withContentType(contentType));
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 assertSame(type, type.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 assertSame(type, type.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 assertSame(type, type.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 assertSame(type, type.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertSame(type, type.withStaticTyping());
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertSame(type, type.withNarrow(null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.refine(null, null, null, null));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertSame("[]", type.toString());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 String expected = "[recursive type; " + refType.getRawClass().getName() + "]";
 assertSame(expected, type.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type1.equals(type2));

 JavaType refType = mock(JavaType.class);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 type3.setReference(refType);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, null);
 type4.setReference(refType);
 assertTrue(type3.equals(type4));
 }
}