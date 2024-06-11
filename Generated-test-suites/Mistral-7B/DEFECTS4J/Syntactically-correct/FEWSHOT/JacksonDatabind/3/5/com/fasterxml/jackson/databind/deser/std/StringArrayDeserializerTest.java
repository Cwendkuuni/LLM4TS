package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"foo\",\"bar\",\"baz\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        String[] result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertEquals(result, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "[null, \"bar\", \"baz\"]";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[]{null, "bar", "baz"}, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "[]";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[0], deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "\"foo\"";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[]{"foo"}, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "null";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[]{null}, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "[]";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[0], deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        String json = "[\"foo\",\"bar\",\"baz\"]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);

        String[] result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertEquals(result, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "[null, \"bar\", \"baz\"]";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[]{null, "bar", "baz"}, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "[]";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[0], deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "\"foo\"";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[]{"foo"}, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));

        json = "null";
        result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertArrayEquals(new String[]{null}, deserializer.deserialize(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration())));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer stringTypeDeserializer = mapper.getTypeFactory()
                .constructType(String[].class);

        String json = "[\"foo\",\"bar\",\"baz\"]";
        String[] result = mapper.readerForArrayOf(String.class).readValue(JsonFactory.instance.createParser(json));
        assertEquals(result, StringArrayDeserializer.instance.deserializeWithType(mapper.getFactory().createParser(json), new DeserializationContext(mapper.getConfiguration()), stringTypeDeserializer));
    }
}