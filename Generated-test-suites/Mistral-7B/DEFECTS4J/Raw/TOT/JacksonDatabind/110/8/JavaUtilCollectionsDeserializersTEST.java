package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals(Boolean.TRUE, mapper.readValue(json.traverse(), deserializer));
 }

 @Test
 public void testFindForCollection_SingletonList_Null() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("null");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertNull(mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_SingletonList_EmptyArray() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("[null]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertInstanceOf(ArrayList.class, mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("{\"class\":\"java.util.HashSet\",\"value\":[true]}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals(Boolean.TRUE, mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_SingletonSet_Null() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("null");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertNull(mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_SingletonSet_EmptyMap() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertInstanceOf(HashSet.class, mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("[true]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertEquals(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)), mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_UnmodifiableList_Null() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("null");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertNull(mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForCollection_UnmodifiableList_EmptyArray() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 JsonNode json = mapper.readTree("[null]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

 assertInstanceOf(ArrayList.class, mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonNode json = mapper.readTree("{\"key\":\"a\", \"value\":\"b\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertEquals(Collections.singletonMap("a", "b"), mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForMap_SingletonMap_Null() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonNode json = mapper.readTree("null");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertNull(mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForMap_SingletonMap_EmptyMap() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonNode json = mapper.readTree("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertInstanceOf(HashMap.class, mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonNode json = mapper.readTree("{\"key\":\"a\", \"value\":\"b\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertEquals(Collections.unmodifiableMap(Collections.singletonMap("a", "b")), mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForMap_UnmodifiableMap_Null() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonNode json = mapper.readTree("null");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertNull(mapper.readValue(json, deserializer));
 }

 @Test
 public void testFindForMap_UnmodifiableMap_EmptyMap() throws JsonProcessingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 JsonNode json = mapper.readTree("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

 assertInstanceOf(HashMap.class, mapper.readValue(json, deserializer));
 }

}