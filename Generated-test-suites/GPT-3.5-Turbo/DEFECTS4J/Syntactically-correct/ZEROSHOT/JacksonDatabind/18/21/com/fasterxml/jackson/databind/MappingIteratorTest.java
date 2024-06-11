package com.fasterxml.jackson.databind;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MappingIteratorTest {

    private MappingIterator<Integer> iterator;

    @Before
    public void setUp() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Integer> deserializer = mock(JsonDeserializer.class);
        iterator = new MappingIterator<>(JavaType.INT_TYPE, parser, context, deserializer, true, null);
    }

    @Test
    public void testHasNext() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(null);
        assertFalse(iterator.hasNext());

        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        JsonMappingException mappingException = mock(JsonMappingException.class);
        IOException ioException = mock(IOException.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY).thenReturn(JsonToken.VALUE_NUMBER_INT)
                .thenReturn(null);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT).thenReturn(null);
        when(parser.clearCurrentToken()).thenReturn(parser);
        when(parser.close()).thenReturn(null);

        when(deserializer.deserialize(parser, context)).thenReturn(1);
        when(deserializer.deserialize(parser, context, null)).thenReturn(null);
        when(deserializer.deserialize(parser, context, 1)).thenReturn(null);

        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
        }

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());

        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        iterator.remove();
    }

    @Test
    public void testClose() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        iterator._parser = parser;

        iterator.close();
        verify(parser).close();

        iterator._parser = null;
        iterator.close();
        verifyZeroInteractions(parser);
    }

    @Test
    public void testHasNextValue() throws Exception {
        iterator._parser = null;
        assertFalse(iterator.hasNextValue());

        JsonParser parser = mock(JsonParser.class);
        iterator._parser = parser;

        when(parser.getCurrentToken()).thenReturn(null);
        assertTrue(iterator.hasNextValue());

        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        assertTrue(iterator.hasNextValue());

        iterator._hasNextChecked = true;
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        JsonMappingException mappingException = mock(JsonMappingException.class);
        IOException ioException = mock(IOException.class);

        iterator._parser = null;
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
        }

        iterator._parser = parser;

        iterator._hasNextChecked = false;
        assertFalse(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
        }

        iterator._hasNextChecked = true;
        when(parser.getCurrentToken()).thenReturn(null);
        assertFalse(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
        }

        iterator._hasNextChecked = true;
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT)
                .thenReturn(JsonToken.VALUE_NUMBER_INT).thenReturn(null);
        assertFalse(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
        }

        iterator._hasNextChecked = true;
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(deserializer.deserialize(parser, context)).thenReturn(1);
        assertEquals(1, iterator.nextValue().intValue());

        iterator._hasNextChecked = true;
        iterator._updatedValue = 1;
        when(deserializer.deserialize(parser, context, 1)).thenReturn(null);
        assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        iterator._parser = parser;

        when(iterator.hasNextValue()).thenReturn(false);
        assertTrue(iterator.readAll().isEmpty());

        when(iterator.hasNextValue()).thenReturn(true).thenReturn(false);
        when(iterator.nextValue()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        List<Integer> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(5, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals(4, list.get(3).intValue());
        assertEquals(5, list.get(4).intValue());

        iterator._parser = null;
        iterator.readAll();
    }

    @Test
    public void testReadAllWithCollection() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        iterator._parser = parser;

        when(iterator.hasNextValue()).thenReturn(false);
        assertTrue(iterator.readAll(new ArrayList<>()).isEmpty());

        when(iterator.hasNextValue()).thenReturn(true).thenReturn(false);
        when(iterator.nextValue()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        List<Integer> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(5, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
        assertEquals(4, list.get(3).intValue());
        assertEquals(5, list.get(4).intValue());

        iterator._parser = null;
        iterator.readAll(new ArrayList<>());
    }

    @Test
    public void testGetParser() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        iterator._parser = parser;

        assertSame(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        FormatSchema schema = mock(FormatSchema.class);
        iterator._parser = parser;

        when(parser.getSchema()).thenReturn(schema);
        assertSame(schema, iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        JsonLocation location = mock(JsonLocation.class);
        iterator._parser = parser;

        when(parser.getCurrentLocation()).thenReturn(location);
        assertSame(location, iterator.getCurrentLocation());
    }
}