package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 objectMapper = new ObjectMapper();
 objectMapper.setTypeResolver(resolver);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>() {}.getType());
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMapping_Collection() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 assertEquals(LinkedList.class, resolvedType.getRawClass());
 }

 @Test
 public void testFindTypeMapping_AbstractClass() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(AbstractClassWithList.class);
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 assertEquals(AbstractSubClassWithList.class, resolvedType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMapping_IllegalMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 resolver.addMapping(String.class, Collection.class); // Illegal mapping
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMapping_SelfMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 resolver.addMapping(String.class, String.class); // Self mapping
 JavaType resolvedType = resolver.findTypeMapping(config, javaType);
 }
}

class AbstractClassWithList {
 List<String> abstractList;
}

class AbstractSubClassWithList extends AbstractClassWithList {
 // Empty
}