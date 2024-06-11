package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonValueSerializerTest {
    @Test
    public void testSerialize() {
        // Test case 1
        JsonValueSerializer serializer = new JsonValueSerializer();
        serializer.serialize(null, null, null);

        // Test case 2
        Object bean = new Object();
        serializer.serialize(bean, null, null);
    }

    @Test
    public void testSerializeWithType() {
        // Test case 1
        JsonValueSerializer serializer = new JsonValueSerializer();
        serializer.serializeWithType(null, null, null, null);

        // Test case 2
        Object bean = new Object();
        serializer.serializeWithType(bean, null, null, null);
    }

    @Test
    public void testGetSchema() {
        JsonValueSerializer serializer = new JsonValueSerializer();
        assertNull(serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonValueSerializer serializer = new JsonValueSerializer();
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testToString() {
        JsonValueSerializer serializer = new JsonValueSerializer();
        assertEquals("(@JsonValue serializer for method null#null)", serializer.toString());
    }
}