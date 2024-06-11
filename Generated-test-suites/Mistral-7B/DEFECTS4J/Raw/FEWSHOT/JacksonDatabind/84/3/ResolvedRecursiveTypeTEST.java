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
 type.setReference(refTypeMock);
 type.setReference(refTypeMock);
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
 assertEquals("", sb.toString());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 String expected = refType.getRawClass().getName().replaceAll("\\$", "").replaceAll("\\[", "<").replaceAll("]", ">");
 sb = new StringBuilder(expected.length() + 40);
 sb.append("[recursive type; ");
 sb.append(refType.getRawClass().getName());
 sb.append("]");
 type.getGenericSignature(sb);
 assertEquals(sb.toString(), refType.getGenericSignature(new StringBuilder()).toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertEquals("", sb.toString());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 sb = new StringBuilder(refType.getRawClass().getName().length() + 40);
 sb.append("[recursive type; ");
 sb.append(refType.getRawClass().getName());
 sb.append("]");
 type.getErasedSignature(sb);
 assertEquals(sb.toString(), refType.getErasedSignature(new StringBuilder()).toString());
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
 @Deprecated
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> subclass = String.class;
 assertSame(type, type.withNarrow(subclass));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 Class<?> rawType = Object.class;
 TypeBindings bindings = null;
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInterfaces = new JavaType[0];
 type.refine(rawType, bindings, superClass, superInterfaces);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
 String expected = "[recursive type; ]";
 assertEquals(expected, type.toString());

 JavaType refType = mock(JavaType.class);
 type.setReference(refType);
 String refTypeName = refType.getRawClass().getName().replaceAll("\\$", "").replaceAll("\\[", "<").replaceAll("]", ">");
 expected = "[recursive type; " + refTypeName + "]";
 type.toString();
 String actual = type.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(type1.equals(type2));

 ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = mock(JavaType.class);
 type3.setReference(refType);
 assertFalse(type1.equals(type3));

 JavaType refType2 = mock(JavaType.class);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, null);
 type4.setReference(refType2);
 assertFalse(type1.equals(type4));

 JavaType refType3 = mock(JavaType.class);
 type1.setReference(refType3);
 JavaType refType4 = mock(JavaType.class);
 type2.setReference(refType4);
 assertSame(refType3, refType4);
 assertTrue(type1.equals(type2));
 }
}