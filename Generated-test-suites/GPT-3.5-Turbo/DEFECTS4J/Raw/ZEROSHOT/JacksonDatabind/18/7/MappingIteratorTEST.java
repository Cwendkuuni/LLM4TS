package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MappingIteratorTest {

    private MappingIterator<String> mappingIterator;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonDeserializer<String> jsonDeserializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mappingIterator = new MappingIterator<>("java.lang.String", jsonParser, deserializationContext, jsonDeserializer, true, null);
    }

    @Test
    public void testHasNextWhenParserIsNull() {
        mappingIterator._parser = null;
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextWhenHasNextCheckedIsFalse() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(null);
        assertTrue(mappingIterator.hasNext());
        assertTrue(mappingIterator._hasNextChecked);
        verify(jsonParser).getCurrentToken();
    }

    @Test
    public void testHasNextWhenHasNextCheckedIsTrue() throws IOException {
        mappingIterator._hasNextChecked = true;
        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator._hasNextChecked);
        verify(jsonParser, never()).getCurrentToken();
    }

    @Test
    public void testNextValueWhenHasNextCheckedIsFalse() throws IOException {
        mappingIterator._hasNextChecked = false;
        mappingIterator._parser = null;
        assertNull(mappingIterator.nextValue());
        assertFalse(mappingIterator._hasNextChecked);
    }

    @Test
    public void testNextValueWhenParserIsNull() throws IOException {
        mappingIterator._parser = null;
        assertNull(mappingIterator.nextValue());
        assertFalse(mappingIterator._hasNextChecked);
    }

    @Test
    public void testNextValueWhenUpdatedValueIsNull() throws IOException {
        String value = "test";
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value);
        assertEquals(value, mappingIterator.nextValue());
        assertFalse(mappingIterator._hasNextChecked);
        verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
        verify(jsonDeserializer, never()).deserialize(jsonParser, deserializationContext, null);
    }

    @Test
    public void testNextValueWhenUpdatedValueIsNotNull() throws IOException {
        String value = "test";
        mappingIterator._updatedValue = value;
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext, value)).thenReturn(value);
        assertEquals(value, mappingIterator.nextValue());
        assertFalse(mappingIterator._hasNextChecked);
        verify(jsonDeserializer, never()).deserialize(jsonParser, deserializationContext);
        verify(jsonDeserializer).deserialize(jsonParser, deserializationContext, value);
    }

    @Test
    public void testReadAllWhenResultListIsNull() throws IOException {
        List<String> resultList = null;
        mappingIterator._parser = null;
        assertNull(mappingIterator.readAll(resultList));
    }

    @Test
    public void testReadAllWhenHasNextValueIsFalse() throws IOException {
        List<String> resultList = new ArrayList<>();
        when(jsonParser.getCurrentToken()).thenReturn(null);
        assertTrue(mappingIterator.readAll(resultList).isEmpty());
        verify(jsonParser).getCurrentToken();
    }

    @Test
    public void testReadAllWhenHasNextValueIsTrue() throws IOException {
        List<String> resultList = new ArrayList<>();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(null);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(null);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value").thenReturn(null);
        mappingIterator.readAll(resultList);
        assertEquals(1, resultList.size());
        assertEquals("value", resultList.get(0));
        verify(jsonParser, times(3)).getCurrentToken();
        verify(jsonParser, times(2)).nextToken();
        verify(jsonDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testReadAllWhenResultsIsNull() throws IOException {
        Collection<String> results = null;
        mappingIterator._parser = null;
        assertNull(mappingIterator.readAll(results));
    }

    @Test
    public void testReadAllWhenHasNextValueIsFalse2() throws IOException {
        Collection<String> results = new HashSet<>();
        when(jsonParser.getCurrentToken()).thenReturn(null);
        assertTrue(mappingIterator.readAll(results).isEmpty());
        verify(jsonParser).getCurrentToken();
    }

    @Test
    public void testReadAllWhenHasNextValueIsTrue2() throws IOException {
        Collection<String> results = new HashSet<>();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(null);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(null);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value").thenReturn(null);
        mappingIterator.readAll(results);
        assertEquals(1, results.size());
        assertTrue(results.contains("value"));
        verify(jsonParser, times(3)).getCurrentToken();
        verify(jsonParser, times(2)).nextToken();
        verify(jsonDeserializer, times(2)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testGetParser() {
        assertEquals(jsonParser, mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        when(jsonParser.getSchema()).thenReturn(mock(FormatSchema.class));
        assertNotNull(mappingIterator.getParserSchema());
        verify(jsonParser).getSchema();
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = mock(JsonLocation.class);
        when(jsonParser.getCurrentLocation()).thenReturn(location);
        assertEquals(location, mappingIterator.getCurrentLocation());
        verify(jsonParser).getCurrentLocation();
    }
}