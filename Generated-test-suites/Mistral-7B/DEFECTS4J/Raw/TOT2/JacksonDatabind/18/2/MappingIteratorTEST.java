package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    @Test
    public void testHasNextAndNext() throws IOException {
        JavaType type = new TypeFactory().constructType(String.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[\"apple\", \"banana\"]");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<String> deserializer = new StringDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("apple", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("banana", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        JavaType type = new TypeFactory().constructType(String.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[]");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<String> deserializer = new StringDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.next();
        iterator.next();
        iterator.next(); // should throw NoSuchElementException
    }

    @Test(expected = RuntimeJsonMappingException.class)
    public void testJsonMappingException() throws IOException {
        JavaType type = new TypeFactory().constructType(String.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{abc}");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<String> deserializer = new StringDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.next(); // should throw JsonMappingException
    }

    @Test(expected = RuntimeException.class)
    public void testIOException() throws IOException {
        JavaType type = new TypeFactory().constructType(String.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"foo\":1}");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<String> deserializer = new StringDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        parser.close(); // close parser before testing exception
        iterator.next(); // should throw IOException
    }

    @Test
    public void testCloseParser() throws IOException {
        JavaType type = new TypeFactory().constructType(String.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[\"apple\", \"banana\"]");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<String> deserializer = new StringDeserializer();

        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.next();
        iterator.next();

        iterator.close();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType type = new TypeFactory().constructType(List.class);
        JavaType listType = new TypeFactory().constructType(String.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[\"apple\", \"banana\", \"orange\"]");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<List<String>> listDeserializer = new ListDeserializer<>(listType, new StringDeserializer());

        MappingIterator<List<String>> iterator = new MappingIterator<>(type, parser, context,
                listDeserializer, true, new ArrayList<List<String>>());

        List<List<String>> result = iterator.readAll();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("apple", result.get(0).get(0));
        Assert.assertEquals("banana", result.get(0).get(1));
        Assert.assertEquals("orange", result.get(0).get(2));
    }

    @Test
    public void testReadAllCollection() throws IOException {
        JavaType type = new TypeFactory().constructType(ArrayList.class);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[\"apple\", \"banana\", \"orange\"]");
        DeserializationContext context = new TestDeserializationContext();
        JsonDeserializer<ArrayList<String>> listDeserializer = new ListDeserializer<>(type, new StringDeserializer());

        MappingIterator<ArrayList<String>> iterator = new MappingIterator<>(type, parser, context, listDeserializer, true, null);

        ArrayList<String> result = iterator.readAll(new ArrayList<String>());
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("apple", result.get(0));
        Assert.assertEquals("banana", result.get(1));
        Assert.assertEquals("orange", result.get(2));
    }
}