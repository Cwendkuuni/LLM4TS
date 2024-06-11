package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for {@link StringArrayDeserializer}.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    @Test
    public void testEmptyArray() throws Exception {
        String[] result = MAPPER.readValue("[]", String[].class);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws Exception {
        String[] result = MAPPER.readValue("['foo']", String[].class);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        String[] result = MAPPER.readValue("['foo']", String[].class, new CustomStringDeserializer());
        assertEquals("foo", result[0]);
    }

    @Test
    public void testNullElement() throws Exception {
        String[] result = MAPPER.readValue("[null]", String[].class);
        assertNull(result[0]);
    }

    @Test
    public void testNullArray() throws Exception {
        String[] result = MAPPER.readValue("null", String[].class);
        assertNull(result);
    }

    @Test
    public void testEmptyString() throws Exception {
        String[] result = MAPPER.readValue("[\"\"]", String[].class);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testMultipleElements() throws Exception {
        String[] result = MAPPER.readValue("['foo', 'bar']", String[].class);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testCustomDeserializerWithContext() throws Exception {
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        String[] result = MAPPER.readValue("['foo']", String[].class, new StringArrayDeserializer(customDeserializer));
        assertEquals("BAR", result[0]);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws Exception {
        MAPPER.readValue("'foo'", String[].class);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return ctxt.readValue(jp, String[].class);
            }
        };
        String[] result = MAPPER.readValue(MAPPER.createParser("['foo']"), String[].class, typeDeserializer);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jp = MAPPER.getFactory().createParser("'foo'");
        String[] result = MAPPER.findContextualValueDeserializer(String[].class, null).deserialize(jp, null);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testContextualization() throws Exception {
        String[] result = MAPPER.readValue("['foo']", String[].class, new ObjectMapper.DefaultTyping(String[].class));
        assertEquals("foo", result[0]);
    }

    static class CustomStringDeserializer extends StdDeserializer<String> {
        CustomStringDeserializer() {
            this(null);
        }

        CustomStringDeserializer(JsonDeserializer<String> deser) {
            super(String.class);
            _elementDeserializer = deser;
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _elementDeserializer != null ? _elementDeserializer.deserialize(jp, ctxt) : jp.getText();
        }

        JsonDeserializer<String> _elementDeserializer;
    }
}