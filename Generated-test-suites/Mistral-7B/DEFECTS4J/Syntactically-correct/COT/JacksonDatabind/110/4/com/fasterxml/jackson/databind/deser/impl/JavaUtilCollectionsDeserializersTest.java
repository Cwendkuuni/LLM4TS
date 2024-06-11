package com.example.junit; // replace with your test package if needed

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), javaType);

        List<Integer> expected = Arrays.asList(1, 2, 3);
        byte[] input = objectMapper.writeValueAsBytes(expected);

        List<Integer> actual = deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<String>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), javaType);

        Set<String> expected = Collections.singleton("test");
        byte[] input = objectMapper.writeValueAsBytes(expected);

        Set<String> actual = deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Double>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), javaType);

        List<Double> expected = Arrays.asList(1.0, 2.0, 3.0);
        byte[] input = objectMapper.writeValueAsBytes(Collections.unmodifiableList(expected));

        List<Double> actual = deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Set<Integer>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), javaType);

        Set<Integer> expected = Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().map(i -> i * 2).collect(Collectors.toSet()));
        byte[] input = objectMapper.writeValueAsBytes(expected);

        Set<Integer> actual = deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindForCollection_NullInput() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<String>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(createContext(), javaType);

        byte[] input = null;

        try {
            deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);
            fail("Should have thrown JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<Integer, String>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(createContext(), javaType);

        Map<Integer, String> expected = Collections.singletonMap(1, "test");
        byte[] input = objectMapper.writeValueAsBytes(expected);

        Map<Integer, String> actual = deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String, Double>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(createContext(), javaType);

        Map<String, Double> expected = Collections.unmodifiableMap(Arrays.asList(new AbstractMap.SimpleEntry<String, Double>("test", 1.0), new AbstractMap.SimpleEntry<String, Double>("test2", 2.0)).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        byte[] input = objectMapper.writeValueAsBytes(expected);

        Map<String, Double> actual = deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindForMap_NullInput() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<Map<String, String>>(){}.getType());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(createContext(), javaType);

        byte[] input = null;

        try {
            deserializer.deserialize(objectMapper.readTree(new ByteArrayInputStream(input)), null, null);
            fail("Should have thrown JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private DeserializationContext createContext() {
        return objectMapper.getDeserializationContext();
    }
}