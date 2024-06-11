package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNextAndNextWithNullIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextAndNextWithClosedParser() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = null;
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, true, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        iterator.close();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextAndNextWithEmptyIterator() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = null;
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextAndNextWithNonEmptyIterator() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JsonParser parser = mockParser();
        DeserializationContext context = mockContext();
        JsonDeserializer<Object> deserializer = mockDeserializer();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        parser.nextToken(); // skip START_ARRAY
        assertTrue(iterator.hasNext());
        Object next = iterator.next();
        assertTrue(iterator.hasNext());
        Object next2 = iterator.next();
        assertTrue(iterator.hasNext());
        Object next3 = iterator.next();
        assertFalse(iterator.hasNext());
        assertSame(next, next2);
        assertSame(next2, next3);
        assertSame(next, next2);
        assertSame(next, next3);
    }

    @Test
    public void testHasNextValueAndNextValueWithNullIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testHasNextValueAndNextValueWithClosedParser() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = null;
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, true, null);
        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
        iterator.close();
        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testHasNextValueAndNextValueWithEmptyIterator() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = null;
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        assertFalse(iterator.hasNextValue());
        assertNull(iterator.nextValue());
    }

    @Test
    public void testHasNextValueAndNextValueWithNonEmptyIterator() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JsonParser parser = mockParser();
        DeserializationContext context = mockContext();
        JsonDeserializer<Object> deserializer = mockDeserializer();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        parser.nextToken(); // skip START_ARRAY
        assertTrue(iterator.hasNextValue());
        Object nextValue = iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        Object nextValue2 = iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        Object nextValue3 = iterator.nextValue();
        assertFalse(iterator.hasNextValue());
        assertSame(nextValue, nextValue2);
        assertSame(nextValue2, nextValue3);
        assertSame(nextValue, nextValue2);
        assertSame(nextValue, nextValue3);
    }

    @Test
    public void testHasNextValueAndNextValueWithClosedParserAndDeserializationFailure() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JsonParser parser = mockParser();
        DeserializationContext context = mockContext();
        JsonDeserializer<Object> deserializer = mockDeserializerWithThrowingDeserialize();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        parser.nextToken(); // skip START_ARRAY
        assertTrue(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeJsonMappingException e) { // expected
            // continue
        }
        assertTrue(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeJsonMappingException e) { // expected
            // continue
        }
        assertTrue(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeJsonMappingException e) { // expected
            // continue
        }
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testHasNextValueAndNextValueWithClosedParserAndIOException() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JsonParser parser = mockParserWithIOExceptionOnNextToken();
        DeserializationContext context = mockContext();
        JsonDeserializer<Object> deserializer = mockDeserializer();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        parser.nextToken(); // skip START_ARRAY
        assertTrue(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeException e) { // expected
            // continue
        }
        assertTrue(iterator.hasNextValue());
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeException e) { // expected
            // continue
        }
        try {
            iterator.nextValue();
            fail();
        } catch (RuntimeException e) { // expected
            // continue
        }
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testReadAllWithNullIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        List<Object> result = iterator.readAll();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testReadAllWithClosedParser() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = null;
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, true, null);
        List<Object> result = iterator.readAll();
        assertEquals(Collections.emptyList(), result);
        iterator.close();
        List<Object> result2 = iterator.readAll();
        assertEquals(Collections.emptyList(), result2);
    }

    @Test
    public void testReadAllWithEmptyIterator() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = null;
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        List<Object> result = iterator.readAll();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testReadAllWithNonEmptyIterator() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JsonParser parser = mockParser();
        DeserializationContext context = mockContext();
        JsonDeserializer<Object> deserializer = mockDeserializer();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        parser.nextToken(); // skip START_ARRAY
        List<Object> result = iterator.readAll();
        assertEquals(Arrays.asList(new Object(), new Object(), new Object()), result);
        iterator.close();
        List<Object> result2 = iterator.readAll();
        assertEquals(Collections.emptyList(), result2);
    }

    @Test
    public void testReadAllWithNonEmptyIteratorAndUpdatedValue() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JsonParser parser = mockParser();
        DeserializationContext context = mockContext();
        JsonDeserializer<Object> deserializer = mockDeserializer();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<Object>(type, parser, context, deserializer, false, updatedValue);
        parser.nextToken(); // skip START_ARRAY
        List<Object> result = iterator.readAll();
        assertEquals(Arrays.asList(new Object(), new Object(), new Object()), result);
        Object next = iterator.nextValue();
        assertSame(updatedValue, next);
        List<Object> result2 = iterator.readAll();
        assertEquals(Arrays.asList(updatedValue, updatedValue, updatedValue), result2);
        iterator.close();
        List<Object> result3 = iterator.readAll();
        assertEquals(Collections.emptyList(), result3);
    }

    private JsonParser mockParser() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentLocation()).thenReturn(mockLocation());
        when(parser.nextToken()).thenAnswer(invocation -> parser.getCurrentToken());
        when(parser.clearCurrentToken()).thenReturn(parser);
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        return parser;
    }

    private DeserializationContext mockContext() {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.getRootValueSerializer()).thenReturn(mockSerializer());
        return context;
    }

    private JsonDeserializer<Object> mockDeserializer() {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenAnswer(invocation -> new Object());
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class))).thenAnswer(invocation -> {
            Object object = invocation.getArgument(2);
            when(deserializer.getNullValue()).thenReturn(object);
            return object;
        });
        return deserializer;
    }

    private JsonDeserializer<Object> mockDeserializerWithThrowingDeserialize() {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenThrow(new RuntimeJsonMappingException(""));
        return deserializer;
    }

    private JsonParser mockParserWithIOExceptionOnNextToken() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentLocation()).thenReturn(mockLocation());
        when(parser.nextToken()).thenThrow(new IOException(""));
        when(parser.clearCurrentToken()).thenReturn(parser);
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        return parser;
    }

    private ObjectName mockObjectName() {
        return new ObjectMapper.DefaultObjectMapper.ObjectName("test");
    }

    private JsonLocation mockLocation() {
        return new JsonLocation(1, 1, 1, 1);
    }

    private Serializer<?> mockSerializer() {
        return mock(Serializer.class);
    }
}