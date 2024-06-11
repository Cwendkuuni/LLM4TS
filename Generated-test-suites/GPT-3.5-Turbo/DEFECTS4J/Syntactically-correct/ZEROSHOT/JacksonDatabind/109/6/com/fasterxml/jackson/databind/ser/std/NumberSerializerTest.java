package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigInteger() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(new BigInteger("123"), generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123", result);
    }

    @Test
    public void testSerializeBigDecimal() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(new BigDecimal("123.456"), generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123.456", result);
    }

    @Test
    public void testSerializeLong() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Long.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123L, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123", result);
    }

    @Test
    public void testSerializeDouble() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Double.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123.456, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123.456", result);
    }

    @Test
    public void testSerializeFloat() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Float.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123.456f, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123.456", result);
    }

    @Test
    public void testSerializeInteger() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123", result);
    }

    @Test
    public void testSerializeByte() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize((byte) 123, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123", result);
    }

    @Test
    public void testSerializeShort() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Short.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize((short) 123, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123", result);
    }

    @Test
    public void testSerializeDefault() {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123, generator, null);
        generator.flush();
        String result = writer.toString();

        // Assert
        assertEquals("123", result);
    }
}