package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

        Object deserialized = deserializer.deserialize(objectMapper, new Object(), new TypeReference<List<Boolean>>() {});

        List<Boolean> expected = Arrays.asList(Boolean.TRUE);
        assertEquals(expected, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

        Object deserialized = deserializer.deserialize(objectMapper, new Object(), new TypeReference<Set<Boolean>>() {});

        Set<Boolean> expected = Collections.singleton(Boolean.TRUE);
        assertEquals(expected, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaType unmodifiableListType = TypeFactory.defaultInstance().constructType(List.class, javaType.getRawClass());
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), unmodifiableListType);

        Object deserialized = deserializer.deserialize(objectMapper, new Object(), new TypeReference<List<Boolean>>() {});

        List<Boolean> expected = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
        assertEquals(expected, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaType unmodifiableSetType = TypeFactory.defaultInstance().constructType(Set.class, javaType.getRawClass());
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), unmodifiableSetType);

        Object deserialized = deserializer.deserialize(objectMapper, new Object(), new TypeReference<Set<Boolean>>() {});

        Set<Boolean> expected = Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE));
        assertEquals(expected, deserialized);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), javaType);

        Object deserialized = deserializer.deserialize(objectMapper, new Object(), new TypeReference<Map<String, String>>() {});

        Map<String, String> expected = Collections.singletonMap("a", "b");
        assertEquals(expected, deserialized);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaType unmodifiableMapType = TypeFactory.defaultInstance().constructType(Map.class, javaType.getComponentType(), javaType.getComponentType());
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getContext(), unmodifiableMapType);

        Object deserialized = deserializer.deserialize(objectMapper, new Object(), new TypeReference<Map<String, String>>() {});

        Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        assertEquals(expected, deserialized);
    }

    @Test (expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int.class, Integer.TYPE);
        StdDelegatingDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getContext(), javaType);

        deserializer.deserialize(objectMapper, new Object(), new TypeReference<Object>() {});

        fail("Should have thrown JsonMappingException");
    }
}