package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType mockJavaType;

 @Before
 public void setup() {
 mockJavaType = mock(JavaType.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
 resolvedRecursiveType.setReference(mockJavaType);
 assertSame(mockJavaType, resolvedRecursiveType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
 resolvedRecursiveType.setReference(mockJavaType);
 resolvedRecursiveType.setReference(mockJavaType);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
 assertNull(resolvedRecursiveType.getSelfReferencedType());

 JavaType javaType = mock(JavaType.class);
 resolvedRecursiveType.setReference(javaType);
 assertSame(javaType, resolvedRecursiveType.getSelfReferencedType());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Object.class, null);
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

 ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Object.class, null);
 resolvedRecursiveType3.setReference(mockJavaType);
 resolvedRecursiveType1.setReference(mockJavaType);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType3));
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
 assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());

 JavaType javaType = mock(JavaType.class);
 resolvedRecursiveType.setReference(javaType);
 assertEquals("[recursive type; " + javaType.getRawClass().getName() + "]", resolvedRecursiveType.toString());
 }
}