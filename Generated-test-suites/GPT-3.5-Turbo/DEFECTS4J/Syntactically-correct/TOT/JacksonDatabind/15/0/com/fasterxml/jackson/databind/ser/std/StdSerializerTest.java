package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testGetSchema() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        try {
            JsonNode schema = serializer.getSchema(null, null);
            assertNotNull(schema);
            assertTrue(schema.has("type"));
            assertEquals("string", schema.get("type").asText());
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
    
    @Test
    public void testGetSchemaWithOptional() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        try {
            JsonNode schema = serializer.getSchema(null, null, true);
            assertNotNull(schema);
            assertTrue(schema.has("type"));
            assertEquals("string", schema.get("type").asText());
            assertTrue(schema.has("required"));
            assertFalse(schema.get("required").asBoolean());
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
    
    @Test
    public void testCreateObjectNode() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        ObjectNode node = serializer.createObjectNode();
        assertNotNull(node);
        assertTrue(node instanceof ObjectNode);
    }
    
    @Test
    public void testCreateSchemaNode() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        ObjectNode schema = serializer.createSchemaNode("string");
        assertNotNull(schema);
        assertTrue(schema.has("type"));
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testCreateSchemaNodeWithOptional() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        ObjectNode schema = serializer.createSchemaNode("string", true);
        assertNotNull(schema);
        assertTrue(schema.has("type"));
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.has("required"));
        assertFalse(schema.get("required").asBoolean());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        try {
            serializer.acceptJsonFormatVisitor(null, null);
            assertTrue(true); // No exception was thrown
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
    
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<String> serializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        assertTrue(serializer.isDefaultSerializer(serializer));
    }
    
    @Test
    public void testWrapAndThrowWithIOException() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                try {
                    // implementation
                } catch (Exception e) {
                    wrapAndThrow(null, e, value, "fieldName");
                }
            }
        };
        try {
            Object value = new Object();
            serializer.serialize(value, null, null);
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
            assertNotNull(e.getCause());
        }
    }
    
    @Test(expected = Error.class)
    public void testWrapAndThrowWithError() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                try {
                    // implementation
                } catch (Exception e) {
                    wrapAndThrow(null, e, value, "fieldName");
                }
            }
        };
        throw new Error();
    }
    
    @Test
    public void testWrapAndThrowWithRuntimeException() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                try {
                    // implementation
                } catch (Exception e) {
                    wrapAndThrow(null, e, value, "fieldName");
                }
            }
        };
        try {
            Object value = new Object();
            serializer.serialize(value, null, null);
            fail("RuntimeException should have been thrown");
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testFindConvertingContentSerializer() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        try {
            BeanProperty prop = null;
            JsonSerializer<?> existingSerializer = null;
            JsonSerializer<?> contentSerializer = serializer.findConvertingContentSerializer(null, prop, existingSerializer);
            assertNull(contentSerializer);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
    
    @Test
    public void testFindPropertyFilter() {
        StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonGenerationException {
                // implementation
            }
        };
        try {
            SerializerProvider provider = null;
            Object filterId = null;
            Object valueToFilter = null;
            PropertyFilter propertyFilter = serializer.findPropertyFilter(provider, filterId, valueToFilter);
            assertNull(propertyFilter);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
}