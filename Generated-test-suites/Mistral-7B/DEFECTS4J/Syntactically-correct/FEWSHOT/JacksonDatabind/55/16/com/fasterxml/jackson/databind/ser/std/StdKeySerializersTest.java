package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Given
        SerializationConfig config = new SerializationConfig();

        // When
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // When - Date
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // When - Calendar
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // When - Class
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // When - Enum
        serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // When - Number
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.DefaultKeySerializer);

        // When - String
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);

        // When - Object
        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // When - Unknown
        serializer = StdKeySerializers.getStdKeySerializer(config, new MyUnknownClass(), false);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Given
        SerializationConfig config = new SerializationConfig();

        // When
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);

        // When - Class (Enum)
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Default);

        // When - Class (Unknown)
        serializer = StdKeySerializers.getFallbackKeySerializer(config, new MyUnknownClass());

        // Then
        assertNotNull(serializer);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testDynamicSerialization() throws IOException {
        // Given
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);

        // When
        String json = mapper.writeValueAsString(new MyMapWithDynamicKeys());

        // Then
        assertNotNull(json);
        assertTrue(json.contains("\"my_dynamic_key\":1"));
    }

    static class MyUnknownClass {
        // Empty class
    }

    static class MyMapWithDynamicKeys {
        private final java.util.Map<JsonParser.ValueType, Object> myMap;

        public MyMapWithDynamicKeys() {
            myMap = new java.util.HashMap<>();
            myMap.put(JsonParser.ValueType.NUMBER_INT, 1);
            myMap.put(JsonParser.ValueType.STRING, "my_dynamic_key");
        }
    }
}