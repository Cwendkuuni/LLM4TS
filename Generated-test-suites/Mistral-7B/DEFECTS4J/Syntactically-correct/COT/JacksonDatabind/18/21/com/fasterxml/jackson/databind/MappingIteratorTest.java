package com.fasterxml.jackson.databind.test; // Add appropriate test package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class MappingIteratorTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonFactory.Feature.FAIL_ON_EMPTY_BEANS, false);
        jsonFactory.setCodecFeature(JsonCodecFeature.WRITE_NULL_SERIALIZED_AS_NULL, true);
        objectMapper = new ObjectMapper(jsonFactory);
        deserializationContext = objectMapper.getDeserializationContext();
        ObjectReader reader = objectMapper.readerForListOf(String.class);
        jsonParser = reader.getParser();
    }

    @Test
    public void testHasNext() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        assertTrue(mappingIterator.hasNext());
        mappingIterator.next();
        assertTrue(mappingIterator.hasNext());
        mappingIterator.next();
        mappingIterator.next();
        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.nextValue();
        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNext() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        assertEquals("[]", mappingIterator.next().toString());
        assertEquals("\"element1\"", mappingIterator.next().toString());
        assertEquals("\"element2\"", mappingIterator.next().toString());
        assertEquals("\"element3\"", mappingIterator.next().toString());
        assertEquals("\"element4\"", mappingIterator.next().toString());
        mappingIterator.close();
        assertNull(mappingIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        mappingIterator.next();
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testReadAll() throws Exception {
        List<String> list = Arrays.asList("element1", "element2", "element3", "element4");
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        assertEquals(list, mappingIterator.readAll());
    }

    @Test
    public void testReadAllEmptyList() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        assertEquals(Collections.emptyList(), mappingIterator.readAll());
    }

    @Test
    public void testReadAllArrayList() throws Exception {
        ArrayList<String> list = new ArrayList<>();
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, list);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();

        assertEquals(list, mappingIterator.readAll());
    }

    @Test
    public void testNextValueNullDeserializer() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                null, true, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();

        mappingIterator.nextValue();
        assertNull(mappingIterator.nextValue());
        mappingIterator.close();
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testNextValueErrorDeserialization() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                new StringDeserializationError(), true, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();

        mappingIterator.nextValue();
        try {
            mappingIterator.nextValue();
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // This is the expected outcome
        }
        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValueIOException() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();

        jsonParser.setEncoding(null);
        mappingIterator.nextValue();

        try {
            mappingIterator.nextValue();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // This is the expected outcome
        }
        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValueNullInput() throws Exception {
        JsonParser jsonParserNull = null;
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParserNull, deserializationContext,
                StringDeserializer.instance, true, null);

        assertFalse(mappingIterator.hasNextValue());
        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValueEndArray() throws Exception {
        MappingIterator<String> mappingIterator = new MappingIterator<>(
                objectMapper.getTypeFactory().constructType(List.class), jsonParser, deserializationContext,
                StringDeserializer.instance, true, null);

        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();
        mappingIterator.nextValue();

        mappingIterator.nextValue();
        mappingIterator.nextValue(); // Should consume START_ARRAY token
        jsonParser.nextToken(); // Should consume END_ARRAY token
        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    private static class StringDeserializer extends JsonDeserializer<String> {

        public static final StringDeserializer instance = new StringDeserializer();

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public void deserialize(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
            p.setValue(bean, p.getText());
        }
    }

    private static class StringDeserializationError extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            throw new JsonMappingException("Error deserialization");
        }

        @Override
        public void deserialize(JsonParser p, DeserializationContext ctxt, Object bean) throws IOException {
            throw new JsonMappingException("Error deserialization");
        }
    }
}