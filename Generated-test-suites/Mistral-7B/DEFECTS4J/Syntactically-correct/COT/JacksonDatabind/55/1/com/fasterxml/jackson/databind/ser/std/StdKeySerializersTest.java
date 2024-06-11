package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        Class<Integer> intType = Integer.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, intType, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        Class<Date> dateType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, dateType, false);
        assertEquals(StdKeySerializers.DEFAULT, StdKeySerializers.getDefault(), serializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, dateType, true);
        assertNotNull(serializer);

        Class<Calendar> calendarType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, calendarType, false);
        assertEquals(StdKeySerializers.DEFAULT, StdKeySerializers.getDefault(), serializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, calendarType, true);
        assertNotNull(serializer);

        Class<java.util.UUID> uuidType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(config, uuidType, false);
        assertNull(serializer);
        serializer = StdKeySerializers.getStdKeySerializer(config, uuidType, true);
        assertNotNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        Class<Enum> enumType = Enum.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, enumType);
        assertEquals(StdKeySerializers.DEFAULT, StdKeySerializers.getDefault(), serializer);

        Class<Integer> intType = Integer.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, intType);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);

        Class<Date> dateType = Date.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, dateType);
        assertEquals(StdKeySerializers.DEFAULT, StdKeySerializers.getDefault(), serializer);

        Class<java.util.UUID> uuidType = java.util.UUID.class;
        serializer = StdKeySerializers.getFallbackKeySerializer(config, uuidType);
        assertNull(serializer);
    }

    @Test
    public void testDefault() {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testDynamic() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);
        assertEquals(StdKeySerializers.DEFAULT, StdKeySerializers.getDefault(), serializer);

        Object obj = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };

        PropertySerializerMap map = new PropertySerializerMap();
        JsonSerializer<Object> dynamicSerializer = StdKeySerializers.Dynamic._findAndAddDynamic(map, obj.getClass(), new SerializerProvider());
        assertNotNull(dynamicSerializer);

        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        dynamic.serialize(obj, generator, new SerializerProvider());
        String result = generator.getOutputBuffer().toString();
        assertEquals("test", result);
    }

    @Test
    public void testStringKeySerializer() throws JsonProcessingException {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };

        SerializationConfig config = new SerializationConfig();
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        StdKeySerializers.StringKeySerializer stringSerializer = new StdKeySerializers.StringKeySerializer();
        stringSerializer.serialize(obj, generator, new SerializerProvider());
        String result = generator.getOutputBuffer().toString();
        assertEquals("test", result);
    }

    @Test
    public void testEnumKeySerializer() throws JsonProcessingException {
        EnumSet<MyEnum> enumSet = EnumSet.allOf(MyEnum.class);

        SerializationConfig config = new SerializationConfig();
        config.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        StdKeySerializers.Default enumSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        enumSerializer.serialize(enumSet, generator, new SerializerProvider());
        String result = generator.getOutputBuffer().toString();
        List<String> expected = new ArrayList<>();
        for (MyEnum e : enumSet) {
            expected.add(e.toString());
        }
        assertEquals(expected.toString(), result);

        config.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        enumSerializer.serialize(enumSet, generator, new SerializerProvider());
        result = generator.getOutputBuffer().toString();
        expected.clear();
        for (MyEnum e : enumSet) {
            expected.add(e.name());
        }
        assertEquals(expected.toString(), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownType() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        Object obj = new Object() {};
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        dynamic.serialize(obj, generator, new SerializerProvider());
    }

    enum MyEnum {
        ONE, TWO, THREE
    }
}