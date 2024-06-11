package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(config, null, false);
        assertNull(serializer);
    }

    @Test
    public void testDefaultStringSerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());
    }

    @Test
    public void testGetStdKeySerializerForObject() {
        SerializationConfig config = new SerializationConfig();

        // Test for null and Object type
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        // Test for String type
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // Test for primitive types and Number
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), StdKeySerializers.getStdKeySerializer(config, int.class, false).getClass());
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), StdKeySerializers.getStdKeySerializer(config, double.class, false).getClass());

        // Test for Class type
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(config, Class.class, false).getClass());

        // Test for Date type
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(config, Date.class, false).getClass());

        // Test for Calendar type
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(config, Calendar.class, false).getClass());

        // Test for UUID type
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false).getClass());
    }

    @Test
    public void testGetFallbackKeySerializerForClass() {
        SerializationConfig config = new SerializationConfig();

        // Test for null
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));

        // Test for Enum type
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(config, Enum.class).getClass());

        // Test for other types
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), StdKeySerializers.getFallbackKeySerializer(config, int.class).getClass());
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), StdKeySerializers.getFallbackKeySerializer(config, Double.class).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(config, Class.class).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(config, Date.class).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(config, Calendar.class).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class).getClass());
    }

    @Test
    public void testGetFallbackKeySerializerForNull() {
        SerializationConfig config = new SerializationConfig();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), StdKeySerializers.getFallbackKeySerializer(config, null).getClass());
    }

    @Test
    public void testDynamicKeySerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        PropertySerializerMap dynamicSerializers = new PropertySerializerMap();

        // Test serialization with dynamic serializer
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultKeySerializer(config, Long.class, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        JsonNode node = mapper.readValue("{\"1\": 1, \"2\": 2, \"3\": 3}", JsonNode.class);
        JsonNode dynamicNode = node.get("3");
        assertEquals("3", dynamicNode.asText());

        // Test serialization with custom serializer
        JsonSerializer<Object> customSerializer = mapper.findValueSerializer(Long.class);
        dynamicSerializers.addSerializer(Long.class, customSerializer);
        dynamicSerializers = PropertySerializerMap.merge(dynamicSerializers, PropertySerializerMap.emptyForProperties());

        JsonNode updatedNode = mapper.readValue("{\"1\": 1, \"2\": 2, \"3\": 3}", JsonNode.class);
        JsonNode updatedDynamicNode = updatedNode.get("3");
        assertEquals("CUSTOM_SERIALIZER", updatedDynamicNode.asText());

        // Test serialization with null value
        Object nullValue = null;
        JsonSerializer<Object> nullSerializer = mapper.findValueSerializer(null.getClass());
        dynamicSerializers.addSerializer(null.getClass(), nullSerializer);
        dynamicSerializers = PropertySerializerMap.merge(dynamicSerializers, PropertySerializerMap.emptyForProperties());

        JsonNode updatedNullNode = mapper.readValue("{\"1\": 1, \"2\": 2, \"3\": null}", JsonNode.class);
        JsonNode updatedNullDynamicNode = updatedNullNode.get("3");
        assertNull(updatedNullDynamicNode);
    }
}