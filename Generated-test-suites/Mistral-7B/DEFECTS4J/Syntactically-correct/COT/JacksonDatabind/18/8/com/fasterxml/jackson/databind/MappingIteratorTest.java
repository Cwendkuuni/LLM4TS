package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonFactory factory = mapper.getFactory();
    private static final JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);

    private Iterator<String> iterator;

    @Before
    public void setup() throws IOException {
        String json = "[\"foo\",\"bar\",\"baz\"]";
        JsonParser parser = factory.createParser(json);
        iterator = mapper.readValue(parser, javaType.withoutGenericTypes());
        parser.close();
    }

    @Test
    public void testHasNext() {
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals("foo", iterator.next());
            Assert.assertEquals("bar", iterator.next());
            Assert.assertEquals("baz", iterator.next());
        }
        Assert.assertNull(iterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        iterator.close();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        Assert.assertTrue(iterator.hasNextValue());
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals("foo", iterator.nextValue());
            Assert.assertEquals("bar", iterator.nextValue());
            Assert.assertEquals("baz", iterator.nextValue());
        }
        Assert.assertNull(iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> list = iterator.readAll();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("foo", list.get(0));
        Assert.assertEquals("bar", list.get(1));
        Assert.assertEquals("baz", list.get(2));
    }

    @Test(expected = IOException.class)
    public void testEmptyIterator() throws IOException {
        iterator = MappingIterator.emptyIterator();
        iterator.nextValue();
    }

    @Test
    public void testNullIterator() {
        Assert.assertNull(MappingIterator.emptyIterator());
    }

    @Test(expected = JsonMappingException.class)
    public void testMappingException() throws IOException {
        String json = "{\"foo\":[\"foo\",\"bar\"]}";
        JsonParser parser = factory.createParser(json);
        iterator = mapper.readValue(parser, javaType.withoutGenericTypes());
        parser.nextToken();
        parser.nextToken();
        iterator.nextValue();
        iterator.nextValue();
        parser.nextToken(); // INVALID_BODY
        iterator.nextValue();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        String json = "{\"foo\":[\"foo\",\"bar\"]}";
        JsonParser parser = factory.createParser(json);
        iterator = mapper.readValue(parser, javaType.withoutGenericTypes());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // EOF_AFTER_START_ARRAY
        iterator.nextValue();
    }
}