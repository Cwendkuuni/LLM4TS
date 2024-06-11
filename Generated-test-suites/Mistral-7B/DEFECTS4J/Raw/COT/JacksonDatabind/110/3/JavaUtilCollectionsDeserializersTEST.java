package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new JsonMapper()
 .enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>() {});
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 List<Integer> list = mapper.readValue(" [1]", List.class);
 assertEquals(list, deserializer.deserialize(mapper.getDeserializationContext(), list.getClass(), null));
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<Integer>>() {});
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Set<Integer> set = Collections.singleton(1);
 assertEquals(set, deserializer.deserialize(mapper.getDeserializationContext(), set.getClass(), null));
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, new Class[0]);
 JavaType unmodifiableListType = mapper.getTypeFactory().constructType(List.class, javaType.getRawClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableListType);
 assertNotNull(deserializer);
 List<Integer> list = Arrays.asList(1, 2, 3);
 assertEquals(Collections.unmodifiableList(list), deserializer.deserialize(mapper.getDeserializationContext(), javaType.getRawClass(), null));
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, new Class[0]);
 JavaType unmodifiableSetType = mapper.getTypeFactory().constructType(Set.class, javaType.getRawClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableSetType);
 assertNotNull(deserializer);
 Set<Integer> set = Collections.singleton(1);
 assertEquals(Collections.unmodifiableSet(set), deserializer.deserialize(mapper.getDeserializationContext(), javaType.getRawClass(), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, new Class[0]);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
 assertNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String, Integer>>() {});
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 assertNotNull(deserializer);
 Map<String, Integer> map = Collections.singletonMap("key", 1);
 assertEquals(map, deserializer.deserialize(mapper.getDeserializationContext(), map.getClass(), null));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, new Class[0]);
 JavaType unmodifiableMapType = mapper.getTypeFactory().constructType(Map.class, javaType.getRawClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unmodifiableMapType);
 assertNotNull(deserializer);
 Map<String, Integer> map = Collections.singletonMap("key", 1);
 assertEquals(Collections.unmodifiableMap(map), deserializer.deserialize(mapper.getDeserializationContext(), javaType.getRawClass(), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, new Class[0]);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
 assertNull(deserializer);
 }
}