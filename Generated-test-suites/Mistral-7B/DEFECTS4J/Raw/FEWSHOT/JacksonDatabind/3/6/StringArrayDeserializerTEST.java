package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeseralizerTest.TestStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final JsonDeserializer<String> DEFAULT_STRING_DESERIALIZER = new StringDeseralizerTest.TestStringDeserializer();

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("[]");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElementArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("['foo']");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new TestStringDeserializer());
        JsonParser parser = createParser("['foo']");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @Test
    public void testDeserializeNullElement() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("['null']");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeNullArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("null");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("['']");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeCustomNullValue() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new TestStringDeserializer());
        JsonParser parser = createParser("['null']");
        String[] result = deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
        assertArrayEquals(new String[]{null}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("{foo: 'bar'}");
        deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonString() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParser("'foo][bar]'");
        deserializer.deserialize(parser, new DeserializationContext.Builder().buildContext());
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext context = new DeserializationContext.Builder().buildContext();
        JsonDeserializer<String> deser = new TestStringDeserializer();
        ObjectDeserializer contextualDeserializer = deserializer.createContextual(context, null);
        Assert.assertEquals(StringArrayDeserializer.class, contextualDeserializer.getClass());
        JsonDeserializer<String> elementDeserializer = ((StringArrayDeserializer) contextualDeserializer)._elementDeserializer;
        Assert.assertEquals(deser.getClass(), elementDeserializer.getClass());
    }

    @Test
    public void testCreateContextualWithNullElementDeserializer() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext context = new DeserializationContext.Builder().buildContext();
        ObjectDeserializer contextualDeserializer = deserializer.createContextual(context, null);
        Assert.assertNull(contextualDeserializer._elementDeserializer);
    }

    @Test
    public void testCreateContextualWithEmptyStringAsNullFeature() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext context = new DeserializationContext.Builder()
                .withFeature(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .buildContext();
        ObjectDeserializer contextualDeserializer = deserializer.createContextual(context, null);
        Assert.assertEquals(StringArrayDeserializer.class, contextualDeserializer.getClass());
        JsonDeserializer<String> elementDeserializer = ((StringArrayDeserializer) contextualDeserializer)._elementDeserializer;
        Assert.assertNull(elementDeserializer);
    }

    @Test
    public void testCreateContextualWithSingleValueAsArrayFeature() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext context = new DeserializationContext.Builder()
                .withFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .buildContext();
        ObjectDeserializer contextualDeserializer = deserializer.createContextual(context, null);
        Assert.assertEquals(StringArrayDeserializer.class, contextualDeserializer.getClass());
        JsonDeserializer<String> elementDeserializer = ((StringArrayDeserializer) contextualDeserializer)._elementDeserializer;
        Assert.assertNull(elementDeserializer);
    }

    private JsonParser createParser(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(json));
    }
}