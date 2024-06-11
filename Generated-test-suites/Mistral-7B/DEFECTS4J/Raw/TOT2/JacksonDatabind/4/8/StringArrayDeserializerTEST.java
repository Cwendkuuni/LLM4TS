package com.example.junit;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[\"foo\",\"bar\",\"baz\"]";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = deserializer.deserialize(parser, new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testDeserializeNullInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(null);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        try {
            deserializer.deserialize(parser, new DeserializationContext(null, null, null));
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Can not deserialize instance of java.lang.String[] out of JSON Stream", e.getMessage());
        }
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "[]";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = deserializer.deserialize(parser, new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> customDeserializer = new StringDeserializer(); // Implement your custom deserializer here
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String jsonString = "[\"foo\",\"bar\"]";
        JsonParser parser = mapper.getFactory().createParser(jsonString);

        String[] result = deserializer.deserialize(parser, new DeserializationContext(null, null, null));
        Assert.assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    // Add other test cases for edge cases and exceptions as needed
}