package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private ObjectMapper mapper;
    private JsonSerializer<Object> intSerializer;
    private JsonSerializer<Object> longSerializer;
    private JsonSerializer<Object> shortSerializer;
    private JsonSerializer<Object> floatSerializer;
    private JsonSerializer<Object> doubleSerializer;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                NumberSerializers numberSerializers = new NumberSerializers();
                numberSerializers.addAll(context.constructValueMap());
                context.addSerializerMapping(Map.class, new MinimalJacksonSerializer());
            }
        });

        intSerializer = mapper.findValueSerializer(Integer.class);
        longSerializer = mapper.findValueSerializer(Long.class);
        shortSerializer = mapper.findValueSerializer(Short.class);
        floatSerializer = mapper.findValueSerializer(Float.class);
        doubleSerializer = mapper.findValueSerializer(Double.class);
    }

    @Test
    public void testIntegerSerialization() throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Integer(1);

        when(gen.writeNumber(anyInt())).thenReturn(value);

        intSerializer.serialize(value, gen, mapper.getSerializerProvider());

        verify(gen).writeNumber(1);
    }

    @Test
    public void testLongSerialization() throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Long(1);

        when(gen.writeNumber(anyLong())).thenReturn(value);

        longSerializer.serialize(value, gen, mapper.getSerializerProvider());

        verify(gen).writeNumber(1L);
    }

    @Test
    public void testShortSerialization() throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Short((short) 1);

        when(gen.writeNumber(anyInt())).thenReturn(value);

        shortSerializer.serialize(value, gen, mapper.getSerializerProvider());

        verify(gen).writeNumber(1);
    }

    @Test
    public void testFloatSerialization() throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Float(1.0f);

        when(gen.writeNumber(anyFloat())).thenReturn(value);

        floatSerializer.serialize(value, gen, mapper.getSerializerProvider());

        verify(gen).writeNumber(1.0f);
    }

    @Test
    public void testDoubleSerialization() throws JsonProcessingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Double(1.0);

        when(gen.writeNumber(anyDouble())).thenReturn(value);

        doubleSerializer.serialize(value, gen, mapper.getSerializerProvider());

        verify(gen).writeNumber(1.0);
    }
}