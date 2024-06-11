package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jp = MAPPER.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, null);
        assertNull(result);

        jp = MAPPER.getFactory().createParser("[\"test1\", \"test2\"]");
        result = deserializer.deserialize(jp, null);
        assertArrayEquals(new String[]{"test1", "test2"}, result);

        jp = MAPPER.getFactory().createParser("[null, \"test2\", null]");
        result = deserializer.deserialize(jp, null);
        assertArrayEquals(new String[]{null, "test2", null}, result);

        jp = MAPPER.getFactory().createParser("[1, 2, 3]"); // edge case: non-string elements
        try {
            deserializer.deserialize(jp, null);
            fail();
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jp = MAPPER.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        String[] result = deserializer.deserialize(jp, null);
        assertNull(result);

        jp = MAPPER.getFactory().createParser("[\"test1\", \"test2\"]");
        result = deserializer.deserialize(jp, null);
        assertArrayEquals(new String[]{"test1", "test2"}, result);

        jp = MAPPER.getFactory().createParser("[null, \"test2\", null]");
        result = deserializer.deserialize(jp, null);
        assertArrayEquals(new String[]{null, "test2", null}, result);

        JsonDeserializer<String> elementDeserializer = MAPPER.findValueDeserializer(String.class, null);
        deserializer = new StringArrayDeserializer(elementDeserializer);
        jp = MAPPER.getFactory().createParser("[\"test1\", \"test2\"]");
        result = deserializer.deserialize(jp, null);
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = MAPPER.getFactory().createParser("[]");
        Object result = StringArrayDeserializer.instance.deserializeWithType(jp, null, MAPPER.getTypeFactory().constructType(String[].class));
        assertNull(result);

        jp = MAPPER.getFactory().createParser("[\"test1\", \"test2\"]");
        result = StringArrayDeserializer.instance.deserializeWithType(jp, null, MAPPER.getTypeFactory().constructType(String[].class));
        assertArrayEquals(new String[]{"test1", "test2"}, (String[])result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jp = MAPPER.getFactory().createParser("\"test\"");
        deserializer.handleNonArray(jp, null);
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        BeanProperty property = new StdName("propertyName");
        JsonDeserializer<String> elementDeserializer = MAPPER.findValueDeserializer(String.class, property);
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        StringArrayDeserializer deserializer2 = (StringArrayDeserializer) result;
        assertEquals(elementDeserializer, deserializer2._elementDeserializer);
    }
}