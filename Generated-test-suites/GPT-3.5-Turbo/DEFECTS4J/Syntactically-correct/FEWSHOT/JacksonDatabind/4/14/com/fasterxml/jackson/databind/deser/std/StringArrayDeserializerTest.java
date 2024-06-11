
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer = new StringArrayDeserializer();

    @Test
    public void testDeserialize() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectCodec codec = factory.getCodec();
        DeserializationContext context = null;
        TypeDeserializer typeDeserializer = null;

        JsonParser parser = factory.createParser(codec, "[\"foo\", \"bar\"]");
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        String[] result = deserializer.deserialize(parser, context);
        Assert.assertEquals("foo", result[0]);
        Assert.assertEquals("bar", result[1]);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectCodec codec = factory.getCodec();
        DeserializationContext context = null;
        TypeDeserializer typeDeserializer = null;

        JsonParser parser = factory.createParser(codec, "[\"foo\", \"bar\"]");
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        Assert.assertArrayEquals(new String[] { "foo", "bar" }, (String[]) result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectCodec codec = factory.getCodec();
        DeserializationContext context = null;

        JsonParser parser = factory.createParser(codec, "\"foo\"");
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);

        String[] result = deserializer.handleNonArray(parser, context);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals("foo", result[0]);
    }

    @Test
    public void testCreateContextual() {
        JsonDeserializer<?> deser = deserializer.createContextual(null, null);
        Assert.assertEquals(deserializer, deser);
    }

}
