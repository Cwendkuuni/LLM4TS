package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<List<String>>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals("List deserializer", deserializer.getClass().getName(), deserializer.getClass().getSimpleName());

 List<String> list = mapper.readValue( "[\"test\"]", JavaType.constructParametricType(List.class, String.class));
 assertNotNull(list);
 assertEquals(1, list.size());
 assertEquals("test", list.get(0));

 String json = mapper.writerWithTypeValue().writeValueAsString(list);
 Object deserialized = mapper.readValue(json, JavaType.constructParametricType(List.class, String.class));
 assertEquals(deserializer, deserializer.getClass().getDeclaredField("delegate").get(deserializer));
 assertEquals(list, deserialized);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<Set<String>>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals("Set deserializer", deserializer.getClass().getName(), deserializer.getClass().getSimpleName());

 Set<String> set = mapper.readValue( "[\"test\"]", JavaType.constructParametricType(Set.class, String.class));
 assertNotNull(set);
 assertEquals(1, set.size());
 assertEquals("test", set.iterator().next());

 String json = mapper.writerWithTypeValue().writeValueAsString(set);
 Object deserialized = mapper.readValue(json, JavaType.constructParametricType(Set.class, String.class));
 assertEquals(deserializer, deserializer.getClass().getDeclaredField("delegate").get(deserializer));
 assertEquals(set, deserialized);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<List<String>>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ctxt.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals("UnmodifiableList deserializer", deserializer.getClass().getName(), deserializer.getClass().getSimpleName());

 List<String> list = mapper.readValue("[\"test\"]", JavaType.constructParametricType(List.class, String.class));
 assertNotNull(list);
 assertEquals(1, list.size());
 assertEquals("test", list.get(0));

 // check if deserialized list is unmodifiable
 assertFalse(list.getClass().getName().contains("Mutable"));
 try {
 list.add("newTest");
 fail("Should not be able to add new element");
 } catch (UnsupportedOperationException expected) {
 // expected
 }

 String json = mapper.writerWithTypeValue().writeValueAsString(list);
 Object deserialized = mapper.readValue(json, JavaType.constructParametricType(List.class, String.class));
 assertEquals(deserializer, deserializer.getClass().getDeclaredField("delegate").get(deserializer));
 assertEquals(list, deserialized);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<Set<String>>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ctxt.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals("UnmodifiableSet deserializer", deserializer.getClass().getName(), deserializer.getClass().getSimpleName());

 Set<String> set = mapper.readValue("[\"test\"]", JavaType.constructParametricType(Set.class, String.class));
 assertNotNull(set);
 assertEquals(1, set.size());
 assertEquals("test", set.iterator().next());

 // check if deserialized set is unmodifiable
 assertFalse(set.getClass().getName().contains("Mutable"));
 try {
 set.add("newTest");
 fail("Should not be able to add new element");
 } catch (UnsupportedOperationException expected) {
 // expected
 }

 String json = mapper.writerWithTypeValue().writeValueAsString(set);
 Object deserialized = mapper.readValue(json, JavaType.constructParametricType(Set.class, String.class));
 assertEquals(deserializer, deserializer.getClass().getDeclaredField("delegate").get(deserializer));
 assertEquals(set, deserialized);
 }

 @Test
 public void testFindForCollection_UnknownType() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<Object>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<Map<String, String>>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertEquals("Map deserializer", deserializer.getClass().getName(), deserializer.getClass().getSimpleName());

 Map<String, String> map = mapper.readValue("{\"key\":\"value\"}", JavaType.constructParametricType(Map.class, String.class, String.class));
 assertNotNull(map);
 assertEquals(1, map.size());
 assertEquals("value", map.get("key"));

 String json = mapper.writerWithTypeValue().writeValueAsString(map);
 Object deserialized = mapper.readValue(json, JavaType.constructParametricType(Map.class, String.class, String.class));
 assertEquals(deserializer, deserializer.getClass().getDeclaredField("delegate").get(deserializer));
 assertEquals(map, deserialized);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<Map<String, String>>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ctxt.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertEquals("UnmodifiableMap deserializer", deserializer.getClass().getName(), deserializer.getClass().getSimpleName());

 Map<String, String> map = mapper.readValue("{\"key\":\"value\"}", JavaType.constructParametricType(Map.class, String.class, String.class));
 assertNotNull(map);
 assertEquals(1, map.size());
 assertEquals("value", map.get("key"));

 // check if deserialized map is unmodifiable
 assertFalse(map.getClass().getName().contains("Mutable"));
 try {
 map.put("newKey", "newValue");
 fail("Should not be able to put new key-value pair");
 } catch (UnsupportedOperationException expected) {
 // expected
 }

 String json = mapper.writerWithTypeValue().writeValueAsString(map);
 Object deserialized = mapper.readValue(json, JavaType.constructParametricType(Map.class, String.class, String.class));
 assertEquals(deserializer, deserializer.getClass().getDeclaredField("delegate").get(deserializer));
 assertEquals(map, deserialized);
 }

 @Test
 public void testFindForMap_UnknownType() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory()
 .constructType(new TypeReference<Object>() {});
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertNull(deserializer);
 }
}