package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceNotFound;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private ObjectReader objectReader;
    private MappingIterator<String> mappingIterator;
    private JsonFactory jsonFactory;

    @BeforeMethod
    public void setUp() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper();
        objectReader = objectMapper.readerForListOf(String.class);
    }

    @Test
    public void testEmptyIterator() {
        MappingIterator<String> emptyMappingIterator = MappingIterator.emptyIterator();
        Assert.assertFalse(emptyMappingIterator.hasNext());
        emptyMappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithNullDeserializer() {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);

        try {
            mappingIterator = new MappingIterator<String>(javaType, null, null, null, false, null);
            mappingIterator.next();
            Assert.fail("Expected IOException when passing null JsonParser.");
        } catch (IOException e) {
            // Expected
        } finally {
            mappingIterator.close();
        }
    }

    @Test
    public void testMappingIteratorWithValidDeserializer() throws IOException {
        String jsonString = "[\"abc\", \"def\", \"ghi\"]";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser jsonParser = jsonFactory.createParser(byteArrayInputStream);

        mappingIterator = objectReader.readValues(jsonParser);

        List<String> list = new ArrayList<>();
        list.addAll(mappingIterator.readAll());

        Assert.assertEquals(list, Arrays.asList("abc", "def", "ghi"));
        mappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserialization() throws IOException {
        String jsonString = "[\"abc\", null, \"def\", \"ghi\"]";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser jsonParser = jsonFactory.createParser(byteArrayInputStream);

        mappingIterator = objectReader.readValues(jsonParser);

        try {
            mappingIterator.next();
            mappingIterator.next();
            String nullValue = mappingIterator.next();

            // DeserializationException should be thrown here
            Assert.fail("Expected DeserializationException");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertTrue(e.getCause() instanceof InstantiationException);
            mappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserializationAndClosingParser() throws IOException {
        String jsonString = "[\"abc\", null, \"def\", \"ghi\"]";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());

        mappingIterator = new MappingIterator<String>(
            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
            jsonFactory.createParser(byteArrayInputStream),
            null,
            null,
            true,
            null
        );

        try {
            mappingIterator.next();
            mappingIterator.next();
            String nullValue = mappingIterator.next();

            // DeserializationException should be thrown here
            Assert.fail("Expected DeserializationException");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertTrue(e.getCause() instanceof InstantiationException);
            mappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserializationAndEmptyList() throws IOException {
        String jsonString = "[null]";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());

        mappingIterator = objectReader.readValues(jsonFactory.createParser(byteArrayInputStream));

        try {
            mappingIterator.next();
            Assert.fail("Expected JsonMappingException due to empty list");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertEquals(e.getCause(), new ReferenceNotFound("No content to map due to: empty JSON list to unwrapped TypeId 'class java.util.ArrayList<java.lang.String>'", null));
            mappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserializationAndNullList() throws IOException {
        String jsonString = "null";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());

        mappingIterator = objectReader.readValues(jsonFactory.createParser(byteArrayInputStream));

        try {
            mappingIterator.next();
            Assert.fail("Expected JsonMappingException due to null list");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertEquals(e.getCause(), new ReferenceNotFound("No content to map due to: null JSON list", null));
            mappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserializationAndCustomDeserializer() throws IOException {
        String jsonString = "[{\"value\": null}]";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());

        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, CustomClass.class);
        JsonDeserializer<CustomClass> customDeserializer = new JsonDeserializer<CustomClass>(CustomClass.class) {
            @Override
            public CustomClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                throw new InstantiationException("Test custom deserializer exception");
            }
        };

        mappingIterator = new MappingIterator<CustomClass>(javaType, jsonFactory.createParser(byteArrayInputStream), null, customDeserializer, false, null);

        try {
            mappingIterator.next();
            Assert.fail("Expected InstantiationException");
        } catch (IOException e) {
            // Expected
            Assert.assertEquals(e.getCause(), new InstantiationException("Test custom deserializer exception"));
            mappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserializationAndEmptyIterator() {
        MappingIterator<String> emptyMappingIterator = new MappingIterator<String>(
            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
            null,
            null,
            null,
            false,
            null
        );

        try {
            emptyMappingIterator.next();
            Assert.fail("Expected IOException when parsing null JsonParser.");
        } catch (IOException e) {
            // Expected
        } finally {
            emptyMappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(emptyMappingIterator.hasNext());
    }

    @Test
    public void testMappingIteratorWithExceptionDuringDeserializationAndEmptyDeserializer() {
        String jsonString = "[\"abc\", null, \"def\", \"ghi\"]";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonString.getBytes());
        JsonParser jsonParser = jsonFactory.createParser(byteArrayInputStream);

        mappingIterator = new MappingIterator<String>(
            objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
            jsonParser,
            null,
            new None<String>(),
            false,
            null
        );

        try {
            mappingIterator.next();
            mappingIterator.next();
            mappingIterator.next();
            mappingIterator.next();
            Assert.fail("Expected JsonMappingException due to no deserializer");
        } catch (JsonMappingException e) {
            // Expected
            Assert.assertEquals(e.getCause(), new InstantiationException("No ValueDeserializer found for class [java.lang.String] and input [START_ARRAY]"));
            mappingIterator.close();
        }

        // Verify that iterator is closed and has no more elements
        Assert.assertFalse(mappingIterator.hasNext());
        mappingIterator.close();
    }

    public static class CustomClass {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}