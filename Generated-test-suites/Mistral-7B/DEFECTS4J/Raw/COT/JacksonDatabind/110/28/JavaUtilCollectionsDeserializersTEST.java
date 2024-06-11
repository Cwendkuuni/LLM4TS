package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException, JsonProcessingException {
 List<String> input = Arrays.asList("test1", "test2");
 JavaType javaType = MAPPER.getTypeFactory().constructType(List.class, String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), javaType);
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString(input), deserializer.getType());
 assertNotNull(deserialized);
 assertEquals(deserialized.getClass(), ArrayList.class);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException, JsonProcessingException {
 Set<String> input = Collections.singleton(Boolean.TRUE);
 JavaType javaType = MAPPER.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPper.getContext(), javaType);
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString(input), deserializer.getType());
 assertNotNull(deserialized);
 assertEquals(deserialized.getClass(), HashSet.class);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException, JsonProcessingException {
 List<String> input = Arrays.asList("test1", "test2");
 JavaType javaType = MAPPER.getTypeFactory().constructType(List.class, String.class);
 JavaType unmodifiableListType = MAPPER.getTypeFactory().constructType(Collection.class, javaType.getRawClass());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), unmodifiableListType);
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString(input), deserializer.getType());
 assertNotNull(deserialized);
 assertEquals(deserialized.getClass(), Collections.unmodifiableList(input).getClass());
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException, JsonProcessingException {
 Set<String> input = Collections.singleton(Boolean.TRUE);
 JavaType javaType = MAPPER.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JavaType unmodifiableSetType = MAPPER.getTypeFactory().constructType(Collection.class, javaType.getRawClass());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), unmodifiableSetType);
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString(input), deserializer.getType());
 assertNotNull(deserialized);
 assertEquals(deserialized.getClass(), Collections.unmodifiableSet(input).getClass());
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException, JsonProcessingException {
 Map<String, Integer> input = Collections.singletonMap("testKey", 1);
 JavaType javaType = MAPPER.getTypeFactory().constructType(Map.class, String.class, Integer.TYPE);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getContext(), javaType);
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString(input), deserializer.getType());
 assertNotNull(deserialized);
 assertEquals(deserialized.getClass(), HashMap.class);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException, JsonProcessingException {
 Map<String, Integer> input = Collections.singletonMap("testKey", 1);
 JavaType javaType = MAPPER.getTypeFactory().constructType(Map.class, String.class, Integer.TYPE);
 JavaType unmodifiableMapType = MAPPER.getTypeFactory().constructType(Map.class, javaType.getRawClass());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getContext(), unmodifiableMapType);
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 Object deserialized = MAPPER.readValue(MAPPER.writeValueAsString(input), deserializer.getType());
 assertNotNull(deserialized);
 assertEquals(deserialized.getClass(), Collections.unmodifiableMap(input).getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForUnknownType() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getContext(), javaType);
 assertNull(deserializer);
 }

}