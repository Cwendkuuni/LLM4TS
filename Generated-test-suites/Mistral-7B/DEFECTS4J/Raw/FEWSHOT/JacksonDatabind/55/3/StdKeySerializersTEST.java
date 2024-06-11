package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.new Dynamic(), serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        assertEquals(StdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), serializer);
    }

    @Test
    public void testGetStringSerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        String str = "test";
        ObjectKeyFactory keyFactory = new ObjectKeyFactory();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(str, generator, new JsonSerializer.DefaultserializerProvider().findValueSerializer(config, String.class));
        assertEquals("test", generator.getOutputContent().toString());
    }

    @Test
    public void testDynamicKeySerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);
        assertEquals(StdKeySerializers.new Dynamic(), serializer);

        Object key = new Object() {
            private static final long serialVersionUID = 1L;
            public String toString() {
                return "test";
            }
        };

        ObjectKeyFactory keyFactory = new ObjectKeyFactory();
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(key, generator, new JsonSerializer.DefaultserializerProvider().findValueSerializer(config, Object.class.getClassLoader()));
        assertEquals("test", generator.getOutputContent().toString());
    }
}