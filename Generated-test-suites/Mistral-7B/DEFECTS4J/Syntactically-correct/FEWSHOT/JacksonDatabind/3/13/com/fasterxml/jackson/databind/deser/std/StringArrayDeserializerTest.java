package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test cases for StringArrayDeserializer.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = mapper.getFactory().createParser("[]");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals(new String[0], result);
    }

    @Test
    public void testSingleElementArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = mapper.getFactory().createParser("[\"foo\"]");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testMultipleElementsArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testNullElementArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = mapper.getFactory().createParser("[\n null,\n null,\n null\n]");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals(new String[]{"null", "null", "null"}, result);
    }

    @Test
    public void testCustomElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        JsonParser parser = mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testCustomElementDeserializerWithContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        BeanProperty property = new BeanProperty("foo", String[].class);
        JsonDeserializer<?> deser = deserializer.createContextual(new DeserializationContext(), property);
        Object[] result = ((StringArrayDeserializer) deser).deserialize(mapper.getFactory().createParser("[\"foo\", \"bar\", \"baz\"]"), new DeserializationContext(), null);
        assertEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testEmptyStringAsNull() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = mapper.getFactory().createParser("[\"\"]");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertNull(result);
    }

    @Test
    public void testNonArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = mapper.getFactory().createParser("\"foo\"");
        Object[] result = deserializer.deserialize(parser, new DeserializationContext(), null);
        assertEquals(new String[]{"foo"}, result);
    }
}