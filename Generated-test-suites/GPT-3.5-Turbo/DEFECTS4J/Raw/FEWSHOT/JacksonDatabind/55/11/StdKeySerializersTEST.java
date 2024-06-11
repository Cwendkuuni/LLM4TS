package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class StdKeySerializersTest {
    @Test
    public void testGetStdKeySerializer() {
        // Test for rawKeyType being null
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, false));
        
        // Test for rawKeyType being Object.class
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getStdKeySerializer(null, Object.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
        
        // Test for rawKeyType being String.class
        assertEquals(StdKeySerializers.StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(null, String.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, String.class, false));
        
        // Test for rawKeyType being primitive types or subclasses of Number
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, int.class, true).getClass());
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Integer.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, int.class, false));
        assertNull(StdKeySerializers.getStdKeySerializer(null, Integer.class, false));
        
        // Test for rawKeyType being Class.class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Class.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, Class.class, false));
        
        // Test for rawKeyType being subclasses of Date
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Date.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, Date.class, false));
        
        // Test for rawKeyType being subclasses of Calendar
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, Calendar.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, Calendar.class, false));
        
        // Test for rawKeyType being java.util.UUID.class
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true).getClass());
        assertNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false));
        
        // Test for rawKeyType not meeting any of the above conditions
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, true));
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test for rawKeyType being null
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(null, null).getClass());
        
        // Test for rawKeyType being Enum.class
        assertEquals(StdKeySerializers.Dynamic.class, StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());
        
        // Test for rawKeyType being subclass of Enum
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(null, MyEnum.class).getClass());
        
        // Test for rawKeyType not being Enum
        assertEquals(StdKeySerializers.Default.class, StdKeySerializers.getFallbackKeySerializer(null, Object.class).getClass());
    }
    
    public enum MyEnum {
        ONE, TWO, THREE;
    }
}