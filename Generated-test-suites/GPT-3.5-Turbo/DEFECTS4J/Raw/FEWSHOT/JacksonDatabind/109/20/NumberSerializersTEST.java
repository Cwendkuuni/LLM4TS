package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class NumberSerializersTest {

    private SerializerProvider provider;
    private JsonGenerator gen;
    private BeanProperty property;

    @Before
    public void setUp() {
        provider = mock(SerializerProvider.class);
        gen = mock(JsonGenerator.class);
        property = mock(BeanProperty.class);
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        Short value = 10;
        
        serializer.serialize(value, gen, provider);
        
        ArgumentCaptor<Short> captor = ArgumentCaptor.forClass(Short.class);
        verify(gen).writeNumber(captor.capture());
        assertEquals(value, captor.getValue());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 100;
        
        serializer.serialize(value, gen, provider);
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(gen).writeNumber(captor.capture());
        assertEquals(value, captor.getValue());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        Number value = 100;
        
        serializer.serialize(value, gen, provider);
        
        ArgumentCaptor<Number> captor = ArgumentCaptor.forClass(Number.class);
        verify(gen).writeNumber(captor.capture());
        assertEquals(value.intValue(), captor.getValue().intValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 1000L;
        
        serializer.serialize(value, gen, provider);
        
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(gen).writeNumber(captor.capture());
        assertEquals(value, captor.getValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        Float value = 10.5f;
        
        serializer.serialize(value, gen, provider);
        
        ArgumentCaptor<Float> captor = ArgumentCaptor.forClass(Float.class);
        verify(gen).writeNumber(captor.capture());
        assertEquals(value, captor.getValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 10.5;
        
        serializer.serialize(value, gen, provider);
        
        ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
        verify(gen).writeNumber(captor.capture());
        assertEquals(value, captor.getValue());
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode result = serializer.getSchema(provider, typeHint);

        assertEquals("number", result.get("type").asText());
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectFloatFormat(typeHint);
    }

    @Test
    public void testBaseContextualWithFormat() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        JsonFormat.Value format = new JsonFormat.Value();
        format.withShape(JsonFormat.Shape.STRING);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonSerializer<?> result = serializer.createContextual(prov, property);

        assertEquals(ToStringSerializer.instance, result);
    }

    @Test
    public void testBaseContextualWithoutFormat() throws JsonMappingException {
        NumberSerializers.Base<?> serializer = new NumberSerializers.ShortSerializer();
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        JsonSerializer<?> result = serializer.createContextual(prov, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(NumberSerializers.ShortSerializer.instance, allDeserializers.get(Short.class.getName()));
        assertEquals(NumberSerializers.ShortSerializer.instance, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(NumberSerializers.IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(NumberSerializers.IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.IntLikeSerializer.instance, allDeserializers.get(Byte.class.getName()));
        assertEquals(NumberSerializers.IntLikeSerializer.instance, allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(NumberSerializers.ShortSerializer.instance, allDeserializers.get(Short.class.getName()));
        assertEquals(NumberSerializers.ShortSerializer.instance, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(NumberSerializers.DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(NumberSerializers.DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.FloatSerializer.instance, allDeserializers.get(Float.class.getName()));
        assertEquals(NumberSerializers.FloatSerializer.instance, allDeserializers.get(Float.TYPE.getName()));
    }
}