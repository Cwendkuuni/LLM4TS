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
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 testType.setReference(refTypeMock);
 testType.setReference(refTypeMock);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 assertNull(testType.getSelfReferencedType());

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 StringBuilder sb = new StringBuilder();
 assertNull(testType.getGenericSignature(sb));

 testType.setReference(refTypeMock);
 when(refTypeMock.getGenericSignature(sb)).thenReturn(sb);
 assertSame(sb, testType.getGenericSignature(sb));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 StringBuilder sb = new StringBuilder();
 assertNull(testType.getErasedSignature(sb));

 testType.setReference(refTypeMock);
 when(refTypeMock.getErasedSignature(sb)).thenReturn(sb);
 assertSame(sb, testType.getErasedSignature(sb));
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 JavaType contentType = mock(JavaType.class);
 assertNull(testType.withContentType(contentType));

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.withContentType(contentType));
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 Object handler = mock(Object.class);
 assertNull(testType.withTypeHandler(handler));

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 Object handler = mock(Object.class);
 assertNull(testType.withContentTypeHandler(handler));

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 Object handler = mock(Object.class);
 assertNull(testType.withValueHandler(handler));

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 Object handler = mock(Object.class);
 assertNull(testType.withContentValueHandler(handler));

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType testType = new ResolvedRecursiveType(Null.class, null);
 assertNull(testType.withStaticTyping());

 testType.setReference(refTypeMock);
 assertSame(refTypeMock, testType.withStaticTyping());
 }
}