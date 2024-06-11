package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.*;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollectionSingletonSet() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollectionSingletonList() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollectionSingletonMap() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollectionUnmodifiableSet() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollectionUnmodifiableList() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollectionUnmodifiableMap() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMapSingletonSet() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Map.class, Set.class, String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMapSingletonList() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Map.class, List.class, String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMapSingletonMap() throws Exception {
 JavaType type = mapper.getTypeFactory().constructType(Map.class, Map.class, String.class);
 DeserializationContext context = mapper.getDeserializationContext();
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, type);
 assertNotNull(deserializer);
 }
}