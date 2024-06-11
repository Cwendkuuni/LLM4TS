
package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StdSerializerTest {

    // Expert 1's test cases

    @Test
    public void testDefaultConstructor() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertNotNull(serializer);
    }

    @Test
    public void testHandledType() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertEquals(String.class, serializer.handledType());
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectWriter writer = new ObjectMapper().writerWithRootValue(serializer);
        String json = writer.writeValueAsString(null);
        assertEquals("null", json);
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectWriter writer = new ObjectMapper().writerWithRootValue(serializer);
        String input = "Hello, World!";
        String json = writer.writeValueAsString(input);
        assertEquals(input, writer.readValueAs(String.class, json));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeUnknownType() throws JsonProcessingException {
        StdSerializer<Object> serializer = new StdSerializer<>(Object.class);
        ObjectWriter writer = new ObjectMapper().writerWithRootValue(serializer);
        writer.writeValueAsString(new Object()); // This should throw a JsonMappingException
    }

    // Expert 2's test cases

    @Test
    public void testAbstractConstructor() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertNotNull(serializer);
    }

    @Test
    public void testAbstractConstructorWithType() {
        StdSerializer<String> serializer = new StdSerializer<>(JavaType.constructType(String.class));
        assertNotNull(serializer);
    }

    @Test
    public void testAbstractConstructorWithClassAndBoolean() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class, false);
        assertNotNull(serializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        ObjectNode schema = serializer.getSchema(new ObjectMapper().getSerializerProvider(), String.class);
        assertEquals("string", schema.get("type").asText());
    }

    // Expert 3's test cases

    @Test
    public void testWrapAndThrowNull() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = new StandardSerializerProvider();
        try {
            serializer.wrapAndThrow(provider, null, null, "test");
            fail();
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testWrapAndThrowRuntimeException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = new StandardSerializerProvider();
        try {
            throw new RuntimeException("Test Exception");
        } catch (IOException e) {
            // Pass
        } finally {
            serializer.wrapAndThrow(provider, e, null, "test");
            fail();
        }
    }

    @Test
    public void testWrapAndThrowIOException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = new StandardSerializerProvider();
        try {
            throw new IOException("Test IOException");
        } catch (JsonMappingException e) {
            // Pass
        } finally {
            serializer.wrapAndThrow(provider, e, null, "test");
            fail();
        }
    }

    @Test
    public void testWrapAndThrowJsonMappingException() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = new StandardSerializerProvider();
        try {
            throw new JsonMappingException("Test JsonMappingException");
        } catch (JsonMappingException e) {
            // Pass
        } finally {
            serializer.wrapAndThrow(provider, e, null, "test");
            fail();
        }
    }

    @Test
    public void testWrapAndThrowError() throws IOException {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        SerializerProvider provider = new StandardSerializerProvider();
        try {
            throw new Error("Test Error");
        } catch (Error e) {
            // Pass
        } finally {
            serializer.wrapAndThrow(provider, e, null, "test");
            fail();
        }
    }
}