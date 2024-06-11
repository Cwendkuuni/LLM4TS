package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext ctxt = null;
        JsonDeserializer<Object> deserializer = null;
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, null);

        // Test when iterator is closed
        assertFalse(iterator.hasNext());
        iterator.close();
        assertFalse(iterator.hasNext());

        // Test when iterator is not initialized
        assertFalse(iterator.hasNext());

        // Test when parser returns null token
        parser = Mockito.mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(null);
        iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, null);
        assertFalse(iterator.hasNext());

        // Test when parser returns END_ARRAY token
        when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        assertFalse(iterator.hasNext());

        // Test when parser returns valid token and has not checked next value
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, null);
        assertTrue(iterator.hasNext());

        // Test when parser returns valid token and has checked next value
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        iterator.hasNextValue();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        JavaType type = null;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, updatedValue);

        // Test when iterator is closed
        iterator.close();
        try {
            iterator.next();
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test when parser returns null token
        when(parser.getCurrentToken()).thenReturn(null);
        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException e) {
            // Expected
        }

        // Test when parser returns START_OBJECT token and deserializer returns null
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(null);
        try {
            iterator.next();
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test when parser returns START_OBJECT token and deserializer returns value
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(new Object());
        assertEquals(new Object(), iterator.next());

        // Test when parser returns VALUE_STRING token and deserializer returns value
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserialize(parser, ctxt, updatedValue)).thenReturn(new Object());
        assertEquals(new Object(), iterator.next());
        assertEquals(updatedValue, iterator.next());
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType type = null;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, updatedValue);

        List<Object> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        // Test when iterator is closed
        iterator.close();
        try {
            iterator.next();
            fail();
        } catch (IOException e) {
            // Expected
        }
        assertEquals(0, resultList.size());

        // Test when parser returns null token
        when(parser.getCurrentToken()).thenReturn(null);
        iterator.next();
        assertEquals(0, resultList.size());

        // Test when parser returns START_OBJECT token and deserializer returns null
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(null);
        iterator.next();
        assertEquals(0, resultList.size());

        // Test when parser returns START_OBJECT token and deserializer returns value
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(new Object());
        iterator.next();
        resultList.clear();
        iterator.readAll(resultList);
        assertEquals(1, resultList.size());
        assertEquals(new Object(), resultList.get(0));

        // Test when parser returns VALUE_STRING token and deserializer returns value
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserialize(parser, ctxt, updatedValue)).thenReturn(new Object());
        iterator.next();
        resultList.clear();
        iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, updatedValue);
        iterator.next();
        iterator.readAll(resultList);
        assertEquals(1, resultList.size());
        assertEquals(updatedValue, resultList.get(0));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        JavaType type = null;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = Mockito.mock(JsonDeserializer.class);
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, updatedValue);

        Collection<Object> resultList = new ArrayList<>();
        iterator.readAll(resultList);

        // Test when iterator is closed
        iterator.close();
        try {
            iterator.next();
            fail();
        } catch (IOException e) {
            // Expected
        }
        assertEquals(0, resultList.size());

        // Test when parser returns null token
        when(parser.getCurrentToken()).thenReturn(null);
        iterator.next();
        assertEquals(0, resultList.size());

        // Test when parser returns START_OBJECT token and deserializer returns null
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(null);
        iterator.next();
        assertEquals(0, resultList.size());

        // Test when parser returns START_OBJECT token and deserializer returns value
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, ctxt)).thenReturn(new Object());
        iterator.next();
        resultList.clear();
        iterator.readAll(resultList);
        assertEquals(1, resultList.size());
        assertEquals(new Object(), resultList.iterator().next());

        // Test when parser returns VALUE_STRING token and deserializer returns value
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserialize(parser, ctxt, updatedValue)).thenReturn(new Object());
        iterator.next();
        resultList.clear();
        iterator = new MappingIterator<>(type, parser, ctxt, deserializer, true, updatedValue);
        iterator.next();
        iterator.readAll(resultList);
        assertEquals(1, resultList.size());
        assertEquals(updatedValue, resultList.iterator().next());
    }
}