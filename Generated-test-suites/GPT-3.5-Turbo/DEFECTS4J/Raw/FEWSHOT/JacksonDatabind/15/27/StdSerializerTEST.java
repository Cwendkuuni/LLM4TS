package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdSerializerTest {
    @Test
    public void testGetSchema() {
        StdSerializer<Integer> serializer = new StdSerializer<>(Integer.class);
        assertEquals("integer", serializer.getSchema(null, Integer.class).get("type").asText());
    }
    
    @Test
    public void testGetSchemaOptional() {
        StdSerializer<String> serializer = new StdSerializer<>(String.class);
        assertEquals("string", serializer.getSchema(null, String.class, true).get("type").asText());
        assertEquals(false, serializer.getSchema(null, String.class, true).get("required").asBoolean());
    }
    
    @Test
    public void testCreateObjectNode() {
        StdSerializer<Double> serializer = new StdSerializer<>(Double.class);
        assertEquals("object", serializer.createObjectNode().get("type").asText());
    }
    
    @Test
    public void testCreateSchemaNode() {
        StdSerializer<Boolean> serializer = new StdSerializer<>(Boolean.class);
        assertEquals("boolean", serializer.createSchemaNode("boolean").get("type").asText());
    }
    
    @Test
    public void testCreateSchemaNodeOptional() {
        StdSerializer<Long> serializer = new StdSerializer<>(Long.class);
        assertEquals("long", serializer.createSchemaNode("long", true).get("type").asText());
        assertEquals(false, serializer.createSchemaNode("long", true).get("required").asBoolean());
    }
    
    @Test
    public void testIsDefaultSerializer() {
        StdSerializer<Short> serializer = new StdSerializer<>(Short.class);
        assertEquals(false, serializer.isDefaultSerializer(serializer));
    }
    
    @Test
    public void testFindConvertingContentSerializer() {
        StdSerializer<Byte> serializer = new StdSerializer<>(Byte.class);
        assertNull(serializer.findConvertingContentSerializer(null, null, null));
    }
    
    @Test
    public void testFindPropertyFilter() {
        StdSerializer<Float> serializer = new StdSerializer<>(Float.class);
        assertNull(serializer.findPropertyFilter(null, null, null));
    }
}