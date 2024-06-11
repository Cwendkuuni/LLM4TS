package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer deserializer = new StringArrayDeserializer();

    @Test
    public void testDeserialize() throws Exception {
        // Null
        JsonParser jp = TestUtils.createJsonParser("null");
        DeserializationContext ctxt = TestUtils.createDeserializationContext();
        assertNull(deserializer.deserialize(jp, ctxt));

        // Empty array
        jp = TestUtils.createJsonParser("[]");
        String[] result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);

        // Array with values
        jp = TestUtils.createJsonParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(jp, ctxt);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = TestUtils.createJsonParser("[]");
        DeserializationContext ctxt = TestUtils.createDeserializationContext();
        TypeDeserializer typeDeserializer = TestUtils.createTypeDeserializer(JavaType.builder().build());
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertNotNull(result);
        assertEquals(String[].class, result.getClass());
        assertEquals(0, ((String[]) result).length);
    }

    @Test
    public void test_deserializeCustom() throws Exception {
        // Null
        JsonParser jp = TestUtils.createJsonParser("null");
        DeserializationContext ctxt = TestUtils.createDeserializationContext();
        JsonDeserializer<String> elementDeserializer = TestUtils.createJsonDeserializer(String.class);
        deserializer._elementDeserializer = elementDeserializer;
        assertNull(deserializer._deserializeCustom(jp, ctxt));

        // Empty array
        jp = TestUtils.createJsonParser("[]");
        elementDeserializer = TestUtils.createJsonDeserializer(String.class);
        deserializer._elementDeserializer = elementDeserializer;
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        assertNotNull(result);
        assertEquals(0, result.length);

        // Array with values
        jp = TestUtils.createJsonParser("[\"foo\",\"bar\"]");
        elementDeserializer = TestUtils.createJsonDeserializer(String.class);
        deserializer._elementDeserializer = elementDeserializer;
        result = deserializer._deserializeCustom(jp, ctxt);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        // Single value
        JsonParser jp = TestUtils.createJsonParser("\"foo\"");
        DeserializationContext ctxt = TestUtils.createDeserializationContext();
        assertNull(deserializer.handleNonArray(jp, ctxt));

        // Single value as null
        jp = TestUtils.createJsonParser("null");
        assertNull(deserializer.handleNonArray(jp, ctxt));

        // Not accepting single value as array
        jp = TestUtils.createJsonParser("\"foo\"");
        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        try {
            deserializer.handleNonArray(jp, ctxt);
        } catch (JsonMappingException e) {
            assertEquals(String[].class, e.getMessage());
        }
    }

    @Test
    public void testCreateContextual() throws Exception {
        JavaType javaType = JavaType.builder().build();
        JsonDeserializer<?> deser = deserializer.createContextual(TestUtils.createDeserializationContext(), null);
        assertNull(deser);

        JsonDeserializer<String> elementDeserializer = TestUtils.createJsonDeserializer(String.class);
        deserializer._elementDeserializer = elementDeserializer;

        deser = deserializer.createContextual(TestUtils.createDeserializationContext(), null);
        assertNotNull(deser);
        assertEquals(StringArrayDeserializer.class, deser.getClass());
        assertEquals(elementDeserializer, ((StringArrayDeserializer) deser)._elementDeserializer);
    }
}