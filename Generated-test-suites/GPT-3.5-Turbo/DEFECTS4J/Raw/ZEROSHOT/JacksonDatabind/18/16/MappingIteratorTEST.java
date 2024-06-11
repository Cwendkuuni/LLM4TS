package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator(null, parser, context, deserializer, false, null);
        
        boolean result = mappingIterator.hasNext();
        
        assertFalse(result);
    }

    @Test
    public void testNext() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        
        try {
            mappingIterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testRemove() {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        try {
            mappingIterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        mappingIterator.close();
    }

    @Test
    public void testHasNextValue() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        boolean result = mappingIterator.hasNextValue();

        assertFalse(result);
    }

    @Test
    public void testNextValue() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        try {
            mappingIterator.nextValue();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testReadAll() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        List result = mappingIterator.readAll();

        assertNotNull(result);
    }

    @Test
    public void testReadAllWithResultList() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        List resultList = new ArrayList<>();
        List result = mappingIterator.readAll(resultList);

        assertTrue(result == resultList);
    }

    @Test
    public void testReadAllWithResults() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        List results = new ArrayList<>();
        List result = mappingIterator.readAll(results);

        assertTrue(result == results);
    }

    @Test
    public void testGetParser() {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        JsonParser result = mappingIterator.getParser();

        assertNotNull(result);
    }

    @Test
    public void testGetParserSchema() {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        FormatSchema result = mappingIterator.getParserSchema();

        assertNull(result);
    }

    @Test
    public void testGetCurrentLocation() {
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer deserializer = null;
        MappingIterator mappingIterator = new MappingIterator<>(null, parser, context, deserializer, false, null);

        JsonLocation result = mappingIterator.getCurrentLocation();

        assertNotNull(result);
    }

}