
package com.fasterxml.jackson.databind.ser.std;

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
        
        // Test cases for null or Object class
        assertEquals(StdKeySerializers.Dynamic.class,
                     StdKeySerializers.getStdKeySerializer(config, null, true).getClass());
        assertEquals(StdKeySerializers.Dynamic.class,
                     StdKeySerializers.getStdKeySerializer(config, Object.class, true).getClass());
        
        // Test cases for String
        assertEquals(StdKeySerializers.StringKeySerializer.class,
                     StdKeySerializers.getStdKeySerializer(config, String.class, true).getClass());
        assertEquals(StdKeySerializers.StringKeySerializer.class,
                     StdKeySerializers.getStdKeySerializer(config, String.class, false).getClass());
        
        // Test cases for primitive types
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, int.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, int.class, false).getClass());
        
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, float.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, float.class, false).getClass());
        
        // Test cases for Number and subclasses
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Number.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Number.class, false).getClass());
        
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Integer.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Integer.class, false).getClass());
        
        // Test cases for Class
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Class.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Class.class, false).getClass());
        
        // Test cases for Date
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Date.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Date.class, false).getClass());
        
        // Test cases for Calendar
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Calendar.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, Calendar.class, false).getClass());
        
        // Test cases for UUID
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false).getClass());
        
        // Test case for unknown type without fallback
        assertNull(StdKeySerializers.getStdKeySerializer(config, CustomClass.class, false));
        
        // Test case for unknown type with fallback
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getStdKeySerializer(config, CustomClass.class, true).getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        
        // Test cases for null or Object class
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
                     StdKeySerializers.getFallbackKeySerializer(config, null));
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER,
                     StdKeySerializers.getFallbackKeySerializer(config, Object.class));
        
        // Test case for Enum
        assertEquals(StdKeySerializers.Dynamic.class,
                     StdKeySerializers.getFallbackKeySerializer(config, Enum.class).getClass());
        
        // Test case for non-null unknown type
        assertEquals(StdKeySerializers.Default.class,
                     StdKeySerializers.getFallbackKeySerializer(config, CustomClass.class).getClass());
    }
    
    @Test
    public void testDefaultSerialize() throws IOException {
        // We will only test TYPE_DATE for Default.serialize() method
        StdKeySerializers.Default serializer = new StdKeySerializers.Default(
                StdKeySerializers.Default.TYPE_DATE, Date.class);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(System.out, JsonEncoding.UTF8);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        // Test case for TYPE_DATE
        serializer.serialize(date, generator, provider);
        
        // Test case for TYPE_CALENDAR
        serializer = new StdKeySerializers.Default(
                StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        serializer.serialize(calendar, generator, provider);
        
        generator.close();
    }
    
    @Test
    public void testStringKeySerializer() throws IOException {
        StdKeySerializers.StringKeySerializer serializer = new StdKeySerializers.StringKeySerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(System.out, JsonEncoding.UTF8);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        String key = "test";
        
        serializer.serialize(key, generator, provider);
        
        generator.close();
    }
    
    private static class CustomClass {
        
    }
}
