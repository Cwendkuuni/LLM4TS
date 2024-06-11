package com.fasterxml.jackson.databind;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MappingIteratorTest {

    private MappingIterator<Object> mappingIterator;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<Object> jsonDeserializer;

    @Before
    public void setUp() {
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        jsonDeserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testHasNextValueTrue() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        Assert.assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValueFalse() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        Assert.assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValueWithoutUpdatedValue() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING);
        Object value = new Object();
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        Assert.assertEquals(value, mappingIterator.nextValue());
    }

    @Test
    public void testNextValueWithUpdatedValue() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING);
        Object value = new Object();
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext, value)).thenReturn(value);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, value);
        Assert.assertEquals(value, mappingIterator.nextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValueThrowsNoSuchElementException() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        mappingIterator.nextValue();
    }

    @Test
    public void testReadAll() throws IOException {
        Object value1 = new Object();
        Object value2 = new Object();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value1, value2);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        List<Object> resultList = mappingIterator.readAll();
        Assert.assertEquals(Arrays.asList(value1, value2), resultList);
    }

    @Test
    public void testReadAllWithExistingList() throws IOException {
        Object value1 = new Object();
        Object value2 = new Object();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value1, value2);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        List<Object> resultList = new ArrayList<>();
        List<Object> finalList = mappingIterator.readAll(resultList);
        Assert.assertSame(resultList, finalList);
        Assert.assertEquals(Arrays.asList(value1, value2), finalList);
    }

    @Test
    public void testReadAllWithEmptyList() throws IOException {
        Object value1 = new Object();
        Object value2 = new Object();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value1, value2);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        List<Object> resultList = Collections.emptyList();
        List<Object> finalList = mappingIterator.readAll(resultList);
        Assert.assertNotSame(resultList, finalList);
        Assert.assertEquals(Arrays.asList(value1, value2), finalList);
    }

    @Test
    public void testReadAllWithExistingCollection() throws IOException {
        Object value1 = new Object();
        Object value2 = new Object();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value1, value2);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        List<Object> resultList = new ArrayList<>();
        List<Object> finalList = mappingIterator.readAll(resultList);
        Assert.assertSame(resultList, finalList);
        Assert.assertEquals(Arrays.asList(value1, value2), finalList);
    }

    @Test
    public void testReadAllWithExistingCollectionEmpty() throws IOException {
        Object value1 = new Object();
        Object value2 = new Object();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value1, value2);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        List<Object> resultList = new ArrayList<>();
        List<Object> finalList = mappingIterator.readAll(Collections.emptyList());
        Assert.assertNotSame(resultList, finalList);
        Assert.assertEquals(Arrays.asList(value1, value2), finalList);
    }

    @Test
    public void testGetParser() {
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        Assert.assertSame(jsonParser, mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        FormatSchema formatSchema = mock(FormatSchema.class);
        when(jsonParser.getSchema()).thenReturn(formatSchema);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        Assert.assertSame(formatSchema, mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation jsonLocation = mock(JsonLocation.class);
        when(jsonParser.getCurrentLocation()).thenReturn(jsonLocation);
        mappingIterator = new MappingIterator<>(null, jsonParser, deserializationContext, jsonDeserializer, true, null);
        Assert.assertSame(jsonLocation, mappingIterator.getCurrentLocation());
    }
}