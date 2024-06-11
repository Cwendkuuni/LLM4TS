package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

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

    private static final StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

    @Test
    public void testDeserializeNullElementDeserializer() throws IOException {
        JsonParser jsonParser = createJsonParser("[]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = createJsonParser("[]");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElement() throws IOException {
        JsonParser jsonParser = createJsonParser("['abc']");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertEquals("abc", result[0]);
    }

    @Test
    public void testDeserializeMultipleElements() throws IOException {
        JsonParser jsonParser = createJsonParser("['abc', 'def']");
        String[] result = deserializer.deserialize(jsonParser, new TestContext());
        assertArrayEquals(new String[]{"abc", "def"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser jsonParser = createJsonParser("{InvalidJson}");
        deserializer.deserialize(jsonParser, new TestContext());
    }

    @Test
    public void testDeserializeCustomElementDeserializerNullValue() throws IOException {
        StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(null);
        JsonParser jsonParser = createJsonParser("['null']");
        String[] result = deserializerWithCustomElementDeserializer.deserialize(jsonParser, new TestContext());
        assertNull(result[0]);
    }

    @Test
    public void testDeserializeCustomElementDeserializerStringValue() throws IOException {
        String elementDeserializer = "custom.StringDeserializer";
        StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(new ClassNameDeserializer(elementDeserializer));
        JsonParser jsonParser = createJsonParser("['abc']");
        String[] result = deserializerWithCustomElementDeserializer.deserialize(jsonParser, new TestContext());
        assertEquals("abc", result[0]);
    }

    @Test(expected = IOException.class)
    public void testDeserializeCustomElementDeserializerInvalidJson() throws IOException {
        String elementDeserializer = "custom.InvalidDeserializer";
        StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(new ClassNameDeserializer(elementDeserializer));
        JsonParser jsonParser = createJsonParser("{InvalidJson}");
        deserializerWithCustomElementDeserializer.deserialize(jsonParser, new TestContext());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = createJsonParser("['abc', 'def']");
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, new TestContext())).thenReturn(new String[]{"abc", "def"});
        Object result = deserializer.deserializeWithType(jsonParser, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[]{"abc", "def"}, result);
    }

    @Test
    public void testHandleNonArraySingleValue() throws IOException {
        JsonParser jsonParser = createJsonParser("'abc'");
        String[] result = deserializer.handleNonArray(jsonParser, new TestContext());
        assertEquals("abc", result[0]);
    }

    @Test
    public void testHandleNonArrayEmptyString() throws IOException {
        JsonParser jsonParser = createJsonParser("''");
        String[] result = deserializer.handleNonArray(jsonParser, new TestContext());
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayInvalidJson() throws IOException {
        JsonParser jsonParser = createJsonParser("{InvalidJson}");
        deserializer.handleNonArray(jsonParser, new TestContext());
    }

    @Test
    public void testCreateContextualNullElementDeserializer() {
        JsonDeserializer<String> nullElementDeserializer = null;
        JsonDeserializer<?> result = deserializer.createContextual(new TestContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(deserializer, result.getClassInstance());
        assertNull(result.getContentDeserializer());
    }

    @Test
    public void testCreateContextualCustomElementDeserializer() {
        String elementDeserializer = "custom.StringDeserializer";
        JsonDeserializer<String> customElementDeserializer = mock(JsonDeserializer.class);
        when(customElementDeserializer.getClass()).thenReturn(Class.forName(elementDeserializer));
        JsonDeserializer<?> result = deserializer.createContextual(new TestContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(deserializer, result.getClassInstance());
        assertEquals(customElementDeserializer, result.getContentDeserializer());
    }

    @Test
    public void testCreateContextualDefaultStringDeserializer() {
        JsonDeserializer<String> defaultStringDeserializer = new StandardSerializer<>(String.class);
        JsonDeserializer<?> result = deserializer.createContextual(new TestContext(), null);
        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(deserializer, result.getClassInstance());
        assertNull(result.getContentDeserializer());
    }

    private JsonParser createJsonParser(String json) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createParser(json);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer();
        }
    }

    private static class ClassNameDeserializer extends JsonDeserializer<Object> {
        private final String className;

        public ClassNameDeserializer(String className) {
            this.className = className;
        }

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            try {
                Class<?> clazz = Class.forName(className);
                return ctxt.findValueDeserializer(clazz);
            } catch (ClassNotFoundException e) {
                throw ctxt.mappingException(String.format("Could not find class: %s", className));
            }
        }
    }
}