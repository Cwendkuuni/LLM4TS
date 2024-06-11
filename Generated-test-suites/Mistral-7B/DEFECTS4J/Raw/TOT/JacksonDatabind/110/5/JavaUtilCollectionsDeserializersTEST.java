package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @org.junit.Test
 public void testFindForCollection_SingletonList() throws Exception {
 List<Integer> expectedList = Arrays.asList(1, 2, 3);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonNode jsonNode = mapper.valueToTree(expectedList);
 JsonNode json = mapper.readTree("{\"list\": "+jsonNode.toString()+"}");
 Object deserializedObject = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), json.get("list"));
 List<?> deserializedList = (List<?>) deserializedObject;
 assertEquals(expectedList, deserializedList);
 }

 @org.junit.Test
 public void testFindForCollection_SingletonSet() throws Exception {
 Set<Integer> expectedSet = Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet());
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JsonNode jsonNode = mapper.valueToTree(expectedSet);
 JsonNode json = mapper.readTree("{\"set\": "+jsonNode.toString()+"}");
 Object deserializedObject = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), json.get("set"));
 Set<?> deserializedSet = (Set<?>) deserializedObject;
 assertEquals(expectedSet, deserializedSet);
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 List<Integer> expectedList = Arrays.asList(1, 2, 3);
 List<Integer> unmodifiableList = Collections.unmodifiableList(expectedList);
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 Object deserializedObject = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType.getInstance(unmodifiableList));
 List<?> deserializedList = (List<?>) deserializedObject;
 assertEquals(expectedList, deserializedList);
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 Set<Integer> expectedSet = Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet());
 Set<Integer> unmodifiableSet = Collections.unmodifiableSet(expectedSet);
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 Object deserializedObject = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType.getInstance(unmodifiableSet));
 Set<?> deserializedSet = (Set<?>) deserializedObject;
 assertEquals(expectedSet, deserializedSet);
 }

 @org.junit.Test
 public void testFindForCollection_UnexpectedType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonNode jsonNode = mapper.valueToTree(new HashMap<>());
 JsonNode json = mapper.readTree("{\"class\": \"java.util.ArrayList\", \"list\": "+jsonNode.toString()+"}");
 assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), json.get("list")));
 }

 @org.junit.Test
 public void testFindForMap_SingletonMap() throws Exception {
 Map<String, Integer> expectedMap = new HashMap<>();
 expectedMap.put("key", 1);
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonNode jsonNode = mapper.valueToTree(expectedMap);
 JsonNode json = mapper.readTree("{\"map\": "+jsonNode.toString()+"}");
 Object deserializedObject = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), json.get("map"));
 Map<String, Integer> deserializedMap = (Map<String, Integer>) deserializedObject;
 assertEquals(expectedMap, deserializedMap);
 }

 @org.junit.Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 Map<String, Integer> expectedMap = new HashMap<>();
 expectedMap.put("key", 1);
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(expectedMap);
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 Object deserializedObject = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType.getInstance(unmodifiableMap));
 Map<String, Integer> deserializedMap = (Map<String, Integer>) deserializedObject;
 assertEquals(expectedMap, deserializedMap);
 }

 @org.junit.Test
 public void testFindForMap_UnexpectedType() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonNode jsonNode = mapper.valueToTree(new ArrayList<>());
 JsonNode json = mapper.readTree("{\"class\": \"java.util.HashMap\", \"map\": "+jsonNode.toString()+"}");
 assertNull(JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), json.get("map")));
 }

}