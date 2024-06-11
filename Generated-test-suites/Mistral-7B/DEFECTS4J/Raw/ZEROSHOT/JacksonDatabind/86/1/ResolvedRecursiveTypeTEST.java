package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeConstruction() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 assertNotNull(resolvedRecursiveType);
 assertEquals(String.class, resolvedRecursiveType.getRawClass());
 assertNull(resolvedRecursiveType.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveTypeSetReference() {
 JavaType refType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.setReference(refType);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertEquals(refType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveTypeSetReferenceTwice() {
 JavaType refType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.setReference(refType);
 resolvedRecursiveType.setReference(refType);
 }

 @Test
 public void testResolvedRecursiveTypeGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.getGenericSignature(sb);
 String signature = sb.toString();
 assertTrue(signature.startsWith("[recursive type; "));
 assertEquals("]", signature.charAt(signature.length() - 1));
 }

 @Test
 public void testResolvedRecursiveTypeGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType.getErasedSignature(sb);
 String signature = sb.toString();
 assertEquals("", signature);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 JavaType contentType = new TypeReference<List<String>>() {}.getType();
 resolvedRecursiveType = resolvedRecursiveType.withContentType(contentType);
 JavaType result = resolvedRecursiveType.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType = resolvedRecursiveType.withTypeHandler(null);
 JavaType result = resolvedRecursiveType.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType = resolvedRecursiveType.withContentTypeHandler(null);
 JavaType result = resolvedRecursiveType.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType = resolvedRecursiveType.withValueHandler(null);
 JavaType result = resolvedRecursiveType.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithContentValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType = resolvedRecursiveType.withContentValueHandler(null);
 JavaType result = resolvedRecursiveType.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeWithStaticTyping() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType = resolvedRecursiveType.withStaticTyping();
 JavaType result = resolvedRecursiveType.getSelfReferencedType();
 assertNull(result);
 }

 @Test
 public void testResolvedRecursiveTypeToString() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
 String result = resolvedRecursiveType.toString();
 assertTrue(result.startsWith("[recursive type; "));
 assertFalse(result.contains("UNRESOLVED"));
 }

 @Test
 public void testResolvedRecursiveTypeEquals() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, null);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 JavaType refType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType3.setReference(refType);
 ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(String.class, null);
 resolvedRecursiveType4.setReference(refType);
 assertTrue(resolvedRecursiveType3.equals(resolvedRecursiveType4));
 ResolvedRecursiveType resolvedRecursiveType5 = new ResolvedRecursiveType(Integer.class, null);
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType5));
 }
}