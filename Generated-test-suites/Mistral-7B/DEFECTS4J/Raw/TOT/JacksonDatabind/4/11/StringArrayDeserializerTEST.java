package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        // Test empty array
        JsonParser jp = MAPPER.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeCustomDeserializer() throws JsonProcessingException {
        // Test custom deserializer
        String[] expected = {"apple", "banana"};
        JsonParser jp = MAPPER.getFactory().createParser("[\"apple\", \"banana\"]");
        ObjectDeserializer customDeserializer = new BasicValueDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeNullArray() throws JsonProcessingException {
        // Test null array
        JsonParser jp = MAPPER.getFactory().createParser("null");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws JsonProcessingException {
        // Test empty string as null
        JsonParser jp = MAPPER.getFactory().createParser("[]");
        MAPPER.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleValueAsArray() throws JsonProcessingException {
        // Test single value as array
        JsonParser jp = MAPPER.getFactory().createParser("\"apple\"");
        MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertArrayEquals(new String[]{"apple"}, result);
    }

    @Test
    public void testDeserializeCustomContentConverter() throws JsonProcessingException {
        // Test custom content converter
        String[] expected = {"hex1", "hex2"};
        JsonParser jp = MAPPER.getFactory().createParser("[\".hex1.\", \".hex2.\"]");
        MAPPER.registerModule(new Base64Module(Base64Variants.STANDARD));
        JsonDeserializer<String> contentConverter = new Base64Deserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.setContentConverter(contentConverter);
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeNullElement() throws JsonProcessingException {
        // Test null element
        JsonParser jp = MAPPER.getFactory().createParser("[null, \"apple\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, new TestDeserializationContext());
        assertArrayEquals(new String[]{"null", "apple"}, result);
    }

    @Test
    public void testDeserializeCustomDeserializerWithTypeId() throws JsonProcessingException {
        // Test custom deserializer with type id
        String[] expected = {"apple", "banana"};
        JsonParser jp = MAPPER.getFactory().createParser("[\".apple.\", \".banana.\"]");
        ObjectDeserializer customDeserializer = new BasicValueDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        deserializer.setContentTypeId("customTypeId");
        String[] result = deserializer.deserializeWithType(jp, new TestDeserializationContext(), MAPPER.getTypeFactory().constructType("java.lang.String[]"));
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws JsonProcessingException {
        // Test invalid format
        JsonParser jp = MAPPER.getFactory().createParser("{ \"apple\": \"banana\" }");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestDeserializationContext());
    }

    /**
     * Test deserialization context used in tests.
     */
    static class TestDeserializationContext extends DeserializationContext {
        TestDeserializationContext() {
            super(null, null, null, null);
        }

        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer(10);
        }

        @Override
        public void returnObjectBuffer(ObjectBuffer buffer) {
            // No-op
        }
    }
}