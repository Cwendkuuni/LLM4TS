package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

 private JavaType listType, setType, mapType;
 private JavaType unmodifiableListType, unmodifiableSetType, unmodifiableMapType;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 Class<?> arrList = Arrays.asList(null, null).getClass();
 Class<?> singletonSet = Collections.singleton(Boolean.TRUE).getClass();
 Class<?> singletonList = Collections.singletonList(Boolean.TRUE).getClass();
 Class<?> singletonMap = Collections.singletonMap("a", "b").getClass();
 Class<?> unmodifiableSet = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass();
 Class<?> unmodifiableList = Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass();
 Class<?> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass();

 JavaUtilCollectionsDeserializers.CLASS_AS_ARRAYS_LIST = arrList;
 JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET = singletonSet;
 JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST = singletonList;
 JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP = singletonMap;
 JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET = unmodifiableSet;
 JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST = unmodifiableList;
 JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP = unmodifiableMap;

 JavaUtilCollectionsDeserializers.typeFactory = new TypeFactory();
 }

 @Before
 public void init() {
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(JavaUtilCollectionsDeserializers.class);
 JsonNode node = new ObjectNode(mapper.getFactory());

 try {
 listType = reader.readValueAsValue(node.set("type", 7).set("value", new TypeReference<List<Integer>>(){}.getType()));
 setType = reader.readValueAsValue(node.set("type", 1).set("value", new TypeReference<Set<Integer>>(){}.getType()));
 mapType = reader.readValueAsValue(node.set("type", 3).set("value", new TypeReference<Map<String, Integer>>(){}.getType()));
 unmodifiableListType = reader.readValueAsValue(node.set("type", 5).set("value", new TypeReference<List<Integer>>(){}.getType()));
 unmodifiableSetType = reader.readValueAsValue(node.set("type", 6).set("value", new TypeReference<Set<Integer>>(){}.getType()));
 unmodifiableMapType = reader.readValueAsValue(node.set("type", 6).set("value", new TypeReference<Map<String, Integer>>(){}.getType()));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testFindForCollection_asList() throws JsonMappingException {
 List<Integer> expected = Arrays.asList(1, 2, 3);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<List<Integer>>(){}.getType());
 assertEquals(expected.getClass(), deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Integer> actual = deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(List.class)));
 assertEquals(expected, actual);
 }

 @Test
 public void testFindForCollection_singletonList() throws JsonMappingException {
 List<Integer> expected = new ArrayList<>();
 expected.add(1);
 Set<List<Integer>> inputSet = new HashSet<>();
 inputSet.add(expected);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<Set<List<Integer>>>() {}.getType());
 assertEquals(Set.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 Set<List<Integer>> actual = (Set<List<Integer>>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(Set.class)));
 assertEquals(inputSet, actual);
 List<Integer> actualList = actual.iterator().next();
 assertEquals(expected, actualList);
 }

 @Test
 public void testFindForCollection_singletonSet() throws JsonMappingException {
 Set<Integer> expected = new HashSet<>();
 expected.add(1);
 List<Set<Integer>> inputList = new ArrayList<>();
 inputList.add(expected);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<List<Set<Integer>>>() {}.getType());
 assertEquals(List.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Set<Integer>> actual = (List<Set<Integer>>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(List.class)));
 assertEquals(inputList, actual);
 Set<Integer> actualSet = actual.get(0);
 assertEquals(expected, actualSet);
 }

 @Test
 public void testFindForCollection_singletonMap() throws JsonMappingException {
 Map<String, Integer> expected = new HashMap<>();
 expected.put("a", 1);
 List<Map<String, Integer>> inputList = new ArrayList<>();
 inputList.add(expected);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<List<Map<String, Integer>>>() {}.getType());
 assertEquals(List.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Map<String, Integer>> actual = (List<Map<String, Integer>>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(List.class)));
 assertEquals(inputList, actual);
 Map<String, Integer> actualMap = actual.get(0);
 assertEquals(expected, actualMap);
 }

 @Test
 public void testFindForCollection_unmodifiableList() throws JsonMappingException {
 List<Integer> expected = Arrays.asList(1, 2, 3);
 List<List<Integer>> inputList = new ArrayList<>();
 inputList.add(Collections.unmodifiableList(expected));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<List<List<Integer>>>() {}.getType());
 assertEquals(List.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<List<Integer>> actual = (List<List<Integer>>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(List.class)));
 assertEquals(inputList, actual);
 List<Integer> actualList = actual.get(0);
 assertEquals(Collections.unmodifiableList(expected), actualList);
 }

 @Test
 public void testFindForCollection_unmodifiableSet() throws JsonMappingException {
 Set<Integer> expected = new HashSet<>();
 expected.add(1);
 Set<Set<Integer>> inputSet = new HashSet<>();
 inputSet.add(Collections.unmodifiableSet(expected));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<Set<Set<Integer>>>() {}.getType());
 assertEquals(Set.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 Set<Set<Integer>> actual = (Set<Set<Integer>>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(Set.class)));
 assertEquals(inputSet, actual);
 Set<Integer> actualSet = actual.iterator().next().iterator().next();
 assertEquals(expected, actualSet);
 }

 @Test
 public void testFindForCollection_unmodifiableMap() throws JsonMappingException {
 Map<String, Integer> expected = new HashMap<>();
 expected.put("a", 1);
 List<Map<String, Integer>> inputList = new ArrayList<>();
 inputList.add(Collections.unmodifiableMap(expected));
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<List<Map<String, Integer>>>() {}.getType());
 assertEquals(List.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Map<String, Integer>> actual = (List<Map<String, Integer>>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(List.class)));
 assertEquals(inputList, actual);
 Map<String, Integer> actualMap = actual.get(0);
 assertEquals(Collections.unmodifiableMap(expected), actualMap);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForCollection_notFound() throws JsonMappingException {
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(null, new TypeReference<Map<String, String>>(){}.getType());
 fail("Should not find deserializer for Map<String, String>");
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_asList() throws JsonMappingException {
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(null, new TypeReference<List<Integer>>(){}.getType());
 fail("Should not find deserializer for List<Integer>");
 }

 @Test
 public void testFindForMap_singletonMap() throws JsonMappingException {
 Map<String, Integer> expected = new HashMap<>();
 expected.put("a", 1);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(null, new TypeReference<Map<String, Integer>>(){}.getType());
 assertEquals(Map.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 Map<String, Integer> actual = (Map<String, Integer>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(Map.class)));
 assertEquals(expected, actual);
 }

 @Test
 public void testFindForMap_unmodifiableMap() throws JsonMappingException {
 Map<String, Integer> expected = new HashMap<>();
 expected.put("a", 1);
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(null, new TypeReference<Map<String, Integer>>(){}.getType());
 assertEquals(Map.class, deserializer.getInputType(JavaUtilCollectionsDeserializers.typeFactory));
 Map<String, Integer> actual = (Map<String, Integer>) deserializer.deserialize(null, null, new StdValueInstantiator(), null, null, new ObjectNode(JavaUtilCollectionsDeserializers.typeFactory.constructType(Map.class)));
 assertEquals(Collections.unmodifiableMap(expected), actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindForMap_notFound() throws JsonMappingException {
 JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(null, new TypeReference<List<Integer>>(){}.getType());
 fail("Should not find deserializer for List<Integer>");
 }

 @Test
 public void testConverter_asList() throws JsonMappingException {
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(7, listType, List.class);
 assertEquals(List.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Integer> actual = (List<Integer>) converter.convert(Arrays.asList(1, 2, 3));
 assertEquals(Arrays.asList(1, 2, 3), actual);
 }

 @Test
 public void testConverter_singletonList() throws JsonMappingException {
 List<Integer> inputList = Arrays.asList(1);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(5, unmodifiableListType, List.class);
 assertEquals(List.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Integer> actual = (List<Integer>) converter.convert(inputList);
 assertEquals(Collections.unmodifiableList(inputList), actual);
 }

 @Test
 public void testConverter_singletonSet() throws JsonMappingException {
 Set<Integer> inputSet = new HashSet<>();
 inputSet.add(1);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, setType, Set.class);
 assertEquals(Set.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 Set<Integer> actual = (Set<Integer>) converter.convert(inputSet);
 assertEquals(Collections.unmodifiableSet(inputSet), actual);
 }

 @Test
 public void testConverter_singletonMap() throws JsonMappingException {
 Map<String, Integer> inputMap = new HashMap<>();
 inputMap.put("a", 1);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(3, mapType, Map.class);
 assertEquals(Map.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 Map<String, Integer> actual = (Map<String, Integer>) converter.convert(inputMap);
 assertEquals(Collections.unmodifiableMap(inputMap), actual);
 }

 @Test
 public void testConverter_unmodifiableList() throws JsonMappingException {
 List<Integer> inputList = Arrays.asList(1, 2, 3);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(5, unmodifiableListType, List.class);
 assertEquals(List.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 List<Integer> actual = (List<Integer>) converter.convert(Collections.unmodifiableList(inputList));
 assertEquals(Collections.unmodifiableList(inputList), actual);
 }

 @Test
 public void testConverter_unmodifiableSet() throws JsonMappingException {
 Set<Integer> inputSet = new HashSet<>();
 inputSet.add(1);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(6, unmodifiableSetType, Set.class);
 assertEquals(Set.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 Set<Integer> actual = (Set<Integer>) converter.convert(Collections.unmodifiableSet(inputSet));
 assertEquals(Collections.unmodifiableSet(inputSet), actual);
 }

 @Test
 public void testConverter_unmodifiableMap() throws JsonMappingException {
 Map<String, Integer> inputMap = new HashMap<>();
 inputMap.put("a", 1);
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(6, unmodifiableMapType, Map.class);
 assertEquals(Map.class, converter.getOutputType(JavaUtilCollectionsDeserializers.typeFactory));
 Map<String, Integer> actual = (Map<String, Integer>) converter.convert(Collections.unmodifiableMap(inputMap));
 assertEquals(Collections.unmodifiableMap(inputMap), actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConverter_invalidType() throws JsonMappingException {
 JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, setType, Integer.class);
 fail("Should not convert Set<Integer> to Integer");
 }
}