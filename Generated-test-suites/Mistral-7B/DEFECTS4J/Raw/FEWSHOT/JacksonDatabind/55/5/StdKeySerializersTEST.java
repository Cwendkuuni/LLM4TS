package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonProcessingException.fromThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Test;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        
        // test for null rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // test for String rawKeyType
        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // test for primitive types
        serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // test for Date
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);

        // test for Calendar
        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);

        // test for Class
        serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_CLASS, ((StdKeySerializers.Default) serializer)._typeId);

        // test for Enum
        Enum e = Enum.valueOf(Enum.class, "VALUE");
        serializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // test for other JDK types
        serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // test for custom class
        CustomClass cc = new CustomClass();
        serializer = StdKeySerializers.getStdKeySerializer(config, CustomClass.class, false);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();

        // test for null rawKeyType
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        // test for String rawKeyType
        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        // test for primitive types
        serializer = StdKeySerializers.getFallbackKeySerializer(config, int.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        // test for Date
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_DATE, ((StdKeySerializers.Default) serializer)._typeId);

        // test for Calendar
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_CALENDAR, ((StdKeySerializers.Default) serializer)._typeId);

        // test for Class
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Class.class);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_CLASS, ((StdKeySerializers.Default) serializer)._typeId);

        // test for Enum
        Enum e = Enum.valueOf(Enum.class, "VALUE");
        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_ENUM, ((StdKeySerializers.Default) serializer)._typeId);

        // test for other JDK types
        serializer = StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class);
        assertEquals(StdKeySerializers.Default.class, serializer.getClass());
        assertEquals(Default.TYPE_TO_STRING, ((StdKeySerializers.Default) serializer)._typeId);

        // test for custom class
        CustomClass cc = new CustomClass();
        serializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass.class);
        assertEquals(StdKeySerializers.Dynamic.class, serializer.getClass());
    }

    @Test
    public void testDynamicSerialization() throws JsonProcessingException {
        SerializationConfig config = new SerializationConfig();
        PropertySerializerMap map = PropertySerializerMap.emptyForProperties();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass.class);

        CustomClass cc1 = new CustomClass();
        CustomClass cc2 = new CustomClass();

        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        SerializerProvider provider = new SerializerProvider().setSerializerInstance(CustomClass.class, serializer);

        serializer.serialize(cc1, gen, provider);
        String json1 = gen.getOutputBuffer().toString();
        gen.close();

        serializer = StdKeySerializers._findAndAddDynamic(map, CustomClass.class, provider);

        gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        provider = new SerializerProvider().setSerializerInstance(CustomClass.class, serializer);

        serializer.serialize(cc2, gen, provider);
        String json2 = gen.getOutputBuffer().toString();
        gen.close();

        assertEquals(json1, json2);
    }

    static class CustomClass {}
}