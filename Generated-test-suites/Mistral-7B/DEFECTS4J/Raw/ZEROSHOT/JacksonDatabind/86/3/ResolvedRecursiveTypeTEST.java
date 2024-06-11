package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeConstruction() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals(String.class, resolvedRecursiveType.getRawClass());
 assertNull(resolvedRecursiveType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testResolvedRecursiveTypeSetReferenceTwice() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType ref = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(ref);
 resolvedRecursiveType.setReference(ref);
 }

 @Test
 public void testResolvedRecursiveTypeGetSelfReferencedType() {
 JavaType ref = new TypeReference<List<String>>(){}.getType();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 resolvedRecursiveType.setReference(ref);
 assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
 }

 @Test
 public void testResolvedRecursiveTypeGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());
 JavaType ref = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(ref);
 resolvedRecursiveType.getGenericSignature(sb);
 String expected = "[Ljava.lang.String;[";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testResolvedRecursiveTypeGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String[].class, new TypeBindings());
 JavaType ref = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(ref);
 resolvedRecursiveType.getErasedSignature(sb);
 String expected = "Ljava/util/List;";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testResolvedRecursiveTypeWithContentType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType contentType = new TypeReference<StringBuilder>(){}.getType();
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
 }

 @Test
 public void testResolvedRecursiveTypeWithTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithContentTypeHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithContentValueHandler() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Object handler = new Object();
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
 }

 @Test
 public void testResolvedRecursiveTypeWithStaticTyping() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testResolvedRecursiveTypeNarrow() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(String[].class));
 }

 @Test(expected = NullPointerException.class)
 public void testResolvedRecursiveTypeRefineNullSuperClass() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals(resolvedRecursiveType, resolvedRecursiveType.refine(null, new TypeBindings(), null, new JavaType[0]));
 }

 @Test
 public void testResolvedRecursiveTypeToString() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType ref = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(ref);
 String expected = "[recursive type; [Ljava.util.List;]";
 assertEquals(expected, resolvedRecursiveType.toString());
 }

 @Test
 public void testResolvedRecursiveTypeEquals() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType ref = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType1.setReference(ref);
 resolvedRecursiveType2.setReference(ref);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }

 @Test
 public void testResolvedRecursiveTypeEqualsDifferentTypes() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }

 @Test
 public void testResolvedRecursiveTypeEqualsNull() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType.equals(null));
 }
}