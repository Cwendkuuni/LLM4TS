package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper objectMapper = new JsonMapper();

 @org.junit.Test
 public void testFindForCollection_SingletonList() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("list", Arrays.asList(1, 2, 3).getClass().getName());
 Object output = objectMapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST, output.getClass());
 List<Integer> list = ((StdDelegatingDeserializer<Object>) output).getDeserializer().deserialize(null, new TypeReference<List<Integer>>() {}.getType(), null);
 assertEquals(Arrays.asList(1), list);
 }

 @org.junit.Test
 public void testFindForCollection_SingletonSet() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("set", Collections.singleton(1).getClass().getName());
 Object output = objectMapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET, output.getClass());
 Set<Integer> set = ((StdDelegatingDeserializer<Object>) output).getDeserializer().deserialize(null, new TypeReference<Set<Integer>>() {}.getType(), null);
 assertEquals(Collections.singleton(1), set);
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("list", Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass().getName());
 Object output = objectMapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST, output.getClass());
 List<Integer> list = ((StdDelegatingDeserializer<Object>) output).getDeserializer().deserialize(null, new TypeReference<List<Integer>>() {}.getType(), null);
 assertEquals(Collections.unmodifiableList(Arrays.asList(1)), list);
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("set", Collections.unmodifiableSet(Collections.singleton(1)).getClass().getName());
 Object output = objectMapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET, output.getClass());
 Set<Integer> set = ((StdDelegatingDeserializer<Object>) output).getDeserializer().deserialize(null, new TypeReference<Set<Integer>>() {}.getType(), null);
 assertEquals(Collections.unmodifiableSet(Collections.singleton(1)), set);
 }

 @org.junit.Test
 public void testFindForCollection_InvalidType() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("type", "java.util.ArrayList".getClass().getName());
 ObjectMapper mapper = new JsonMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 try {
 Object output = mapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @org.junit.Test
 public void testFindForMap_SingletonMap() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("map", Collections.singletonMap("key", "value").getClass().getName());
 Object output = objectMapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP, output.getClass());
 Map<String, String> map = ((StdDelegatingDeserializer<Object>) output).getDeserializer().deserialize(null, new TypeReference<Map<String, String>>() {}.getType(), null);
 assertEquals(Collections.singletonMap("key", "value"), map);
 }

 @org.junit.Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("map", Collections.unmodifiableMap(Collections.singletonMap("key", "value")).getClass().getName());
 Object output = objectMapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 assertEquals(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP, output.getClass());
 Map<String, String> map = ((StdDelegatingDeserializer<Object>) output).getDeserializer().deserialize(null, new TypeReference<Map<String, String>>() {}.getType(), null);
 assertEquals(Collections.unmodifiableMap(Collections.singletonMap("key", "value")), map);
 }

 @org.junit.Test
 public void testFindForMap_InvalidType() throws Exception {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("type", "java.util.HashMap".getClass().getName());
 ObjectMapper mapper = new JsonMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 try {
 Object output = mapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.class.getTypeName());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}