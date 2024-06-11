package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test class for JavaUtilCollectionsDeserializers
 */
public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>(){}.getType()));
        List<Boolean> input = Arrays.asList(true);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        List<Boolean> output = mapper.readValue(json, List.class);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object,Object> converter = delegatingDeserializer.getDelegate();
        assertEquals(input, converter.convert(output));
    }

    @Test
    public void testFindForCollection_SingletonSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>(){}.getType()));
        Set<Boolean> input = Collections.singleton(true);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        Set<Boolean> output = mapper.readValue(json, Set.class);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object,Object> converter = delegatingDeserializer.getDelegate();
        assertEquals(input, converter.convert(output));
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>(){}.getType()));
        List<Boolean> input = Collections.unmodifiableList(Arrays.asList(true));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        List<Boolean> output = mapper.readValue(json, List.class);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object,Object> converter = delegatingDeserializer.getDelegate();
        assertEquals(input, converter.convert(output));
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>(){}.getType()));
        Set<Boolean> input = Collections.unmodifiableSet(Collections.singleton(true));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        Set<Boolean> output = mapper.readValue(json, Set.class);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object,Object> converter = delegatingDeserializer.getDelegate();
        assertEquals(input, converter.convert(output));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String,String>>(){}.getType()));
        String json = "[]";
        mapper.readValue(json, Map.class);
        fail("Expected JsonMappingException but no exception was thrown");
    }

    @Test
    public void testFindForMap_SingletonMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String,String>>(){}.getType()));
        Map<String, String> input = Collections.singletonMap("key", "value");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        Map<String, String> output = mapper.readValue(json, Map.class);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object,Object> converter = delegatingDeserializer.getDelegate();
        assertEquals(input, converter.convert(output));
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String,String>>(){}.getType()));
        Map<String, String> input = Collections.unmodifiableMap(Collections.singletonMap("key", "value"));
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        Map<String, String> output = mapper.readValue(json, Map.class);
        StdDelegatingDeserializer<Object> delegatingDeserializer = (StdDelegatingDeserializer<Object>) deserializer;
        Converter<Object,Object> converter = delegatingDeserializer.getDelegate();
        assertEquals(input, converter.convert(output));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMap_InvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>(){}.getType()));
        String json = "{\"key\":\"value\"}";
        mapper.readValue(json, List.class);
        fail("Expected JsonMappingException but no exception was thrown");
    }
}