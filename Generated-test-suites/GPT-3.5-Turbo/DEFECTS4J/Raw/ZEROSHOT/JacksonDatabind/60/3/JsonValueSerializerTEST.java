package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.junit.Before;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;

import static org.mockito.Mockito.*;

public class JsonValueSerializerTest {

    private JsonGenerator gen;
    private SerializerProvider prov;
    private BeanProperty property;
    private JsonSerializer<Object> ser;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setup() {
        gen = mock(JsonGenerator.class);
        prov = mock(SerializerProvider.class);
        property = mock(BeanProperty.class);
        ser = mock(JsonSerializer.class);
        jsonValueSerializer = new JsonValueSerializer(ser, property);
    }

    @Test
    public void testSerialize_withNullValue_shouldCallDefaultSerializeNull() throws Exception {
        jsonValueSerializer.serialize(null, gen, prov);
        verify(prov).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize_withNonNullValue_shouldCallSerialize() throws Exception {
        Object value = new Object();
        jsonValueSerializer.serialize(value, gen, prov);
        verify(ser).serialize(value, gen, prov);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerialize_withException_shouldThrowJsonMappingException() throws Exception {
        Object value = new Object();
        when(ser.serialize(value, gen, prov)).thenThrow(new Exception());
        jsonValueSerializer.serialize(value, gen, prov);
    }
}