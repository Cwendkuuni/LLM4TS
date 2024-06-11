package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import org.junit.Assert;
import org.junit.Test;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    public NumberSerializerTest() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void createContextualShouldReturnNumberSerializer() throws JsonMappingException {
        SerializerProvider prov = null;
        BeanProperty property = null;
        JsonSerializer<?> serializer = numberSerializer.createContextual(prov, property);
        Assert.assertEquals(numberSerializer, serializer);
    }

    @Test
    public void serializeShouldWriteBigDecimal() throws IOException {
        Number value = new BigDecimal(10);
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteBigInteger() throws IOException {
        Number value = new BigInteger("10");
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteLong() throws IOException {
        Number value = 10L;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteDouble() throws IOException {
        Number value = 10.0;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteFloat() throws IOException {
        Number value = 10.0F;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteInteger() throws IOException {
        Number value = 10;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteByte() throws IOException {
        Number value = (byte) 10;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteShort() throws IOException {
        Number value = (short) 10;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void serializeShouldWriteToString() throws IOException {
        Number value = 10;
        JsonGenerator g = null;
        SerializerProvider provider = null;
        numberSerializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void getSchemaShouldCreateSchemaNode() {
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode node = numberSerializer.getSchema(provider, typeHint);
        // Add assertion here
    }

    @Test
    public void acceptJsonFormatVisitorShouldVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add assertion here
    }

    @Test
    public void acceptJsonFormatVisitorShouldVisitFloatFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add assertion here
    }
}