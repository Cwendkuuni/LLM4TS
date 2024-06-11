package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(List.class, ArrayList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType listType = new TypeReference<List<Integer>>(){}.getType();
 JavaType expectedType = new TypeReference<ArrayList<Integer>>(){}.getType();
 JavaType actualType = resolver.findTypeMapping(config, listType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testFindTypeMappingWithAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType abstractListType = new ClassKey(List.class).getType(config);
 JavaType expectedType = new ClassKey(ArrayList.class).getType(config);
 JavaType actualType = resolver.findTypeMapping(config, abstractListType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testFindTypeMappingWithInvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType listType = new TypeReference<List<Integer>>(){}.getType();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(String.class, List.class); // invalid mapping
 JavaType actualType = resolver.findTypeMapping(config, listType);
 assertNull(actualType);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType abstractListType = new ClassKey(List.class).getType(config);
 JavaType actualType = resolver.resolveAbstractType(config, abstractListType);
 assertNull(actualType);
 }

 @Test
 public void testResolveAbstractTypeWithValidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType abstractListType = new ClassKey(List.class).getType(config);
 resolver.addMapping(List.class, ArrayList.class);
 JavaType expectedType = new ClassKey(ArrayList.class).getType(config);
 JavaType actualType = resolver.resolveAbstractType(config, abstractListType);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testResolveAbstractTypeWithMultipleMappings() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType abstractListType = new ClassKey(List.class).getType(config);
 resolver.addMapping(List.class, ArrayList.class);
 resolver.addMapping(List.class, LinkedList.class); // multiple mappings
 JavaType expectedType1 = new ClassKey(ArrayList.class).getType(config);
 JavaType expectedType2 = new ClassKey(LinkedList.class).getType(config);
 JavaType actualType = resolver.resolveAbstractType(config, abstractListType);
 assertEquals(expectedType1, actualType); // first mapping takes priority
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSubType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 List<String> list = new ArrayList<>();
 resolver.addMapping(List.class, (Class<?>) list.getClass()); // invalid subtype
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMappingInvalidSuperType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 ObjectMapper mapper = new ObjectMapper();
 JavaType javaUtilMapType = mapper.getTypeFactory().constructType(java.util.Map.class, String.class, Integer.class);
 resolver.addMapping((Class<T>) javaUtilMap.getClass(), ArrayList.class); // invalid supertype
 }
}