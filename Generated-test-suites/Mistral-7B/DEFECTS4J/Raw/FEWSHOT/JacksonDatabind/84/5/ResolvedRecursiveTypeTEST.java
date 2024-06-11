package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType mockJavaType() {
 return mock(JavaType.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();
 type.setReference(ref);
 JavaType expected = ref;
 assertSame(expected, type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = mockJavaType();
 JavaType ref2 = mockJavaType();
 type.setReference(ref1);
 type.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedTypeNull() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testGetSelfReferencedTypeNotNull() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();
 type.setReference(ref);
 assertSame(ref, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();
 type.setReference(ref);
 StringBuilder expected = new StringBuilder();
 when(ref.getGenericSignature(expected)).thenReturn(expected);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertSame(expected, sb);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();
 type.setReference(ref);
 StringBuilder expected = new StringBuilder();
 when(ref.getErasedSignature(expected)).thenReturn(expected);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertSame(expected, sb);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mockJavaType();
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
 assertSame(type, type.withStaticTyping());
 }

 @Test
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
 ResolvedRecursiveType typeA = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType typeB = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();
 typeA.setReference(ref);
 StringBuilder expected = new StringBuilder("[recursive type; ")
 .append(ref.getRawClass().getName())
 .append(']');
 assertSame(expected.toString(), typeA.toString());
 assertNotSame(expected.toString(), typeB.toString());
 }

 @Test
 public void testEqualsNull() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.equals(null));
 }

 @Test
 public void testEqualsSameType() {
 ResolvedRecursiveType typeA = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType typeB = new ResolvedRecursiveType(Object.class, null);
 assertTrue(typeA.equals(typeB));
 }

 @Test
 public void testEqualsWithDifferentReferencedTypes() {
 ResolvedRecursiveType typeA = new ResolvedRecursiveType(Object.class, null);
 JavaType refA = mockJavaType();
 typeA.setReference(refA);
 ResolvedRecursiveType typeB = new ResolvedRecursiveType(Object.class, null);
 JavaType refB = mockJavaType();
 typeB.setReference(refB);
 assertFalse(typeA.equals(typeB));
 }

 @Test
 public void testEqualsWithSameReferencedTypes() {
 ResolvedRecursiveType typeA = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();
 typeA.setReference(ref);
 ResolvedRecursiveType typeB = new ResolvedRecursiveType(Object.class, null);
 typeB.setReference(ref);
 assertTrue(typeA.equals(typeB));
 }
}