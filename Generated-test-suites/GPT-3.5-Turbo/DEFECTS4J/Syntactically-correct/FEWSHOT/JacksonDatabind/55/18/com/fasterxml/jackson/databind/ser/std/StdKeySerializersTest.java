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
    public void testGetStdKeySerializer_String() {
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, String.class, true));
    }

    @Test
    public void testGetStdKeySerializer_Object() {
        assertEquals(StdKeySerializers.Dynamic(), StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetStdKeySerializer_Primitive() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, int.class, false));
    }

    @Test
    public void testGetStdKeySerializer_Number() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Number.class, false));
    }

    @Test
    public void testGetStdKeySerializer_Class() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), StdKeySerializers.getStdKeySerializer(null, Class.class, false));
    }

    @Test
    public void testGetStdKeySerializer_Date() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), StdKeySerializers.getStdKeySerializer(null, Date.class, false));
    }

    @Test
    public void testGetStdKeySerializer_Calendar() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), StdKeySerializers.getStdKeySerializer(null, Calendar.class, false));
    }

    @Test
    public void testGetStdKeySerializer_UUID() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false));
    }

    @Test
    public void testGetStdKeySerializer_NoMatch() {
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer_NullType() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, null));
    }

    @Test
    public void testGetFallbackKeySerializer_TypeEnum() {
        assertEquals(new StdKeySerializers.Dynamic(), StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }

    @Test
    public void testGetFallbackKeySerializer_TypeEnumValue() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class), StdKeySerializers.getFallbackKeySerializer(null, MyEnum.class));
    }

    @Test
    public void testGetFallbackKeySerializer_NoMatch() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, Object.class));
    }

    @Test
    public void testSerialize_Default_DateType() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        assertEquals("expectedDate", defaultKeySerializer.serialize(new Date(), mockJsonGenerator(), mockSerializerProvider()));
    }

    @Test
    public void testSerialize_Default_CalendarType() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        assertEquals("expectedCalendar", defaultKeySerializer.serialize(Calendar.getInstance(), mockJsonGenerator(), mockSerializerProvider()));
    }

    @Test
    public void testSerialize_Default_ClassType() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        assertEquals("expectedClassName", defaultKeySerializer.serialize(Class.class, mockJsonGenerator(), mockSerializerProvider()));
    }

    @Test
    public void testSerialize_Default_EnumType_With_WriteEnumsUsingToStringEnabled() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        assertEquals("ENUM1", defaultKeySerializer.serialize(MyEnum.ENUM1, mockJsonGenerator(), mockSerializerProviderEnabledWriteEnumsUsingToString()));
    }

    @Test
    public void testSerialize_Default_EnumType_With_WriteEnumsUsingToStringDisabled() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, MyEnum.class);
        assertEquals("ENUM1", defaultKeySerializer.serialize(MyEnum.ENUM1, mockJsonGenerator(), mockSerializerProviderDisabledWriteEnumsUsingToString()));
    }

    @Test
    public void testSerialize_Default_UnknownType() throws IOException {
        StdKeySerializers.Default defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class);
        assertEquals("anyString", defaultKeySerializer.serialize("anyString", mockJsonGenerator(), mockSerializerProvider()));
    }

    private JsonGenerator mockJsonGenerator() throws IOException {
        return null;
    }

    private SerializerProvider mockSerializerProvider() throws IOException {
        return null;
    }

    private SerializerProvider mockSerializerProviderEnabledWriteEnumsUsingToString() throws IOException {
        return null;
    }

    private SerializerProvider mockSerializerProviderDisabledWriteEnumsUsingToString() throws IOException {
        return null;
    }

    private enum MyEnum {
        ENUM1,
        ENUM2
    }
}