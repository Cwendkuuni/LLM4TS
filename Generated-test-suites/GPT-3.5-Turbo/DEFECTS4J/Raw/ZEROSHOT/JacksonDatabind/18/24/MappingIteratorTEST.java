package com.fasterxml.jackson.databind;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    private MappingIterator<String> mappingIterator;
    private DeserializationContext deserializationContext;
    private JsonDeserializer<String> jsonDeserializer;
    private JsonParser jsonParser;

    @Test
    public void testHasNext() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        Assert.assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testNext() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        try {
            mappingIterator.next();
        } catch (RuntimeException e) {
            Assert.assertEquals("java.lang.NullPointerException: null", e.getMessage());
        }
    }

    @Test
    public void testRemove() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        try {
            mappingIterator.remove();
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("java.lang.UnsupportedOperationException", e.toString());
        }
    }

    @Test
    public void testClose() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        try {
            mappingIterator.close();
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException", e.toString());
        }
    }

    @Test
    public void testHasNextValue() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        try {
            Assert.assertFalse(mappingIterator.hasNextValue());
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException", e.toString());
        }
    }

    @Test
    public void testNextValue() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        try {
            Assert.assertNull(mappingIterator.nextValue());
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException", e.toString());
        }
    }

    @Test
    public void testReadAll() {
        mappingIterator = new MappingIterator<String>(null, null, null, null, false, null);

        try {
            Assert.assertEquals(new ArrayList<String>(), mappingIterator.readAll());
        } catch (IOException e) {
            Assert.assertEquals("java.io.IOException", e.toString());
        }
    }
}