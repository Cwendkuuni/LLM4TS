package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

    @Test
    public void testEmptyArray() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        JsonDeserializer<String> stringDeserializer = null;
        String[] result = deserializer.deserialize(jp, new TestContext(stringDeserializer));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testEmptyArrayWithElementDeserializer() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        JsonDeserializer<String> stringDeserializer = createStringDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext(stringDeserializer));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value"]
        String[] result = deserializer.deserialize(jp, new TestContext(null));
        assertEquals("value", result[0]);
    }

    @Test
    public void testSingleElementArrayWithElementDeserializer() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value"]
        JsonDeserializer<String> stringDeserializer = createStringDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext(stringDeserializer));
        assertEquals("value", result[0]);
    }

    @Test
    public void testMultipleElementsArray() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // ["value2"]
        String[] result = deserializer.deserialize(jp, new TestContext(null));
        assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testMultipleElementsArrayWithElementDeserializer() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // ["value2"]
        JsonDeserializer<String> stringDeserializer = createStringDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext(stringDeserializer));
        assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testNullElementInArray() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // [null]
        jp.nextToken(); // ["value3"]
        String[] result = deserializer.deserialize(jp, new TestContext(null));
        assertArrayEquals(new String[]{ "value1", null, "value3" }, result);
    }

    @Test
    public void testNullElementInArrayWithElementDeserializer() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // [null]
        jp.nextToken(); // ["value3"]
        JsonDeserializer<String> stringDeserializer = createStringDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext(stringDeserializer));
        assertArrayEquals(new String[]{ "value1", null, "value3" }, result);
    }

    @Test
    public void testEmptyStringAsNullElement() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // [""]
        jp.nextToken(); // ["value3"]
        TestContext context = new TestContext(null);
        DeserializationContext ctxt = new TestDeserializationContext();
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
            String[] result = deserializer.deserialize(jp, context);
            assertArrayEquals(new String[]{ "value1", null, "value3" }, result);
        } else {
            String[] result = deserializer.deserialize(jp, context);
            assertArrayEquals(new String[]{ "value1", "", "value3" }, result);
        }
    }

    @Test
    public void testEmptyStringAsNullElementWithElementDeserializer() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // [""]
        jp.nextToken(); // ["value3"]
        JsonDeserializer<String> stringDeserializer = createStringDeserializer();
        TestContext context = new TestContext(stringDeserializer);
        DeserializationContext ctxt = new TestDeserializationContext();
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
            String[] result = deserializer.deserialize(jp, context);
            assertArrayEquals(new String[]{ "value1", null, "value3" }, result);
        } else {
            String[] result = deserializer.deserialize(jp, context);
            assertArrayEquals(new String[]{ "value1", "", "value3" }, result);
        }
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // [ERROR]
        jp.nextToken(); // ["value3"]
        deserializer.deserialize(jp, new TestContext(null));
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // ["value2"]
        JsonDeserializer<String> stringDeserializer = createStringDeserializer();
        String[] result = deserializer.deserialize(jp, new TestContext(stringDeserializer));
        for (int i = 0; i < result.length; i++) {
            assertEquals("CustomValue-" + i, result[i]);
        }
    }

    @Test
    public void testCustomDeserializerWithEmptyStringAsNull() throws IOException {
        JsonParser jp = createContext();
        jp.nextToken(); // [START_ARRAY]
        jp.nextToken(); // ["value1"]
        jp.nextToken(); // [""]
        jp.nextToken(); // ["value3"]
        JsonDeserializer<String> stringDeserializer = new StringDeserializer() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
                    return null;
                }
                return super.deserialize(jp, ctxt);
            }
        };
        TestContext context = new TestContext(stringDeserializer);
        DeserializationContext ctxt = new TestDeserializationContext();
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
            String[] result = deserializer.deserialize(jp, context);
            assertArrayEquals(new String[]{ "value1", null, "value3" }, result);
        } else {
            String[] result = deserializer.deserialize(jp, context);
            assertArrayEquals(new String[]{ "value1", "", "value3" }, result);
        }
    }

    private JsonParser createContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser("[]");
    }

    private JsonDeserializer<String> createStringDeserializer() {
        StringDeserializer stringDeserializer = new StringDeserializer();
        stringDeserializer.findContentConverter(new TestDeserializationContext(), null);
        return stringDeserializer;
    }

    static class TestContext extends TestBaseContext {
        private JsonDeserializer<String> _elementDeserializer;

        TestContext(JsonDeserializer<String> elementDeserializer) {
            _elementDeserializer = elementDeserializer;
        }

        @Override
        public JsonDeserializer<?> getElementDeserializer() {
            return _elementDeserializer;
        }
    }

    static class TestDeserializationContext extends DeserializationContext {
        TestDeserializationContext() {
            super(null, null, null, null);
        }
    }
}