package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

/**
 * Test cases for {@link MappingIterator} class.
 */
public class MappingIteratorTest {

    @SuppressWarnings("unchecked")
    private MappingIterator<String> createMappingIterator(JavaType type, JsonParser parser, DeserializationContext context,
            JsonDeserializer<String> deserializer, boolean managedParser) throws IOException {
        return new MappingIterator<String>(type, parser, context, deserializer, managedParser, null);
    }

    @Test
    public void testHasNext() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        verify(parser, times(3)).nextToken();
    }

    @Test
    public void testHasNextValue() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        assertTrue(iterator.hasNextValue());
        assertTrue(iterator.hasNextValue());

        verify(parser, times(2)).nextToken();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoNextValue() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);

        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        iterator.nextValue();
        iterator.nextValue();

        assertFalse(iterator.hasNextValue());

        verify(parser, times(3)).nextToken();
    }

    @Test
    public void testNextValue() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);

        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserialize(parser, context)).thenReturn("hello");

        assertEquals("hello", iterator.nextValue());
        assertEquals("hello", iterator.nextValue());

        verify(parser, times(3)).nextToken();
        verify(deserializer, times(2)).deserialize(parser, context);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);

        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        JsonParser parserSpy = spy(parser);

        MappingIterator<String> iterator = new MappingIterator<String>(null, parser, null, null, false, null);

        iterator.close();

        verify(parserSpy).close();
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);
        List<String> resultList = new ArrayList<String>();

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(deserializer.deserialize(parser, context)).thenReturn("foo");
        when(deserializer.deserialize(parser, context)).thenReturn("bar");

        iterator.readAll(resultList);

        assertEquals(2, resultList.size());
        assertEquals("foo", resultList.get(0));
        assertEquals("bar", resultList.get(1));

        verify(parser, times(5)).nextToken();
        verifyNoMoreInteractions(parser);
    }

    @Test
    public void testReadAllCollection() throws IOException {
        JavaType type = new TypeFactory().constructType(String[].class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        MappingIterator<String> iterator = createMappingIterator(type, parser, context, deserializer, true);
        Collection<String> resultList = new ArrayList<String>();

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(deserializer.deserialize(parser, context)).thenReturn("foo");
        when(deserializer.deserialize(parser, context)).thenReturn("bar");

        iterator.readAll(resultList);

        assertEquals(2, resultList.size());
        assertEquals("foo", resultList.iterator().next());
        assertEquals("bar", resultList.iterator().next());

        verify(parser, times(5)).nextToken();
        verifyNoMoreInteractions(parser);
    }
}