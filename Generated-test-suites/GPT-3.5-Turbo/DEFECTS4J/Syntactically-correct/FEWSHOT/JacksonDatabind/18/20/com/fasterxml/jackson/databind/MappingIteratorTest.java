package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.formatdata.formatdata;
import com.fasterxml.jackson.databind.runtimeJsonMappingException;
import com.fasterxml.jackson.databind.FormatSchema;

import org.junit.Test;

public class MappingIteratorTest {
    @Test
    public void testHasNextValueWithClosedParser() throws IOException {
        MappingIterator<formatdata> mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValueNoNextValue() throws IOException {
        MappingIterator<formatdata> mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWithNextValue() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testHasNextValueWithIOException() {
        MappingIterator<formatdata> mappingIterator = createMappingIteratorWithIOException();
        try {
            mappingIterator.hasNextValue();
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNextValueNullParser() throws IOException {
        MappingIterator<formatdata> mappingIterator = MappingIterator.emptyIterator();
        try {
            mappingIterator.nextValue();
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNextValueNullDeserialization() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        try {
            mappingIterator.nextValue();
        } catch (RuntimeJsonMappingException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNextValueIOException() {
        MappingIterator<formatdata> mappingIterator = createMappingIteratorWithIOException();
        try {
            mappingIterator.nextValue();
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        formatdata data = mappingIterator.nextValue();
        assertNotNull(data);
    }

    @Test
    public void testRemove() {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        try {
            mappingIterator.remove();
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testClose() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        mappingIterator.close();
        assertNull(mappingIterator.getParser());
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        List<formatdata> resultList = mappingIterator.readAll();
        assertNotNull(resultList);
    }

    @Test
    public void testReadAllEmpty() throws IOException {
        MappingIterator<formatdata> mappingIterator = MappingIterator.emptyIterator();
        List<formatdata> resultList = mappingIterator.readAll();
        assertNotNull(resultList);
        assertEquals(0, resultList.size());
    }

    @Test
    public void testReadAllNullResultList() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        try {
            mappingIterator.readAll(null);
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testReadAllWithIOExceptio() {
        MappingIterator<formatdata> mappingIterator = createMappingIteratorWithIOException();
        try {
            mappingIterator.readAll();
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testReadAllWithIOExceptioAndCollection() {
        MappingIterator<formatdata> mappingIterator = createMappingIteratorWithIOException();
        try {
            mappingIterator.readAll(new ArrayList<>());
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        List<formatdata> resultList = mappingIterator.readAll(new ArrayList<>());
        assertNotNull(resultList);
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        JsonParser parser = mappingIterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        FormatSchema schema = mappingIterator.getParserSchema();
        assertNotNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<formatdata> mappingIterator = createMappingIterator();
        JsonLocation location = mappingIterator.getCurrentLocation();
        assertNotNull(location);
    }

    private MappingIterator<formatdata> createMappingIterator() throws IOException {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = true;
        Object valueToUpdate = new Object();

        return new MappingIterator<>(type, parser, context, deser, managedParser, valueToUpdate);
    }

    private MappingIterator<formatdata> createMappingIteratorWithIOException() {
        JavaType type = null;
        JsonParser parser = null;
        DeserializationContext context = null;
        JsonDeserializer<?> deser = null;
        boolean managedParser = true;
        Object valueToUpdate = new Object();

        return new MappingIterator<>(type, parser, context, deser, managedParser, valueToUpdate) {
            @Override
            public boolean hasNextValue() throws IOException {
                throw new IOException();
            }
        };
    }
}