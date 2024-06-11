package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@RunWith(MockitoJUnitRunner.class)
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Mock
    private Number value;

    @Mock
    private JsonGenerator g;

    @Mock
    private SerializerProvider provider;

    @Before
    public void setup() {

    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testSerializeOther() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(value, g, provider);
        // Add assertion here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(provider, null);
        assertTrue(schema.isObject());
        // Add assertion here
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add assertion here
    }
}