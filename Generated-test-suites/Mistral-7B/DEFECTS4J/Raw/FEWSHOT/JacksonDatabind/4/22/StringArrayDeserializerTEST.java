package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] result = StringArrayDeserializer.instance.deserialize(mapper.getFactory().createParser("[]"), new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeArrayWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"foo\"]";
        String[] result = StringArrayDeserializer.instance.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testDeserializeArrayWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"foo\", \"bar\", \"baz\"]";
        String[] result = StringArrayDeserializer.instance.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[null]";
        String[] result = StringArrayDeserializer.instance.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customElementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customElementDeserializer);
        String jsonString = "[\"foo\", \"bar\", \"baz\"]";
        String[] result = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeArrayWithNullCustomElementDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        String jsonString = "[\"foo\", \"bar\", \"baz\"]";
        String[] result = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeArrayWithEmptyStringAsNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.getFactory().createParser("[\"\", \"foo\"]");
        DeserializationContext context = new DeserializationContext(parser.getCodec(), parser, null);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, context);
        Assert.assertArrayEquals(new String[]{null, "foo"}, result);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customElementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsString();
            }
        };
        JsonDeserializer<String[]> contextualDeserializer = StringArrayDeserializer.instance.createContextual(mapper.getDeserializationContext(), null);
        Assert.assertTrue(contextualDeserializer instanceof StringArrayDeserializer);
        StringArrayDeserializer deserializer = (StringArrayDeserializer) contextualDeserializer;
        Assert.assertNull(deserializer._elementDeserializer);

        JsonDeserializer<String[]> contextualDeserializerWithCustomElementDeserializer = StringArrayDeserializer.instance.createContextual(mapper.getDeserializationContext(), null);
        Assert.assertTrue(contextualDeserializerWithCustomElementDeserializer instanceof StringArrayDeserializer);
        StringArrayDeserializer deserializerWithCustomElementDeserializer = (StringArrayDeserializer) contextualDeserializerWithCustomElementDeserializer;
        Assert.assertNotNull(deserializerWithCustomElementDeserializer._elementDeserializer);
        Assert.assertEquals(customElementDeserializer, deserializerWithCustomElementDeserializer._elementDeserializer);
    }
}