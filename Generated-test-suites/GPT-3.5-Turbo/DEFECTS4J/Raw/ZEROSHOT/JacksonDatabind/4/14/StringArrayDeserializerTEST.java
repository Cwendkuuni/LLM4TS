package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("[\"hello\", \"world\"]");
        DeserializationContext deserializationContext = new DefaultDeserializationContext(stringArrayDeserializer, null, null);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(2, result.length);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("[\"hello\", null, \"world\"]");
        DeserializationContext deserializationContext = new DefaultDeserializationContext(stringArrayDeserializer, null, null);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(3, result.length);
        assertEquals("hello", result[0]);
        assertNull(result[1]);
        assertEquals("world", result[2]);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("[]");
        DeserializationContext deserializationContext = new DefaultDeserializationContext(stringArrayDeserializer, null, null);
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithNonArray() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("\"hello\"");
        DeserializationContext deserializationContext = new DefaultDeserializationContext(stringArrayDeserializer, null, null);
        try {
            stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
            assertEquals("Unexpected token: VALUE_STRING, expected: START_ARRAY\n at [Source: (String)\"hello\"; line: 1, column: 1]", e.getMessage());
        }
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = new JsonFactory().createParser("[\"hello\", \"world\"]");
        DeserializationContext deserializationContext = new DefaultDeserializationContext(stringArrayDeserializer, null, null);
        DummyTypeDeserializer typeDeserializer = new DummyTypeDeserializer();
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    private class DummyTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}