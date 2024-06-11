package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static Object mapper() {
 ObjectMapper mapper = new ObjectMapper();
 mapper.findAndRegisterModules();
 return mapper;
 }

 @org.junit.Test
 public void testFindForCollection_SingletonList() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 assertEquals(List.class, deserializer.getDeserializationContext().getDeserializerFactory().getValueDeserializer(javaType).getClass());
 }

 @org.junit.Test
 public void testFindForCollection_SingletonSet() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 assertEquals(Set.class, deserializer.getDeserializationContext().getDeserializerFactory().getValueDeserializer(javaType).getClass());
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 JavaType unmodifiableListType = new TypeReference<List<Boolean>>(){}.getType().getRawClass();
 JavaType inputType = TypeFactory.defaultInstance().constructType(Object.class, unmodIFIableListType);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, inputType);
 assertEquals(List.class, deserializer.getDeserializationContext().getDeserializerFactory().getValueDeserializer(javaType).getClass());
 }

 @org.junit.Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 JavaType unmodifiableSetType = new TypeReference<Set<Boolean>>(){}.getType().getRawClass();
 JavaType inputType = TypeFactory.defaultInstance().constructType(Object.class, unmodifiableSetType);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, inputType);
 assertEquals(Set.class, deserializer.getDeserializationContext().getDeserializerFactory().getValueDeserializer(javaType).getClass());
 }

 @org.junit.Test
 public void testFindForMap_SingletonMap() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Map.Entry<String, String>>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType.getRawClass());
 assertEquals(Map.class, deserializer.getDeserializationContext().getDeserializerFactory().getValueDeserializer(javaType).getClass());
 }

 @org.junit.Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Map<String, String>>(){}.getType();
 JavaType unmodifiableMapType = new TypeReference<Map<String, String>>(){}.getType().getRawClass();
 JavaType inputType = TypeFactory.defaultInstance().constructType(Object.class, unmodifiableMapType);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, inputType);
 assertEquals(Map.class, deserializer.getDeserializationContext().getDeserializerFactory().getValueDeserializer(javaType).getClass());
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<String>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
 fail("Should throw JsonMappingException");
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<ObjectNode>(){}.getType();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
 fail("Should throw JsonMappingException");
 }

 @org.junit.Test
 public void testConverter_SingletonSet() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
 assertEquals(JavaUtilCollectionsConverter.class, deserializer.getClass());
 }

 @org.junit.Test
 public void testConverter_SingletonList() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, List.class);
 assertEquals(JavaUtilCollectionsConverter.class, deserializer.getClass());
 }

 @org.junit.Test
 public void testConverter_SingletonMap() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Map.Entry<String, String>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaType, Map.class);
 assertEquals(JavaUtilCollectionsConverter.class, deserializer.getClass());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testConverter_SingletonWithSizeGreaterThanOne() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<ArrayList<Boolean>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
 fail("Should throw IllegalArgumentException");
 }

 @org.junit.Test
 public void testConverter_UnmodifiableSet() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Set<Boolean>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, javaType, Set.class);
 assertEquals(JavaUtilCollectionsConverter.class, deserializer.getClass());
 }

 @org.junit.Test
 public void testConverter_UnmodifiableList() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaType, List.class);
 assertEquals(JavaUtilCollectionsConverter.class, deserializer.getClass());
 }

 @org.junit.Test
 public void testConverter_UnmodifiableMap() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<Map<String, String>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, javaType, Map.class);
 assertEquals(JavaUtilCollectionsConverter.class, deserializer.getClass());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testConverter_InvalidType() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<ObjectNode>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(0, javaType, Map.class);
 fail("Should throw IllegalArgumentException");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testConverter_InvalidSuperType() throws Exception {
 DeserializationContext ctxt = new DeserializationContext(mapper(), new StandardDeserializationContext());
 JavaType javaType = new TypeReference<List<Boolean>>(){}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, Map.class);
 fail("Should throw IllegalArgumentException");
 }
}