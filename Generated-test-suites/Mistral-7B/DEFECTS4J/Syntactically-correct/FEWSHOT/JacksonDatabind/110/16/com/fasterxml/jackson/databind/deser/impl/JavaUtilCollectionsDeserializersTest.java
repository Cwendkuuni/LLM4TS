package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueContainerDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper;

    @org.junit.BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeList);
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(List.class, Boolean.TYPE), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_LIST, javaTypeList, List.class).getInstance(null));
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaTypeSet);
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(Set.class, Boolean.TYPE), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, javaTypeSet, Set.class).getInstance(null));
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), JavaType.constructFromRawType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass()));
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(List.class, Boolean.TYPE), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_LIST, javaTypeList, List.class).getInstance(null));
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), JavaType.constructFromRawType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass()));
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(Set.class, Boolean.TYPE), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_SET, javaTypeSet, Set.class).getInstance(null));
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaTypeMap);
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(Map.class, String.class, String.class), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_MAP, javaTypeMap, Map.class).getInstance(null));
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), JavaType.constructFromRawType(Collections.unmodifiableMap(Collections.singletonMap("key", "value")).getClass()));
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(Map.class, String.class, String.class), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_MAP, javaTypeMap, Map.class).getInstance(null));
    }

    @Test
    public void testFindForMap_AlternateImplType() throws JsonMappingException {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String[].class, String[].class);
        JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), JavaType.constructFromRawType(Collections.unmodifiableMap(Collections.singletonMap(new String[]{"key"}, new String[]{"value"})).getClass()));
        StdDelegatingDeserializer deserializer = (StdDelegatingDeserializer) mapper.getDeserializationConfig().findValueDeserializer(
                new ClassKey(Map.class, String[].class, String[].class), null, true);
        assertSame(deserializer.getDelegate(), JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_MAP, javaTypeMap, Map.class).getInstance(null));
    }

    @Test
    public void testConverter_SingletonList() {
        JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_LIST, null, List.class).getInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonList_WrongSize() {
        JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_LIST, null, List.class).getInstance(Collections.singletonList(Boolean.TRUE, Boolean.TRUE));
    }

    @Test
    public void testConverter_SingletonSet() {
        JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, null, Set.class).getInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonSet_WrongSize() {
        JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_SET, null, Set.class).getInstance(Collections.singleton(Boolean.TRUE, Boolean.TRUE));
    }

    @Test
    public void testConverter_SingletonMap() {
        JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_MAP, null, Map.class).getInstance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConverter_SingletonMap_WrongSize() {
        JavaUtilCollectionsDeserializers.converter(TYPE_SINGLETON_MAP, null, Map.class).getInstance(Collections.singletonMap("key1", "value1", "key2", "value2"));
    }

    @Test
    public void testConverter_UnmodifiableList() {
        JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_LIST, null, List.class).getInstance(null);
    }

    @Test
    public void testConverter_UnmodifiableSet() {
        JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_SET, null, Set.class).getInstance(null);
    }

    @Test
    public void testConverter_UnmodifiableMap() {
        JavaUtilCollectionsDeserializers.converter(TYPE_UNMODIFIABLE_MAP, null, Map.class).getInstance(null);
    }

    @Test
    public void testConverter_AsList() {
        JavaUtilCollectionsDeserializers.converter(TYPE_AS_LIST, null, List.class).getInstance(null);
    }

    @Test
    public void testDeserialize_SingletonList() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[true]");

        List<Boolean> list = mapper.readValue(node.traverse(), JavaType.constructType(List.class, Boolean.TYPE));
        assertEquals(1, list.size());
        assertEquals(true, list.get(0));
    }

    @Test
    public void testDeserialize_SingletonSet() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[true]");

        Set<Boolean> set = mapper.readValue(node.traverse(), JavaType.constructType(Set.class, Boolean.TYPE));
        assertEquals(1, set.size());
        assertEquals(true, set.iterator().next());
    }

    @Test
    public void testDeserialize_SingletonMap() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "{\"key\":\"value\"}");

        Map<String, String> map = mapper.readValue(node.traverse(), JavaType.constructType(Map.class, String.class, String.class));
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));
    }

    @Test
    public void testDeserialize_UnmodifiableList() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[true]");

        List<Boolean> list = mapper.readValue(node.traverse(), JavaType.constructType(List.class, Boolean.TYPE));
        assertEquals(1, list.size());
        assertEquals(true, list.get(0));

        assertInstanceOf(UnmodifiableList.class, list);
    }

    @Test
    public void testDeserialize_UnmodifiableSet() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[true]");

        Set<Boolean> set = mapper.readValue(node.traverse(), JavaType.constructType(Set.class, Boolean.TYPE));
        assertEquals(1, set.size());
        assertEquals(true, set.iterator().next());

        assertInstanceOf(UnmodifiableSet.class, set);
    }

    @Test
    public void testDeserialize_UnmodifiableMap() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "{\"key\":\"value\"}");

        Map<String, String> map = mapper.readValue(node.traverse(), JavaType.constructType(Map.class, String.class, String.class));
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));

        assertInstanceOf(UnmodifiableMap.class, map);
    }

    @Test
    public void testDeserialize_ListWithEmptyCollectionFactory() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[true]");

        List<Boolean> list = mapper.readValue(node.traverse(), JavaType.constructType(List.class, Boolean.TYPE), new StdValueContainerDeserializer.EmptyCreator<>());
        assertEquals(1, list.size());
        assertEquals(true, list.get(0));

        assertInstanceOf(ArrayList.class, list);
    }

    @Test
    public void testDeserialize_SetWithEmptyCollectionFactory() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[true]");

        Set<Boolean> set = mapper.readValue(node.traverse(), JavaType.constructType(Set.class, Boolean.TYPE), new StdValueContainerDeserializer.EmptyCreator<>());
        assertEquals(1, set.size());
        assertEquals(true, set.iterator().next());

        assertInstanceOf(HashSet.class, set);
    }

    @Test
    public void testDeserialize_MapWithEmptyCollectionFactory() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "{\"key\":\"value\"}");

        Map<String, String> map = mapper.readValue(node.traverse(), JavaType.constructType(Map.class, String.class, String.class), new StdValueContainerDeserializer.EmptyCreator<>());
        assertEquals(1, map.size());
        assertEquals("value", map.get("key"));

        assertInstanceOf(HashMap.class, map);
    }

    @Test
    public void testDeserialize_EmptyList() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[]");

        List<Boolean> list = mapper.readValue(node.traverse(), JavaType.constructType(List.class, Boolean.TYPE));
        assertEquals(0, list.size());
        assertNull(list.get(0));

        assertInstanceOf(ArrayList.class, list);
    }

    @Test
    public void testDeserialize_EmptySet() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "[]");

        Set<Boolean> set = mapper.readValue(node.traverse(), JavaType.constructType(Set.class, Boolean.TYPE));
        assertEquals(0, set.size());
        assertNull(set.iterator().next());

        assertInstanceOf(HashSet.class, set);
    }

    @Test
    public void testDeserialize_EmptyMap() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = mapper.createObjectNode();
        node.put("value", "{}");

        Map<String, String> map = mapper.readValue(node.traverse(), JavaType.constructType(Map.class, String.class, String.class));
        assertEquals(0, map.size());
        assertNull(map.get("key"));

        assertInstanceOf(HashMap.class, map);
    }
}