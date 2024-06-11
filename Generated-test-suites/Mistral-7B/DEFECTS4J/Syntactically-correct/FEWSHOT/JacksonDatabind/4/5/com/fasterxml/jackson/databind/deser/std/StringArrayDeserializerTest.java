package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.core.JsonToken.*;

@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final String[] EMPTY_STRING_ARRAY = {};

    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createContext();
        parser.nextToken(); // [START_ARRAY]

        String[] result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null, null, null, null));

        Assert.assertArrayEquals(EMPTY_STRING_ARRAY, result);
    }

    @Test
    public void testDeserialize_NullElement() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createContext();
        parser.nextToken(VALUE_NULL);

        String[] result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null, null, null, null));

        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserialize_SingleElement() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createContext();
        parser.nextToken(VALUE_STRING);
        parser.nextText("hello");

        String[] result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null, null, null, null));

        Assert.assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testDeserialize_MultipleElements() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createContext();
        parser.nextToken(START_ARRAY);

        parser.nextToken(VALUE_STRING);
        parser.nextText("foo");
        parser.nextToken(VALUE_STRING);
        parser.nextText("bar");
        parser.nextToken(VALUE_NULL);
        parser.nextToken(VALUE_STRING);
        parser.nextText("baz");
        parser.nextToken(VALUE_STRING);
        parser.nextText("qux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("quux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("corge");
        parser.nextToken(END_ARRAY);

        String[] result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null, null, null, null));

        Assert.assertArrayEquals(new String[]{"foo", "bar", null, "baz", "qux", "quux", "corge"}, result);
    }

    @Test
    public void testDeserialize_CustomElementDeserializer() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new MyStringDeserializer());
        JsonParser parser = createContext();
        parser.nextToken(START_ARRAY);

        parser.nextToken(VALUE_STRING);
        parser.nextText("foo");
        parser.nextToken(VALUE_STRING);
        parser.nextText("bar");
        parser.nextToken(VALUE_NULL);
        parser.nextToken(VALUE_STRING);
        parser.nextText("baz");
        parser.nextToken(VALUE_STRING);
        parser.nextText("qux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("quux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("corge");
        parser.nextToken(END_ARRAY);

        String[] result = deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null, null, null, null));

        Assert.assertArrayEquals(new String[]{"foo", "bar", null, "baz", "qux", "quux", "corge"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_NonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser parser = createContext();
        parser.nextToken(VALUE_NUMBER);

        deserializer.deserialize(parser, new DeserializationContext(parser.getCodec(), null, null, null, null));
    }

    @Test
    public void testDeserializeContextual_EmptyArray() throws IOException {
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);

        JsonParser parser = createContext();
        parser.nextToken(START_ARRAY);

        String[] result = deserializer.deserializeWithType(parser, ctxt, null);

        Assert.assertArrayEquals(EMPTY_STRING_ARRAY, result);
    }

    @Test
    public void testDeserializeContextual_NullElement() throws IOException {
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);

        JsonParser parser = createContext();
        parser.nextToken(VALUE_NULL);

        String[] result = deserializer.deserializeWithType(parser, ctxt, null);

        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeContextual_SingleElement() throws IOException {
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);

        JsonParser parser = createContext();
        parser.nextToken(VALUE_STRING);
        parser.nextText("hello");

        String[] result = deserializer.deserializeWithType(parser, ctxt, null);

        Assert.assertArrayEquals(new String[]{"hello"}, result);
    }

    @Test
    public void testDeserializeContextual_MultipleElements() throws IOException {
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);

        JsonParser parser = createContext();
        parser.nextToken(START_ARRAY);

        parser.nextToken(VALUE_STRING);
        parser.nextText("foo");
        parser.nextToken(VALUE_STRING);
        parser.nextText("bar");
        parser.nextToken(VALUE_NULL);
        parser.nextToken(VALUE_STRING);
        parser.nextText("baz");
        parser.nextToken(VALUE_STRING);
        parser.nextText("qux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("quux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("corge");
        parser.nextToken(END_ARRAY);

        String[] result = deserializer.deserializeWithType(parser, ctxt, null);

        Assert.assertArrayEquals(new String[]{"foo", "bar", null, "baz", "qux", "quux", "corge"}, result);
    }

    @Test
    public void testDeserializeContextual_CustomElementDeserializer() throws IOException {
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer(new MyStringDeserializer());
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);

        JsonParser parser = createContext();
        parser.nextToken(START_ARRAY);

        parser.nextToken(VALUE_STRING);
        parser.nextText("foo");
        parser.nextToken(VALUE_STRING);
        parser.nextText("bar");
        parser.nextToken(VALUE_NULL);
        parser.nextToken(VALUE_STRING);
        parser.nextText("baz");
        parser.nextToken(VALUE_STRING);
        parser.nextText("qux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("quux");
        parser.nextToken(VALUE_STRING);
        parser.nextText("corge");
        parser.nextToken(END_ARRAY);

        String[] result = deserializer.deserializeWithType(parser, ctxt, null);

        Assert.assertArrayEquals(new String[]{"foo", "bar", null, "baz", "qux", "quux", "corge"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeContextual_NonArray() throws IOException {
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext(null, null, null, null, null);

        JsonParser parser = createContext();
        parser.nextToken(VALUE_NUMBER);

        deserializer.deserializeWithType(parser, ctxt, null);
    }

    private JsonParser createContext() throws IOException {
        return new TestJsonFactory().createParser("[]");
    }

    private static class MyStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }
}