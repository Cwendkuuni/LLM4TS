package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {
    private StdKeySerializers.Default defaultKeySerializer = Mockito.mock(StdKeySerializers.Default.class);
    private StdKeySerializers.Dynamic dynamicKeySerializer = Mockito.mock(StdKeySerializers.Dynamic.class);
    private StdKeySerializers.StringKeySerializer stringKeySerializer = Mockito.mock(StdKeySerializers.StringKeySerializer.class);

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeNull() {
        assertEquals(dynamicKeySerializer, StdKeySerializers.getStdKeySerializer(null, null, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeObject() {
        assertEquals(dynamicKeySerializer, StdKeySerializers.getStdKeySerializer(null, Object.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeString() {
        assertEquals(stringKeySerializer, StdKeySerializers.getStdKeySerializer(null, String.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypePrimitive() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getStdKeySerializer(null, int.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeNumber() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getStdKeySerializer(null, Number.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeClass() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getStdKeySerializer(null, Class.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeDate() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getStdKeySerializer(null, Date.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeCalendar() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getStdKeySerializer(null, Calendar.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeUUID() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true));
    }

    @Test
    public void testGetStdKeySerializer_withRawKeyTypeUUIDUseDefaultFalse() {
        assertNull(StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer_withRawKeyTypeNull() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getFallbackKeySerializer(null, null));
    }

    @Test
    public void testGetFallbackKeySerializer_withRawKeyTypeEnum() {
        assertEquals(dynamicKeySerializer, StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }

    @Test
    public void testGetFallbackKeySerializer_withRawKeyTypeEnumSubclass() {
        assertEquals(defaultKeySerializer, StdKeySerializers.getFallbackKeySerializer(null, MockEnum.class));
    }

    @Test
    public void testDefaultSerializerSerializeWithTypeDate() throws IOException {
        Object value = new Date();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer.serialize(value, generator, provider);
        verify(provider).defaultSerializeDateKey((Date)value, generator);
    }

    @Test
    public void testDefaultSerializerSerializeWithTypeCalendar() throws IOException {
        Calendar value = Calendar.getInstance();
        value.setTime(new Date());
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        defaultKeySerializer.serialize(value, generator, provider);
        verify(provider).defaultSerializeDateKey(value.getTimeInMillis(), generator);
    }

    @Test
    public void testDefaultSerializerSerializeWithTypeClass() throws IOException {
        Class<?> value = MockClass.class;
        JsonGenerator generator = mock(JsonGenerator.class);
        defaultKeySerializer.serialize(value, generator, null);
        verify(generator).writeFieldName(value.getName());
    }

    @Test
    public void testDefaultSerializerSerializeWithTypeEnum_useToString() throws IOException {
        Enum<?> value = MockEnum.TEST;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Mockito.when(provider.isEnabled(Mockito.any())).thenReturn(true);
        defaultKeySerializer.serialize(value, generator, provider);
        verify(generator).writeFieldName(value.toString());
    }

    @Test
    public void testDefaultSerializerSerializeWithTypeEnum_useName() throws IOException {
        Enum<?> value = MockEnum.TEST;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Mockito.when(provider.isEnabled(Mockito.any())).thenReturn(false);
        defaultKeySerializer.serialize(value, generator, provider);
        verify(generator).writeFieldName(value.name());
    }

    @Test
    public void testDefaultSerializerSerializeWithTypeToString() throws IOException {
        Object value = new MockToString();
        JsonGenerator generator = mock(JsonGenerator.class);
        defaultKeySerializer.serialize(value, generator, null);
        verify(generator).writeFieldName(value.toString());
    }

    @Test
    public void testStringKeySerializerSerialize() throws IOException {
        Object value = "test";
        JsonGenerator generator = mock(JsonGenerator.class);
        stringKeySerializer.serialize(value, generator, null);
        verify(generator).writeFieldName((String) value);
    }

    enum MockEnum {
        TEST
    }

    class MockClass {

    }

    class MockToString {
        public String toString() {
            return "test";
        }
    }
}