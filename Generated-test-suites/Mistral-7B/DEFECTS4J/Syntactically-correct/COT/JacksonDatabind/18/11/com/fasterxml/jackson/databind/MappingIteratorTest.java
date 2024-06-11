package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {
    private JsonParser parser;
    private DeserializationContext context;
    private ObjectMapper objectMapper;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setUp() throws Exception {
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        objectMapper = mock(ObjectMapper.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.getCurrentLocation()).thenReturn(new JsonLocation());

        JavaType jsonType = mock(JavaType.class);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);

        mappingIterator = new MappingIterator<String>(jsonType, parser, context, jsonDeserializer, false, null);
    }

    @Test
    public void testHasNext() {
        assertTrue(mappingIterator.hasNext());
    }

    @Test
    public void testHasNext_whenParserIsNull_returnsFalse() {
        mappingIterator = new MappingIterator<>(null, null, null, null, false, null);
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNext_whenParserHasNullCurrentToken_returnsFalse() {
        when(parser.getCurrentToken()).thenReturn(null);
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNext_whenParserHasEndArrayToken_returnsFalse() {
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = JsonMappingException.class)
    public void testNext_whenHasNextValueThrowsJsonMappingException() throws IOException {
        when(parser.nextToken()).thenThrow(JsonMappingException.class);
        mappingIterator.next();
    }

    @Test(expected = JsonMappingException.class)
    public void testNext_whenParserReturnsInvalidToken_throwsJsonMappingException() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        mappingIterator.next();
    }

    @Test(expected = IOException.class)
    public void testNext_whenParserThrowsIOException_throwsRuntimeException() throws IOException {
        when(parser.nextToken()).thenThrow(IOException.class);
        mappingIterator.next();
    }

    @Test
    public void testNextValue() throws IOException {
        String expected = "test";
        when(jsonDeserializer.deserialize(parser, context)).thenReturn(expected);
        String actual = mappingIterator.nextValue();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextValue_whenUpdatedValueIsNotNull_updatesIt() throws IOException {
        String expected = "test";
        String updatedValue = "updated";
        mappingIterator = new MappingIterator<String>(null, parser, context, jsonDeserializer, false, updatedValue);
        when(jsonDeserializer.deserialize(parser, context)).thenReturn(expected);
        mappingIterator.nextValue();
        assertEquals(expected, updatedValue);
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> expectedList = Arrays.asList("test1", "test2", "test3");
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        List<String> actualList = mappingIterator.readAll();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testReadAll_whenParserReturnsNull_returnsEmptyList() throws IOException {
        List<String> expectedList = Collections.emptyList();
        when(parser.nextToken()).thenReturn(null);

        List<String> actualList = mappingIterator.readAll();
        assertEquals(expectedList, actualList);
    }
}