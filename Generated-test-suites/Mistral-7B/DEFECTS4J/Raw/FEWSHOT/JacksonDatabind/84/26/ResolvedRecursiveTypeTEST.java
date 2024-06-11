package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType mockedJavaType = mock(JavaType.class);

 @Test
 public void testSetReference() {
 ResolvedRecursiveType resolvableType = new ResolvedRecursiveType(Object.class, null);
 resolvableType.setReference(mockedJavaType);
 assertSame(mockedJavaType, resolvableType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType resolvableType = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = mock(JavaType.class);
 JavaType ref2 = mock(JavaType.class);
 resolvableType.setReference(ref1);
 resolvableType.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType resolvableType = new ResolvedRecursiveType(Object.class, null);
 assertNull(resolvableType.getSelfReferencedType());

 JavaType ref = mock(JavaType.class);
 resolvableType.setReference(ref);
 assertSame(ref, resolvableType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType resolvableType = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 resolvableType.getGenericSignature(sb);
 assertNull(sb.toString());

 JavaType ref = mock(JavaType.class);
 resolvableType.setReference(ref);
 when(ref.getGenericSignature(sb)).thenReturn(sb);
 resolvableType.getGenericSignature(sb);
 assertSame(sb.toString(), ref.getGenericSignature(sb).toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType resolvableType = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 resolvableType.getErasedSignature(sb);
 assertNull(sb.toString());

 JavaType ref = mock(JavaType.class);
 resolvableType.setReference(ref);
 when(ref.getErasedSignature(sb)).thenReturn(sb);
 resolvableType.getErasedSignature(sb);
 assertSame(sb.toString(), ref.getErasedSignature(sb).toString());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType resolvableType = new ResolvedRecursiveType(Object.class, null);
 assertNull(resolvableType.getSelfReferencedType());
 String expected = "[recursive type; UNRESOLVED]";
 String actual = resolvableType.toString();
 assertSame(expected, actual);

 JavaType ref = mock(JavaType.class);
 resolvableType.setReference(ref);
 String typeName = "com.test.MyType";
 when(ref.getRawClass().getName()).thenReturn(typeName);
 expected = "[recursive type; " + typeName + "]";
 actual = resolvableType.toString();
 assertSame(expected, actual);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType resolvableType1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType resolvableType2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(resolvableType1.equals(resolvableType2));

 JavaType ref1 = mock(JavaType.class);
 JavaType ref2 = mock(JavaType.class);
 ResolvedRecursiveType resolvableType3 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType resolvableType4 = new ResolvedRecursiveType(Object.class, null);
 resolvableType3.setReference(ref1);
 resolvableType4.setReference(ref2);
 assertFalse(resolvableType3.equals(resolvableType4));

 JavaType sameRef = mock(JavaType.class);
 ResolvedRecursiveType resolvableType5 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType resolvableType6 = new ResolvedRecursiveType(Object.class, null);
 resolvableType5.setReference(sameRef);
 resolvableType6.setReference(sameRef);
 assertTrue(resolvableType5.equals(resolvableType6));
 }
}