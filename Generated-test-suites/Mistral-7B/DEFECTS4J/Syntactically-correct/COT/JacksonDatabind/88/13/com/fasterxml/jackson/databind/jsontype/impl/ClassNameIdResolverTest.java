package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 resolver = new ClassNameIdResolver(Object.class.getTypeName(), mapper.getTypeFactory());
 }

 @Test
 public void testGetMechanism() {
 JsonTypeInfo.Id id = resolver.getMechanism();
 assertEquals(JsonTypeInfo.Id.CLASS, id);
 }

 @Test
 public void testRegisterSubtype() {
 try {
 resolver.registerSubtype(String.class, "testString");
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = resolver.idFromValue(value);
 assertNotNull(id);
 assertEquals(value.getClass().getName(), id);

 value = new Enum("testEnum");
 id = resolver.idFromValue(value);
 assertNotNull(id);
 assertEquals(value.getClass().getName(), id.substring(0, id.lastIndexOf('$')));
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 String id = resolver.idFromValueAndType(value, String.class);
 assertNotNull(id);
 assertEquals(String.class.getName(), id);

 value = new Enum("testEnum");
 id = resolver.idFromValueAndType(value, Object.class.getTypeName());
 assertNotNull(id);
 assertEquals(value.getClass().getName().substring(0, value.getClass().getName().lastIndexOf('$')), id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.util.HashMap";
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 TypeReference<?> typeRef = new TypeReference<HashMap<String, Integer>>() {};
 assertEquals(typeRef.getType(), javaType);

 id = "java.lang.String";
 javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(String.class, javaType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 String id = "invalidTypeId";
 resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 fail("Did not throw IllegalArgumentException");
 }

 @Test
 public void testUnknownClass() throws Exception {
 String id = "com.unknown.UnknownClass";
 try {
 resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 fail("Expected java.lang.ClassNotFoundException");
 } catch (ClassNotFoundException e) {
 // Expected
 }
 }

 @Test
 public void testArrayType() throws Exception {
 String id = "java.lang.int[]";
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 TypeReference<?> typeRef = new TypeReference<int[]>() {};
 assertEquals(typeRef.getType(), javaType);
 }

 @Test
 public void testCollectionType() throws Exception {
 String id = "java.util.List<java.lang.String>";
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 TypeReference<?> typeRef = new TypeReference<List<String>>() {};
 assertEquals(typeRef.getType(), javaType);
 }

 @Test
 public void testMapType() throws Exception {
 String id = "java.util.Map<java.lang.String, java.lang.Integer>";
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 TypeReference<?> typeRef = new TypeReference<Map<String, Integer>>() {};
 assertEquals(typeRef.getType(), javaType);
 }

 @Test
 public void testEnumType() throws Exception {
 Class<?> enumClass = Enum.class;
 String id = enumClass.getName();
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 TypeReference<?> typeRef = new TypeReference<Collection<Enum>>() {}.where(new TypeReference<EnumSet<Enum>>() {});
 assertEquals(typeRef.getType(), javaType);
 }

 @Test
 public void testInnerClassType() throws Exception {
 Class<?> outerClass = Object.class;
 Class<?> innerClass = Outer.Inner.class;
 String id = innerClass.getName();
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 TypeReference<?> typeRef = new TypeReference<Inner>() {}.asSubTypeOf(new TypeReference<Outer.Inner>() {});
 assertEquals(typeRef.getType(), javaType);
 }
}

class Outer {
 static class Inner {
 }
}