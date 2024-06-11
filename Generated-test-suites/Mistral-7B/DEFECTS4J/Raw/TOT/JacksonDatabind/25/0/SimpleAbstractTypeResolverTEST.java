package com.fasterxml.jackson.databind.module.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, LinkedList.class);
 resolver.addMapping(List.class, LinkedList.class);
 }

 @Test
 public void testCollectionMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[\"apple\", \"banana\", \"orange\"]";
 List<String> list = mapper.readValue(json, new TypeReference<List<String>>(){});
 assertEquals(LinkedList.class, list.getClass());
 }

 @Test
 public void testListMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[\"apple\", \"banana\", \"orange\"]";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){});
 List<String> list = mapper.readValue(json, javaType);
 assertEquals(LinkedList.class, list.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalMapping() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 try {
 resolver.addMapping(String.class, Integer.class);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalMappingSuperAndSubSameClass() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 try {
 resolver.addMapping(String.class, String.class);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalMappingSubNotSubType() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 try {
 resolver.addMapping(String.class, Integer[].class);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalMappingAbstractTypeNotAbstract() {
 SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
 try {
 resolver.addMapping(String[].class, String.class);
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got " + e.getMessage());
 }
 }

 @Test
 public void testNullMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "null";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){});
 try {
 mapper.readValue(json, javaType);
 fail("Expected java.lang.IllegalArgumentException: Can not construct instance of java.util.LinkedList from String value (null)");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEmptyListMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[]";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){});
 List<String> list = mapper.readValue(json, javaType);
 assertNotNull(list);
 assertTrue(list.isEmpty());
 assertEquals(LinkedList.class, list.getClass());
 }

 @Test
 public void testEmptyMapMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "{}";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String,String>>(){});
 Map<String,String> map = mapper.readValue(json, javaType);
 assertNotNull(map);
 assertTrue(map.isEmpty());
 assertEquals(HashMap.class, map.getClass());
 }

 @Test
 public void testListOfMapsMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[{\"key\":\"value1\",\"value\":\"value1\"},{\"key\":\"value2\",\"value\":\"value2\"}]";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Map<String, String>>>(){});
 List<Map<String, String>> list = mapper.readValue(json, javaType);
 assertEquals(LinkedList.class, list.getClass());
 for (Map<String, String> map : list) {
 assertEquals(HashMap.class, map.getClass());
 assertEquals(2, map.size());
 }
 }

 @Test
 public void testNestedListMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[[\"apple\", \"banana\", \"orange\"],[\"grape\", \"kiwi\", \"mango\"]]";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<List<String>>>(){});
 List<List<String>> list = mapper.readValue(json, javaType);
 assertEquals(LinkedList.class, list.getClass());
 for (List<String> innerList : list) {
 assertEquals(LinkedList.class, innerList.getClass());
 assertEquals(3, innerList.size());
 }
 }

 @Test
 public void testNullInListMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[null,\"apple\", null]";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){});
 List<String> list = mapper.readValue(json, javaType);
 assertEquals(LinkedList.class, list.getClass());
 assertEquals(3, list.size());
 assertTrue(list.get(0) == null);
 assertTrue(list.get(1) != null);
 assertTrue(list.get(2) == null);
 }

 @Test
 public void testNullInNestedListMapping() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 String json = "[[null,\"apple\"],[null, null]]";
 JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<List<String>>>(){});
 List<List<String>> list = mapper.readValue(json, javaType);
 assertEquals(LinkedList.class, list.getClass());
 for (List<String> innerList : list) {
 assertEquals(2, innerList.size());
 assertTrue(innerList.get(0) == null);
 assertTrue(innerList.get(1) == null);
 }
 }

 @Test
 public void testCircularDependency() {
 DeserializationConfig config = new DeserializationConfig();
 config.setTypeResolver(resolver);
 ObjectMapper mapper = new ObjectMapper(config);

 JavaType javaType = TypeFactory.defaultInstance()
 .constructType(new TypeReference<A<B<C<A<B<C>>>>>>() {});
 try {
 mapper.readValue(mapper.writeValueAsString(new A<B<C<A<B<C>>>>>(){}), javaType);
 fail("Expected java.lang.IllegalStateException: Circular reference detected: class [B] in field value [B]");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 private static class A<T> {
 private T value;

 public A(T value) {
 this.value = value;
 }

 public T getValue() {
 return value;
 }
 }

 private static class B<T> {
 private A<C<B<T>>> a;

 public B(A<C<B<T>>> a) {
 this.a = a;
 }

 public A<C<B<T>>> getA() {
 return a;
 }
 }

 private static class C<T> {
 // empty
 }

}