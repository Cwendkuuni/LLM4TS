package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setup() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize_emptyArray() throws IOException {
        JsonParser jp = TestUtils.createJsonParser("[]");
        DeserializationContext ctxt = JsonUtils.createDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_withStrings() throws IOException {
        JsonParser jp = TestUtils.createJsonParser("[\"string1\", \"string2\", \"string3\"]");
        DeserializationContext ctxt = JsonUtils.createDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("string1", result[0]);
        assertEquals("string2", result[1]);
        assertEquals("string3", result[2]);
    }

    @Test
    public void testDeserialize_withNulls() throws IOException {
        JsonParser jp = TestUtils.createJsonParser("[null, null, null]");
        DeserializationContext ctxt = JsonUtils.createDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testDeserialize_withMixedValues() throws IOException {
        JsonParser jp = TestUtils.createJsonParser("[\"string1\", null, \"string2\"]");
        DeserializationContext ctxt = JsonUtils.createDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("string1", result[0]);
        assertNull(result[1]);
        assertEquals("string2", result[2]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = TestUtils.createJsonParser("[\"string1\", \"string2\", \"string3\"]");
        DeserializationContext ctxt = JsonUtils.createDeserializationContext();
        TypeDeserializer typeDeserializer = TestUtils.createTypeDeserializer(ctxt);
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("string1", arrayResult[0]);
        assertEquals("string2", arrayResult[1]);
        assertEquals("string3", arrayResult[2]);
    }

    @Test
    public void testCreateContextual_defaultDeser() throws IOException {
        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(JsonUtils.createDeserializationContext(), null);
        assertNull(deserializer);
    }

    @Test
    public void testCreateContextual_customDeser() throws IOException {
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        JsonDeserializer<?> deserializer = stringArrayDeserializer.createContextual(JsonUtils.createDeserializationContext(), null);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StringArrayDeserializer);
        StringArrayDeserializer arrayDeserializer = (StringArrayDeserializer) deserializer;
        assertNotEquals(stringArrayDeserializer, arrayDeserializer);
        assertEquals(customDeserializer, arrayDeserializer.getElementDeserializer());
    }
}