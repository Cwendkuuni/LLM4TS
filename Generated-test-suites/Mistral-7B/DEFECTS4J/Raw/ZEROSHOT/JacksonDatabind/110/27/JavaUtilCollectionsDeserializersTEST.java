package com.yourpackagename.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 Object input = Arrays.asList(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(Mockito.mock(DeserializationContext.class), javaType);
 Object output = objectMapper.readValue(JsonTestUtils.mapperWriteValueAsString(input), new TypeReference<List<Boolean>>(){}.getType());
 List<Boolean> expected = Arrays.asList(Boolean.TRUE);
 assertEquals(expected, output);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 Object input = Collections.singleton(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(Mockito.mock(DeserializationContext.class), javaType);
 Object output = objectMapper.readValue(JsonTestUtils.mapperWriteValueAsString(input), new TypeReference<Set<Boolean>>(){}.getType());
 Set<Boolean> expected = Collections.singleton(Boolean.TRUE);
 assertEquals(expected, output);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 Object input = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(Mockito.mock(DeserializationContext.class), javaType);
 Object output = objectMapper.readValue(JsonTestUtils.mapperWriteValueAsString(input), new TypeReference<List<Boolean>>(){}.getType());
 List<Boolean> expected = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 assertEquals(expected, output);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 Object input = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(Mockito.mock(DeserializationContext.class), javaType);
 Object output = objectMapper.readValue(JsonTestUtils.mapperWriteValueAsString(input), new TypeReference<Set<Boolean>>(){}.getType());
 Set<Boolean> expected = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 assertEquals(expected, output);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Object input = Collections.singletonMap("key", "value");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(Mockito.mock(DeserializationContext.class), javaType);
 Object output = objectMapper.readValue(JsonTestUtils.mapperWriteValueAsString(input), new TypeReference<Map<String, String>>(){}.getType());
 Map<String, String> expected = Collections.singletonMap("key", "value");
 assertEquals(expected, output);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 Object input = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(Mockito.mock(DeserializationContext.class), javaType);
 Object output = objectMapper.readValue(JsonTestUtils.mapperWriteValueAsString(input), new TypeReference<Map<String, String>>(){}.getType());
 Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
 assertEquals(expected, output);
 }

 @Test
 public void testFindForUnknownType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(Mockito.mock(DeserializationContext.class), javaType);
 assertNull(deserializer);
 }
}