
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test typical use case - rawKeyType is a String
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, String.class, true));

        // Test typical use case - rawKeyType is a primitive type
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, int.class, true));

        // Test typical use case - rawKeyType is a Number type
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Double.class, true));

        // Test typical use case - rawKeyType is Class type
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), StdKeySerializers.getStdKeySerializer(null, Class.class, true));

        // Test typical use case - rawKeyType is Date type
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), StdKeySerializers.getStdKeySerializer(null, Date.class, true));

        // Test typical use case - rawKeyType is Calendar type
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), StdKeySerializers.getStdKeySerializer(null, Calendar.class, true));

        // Test typical use case - rawKeyType is UUID type
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true));

    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test typical use case - rawKeyType is Enum type
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class), StdKeySerializers.getFallbackKeySerializer(null, MyEnum.class));

        // Test typical use case - rawKeyType is null
        assertNull(StdKeySerializers.getFallbackKeySerializer(null, null));
        
    }

    @Test
    public void testSerialization() {
        // Test typical use case - serialization of a String key
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        stringKeySerializer.serialize("test", generator, provider);
        
        verify(generator).writeFieldName(eq("test"));

        // Test typical use case - serialization of a Date key
        StdKeySerializers.Default dateKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);

        Date date = new Date();
        dateKeySerializer.serialize(date, generator, provider);

        verify(provider).defaultSerializeDateKey(eq(date), eq(generator));

        // Test typical use case - serialization of a Calendar key
        StdKeySerializers.Default calendarKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);

        Calendar calendar = Calendar.getInstance();
        calendarKeySerializer.serialize(calendar, generator, provider);

        verify(provider).defaultSerializeDateKey(eq(calendar.getTimeInMillis()), eq(generator));

        // Test typical use case - serialization of a Class key
        StdKeySerializers.Default classKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);

        Class<?> clazz = MyClass.class;
        classKeySerializer.serialize(clazz, generator, provider);

        verify(generator).writeFieldName(eq(clazz.getName()));

        // Test typical use case - serialization of an Enum key
        StdKeySerializers.Default enumKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);

        MyEnum enumValue = MyEnum.VALUE;
        enumKeySerializer.serialize(enumValue, generator, provider);

        verify(generator).writeFieldName(eq(enumValue.toString()));

        // Test typical use case - serialization of a key with toString() method
        StdKeySerializers.Default toStringKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class);

        java.util.UUID uuid = java.util.UUID.randomUUID();
        toStringKeySerializer.serialize(uuid, generator, provider);

        verify(generator).writeFieldName(eq(uuid.toString()));

    }

    @Test
    public void testDynamicDefaultKeySerializer() {
        // Test typical use case - serialization of a dynamic key
        StdKeySerializers.Dynamic dynamicKeySerializer = new StdKeySerializers.Dynamic();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        String dynamicKey = "test";
        dynamicKeySerializer.serialize(dynamicKey, generator, provider);

        verify(generator).writeFieldName(eq(dynamicKey));
    }

    public enum MyEnum {
        VALUE
    }

    public class MyClass {
        // ...
    }

}

