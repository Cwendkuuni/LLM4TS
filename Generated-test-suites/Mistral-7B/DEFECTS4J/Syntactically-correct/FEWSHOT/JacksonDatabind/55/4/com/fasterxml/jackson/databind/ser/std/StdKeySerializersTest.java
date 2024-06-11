package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.Feature.WRITE_ENUMS_USING_TO_STRING;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, boolean.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        Class<?> enumType = Enum.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, enumType, false);
        assert serializer instanceof StdKeySerializers.Default;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        // Test with a specific enum type
        EnumTestEnum enumValue = EnumTestEnum.FIRST;
        serializer = StdKeySerializers.getStdKeySerializer(config, enumValue.getClass(), false);
        assert serializer instanceof StdKeySerializers.Default;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        // Test with a custom enum serializer
        BeanDescription desc = new BeanDescription();
        ObjectIdReader idReader = new ObjectIdReader();
        config.setObjectMapper(new JacksonAnnotationModule().getMapper(new AnnotationIntrospector()));
        config.setObjectMapper(config.getObjectMapper().assignSerializer(enumValue.getClass(), idReader));
        serializer = StdKeySerializers.getStdKeySerializer(config, enumValue.getClass(), false);
        assert serializer == idReader;
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer._typeId);

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer._typeId);

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer._typeId);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assert serializer instanceof StdKeySerializers.Default;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer._typeId);

        rawKeyType = Object.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testDefaultSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testDefaultStringSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefaultStringSerializer();
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testDynamicSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String[].class, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());

        Object key = new String[]{"test"};
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(key, generator, config.getSerializerProvider());
        String json = generator.getOutputContent();
        assertEquals("[\"test\"]", json);

        // Test multiple serializer registrations
        map = PropertySerializerMap.emptyForProperties();
        JacksonAnnotationModule module = new JacksonAnnotationModule();
        ObjectMapper mapper = module.getMapper(new AnnotationIntrospector());
        config.setObjectMapper(mapper);
        JsonSerializer<Object> stringKeySerializer = new StringKeySerializer();
        config.getObjectMapper().registerSerializer(String[].class, stringKeySerializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, String[].class, false);
        assertEquals(stringKeySerializer, serializer);

        generator.close();
    }

    @Test
    public void testEnumSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new JacksonAnnotationModule().getMapper(new AnnotationIntrospector());
        config.setObjectMapper(mapper);

        EnumTestEnum first = EnumTestEnum.FIRST;
        JsonSerializer<Object> serializer = config.getObjectMapper().getSerializerProvider().findValueSerializer(first, null);
        assertEquals(String.class, serializer.getType());

        Class<EnumTestEnum> enumType = EnumTestEnum.class;
        JsonSerializer<Object> defaultSerializer = StdKeySerializers.getStdKeySerializer(config, enumType, false);
        JsonSerializer<Object> customSerializer = new EnumKeySerializer();
        config.getObjectMapper().registerSerializer(enumType, customSerializer);
        JsonSerializer<Object> newSerializer = StdKeySerializers.getStdKeySerializer(config, enumType, false);

        assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultSerializer._typeId);
        assertEquals(customSerializer.getClass(), newSerializer.getClass());
    }

    public static class EnumKeySerializer extends KeySerializerBase<EnumTestEnum> {
        @Override
        public void serialize(EnumTestEnum value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            generator.writeFieldName(value.toString());
        }
    }

    public enum EnumTestEnum {
        FIRST, SECOND, THIRD;

        @JsonValue
        public String toString() {
            return name();
        }
    }
}