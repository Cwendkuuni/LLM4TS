package com.example.junit;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
 }

 @Test
 public void testFindForCollection_EmptyList() throws Exception {
 JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNull(deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
 JavaType javaTypeUnmodifiable = mapper.getTypeFactory().constructType(List.class, Integer.class, Collections.unmodifiableList(Arrays.asList(1)));
 Object deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeUnmodifiable);
 assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableList_WrongType() throws Exception {
 JavaType javaType = new TypeReference<List<Integer>>() {}.getType();
 JavaType javaTypeUnmodifiableMap = mapper.getTypeFactory().constructType(Map.class, Integer.class, Integer.class, Collections.unmodifiableMap(Arrays.asList(new Integer[]{1, 1})));
 Object deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeUnmodifiableMap);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
 }

 @Test
 public void testFindForMap_EmptyMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
 Object deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
 JavaType javaTypeUnmodifiable = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.class, Collections.unmodifiableMap(Arrays.asList(new Map.Entry<String, Integer>[] {new Map.Entry<>("k", 1)})));
 Object deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeUnmodifiable);
 assertInstanceOf(StdDelegatingDeserializer.class, deserializer);
 }

 @Test
 public void testFindForMap_UnmodifiableMap_WrongType() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
 JavaType javaTypeUnmodifiableList = mapper.getTypeFactory().constructType(List.class, Integer.class, Collections.unmodifiableList(Arrays.asList(1)));
 Object deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeUnmodifiableList);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_UnmodifiableMap_WrongClass() throws Exception {
 JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
 JavaType javaTypeUnmodifiableSet = mapper.getTypeFactory().constructType(Set.class, Integer.class, Collections.unmodifiableSet(Collections.singleton(1)));
 Object deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeUnmodifiableSet);
 assertNull(deserializer);
 }

}