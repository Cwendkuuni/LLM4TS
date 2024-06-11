package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        // Test for empty list
        JavaType emptyListType = mapper.getTypeFactory().constructType(List.class);
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), emptyListType);
        assertNotNull(deserializer);

        // Test for singleton list
        Object singletonList = Arrays.asList(new Object());
        JavaType singletonListType = mapper.getTypeFactory().constructType(new TypeReference<List<Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), singletonListType);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Object deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(singletonList)), emptyListType);
        assertEquals(singletonList.get(0), deserialized);

        // Test for unmodifiable list
        List<Object> unmodifiableList = Collections.unmodifiableList(Arrays.asList(new Object()));
        JavaType unmodifiableListType = mapper.getTypeFactory().constructType(new TypeReference<List<Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), unmodifiableListType);
        delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(unmodifiableList)), unmodifiableListType);
        assertEquals(unmodifiableList.get(0), deserialized);

        // Test for other collection types
        Set<Object> singletonSet = Collections.singleton(new Object());
        JavaType singletonSetType = mapper.getTypeFactory().constructType(new TypeReference<Set<Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), singletonSetType);
        delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(singletonSet)), singletonSetType);
        assertEquals(singletonSet.iterator().next(), deserialized);

        Map<String, Object> singletonMap = Collections.singletonMap("key", new Object());
        JavaType singletonMapType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), singletonMapType);
        delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(singletonMap)), singletonMapType);
        assertEquals(singletonMap.get("key"), deserialized);

        Map<String, Object> unmodifiableMap = Collections.unmodifiableMap(Collections.singletonMap("key", new Object()));
        JavaType unmodifiableMapType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), unmodifiableMapType);
        delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(unmodifiableMap)), unmodifiableMapType);
        assertEquals(unmodifiableMap.get("key"), deserialized);

        // Test for null input
        JavaType nullType = mapper.getTypeFactory().constructType(Object.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), nullType);
        assertNull(deserializer);

        // Test for unsupported types
        JavaType unsupportedType = mapper.getTypeFactory().constructType(int[].class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(factory.getDeserializationContext(), unsupportedType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        // Test for empty map
        JavaType emptyMapType = mapper.getTypeFactory().constructType(Map.class);
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(factory.getDeserializationContext(), emptyMapType);
        assertNotNull(deserializer);

        // Test for singleton map
        Map<String, Object> singletonMap = Collections.singletonMap("key", new Object());
        JavaType singletonMapType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(factory.getDeserializationContext(), singletonMapType);
        StdDelegatingDeserializer<Map<String, Object>> delegatingDeserializer = (StdDelegatingDeserializer<Map<String, Object>>) deserializer;
        ObjectMapper.SerializedForm deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(singletonMap)), emptyMapType);
        assertEquals(singletonMap, mapper.readValue(mapper.writeValueAsString(deserialized), Map.class));

        // Test for unmodifiable map
        Map<String, Object> unmodifiableMap = Collections.unmodifiableMap(singletonMap);
        JavaType unmodifiableMapType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(factory.getDeserializationContext(), unmodifiableMapType);
        delegatingDeserializer = (StdDelegatingDeserializer<Map<String, Object>>) deserializer;
        deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(unmodifiableMap)), unmodifiableMapType);
        assertEquals(unmodifiableMap, mapper.readValue(mapper.writeValueAsString(deserialized), Map.class));

        // Test for other map types
        Map<Integer, Object> map = new HashMap<>();
        map.put(1, new Object());
        JavaType mapType = mapper.getTypeFactory().constructType(new TypeReference<Map<Integer, Object>>() {}.getType());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(factory.getDeserializationContext(), mapType);
        delegatingDeserializer = (StdDelegatingDeserializer<Map<Integer, Object>>) deserializer;
        deserialized = delegatingDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(map)), mapType);
        assertEquals(map, mapper.readValue(mapper.writeValueAsString(deserialized), Map.class));

        // Test for null input
        JavaType nullType = mapper.getTypeFactory().constructType(Object.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(factory.getDeserializationContext(), nullType);
        assertNull(deserializer);

        // Test for unsupported types
        JavaType unsupportedType = mapper.getTypeFactory().constructType(int[].class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(factory.getDeserializationContext(), unsupportedType);
        assertNull(deserializer);
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null || !expected.getClass().equals(actual.getClass())) {
            throw new AssertionError("Expected:<" + expected + ">, but was:<" + actual + ">");
        }
        assertEquals(expected.toString(), actual.toString());
    }
}