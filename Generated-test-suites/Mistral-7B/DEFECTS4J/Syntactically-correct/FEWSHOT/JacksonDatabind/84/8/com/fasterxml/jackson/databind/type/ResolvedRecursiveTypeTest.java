package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType refTypeMock;

 @Before
 public void setup() {
 refTypeMock = mock(JavaType.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 recursiveType.setReference(refTypeMock);
 assertSame(refTypeMock, recursiveType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 recursiveType.setReference(refTypeMock);
 recursiveType.setReference(refTypeMock);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 assertNull(recursiveType.getSelfReferencedType());

 ResolvedRecursiveType recursiveTypeWithReference = new ResolvedRecursiveType(Object.class, null);
 recursiveTypeWithReference.setReference(refTypeMock);
 assertSame(refTypeMock, recursiveTypeWithReference.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 recursiveType.getGenericSignature(sb);
 assertNull(sb.toString());

 JavaType refTypeWithGenericSignature = mock(JavaType.class);
 when(refTypeWithGenericSignature.getGenericSignature(sb)).thenReturn(sb);
 ResolvedRecursiveType recursiveTypeWithReference = new ResolvedRecursiveType(Object.class, null);
 recursiveTypeWithReference.setReference(refTypeWithGenericSignature);
 recursiveTypeWithReference.getGenericSignature(sb);
 assertSame(sb.toString(), "[]");
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 recursiveType.getErasedSignature(sb);
 assertNull(sb.toString());

 JavaType refTypeWithErasedSignature = mock(JavaType.class);
 when(refTypeWithErasedSignature.getErasedSignature(sb)).thenReturn(sb);
 ResolvedRecursiveType recursiveTypeWithReference = new ResolvedRecursiveType(Object.class, null);
 recursiveTypeWithReference.setReference(refTypeWithErasedSignature);
 recursiveTypeWithReference.getErasedSignature(sb);
 assertSame(sb.toString(), "Object");
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mock(JavaType.class);
 recursiveType.withContentType(contentType);
 assertSame(recursiveType, recursiveType);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 recursiveType.withTypeHandler(handler);
 assertSame(recursiveType, recursiveType);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 recursiveType.withContentTypeHandler(handler);
 assertSame(recursiveType, recursiveType);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 recursiveType.withValueHandler(handler);
 assertSame(recursiveType, recursiveType);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 Object handler = new Object();
 recursiveType.withContentValueHandler(handler);
 assertSame(recursiveType, recursiveType);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 recursiveType.withStaticTyping();
 assertSame(recursiveType, recursiveType);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 Class<?> subclass = String.class;
 assertSame(recursiveType, recursiveType.withNarrow(subclass));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 Class<?> rawType = Object.class;
 TypeBindings bindings = null;
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInterfaces = new JavaType[0];
 recursiveType.refine(rawType, bindings, superClass, superInterfaces);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 assertFalse(recursiveType.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null);
 assertEquals("[recursive type; UNRESOLVED]", recursiveType.toString());

 JavaType refType = mock(JavaType.class);
 when(refType.getRawClass()).thenReturn(String.class);
 ResolvedRecursiveType recursiveTypeWithReference = new ResolvedRecursiveType(Object.class, null);
 recursiveTypeWithReference.setReference(refType);
 assertEquals("[recursive type; [class java.lang.String]]", recursiveTypeWithReference.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(recursiveType1.equals(recursiveType2));

 JavaType refType1 = mock(JavaType.class);
 JavaType refType2 = mock(JavaType.class);
 ResolvedRecursiveType recursiveTypeWithReference1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType recursiveTypeWithReference2 = new ResolvedRecursiveType(Object.class, null);
 recursiveTypeWithReference1.setReference(refType1);
 recursiveTypeWithReference2.setReference(refType2);
 assertFalse(recursiveTypeWithReference1.equals(recursiveTypeWithReference2));

 JavaType sameReference = refType1;
 ResolvedRecursiveType recursiveTypeWithSameReference = new ResolvedRecursiveType(Object.class, null);
 recursiveTypeWithSameReference.setReference(sameReference);
 assertTrue(recursiveTypeWithSameReference.equals(recursiveTypeWithReference1));
 }
}