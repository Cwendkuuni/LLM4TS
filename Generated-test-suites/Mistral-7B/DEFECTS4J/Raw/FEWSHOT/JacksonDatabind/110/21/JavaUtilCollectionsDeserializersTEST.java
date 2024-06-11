package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
        Object input = Arrays.asList("one", "two");

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        List<String> list = (List<String>) output;
        assertEquals(2, list.size());
        assertArrayEquals(new String[]{"one", "two"}, list.toArray());

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Set<String>>() {}.getType());
        Object input = Collections.singleton("one");

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Set<String> set = (Set<String>) output;
        assertEquals(1, set.size());
        assertTrue(set.contains("one"));

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Object input = Collections.singletonMap("one", "oneValue");

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Map<String, String> map = (Map<String, String>) output;
        assertEquals(1, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.get("one").equals("oneValue"));

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForCollection_EmptyList() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
        Object input = Collections.emptyList();

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        List<?> list = output;
        assertEquals(0, list.size());
    }

    @Test
    public void testFindForCollection_EmptySet() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Set<String>>() {}.getType());
        Object input = Collections.emptySet();

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Set<?> set = output;
        assertEquals(0, set.size());
    }

    @Test
    public void testFindForCollection_EmptyMap() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Object input = Collections.emptyMap();

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Map<?,?> map = output;
        assertEquals(0, map.size());
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
        Object input = Collections.unmodifiableList(Arrays.asList("one", "two"));

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        List<String> list = (List<String>) output;
        assertEquals(2, list.size());
        assertArrayEquals(new String[]{"one", "two"}, list.toArray());

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Set<String>>() {}.getType());
        Object input = Collections.unmodifiableSet(Arrays.asList("one"));

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Set<String> set = (Set<String>) output;
        assertEquals(1, set.size());
        assertTrue(set.contains("one"));

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Object input = Collections.unmodifiableMap(Collections.singletonMap("one", "oneValue"));

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Map<String, String> map = (Map<String, String>) output;
        assertEquals(1, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.get("one").equals("oneValue"));

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForCollection_Null() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<List<String>>() {}.getType());
        Object input = null;

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        assertNull(deserializer.deserialize(null, new ObjectFactory(), type, input));
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Object input = Collections.singletonMap("one", "oneValue");

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Map<String, String> map = (Map<String, String>) output;
        assertEquals(1, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.get("one").equals("oneValue"));

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Object input = Collections.unmodifiableMap(Collections.singletonMap("one", "oneValue"));

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        Object output = deserializer.deserialize(null, new ObjectFactory(), type, input);
        Map<String, String> map = (Map<String, String>) output;
        assertEquals(1, map.size());
        assertTrue(map.containsKey("one"));
        assertTrue(map.get("one").equals("oneValue"));

        // Check that the deserializer correctly returns StdDelegatingDeserializer
        StdDelegatingDeserializer stdDelegatingDeserializer = (StdDelegatingDeserializer) deserializer;
        JsonDeserializer<?> delegatedDeserializer = stdDelegatingDeserializer.getDelegatingDeserializer();
        assertTrue(delegatedDeserializer instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testFindForMap_Null() throws Exception {
        JavaType type = new TypeFactory().constructType(new TypeReference<Map<String, String>>() {}.getType());
        Object input = null;

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(createContext(), type);
        assertEquals(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getDefaultValue(), deserializer);

        assertNull(deserializer.deserialize(null, new ObjectFactory(), type, input));
    }
}