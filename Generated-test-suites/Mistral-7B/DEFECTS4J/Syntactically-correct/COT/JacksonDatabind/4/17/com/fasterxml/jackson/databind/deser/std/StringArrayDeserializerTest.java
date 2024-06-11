package com.example.junit.test;

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
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    // Test for deserialize method with null _elementDeserializer
    @Test
    public void testDeserializeWithNullElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createParser("[]");
        String[] result = deserializer.deserialize(parser, new TestContext());
        assertArrayEquals(new String[0], result);
    }

    // Test for deserialize method with _elementDeserializer
    @Test
    public void testDeserializeWithElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        JsonParser parser = createParser("[]");
        String[] result = deserializer.deserialize(parser, new TestContext());
        assertArrayEquals(new String[0], result);

        String element = "test";
        parser = createParser("[\"" + element + "\"]");
        result = deserializer.deserialize(parser, new TestContext());
        assertEquals(element, result[0]);
    }

    // Test for deserializeCustom method with null _elementDeserializer
    @Test(expected = IOException.class)
    public void testDeserializeCustomWithNullElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createParser("[]");
        deserializer._deserializeCustom(parser, new TestContext());
    }

    // Test for deserializeCustom method with _elementDeserializer
    @Test
    public void testDeserializeCustomWithElementDeserializer() throws IOException {
        ObjectBuffer buffer = new ObjectBuffer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        JsonParser parser = createParser("[]");
        String[] result = deserializer._deserializeCustom(parser, new TestContext());
        assertArrayEquals(new String[0], result);

        String element = "test";
        parser = createParser("[\"" + element + "\"]");
        result = deserializer._deserializeCustom(parser, new TestContext());
        assertEquals(element, result[0]);
    }

    // Test for deserializeWithType method
    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
                String[] result = new StringArrayDeserializer().deserialize(jp, ctxt);
                return result;
            }
        };
        JsonParser parser = createParser("[]");
        Object result = deserializer.deserializeWithType(parser, new TestContext(), typeDeserializer);
        assertArrayEquals(new String[0], result);
    }

    // Test for handleNonArray method with single value
    @Test
    public void testHandleNonArrayWithSingleValue() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createParser("\"test\"");
        String[] result = deserializer.handleNonArray(parser, new TestContext());
        assertArrayEquals(new String[]{"test"}, result);
    }

    // Test for handleNonArray method with empty string
    @Test
    public void testHandleNonArrayWithEmptyString() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createParser("\"\"");
        String[] result = deserializer.handleNonArray(parser, new TestContext());
        assertNull(result);
    }

    // Test for createContextual method with null _elementDeserializer
    @Test
    public void testCreateContextualWithNullElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        assertNull(result);
    }

    // Test for createContextual method with assigned _elementDeserializer
    @Test
    public void testCreateContextualWithAssignedElementDeserializer() throws IOException {
        StringDeserializer elementDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        assertNull(result);

        result = deserializer.createContextual(ctxt, new BeanProperty());
        assertInstanceOf(StringArrayDeserializer.class, result);
    }

    // Test for createContextual method with contextual value deserializer
    @Test
    public void testCreateContextualWithContextualValueDeserializer() throws IOException {
        StringDeserializer contextualValueDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, new BeanProperty());
        assertInstanceOf(StringArrayDeserializer.class, result);

        result = deserializer.createContextual(ctxt, new BeanProperty());
        assertNotNull(result);
        JsonDeserializer<?> elementDeserializer = ((StringArrayDeserializer) result)._elementDeserializer;
        assertInstanceOf(contextualValueDeserializer.getClass(), elementDeserializer.getClass());
    }

    // Test for isDefaultDeserializer method
    @Test
    public void testIsDefaultDeserializer() {
        StringDeserializer stringDeserializer = new StringDeserializer();
        assertFalse(new StringArrayDeserializer().isDefaultDeserializer(stringDeserializer));
    }

    // Test for findConvertingContentDeserializer method
    @Test
    public void testFindConvertingContentDeserializer() throws IOException {
        StringDeserializer stringDeserializer = new StringDeserializer();
        TestContext context = new TestContext();
        JsonDeserializer<?> result = new StringArrayDeserializer().findConvertingContentDeserializer(context, null, stringDeserializer);
        assertNull(result);

        result = new StringArrayDeserializer().findConvertingContentDeserializer(context, new BeanProperty(), stringDeserializer);
        assertNull(result);
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(json));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer();
        }

        @Override
        public void returnObjectBuffer(ObjectBuffer buffer) {
            // Do nothing
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }

        @Override
        public DeserializationConfig getConfig() {
            return new DeserializationConfig();
        }

        @Override
        public void reportError(MessageBase msg) throws IOException {
            fail("reportError should not be called in tests");
        }
    }
}