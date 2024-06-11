package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static JavaType getTestType(DeserializationContext ctxt, int type) {
 return new TypeReference<Object>() {}.getType(ctxt.constructType(Object.class, int.class));
 }

 @ suppressWarnings("unchecked")
 @Test
 public void testFindForCollection_SingletonList() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<List<Object>>() {}.getType());
 String json = "[true]";
 List<Object> list = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertEquals(list.get(0), readValue);
 }

 @Test
 public void testFindForCollection_SingletonList_Null() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<List<Object>>() {}.getType());
 String json = "null";
 List<Object> list = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertNull(readValue);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Set<Object>>() {}.getType());
 String json = "[true]";
 Set<Object> set = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertEquals(set.iterator().next(), readValue);
 }

 @Test
 public void testFindForCollection_SingletonSet_Null() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Set<Object>>() {}.getType());
 String json = "null";
 Set<Object> set = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertNull(readValue);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<List<Object>>() {}.getType());
 String json = "[true]";
 List<Object> list = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertSame(list, readValue);
 }

 @Test
 public void testFindForCollection_UnmodifiableList_Null() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<List<Object>>() {}.getType());
 String json = "null";
 List<Object> list = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertNull(readValue);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnexpectedType() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Map<Object, Object>>() {}.getType());
 String json = "[true]";
 List<Object> list = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 }

 @suppressWarnings("unchecked")
 @Test
 public void testFindForMap_SingletonMap() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Map<Object, Object>>() {}.getType());
 String json = "{\"key\":true}";
 Map<Object, Object> map = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertEquals(map.entrySet().iterator().next().getValue(), readValue);
 }

 @Test
 public void testFindForMap_SingletonMap_Null() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Map<Object, Object>>() {}.getType());
 String json = "null";
 Map<Object, Object> map = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertNull(readValue);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Map<Object, Object>>() {}.getType());
 String json = "{\"key\":true}";
 Map<Object, Object> map = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertSame(map, readValue);
 }

 @Test
 public void testFindForMap_UnmodifiableMap_Null() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<Map<Object, Object>>() {}.getType());
 String json = "null";
 Map<Object, Object> map = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 Object readValue = ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 assertNull(readValue);
 }

 @suppressWarnings("unchecked")
 @Test(expected = JsonMappingException.class)
 public void testFindForMap_UnexpectedType() throws JsonProcessingException {
 DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
 ObjectReader reader = ctxt.readerFor(new TypeReference<List<Object>>() {}.getType());
 String json = "{\"key\":true}";
 Map<Object, Object> map = reader.readValue(new StringReader(json));
 JavaType type = getTestType(ctxt, JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 ctxt.readValue(new ByteArrayInputStream(json.getBytes()), deserializer);
 }

}