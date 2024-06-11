package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>() {});
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);

 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMappingWithCustomType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<ArrayList<String>>() {});
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);

 assertEquals(ArrayList.class, resolvedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);

 // This should throw an IllegalArgumentException
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);

 assertEquals(null, resolvedType);
 }

 @Test
 public void testResolveAbstractTypeWithCustomType() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<HashMap<String, Integer>>() {});
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);

 // This should return null, since abstract types are not resolved
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingWithInvalidTypes() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

 resolver.addMapping(String.class, Integer.class);
 }
}