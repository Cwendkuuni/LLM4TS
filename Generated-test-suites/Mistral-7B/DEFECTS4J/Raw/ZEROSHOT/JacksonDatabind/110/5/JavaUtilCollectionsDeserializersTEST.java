package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

 private DeserializationContext context;
 private TypeFactory typeFactory;

 @Before
 public void setUp() {
 context = mock(DeserializationContext.class);
 typeFactory = mock(TypeFactory.class);
 }

 @Test
 public void testFindForCollection_SingletonList() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<List<Boolean>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_SingletonSet() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<Set<Boolean>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<List<Boolean>>() {}.getType());
 JavaType unmodifyableListType = typeFactory.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);
 when(context.findValueDeserializer(unmodifyableListType)).thenReturn(null);
 when(context.handleUnknownProperty("unmodifiableList", javaType)).thenReturn(null);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<Set<Boolean>>() {}.getType());
 JavaType unmodifyableSetType = typeFactory.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);
 when(context.findValueDeserializer(unmodifyableSetType)).thenReturn(null);
 when(context.handleUnknownProperty("unmodifiableSet", javaType)).thenReturn(null);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMap_SingletonMap() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<Map<String, String>>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 assertNotNull(deserializer);
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<Map<String, String>>() {}.getType());
 JavaType unmodifyableMapType = typeFactory.constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);
 when(context.findValueDeserializer(unmodifyableMapType)).thenReturn(null);
 when(context.handleUnknownProperty("unmodifiableMap", javaType)).thenReturn(null);
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 assertNotNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_InvalidType() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<Integer[][]>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
 fail("Should throw JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_InvalidType() throws Exception {
 JavaType javaType = typeFactory.constructType(new TypeReference<Double[][]>() {}.getType());
 JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testConvert_SingletonSet() throws Exception {
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, typeFactory.constructType(Set.class));
 Object result = converter.convert(Collections.singleton(Boolean.TRUE));
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testConvert_SingletonList() throws Exception {
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, typeFactory.constructType(List.class));
 Object result = converter.convert(Collections.singletonList(Boolean.TRUE));
 assertEquals(Boolean.TRUE, ((List) result).get(0));
 }

 @Test
 public void testConvert_UnmodifiableSet() throws Exception {
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, typeFactory.constructType(Set.class));
 Set<Boolean> input = Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE));
 Object result = converter.convert(input);
 assertEquals(input, result);
 }

 @Test
 public void testConvert_UnmodifiableList() throws Exception {
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, typeFactory.constructType(List.class));
 List<Boolean> input = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 Object result = converter.convert(input);
 assertEquals(input, result);
 }

 @Test
 public void testConvert_UnmodifiableMap() throws Exception {
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, typeFactory.constructType(Map.class));
 Map<String, String> input = Collections.unmodifiableMap(Collections.singletonMap("test", "test"));
 Object result = converter.convert(input);
 assertEquals(input, result);
 }

 @Test
 public void testConvert_AsList() throws Exception {
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, typeFactory.constructType(List.class));
 List<Boolean> input = Arrays.asList(Boolean.TRUE);
 Object result = converter.convert(input);
 assertEquals(input, result);
 }

}