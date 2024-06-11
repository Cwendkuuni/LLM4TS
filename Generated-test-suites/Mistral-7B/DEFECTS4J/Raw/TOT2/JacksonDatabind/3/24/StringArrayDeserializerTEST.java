package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDefaultDeserializer() throws IOException {
        StringJsonParser jp = new StringJsonParser("[\"hello\", \"world\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new StdDeserializer<String>(String.class) {};
        StringJsonParser jp = new StringJsonParser("[\"hello\", \"world\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testEmptyArray() throws IOException {
        StringJsonParser jp = new StringJsonParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new TestContext());
        assertArrayEquals(new String[]{}, result);
    }

    @Test(expected = IOException.class)
    public void testMissingStartArrayToken() throws IOException {
        StringJsonParser jp = new StringJsonParser("{}");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, new TestContext());
    }
}