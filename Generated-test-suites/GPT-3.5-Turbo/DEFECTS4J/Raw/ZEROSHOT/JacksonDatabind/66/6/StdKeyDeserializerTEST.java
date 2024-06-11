package com.fasterxml.jackson.databind.deser.std;

import org.junit.*;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();

        // Test case 1: key is null
        String key1 = null;
        Object result1 = deserializer.deserializeKey(key1, null);
        assertNull(result1);

        // Test case 2: key is "true"
        String key2 = "true";
        Object result2 = deserializer.deserializeKey(key2, null);
        assertEquals(true, result2);

        // Test case 3: key is "false"
        String key3 = "false";
        Object result3 = deserializer.deserializeKey(key3, null);
        assertEquals(false, result3);

        // Test case 4: key is "value not 'true' or 'false'"
        String key4 = "random";
        Object result4 = deserializer.deserializeKey(key4, null);
        assertEquals("randomunknown key class", result4);
    }
}