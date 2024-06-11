
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_SingleElementArray() throws IOException {
        String json = "[\"value\"]";

        String[] result = objectMapper.readValue(json, String[].class);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testDeserialize_MultipleElementArray() throws IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";

        String[] result = objectMapper.readValue(json, String[].class);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserialize_EmptyArray() throws IOException {
        String json = "[]";

        String[] result = objectMapper.readValue(json, String[].class);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_NullArray() throws IOException {
        String json = "null";

        String[] result = objectMapper.readValue(json, String[].class);

        assertNull(result);
    }

    @Test
    public void testDeserialize_NonArrayJson() throws IOException {
        String json = "\"value\"";

        try {
            objectMapper.readValue(json, String[].class);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unexpected token (VALUE_STRING), expected START_ARRAY: need JSON Array to contain As.WRAPPER_ARRAY type information", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_NestedObjectArray() throws IOException {
        String json = "[{\"key\":\"value\"}]";

        try {
            objectMapper.readValue(json, String[].class);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Cannot deserialize instance of `java.lang.String` out of START_OBJECT token", e.getMessage());
        }
    }
}
