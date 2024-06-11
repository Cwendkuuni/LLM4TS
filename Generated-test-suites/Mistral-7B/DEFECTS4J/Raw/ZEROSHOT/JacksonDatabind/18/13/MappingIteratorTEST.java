package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    @Test
    public void testMappingIterator() throws IOException {
        JavaType javaType = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser = createMockJsonParser();
        DeserializationContext deserializationContext = createMockDeserializationContext();
        JsonDeserializer<String> jsonDeserializer = createMockJsonDeserializer();

        ObjectMapper objectMapper = new ObjectMapper();

        MappingIterator<String> mappingIterator = objectMapper.readValues(jsonParser, javaType, deserializationContext, jsonDeserializer);

        List<String> expectedList = new ArrayList<>();
        expectedList.add("test1");
        expectedList.add("test2");
        expectedList.add("test3");

        List<String> actualList = mappingIterator.readAll();

        Assert.assertEquals(expectedList, actualList);

        mappingIterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() throws IOException {
        JavaType javaType = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser = createMockJsonParser();
        DeserializationContext deserializationContext = createMockDeserializationContext();
        JsonDeserializer<String> jsonDeserializer = createMockJsonDeserializer();

        ObjectMapper objectMapper = new ObjectMapper();

        MappingIterator<String> mappingIterator = objectMapper.readValues(jsonParser, javaType, deserializationContext, jsonDeserializer);

        mappingIterator.next();
        mappingIterator.next();

        mappingIterator.remove();

        mappingIterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        JavaType javaType = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser = createMockJsonParser();
        DeserializationContext deserializationContext = createMockDeserializationContext();
        JsonDeserializer<String> jsonDeserializer = createMockJsonDeserializer();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonMappingException jsonMappingException = new JsonMappingException("Test error");

        doThrow(jsonMappingException).when(jsonDeserializer).deserialize(jsonParser, deserializationContext);

        MappingIterator<String> mappingIterator = objectMapper.readValues(jsonParser, javaType, deserializationContext, jsonDeserializer);

        mappingIterator.next();
    }

    @Test(expected = RuntimeException.class)
    public void testIOException() throws IOException {
        JavaType javaType = new TypeFactory().constructCollectionType(List.class, String.class);
        JsonParser jsonParser = createMockJsonParser();
        DeserializationContext deserializationContext = createMockDeserializationContext();
        JsonDeserializer<String> jsonDeserializer = createMockJsonDeserializer();

        ObjectMapper objectMapper = new ObjectMapper();

        doThrow(new IOException("Test error")).when(jsonParser).nextToken();

        MappingIterator<String> mappingIterator = objectMapper.readValues(jsonParser, javaType, deserializationContext, jsonDeserializer);

        mappingIterator.next();
    }

    private JsonParser createMockJsonParser() {
        return mock(JsonParser.class);
    }

    private DeserializationContext createMockDeserializationContext() {
        return mock(DeserializationContext.class);
    }

    private JsonDeserializer<String> createMockJsonDeserializer() {
        return mock(JsonDeserializer.class);
    }
}