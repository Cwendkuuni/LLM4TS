package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    /**
     * Test deserialization of empty JSON array.
     */
    @Test
    public void testEmptyArray() throws Exception {
        JsonParser jp = createParser("[]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertEquals(new String[0], deserializer.deserialize(jp, new TestContext()));
    }

    /**
     * Test deserialization of JSON array with one element.
     */
    @Test
    public void testSingleElement() throws Exception {
        JsonParser jp = createParser("[\"foo\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertEquals(new String[]{"foo"}, deserializer.deserialize(jp, new TestContext()));
    }

    /**
     * Test deserialization of JSON array with multiple elements.
     */
    @Test
    public void testMultipleElements() throws Exception {
        JsonParser jp = createParser("[\"foo\", \"bar\", null]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertEquals(new String[]{"foo", "bar", null}, deserializer.deserialize(jp, new TestContext()));
    }

    /**
     * Test deserialization of JSON array with custom element deserializer.
     */
    @Test
    public void testCustomElementDeserializer() throws Exception {
        JsonParser jp = createParser("[{\"name\": \"foo\"}]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new ObjectNameDeserializer());
        assertEquals(new String[]{"foo"}, deserializer.deserialize(jp, new TestContext()));
    }

    /**
     * Test handling of non-array input.
     */
    @Test(expected = JsonMappingException.class)
    public void testNonArrayInput() throws Exception {
        JsonParser jp = createParser("{\"name\": \"foo\"}");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jp, new TestContext());
    }

    /**
     * Test deserialization with type handler.
     */
    @Test
    public void testTypeHandler() throws Exception {
        JsonParser jp = createParser("[1, 2, 3]"); // not a String array
        TypeDeserializer typeDeserializer = new StandardDeserializer<>(Integer[].class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertEquals(new String[]{"1", "2", "3"}, deserializer.deserializeWithType(jp, new TestContext(), typeDeserializer));
    }

    /**
     * Test contextual deserialization.
     */
    @Test
    public void testContextualDeserialization() throws Exception {
        JsonParser jp = createParser("[\"foo\", null]");
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.deserializationConfig().findValueDeserializer(String.class, null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        assertEquals(new String[]{"foo", null}, deserializer.deserialize(jp, new TestContext()));
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leakAndClearBuffer() {
            return null;
        }

        @Override
        public Object findValueDeserializer(Class<?> valueType, PropertyName propertyName) {
            return null;
        }

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(Class<?> type, PropertyName property)
                throws JsonMappingException {
            return null;
        }

        @Override
        public Object instantiateValueHandler(Class<?> type) throws InstantiationException, IllegalAccessException {
            return null;
        }
    }
}