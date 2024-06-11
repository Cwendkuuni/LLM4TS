package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollectionSingletonSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaType, Set.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);
        Set<Boolean> expected = Collections.singleton(Boolean.TRUE);

        assertEquals(expected, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$SingletonSet\" }", Map.class)).get(0));
    }

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaType, List.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);
        List<Boolean> expected = Collections.singletonList(Boolean.TRUE);

        assertEquals(expected, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$SingletonList\" }", Map.class)).get(0));
    }

    @Test
    public void testFindForCollectionSingletonMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(String.class);
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaType, Map.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);
        Map<String, String> expected = Collections.singletonMap("a", "b");

        assertEquals(expected, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$SingletonMap\" }", Map.class)).get(0));
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        Set<Boolean> set = Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, javaType, Set.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);

        assertEquals(set, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$UnmodifiableSet\" }", Map.class)).get(0));
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
        List<Integer> list = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, javaType, List.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);

        assertEquals(list, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$UnmodifiableList\" }", Map.class)).get(0));
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws Exception {
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(Integer.class);
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, keyType, valueType);
        Map<String, Integer> map = Collections.unmodifiableMap(Map.of("a", 1, "b", 2));
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, javaType, Map.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);

        assertEquals(map, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$UnmodifiableMap\" }", Map.class)).get(0));
    }

    @Test
    public void testFindForCollectionAsList() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
        List<Integer> list = Arrays.asList(1, 2, 3);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaType, List.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);

        assertEquals(list, deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$ArrayList\" }", Map.class).get(0)));
    }

    @Test
    public void testEmptyCollections() throws Exception {
        JavaType javaType = mapper.getTypeFactory().constructType(List.class, Integer.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, javaType, List.class);
        StdDelegatingDeserializer<Object> deserializer = new StdDelegatingDeserializer<Object>(converter);

        assertNull(deserializer.deserialize(mapper, mapper.readValue("{ \"class\": \"java.util.Collections$emptyList\" }", Map.class).get(0)));
    }
}