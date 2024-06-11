package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private MappingIterator<String> iterator;
    private JsonParser parser;
    private DeserializationContext context;
    private JsonDeserializer<String> deserializer;

    @Before
    public void setUp() throws Exception {
        parser = new JsonParser();
        context = new DeserializationContext();
        deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                return "test";
            }
        };
        iterator = new MappingIterator<String>(null, parser, context, deserializer, false, null);
    }

    @After
    public void tearDown() throws Exception {
        iterator = null;
        parser = null;
        context = null;
        deserializer = null;
    }

    @Test
    public void testHasNext() {
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testRemove() {
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testClose() {
        try {
            iterator.close();
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testHasNextValue() {
        try {
            assertFalse(iterator.hasNextValue());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testNextValue() {
        try {
            assertNull(iterator.nextValue());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testReadAll() {
        List<String> list = new ArrayList<String>();
        try {
            assertFalse(iterator.readAll(list));
            assertEquals(0, list.size());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testReadAllWithResultList() {
        List<String> list = new ArrayList<String>();
        try {
            assertFalse(iterator.readAll(list));
            assertEquals(0, list.size());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testReadAllWithResults() {
        List<String> list = new ArrayList<String>();
        try {
            assertFalse(iterator.readAll(list));
            assertEquals(0, list.size());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testGetParser() {
        assertEquals(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(iterator.getCurrentLocation());
    }

}