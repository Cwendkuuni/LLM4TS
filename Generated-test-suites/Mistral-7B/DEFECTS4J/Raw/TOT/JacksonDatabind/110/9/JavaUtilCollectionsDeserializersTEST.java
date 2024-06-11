package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType setType = mapper.getTypeFactory().constructType(Collections.class, Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), setType);
        Object deserialized = mapper.readValue("{\"Class\":\"java.util.Collections$SingletonSet\", \"elements\":[true]}", new TypeReference<List<Object>>() {});
        assertEquals(Collections.singleton(Boolean.TRUE), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType listType = mapper.getTypeFactory().constructType(Collections.class, List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), listType);
        Object deserialized = mapper.readValue("{\"Class\":\"java.util.Collections$SingletonList\", \"element\":true}", new TypeReference<List<Object>>() {});
        assertEquals(Collections.singletonList(Boolean.TRUE), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForCollection_SingletonMap() throws Exception {
        JavaType mapType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), mapType);
        Object deserialized = mapper.readValue("{\"Class\":\"java.util.Collections$SingletonMap\", \"key\":\"a\", \"value\":\"b\"}", new TypeReference<Map<String, String>>() {});
        assertEquals(Collections.singletonMap("a", "b"), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType setType = mapper.getTypeFactory().constructType(Collections.class, Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), setType);
        Object deserialized = Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE));
        assertEquals(Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE)), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType listType = mapper.getTypeFactory().constructType(Collections.class, List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), listType);
        Object deserialized = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));
        assertEquals(Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        JavaType mapType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), mapType);
        Object deserialized = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap("a", "b")), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType mapType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(new StdDeserializationContext(), mapType);
        Object deserialized = mapper.readValue("{\"Class\":\"java.util.Collections$SingletonMap\", \"entry\":{\"key\":\"a\", \"value\":\"b\"}}", new TypeReference<Map<String, String>>() {});
        assertEquals(Collections.singletonMap("a", "b"), deserializer.deserialize(deserialized, null));
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType mapType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(new StdDeserializationContext(), mapType);
        Object deserialized = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap("a", "b")), deserializer.deserialize(deserialized, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws Exception {
        JavaType invalidType = mapper.getTypeFactory().constructType(Object.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(new StdDeserializationContext(), invalidType);
        Object deserialized = mapper.readValue("{\"Class\":\"java.util.Collections$SingletonList\"}", new TypeReference<List<Object>>() {});
        deserializer.deserialize(deserialized, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_InvalidType() throws Exception {
        JavaType invalidType = mapper.getTypeFactory().constructType(Object.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(new StdDeserializationContext(), invalidType);
        Object deserialized = mapper.readValue("{\"Class\":\"java.util.Collections$SingletonMap\"}", new TypeReference<Map<String, Object>>() {});
        deserializer.deserialize(deserialized, null);
    }
}