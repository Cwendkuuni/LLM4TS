package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeatures(JsonParser.Feature.AUTO_CLOSE_SOURCE)
                .withoutFeatures(JsonParser.Feature.AUTO_CLOSE_TARGET)
                .build();
        objectMapper = new ObjectMapper(jsonFactory);
    }

    @Test
    public void testEmptyIterator() {
        Iterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNextThrowsExceptionWhenParserIsNull() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertFalse(iterator.hasNext());
        try {
            iterator.hasNextValue();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testNext() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        Object next = iterator.next();
        assertNull(next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNotNull(next);
    }

    @Test
    public void testNextThrowsExceptionWhenParserIsNull() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertNull(iterator.next());
        try {
            iterator.nextValue();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testRemoveUnsupportedOperation() {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testClose() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, true, null);
        iterator.close();
        assertFalse(iterator.hasNext());
        jsonParser.close();
    }

    @Test
    public void testHasNextValueThrowsExceptionWhenParserIsNull() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        assertFalse(iterator.hasNextValue());
        try {
            iterator.hasNextValue();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testNextValueReturnsNullWhenNoNextValue() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        Object nextValue = iterator.nextValue();
        assertNull(nextValue);
    }

    @Test
    public void testNextValueReturnsValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactoryBuilder()
                .withoutFeatures(JsonParser.Feature.AUTO_CLOSE_SOURCE)
                .withoutFeatures(JsonParser.Feature.AUTO_CLOSE_TARGET)
                .build();
        JsonParser jsonParser = jsonFactory.createParser("[]");
        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory()
                .constructTypeDeserializer(List.class, Collections.emptyList(), null);
        MappingIterator<List<Integer>> iterator = new MappingIterator<List<Integer>>(
                objectMapper.getTypeFactory().constructType(List.class, Integer.class), jsonParser,
                objectMapper.getDeserializationContext(), typeDeserializer, false, null);
        Object nextValue = iterator.nextValue();
        assertTrue(nextValue instanceof List);
        List<Integer> list = (List<Integer>) nextValue;
        assertEquals(0, list.size());
        nextValue = iterator.nextValue();
        List<Integer> list2 = (List<Integer>) nextValue;
        assertEquals(1, list2.size());
        assertEquals(0, list2.get(0).intValue());
    }

    @Test
    public void testHasNextValueThrowsMappingException() throws IOException, JsonMappingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        JsonMappingException exception = new JsonMappingException("Test exception");
        iterator._parser = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                throw exception;
            }
        }.getDeserializationContext().getUnderlyingObjectWriter()
                .writeValue(iterator._parser, exception);
        assertFalse(iterator.hasNextValue());
        assertEquals("Test exception", iterator.getParser().getText());
    }

    @Test
    public void testHasNextValueThrowsIOException() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        JsonProcessingException exception = new JsonProcessingException("Test exception");
        iterator._parser = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                throw exception;
            }
        }.getDeserializationContext().getUnderlyingObjectWriter()
                .writeValue(iterator._parser, exception);
        assertFalse(iterator.hasNextValue());
        assertEquals("Test exception", iterator.getParser().getText());
    }

    @Test
    public void testReadAllThrowsExceptionWhenParserIsNull() {
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, null, null, null, false, null);
        try {
            iterator.readAll();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadAllReturnsEmptyList() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        List<Object> list = iterator.readAll();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAllReturnsListWithOneElement() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        List<Object> list = iterator.readAll();
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).intValue());
    }

    @Test
    public void testReadAllReturnsListWithMultipleElements() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2, 3]");
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, jsonParser, null, null, false, null);
        List<Object> list = iterator.readAll();
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).intValue());
        assertEquals(2, list.get(1).intValue());
        assertEquals(3, list.get(2).intValue());
    }

    @Test
    public void testReadAllThrowsMappingException() throws IOException, JsonMappingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2]");
        JsonMappingException exception = new JsonMappingException("Test exception");
        JsonParser parser = jsonParser;
        JsonParser nextParser = parser;
        do {
            nextParser = parser.nextParsedValue();
            parser = nextParser.getNextValueParser();
        } while (nextParser != null);
        parser = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                throw exception;
            }
        }.getDeserializationContext().getUnderlyingObjectWriter()
                .writeValue(parser, exception);
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, false, null);
        try {
            iterator.readAll();
            fail();
        } catch (IOException e) {
            // expected
        }
        assertEquals("Test exception", parser.getText());
    }

    @Test
    public void testReadAllThrowsIOException() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2]");
        JsonProcessingException exception = new JsonProcessingException("Test exception");
        JsonParser parser = jsonParser;
        JsonParser nextParser = parser;
        do {
            nextParser = parser.nextParsedValue();
            parser = nextParser.getNextValueParser();
        } while (nextParser != null);
        parser = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                throw exception;
            }
        }.getDeserializationContext().getUnderlyingObjectWriter()
                .writeValue(parser, exception);
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, false, null);
        try {
            iterator.readAll();
            fail();
        } catch (IOException e) {
            // expected
        }
        assertEquals("Test exception", parser.getText());
    }

    @Test
    public void testReadAllThrowsMappingExceptionAndIOException() throws IOException, JsonMappingException, JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("[1, 2]");
        JsonMappingException mappingException = new JsonMappingException("Test mapping exception");
        JsonProcessingException processingException = new JsonProcessingException("Test processing exception");
        JsonParser parser = jsonParser;
        JsonParser nextParser = parser;
        do {
            nextParser = parser.nextParsedValue();
            parser = nextParser.getNextValueParser();
        } while (nextParser != null && nextParser.isExpectedStartArrayToken());
        if (nextParser != null) {
            nextParser.nextToken();
        }
        parser = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                throw mappingException;
            }
        }.getDeserializationContext().getUnderlyingObjectWriter()
                .writeValue(parser, processingException);
        MappingIterator<Object> iterator = new MappingIterator<Object>(null, parser, null, null, false, null);
        try {
            iterator.readAll();
            fail();
        } catch (IOException e) {
            // expected
        }
        assertEquals("Test processing exception", parser.getText());
    }
}