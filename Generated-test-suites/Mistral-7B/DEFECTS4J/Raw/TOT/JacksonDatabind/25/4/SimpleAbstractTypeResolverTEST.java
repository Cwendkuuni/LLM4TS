package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, ArrayList.class);
 resolver.addMapping(List.class, ArrayList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);

 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Number>>() {}.getType());
 JavaType mappedType = config.getTypeFactory().constructType(new TypeReference<List<Number>>() {}.getType());

 JavaType result = resolver.findTypeMapping(config, javaType);
 assertNotNull(result);
 assertEquals(mappedType, result);
 }

 @Test
 public void testFindTypeMappingSubType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);

 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<LinkedList<String>>() {}.getType());
 JavaType mappedType = config.getTypeFactory().constructType(new TypeReference<ArrayList<String>>() {}.getType());

 JavaType result = resolver.findTypeMapping(config, javaType);
 assertNotNull(result);
 assertEquals(mappedType, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingInvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);

 JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
 JavaType mappedType = config.getTypeFactory().constructType(ArrayList.class);

 JavaType result = resolver.findTypeMapping(config, javaType);
 assertNull(result);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);

 JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);

 JavaType result = resolver.resolveAbstractType(config, javaType);
 assertNull(result);
 }
}