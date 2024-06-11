package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Unit tests for {@link StringArrayDeserializer}.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyArray() throws Exception {
        String[] result = mapper.readValue("[]".getBytes(), StringArrayDeserializer.class.cast(StringArrayDeserializer.instance));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws Exception {
        String[] result = mapper.readValue("[\n  \"hello\"\n]".getBytes(), StringArrayDeserializer.class.cast(StringArrayDeserializer.instance));
        assertEquals("hello", result[0]);
    }

    @Test
    public void testArrayWithNullValue() throws Exception {
        String[] result = mapper.readValue("[\n  null\n]".getBytes(), StringArrayDeserializer.class.cast(StringArrayDeserializer.instance));
        assertNull(result[0]);
    }

    @Test
    public void testArrayWithMultipleElements() throws Exception {
        String[] result = mapper.readValue("[\n  \"hello\"\n  \"world\"\n]".getBytes(), StringArrayDeserializer.class.cast(StringArrayDeserializer.instance));
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    @Test
    public void testArrayWithCustomValueDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = mapper.getFactory().createValueDeserializer(String.class, new SimpleModule().addDeserializer(String.class, new TestStringDeserializer()));
        StringArrayDeserializer deserializerWithCustomDeserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = mapper.readValue("[\n  null\n  \"test\"\n]".getBytes(), deserializerWithCustomDeserializer);
        assertNull(result[0]);
        assertEquals("test", result[1]);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String[] result = mapper.readValue("[\n  \"hello\"\n  \"world\"\n]".getBytes(), new TypeReference<String[]>() {});
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonFormat() throws Exception {
        mapper.readValue("[\n  null\n  { \"name\": \"John\" }\n]".getBytes(), StringArrayDeserializer.class.cast(StringArrayDeserializer.instance));
    }

    private static void assertArrayEquals(String[] expected, String[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Array lengths do not match: expected=" + expected.length + ", actual=" + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Expected and actual arrays differ at index " + i + ": expected=" + expected[i] + ", actual=" + actual[i]);
            }
        }
    }

    static class TestStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }
}