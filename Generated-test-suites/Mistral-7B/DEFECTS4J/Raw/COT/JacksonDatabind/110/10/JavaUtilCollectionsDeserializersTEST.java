package com.example.junit; // replace with your testing package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
 Object input = Arrays.asList(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 List<Boolean> result = deserializer.deserialize(objectMapper, input);
 assertEquals(input, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.class);
 Object input = Collections.singleton(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 Set<Boolean> result = deserializer.deserialize(objectMapper, input);
 assertEquals(input, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
 Object input = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 List<Boolean> result = deserializer.deserialize(objectMapper, input);
 assertEquals(input, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.class);
 Object input = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 Set<Boolean> result = deserializer.deserialize(objectMapper, input);
 assertEquals(input, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindForCollection_InvalidType() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 Object input = Arrays.asList(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Object input = Collections.singletonMap("a", "b");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext(), javaType);
 Map<String, String> result = deserializer.deserialize(objectMapper, input);
 assertEquals(input, result);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Object input = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext(), javaType);
 Map<String, String> result = deserializer.deserialize(objectMapper, input);
 assertEquals(input, result);
 }

 @Test
 public void testFindForMap_InvalidType() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 Object input = Arrays.asList(Map.entry("a", "b"));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mockContext(), javaType);
 assertNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testConverters_NonSupportedType() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 Object input = Arrays.asList(Boolean.TRUE);
 JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 }

 @Test(expected = JsonMappingException.class)
 public void testConverters_InvalidInput() throws JsonMappingException, JsonProcessingException {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
 Object input = new Object();
 JavaUtilCollectionsDeserializers.findForCollection(mockContext(), javaType);
 }

 private DeserializationContext mockContext() {
 return mock(DeserializationContext.class);
 }
}