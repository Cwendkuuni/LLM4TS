package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonSet() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, setType);
        assertNotNull(deserializer);

        Set<Boolean> set = new HashSet<Boolean>() {{
            add(Boolean.TRUE);
        }};

        Object result = deserializer.deserialize(set, context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindForCollection_SingletonList() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, listType);
        assertNotNull(deserializer);

        List<Boolean> list = Arrays.asList(Boolean.TRUE);

        Object result = deserializer.deserialize(list, context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindForCollection_SingletonMap() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, mapType);
        assertNotNull(deserializer);

        Map<String, String> map = new HashMap<String, String>() {{
            put("key", "value");
        }};

        Object result = deserializer.deserialize(map, context);
        assertEquals("value", result);
    }

    @Test
    public void testFindForCollection_EmptyList() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, listType);
        assertNotNull(deserializer);

        List<Boolean> list = Arrays.asList();

        Object result = deserializer.deserialize(list, context);
        assertNull(result);
    }

    @Test
    public void testFindForCollection_EmptySet() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, setType);
        assertNotNull(deserializer);

        Set<Boolean> set = new HashSet<Boolean>();

        Object result = deserializer.deserialize(set, context);
        assertNull(result);
    }

    @Test
    public void testFindForCollection_EmptyMap() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, mapType);
        assertNotNull(deserializer);

        Map<String, String> map = new HashMap<String, String>();

        Object result = deserializer.deserialize(map, context);
        assertNull(result);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType setType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, Collections.unmodifiableSet(new HashSet<Boolean>() {{
            add(Boolean.TRUE);
        }}.toArray(new Boolean[0])));
        assertNotNull(deserializer);

        Object result = deserializer.deserialize(deserializer, context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, Collections.unmodifiableList(Arrays.asList(Boolean.TRUE)));
        assertNotNull(deserializer);

        Object result = deserializer.deserialize(deserializer, context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForCollection(context, Collections.unmodifiableMap(new HashMap<String, String>() {{
            put("key", "value");
        }}.toArray(new String[][]{{"key", "value"}})));
        assertNotNull(deserializer);

        Object result = deserializer.deserialize(deserializer, context);
        assertEquals("value", result);
    }

    @Test
    public void testFindForMap_SingletonMap() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForMap(context, mapType);
        assertNotNull(deserializer);

        Map<String, String> map = new HashMap<String, String>() {{
            put("key", "value");
        }};

        Object result = deserializer.deserialize(map, context);
        assertEquals("value", result);
    }

    @Test
    public void testFindForMap_EmptyMap() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForMap(context, new HashMap<String, String>());
        assertNotNull(deserializer);

        Object result = deserializer.deserialize(deserializer, context);
        assertNull(result);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializer(), new TypeFactory());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JavaUtilCollectionsDeserializers deserializers = new JavaUtilCollectionsDeserializers();

        JsonDeserializer<?> deserializer = deserializers.findForMap(context, Collections.unmodifiableMap(new HashMap<String, String>() {{
            put("key", "value");
        }}.toArray(new String[][]{{"key", "value"}})));
        assertNotNull(deserializer);

        Object result = deserializer.deserialize(deserializer, context);
        assertEquals("value", result);
    }
}