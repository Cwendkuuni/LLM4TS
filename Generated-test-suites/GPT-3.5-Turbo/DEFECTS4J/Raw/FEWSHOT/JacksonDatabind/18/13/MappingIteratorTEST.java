package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Before;
import org.junit.After;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class MappingIteratorTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonDeserializer deserializer;

    @Mock
    private ObjectReader objectReader;

    private MappingIterator mappingIterator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mappingIterator = new MappingIterator(objectReader, jsonParser, deserializationContext, deserializer);
    }

    @After
    public void tearDown() throws Exception {
        mappingIterator.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(null);
        assertFalse(mappingIterator.hasNextValue());

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        assertTrue(mappingIterator.hasNextValue());

        when(jsonParser.nextToken()).thenReturn(null);
        assertFalse(mappingIterator.hasNextValue());

        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(null).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(null).thenReturn(JsonToken.END_ARRAY);
        try {
            mappingIterator.nextValue();
            fail();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        } catch (Exception e) {
            fail();
        }

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        try {
            mappingIterator.nextValue();
            verify(deserializer, times(1)).deserialize(jsonParser, deserializationContext);
        } catch (Exception e) {
            fail();
        }

        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        try {
            mappingIterator.nextValue();
            fail();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testReadAll() throws IOException {
        List resultList = new ArrayList();
        when(jsonParser.getCurrentToken()).thenReturn(null).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        mappingIterator.readAll(resultList);
        assertTrue(resultList.isEmpty());

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT).thenReturn(JsonToken.END_OBJECT);
        try {
            mappingIterator.readAll(resultList);
            verify(deserializer, times(1)).deserialize(jsonParser, deserializationContext);
        } catch (Exception e) {
            fail();
        }

        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT).thenReturn(null);
        try {
            mappingIterator.readAll(resultList);
            fail();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetParser() {
        assertEquals(jsonParser, mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        FormatSchema schema = Mockito.mock(FormatSchema.class);
        when(jsonParser.getSchema()).thenReturn(schema);
        assertEquals(schema, mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = Mockito.mock(JsonLocation.class);
        when(jsonParser.getCurrentLocation()).thenReturn(location);
        assertEquals(location, mappingIterator.getCurrentLocation());
        assertEquals(location, jsonParser.getCurrentLocation());
    }
}