package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        
        assertNotNull(StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), int.class, true));
        assertNull(StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Enum.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();

        assertNotNull(StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), String.class));
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), Enum.class));
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), null));
    }

    @Test
    public void testGetDefault() {
        ObjectMapper mapper = new ObjectMapper();

        assertNotNull(StdKeySerializers.getDefault());
    }

    @Test
    public void testDefaultSerialization() {
        StdKeySerializers.Default defaultSerializerDate = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        StdKeySerializers.Default defaultSerializerCalendar = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        
        assertNotNull(defaultSerializerDate);
        assertNotNull(defaultSerializerCalendar);
    }

    @Test
    public void testDynamicSerialization() {
        StdKeySerializers.Dynamic dynamicSerializer = new StdKeySerializers.Dynamic();
        ObjectMapper mapper = new ObjectMapper();
        
        assertNotNull(dynamicSerializer);
        assertNotNull(dynamicSerializer._findAndAddDynamic(dynamicSerializer._dynamicSerializers, String.class, mapper.getSerializerProvider()));
    }

    @Test
    public void testStringKeySerializer() {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        
        assertNotNull(stringKeySerializer);
    }

}