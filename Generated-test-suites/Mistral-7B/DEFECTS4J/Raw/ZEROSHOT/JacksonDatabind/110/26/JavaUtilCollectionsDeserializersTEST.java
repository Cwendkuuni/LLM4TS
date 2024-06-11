package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueHolderDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 Set<Boolean> set = new HashSet<>(Collections.singleton(Boolean.TRUE));
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeSet);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(set);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(set, readValue);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 List<Boolean> list = new ArrayList<>(Collections.singletonList(Boolean.TRUE));
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeList);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(list);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(list, readValue);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 Map<String, String> map = new HashMap<>(Collections.singletonMap("key", "value"));
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeMap);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(map);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(map, readValue);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 Set<Boolean> set = Collections.unmodifiableSet(new HashSet<>(Collections.singleton(Boolean.TRUE)));
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeSet);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(set);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(set, readValue);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 List<Boolean> list = Collections.unmodifiableList(new ArrayList<>(Collections.singletonList(Boolean.TRUE)));
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeList);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(list);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(list, readValue);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 Map<String, String> map = new HashMap<>(Collections.singletonMap("key", "value"));
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaTypeMap);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(map);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(map, readValue);
 }

 @Test
 public void testFindForMapUnmodifiableMap() throws Exception {
 Map<String, String> map = Collections.unmodifiableMap(new HashMap<>(Collections.singletonMap("key", "value")));
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaTypeMap);
 StdValueHolderDeserializer delegate = (StdValueHolderDeserializer) deserializer;
 JsonNode node = mapper.valueToTree(map);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, delegate);
 assertEquals(map, readValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForInvalidCollection() throws Exception {
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeList);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testFindForCollectionAsList() throws Exception {
 List<String> list = Arrays.asList("one", "two", "three");
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaTypeList);
 Object readValue = mapper.readerForUpdating(null).readValue(JsonNode.class, deserializer);
 assertTrue(readValue instanceof List);
 assertEquals(list, readValue);
 }
}