package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.classinfo.ClassInfo;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private static final String PACKAGE = "com.fasterxml.jackson.databind.jsontype.impl";
 private static final String BASE_PACKAGE = "com.fasterxml.jackson.databind";

 /**
 * Test constructor.
 */
 @org.junit.Test
 public void testConstructor() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 }

 /**
 * Test getMechanism().
 */
 @org.junit.Test
 public void testGetMechanism() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 /**
 * Test registerSubtype().
 */
 @org.junit.Test
 public void testRegisterSubtype() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 assertNull(resolver); // not used with class name - based resolvers
 }

 /**
 * Test idFromValue().
 */
 @org.junit.Test
 public void testIdFromValue() throws IOException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 Object value = new Object();
 String id = resolver.idFromValue(value);
 assertEquals("java.lang.Object", id);

 value = new int[0];
 id = resolver.idFromValue(value);
 assertEquals("java.lang.int[]", id);

 value = EnumSet.allOf(Enum.class);
 id = resolver.idFromValue(value);
 assertEquals("java.util.EnumSet<java.lang.Enum>", id);
 }

 /**
 * Test idFromValueAndType().
 */
 @org.junit.Test
 public void testIdFromValueAndType() throws IOException {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 Object value = new Object();
 Class<?> type = String.class;
 String id = resolver.idFromValueAndType(value, type);
 assertEquals("java.lang.Object", id);

 value = new int[0];
 id = resolver.idFromValueAndType(value, type);
 assertEquals("java.lang.int[]", id);

 value = EnumSet.allOf(Enum.class);
 id = resolver.idFromValueAndType(value, type);
 assertEquals("java.util.EnumSet<java.lang.Enum>", id);

 type = List.class;
 value = new ArrayList<>();
 id = resolver.idFromValueAndType(value, type);
 assertEquals("java.util.ArrayList", id);
 }

 /**
 * Test typeFromId().
 */
 @org.junit.Test
 public void testTypeFromId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, mapper.getTypeFactory());

 String id = "java.lang.String";
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(String.class, javaType.getRawClass());

 id = "java.util.EnumSet<java.lang.Enum>";
 javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(EnumSet.class, javaType.getRawClass());
 javaType = javaType.getComponentType();
 assertEquals(Enum.class, javaType.getRawClass());

 id = "java.util.ArrayList<java.util.Map<java.lang.String,java.lang.Integer>>";
 javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(Map.class, javaType.getRawClass());
 javaType = javaType.getType(new TypeReference<Map<String, Integer>>(){});
 assertEquals(String.class, javaType.getComponentType(0).getRawClass());
 assertEquals(Integer.class, javaType.getComponentType(1).getRawClass());
 }

 /**
 * Test _idFrom().
 */
 @org.junit.Test
 public void test_idFrom() {
 TypeIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 Object value = new Object();
 Class<?> cls = value.getClass();
 String id = resolver._idFrom(value, cls, new TypeFactory());
 assertEquals("java.lang.Object", id);

 value = new int[0];
 cls = int[].class;
 id = resolver._idFrom(value, cls, new TypeFactory());
 assertEquals("java.lang.int[]", id);

 value = EnumSet.allOf(Enum.class);
 id = resolver._idFrom(value, cls = EnumSet.class, new TypeFactory());
 assertEquals("java.util.EnumSet<java.lang.Enum>", id);

 value = new HashMap<>();
 cls = HashMap.class;
 id = resolver._idFrom(value, cls, new TypeFactory());
 // not optimal: but EnumMap is not a customizable type so this is sort of ok
 id = resolver._idFrom(value, new TypeReference<Map<String, Integer>>(){}.getType(), new TypeFactory());
 assertEquals("java.util.HashMap<java.lang.String,java.lang.Integer>", id);
 }

 /**
 * Test getDescForKnownTypeIds().
 */
 @org.junit.Test
 public void testGetDescForKnownTypeIds() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactory());
 assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
 }

 /**
 * Test convert class name to canonical id.
 */
 @org.junit.Test
 public void testConvertClassNameToCanonicalId() {
 ObjectMapper mapper = new ObjectMapper();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, mapper.getTypeFactory());

 Class<?> cls = String.class;
 String id = resolver._idFrom(null, cls, new TypeFactory());
 assertEquals("java.lang.String", id);

 cls = Integer[].class;
 id = resolver._idFrom(null, cls, new TypeFactory());
 assertEquals("java.lang.int[]", id);

 cls = EnumSet.class;
 id = resolver._idFrom(null, cls, new TypeFactory());
 assertEquals("java.util.EnumSet<java.lang.Enum>", id);

 ClassInfo mapClassInfo = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Integer.class);
 id = resolver._idFrom(null, mapClassInfo.getRawClass(), new TypeFactory());
 assertEquals("java.util.HashMap<java.lang.String,java.lang.Integer>", id);
 }

 /**
 * Test convert id to class.
 */
 @org.junit.Test
 public void testConvertIdToClass() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, mapper.getTypeFactory());

 String id = "java.lang.String";
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(String.class, javaType.getRawClass());

 id = "java.util.EnumSet<java.lang.Enum>";
 javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(EnumSet.class, javaType.getRawClass());
 javaType = javaType.getComponentType();
 assertEquals(Enum.class, javaType.getRawClass());

 id = "java.util.ArrayList<java.util.Map<java.lang.String,java.lang.Integer>>";
 javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
 assertEquals(Map.class, javaType.getRawClass());
 javaType = javaType.getType(new TypeReference<Map<String, Integer>>(){});
 assertEquals(String.class, javaType.getComponentType(0).getRawClass());
 assertEquals(Integer.class, javaType.getComponentType(1).getRawClass());
 }
}