package com.fasterxml.jackson.databind.deser.std;

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
public class StringArrayDeserializerTest {

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)}
     */
    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = createParser("[]");
        ObjectBuffer buffer = new ObjectBuffer(16);
        String[] result = new StringArrayDeserializer().deserialize(jp, new TestContext());
        assertEquals(new String[0], result);
        jp = createParser("[\"foo\",\"bar\"],[\"baz\"]");
        result = new StringArrayDeserializer().deserialize(jp, new TestContext());
        assertEquals(new String[]{"foo", "bar", "baz"}, result);
        jp = createParser("[null,null,null]");
        result = new StringArrayDeserializer().deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{null, null, null}, result);
        jp = createParser("[null,null,null,null]");
        result = new StringArrayDeserializer().deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{null, null, null, null}, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)}
     */
    @Test
    public void testDeserializeCustom() throws IOException {
        JsonParser jp = createParser("[]");
        ObjectBuffer buffer = new ObjectBuffer(16);
        String[] result = new StringArrayDeserializer(null)._deserializeCustom(jp, new TestContext());
        assertEquals(new String[0], result);
        jp = createParser("[\"foo\",\"bar\"],[\"baz\"]");
        StringDeserializer elementDeserializer = new StringDeserializer();
        result = new StringArrayDeserializer(elementDeserializer)._deserializeCustom(jp, new TestContext());
        assertEquals(new String[]{"foo", "bar", "baz"}, result);
        jp = createParser("[null,null,null]");
        result = new StringArrayDeserializer(null)._deserializeCustom(jp, new TestContext());
        assertArrayEquals(new String[]{null, null, null}, result);
        jp = createParser("[null,null,null,null]");
        result = new StringArrayDeserializer(null)._deserializeCustom(jp, new TestContext());
        assertArrayEquals(new String[]{null, null, null, null}, result);
        jp = createParser("[1,2]");
        try {
            new StringArrayDeserializer(null)._deserializeCustom(jp, new TestContext());
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = createParser("[]");
        String[] result = new StringArrayDeserializer().deserializeWithType(jp, new TestContext(), new SimpleTypeDeserializer());
        assertEquals(new String[0], result);
        jp = createParser("[\"foo\",\"bar\"],[\"baz\"]");
        result = new StringArrayDeserializer().deserializeWithType(jp, new TestContext(), new SimpleTypeDeserializer());
        assertEquals(new String[]{"foo", "bar", "baz"}, result);
        jp = createParser("[null,null,null]");
        result = new StringArrayDeserializer().deserializeWithType(jp, new TestContext(), new SimpleTypeDeserializer());
        assertArrayEquals(new String[]{null, null, null}, result);
        jp = createParser("[null,null,null,null]");
        result = new StringArrayDeserializer().deserializeWithType(jp, new TestContext(), new SimpleTypeDeserializer());
        assertArrayEquals(new String[]{null, null, null, null}, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} - handles non-array input
     */
    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = createParser("\"foo\"");
        String[] result = new StringArrayDeserializer().handleNonArray(jp, new TestContext());
        assertEquals(new String[]{"foo"}, result);
        jp = createParser("null");
        result = new StringArrayDeserializer().handleNonArray(jp, new TestContext());
        assertArrayEquals(new String[]{null}, result);
        jp = createParser("123");
        try {
            new StringArrayDeserializer().handleNonArray(jp, new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test method for {@link StringArrayDeserializer#createContextual(DeserializationContext, BeanProperty)}
     */
    @Test
    public void testCreateContextualDeserializer() throws IOException {
        JsonDeserializer<String> elementDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        StringArrayDeserializer contextualDeserializer = deserializer.createContextual(new TestContext(), null);
        assertNotNull(contextualDeserializer);
        assertEquals(deserializer.getClass(), contextualDeserializer.getClass());
        JsonDeserializer<String> contextualElementDeserializer = contextualDeserializer.getContentDeserializer();
        assertEquals(elementDeserializer.getClass(), contextualElementDeserializer.getClass());
        StringArrayDeserializer deserializerWithCustom = new StringArrayDeserializer(elementDeserializer);
        StringArrayDeserializer contextualDeserializerWithCustom = deserializerWithCustom.createContextual(new TestContext(), null);
        assertNotNull(contextualDeserializerWithCustom);
        assertEquals(deserializerWithCustom.getClass(), contextualDeserializerWithCustom.getClass());
        JsonDeserializer<String> contextualElementDeserializerWithCustom = contextualDeserializerWithCustom.getContentDeserializer();
        assertEquals(elementDeserializer.getClass(), contextualElementDeserializerWithCustom.getClass());
    }

    /**
     * Test method for {@link StringArrayDeserializer#isDefaultDeserializer(JsonDeserializer)}
     */
    @Test
    public void testIsDefaultDeserializer() {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        JsonDeserializer<Integer> intDeserializer = new SimpleIntDeserializer();
        assertTrue(new StringArrayDeserializer(null).isDefaultDeserializer(stringDeserializer));
        assertFalse(new StringArrayDeserializer(null).isDefaultDeserializer(intDeserializer));
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(json));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectFactory getFactory() {
            return new ObjectFactory() {
                @Override
                public void findAndRegisterValues(Class<?> type, JsonValueDeserializer deserializer, DeserializationConfig config) {
                    if (type == String.class) {
                        registerValueHandler(new BeanProperty.Standard(null, null), deserializer);
                    }
                }
            };
        }

        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer(16);
        }

        @Override
        public void failContextual(String msg, Throwable t) throws IOException {
            throw new IOException(msg, t);
        }

        @Override
        public void failContextual(String msg) throws IOException {
            throw new IOException(msg);
        }

        @Override
        public void reportError(MessageBase mb) throws IOException {
            //nop
        }

        @Override
        public void reportError(MessageBase mb, Throwable t) throws IOException {
            //nop
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public void setContextualHandler(DeserializationProblemHandler h) {
            //nop
        }
    }
}