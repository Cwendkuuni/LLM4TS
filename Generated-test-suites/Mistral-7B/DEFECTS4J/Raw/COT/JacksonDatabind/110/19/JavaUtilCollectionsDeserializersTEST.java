package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.test.BaseTest;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaUtilCollectionsDeserializersTest extends BaseTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection_AsList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {});
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.where(new TypeReference<List<Integer>>() {}.withoutParameter(0)));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Integer>>() {}.where(new TypeReference<Set<Integer>>() {}.withoutParameter(0)));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList(1, 2)).getClass());
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
 JavaType keyType = mapper.getTypeFactory().constructType(Integer.class);
 JavaType valueType = mapper.getTypeFactory().constructType(String.class);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<Integer, String>>() {}.withValueType(valueType).withKeyType(keyType));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>() {}.where(new TypeReference<Map<String, Integer>>() {}.withoutParameter(0)));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
 JavaType keyType = mapper.getTypeFactory().constructType(Integer.class);
 JavaType valueType = mapper.getTypeFactory().constructType(String.class);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<Integer, String>>() {}.withValueType(valueType).withKeyType(keyType));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(getContext(), javaType);
 assertNotNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnsupportedType() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(getContext(), javaType);
 assertNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_UnsupportedType() throws JsonMappingException {
 JavaType keyType = mapper.getTypeFactory().constructType(Integer.class);
 JavaType valueType = mapper.getTypeFactory().constructType(Integer.class);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<Integer, Integer>>() {}.withValueType(valueType).withKeyType(keyType));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(getContext(), javaType);
 assertNull(deserializer);
 }

 private static DeserializationContext getContext() {
 return mapper.getDeserializationContext();
 }

}