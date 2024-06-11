package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StdKeyDeserializerTest {

    @Test
    public void deserializeKey_withString_shouldReturnString() throws IOException {
        String input = "test";
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = stringKD.deserializeKey(input, ctxt);
        Assert.assertEquals(input, result);
    }

    @Test
    public void deserializeKey_withBoolean_shouldReturnBoolean() throws IOException {
        String input = "true";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN,
                Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = stdKeyDeserializer.deserializeKey(input, ctxt);
        Assert.assertEquals(true, result);
    }

    @Test
    public void deserializeKey_withInvalidBoolean_shouldThrowException() throws IOException {
        String input = "invalid";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN,
                Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            stdKeyDeserializer.deserializeKey(input, ctxt);
            Assert.fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            Assert.assertEquals("not a valid representation: invalid", e.getMessage());
        }
    }

    @Test
    public void deserializeKey_withByte_shouldReturnByte() throws IOException {
        String input = "10";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE,
                Byte.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = stdKeyDeserializer.deserializeKey(input, ctxt);
        Assert.assertEquals(Byte.valueOf((byte) 10), result);
    }

    @Test
    public void deserializeKey_withInvalidByte_shouldThrowException() throws IOException {
        String input = "128";
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE,
                Byte.class);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            stdKeyDeserializer.deserializeKey(input, ctxt);
            Assert.fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            Assert.assertEquals("overflow, value can not be represented as 8-bit value", e.getMessage());
        }
    }

    // Test cases for other methods goes here

}