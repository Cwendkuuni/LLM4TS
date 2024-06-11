package com.fasterxml.jackson.databind.ser.std;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setup() {
        allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testBaseGetSchema() {
        Base<Integer> base = new IntegerSerializer(Integer.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Integer.class;
        JsonNode schema = base.getSchema(provider, typeHint);
        assertNotNull(schema);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        Base<Integer> base = new IntegerSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        base.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor, Mockito.times(1)).expectIntFormat(typeHint);
    }

    @Test
    public void testBaseCreateContextual() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        Mockito.when(integerSerializer.findFormatOverrides(provider, property, Integer.class)).thenReturn(format);
        ContextualSerializer contextualSerializer = integerSerializer.createContextual(provider, property);
        assertNotNull(contextualSerializer);
        assertEquals(ToStringSerializer.instance, contextualSerializer);
    }

    @Test
    public void testShortSerialize() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        shortSerializer.serialize((short) 10, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10);
    }

    @Test
    public void testIntegerSerialize() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        integerSerializer.serialize(10, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10);
    }

    @Test
    public void testIntLikeSerialize() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        intLikeSerializer.serialize(10, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10);
    }

    @Test
    public void testLongSerialize() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        longSerializer.serialize((long) 100000, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(100000);
    }

    @Test
    public void testFloatSerialize() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        floatSerializer.serialize(10.5f, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10.5f);
    }

    @Test
    public void testDoubleSerialize() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        doubleSerializer.serialize(10.111, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10.111);
    }

}