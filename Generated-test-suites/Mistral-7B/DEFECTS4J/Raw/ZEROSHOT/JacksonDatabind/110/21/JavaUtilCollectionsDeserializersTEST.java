package com.yourpackage;

import com.fasterxml.jackson.core.base.TypeAttributes;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(List.class, Boolean.class);
 Object input = Arrays.asList(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 assertEquals(List.class, deserializer.getTargetValueType(null));
 Object output = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(input)), Object.class);
 assertEquals(input, deserializer.deserialize(null, javaType, null));
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(Set.class, Boolean.class);
 Object input = Collections.singleton(Boolean.TRUE);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 assertEquals(Set.class, deserializer.getTargetValueType(null));
 Object output = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(input)), Object.class);
 assertEquals(input, deserializer.deserialize(null, javaType, null));
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(List.class, Boolean.class);
 Object input = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 assertEquals(List.class, deserializer.getTargetValueType(null));
 Object output = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(input)), Object.class);
 assertTrue(output instanceof List);
 assertEquals(input, output);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(Set.class, Boolean.class);
 Object input = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 assertEquals(Set.class, deserializer.getTargetValueType(null));
 Object output = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(input)), Object.class);
 assertTrue(output instanceof Set);
 assertEquals(input, output);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(Map.class, String.class, String.class);
 Object input = Collections.singletonMap("key", "value");
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
 assertEquals(Map.class, deserializer.getTargetValueType(null));
 Object output = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(input)), Object.class);
 assertEquals(input, output);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(Map.class, String.class, String.class);
 Object input = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
 assertEquals(Map.class, deserializer.getTargetValueType(null));
 Object output = objectMapper.readValue(new ByteArrayInputStream(objectMapper.writeValueAsBytes(input)), Object.class);
 assertTrue(output instanceof Map);
 assertEquals(input, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_UnknownType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(Object.class, Boolean.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);
 fail("Unexpected deserializer for unknown type: " + deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_UnknownType() throws Exception {
 JavaType javaType = TypeFactory.defaultInstance().constructParametricType(Object.class, String.class, String.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);
 fail("Unexpected deserializer for unknown type: " + deserializer);
 }
}