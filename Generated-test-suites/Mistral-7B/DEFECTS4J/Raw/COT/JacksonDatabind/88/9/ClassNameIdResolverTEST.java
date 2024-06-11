package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final TypeIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), new ObjectMapper().getTypeFactory());

 @Test
 public void testGetMechanism() {
  JsonTypeInfo.Id id = resolver.getMechanism();
  assertEquals(JsonTypeInfo.Id.CLASS, id);
 }

 @Test
 public void testRegisterSubtype() {
  try {
   resolver.registerSubtype(String.class, "com.test.StringType");
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected
  }
 }

 @Test
 public void testIdFromValue() throws Exception {
  Class<String> cls = String.class;
  Object value = "test";
  String id = resolver.idFromValue(value);
  assertEquals("java.lang.String", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
  String id = resolver.idFromValueAndType(new Object(), String.class);
  assertEquals("java.lang.String", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  DatabindContext ctxt = mapper.getDeserializationContext();
  String id = "java.util.ArrayList";
  JavaType javaType = resolver.typeFromId(ctxt, id);
  assertEquals(Arrays.asList(String.class).getClass(), javaType.getRawClass());
 }

 @Test(expected = IOException.class)
 public void testInvalidTypeId() throws Exception {
  DatabindContext ctxt = new DatabindContext();
  resolver.typeFromId(ctxt, "InvalidTypeId");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId2() throws Exception {
  DatabindContext ctxt = new DatabindContext();
  resolver.typeFromId(ctxt, "java.util.ArrayList<InvalidTypeId>");
 }

 @Test
 public void testIdFromEnumSet() throws Exception {
  EnumSet<Integer> enumSet = EnumSet.allOf(Integer.class);
  String id = resolver.idFromValue(enumSet);
  String expectedId = "java.util.EnumSet";
  assertEquals(expectedId, id);
 }

 @Test
 public void testIdFromEnumMap() throws Exception {
  EnumMap<String, Integer> enumMap = new EnumMap<>(String.class);
  enumMap.put(Enum.valueOf(String.class, "KEY"), Enum.valueOf(Integer.class, "VALUE"));
  String id = resolver.idFromValue(enumMap);
  String expectedId = "java.util.EnumMap";
  assertEquals(expectedId, id);
 }

 @Test
 public void testIdFromValueWithInnerClass() throws Exception {
  class OuterClass {
   static class InnerClass {}
  }
  InnerClass innerClass = new OuterClass.InnerClass();
  String id = resolver.idFromValue(innerClass);
  String expectedId = "com.fasterxml.jackson.databind.jsontype.impl.OuterClass$InnerClass";
  assertEquals(expectedId, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromValueWithInvalidInnerClass() throws Exception {
  class OuterClass {
   static class InnerClass {
    static int i = 0;
   }
  }
  InnerClass innerClass = new OuterClass.InnerClass();
  String id = resolver.idFromValue(innerClass);
  fail("Expected IllegalArgumentException");
 }

 @Test
 public void testIdFromArraysAsList() throws Exception {
  Object[] values = new Object[] { "test1", "test2" };
  String id = resolver.idFromValue(Arrays.asList(values));
  String expectedId = "java.util.ArrayList";
  assertEquals(expectedId, id);
 }

 @Test
 public void testIdFromCollectionsMap() throws Exception {
  Map<String, Object> map = new HashMap<>();
  map.put("KEY", "VALUE");
  String id = resolver.idFromValue(map);
  String expectedId = "java.util.HashMap";
  assertEquals(expectedId, id);
 }
}