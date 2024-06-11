package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;

public class ClassNameIdResolverTest {

 private TypeIdResolver baseTypeResolver;
 private TypeFactory typeFactory;

 @org.junit.Before
 public void setUp() throws Exception {
 baseTypeResolver = new ClassNameIdResolver(JavaType.construct(String.class), typeFactory);
 }

 @org.junit.Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String expectedId = "java.lang.Object";
 String actualId = baseTypeResolver.idFromValue(value);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> type = String[].class;
 String expectedId = "java.lang.Object[]";
 String actualId = baseTypeResolver.idFromValueAndType(value, type);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testTypeFromId() throws Exception {
 String id = "java.util.List";
 JavaType expectedType = typeFactory.constructCollectionType(ArrayList.class, String.class);
 JavaType actualType = baseTypeResolver.typeFromId(null, id);
 assertEquals(expectedType, actualType);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testUnknownIdThrowsJsonMappingException() throws Exception {
 String id = "UnknownId";
 baseTypeResolver.typeFromId(null, id);
 }

 @org.junit.Test
 public void testIdFromJavaType() throws Exception {
 JavaType javaType = JavaType.construct(String[].class);
 String expectedId = "java.lang.Object[]";
 String actualId = baseTypeResolver.idFromValueAndType(null, javaType.getRawClass());
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromEnum() throws Exception {
 Enum e = Enum.valueOf(String.class, "VALUE");
 String expectedId = String.class.getName();
 String actualId = baseTypeResolver.idFromValue(e);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromInnerClass() throws Exception {
 Class<?> outerClass = Outer.class;
 Class<?> innerClass = Outer.Inner.class;
 String expectedId = outerClass.getName() + '$' + innerClass.getSimpleName();
 String actualId = baseTypeResolver.idFromValueAndType(null, innerClass);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromArray() throws Exception {
 Class<?> type = int[].class;
 String expectedId = "int[]";
 String actualId = baseTypeResolver.idFromValueAndType(null, type);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromMap() throws Exception {
 Class<?> keyType = String.class;
 Class<?> valueType = Integer.class;
 Class<?> mapType = Map.class;
 JavaType javaType = JavaType.construct(mapType, keyType, valueType);
 String expectedId = mapType.getName() + '<' + keyType.getName() + ',' + valueType.getName() + '>';
 String actualId = baseTypeResolver.idFromValueAndType(null, javaType.getRawClass());
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromList() throws Exception {
 Class<?> listType = ArrayList.class;
 JavaType javaType = JavaType.construct(listType, String.class);
 String expectedId = listType.getName() + '<' + String.class.getName() + '>';
 String actualId = baseTypeResolver.idFromValueAndType(null, javaType.getRawClass());
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromCollectionWithGenericType() throws Exception {
 List list = Arrays.asList(new Object(), new Object());
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Object.class);
 String expectedId = javaType.getName();
 String actualId = baseTypeResolver.idFromValue(list);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromCollectionWithGenericTypeAndType() throws Exception {
 List list = Arrays.asList(new Object(), new Object());
 JavaType javaType = JavaType.constructCollectionType(ArrayList.class, Object.class);
 String expectedId = javaType.getName();
 String actualId = baseTypeResolver.idFromValueAndType(list, javaType.getRawClass());
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromEnumSet() throws Exception {
 EnumSet enumSet = EnumSet.allOf(String.class);
 JavaType javaType = JavaType.constructCollectionType(EnumSet.class, String.class);
 String expectedId = javaType.getName();
 String actualId = baseTypeResolver.idFromValue(enumSet);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromEnumMap() throws Exception {
 EnumMap enumMap = new EnumMap<>(String.class);
 Enum e = Enum.valueOf(String.class, "VALUE");
 enumMap.put(e, "VALUE");
 JavaType javaType = JavaType.constructMapType(EnumMap.class, String.class, String.class);
 String expectedId = javaType.getName();
 String actualId = baseTypeResolver.idFromValue(enumMap);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromArrayWithGenericType() throws Exception {
 Class<?> arrayType = Array.newInstance(String[].class, 1).getClass();
 String expectedId = arrayType.getName() + '<' + String[].class.getName() + '>';
 String actualId = baseTypeResolver.idFromValueAndType(null, arrayType);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromArrayWithGenericTypeAndType() throws Exception {
 Class<?> arrayType = Array.newInstance(String[].class, 1).getClass();
 String expectedId = arrayType.getName() + '<' + String[].class.getName() + '>';
 String actualId = baseTypeResolver.idFromValueAndType(null, arrayType);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromMapWithGenericType() throws Exception {
 Class<?> mapType = HashMap.class;
 JavaType javaType = JavaType.constructMapType(mapType, String.class, String.class);
 String expectedId = javaType.getName() + '<' + String.class.getName() + ',' + String.class.getName() + '>';
 String actualId = baseTypeResolver.idFromValueAndType(null, mapType);
 assertEquals(expectedId, actualId);
 }

 @org.junit.Test
 public void testIdFromMapWithGenericTypeAndValueType() throws Exception {
 Class<?> mapType = HashMap.class;
 JavaType javaType = JavaType.constructMapType(mapType, String.class, Integer.class);
 String expectedId = javaType.getName() + '<' + String.class.getName() + ',' + Integer.class.getName() + '>';
 String actualId = baseTypeResolver.idFromValueAndType(null, mapType);
 assertEquals(expectedId, actualId);
 }
}

class Outer {
 static class Inner {
 }
}