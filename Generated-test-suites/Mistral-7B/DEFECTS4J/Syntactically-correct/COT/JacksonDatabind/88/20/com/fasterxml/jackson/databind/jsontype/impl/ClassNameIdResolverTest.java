package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;
 private ObjectMapper mapper;
 private TypeFactory typeFactory;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 resolver = new ClassNameIdResolver(Object.class.getTypeName(), typeFactory);
 mapper = new ObjectMapper();
 typeFactory = mapper.getTypeFactory();
 }

 @Test
 public void testGetMechanism() {
 JsonTypeInfo.Id id = resolver.getMechanism();
 assertEquals(JsonTypeInfo.Id.CLASS, id);
 }

 @Test
 public void testRegisterSubtype() {
 try {
 resolver.registerSubtype(String.class, "string");
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdFromValue() throws IOException {
 Object value = new Object();
 String id = resolver.idFromValue(value);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(value.getClass());
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIdFromValueAndType() throws IOException {
 Object value = new Object();
 Class<?> type = value.getClass();
 String id = resolver.idFromValueAndType(value, type);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(type);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdNullContext() throws IOException {
 resolver.typeFromId(null, "className");
 }

 @Test
 public void testTypeFromIdUnknownId() throws IOException {
 String id = "unknownId";
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 assertNull(actualType);
 }

 @Test
 public void testIdFromValueEnum() throws IOException {
 Enum value = Enum.valueOf(String.class, "FOO");
 String id = resolver.idFromValue(value);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(Enum.class, String.class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIdFromValueInnerClass() throws IOException {
 Class<?> outer = Object.class;
 Class<?> inner = OuterClass.InnerClass.class;
 Object value = new OuterClass();
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), "outer$inner");
 JavaType expectedType = typeFactory.constructType(inner, outer.getTypeName());
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testTypeFromIdGenericType() throws IOException {
 String id = "java.util.ArrayList<java.lang.String>";
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructCollectionType(ArrayList.class, String.class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIdFromValueArrays() throws IOException {
 Object value = new String[1];
 String id = resolver.idFromValue(value);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(String[].class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIdFromValueCollections() throws IOException {
 Object value = new HashSet<>();
 String id = resolver.idFromValue(value);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(HashSet.class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIdFromValueMaps() throws IOException {
 Object value = new HashMap<>();
 String id = resolver.idFromValue(value);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(HashMap.class);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testIdFromValueArraysEnumMap() throws IOException {
 Object value = new EnumMap<>(Enum.class);
 value.put(Enum.valueOf(Enum.class, "FOO"), new String[1]);
 String id = resolver.idFromValue(value);
 JavaType actualType = resolver.typeFromId(mapper.getDeserializationContext(), id);
 JavaType expectedType = typeFactory.constructType(EnumMap.class, Enum.class, String[].class);
 assertEquals(expectedType, actualType);
 }

}

class OuterClass {
 static class InnerClass {
 }
}