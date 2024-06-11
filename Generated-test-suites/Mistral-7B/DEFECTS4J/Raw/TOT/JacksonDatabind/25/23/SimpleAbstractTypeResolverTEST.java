package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(ArrayList.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 assertTrue(LinkedList.class.isAssignableFrom(mappedType.getRawClass()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMappingWithInvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(String.class, Integer.class); // This should fail
 JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType mappedType = resolver.findTypeMapping(config, javaType);
 fail("Expected IllegalArgumentException but got null");
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = mapper.getTypeFactory().constructType(Collection.class);
 JavaType mappedType = resolver.resolveAbstractType(config, javaType);
 assertNull(mappedType);
 }

 @Test
 public void testResolveAbstractTypeWithMapping() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = mapper.getTypeFactory().constructType(AbstractList.class);
 JavaType mappedType = resolver.resolveAbstractType(config, javaType);
 assertEquals(LinkedList.class, mappedType.getRawClass());
 }
}