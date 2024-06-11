package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class MappingIteratorTest {

    @Mock
    private JsonParser _parser;

    @Mock
    private DeserializationContext _context;

    @Mock
    private JsonDeserializer<Object> _deserializer;

    private MappingIterator<Object> _mappingIterator;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        _mappingIterator = new MappingIterator<Object>(null, _parser, _context, _deserializer, true, null);
    }

    @Test
    public void testHasNext() throws IOException {
        when(_parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.END_ARRAY);
        assertTrue(_mappingIterator.hasNext());
        assertFalse(_mappingIterator.hasNext());
        verify(_parser, times(2)).getCurrentToken();

        when(_parser.getCurrentToken()).thenReturn(null);
        when(_parser.nextToken()).thenReturn(null, JsonToken.END_ARRAY);
        assertFalse(_mappingIterator.hasNext());
        assertFalse(_mappingIterator.hasNext());
        verify(_parser, times(3)).getCurrentToken();
        verify(_parser, times(2)).nextToken();
    }

    @Test
    public void testNext() throws IOException {
        when(_parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(_deserializer.deserialize(_parser, _context)).thenReturn(null);
        assertNull(_mappingIterator.next());

        when(_parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(_deserializer.deserialize(_parser, _context)).thenReturn("test");
        assertEquals("test", _mappingIterator.next());

        verify(_deserializer, times(2)).deserialize(_parser, _context);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        _mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        _mappingIterator.close();
        verify(_parser).close();
    }

    @Test
    public void testReadAllWithList() throws IOException {
        List<Object> resultList = new ArrayList<Object>();
        when(_parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);
        when(_deserializer.deserialize(_parser, _context)).thenReturn("test", 123);
        _mappingIterator.readAll(resultList);
        assertEquals(2, resultList.size());
        assertEquals("test", resultList.get(0));
        assertEquals(123, resultList.get(1));
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        ArrayList<Object> results = new ArrayList<Object>();
        when(_parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY);
        when(_deserializer.deserialize(_parser, _context)).thenReturn("test", 123);
        _mappingIterator.readAll(results);
        assertEquals(2, results.size());
        assertEquals("test", results.get(0));
        assertEquals(123, results.get(1));
    }

    @Test
    public void testGetParser() {
        assertEquals(_parser, _mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(_mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = mock(JsonLocation.class);
        when(_parser.getCurrentLocation()).thenReturn(location);
        assertEquals(location, _mappingIterator.getCurrentLocation());
    }

    @Test(expected = NoSuchElementException.class)
    public void test_throwNoSuchElement() {
        _mappingIterator._throwNoSuchElement();
    }

    @Test(expected = RuntimeJsonMappingException.class)
    public void test_handleMappingException() {
        JsonMappingException e = mock(JsonMappingException.class);
        _mappingIterator._handleMappingException(e);
    }

    @Test(expected = RuntimeException.class)
    public void test_handleIOException() {
        IOException e = mock(IOException.class);
        _mappingIterator._handleIOException(e);
    }
}