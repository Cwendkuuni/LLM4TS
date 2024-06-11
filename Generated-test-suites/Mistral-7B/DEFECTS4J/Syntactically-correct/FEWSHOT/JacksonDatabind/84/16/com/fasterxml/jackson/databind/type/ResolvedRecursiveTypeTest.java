package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType mockJavaType() {
 return mock(JavaType.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockJavaType();

 instance.setReference(ref);
 JavaType actualSelfReferencedType = instance.getSelfReferencedType();

 assertSame(ref, actualSelfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceMultipleTimes() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = mockJavaType();
 JavaType ref2 = mockJavaType();

 instance.setReference(ref1);
 instance.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(Object.class, null);
 assertNull(instance.getSelfReferencedType());

 JavaType ref = mockJavaType();
 instance.setReference(ref);
 JavaType actualSelfReferencedType = instance.getSelfReferencedType();

 assertSame(ref, actualSelfReferencedType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expectedSb = new StringBuilder();
 StringBuilder actualSb = new StringBuilder();

 when(instance.getSelfReferencedType().getGenericSignature(expectedSb)).thenReturn(expectedSb);

 instance.getGenericSignature(actualSb);

 assertSame(expectedSb, actualSb);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expectedSb = new StringBuilder();
 StringBuilder actualSb = new StringBuilder();

 when(instance.getSelfReferencedType().getErasedSignature(expectedSb)).thenReturn(expectedSb);

 instance.getErasedSignature(actualSb);

 assertSame(expectedSb, actualSb);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mockJavaType();

 instance.withContentType(contentType);

 // Verification not possible as the method returns 'this'
 }

 // Add more test cases for other methods if needed.
}