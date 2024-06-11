
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setup() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        Integer value = 10;
        assertEquals("Failed to serialize Integer", "10", serializeValue(serializer, value));
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        Long value = 100L;
        assertEquals("Failed to serialize Long", "100", serializeValue(serializer, value));
    }

    @Test
    public void testByteSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Byte.class.getName());
        Byte value = 1;
        assertEquals("Failed to serialize Byte", "1", serializeValue(serializer, value));
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        Short value = 100;
        assertEquals("Failed to serialize Short", "100", serializeValue(serializer, value));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        Float value = 3.14f;
        assertEquals("Failed to serialize Float", "3.14", serializeValue(serializer, value));
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        Double value = 3.14159;
        assertEquals("Failed to serialize Double", "3.14159", serializeValue(serializer, value));
    }

    private String serializeValue(JsonSerializer<?> serializer, Object value) throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(value, generator, provider);
        generator.close();
        return generator.toString();
    }
}

