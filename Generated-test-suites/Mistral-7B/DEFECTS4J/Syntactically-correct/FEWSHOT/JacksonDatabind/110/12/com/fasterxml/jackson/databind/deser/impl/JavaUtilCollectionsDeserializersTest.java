package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.Wrapper;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

 private static final JavaType SINGLETON_SET_TYPE = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
 private static final JavaType SINGLETON_LIST_TYPE = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
 private static final JavaType SINGLETON_MAP_TYPE = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 private static final JavaType UNMODIFIABLE_SET_TYPE = TypeFactory.defaultInstance().constructType(Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Boolean.TRUE, Boolean.FALSE))).getClass());
 private static final JavaType UNMODIFIABLE_LIST_TYPE = TypeFactory.defaultInstance().constructType(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE, Boolean.FALSE)).getClass());
 private static final JavaType UNMODIFIABLE_MAP_TYPE = TypeFactory.defaultInstance().constructType(Collections.unmodifiableMap(new HashMap<>()).getClass());

 @Test
 public void testFindForCollection() throws JsonMappingException {
 DeserializationContext ctxt = new TestContext();
 JavaType type;
 JsonDeserializer<?> deserializer;

 // Test for asList type
 type = TypeFactory.defaultInstance().constructType(Arrays.asList(Boolean.TRUE, Boolean.FALSE).getClass());
 deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);

 // Test for singleton Set type
 type = SINGLETON_SET_TYPE;
 deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);

 // Test for unmodifiable Set type
 type = UNMODIFIABLE_SET_TYPE;
 deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);

 // Test for singleton List type
 type = SINGLETON_LIST_TYPE;
 deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);

 // Test for unmodifiable List type
 type = UNMODIFIABLE_LIST_TYPE;
 deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
 assertNotNull(deserializer);

 // Test for singleton Map type
 type = SINGLETON_MAP_TYPE;
 deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 assertNotNull(deserializer);

 // Test for unmodifiable Map type
 type = UNMODIFIABLE_MAP_TYPE;
 deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
 assertNotNull(deserializer);
 }

 @Test
 public void testConvertSingletonSet() {
 Set<?> set = new HashSet<>(Arrays.asList(Boolean.TRUE));
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, set.getClass());
 Object result = converter.convert(set);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testConvertSingletonList() {
 List<?> list = Arrays.asList(Boolean.TRUE);
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, list.getClass());
 Object result = converter.convert(list);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testConvertSingletonMap() {
 Map<?,?> map = new HashMap<>();
 map.put("key", "value");
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, map.getClass());
 Object result = converter.convert(map);
 assertEquals("value", result);
 }

 @Test
 public void testConvertUnmodifiableSet() {
 Set<?> set = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Boolean.TRUE)));
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, set.getClass());
 Object result = converter.convert(set);
 assertNotNull(result);
 assertEquals(set.size(), result.getClass().getComponentType().getComponentCount());
 }

 @Test
 public void testConvertUnmodifiableList() {
 List<?> list = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, list.getClass());
 Object result = converter.convert(list);
 assertNotNull(result);
 assertEquals(list.size(), result.getClass().getComponentType().getComponentCount());
 }

 @Test
 public void testConvertUnmodifiableMap() {
 Map<?,?> map = Collections.unmodifiableMap(new HashMap<>());
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, map.getClass());
 Object result = converter.convert(map);
 assertNotNull(result);
 }

 @Test
 public void testConvertAsList() {
 List<?> list = Arrays.asList(Boolean.TRUE);
 JavaUtilCollectionsConverter converter = new JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, list.getClass());
 Object result = converter.convert(list);
 assertEquals(list, result);
 }

 static class TestContext extends DeserializationContext {
 @Override
 public Converter<Object, Object> findValueConverter(JavaType inputType, JavaType outputType) {
 return new Wrapper<JavaUtilCollectionsConverter>(new JavaUtilCollectionsConverter(0, null));
 }
 }
}