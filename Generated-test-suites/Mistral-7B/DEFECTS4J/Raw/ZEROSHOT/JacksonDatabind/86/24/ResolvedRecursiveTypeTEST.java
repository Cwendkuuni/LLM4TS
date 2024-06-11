package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings()) {
 };
 }

 @Test
 public void testNewInstance() {
 Class<?> erasedType = Object.class;
 ResolvedRecursiveType resolvedRecursiveType = createResolvedRecursiveType(erasedType);
 assertEquals(erasedType.getName(), resolvedRecursiveType.getErasedSignature(new StringBuilder()).toString());
 assertNull(resolvedRecursiveType.getSelfReferencedType());
 }

 @Test
 public void testSetReference() {
 Class<?> erasedType = Object.class;
 JavaType refType = createResolvedRecursiveType(String.class);
 ResolvedRecursiveType resolvedRecursiveType = createResolvedRecursiveType(erasedType);
 resolvedRecursiveType.setReference(refType);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertEquals(refType.getRawClass(), selfReferencedType.getRawClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 Class<?> erasedType = Object.class;
 JavaType refType = createResolvedRecursiveType(String.class);
 ResolvedRecursiveType resolvedRecursiveType = createResolvedRecursiveType(erasedType);
 resolvedRecursiveType.setReference(refType);
 resolvedRecursiveType.setReference(refType);
 }

 @Test
 public void testGetGenericSignature() {
 Class<?> erasedType = Object.class;
 JavaType refType = createResolvedRecursiveType(String.class);
 ResolvedRecursiveType resolvedRecursiveType = createResolvedRecursiveType(erasedType);
 resolvedRecursiveType.setReference(refType);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder actual = resolvedRecursiveType.getGenericSignature(new StringBuilder());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 Class<?> erasedType = Object.class;
 JavaType refType = createResolvedRecursiveType(String.class);
 ResolvedRecursiveType resolvedRecursiveType = createResolvedRecursiveType(erasedType);
 resolvedRecursiveType.setReference(refType);
 StringBuilder expected = new StringBuilder(refType.getRawClass().getName());
 expected.append("+");
 StringBuilder actual = resolvedRecursiveType.getErasedSignature(new StringBuilder());
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 Class<?> erasedType = Object.class;
 JavaType refType1 = createResolvedRecursiveType(erasedType);
 JavaType refType2 = createResolvedRecursiveType(erasedType);
 JavaType refType3 = createResolvedRecursiveType(String.class);
 ResolvedRecursiveType resolvedRecursiveType1 = createResolvedRecursiveType(erasedType);
 ResolvedRecursiveType resolvedRecursiveType2 = createResolvedRecursiveType(erasedType);
 ResolvedRecursiveType resolvedRecursiveType3 = createResolvedRecursiveType(erasedType);
 resolvedRecursiveType1.setReference(refType1);
 resolvedRecursiveType2.setReference(refType2);
 resolvedRecursiveType3.setReference(refType3);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType3));
 assertFalse(resolvedRecursiveType1.equals(new Object()));
 }
}