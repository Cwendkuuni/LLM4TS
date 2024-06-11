package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeySerializerTest {
    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        Object value = null;
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;

        try {
            serializer.serialize(value, jsonGenerator, serializerProvider);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        value = new Date();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);

        serializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(value.toString());
    }

    @Test
    public void testSerializeWithNullJsonGenerator() {
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = new Date();
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        try {
            serializer.serialize(value, null, serializerProvider);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testSerializeWithNullSerializerProvider() {
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = new Date();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        try {
            serializer.serialize(value, jsonGenerator, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testSerializeWithEmptyStringValue() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        serializer.serialize("", jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName("");
    }

    @Test
    public void testSerializeWithValueThatDoesntOverrideToString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Object value = new Object() {
            @Override
            public String toString() {
                throw new UnsupportedOperationException("toString not implemented");
            }
        };

        try {
            serializer.serialize(value, jsonGenerator, serializerProvider);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    // ... Test cases for other methods
}