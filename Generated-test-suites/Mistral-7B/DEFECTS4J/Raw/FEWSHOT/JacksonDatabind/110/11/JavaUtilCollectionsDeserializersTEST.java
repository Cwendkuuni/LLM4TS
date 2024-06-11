package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}.getType());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

        Object result = mapper.readValue("[]", Object.class);
        assertEquals(deserializer.getClass(), result.getClass());
        List<String> deserializedList = mapper.readValue("[]", new TypeReference<List<String>>() {}.getType());
        assertEquals(deserializer, deserializedList.get(0).getClass().getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<String>>() {}.getType());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

        Object result = mapper.readValue("{}", Object.class);
        assertEquals(deserializer.getClass(), result.getClass());
        Set<String> deserializedSet = mapper.readValue("{}", new TypeReference<Set<String>>() {}.getType());
        assertEquals(deserializer, deserializedSet.iterator().next().getClass().getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList("foo", "bar")).getClass());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

        Object result = mapper.readValue("[]", Object.class);
        assertEquals(deserializer.getClass(), result.getClass());
        List<String> deserializedList = mapper.readValue("[\"" + "foo" + "\",\"" + "bar" + "\"]", new TypeReference<List<String>>() {}.getType());
        assertEquals(deserializer, deserializedList.getClass().getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList("foo")).getClass());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);

        Object result = mapper.readValue("{}", Object.class);
        assertEquals(deserializer.getClass(), result.getClass());
        Set<String> deserializedSet = mapper.readValue("[\"" + "foo" + "\"]", new TypeReference<Set<String>>() {}.getType());
        assertEquals(deserializer, deserializedSet.getClass().getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);

        Object result = mapper.readValue("{}", Object.class);
        assertEquals(deserializer.getClass(), result.getClass());
        Map<String, String> deserializedMap = mapper.readValue("{\"key\":\"value\"}", new TypeReference<Map<String, String>>() {}.getType());
        assertEquals(deserializer, deserializedMap.getClass().getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Map.of("key", "value")).getClass());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);

        Object result = mapper.readValue("{}", Object.class);
        assertEquals(deserializer.getClass(), result.getClass());
        Map<String, String> deserializedMap = mapper.readValue(new ObjectNode(mapper.getFactory()).put("key", "value").toString(), new TypeReference<Map<String, String>>() {}.getType());
        assertEquals(deserializer, deserializedMap.getClass().getAnnotation(JsonDeserializer.class));
    }
}