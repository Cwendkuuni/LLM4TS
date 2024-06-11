package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson. core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.*;

import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsConverter;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper;

 static {
 try {
 mapper = new ObjectMapper();
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @org.junit.Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 List<Integer> list = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 Object deserialized = mapper.deserializeValue("[]", javaType, deserializer);
 assertEquals(Arrays.asList(1), deserialized);
 }

 @org.junit.Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException {
 Set<Integer> set = Collections.singleton(1);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 Object deserialized = mapper.deserializeValue("[]", javaType, deserializer);
 assertEquals(set, deserialized);
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
 List<Integer> list = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaType unmodifiableJavaType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(list).getClass());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), unmodifiableJavaType);
 assertNotNull(deserializer);

 Object deserialized = mapper.deserializeValue("[]", unmodifiableJavaType, deserializer);
 assertSame(Collections.unmodifiableList(list), deserialized);
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
 Set<Integer> set = Collections.singleton(1);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JavaType unmodifiableJavaType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(set).getClass());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), unmodifiableJavaType);
 assertNotNull(deserializer);

 Object deserialized = mapper.deserializeValue("[]", unmodifiableJavaType, deserializer);
 assertSame(Collections.unmodifiableSet(set), deserialized);
 }

 @org.junit.Test
 public void testFindForMap_SingletonMap() throws JsonMappingException {
 Map<String, Integer> map = Collections.singletonMap("key", 1);
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 Object deserialized = mapper.deserializeValue("{}", javaType, deserializer);
 assertEquals(map, deserialized);
 }

 @org.junit.Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
 Map<String, Integer> map = Collections.singletonMap("key", 1);
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JavaType unmodifiableJavaType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(map).getClass());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), unmodifiableJavaType);
 assertNotNull(deserializer);

 Object deserialized = mapper.deserializeValue("{}", unmodifiableJavaType, deserializer);
 assertSame(Collections.unmodifiableMap(map), deserialized);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 fail("Deserializer should not be created for invalid type");
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 fail("Deserializer should not be created for invalid type");
 }

 @org.junit.Test
 public void testConverter_SingletonList() throws JsonMappingException {
 List<Integer> list = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, List.class);
 Object deserialized = converter.convert(list);
 assertEquals(Collections.singletonList(1), deserialized);
 }

 @org.junit.Test
 public void testConverter_SingletonSet() throws JsonMappingException {
 Set<Integer> set = Collections.singleton(1);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
 Object deserialized = converter.convert(set);
 assertEquals(set, deserialized);
 }

 @org.junit.Test
 public void testConverter_UnmodifiableList() throws JsonMappingException {
 List<Integer> list = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaType, List.class);
 Object deserialized = converter.convert(Collections.unmodifiableList(list));
 assertSame(Collections.unmodifiableList(list), deserialized);
 }

 @org.junit.Test
 public void testConverter_UnmodifiableSet() throws JsonMappingException {
 Set<Integer> set = Collections.singleton(1);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, javaType, Set.class);
 Object deserialized = converter.convert(Collections.unmodifiableSet(set));
 assertSame(Collections.unmodifiableSet(set), deserialized);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testConverter_ModifiableCollection() throws JsonMappingException {
 List<Integer> list = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaType, List.class);
 converter.convert(list); // should not be allowed to modify the original collection
 }
}