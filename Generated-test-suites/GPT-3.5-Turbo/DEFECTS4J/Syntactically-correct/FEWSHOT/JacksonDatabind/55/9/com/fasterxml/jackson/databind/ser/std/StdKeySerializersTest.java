package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {
    @Test
    public void testGetStdKeySerializer() {
        //Test when rawKeyType is null
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, false));

        //Test when rawKeyType is Object class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Object.class, true));

        //Test when rawKeyType is String class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));

        //Test when rawKeyType is primitive type
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, boolean.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, byte.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, short.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, int.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, long.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, float.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, double.class, true));

        //Test when rawKeyType is Number class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Number.class, true));

        //Test when rawKeyType is Class class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Class.class, true));

        //Test when rawKeyType is Date class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Date.class, true));

        //Test when rawKeyType is Calendar class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Calendar.class, true));

        //Test when rawKeyType is java.util.UUID class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true));
        
        //Test when rawKeyType is Enum class
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Enum.class, true));
        
        //Test when rawKeyType is other type
        assertNull(StdKeySerializers.getStdKeySerializer(null, StringBuilder.class, true));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        //Test when rawKeyType is null
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, null));

        //Test when rawKeyType is Enum class
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));

        //Test when rawKeyType is other type
        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, StringBuilder.class));
    }

    @Test
    public void testSerialize() {
        //Create instances for test
        StdKeySerializers.Default defaultDateSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        StdKeySerializers.Default defaultCalendarSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        StdKeySerializers.Default defaultClassSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        StdKeySerializers.Default defaultEnumSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        StdKeySerializers.Default defaultToStringSerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, String.class);
        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();

        //Create mock objects for test
        JsonGenerator g = null;
        SerializerProvider provider = null;
        
        try {
            //Test serialize method of defaultDateSerializer
            defaultDateSerializer.serialize(new Date(), g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }

        try {
            //Test serialize method of defaultCalendarSerializer
            defaultCalendarSerializer.serialize(Calendar.getInstance(), g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }

        try {
            //Test serialize method of defaultClassSerializer
            defaultClassSerializer.serialize(Object.class, g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }

        try {
            //Test serialize method of defaultEnumSerializer
            defaultEnumSerializer.serialize(Enum.valueOf(TestEnum.class, "VALUE"), g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }

        try {
            //Test serialize method of defaultToStringSerializer
            defaultToStringSerializer.serialize("Test", g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }

        try {
            //Test serialize method of dynamic
            dynamic.serialize("Test", g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }

        try {
            //Test serialize method of stringKeySerializer
            stringKeySerializer.serialize("Test", g, provider);
        } catch (Exception e) {
            fail("Serialize method should not throw an exception");
        }
    }
    
    private enum TestEnum {
        VALUE
    }
}