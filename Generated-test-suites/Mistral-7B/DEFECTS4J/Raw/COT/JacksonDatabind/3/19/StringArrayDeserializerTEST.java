package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final StringArrayDeserializer serializer = StringArrayDeserializer.instance;

    @SuppressWarnings("unchecked")
    @org.junit.BeforeClass
    public static void setUp() {
        // Initialize ObjectMapper for testing
        ObjectMapper mapper = new ObjectMapper();
    }

    // Test for deserialize method
    @org.junit.Test
    public void testDeserialize() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("[\"hello\",\"world\"]");
        String[] result = serializer.deserialize(jp, new TestingDeserializationContext());
        assertEquals(new String[]{"hello", "world"}, result);
    }

    // Test for _deserializeCustom method
    @org.junit.Test
    public void testDeserializeCustom() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("[\"hello\",\"world\"]");
        String[] result = serializer._deserializeCustom(jp, new TestingDeserializationContext());
        assertEquals(new String[]{"hello", "world"}, result);
    }

    // Test for deserializeWithType method
    @org.junit.Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("[\"String\",\"Array\"]");
        String[] result = serializer.deserializeWithType(jp, new TestingDeserializationContext(), null);
        assertArrayEquals(new String[]{"String", "Array"}, result);
    }

    // Test for handleNonArray method
    @org.junit.Test
    public void testHandleNonArrayNull() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("null");
        String[] result = serializer.handleNonArray(jp, new TestingDeserializationContext());
        assertNull(result);
    }

    // Test for handleNonArray single value
    @org.junit.Test
    public void testHandleNonArraySingleValue() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("\"hello\"");
        String[] result = serializer.handleNonArray(jp, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"hello"}, result);
    }

    // Test for handleNonArray non-String value
    @org.junit.Test
    public void testHandleNonArrayNonStringValue() throws Exception {
        JsonParser jp = mapper.getFactory().createParser(JsonFactory.valueToTree("123"));
        String[] result = serializer.handleNonArray(jp, new TestingDeserializationContext());
        assertArrayEquals(new String[]{"123"}, result);
    }

    // Test for createContextual method
    @org.junit.Test
    public void testCreateContextual() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("[\"hello\",\"world\"]");
        JsonDeserializer<String> elementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText();
            }
        };
        JsonDeserializer<String[]> result = serializer.createContextual(new TestingDeserializationContext(), null);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(elementDeserializer, ((StringArrayDeserializer) result)._elementDeserializer);
    }

    // Test for isDefaultDeserializer method
    @org.junit.Test
    public void testIsDefaultDeserializer() {
        JsonDeserializer<String> defaultDeserializer = new StdDeserializer<String>(String.class);
        assertTrue(serializer.isDefaultDeserializer(defaultDeserializer));
    }

    // Test for findConvertingContentDeserializer method
    @org.junit.Test
    public void testFindConvertingContentDeserializer() throws Exception {
        JsonParser jp = mapper.getFactory().createParser("[1, 2, 3]");
        JsonDeserializer<List<Integer>> convertingDeserializer = new ArrayContentDeserializer();
        JsonDeserializer<String[]> result = serializer.createContextual(new TestingDeserializationContext(), null);
        assertEquals(convertingDeserializer, result.findConvertingContentDeserializer(new TestingDeserializationContext(), null, String[].class));
    }

    private static class TestingDeserializationContext extends BaseDeserializerProvider.BaseContextualDeserializerProvider {
        @Override
        public JsonDeserializer<?> findContentDeserializer(DeserializationContext ctxt, BeanProperty property, Type deserType) throws JsonMappingException, JsonProcessingException {
            return null;
        }
    }

    private static class ArrayContentDeserializer extends StdDeserializer<List<Integer>> {
        @Override
        public List<Integer> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            while ((jp.nextToken()) != JsonToken.END_ARRAY) {
                int value = jp.getIntValue();
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }
            List<Integer> result = buffer.completeAndClearBuffer(chunk, ix);
            ctxt.returnObjectBuffer(buffer);
            return result;
        }
    }
}