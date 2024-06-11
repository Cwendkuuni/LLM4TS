package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 List<Integer> inputList = Arrays.asList(1, 2, 3);
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
 List<Integer> outputList = mapper.readValue(mapper.createParser("[]"), new TypeReference<List<Integer>>() {});
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 assertEquals(outputList, deserializer.deserialize(inputList));
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 Set<Integer> inputSet = new HashSet<>(Arrays.asList(1, 2, 3));
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeSet);
 Set<Integer> outputSet = mapper.readValue(mapper.createParser("[]"), new TypeReference<Set<Integer>>() {});
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 assertEquals(outputSet, deserializer.deserialize(inputSet));
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 List<Integer> inputList = Arrays.asList(1, 2, 3);
 List<Integer> unmodifiableList = Collections.unmodifiableList(inputList);
 JavaType javaTypeList = mapper.getTypeFactory().constructType(List.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
 List<Integer> outputList = mapper.readValue(mapper.createParser("[]"), new TypeReference<List<Integer>>() {});
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 assertEquals(outputList, deserializer.deserialize(unmodifiableList));
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 Set<Integer> inputSet = new HashSet<>(Arrays.asList(1, 2, 3));
 Set<Integer> unmodifiableSet = Collections.unmodifiableSet(inputSet);
 JavaType javaTypeSet = mapper.getTypeFactory().constructType(Set.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeSet);
 Set<Integer> outputSet = mapper.readValue(mapper.createParser("[]"), new TypeReference<Set<Integer>>() {});
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 assertEquals(outputSet, deserializer.deserialize(unmodifiableSet));
 }

 @Test
 public void testFindForCollection_UnsupportedType() throws Exception {
 JavaType javaTypeStringList = mapper.getTypeFactory().constructType(List.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeStringList);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 Map<String, Integer> inputMap = new HashMap<>();
 inputMap.put("key1", 1);
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
 Map<String, Integer> outputMap = mapper.readValue(mapper.createParser("{}"), new TypeReference<Map<String, Integer>>() {});
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 assertEquals(outputMap, deserializer.deserialize(inputMap));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 Map<String, Integer> inputMap = new HashMap<>();
 inputMap.put("key1", 1);
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(inputMap);
 JavaType javaTypeMap = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
 Map<String, Integer> outputMap = mapper.readValue(mapper.createParser("{}"), new TypeReference<Map<String, Integer>>() {});
 assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
 assertEquals(outputMap, deserializer.deserialize(unmodifiableMap));
 }

 @Test
 public void testFindForMap_UnsupportedType() throws Exception {
 JavaType javaTypeStringMap = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeStringMap);
 assertNull(deserializer);
 }
}