package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;

 @ org.junit.Before
 public void setUp() {
 ObjectMapper mapper = new ObjectMapper();
 resolver = mapper.getPolymorphicTypeValidator().getActiveBaseTypeResolver();
 }

 @ org.junit.Test
 public void testIdFromValue() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValue(obj);
 assertEquals("java.lang.Object", id);

 EnumTest enumTest = new EnumTest();
 id = resolver.idFromValue(enumTest);
 assertEquals("com.fasterxml.jackson.databind.ClassNameIdResolverTest$EnumTest", id);
 }

 @ org.junit.Test
 public void testIdFromValueAndType() throws Exception {
 Object obj = new Object();
 Class<?> type = Object.class;
 String id = resolver.idFromValueAndType(obj, type);
 assertEquals("java.lang.Object", id);

 EnumTest enumTest = new EnumTest();
 id = resolver.idFromValueAndType(enumTest, EnumTest.class);
 assertEquals("com.fasterxml.jackson.databind.ClassNameIdResolverTest$EnumTest", id);
 }

 @ org.junit.Test
 public void testTypeFromId() throws Exception {
 DatabindContext context = new DeserializationContext(new ObjectMapper(), Collections.emptyMap(), null, null, new Object());
 JavaType javaType = resolver.typeFromId(context, "java.lang.String");
 assertEquals(String.class, javaType.getRawClass());

 JavaType javaTypeEnum = resolver.typeFromId(context, "com.fasterxml.jackson.databind.ClassNameIdResolverTest$EnumTest");
 assertEquals(EnumTest.class, javaTypeEnum.getRawClass());
 }

 @ org.junit.Test
 public void testTypeFromIdWithGenerics() throws Exception {
 TypeFactory typeFactory = new TypeFactory();
 JavaType javaTypeList = resolver.typeFromId(context, "java.util.List<java.lang.String>");
 TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {};
 assertEquals(typeReference.getType(), javaTypeList);

 JavaType javaTypeEnumMap = resolver.typeFromId(context, "java.util.Map<com.fasterxml.jackson.databind.ClassNameIdResolverTest$EnumTest, java.lang.String>");
 TypeReference<Map<EnumTest, String>> typeReferenceMap = new TypeReference<Map<EnumTest, String>>() {};
 assertEquals(typeReferenceMap.getType(), javaTypeEnumMap);
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 DatabindContext context = new DeserializationContext(new ObjectMapper(), Collections.emptyMap(), null, null, new Object());
 String id = "invalidTypeId";
 resolver.typeFromId(context, id);
 fail("Expected IllegalArgumentException");
 }

 @ org.junit.Test(expected = ClassNotFoundException.class)
 public void testClassNotFoundException() throws Exception {
 DatabindContext context = new DeserializationContext(new ObjectMapper(), Collections.emptyMap(), null, null, new Object());
 String id = "com.non-existent.package.NonExistentClass";
 JavaType javaType = resolver.typeFromId(context, id);
 fail("Expected ClassNotFoundException");
 }

 @ org.junit.Test
 public void testIdFromValueWithCollections() throws Exception {
 Object hashMap = new HashMap<>();
 String id = resolver.idFromValue(hashMap);
 assertEquals("java.util.Map", id);

 Object enumSet = EnumSet.of(EnumTest.VALUE1, EnumTest.VALUE2);
 id = resolver.idFromValue(enumSet);
 assertEquals("java.util.EnumSet", id);
 }

 @ org.junit.Test
 public void testIdFromValueWithArrayList() throws Exception {
 Object list = Arrays.asList("test1", "test2");
 String id = resolver.idFromValue(list);
 assertEquals("java.util.ArrayList", id);
 }
}

 enum EnumTest {
 VALUE1,
 VALUE2;
}