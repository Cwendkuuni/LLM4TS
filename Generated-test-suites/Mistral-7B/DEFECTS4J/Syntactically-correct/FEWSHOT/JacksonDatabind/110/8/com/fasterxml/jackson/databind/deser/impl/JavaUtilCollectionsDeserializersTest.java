package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonSet() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertEquals(Set.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionSingletonList() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertEquals(List.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionSingletonMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertEquals(Map.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertEquals(JsonDeserializer.None.class, deserializer.getTargetType(null));
        assertNotNull(deserializer);
        
        Set<?> set = Collections.singleton(Boolean.TRUE);
        JsonNode node = mapper.valueToTree(set);
        Object deserialized = mapper.readValue(node.traverse(), javaType);
        assertNotNull(deserialized);
        assertEquals(set, deserialized);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertEquals(JsonDeserializer.None.class, deserializer.getTargetType(null));
        assertNotNull(deserializer);

        List<?> list = Collections.singletonList(Boolean.TRUE);
        JsonNode node = mapper.valueToTree(list);
        Object deserialized = mapper.readValue(node.traverse(), javaType);
        assertNotNull(deserialized);
        assertEquals(list, deserialized);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertEquals(JsonDeserializer.None.class, deserializer.getTargetType(null));
        assertNotNull(deserializer);

        Map<?,?> map = Collections.singletonMap("a", "b");
        JsonNode node = mapper.valueToTree(map);
        Object deserialized = mapper.readValue(node.traverse(), javaType);
        assertNotNull(deserialized);
        assertEquals(map, deserialized);
    }

    @Test
    public void testFindForMapSingletonMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertEquals(Map.class, deserializer.getTargetType(null));
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);
        assertEquals(JsonDeserializer.None.class, deserializer.getTargetType(null));
        assertNotNull(deserializer);

        Map<?,?> map = Collections.singletonMap("a", "b");
        JsonNode node = mapper.valueToTree(map);
        Object deserialized = mapper.readValue(node.traverse(), javaType);
        assertNotNull(deserialized);
        assertEquals(map, deserialized);
    }
}