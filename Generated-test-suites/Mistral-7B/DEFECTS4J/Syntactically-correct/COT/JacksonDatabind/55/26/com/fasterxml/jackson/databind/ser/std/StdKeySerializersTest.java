package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.StringKeySerializer.class, serializer);

        rawKeyType = Object.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);

        rawKeyType = boolean.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNull(serializer);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, true);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Default.class, serializer);

        rawKeyType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNotNull(serializer);
        assertInstanceOf(StdKeySerializers.Dynamic.class, serializer);

        rawKeyType = int.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertNull(serializer);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFallbackKeySerializerNullConfig() throws Exception {
        StdKeySerializers.getFallbackKeySerializer(null, int.class);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStdKeySerializerNullConfig() throws Exception {
        StdKeySerializers.getStdKeySerializer(null, int.class, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStdKeySerializerInvalidType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        StdKeySerializers.getStdKeySerializer(config, Object.class, true);
    }

    @Test
    public void testDynamicSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper mapper = new ObjectMapper(config);
        PropertySerializerMap serializers = PropertySerializerMap.emptyForProperties();

        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        serializers.addKeySerializer(Integer.class, serializer);

        JsonSerializer<Object> expectedSerializer = StdKeySerializers.Dynamic.class.newInstance();
        JsonSerializer<Object> resultSerializer = StdKeySerializers.getFallbackKeySerializer(config, Integer.class);
        assertEquals(expectedSerializer.getClass(), resultSerializer.getClass());

        ObjectMapper mapperWithDynamic = mapper.copy();
        mapperWithDynamic.setSerializerProvider(new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullSerializer) {
                return serializers.serializerFor(type);
            }
        });

        ObjectMapper mapperWithDefault = mapper.copy();
        mapperWithDefault.setSerializerProvider(new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> nullSerializer) {
                return StdKeySerializers.getDefault();
            }
        });

        assertNotEquals(mapperWithDynamic, mapperWithDefault);

        JsonGenerator generator = mapperWithDynamic.getFactory().createGenerator(null);
        mapperWithDynamic.writeValue(generator, new Integer(1));
        generator.flush();

        generator = mapperWithDefault.getFactory().createGenerator(null);
        mapperWithDefault.writeValue(generator, new Integer(1));
        generator.flush();

        mapperWithDynamic = null;
        mapperWithDefault = null;

        config = null;
        mapper = null;
        serializers = null;
        serializer = null;
        mapperWithDynamic = null;
        mapperWithDefault = null;
        generator = null;
    }

    @Test
    public void testToStringSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNotNull(serializer);

        Object obj = new Object() {
            @Override
            public String toString() {
                return "Test Object";
            }
        };
        JsonGenerator generator = new JsonFactory(config).createGenerator(null);
        serializer.serialize(obj, generator, new SerializerProvider());
        generator.flush();

        String result = generator.getOutputContentAsString();
        assertEquals("\"Test Object\"", result);

        generator = null;
        result = null;
        obj = null;
        config = null;
        serializer = null;
    }

    @Test
    public void testEnumSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNotNull(serializer);

        Enum e = Enum.valueOf(MyEnum.class, "VALUE_1");
        JsonGenerator generator = new JsonFactory(config).createGenerator(null);
        serializer.serialize(e, generator, new SerializerProvider());
        generator.flush();

        String result = generator.getOutputContentAsString();
        assertEquals("\"VALUE_1\"", result);

        generator = null;
        result = null;
        e = null;
        config = null;
        serializer = null;
    }

    @Test
    public void testClassSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNotNull(serializer);

        Class<?> clazz = Object.class;
        JsonGenerator generator = new JsonFactory(config).createGenerator(null);
        serializer.serialize(clazz.getName(), generator, new SerializerProvider());
        generator.flush();

        String result = generator.getOutputContentAsString();
        assertEquals("\"java.lang.Object\"", result);

        generator = null;
        result = null;
        clazz = null;
        config = null;
        serializer = null;
    }

    @Test
    public void testDateSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNotNull(serializer);

        Date date = new Date();
        JsonGenerator generator = new JsonFactory(config).createGenerator(null);
        generator.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        serializer.serialize(date, generator, new SerializerProvider());
        generator.flush();

        String result = generator.getOutputContentAsString();
        assertEquals(String.format("\"%s\"", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date)), result);

        generator = null;
        result = null;
        date = null;
        config = null;
        serializer = null;
    }

    @Test
    public void testCalendarSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertNotNull(serializer);

        Calendar calendar = Calendar.getInstance();
        JsonGenerator generator = new JsonFactory(config).createGenerator(null);
        generator.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        serializer.serialize(calendar.getTimeInMillis(), generator, new SerializerProvider());
        generator.flush();

        String result = generator.getOutputContentAsString();
        assertEquals(String.format("\"%s\"", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(calendar.getTimeInMillis()))), result);

        generator = null;
        result = null;
        calendar = null;
        config = null;
        serializer = null;
    }

    static void assertEquals(Class<?> expected, Class<?> actual) {
        assertEquals(expected.getName(), actual.getName());
    }

    static void assertEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }
}