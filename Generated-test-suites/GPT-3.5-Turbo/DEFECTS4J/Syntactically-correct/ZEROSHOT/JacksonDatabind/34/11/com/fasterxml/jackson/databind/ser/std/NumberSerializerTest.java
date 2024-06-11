package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal value = new BigDecimal("10.5");

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger value = new BigInteger("100");

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Integer value = 10;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Long value = 100L;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Double value = 10.5;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Float value = 10.5f;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Byte value = 10;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Short value = 10;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        Number value = 10.5;

        serializer.serialize(value, generator, provider);

        Mockito.verify(generator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        NumberSerializer serializer = NumberSerializer.instance;
        JsonNode expectedNode = serializer.createSchemaNode("number", true);

        JsonNode actualNode = serializer.getSchema(provider, null);

        Mockito.assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorFloat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorDefault() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).expectNumberFormat(typeHint);
    }
}