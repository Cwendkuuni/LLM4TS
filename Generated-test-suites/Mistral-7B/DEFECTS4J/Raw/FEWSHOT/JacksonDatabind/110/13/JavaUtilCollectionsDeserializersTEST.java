package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        assertEquals(List.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionSingletonSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<Set<Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        assertEquals(Set.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(1, 2)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableType);
        assertEquals(List.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<Set<Integer>>() {}.getType());
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList(1, 2)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unmodifiableType);
        assertEquals(Set.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionAsList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        assertEquals(List.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForMapSingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(Integer.class);
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>() {}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type);
        assertEquals(Map.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType keyType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(Integer.class);
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Integer>>() {}.getType());
        JavaType unmodifiableType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Collections.singletonMap("key", 1)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unmodifiableType);
        assertEquals(Map.class, deserializer.getTargetType(null));
    }

    @Test
    public void testStdDelegatingDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {}.getType());
        JsonDeserializer<?> collectionDeserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type);
        JsonDeserializer<?> mapDeserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), mapper.getTypeFactory().constructType(new TypeReference<Map<String,Integer>>() {}.getType()));

        JsonDeserializer<?> deserializer = new StdDelegatingDeserializer<>(collectionDeserializer);
        Object input = mapper.readValue("[]", type);
        Object output = deserializer.deserialize(mapper.getDeserializationContext(), input);
        assertEquals(input, output);

        deserializer = new StdDelegatingDeserializer<>(mapDeserializer);
        input = mapper.readValue("{}", new TypeReference<Map<String,Integer>>() {}.getType());
        output = deserializer.deserialize(mapper.getDeserializationContext(), input);
        assertEquals(input, output);
    }

    @Test
    public void testFindForNullType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(null);
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type));
        assertNull(JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type));
    }

    @Test
    public void testFindForInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(int[].class);
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), type));
        assertNull(JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), type));
    }

}