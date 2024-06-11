package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Test;

public class StdKeySerializersTest {
    private final StdKeySerializers.Default defaultSerializer = new StdKeySerializers.Default();

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        Class<Object> clazz = Object.class;
        Enum<?> enumValue = mock(Enum.class);
        Object object = new Object();

        defaultSerializer.serialize(date, generator, provider);
        verify(provider).defaultSerializeDateKey(date, generator);

        defaultSerializer.serialize(calendar, generator, provider);
        verify(provider).defaultSerializeDateKey(calendar.getTimeInMillis(), generator);

        defaultSerializer.serialize(clazz, generator, provider);
        verify(generator).writeFieldName(clazz.getName());

        defaultSerializer.serialize(enumValue, generator, provider);
        verify(enumValue).name();

        defaultSerializer.serialize(object, generator, provider);
        verify(generator).writeFieldName(object.toString());

        reset(generator, provider);

        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();

        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        PropertySerializerMap propertySerializerMap = mock(PropertySerializerMap.class);
        PropertySerializerMap.SerializerAndMapResult result = new PropertySerializerMap.SerializerAndMapResult(serializer, propertySerializerMap);

        when(provider.isEnabled(any())).thenReturn(true);
        when(propertySerializerMap.serializerFor(clazz)).thenReturn(null);
        when(propertySerializerMap.findAndAddKeySerializer(eq(clazz), eq(provider), isNull())).thenReturn(result);

        dynamicSerializer.serialize(object, generator, provider);
        verify(generator).writeFieldName(object.toString());
        verify(provider).isEnabled(any());
        verify(propertySerializerMap).serializerFor(clazz);
        verify(propertySerializerMap).findAndAddKeySerializer(eq(clazz), eq(provider), isNull());
    }

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = mock(SerializationConfig.class);
        Class<?> rawKeyType = mock(Class.class);

        assertNull(StdKeySerializers.getStdKeySerializer(config, null, true));
        assertNull(StdKeySerializers.getStdKeySerializer(config, Object.class, true));

        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, String.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, int.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, Number.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, double.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, Class.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, Date.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, Calendar.class, true));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true));
        assertNull(StdKeySerializers.getStdKeySerializer(config, rawKeyType, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = mock(SerializationConfig.class);
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, String.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, int.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, Number.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, double.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, Class.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, Date.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, Calendar.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, java.util.UUID.class));
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, Enum.class));
    }

    @Test
    public void testGetDefault() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getDefault());
    }
}