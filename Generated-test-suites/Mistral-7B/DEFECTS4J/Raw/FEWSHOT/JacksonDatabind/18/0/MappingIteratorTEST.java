package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        assertTrue(mappingIterator.hasNextValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue_withUpdatedValue() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        String updatedValue = "updatedValue";

        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, updatedValue);

        assertTrue(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue_withNullParser() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(null, null, null, null, false, null);
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testNextValue_withNullDeserializer() {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, null, false, null);
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testNextValue_withNullUpdatedValue() {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        String nextValue = mappingIterator.nextValue();
        assertNotNull(nextValue);
    }

    @Test
    public void testNextValue_withUpdatedValue() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        String updatedValue = "updatedValue";
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, updatedValue);

        String nextValue = mappingIterator.nextValue();
        assertSame(updatedValue, nextValue);
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        List<String> list = mappingIterator.readAll();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withListCapacity() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        ArrayList<String> list = new ArrayList<>(10);
        mappingIterator.readAll(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withCollectionCapacity() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        ArrayList<String> list = new ArrayList<>(10);
        mappingIterator.readAll(Collections.synchronizedList(list));
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withList() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        ArrayList<String> list = new ArrayList<>();
        mappingIterator.readAll(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withCollection() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        ArrayList<String> list = new ArrayList<>();
        mappingIterator.readAll(Collections.synchronizedList(list));
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withListAndCapacity() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        ArrayList<String> list = new ArrayList<>(10);
        mappingIterator.readAll(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withCollectionAndCapacity() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        ArrayList<String> list = new ArrayList<>(10);
        mappingIterator.readAll(Collections.synchronizedList(list));
        assertTrue(list.isEmpty());
    }

    @Test
    public void testReadAll_withNullIterator() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());

        try {
            MappingIterator<Object> mappingIterator = null;
            mappingIterator.readAll(list);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadAll_withNullList() {
        MappingIterator<Object> mappingIterator = new MappingIterator<>(new SimpleType("String").withoutPropertyOrder(), null, null, null, false, null);

        try {
            mappingIterator.readAll(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testClose() throws IOException {
        JavaType javaType = new SimpleType("String").withoutPropertyOrder();
        JsonParser jsonParser = createMockParser();
        DeserializationContext deserializationContext = createMockContext();
        JsonDeserializer<String> jsonDeserializer = createMockDeserializer();
        MappingIterator<String> mappingIterator = new MappingIterator<>(javaType, jsonParser, deserializationContext, jsonDeserializer, true, null);

        mappingIterator.close();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testCloseParser() throws IOException {
        JsonParser jsonParser = createMockParser();
        jsonParser.nextToken();
        jsonParser.close();
    }

    private JsonParser createMockParser() {
        return createMock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return createMock(DeserializationContext.class);
    }

    private JsonDeserializer<String> createMockDeserializer() {
        return createMock(JsonDeserializer.class);
    }
}