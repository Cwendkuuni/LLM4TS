package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JavaType type = createMockType();
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        assertTrue(iterator.hasNext());
        parser.expectNext(JsonToken.VALUE_STRING);
        // Should not throw an exception
        iterator.hasNext();

        // Should throw an exception
        parser.expectNext(JsonToken.START_OBJECT);
        try {
            iterator.hasNext();
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testHasNextValue() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JavaType type = createMockType();
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        assertTrue(iterator.hasNextValue());
        parser.expectNext(JsonToken.VALUE_STRING);
        // Should not throw an exception
        iterator.hasNextValue();

        // Should throw an exception
        parser.expectNext(JsonToken.START_ARRAY);
        try {
            iterator.hasNextValue();
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testNextValue() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JavaType type = createMockType();
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        Object updatedValue = new Object();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, context, deserializer, true, updatedValue);

        Object value = iterator.nextValue();
        assertSame(updatedValue, value);

        parser.expectNext(JsonToken.VALUE_STRING);
        value = iterator.nextValue();
        assertNotNull(value);
    }

    @Test
    public void testReadAll() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JavaType type = createMockType();
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        List<Object> resultList = new ArrayList<>();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.readAll(resultList);
        assertEquals(5, resultList.size());
        for (Object obj : resultList) {
            assertNotNull(obj);
        }

        parser.expectNext(JsonToken.START_ARRAY);
        try {
            iterator.readAll(resultList);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadAllCollection() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JavaType type = createMockType();
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        Collection<Object> resultList = new ArrayList<>();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.readAll(resultList);
        assertEquals(5, resultList.size());
        for (Object obj : resultList) {
            assertNotNull(obj);
        }

        parser.expectNext(JsonToken.START_ARRAY);
        try {
            iterator.readAll(resultList);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCloseParser() throws IOException {
        JsonParser parser = createMockParser();
        DeserializationContext context = createMockContext();
        JavaType type = createMockType();
        JsonDeserializer<Object> deserializer = createMockDeserializer();
        MappingIterator<Object> iterator = new MappingIterator<>(type, parser, context, deserializer, true, null);

        iterator.close();
        parser.expectClosed();
    }

    private JsonParser createMockParser() {
        return createMock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return createMock(DeserializationContext.class);
    }

    private JavaType createMockType() {
        return createMock(JavaType.class);
    }

    private JsonDeserializer<Object> createMockDeserializer() {
        return createMock(JsonDeserializer.class);
    }
}