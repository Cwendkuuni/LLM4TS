package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class StdKeySerializersTest {

    private PropertySerializerMap dynamicSerializers;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializerProvider = mock(SerializerProvider.class);
        dynamicSerializers = PropertySerializerMap.emptyForProperties();
    }

    @Test
    public void testGetStdKeySerializer_Object() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), null, false);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer_String() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), String.class, false);
        assertEquals(DEFAULT_STRING_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer_Number() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), int.class, false);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer_Date() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), Date.class, false);
        assertEquals(new Default(Default.TYPE_DATE, Date.class), result);
    }

    @Test
    public void testGetStdKeySerializer_Calendar() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), Calendar.class, false);
        assertEquals(new Default(Default.TYPE_CALENDAR, Calendar.class), result);
    }

    @Test
    public void testGetStdKeySerializer_JavaUuid() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), java.util.UUID.class, false);
        assertEquals(new Default(Default.TYPE_TO_STRING, java.util.UUID.class), result);
    }

    @Test
    public void testGetStdKeySerializer_UnknownType_UseDefault() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), MyUnknownType.class, true);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer_UnknownType_Null() {
        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), MyUnknownType.class, false);
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer_Enum() {
        JsonSerializer<Object> result = getFallbackKeySerializer(new SerializationConfig(), Enum.class);
        assertEquals(new Default(Default.TYPE_ENUM, Enum.class), result);
    }

    @Test
    public void testGetFallbackKeySerializer_Class() {
        JsonSerializer<Object> result = getFallbackKeySerializer(new SerializationConfig(), MyUnknownType.class);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetFallbackKeySerializer_Null() {
        JsonSerializer<Object> result = getFallbackKeySerializer(new SerializationConfig(), null);
        assertEquals(DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer_Dynamic() {
        when(serializerProvider.findValueSerializer(eq(String.class), any())).thenReturn(new StringSerializer());

        JsonSerializer<Object> result = getStdKeySerializer(new SerializationConfig(), String[].class, false);
        assertEquals(new Dynamic(), result);
    }

    @Test
    public void testDynamicSerializer_FindAndAdd() throws JsonMappingException {
        when(serializerProvider.findValueSerializer(eq(MyDynamicType.class), any())).thenReturn(null);

        JsonSerializer<Object> dynamicSerializer = _findAndAddDynamic(dynamicSerializers, MyDynamicType.class, serializerProvider);
        verify(serializerProvider).findValueSerializer(eq(MyDynamicType.class), any());
        assertEquals(new MyDynamicSerializer(), dynamicSerializer);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        dynamicSerializer.serialize(new MyDynamicObject(), jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName("MyDynamicObject");
    }

    @Test
    public void testDynamicSerializer_AlreadyAdded() throws JsonMappingException {
        when(serializerProvider.findValueSerializer(eq(MyDynamicType.class), any())).thenReturn(new MyDynamicSerializer());

        JsonSerializer<Object> dynamicSerializer = _findAndAddDynamic(dynamicSerializers, MyDynamicType.class, serializerProvider);
        assertEquals(new MyDynamicSerializer(), dynamicSerializer);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        dynamicSerializer.serialize(new MyDynamicObject(), jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName("MyDynamicObject");
    }

    @Test(expected = JsonMappingException.class)
    public void testDynamicSerializer_CircularDependency() throws JsonMappingException {
        when(serializerProvider.findValueSerializer(eq(MyCircularType.class), any())).thenReturn(new MyDynamicSerializer());

        JsonSerializer<Object> dynamicSerializer = _findAndAddDynamic(dynamicSerializers, MyCircularType.class, serializerProvider);
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        JsonSerializer<Object> serializer = new StringKeySerializer();
        serializer.serialize("foo", new JsonGenerator(), serializerProvider);
        assertEquals("foo", serializer.getSerializableValue(new JsonParser().getCodec().treeToValues(new JsonParser(), new Object())));
    }
}

class MyUnknownType {
}

class MyDynamicType {
}

class MyDynamicObject {
}

class MyDynamicSerializer extends StdSerializer<Object> {
    public MyDynamicSerializer() { super(Object.class, false); }

    @Override
    public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
        g.writeFieldName("MyDynamicObject");
    }
}

class MyCircularType {
    static class MyCircularSerializer extends StdSerializer<Object> {
        public MyCircularSerializer() { super(Object.class, false); }

        @Override
        public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
            g.writeFieldName("MyCircularType");
        }
    }
}