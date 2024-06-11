package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class);
        Set<Boolean> set = new HashSet<>(Collections.singleton(Boolean.TRUE));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(set);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(set, deserialized);
    }

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        List<Boolean> list = new ArrayList<>(Collections.singletonList(Boolean.TRUE));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(list, deserialized);
    }

    @Test
    public void testFindForCollectionSingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = new HashMap<>(Collections.singletonMap("a", "b"));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(map, deserialized);
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Set.class);
        Set<Boolean> set = Collections.unmodifiableSet(new HashSet<>(Collections.singleton(Boolean.TRUE)));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(set);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(set, deserialized);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        List<Boolean> list = Collections.unmodifiableList(new ArrayList<>(Collections.singletonList(Boolean.TRUE)));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(list, deserialized);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = Collections.unmodifiableMap(new HashMap<>(Collections.singletonMap("a", "b")));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(map, deserialized);
    }

    @Test
    public void testFindForMapSingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = new HashMap<>(Collections.singletonMap("a", "b"));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(map, deserialized);
    }

    @Test
    public void testFindForMapUnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Map.class, String.class, String.class);
        Map<String, String> map = Collections.unmodifiableMap(new HashMap<>(Collections.singletonMap("a", "b")));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(map, deserialized);
    }

    @Test
    public void testFindForCollectionAsList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(List.class);
        List<Integer> list = Arrays.asList(1, 2, 3);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Assert.assertNotNull(deserializer);

        Object deserialized = mapper.readValue(json, javaType.withoutGenericTypes());
        Assert.assertEquals(list, deserialized);
    }
}