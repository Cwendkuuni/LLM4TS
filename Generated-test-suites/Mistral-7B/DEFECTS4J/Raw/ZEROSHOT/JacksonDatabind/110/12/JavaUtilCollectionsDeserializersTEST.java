package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.impl.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdConverter;

public class JavaUtilCollectionsDeserializersTest {

 private DeserializationContext context;
 private TypeFactory typeFactory;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 context = mock(DeserializationContext.class);
 typeFactory = mock(TypeFactory.class);
 }

 @Test
 public void testFindForCollection_SingletonList() throws JsonMappingException {
 JavaType javaTypeList = typeFactory.constructType(new TypeReference<List<Boolean>>() {});
 StdConverter<Object, List<Boolean>> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeList);
 List<Boolean> expected = Arrays.asList(Boolean.TRUE);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForCollection_SingletonSet() throws JsonMappingException {
 JavaType javaTypeSet = typeFactory.constructType(new TypeReference<Set<Boolean>>() {});
 StdConverter<Object, Set<Boolean>> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeSet);
 Set<Boolean> expected = Collections.singleton(Boolean.TRUE);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
 JavaType javaTypeUnmodifiableList = typeFactory.constructType(new TypeReference<List<Boolean>>() {}.where(new TypeReference<List<Boolean>>() {}.withoutGenericTypes()));
 StdConverter<Object, List<Boolean>> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeUnmodifiableList);
 List<Boolean> expected = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
 JavaType javaTypeUnmodifiableSet = typeFactory.constructType(new TypeReference<Set<Boolean>>() {}.where(new TypeReference<Set<Boolean>>() {}.withoutGenericTypes()));
 StdConverter<Object, Set<Boolean>> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeUnmodifiableSet);
 Set<Boolean> expected = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForCollection_UnmodifiableMap() throws JsonMappingException {
 JavaType javaTypeUnmodifiableMap = typeFactory.constructType(new TypeReference<Map<String, String>>() {}.where(new TypeReference<Map<String, String>>() {}.withoutGenericTypes()));
 StdConverter<Object, Map<String, String>> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeUnmodifiableMap);
 Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForCollection_AsList() throws JsonMappingException {
 JavaType javaTypeAsList = typeFactory.constructType(List.class);
 StdConverter<Object, List<?>> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeAsList);
 List<?> list = Arrays.asList(Boolean.TRUE, Boolean.FALSE);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, list));
 }

 @Test
 public void testFindForMap_SingletonMap() throws JsonMappingException {
 JavaType javaTypeMap = typeFactory.constructType(new TypeReference<Map<String, String>>() {});
 StdConverter<Object, Map<String, String>> converter = JavaUtilCollectionsDeserializers.findForMap(context, javaTypeMap);
 Map<String, String> expected = Collections.singletonMap("key", "value");
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
 JavaType javaTypeUnmodifiableMap = typeFactory.constructType(new TypeReference<Map<String, String>>() {}.where(new TypeReference<Map<String, String>>() {}.withoutGenericTypes()));
 StdConverter<Object, Map<String, String>> converter = JavaUtilCollectionsDeserializers.findForMap(context, javaTypeUnmodifiableMap);
 Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, expected));
 }

 @Test
 public void testFindForUnknownType() throws JsonMappingException {
 JavaType javaTypeUnknown = typeFactory.constructType(List.class.getName());
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.findForCollection(context, javaTypeUnknown);
 assertNull(converter);
 }

 @Test
 public void testConverter_SingletonSet() throws JsonMappingException {
 JavaType javaTypeSet = typeFactory.constructType(new TypeReference<Set<Boolean>>() {});
 JavaType javaTypeSetSuper = typeFactory.constructType(Set.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaTypeSet, Set.class);
 Set<Boolean> set = Collections.singleton(Boolean.TRUE);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, set));
 }

 @Test
 public void testConverter_SingletonList() throws JsonMappingException {
 JavaType javaTypeList = typeFactory.constructType(new TypeReference<List<Integer>>() {});
 JavaType javaTypeListSuper = typeFactory.constructType(List.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaTypeList, List.class);
 List<Integer> list = Arrays.asList(1, 2, 3);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, list));
 }

 @Test
 public void testConverter_SingletonMap() throws JsonMappingException {
 JavaType javaTypeMap = typeFactory.constructType(new TypeReference<Map<String, Integer>>() {});
 JavaType javaTypeMapSuper = typeFactory.constructType(Map.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaTypeMap, Map.class);
 Map<String, Integer> map = Collections.singletonMap("key", 1);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, map));
 }

 @Test
 public void testConverter_UnmodifiableSet() throws JsonMappingException {
 JavaType javaTypeSet = typeFactory.constructType(new TypeReference<Set<Boolean>>() {}.where(new TypeReference<Set<Boolean>>() {}.withoutGenericTypes()));
 JavaType javaTypeSetSuper = typeFactory.constructType(Set.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, javaTypeSet, Set.class);
 Set<Boolean> set = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, set));
 }

 @Test
 public void testConverter_UnmodifiableList() throws JsonMappingException {
 JavaType javaTypeList = typeFactory.constructType(new TypeReference<List<Integer>>() {}.where(new TypeReference<List<Integer>>() {}.withoutGenericTypes()));
 JavaType javaTypeListSuper = typeFactory.constructType(List.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaTypeList, List.class);
 List<Integer> list = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, list));
 }

 @Test
 public void testConverter_UnmodifiableMap() throws JsonMappingException {
 JavaType javaTypeMap = typeFactory.constructType(new TypeReference<Map<String, Integer>>() {}.where(new TypeReference<Map<String, Integer>>() {}.withoutGenericTypes()));
 JavaType javaTypeMapSuper = typeFactory.constructType(Map.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, javaTypeMap, Map.class);
 Map<String, Integer> map = Collections.unmodifiableMap(Collections.singletonMap("key", 1));
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, map));
 }

 @Test
 public void testConverter_AsList() throws JsonMappingException {
 JavaType javaTypeAsList = typeFactory.constructType(List.class);
 JavaType javaTypeListSuper = typeFactory.constructType(List.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaTypeAsList, List.class);
 List<?> list = Arrays.asList(1, 2, 3);
 assertEquals(new StdDelegatingDeserializer<>(converter), converter(context, list));
 }

 @Test
 public void testConverter_UnknownType() throws JsonMappingException {
 JavaType javaTypeUnknown = typeFactory.constructType(List.class.getName());
 JavaType javaTypeListSuper = typeFactory.constructType(List.class);
 StdConverter<Object, Object> converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaTypeUnknown, List.class);
 List<?> list = Arrays.asList(1, 2, 3);
 assertNull(converter);
 }

}