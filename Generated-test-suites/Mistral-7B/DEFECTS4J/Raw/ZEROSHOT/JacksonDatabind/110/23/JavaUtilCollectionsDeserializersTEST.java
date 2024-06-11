package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 SimpleModule module = new SimpleModule();
 module.addDeserializer(List.class, JavaUtilCollectionsDeserializers.class);
 module.addDeserializer(Set.class, JavaUtilCollectionsDeserializers.class);
 module.addDeserializer(Map.class, JavaUtilCollectionsDeserializers.class);
 objectMapper.registerModule(module);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException {
 Set<Boolean> singletonSet = Collections.singleton(Boolean.TRUE);
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Set.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 List<Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(singletonSet), new TypeReference<List<Object>>() {});
 assertEquals(Boolean.TRUE, deserialized.get(0));
 }

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 List<Boolean> singletonList = Collections.singletonList(Boolean.TRUE);
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, List.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 List<Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(singletonList), new TypeReference<List<Object>>() {});
 assertEquals(Boolean.TRUE, deserialized.get(0));
 }

 @Test
 public void testFindForCollection_SingletonMap() throws JsonMappingException {
 Map<String, String> singletonMap = Collections.singletonMap("a", "b");
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Map.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 Map<String, Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(singletonMap), new TypeReference<Map<String, Object>>() {});
 assertEquals("b", deserialized.get("a"));
 }

 @Test
 public void testFindForCollection_EmptyList() throws JsonMappingException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, List.class);
 List<Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(Collections.emptyList()), new TypeReference<List<Object>>() {});
 assertNull(deserialized);
 }

 @Test
 public void testFindForCollection_EmptySet() throws JsonMappingException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Set.class);
 Set<Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(Collections.emptySet()), new TypeReference<Set<Object>>() {});
 assertNull(deserialized);
 }

 @Test
 public void testFindForCollection_EmptyMap() throws JsonMappingException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Map.class);
 Map<String, Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(Collections.emptyMap()), new TypeReference<Map<String, Object>>() {});
 assertNull(deserialized);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
 List<Boolean> unmodifiableList = Arrays.asList(Boolean.TRUE);
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, List.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 List<Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(Collections.unmodifiableList(unmodifiableList)), new TypeReference<List<Object>>() {});
 assertEquals(Boolean.TRUE, deserialized.get(0));
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
 Set<Boolean> unmodifiableSet = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Set.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 Set<Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(unmodifiableSet), new TypeReference<Set<Object>>() {});
 assertEquals(Boolean.TRUE, deserialized.contains(Boolean.TRUE));
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
 Map<String, String> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Map.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 Map<String, Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(unmodifiableMap), new TypeReference<Map<String, Object>>() {});
 assertEquals("b", deserialized.get("a"));
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException {
 Map<String, String> singletonMap = Collections.singletonMap("a", "b");
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Map.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
 Map<String, Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(singletonMap), new TypeReference<Map<String, Object>>() {});
 assertEquals("b", deserialized.get("a"));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
 Map<String, String> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Map.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
 Map<String, Object> deserialized = objectMapper.readValue(new ObjectMapper().writeValueAsString(unmodifiableMap), new TypeReference<Map<String, Object>>() {});
 assertEquals("b", deserialized.get("a"));
 }

 @Test
 public void testFindForMap_EmptyMap() throws JsonMappingException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializersTest.class, Map.class);
 assertNull(JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType));
 }
}