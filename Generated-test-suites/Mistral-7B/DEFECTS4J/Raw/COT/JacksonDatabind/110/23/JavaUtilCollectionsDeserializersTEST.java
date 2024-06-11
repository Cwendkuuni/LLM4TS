package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for JavaUtilCollectionsDeserializers
 */
public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 List<Integer> input = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<List<Integer>>() {});
 Object result = reader.readValue(mapper.createObjectCodec().writeValueAsString(input), deserializer);
 assertEquals(Collections.singletonList(input.get(0)), result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 Set<Integer> input = new HashSet<>(Arrays.asList(1, 2, 3));
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<Set<Integer>>() {});
 Object result = reader.readValue(mapper.createObjectCodec().writeValueAsString(input), deserializer);
 assertEquals(Collections.singleton(input.iterator().next()), result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 List<Integer> input = Arrays.asList(1, 2, 3);
 List<Integer> unmodifiableList = Collections.unmodifiableList(input);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<List<Integer>>() {});
 Object result = reader.readValue(mapper.createObjectCodec().writeValueAsString(unmodifiableList), deserializer);
 assertEquals(unmodifiableList, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 Set<Integer> input = new HashSet<>(Arrays.asList(1, 2, 3));
 Set<Integer> unmodifiableSet = Collections.unmodifiableSet(input);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<Set<Integer>>() {});
 Object result = reader.readValue(mapper.createObjectCodec().writeValueAsString(unmodifiableSet), deserializer);
 assertEquals(unmodifiableSet, result);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<List<Integer>>() {});
 reader.readValue(mapper.createObjectCodec().writeValueAsString(new Object()), deserializer);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 Map<String, Integer> input = new HashMap<>();
 input.put("key", 1);
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<Map<String,Integer>>() {});
 Object result = reader.readValue(mapper.createObjectCodec().writeValueAsString(input), deserializer);
 assertEquals(Collections.singletonMap("key", input.get("key")), result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 Map<String, Integer> input = new HashMap<>();
 input.put("key", 1);
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(input);
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<Map<String,Integer>>() {});
 Object result = reader.readValue(mapper.createObjectCodec().writeValueAsString(unmodifiableMap), deserializer);
 assertEquals(unmodifiableMap, result);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 ObjectReader reader = mapper.readerFor(new TypeReference<Map<String,Integer>>() {});
 reader.readValue(mapper.createObjectCodec().writeValueAsString(new Object()), deserializer);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_SingletonList() {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, List.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_SingletonSet() {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_SingletonMap() {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaType, Map.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_UnmodifiableList() {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaType, List.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_UnmodifiableSet() {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, javaType, Set.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_UnmodifiableMap() {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, javaType, Map.class);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testConverter_AsList() {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaType, List.class);
 }
}