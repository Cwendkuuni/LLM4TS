package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BeanProperty property = new SimpleBeanPropertyDefinition(null, null, null).withType(Object.class);
        JsonSerializer<?> jsonSerializer = numberSerializer.createContextual(provider, property);
        Assert.assertEquals(numberSerializer, jsonSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonSerializer serializer = numberSerializer;
        JsonGenerator generator = new TestJsonGenerator();
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        numberSerializer.serialize(123, generator, provider);
        Assert.assertTrue(generator.hasWriteMethodBeenCalled());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Type typeHint = Number.class;
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        Assert.assertTrue(schema.isValueNode());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonSerializer jsonSerializer = numberSerializer;
        JsonFormatVisitorWrapper visitor = new TestJsonFormatVisitorWrapper();
        JavaType typeHint = TypeFactory.unknownType();
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Assert.assertTrue(visitor.hasExpectNumberFormatMethodBeenCalled());
    }

    private static class TestJsonGenerator extends JsonGenerator {
        private boolean writeMethodCalled = false;

        public boolean hasWriteMethodBeenCalled() {
            return writeMethodCalled;
        }

        // Add all required abstract methods

        @Override
        public void writeNumber(int v) throws IOException {
            writeMethodCalled = true;
        }

        @Override
        public void writeNumber(long v) throws IOException {
            writeMethodCalled = true;
        }

        @Override
        public void writeNumber(double d) throws IOException {
            writeMethodCalled = true;
        }

        @Override
        public void writeNumber(float f) throws IOException {
            writeMethodCalled = true;
        }

        @Override
        public void writeNumber(BigDecimal dec) throws IOException {
            writeMethodCalled = true;
        }

        @Override
        public void writeNumber(String encodedValue) throws IOException {
            writeMethodCalled = true;
        }

        // Implement other abstract methods

    }

    private static class TestJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper {
        private boolean expectNumberFormatMethodCalled = false;

        public boolean hasExpectNumberFormatMethodBeenCalled() {
            return expectNumberFormatMethodCalled;
        }

        // Add all required abstract methods

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            expectNumberFormatMethodCalled = true;
            return super.expectNumberFormat(type);
        }

        // Implement other abstract methods

    }
}