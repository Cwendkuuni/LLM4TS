package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        deserializationContext = new DeserializationContext(null, null, null);
    }

    @Test
    public void testDeserializeKeyWithString() throws Exception {
        String key = "test";
        Object result = keyDeserializer.deserializeKey(key, deserializationContext);

        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKeyWithNullString() throws Exception {
        String key = null;
        Object result = keyDeserializer.deserializeKey(key, deserializationContext);

        assertNull(result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeKeyWithInvalidJson() throws Exception {
        String key = "{test}";
        keyDeserializer.deserializeKey(key, deserializationContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKeyWithUnknownEnumValue() throws Exception {
        StdKeyDeserializer enumKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class);
        String key = "UNKNOWN";
        enumKeyDeserializer.deserializeKey(key, deserializationContext);
    }

}