package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link JavaUtilCollectionsDeserializers}.
 */
public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\": \"java.util.Collections$SingletonSet\", \"element\": \"true\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        MapperFeature.DEFAULT_USE_STATIC_TYPINGS.disable(mapper);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        assertEquals(Collections.singleton(Boolean.TRUE), readValue);
    }

    @Test
    public void testFindForCollectionSingletonList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\": \"java.util.Collections$SingletonList\", \"element\": 1}";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        MapperFeature.DEFAULT_USE_STATIC_TYPINGS.disable(mapper);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        assertEquals(Collections.singletonList(1), readValue);
    }

    @Test
    public void testFindForCollectionSingletonMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\": \"java.util.Collections$SingletonMap\", \"key\": \"a\", \"value\": \"b\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        MapperFeature.DEFAULT_USE_STATIC_TYPINGS.disable(mapper);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        assertEquals(Collections.singletonMap("a", "b"), readValue);
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[true]";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        assertEquals(Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE)), readValue);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<List<Integer>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        assertEquals(Collections.unmodifiableList(Arrays.asList(1)), readValue);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"a\": \"b\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        assertEquals(Collections.unmodifiableMap(new HashMap<String, String>() {{ put("a", "b"); }}), readValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollectionInvalidCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2]";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        // This should throw an exception as the list has more than one element
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollectionInvalidMapKey() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\": \"b\"}";
        JavaType javaType = mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {});
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        Object readValue = mapper.readValue(json, Object.class, deserializer);
        // This should throw an exception as the key is not valid
    }
}