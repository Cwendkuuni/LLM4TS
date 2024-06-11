package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer_DefaultKeyType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_StringType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_NumberType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_ClassType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, Class.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_DateType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, Date.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_CalendarType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_UUIDType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_OtherType_UseDefault() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, CustomType.class, true);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_OtherType_Null() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, CustomType.class, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_EnumType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_ENUM, Enum.class), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_OtherType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, CustomType.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_NullType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testDynamicKeySerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> keySerializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        PropertySerializerMap dynamicSerializers = new PropertySerializerMap();

        JsonSerializer<Object> serializedValueSerializer = new BaseJsonSerializer() {
            @Override
            public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
                g.writeNumber(value.hashCode());
            }
        };

        assertEquals(keySerializer.getClass(), StdKeySerializers.Dynamic.class);

        Object valueToSerialize = new CustomKeyValueObject();
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        StdKeySerializers.Dynamic dynamicSerializer = (StdKeySerializers.Dynamic) keySerializer;
        dynamicSerializer.serialize(valueToSerialize, generator, new DefaultSerializerProvider());

        JsonSerializer<Object> serializedKeySerializer = dynamicSerializer._findAndAddDynamic(dynamicSerializers,
                valueToSerialize.getClass(), new DefaultSerializerProvider());

        assertEquals(serializedValueSerializer.getClass(), serializedKeySerializer.getClass());

        dynamicSerializer.serialize(valueToSerialize, generator, new DefaultSerializerProvider());

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) generator.getOutput();
        String jsonString = outputStream.toString();

        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(jsonString.getBytes()));
        JsonToken token = parser.nextToken();

        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("CustomKeyValueObject", parser.getCurrentName());
    }

    static class CustomType {}

    static class CustomKeyValueObject {}
}