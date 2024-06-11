package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdSerializerTest {

    @Test
    public void testHandleType() {
        StdSerializer<?> stdSerializer = Mockito.mock(StdSerializer.class, Mockito.CALLS_REAL_METHODS);
        assertEquals(Object.class, stdSerializer.handledType());
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        StdSerializer<?> stdSerializer = Mockito.mock(StdSerializer.class, Mockito.CALLS_REAL_METHODS);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        try {
            stdSerializer.serialize(null, jsonGenerator, serializerProvider);
        } catch (Exception e) {
            // Exception handling scenario
        }
        verify(jsonGenerator, times(1)).writeNull();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdSerializer<?> stdSerializer = Mockito.mock(StdSerializer.class, Mockito.CALLS_REAL_METHODS);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        stdSerializer.getSchema(serializerProvider, String.class);
        // Edge case scenario
        stdSerializer.getSchema(serializerProvider, String.class, false);
    }

    @Test
    public void testCreateObjectNode() {
        StdSerializer<?> stdSerializer = Mockito.mock(StdSerializer.class, Mockito.CALLS_REAL_METHODS);
        ObjectNode objectNode = stdSerializer.createObjectNode();
        assertEquals("ObjectNode", objectNode.getNodeType().name());
    }
}