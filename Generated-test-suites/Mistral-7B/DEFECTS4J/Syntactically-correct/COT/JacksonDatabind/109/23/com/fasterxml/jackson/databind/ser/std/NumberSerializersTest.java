package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

    @InjectMocks
    private NumberSerializers numberSerializers;

    @Test
    public void testAddAll() {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);

        numberSerializers.addAll(allDeserializers);

        assertEquals(6, allDeserializers.size());
        assertEquals(Integer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(Integer.TYPE, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(Long.class, allDeserializers.get(Long.class.getName()));
        assertEquals(Long.TYPE, allDeserializers.get(Long.TYPE.getName()));
        assertEquals(Short.class, allDeserializers.get(Short.class.getName()));
        assertEquals(Short.TYPE, allDeserializers.get(Short.TYPE.getName()));
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        ShortSerializer shortSerializer = new ShortSerializer();
        shortSerializer.serialize(Short.parseShort("123"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(123);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        integerSerializer.serialize(Integer.valueOf(123), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(123);
    }

    @Test
    public void testSerializeIntLike() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        intLikeSerializer.serialize(Byte.valueOf((byte) 123), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(123);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        LongSerializer longSerializer = new LongSerializer(Long.class);
        longSerializer.serialize(Long.parseLong("12345678901234567890"), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(12345678901234567890L);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        FloatSerializer floatSerializer = new FloatSerializer();
        floatSerializer.serialize(Float.valueOf(123.45f), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(123.45f);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        doubleSerializer.serialize(Double.valueOf(123.45), jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(123.45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        ShortSerializer shortSerializer = new ShortSerializer();
        shortSerializer.serialize(null, jsonGenerator, serializerProvider);
    }

    @Test
    public void testSerializeWithType_integer() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        integerSerializer.serializeWithType(Integer.valueOf(1), jsonGenerator, serializerProvider, typeSerializer);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(1);
        Mockito.verifyNoMoreInteractions(typeSerializer);
    }

    @Test
    public void testSerializeWithType_double() throws IOException {
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);

        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        doubleSerializer.serializeWithType(Double.valueOf(1.23), jsonGenerator, serializerProvider, typeSerializer);

        Mockito.verify(jsonGenerator, Mockito.times(1)).writeNumber(1.23);
        Mockito.verifyNoMoreInteractions(typeSerializer);
    }

    @Test
    public void testAcceptJsonFormatVisitor_integer() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        integerSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        Mockito.verify(jsonFormatVisitorWrapper, Mockito.times(1)).visitIntFormat(Mockito.any(), Mockito.eq(typeHint), Mockito.eq(JsonParser.NumberType.INT));
    }

    @Test
    public void testAcceptJsonFormatVisitor_double() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        doubleSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        Mockito.verify(jsonFormatVisitorWrapper, Mockito.times(1)).visitFloatFormat(Mockito.any(), Mockito.eq(typeHint), Mockito.eq(JsonParser.NumberType.DOUBLE));
    }

    @Test
    public void testCreateContextual_integerFormat() throws JsonMappingException {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonFormat format = Mockito.mock(JsonFormat.class);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.INT);

        JsonSerializer<?> jsonSerializer = integerSerializer.createContextual(serializerProvider, beanProperty);

        assertNull(jsonSerializer);
    }

    @Test
    public void testCreateContextual_stringFormat() throws JsonMappingException {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonFormat format = Mockito.mock(JsonFormat.class);
        Mockito.when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);

        assertEquals(ToStringSerializer.instance, integerSerializer.createContextual(serializerProvider, beanProperty));
    }

    @Test
    public void testCreateContextual_noFormat() throws JsonMappingException {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);

        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        JsonSerializer<?> jsonSerializer = integerSerializer.createContextual(serializerProvider, beanProperty);

        assertEquals(integerSerializer, jsonSerializer);
    }
}