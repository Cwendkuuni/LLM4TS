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
 public void setup() {
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
 assertSame("Trying to re-set self reference; old value = " + refTypeMock1 + ", new = " + refTypeMock, e.getMessage());
 }
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.getSelfReferencedType());

 type.setReference(refTypeMock);
 assertSame(refTypeMock, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertEquals("", sb.toString());

 JavaType refType = mock(JavaType.class);
 when(refType.getGenericSignature(null)).thenReturn("[]T");
 type.setReference(refType);
 type.getGenericSignature(sb);
 assertEquals("[recursive type; [Ljava.lang.Object;]", sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertEquals("", sb.toString());

 JavaType refType = mock(JavaType.class);
 when(refType.getErasedSignature(null)).thenReturn("java.util.List<T>");
 type.setReference(refType);
 type.getErasedSignature(sb);
 assertEquals("[recursive type; java.util.List<T>]", sb.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mock(JavaType.class);
 type.withContentType(contentType);
 assertSame(type, type);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);
 type.withTypeHandler(handler);
 assertSame(type, type);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);
 type.withContentTypeHandler(handler);
 assertSame(type, type);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);
 type.withValueHandler(handler);
 assertSame(type, type);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);
 type.withContentValueHandler(handler);
 assertSame(type, type);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 type.withStaticTyping();
 assertSame(type, type);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type._narrow(Object.class));
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.refine(Object.class, null, null, null));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertEquals("[recursive type; UNRESOLVED]", type.toString());

 JavaType refType = mock(JavaType.class);
 when(refType.getRawClass().getName()).thenReturn("java.util.List");
 type.setReference(refType);
 assertEquals("[recursive type; java.util.List]", type.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type1.equals(type2));

 JavaType refType = mock(JavaType.class);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 type3.setReference(refType);
 assertFalse(type1.equals(type3));

 JavaType refType1 = mock(JavaType.class);
 when(refType1.hashCode()).thenReturn(1);
 JavaType refType2 = mock(JavaType.class);
 when(refType2.hashCode()).thenReturn(1);
 type1.setReference(refType1);
 type3.setReference(refType2);
 assertFalse(type1.equals(type3));
 }
}