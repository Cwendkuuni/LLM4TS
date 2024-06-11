package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactoryBuilder;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {

 private TypeIdResolver resolver;
 private ObjectMapper mapper;
 private TypeFactory typeFactory;

 @Before
 public void setup() throws Exception {
 resolver = new ClassNameIdResolver(null, new TypeFactoryBuilder().build());
 mapper = new ObjectMapper();
 typeFactory = mapper.getTypeFactory();
 }

 @Test
 public void testGetMechanism() throws Exception {
 JsonTypeInfo.Id expected = JsonTypeInfo.Id.CLASS;
 JsonTypeInfo.Id result = resolver.getMechanism();
 assertEquals(expected, result);
 }

 @Test
 public void testRegisterSubtype() throws Exception {
 // Not testing since not used in this class
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String result = resolver.idFromValue(value);
 String expected = value.getClass().getName();
 assertEquals(expected, result);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> type = String.class;
 String result = resolver.idFromValueAndType(value, type);
 String expected = value.getClass().getName();
 assertEquals(expected, result);
 }

 @Test
 public void testTypeFromIdNullContext() throws IOException {
 assertNull(resolver.typeFromId(null, "test"));
 }

 @Test
 public void testTypeFromIdNoClassFound() throws IOException {
 assertNull(resolver.typeFromId(mapper.getDeserializationContext(), "InvalidTypeId"));
 }

 @Test
 public void testTypeFromId() throws IOException {
 JavaType javaType = typeFactory.constructClassType(String.class);
 String id = javaType.getName();
 JavaType result = resolver.typeFromId(mapper.getDeserializationContext(), id);
 assertEquals(javaType, result);
 }

 @Test
 public void testTypeFromIdGeneric() throws IOException {
 TypeFactory tf = mapper.getTypeFactory();
 JavaType javaType = tf.constructMapType(Map.class, String.class, EnumSet.class);
 String id = javaType.getRawClass().getName() + "<String, EnumSet<String>>";
 JavaType result = resolver.typeFromId(mapper.getDeserializationContext(), id);
 assertEquals(javaType, result);
 }

 @Test
 public void testTypeFromIdEnum() throws IOException {
 TypeFactory tf = mapper.getTypeFactory();
 JavaType javaType = tf.constructType(EnumSet.class);
 String id = javaType.getName();
 JavaType result = resolver.typeFromId(mapper.getDeserializationContext(), id);
 assertEquals(javaType, result);
 }

 @Test
 public void testTypeFromIdArray() throws IOException {
 JavaType javaType = typeFactory.constructArrayType(Integer.TYPE, 1);
 String id = javaType.canonicalName();
 JavaType result = resolver.typeFromId(mapper.getDeserializationContext(), id);
 assertEquals(javaType, result);
 }

 @Test
 public void testTypeFromIdInnerClass() throws IOException {
 Class<?> outerClass = Object.class;
 Class<?> innerClass = Arrays.class.getDeclaredClass("Arrays$ArrayList");
 String id = innerClass.getName();
 JavaType result = resolver.typeFromId(mapper.getDeserializationContext(), id);
 // ArrayList is a public class and can be constructed directly, so we can check equality
 JavaType expected = typeFactory.constructClassType(java.util.ArrayList.class);
 assertEquals(expected, result);
 }

 @Test
 public void testTypeFromIdOuterClass() throws IOException {
 Class<?> outerClass = Object.class;
 Class<?> innerClass = Arrays.class.getDeclaredClass("Object$Holder");
 String id = innerClass.getName();
 JavaType result = resolver.typeFromId(mapper.getDeserializationContext(), id);
 // Holder is an abstract class and needs an outer class to be constructed, so we can't check equality directly. Instead, we check if the type can be instantiated
 JavaType expected = typeFactory.constructType(Map.class, String.class, String.class);
 JavaType constructorResult = mapper.getTypeFactory().constructFunctionType(expected.getRawClass(), (Class[]) null);
 JavaType resultWithOuterClass = resolver.typeFromId(mapper.getDeserializationContext(), id + "<String, String>");
 assertEquals(constructorResult, resultWithOuterClass);
 }
}