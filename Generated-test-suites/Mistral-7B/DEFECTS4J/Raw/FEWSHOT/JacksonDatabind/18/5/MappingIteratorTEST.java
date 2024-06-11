package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MappingIteratorTest {

    @Test
    public void testEmptyIterator() {
        MappingIterator<Object> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorState() {
        JavaType javaType = null;
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        JsonDeserializer<Object> jsonDeserializer = null;
        MappingIterator<Object> iterator = new MappingIterator<Object>(javaType, jsonParser, deserializationContext,
                jsonDeserializer, true, null);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator.close();

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testHasNextAndNext() {
        JavaType javaType = new TypeFactory().constructType(Object[].class);
        JsonParser jsonParser = new TestParser();
        DeserializationContext deserializationContext = new TestContext();
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<Object>(Object[].class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                p.nextToken();
                return new Object[0];
            }
        };
        MappingIterator<Object> iterator = new MappingIterator<Object>(javaType, jsonParser, deserializationContext,
                jsonDeserializer, true, null);

        assertTrue(iterator.hasNext());
        assertSame(new Object[0], iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(new Object[0], iterator.next());
        assertFalse(iterator.hasNext());

        iterator.close();

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testReadAll() throws IOException {
        JavaType javaType = new TypeFactory().constructType(Integer[].class);
        JsonParser jsonParser = new TestParser();
        DeserializationContext deserializationContext = new TestContext();
        JsonDeserializer<Integer> jsonDeserializer = new JsonDeserializer<Integer>(Integer.class) {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                p.nextInt();
                return 1;
            }
        };
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(javaType, jsonParser, deserializationContext,
                jsonDeserializer, true, null);

        List<Integer> expected = Arrays.asList(1, 1, 1);
        List<Integer> actual = iterator.readAll(new ArrayList<Integer>());

        assertEquals(expected, actual);
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        JavaType javaType = new TypeFactory().constructType(Integer[].class);
        JsonParser jsonParser = new TestParser();
        DeserializationContext deserializationContext = new TestContext();
        JsonDeserializer<Integer> jsonDeserializer = new JsonDeserializer<Integer>(Integer.class) {
            @Override
            public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                p.nextInt();
                return 1;
            }
        };
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(javaType, jsonParser, deserializationContext,
                jsonDeserializer, true, null);

        Collection<Integer> expected = Arrays.asList(1, 1, 1);
        Collection<Integer> actual = iterator.readAll(new ArrayList<Integer>());

        assertEquals(expected, actual);
    }
}

class TestParser extends JsonParserBase {
    @Override
    public JsonToken nextToken() throws IOException {
        return null;
    }
}

class TestContext extends DeserializationContext {
}