package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    // Testing deserialize method
    @Test
    public void testDeserialize() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals("foo", deserializer.deserialize(null, null));
    }

    // Testing _deserialize method
    @Test
    public void testDeserializeValue() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals("foo", deserializer._deserialize("foo", null));
        assertNull(deserializer._deserialize(null, null));
    }

    // Testing _deserializeFromEmptyString method
    @Test
    public void testDeserializeFromEmptyString() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    // Testing _deserializeEmbedded method
    @Test
    public void testDeserializeEmbedded() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertNull(deserializer._deserializeEmbedded(null, null));
    }

    // Testing _firstHyphenOrUnderscore method
    @Test
    public void testFirstHyphenOrUnderscore() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals(3, deserializer._firstHyphenOrUnderscore("foo-bar"));
        assertEquals(4, deserializer._firstHyphenOrUnderscore("foo_bar"));
        assertEquals(-1, deserializer._firstHyphenOrUnderscore("foobar"));
        assertEquals(-1, deserializer._firstHyphenOrUnderscore(""));
    }
}