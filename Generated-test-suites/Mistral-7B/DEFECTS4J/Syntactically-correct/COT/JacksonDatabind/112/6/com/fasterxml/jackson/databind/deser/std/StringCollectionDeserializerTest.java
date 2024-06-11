package com.example.stringcollectiondeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    public StringCollectionDeserializerTest() {
        this.objectMapper = new ObjectMapper();
    }

    // Basic test cases for the constructor methods

    @Test
    public void testStringCollectionDeserializerConstruction() {
        JavaType collectionType = null;
        JsonDeserializer<String> valueDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;

        StringCollectionDeserializer stringCollectionDeserializer1 = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator);
        StringCollectionDeserializer stringCollectionDeserializer2 = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator, delegateDeserializer);
    }

    // Basic test cases for the methods

    @Test
    public void testIsCachable() throws JsonMappingException {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        boolean isCachable = stringCollectionDeserializer.isCachable();
        assertTrue(isCachable);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);

        when(stringCollectionDeserializer.createContextual(context, property)).thenReturn(valueDeserializer);

        JsonDeserializer<String> result = stringCollectionDeserializer.createContextual(context, property);

        assertEquals(valueDeserializer, result);
    }

    @Test
    public void testGetContentDeserializer() {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<Object> result = stringCollectionDeserializer.getContentDeserializer();

        assertNotNull(result);
    }

    @Test
    public void testgetValueInstantiator() {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        ValueInstantiator result = stringCollectionDeserializer.getValueInstantiator();

        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextTextValue()).thenReturn("test1", "test2", null);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, context);

        List<String> expectedResult = new ArrayList<>(Arrays.asList("test1", "test2"));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.nextTextValue()).thenReturn("test1", "test2", null);
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn("test3", "test4", null);

        Collection<String> result = stringCollectionDeserializer.deserializeUsingCustom(jsonParser, context, new ArrayList<>(), valueDeserializer);

        List<String> expectedResult = new ArrayList<>(Arrays.asList("test1", "test2", "test3", "test4"));
        assertEquals(expectedResult, result);
    }

    // Test cases for edge cases and exception handling

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidToken() throws IOException {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        stringCollectionDeserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValueProvider() throws IOException {
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.nextTextValue()).thenReturn("test");

        stringCollectionDeserializer.setNullProvider(null);

        stringCollectionDeserializer.deserialize(jsonParser, context);
    }
}