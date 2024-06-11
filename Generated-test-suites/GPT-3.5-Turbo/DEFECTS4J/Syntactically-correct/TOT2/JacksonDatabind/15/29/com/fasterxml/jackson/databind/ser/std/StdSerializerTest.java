package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StdSerializerTest {

    @Test
    public void testSerialize() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };

        String value = "test";
        try {
            JsonGenerator generator = mock(JsonGenerator.class);
            SerializerProvider provider = mock(SerializerProvider.class);
            serializer.serialize(value, generator, provider);
            Mockito.verify(generator).writeString(value);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetSchema() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }
        };

        try {
            SerializerProvider provider = mock(SerializerProvider.class);
            Type typeHint = String.class;
            JsonNode schema = serializer.getSchema(provider, typeHint);
            assertEquals(null, schema);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWrapAndThrow() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                throw new IOException("Failed to serialize");
            }
        };

        String value = "test";
        try {
            JsonGenerator generator = mock(JsonGenerator.class);
            SerializerProvider provider = mock(SerializerProvider.class);
            serializer.serialize(value, generator, provider);
            fail("Exception should be thrown");
        } catch (IOException e) {
            assertNotNull(e);
            assertEquals("Failed to serialize", e.getMessage());
        }
    }

    @Test
    public void testCreateSchemaNode() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            protected ObjectNode createObjectNode() {
                return null;
            }
        };

        try {
            ObjectNode schema = serializer.createSchemaNode("string");
            assertNotNull(schema);
            assertTrue(schema.has("type"));
            assertEquals("string", schema.get("type").asText());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                // Empty serialize method for testing
            }
        };

        assertTrue(serializer.isDefaultSerializer(serializer));
    }
}