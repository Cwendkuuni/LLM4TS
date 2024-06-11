package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.*;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 List<Boolean> list = mapper.readValue(JsonNodeFactory.instance.textNode("[]"), deserializer.getType());
 assertEquals(Collections.singletonList(Boolean.TRUE), list);
 }

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 Set<Boolean> set = mapper.readValue(JsonNodeFactory.instance.textNode("{true}"), deserializer.getType());
 assertEquals(Collections.singleton(Boolean.TRUE), set);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>(){}.getType());
 JavaType unmodifiableType = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JavaType.Static type = javaType.findIntersection(JavaType.construct(Object.class), unmodifiableType);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertNotNull(deserializer);

 List<Boolean> list = mapper.readValue(JsonNodeFactory.instance.textNode("[true]"), deserializer.getType());
 assertEquals(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)), list);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>(){}.getType());
 JavaType unmodifiableType = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JavaType.Static type = javaType.findIntersection(JavaType.construct(Object.class), unmodifiableType);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), type);
 assertNotNull(deserializer);

 Set<Boolean> set = mapper.readValue(JsonNodeFactory.instance.textNode("{true}"), deserializer.getType());
 assertEquals(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)), set);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 Map<String, Integer> map = mapper.readValue(JsonNodeFactory.instance.textNode("{\"key\":1}"), deserializer.getType());
 assertEquals(Collections.singletonMap("key", 1), map);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>(){}.getType());
 JavaType unmodifiableType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JavaType.Static type = javaType.findIntersection(JavaType.construct(Object.class), unmodifiableType);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), type);
 assertNotNull(deserializer);

 Map<String, Integer> map = mapper.readValue(JsonNodeFactory.instance.textNode("{\"key\":1}"), deserializer.getType());
 assertEquals(Collections.unmodifiableMap(Collections.singletonMap("key", 1)), map);
 }

 @Test
 public void testFindForCollectionAsList() throws Exception {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);
 assertNotNull(deserializer);

 List<String> list = mapper.readValue(JsonNodeFactory.instance.textNode("[\"hello\", \"world\"]"), deserializer.getType());
 assertEquals(Arrays.asList("hello", "world"), list);
 }
}