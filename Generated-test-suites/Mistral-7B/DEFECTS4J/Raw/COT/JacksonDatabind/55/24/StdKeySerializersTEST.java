package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.*;

import org.junit.Test;

/**
 * Test class for StdKeySerializers
 */
public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNotNull(serializer);

        SerializationConfig configWithType = new SerializationConfig();
        JsonSerializer<Object> serializerWithType = StdKeySerializers.getStdKeySerializer(configWithType, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializerWithType);

        SerializationConfig configWithTypeAndUseDefaultFalse = new SerializationConfig();
        JsonSerializer<Object> serializerWithTypeAndUseDefaultFalse = StdKeySerializers.getStdKeySerializer(configWithType, String.class, false);
        assertNull(serializerWithTypeAndUseDefaultFalse);

        SerializationConfig configWithTypeAndUseDefaultTrue = new SerializationConfig();
        JsonSerializer<Object> serializerWithTypeAndUseDefaultTrue = StdKeySerializers.getStdKeySerializer(configWithType, String.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializerWithTypeAndUseDefaultTrue);

        Class<?> unknownType = new Object() {}.getClass();
        JsonSerializer<Object> serializerWithUnknownType = StdKeySerializers.getStdKeySerializer(configWithType, unknownType, false);
        assertNull(serializerWithUnknownType);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        Class<?> stringType = String.class;
        JsonSerializer<Object> serializerWithStringType = StdKeySerializers.getFallbackKeySerializer(config, stringType);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializerWithStringType);

        Class<?> enumType = Enum.class;
        JsonSerializer<Object> serializerWithEnumType = StdKeySerializers.getFallbackKeySerializer(config, enumType);
        assertEquals(StdKeySerializers.Dynamic.class, serializerWithEnumType.getClass());

        Class<?> unknownType = new Object() {}.getClass();
        JsonSerializer<Object> serializerWithUnknownType = StdKeySerializers.getFallbackKeySerializer(config, unknownType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializerWithUnknownType);
    }

    @Test
    public void testDynamicSerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();

        Object unknownType = new Object() {}.getClass();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, unknownType, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        PropertySerializerMap map = new PropertySerializerMap();
        JsonSerializer<Object> serializedSerializer = ((Dynamic) serializer)._findAndAddDynamic(map, unknownType, new SerializerProvider());
        assertEquals(UnknownSerializer.class, serializedSerializer.getClass());

        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializeKey(generator, serializedSerializer, new Object());
        generator.close();
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;

        String key = "test";
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(key, generator, new SerializerProvider());
        generator.close();

        assertEquals("\"test\"", readKey(generator));
    }

    @Test
    public void testDefaultSerializerForDate() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testDefaultSerializerForCalendar() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testDefaultSerializerForClass() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testDefaultSerializerForEnum() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test
    public void testDefaultSerializerForUuid() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithIncompatibleType() throws IOException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializer.serialize(new Date(), generator, new SerializerProvider());
    }

    private void serializeKey(JsonGenerator generator, JsonSerializer<Object> serializer, Object key) throws IOException {
        serializer.serialize(key, generator, new SerializerProvider());
        generator.flush();
    }

    private String readKey(JsonGenerator generator) throws IOException {
        JsonParser parser = new JsonFactory().createParser(generator.asCodeBuilder().toString());
        String key = parser.nextValue().toString();
        parser.close();
        return key;
    }

    private static class UnknownSerializer extends Default {
        private static final long serialVersionUID = 1L;

        public UnknownSerializer() {
            super(0, Object.class);
        }

        @Override
        public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
            g.writeFieldName("unknown");
        }
    }
}