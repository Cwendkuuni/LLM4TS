package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private static final ObjectMapper objectMapper;
    private static final JsonFactory jsonFactory;

    static {
        JsonFactoryBuilder builder = new JsonFactoryBuilder();
        jsonFactory = builder.build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private ObjectReader reader;

    @Before
    public void setUp() {
        reader = objectMapper.readerForListOf(String.class);
    }

    @Test
    public void testBasicIteration() throws IOException {
        JsonParser parser = jsonFactory.createParser("[\"apple\", \"banana\", \"cherry\"]");
        MappingIterator<String> iterator = reader.readValues(parser);

        List<String> expected = Collections.singletonList("apple");
        List<String> actual = iterator.readAll();
        Assert.assertEquals(expected, actual);

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("banana", iterator.next());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("cherry", iterator.next());

        Assert.assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testEmptyIterator() throws IOException {
        JsonParser parser = jsonFactory.createParser("[]");
        MappingIterator<String> iterator = reader.readValues(parser);

        List<String> actual = iterator.readAll();
        Assert.assertEquals(Collections.emptyList(), actual);

        Assert.assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoNextValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("[]");
        MappingIterator<String> iterator = reader.readValues(parser);

        iterator.next();
        iterator.next();

        iterator.next(); // will throw NoSuchElementException
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializationError() throws IOException {
        JsonParser parser = jsonFactory.createParser("[\"invalid\"]");
        reader = objectMapper.readerForListOf(String.class);
        reader.setValueHandler(new StringDeserializer(new StandardDeserializationContext()));

        MappingIterator<String> iterator = reader.readValues(parser);

        iterator.next();

        // Force deserialization error
        reader.setValueHandler(null);

        iterator.next(); // will throw IllegalStateException

        iterator.close();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        JsonParser parser = jsonFactory.createParser("");
        MappingIterator<String> iterator = reader.readValues(parser);

        Assert.assertTrue(iterator.hasNext());
        iterator.next();

        parser.close();

        iterator.next(); // will throw IOException

        iterator.close();
    }
}