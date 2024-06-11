package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testResolvedRecursiveTypeConstruction() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals("java.lang.String", resolvedRecursiveType.getErasedSignature().toString());
 assertNull(resolvedRecursiveType.getSelfReferencedType());
 }

 @Test
 public void testSetSelfReferencedType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(refType);
 JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
 assertEquals(List.class, selfReferencedType.getRawClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelfReferencedTypeTwice() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType refType = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(refType);
 resolvedRecursiveType.setReference(refType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 resolvedRecursiveType.getGenericSignature(sb);
 assertEquals("[recursive type; UNRESOLVED]", sb.toString());

 JavaType refType = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(refType);
 sb = new StringBuilder();
 resolvedRecursiveType.getGenericSignature(sb);
 assertEquals("[recursive type; java.util.List<java.lang.String>]", sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals("java.lang.String", resolvedRecursiveType.getErasedSignature().toString());

 JavaType refType = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(refType);
 assertEquals("java.util.List", resolvedRecursiveType.getErasedSignature().toString());
 }

 @Test
 public void testMethodsThatDoNothing() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNull(resolvedRecursiveType.withContentType(null));
 assertNull(resolvedRecursiveType.withTypeHandler(null));
 assertNull(resolvedRecursiveType.withContentTypeHandler(null));
 assertNull(resolvedRecursiveType.withValueHandler(null));
 assertNull(resolvedRecursiveType.withContentValueHandler(null));
 assertNull(resolvedRecursiveType.withStaticTyping());
 }

 @Test
 public void testDeprecatedNarrowMethod() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 Class<?> subclass = String[].class;
 assertNull(resolvedRecursiveType._narrow(subclass));
 }

 @Test
 public void testRefineMethod() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNull(resolvedRecursiveType.refine(Object.class, null, null, new JavaType[0]));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());

 JavaType refType = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType.setReference(refType);
 assertEquals("[recursive type; java.util.List<java.lang.String>]", resolvedRecursiveType.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

 JavaType refType = new TypeReference<List<String>>(){}.getType();
 resolvedRecursiveType1.setReference(refType);
 resolvedRecursiveType2.setReference(refType);
 assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
 }
}