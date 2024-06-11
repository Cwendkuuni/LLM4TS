package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StdKeyDeserializerTest {

    @Test
    public void testStdKeyDeserializer_deserializeKey_NullKey_Success() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext context = new DeserializationContext(null);
        assertNull(stdKeyDeserializer.deserializeKey(null, context));
    }

    @Test
    public void testStdKeyDeserializer_deserializeKey_InvalidKey_Exception() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext context = new DeserializationContext(null);
        try {
            stdKeyDeserializer.deserializeKey("invalidKey", context);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation", e.getMessage());
        }
    }

    @Test
    public void testStdKeyDeserializer_forType_String_Success() {
        StdKeyDeserializer stringKeyDeserializer = StdKeyDeserializer.forType(String.class);
        assertNotNull(stringKeyDeserializer);
    }

    @Test
    public void testStdKeyDeserializer_forType_Enum_Success() {
        StdKeyDeserializer enumKeyDeserializer = StdKeyDeserializer.forType(Enum.class);
        assertNull(enumKeyDeserializer);
    }
}