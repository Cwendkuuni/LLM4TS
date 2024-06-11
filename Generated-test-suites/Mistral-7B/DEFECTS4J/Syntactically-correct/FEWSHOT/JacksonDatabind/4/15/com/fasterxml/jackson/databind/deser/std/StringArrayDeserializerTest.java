package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyArray() throws Exception {
        String[] result = mapper.readValue("[]", String[].class);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullArray() throws Exception {
        String[] result = mapper.readValue("null", String[].class);
        assertNull(result);
    }

    @Test
    public void testSingleElementArray() throws Exception {
        String input = "\"foo\"";
        String[] result = mapper.readValue(input, String[].class);
        assertEquals(1, result.length);
        assertEquals("foo", result[0]);
    }

    @Test
    public void testMultipleElementsArray() throws Exception {
        String input = "[\"foo\", \"bar\", null]";
        String[] result = mapper.readValue(input, String[].class);
        assertEquals(3, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testCustomElementDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String input = "[\"foo\", \"bar\"]";
        ObjectBuffer buffer = new ObjectBuffer(16);
        String[] result = deserializer.deserialize(mapper.parserForObject(input), mapper.deserializationContext());

        verify(customDeserializer).deserialize(mapper.parserForObject("\"foo\""), mapper.deserializationContext());
        verify(customDeserializer).deserialize(mapper.parserForObject("\"bar\""), mapper.deserializationContext());

        assertEquals(2, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testTypeDeserializer() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        String[] result = mapper.readValue("[]", String[].class, typeDeserializer);
        assertEquals(new String[0], result);
    }

    @Test(expected = IOException.class)
    public void testNonArray() throws Exception {
        mapper.readValue("\"foo\"", String[].class);
        fail("Expected IOException");
    }
}