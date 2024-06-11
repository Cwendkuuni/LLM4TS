package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static ClassNameIdResolver createResolver(Class<?> baseType) {
 return new ClassNameIdResolver(JacksonAnnotationIntrospector.findJavaType(baseType), new TypeFactory());
 }

 @org.junit.Test
 public void testIdFromValueAndType() throws IOException {
 TypeIdResolver resolver = createResolver(String.class);
 Object value = new Object();
 String expected = "java.lang.String";
 String actual = resolver.idFromValueAndType(value, String.class);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromValue() throws IOException {
 TypeIdResolver resolver = createResolver(String.class);
 Object value = new Object();
 String expected = "java.lang.String";
 String actual = resolver.idFromValue(value);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testTypeFromId() throws IOException {
 TypeIdResolver resolver = createResolver(Map.class);
 String id = "java.util.HashMap";
 JavaType expected = new TypeReference<Map<String, Integer>>(){}.getType();
 JavaType actual = resolver.typeFromId(null, id);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromUnknownClass() throws IOException {
 TypeIdResolver resolver = createResolver(String.class);
 Object value = new Object();
 Class<?> unknownClass = null; // or some unknown class
 String expected = null;
 String actual = resolver.idFromValueAndType(value, unknownClass);
 assertNull(actual);
 }

 @org.junit.Test
 public void testIdFromEnum() throws IOException {
 TypeIdResolver resolver = createResolver(EnumSet.class);
 EnumSet<java.lang.Float> value = EnumSet.allOf(java.lang.Float.class);
 String expected = "java.util.EnumSet";
 String actual = resolver.idFromValue(value);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromCollectionOfEnum() throws IOException {
 TypeIdResolver resolver = createResolver(Map.class);
 EnumSet<java.lang.Float> value = EnumSet.allOf(java.lang.Float.class);
 Map<String, Object> map = new HashMap<>();
 map.put("strKey", value);
 String expected = "java.util.HashMap<java.lang.String, java.util.EnumSet<java.lang.Float>>";
 String actual = resolver.idFromValue(map);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromArray() throws IOException {
 TypeIdResolver resolver = createResolver(int[].class);
 int[] value = new int[10];
 String expected = "int[]";
 String actual = resolver.idFromValue(value);
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testIdFromInnerClass() throws IOException {
 TypeIdResolver resolver = createResolver(String.class);
 Class<?> outerClass = Integer.class;
 Class<?> innerClass = new Class(() -> outerClass.getInnerClasses()[0]) {
 };
 String expected = "java.lang.Integer$1";
 String actual = resolver.idFromValueAndType(new Object(), innerClass);
 assertEquals(expected, actual);
 }
}