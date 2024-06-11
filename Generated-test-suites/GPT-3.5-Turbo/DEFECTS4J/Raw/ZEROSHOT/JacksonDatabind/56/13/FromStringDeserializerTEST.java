package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;

    @Before
    public void setup() {
        deserializer = new FromStringDeserializer.Std(String.class, 0);
    }

    @Test
    public void testDeserialize() throws Exception {
        String input = "test";
        String result = deserializer.deserialize(input);
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        String input = "";
        String result = deserializer.deserialize(input);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidString() throws Exception {
        String input = "123";
        deserializer.deserialize(input);
    }
}