package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testFindForCollection() throws Exception {
 List<Integer> singleList = Arrays.asList(1);
 Set<Integer> singleSet = Collections.singleton(2);
 Map<String, Integer> singleMap = Collections.singletonMap("key", 3);
 List<Integer> unmodifiableList = Collections.unmodifiableList(Arrays.asList(4, 5));
 Set<Integer> unmodifiableSet = Collections.unmodifiableSet(Arrays.asList(6, 7));
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("key2", 8));

 JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
 JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Integer.class);
 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), listType);
 assertEquals(List.class, deserializer.getClass());
 assertEquals(singleList, deserializer.deserialize(mapper, null, listType, null));

 deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), setType);
 assertEquals(Set.class, deserializer.getClass());
 assertEquals(singleSet, deserializer.deserialize(mapper, null, setType, null));

 deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapType);
 assertEquals(Map.class, deserializer.getClass());
 assertEquals(singleMap, deserializer.deserialize(mapper, null, mapType, null));

 deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), listType);
 assertEquals(List.class, deserializer.getClass());
 assertEquals(unmodifiableList, deserializer.deserialize(mapper, null, listType, null));

 deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), setType);
 assertEquals(Set.class, deserializer.getClass());
 assertEquals(unmodifiableSet, deserializer.deserialize(mapper, null, setType, null));

 deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), mapType);
 assertEquals(Map.class, deserializer.getClass());
 assertEquals(unmodifiableMap, deserializer.deserialize(mapper, null, mapType, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollectionUnknownType() throws Exception {
 JavaType unknownType = mapper.getTypeFactory().constructType(UnknownClass.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unknownType);
 fail("Unexpected deserializer for unknown type: " + deserializer.getClass());
 }

 @Test
 public void testFindForMap() throws Exception {
 List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<>();
 listOfEntries.add(new AbstractMap.SimpleEntry<>("key1", 1));
 Map<String, Integer> map = new HashMap<>();
 map.putAll(listOfEntries);
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);

 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapType);
 assertEquals(Map.class, deserializer.getClass());
 assertEquals(map, deserializer.deserialize(mapper, null, mapType, null));

 deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapType);
 assertEquals(Map.class, deserializer.getClass());
 assertEquals(unmodifiableMap, deserializer.deserialize(mapper, null, mapType, null));
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMapUnknownType() throws Exception {
 JavaType unknownType = mapper.getTypeFactory().constructType(UnknownClassMap.class);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unknownType);
 fail("Unexpected deserializer for unknown type: " + deserializer.getClass());
 }

 @Test
 public void testConverter() throws Exception {
 List<Integer> singleList = Arrays.asList(1);
 Set<Integer> singleSet = Collections.singleton(2);
 Map<String, Integer> singleMap = Collections.singletonMap("key", 3);
 List<Integer> unmodifiableList = Collections.unmodifiableList(Arrays.asList(4, 5));
 Set<Integer> unmodifiableSet = Collections.unmodifiableSet(Arrays.asList(6, 7));
 Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("key2", 8));

 JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
 JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Integer.class);
 JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), listType);
 assertEquals(List.class, deserializer.getClass());
 Object result = deserializer.deserialize(mapper, null, listType, null);
 assertEquals(List.class, result.getClass());
 assertEquals(singleList, result);

 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, listType, List.class);
 result = converter.convert(singleList);
 assertEquals(List.class, result.getClass());
 assertEquals(singleList, result);

 deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), setType);
 assertEquals(Set.class, deserializer.getClass());
 result = deserializer.deserialize(mapper, null, setType, null);
 assertEquals(Set.class, result.getClass());
 assertEquals(singleSet, result);

 converter = JavaUtilCollectionsDeserializers.converter(2, setType, Set.class);
 result = converter.convert(singleSet);
 assertEquals(Set.class, result.getClass());
 assertEquals(singleSet, result);

 deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapType);
 assertEquals(Map.class, deserializer.getClass());
 result = deserializer.deserialize(mapper, null, mapType, null);
 assertEquals(Map.class, result.getClass());
 assertEquals(singleMap, result);

 converter = JavaUtilCollectionsDeserializers.converter(3, mapType, Map.class);
 result = converter.convert(singleMap);
 assertEquals(Map.class, result.getClass());
 assertEquals(singleMap, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConverterSingletonSizeMismatch() throws Exception {
 Set<Integer> set = Collections.singleton(1);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(2, set.getClass(), Set.class);
 fail("Unexpected result for converter with size mismatch: " + converter.convert(set));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConverterUnsupportedCollectionType() throws Exception {
 List<String> list = Arrays.asList("a", "b");
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, list.getClass(), Set.class);
 fail("Unexpected result for converter with unsupported collection type: " + converter.convert(list));
 }

 @Test
 public void testConverterNullInput() throws Exception {
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, listType, List.class);
 assertEquals(null, converter.convert(null));
 }

}

class UnknownClass {
 // empty class for testing unknown type handling
}

class UnknownClassMap extends HashMap<String, Integer> {
 // override equals() for testing unmodifiable map handling
 @Override
 public boolean equals(Object o) {
 return super.equals(o);
 }
}