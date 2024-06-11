
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(new BigDecimal("123.456"), generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123.456");
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(new BigInteger("123"), generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123");
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Long.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123L, generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123");
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Double.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123.456, generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123.456");
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Float.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123.456f, generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123.456");
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(123, generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123");
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize((byte) 123, generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123");
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Short.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize((short) 123, generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "123");
    }

    @Test
    public void testSerializeOtherNumbers() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);

        // Act
        serializer.serialize(new Date(), generator, null);
        generator.flush();

        // Assert
        assertEquals(writer.toString(), "some string representation of the value");
    }
}
