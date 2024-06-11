package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonTypeReference;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 Class<?> rawType = erasedType;
 TypeBindings bindings = new TypeBindings();
 return new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testNullReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testSetReference() {
 JavaType refType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 type.setReference(refType);
 JavaType expected = refType;
 JavaType actual = type.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType refType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 type.setReference(refType);
 type.setReference(new TypeReference<Map<String,String>>() {}.getType());
 }

 @Test
 public void testGetSignatures() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 StringBuffer sb = new StringBuffer();
 type.getErasedSignature(sb);
 String expectedErased = "[java.util.List]";
 String actualErased = sb.toString();
 assertEquals(expectedErased, actualErased);

 type.getGenericSignature(sb);
 String expectedGeneric = "[java.util.List<T extends java.lang.Object>]";
 String actualGeneric = sb.toString();
 assertEquals(expectedGeneric, actualGeneric);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 JavaType contentType = new TypeReference<String>() {}.getType();
 JavaType expected = type;
 JavaType actual = type.withContentType(contentType);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 JavaType handler = new TypeReference<String>() {}.getType();
 JavaType expected = type;
 JavaType actual = type.withTypeHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 JavaType handler = new TypeReference<String>() {}.getType();
 JavaType expected = type;
 JavaType actual = type.withContentTypeHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 JavaType handler = new TypeReference<String>() {}.getType();
 JavaType expected = type;
 JavaType actual = type.withValueHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 JavaType handler = new TypeReference<String>() {}.getType();
 JavaType expected = type;
 JavaType actual = type.withContentValueHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 JavaType expected = type;
 JavaType actual = type.withStaticTyping();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(List.class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(List.class, new TypeBindings());
 assertTrue(type1.equals(type2));

 JavaType refType = new TypeReference<List<String>>() {}.getType();
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(List.class, new TypeBindings());
 type3.setReference(refType);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(List.class, new TypeBindings());
 type4.setReference(refType);
 assertTrue(type3.equals(type4));

 JavaType otherType = new TypeReference<Map<String,String>>() {}.getType();
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(List.class, new TypeBindings());
 type5.setReference(refType);
 assertFalse(type3.equals(type5));
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(List.class, new TypeBindings());
 String expected = "[recursive type; UNRESOLVED]";
 String actual = type.toString();
 assertEquals(expected, actual);

 JavaType refType = new TypeReference<List<String>>() {}.getType();
 type.setReference(refType);
 String expectedResolved = "[recursive type; [" + refType.getRawClass().getName() + "]]";
 String actualResolved = type.toString();
 assertEquals(expectedResolved, actualResolved);
 }
}