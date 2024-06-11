
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdKeySerializersTest {
    @Test
    public void testGetStdKeySerializer() {
        // Test cases for rawKeyType as null
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, false));
        
        // Test cases for rawKeyType as Object.class
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getStdKeySerializer(null, Object.class, true).getClass());
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getStdKeySerializer(null, Object.class, false).getClass());
        
        // Test cases for rawKeyType as String.class
        assertEquals(StdKeySerializers.StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(null, String.class, true).getClass());
        assertEquals(StdKeySerializers.StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(null, String.class, false).getClass());
        
        // Test cases for rawKeyType as primitive types or Number class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, byte.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, byte.class, false).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, int.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, int.class, false).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, double.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, double.class, false).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Number.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Number.class, false).getClass());
        
        // Test cases for rawKeyType as Class.class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Class.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Class.class, false).getClass());
        
        // Test cases for rawKeyType as Date.class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Date.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Date.class, false).getClass());
        
        // Test cases for rawKeyType as Calendar.class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Calendar.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Calendar.class, false).getClass());
        
        // Test cases for rawKeyType as java.util.UUID.class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false).getClass());
        
        // Test cases for unknown rawKeyType
        assertNull(StdKeySerializers.getStdKeySerializer(null, UnknownClass.class, true));
        assertNull(StdKeySerializers.getStdKeySerializer(null, UnknownClass.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test case for rawKeyType as null
        assertNull(StdKeySerializers.getFallbackKeySerializer(null, null));
        
        // Test cases for rawKeyType as Enum.class
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());
        
        // Test cases for rawKeyType as Enum
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(null, TestEnum.class).getClass());
        
        // Test case for unknown rawKeyType
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(null, UnknownClass.class).getClass());
    }
    
    // Add more test methods for other methods in StdKeySerializers class if needed

    public enum TestEnum {
        VALUE1, VALUE2
    }
    
    public static final class UnknownClass {
        // Empty class
    }
}

