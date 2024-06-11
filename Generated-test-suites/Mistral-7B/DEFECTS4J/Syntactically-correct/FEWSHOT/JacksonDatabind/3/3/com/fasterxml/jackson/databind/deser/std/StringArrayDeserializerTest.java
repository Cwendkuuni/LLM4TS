package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectBuffer buffer = new ObjectBuffer(10);

    @Test
    public void testDeserializeNullElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertArrayEquals(new String[0], deserializer.deserialize(null, new TestContext(), null));
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = createContext(new byte[0]);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertArrayEquals(new String[0], deserializer.deserialize(jsonParser, new TestContext(), null));
    }

    @Test
    public void testDeserializeEmptyArrayWithElementDeserializer() throws IOException {
        JsonParser jsonParser = createContext("[]");
        StringDeserializer stringDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        assertArrayEquals(new String[0], deserializer.deserialize(jsonParser, new TestContext(), null));
    }

    @Test
    public void testDeserializeArrayWithSingleElement() throws IOException {
        JsonParser jsonParser = createContext("[\"foo\"]");
        StringDeserializer stringDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = deserializer.deserialize(jsonParser, new TestContext(), null);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testDeserializeArrayWithMultipleElements() throws IOException {
        JsonParser jsonParser = createContext("[\"foo\",\"bar\",null]");
        StringDeserializer stringDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        String[] result = deserializer.deserialize(jsonParser, new TestContext(), null);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testDeserializeArrayWithCustomDeserializer() throws IOException {
        JsonParser jsonParser = createContext("[null,null]");
        CustomStringDeserializer customStringDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customStringDeserializer);
        String[] result = deserializer.deserialize(jsonParser, new TestContext(), null);
        assertArrayEquals(new String[]{"null", "null"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser jsonParser = createContext("{");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jsonParser, new TestContext(), null);
    }

    @Test
    public void testDeserializeWithTypeHandler() throws IOException {
        JsonParser jsonParser = createContext("[\"foo\",\"bar\"]");
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                String[] result = new StringArrayDeserializer().deserialize(p, ctxt, null);
                return result;
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(jsonParser, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{"foo", "bar"}, (String[]) result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonParser jsonParser = createContext("[\"foo\",\"bar\"]");
        StringDeserializer stringDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(new TestContext(), null);
        assertEquals(StringArrayDeserializer.class, contextualDeserializer.getClass());
        JsonDeserializer<?> contextualDeserializerWithElementDeserializer = deserializer.createContextual(new TestContext(), null);
        assertEquals(StringArrayDeserializer.class, contextualDeserializerWithElementDeserializer.getClass());
        JsonDeserializer<?> contextualDeserializerWithCustomDeserializer = deserializer.createContextual(new TestContext(), null);
        assertEquals(StringArrayDeserializer.class, contextualDeserializerWithCustomDeserializer.getClass());
        JsonDeserializer<?> contextualDeserializerWithDefaultDeserializer = deserializer.createContextual(new TestContext(), null);
        assertEquals(StringArrayDeserializer.class, contextualDeserializerWithDefaultDeserializer.getClass());
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jsonParser = createContext("\"foo\"");
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jsonParser, new TestContext());
        assertEquals("foo", result[0]);
    }

    @Test
    public void testHandleNonArrayWithSingleValueAsArrayFeatureDisabled() throws IOException {
        JsonParser jsonParser = createContext("\"foo\"");
        TestContext context = new TestContext();
        context.setEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jsonParser, context);
        throw new AssertionError("Expected mapping exception.");
    }

    @Test
    public void testHandleNonArrayWithEmptyStringAsNullFeatureEnabled() throws IOException {
        JsonParser jsonParser = createContext("\"\"");
        TestContext context = new TestContext();
        context.setEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jsonParser, context);
        assertNull(result);
    }

    private static JsonParser createContext(String content) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(new StringReader(content));
        return jsonParser;
    }

    private static JsonParser createContext(byte[] content) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(content));
        return jsonParser;
    }

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return buffer;
        }
    }

    static class CustomStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "null";
        }
    }
}