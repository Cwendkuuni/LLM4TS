package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_NullRawKeyType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);
    }

    @Test
    public void testGetStdKeySerializer_ObjectRawKeyType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_PrimitiveType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_NumberType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, double.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_ClassType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CLASS, Class.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_DateType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_DATE, Date.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_CalendarType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_CALENDAR, Calendar.class), serializer);
    }

    @Test
    public void testGetStdKeySerializer_UUIDType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, java.util.UUID.class, false);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_TO_STRING, java.util.UUID.class), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_EnumClass() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.new Dynamic(), serializer);
    }

    @Test
    public void testGetFallbackKeySerializer_EnumType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, MyEnum.class);
        assertEquals(StdKeySerializers.new Default(Default.TYPE_ENUM, MyEnum.class), serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetFallbackKeySerializer_UnknownType() {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, MyUnknownType.class);
        assertNull(serializer);
    }

    @Test
    public void testGetDefault() {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test(expected = DeprecationException.class)
    public void testGetDefault_Deprecated() {
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
    }

    // Add test cases for Dynamic class here

    static class MyEnum {
        enum MyEnumValue {
            VALUE1, VALUE2;
        }
    }

    static class MyUnknownType {
        // Add a valid class definition here
    }
}