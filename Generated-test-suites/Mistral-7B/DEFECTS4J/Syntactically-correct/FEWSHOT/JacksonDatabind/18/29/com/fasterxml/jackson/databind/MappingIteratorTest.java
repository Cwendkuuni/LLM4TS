package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private MappingIterator<Object> mappingIterator;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonParser = mock(JsonParser.class);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenAnswer(i -> "");
        when(jsonParser.getCurrentLocation()).thenReturn(new JsonLocation(1, 1, "test.json"));

        mappingIterator = new MappingIterator<>(objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), jsonParser, mock(DeserializationContext.class), null, true, null);
    }

    @Test
    public void testHasNext() throws Exception {
        assertTrue(mappingIterator.hasNext());
        verify(jsonParser).nextToken();

        mappingIterator.next();
        verify(jsonParser).nextToken();
        verifyZeroInteractions(jsonParser);

        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws Exception {
        assertTrue(mappingIterator.hasNextValue());
        verifyZeroInteractions(jsonParser);

        mappingIterator.nextValue();
        verify(jsonParser).clearCurrentToken();
        assertNull(mappingIterator.nextValue());
        verifyZeroInteractions(jsonParser);

        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws Exception {
        mappingIterator.close();
        verify(jsonParser).close();
    }

    @Test(expected = JsonMappingException.class)
    public void testNextValueWithMappingException() throws Exception {
        when(jsonParser.nextToken()).thenThrow(new JsonMappingException("Test Exception"));

        assertFalse(mappingIterator.hasNextValue());

        mappingIterator.nextValue();
        verify(jsonParser).clearCurrentToken();
        throw new AssertionError();
    }

    @Test(expected = IOException.class)
    public void testNextValueWithIOException() throws Exception {
        doThrow(new IOException("Test Exception")).when(jsonParser).nextToken();

        assertFalse(mappingIterator.hasNextValue());

        mappingIterator.nextValue();
        verify(jsonParser).clearCurrentToken();
        throw new AssertionError();
    }

    @Test
    public void testNextValueWithNullValue() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.NULL);

        mappingIterator.nextValue();
        assertEquals(NullValueProvider.instance, mappingIterator.nextValue());
        verify(jsonParser).clearCurrentToken();
    }

    @Test
    public void testReadAll() throws Exception {
        List<Object> list = new ArrayList<>();
        mappingIterator.readAll(list);
        verifyZeroInteractions(jsonParser);
        assertEquals(Collections.singletonList(null), list);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        list = new ArrayList<>();
        mappingIterator.readAll(list);
        assertEquals(Arrays.asList("", "test"), list);

        when(jsonParser.nextToken()).thenThrow(new IOException("Test Exception"));

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        list = new ArrayList<>();
        mappingIterator.readAll(list);
        verifyZeroInteractions(jsonParser);
        assertEquals(Collections.singletonList(null), list);
    }

    @Test
    public void testReadAllWithNullParser() throws Exception {
        when(jsonParser.nextToken()).thenThrow(new IOException("Test Exception"));

        List<Object> list = new ArrayList<>();
        mappingIterator = new MappingIterator<>(objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), null, mock(DeserializationContext.class), null, false, null);
        mappingIterator.readAll(list);
        verifyZeroInteractions(jsonParser);
        assertEquals(Collections.singletonList(null), list);
    }
}