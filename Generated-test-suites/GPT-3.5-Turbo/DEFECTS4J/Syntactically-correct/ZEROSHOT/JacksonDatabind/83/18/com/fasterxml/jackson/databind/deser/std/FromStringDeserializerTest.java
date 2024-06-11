package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer<Object> deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(Object.class, 0);
    }

    @Test
    public void testDeserialize_StringValue() {
        String value = "test";
        try {
            Object result = deserializer.deserialize(new JsonParser(), new DeserializationContext());
            assertEquals(value, result);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeserialize_EmptyString() {
        try {
            Object result = deserializer.deserialize(new JsonParser(), new DeserializationContext());
            assertNull(result);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeserialize_NullValue() {
        try {
            JsonParser p = new JsonParser();
            p.setValueAsString(null);
            Object result = deserializer.deserialize(p, new DeserializationContext());
            assertNull(result);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}