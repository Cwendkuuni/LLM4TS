package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    private StdKeySerializers stdKeySerializers;

    @Before
    public void setup() {
        stdKeySerializers = new StdKeySerializers();
    }

    @Test
    public void testGetStdKeySerializerWithNullRawKeyTypeShouldReturnDynamic() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = null;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializerWithStringRawKeyTypeShouldReturnDefaultStringSerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializerWithPrimitiveRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = int.class;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializerWithClassRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Class.class;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializerWithDateRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Date.class;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_DATE, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializerWithCalendarRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Calendar.class;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializerWithUUIDRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = java.util.UUID.class;
        boolean useDefault = true;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, defaultSerializer._typeId);
    }

    @Test
    public void testGetStdKeySerializerWithUnknownRawKeyTypeAndNoUseDefaultShouldReturnNull() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Object.class;
        boolean useDefault = false;

        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializerWithNullRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = null;

        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(result);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumRawKeyTypeShouldReturnDynamic() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = Enum.class;

        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializerWithEnumClassRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = MyEnum.class;

        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(result);
        assertTrue(result instanceof StdKeySerializers.Default);
        StdKeySerializers.Default defaultSerializer = (StdKeySerializers.Default) result;
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, defaultSerializer._typeId);
    }

    @Test
    public void testGetFallbackKeySerializerWithNonEnumRawKeyTypeShouldReturnDefaultKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;

        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        assertNotNull(result);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetDefaultShouldReturnDefaultKeySerializer() {
        JsonSerializer<Object> result = stdKeySerializers.getDefault();

        assertNotNull(result);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testDefaultSerializeDateKeyShouldCallProvider() {
        Object value = new Date();
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        stdKeySerializers.Default defaultSerializer = new stdKeySerializers.Default(stdKeySerializers.Default.TYPE_DATE, Date.class);
        defaultSerializer.serialize(value, generator, provider);

        assertTrue(generator.defaultSerializeDateKeyCalled);
    }

    @Test
    public void testDefaultSerializeCalendarKeyShouldCallProvider() {
        Object value = Calendar.getInstance();
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        stdKeySerializers.Default defaultSerializer = new stdKeySerializers.Default(stdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        defaultSerializer.serialize(value, generator, provider);

        assertTrue(generator.defaultSerializeDateKeyCalled);
    }

    @Test
    public void testDefaultSerializeClassKeyShouldWriteFieldName() {
        Object value = String.class;
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        stdKeySerializers.Default defaultSerializer = new stdKeySerializers.Default(stdKeySerializers.Default.TYPE_CLASS, Class.class);
        defaultSerializer.serialize(value, generator, provider);

        assertEquals(value.getName(), generator.fieldName);
    }

    @Test
    public void testDefaultSerializeEnumKeyShouldWriteFieldNameUsingToStringIfEnabled() {
        Object value = MyEnum.VALUE;
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        provider.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        stdKeySerializers.Default defaultSerializer = new stdKeySerializers.Default(stdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        defaultSerializer.serialize(value, generator, provider);

        assertEquals(value.toString(), generator.fieldName);
    }

    @Test
    public void testDefaultSerializeEnumKeyShouldWriteFieldNameUsingEnumNameIfDisabled() {
        Object value = MyEnum.VALUE;
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        provider.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        stdKeySerializers.Default defaultSerializer = new stdKeySerializers.Default(stdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        defaultSerializer.serialize(value, generator, provider);

        assertEquals(value.name(), generator.fieldName);
    }

    @Test
    public void testDefaultSerializeDefaultKeyShouldWriteFieldNameUsingToString() {
        Object value = "key";
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        stdKeySerializers.Default defaultSerializer = new stdKeySerializers.Default(stdKeySerializers.Default.TYPE_TO_STRING, Object.class);
        defaultSerializer.serialize(value, generator, provider);

        assertEquals(value.toString(), generator.fieldName);
    }

    private enum MyEnum {
        VALUE;
    }

    private class SerializationConfig {

    }

    private class JsonGenerator {
        public boolean defaultSerializeDateKeyCalled;
        public String fieldName;

        public void defaultSerializeDateKey(Date value, JsonGenerator generator) {
            defaultSerializeDateKeyCalled = true;
        }

        public void defaultSerializeDateKey(Long value, JsonGenerator generator) {
            defaultSerializeDateKeyCalled = true;
        }

        public void writeFieldName(String name) {
            fieldName = name;
        }
    }

    private class SerializerProvider {

        private boolean writeEnumsUsingToString;

        public void enable(SerializationFeature feature) {
            if (feature == SerializationFeature.WRITE_ENUMS_USING_TO_STRING) {
                writeEnumsUsingToString = true;
            }
        }

        public void disable(SerializationFeature feature) {
            if (feature == SerializationFeature.WRITE_ENUMS_USING_TO_STRING) {
                writeEnumsUsingToString = false;
            }
        }

        public boolean isEnabled(SerializationFeature feature) {
            if (feature == SerializationFeature.WRITE_ENUMS_USING_TO_STRING) {
                return writeEnumsUsingToString;
            }
            return false;
        }
    }

}