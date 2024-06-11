package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    /**
     * Static instance of {@link NumberSerializer} to be tested.
     */
    private static final NumberSerializer NUMBER_SERIALIZER = NumberSerializer.instance;

    /**
     * Test for serializing {@link Number} instances.
     *
     * @throws IOException if an error occurs during serialization.
     */
    @Test
    public void testSerializeNumber() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serializing various Number types
        serializeAndCheck(generator, mapper, BigDecimal.ONE, "1.0");
        serializeAndCheck(generator, mapper, BigInteger.ONE, "1");
        serializeAndCheck(generator, mapper, 1, "1");
        serializeAndCheck(generator, mapper, 1.0f, "1.0");
        serializeAndCheck(generator, mapper, 1.0, "1.0");
        serializeAndCheck(generator, mapper, (byte) 1, "1");
        serializeAndCheck(generator, mapper, (short) 1, "1");
        serializeAndCheck(generator, mapper, (long) 1, "1");

        // Test serializing null
        serializeAndCheck(generator, mapper, null, null);

        generator.close();
    }

    /**
     * Test for serializing {@link BigDecimal} and {@link BigInteger} instances using contextual serialization.
     *
     * @throws IOException if an error occurs during serialization.
     */
    @Test
    public void testSerializeCustomNumberTypes() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serializing BigDecimal
        JsonSerializer<BigDecimal> bigDecimalSerializer = mapper.findValueSerializer(BigDecimal.class);
        assertEquals(bigDecimalSerializer, mapper.findContextualSerializer(Number.class, new PropertyName("test")));
        serializeAndCheck(generator, mapper, new BigDecimal("1.0"), "1.0");

        // Test serializing BigInteger
        JsonSerializer<BigInteger> bigIntegerSerializer = mapper.findValueSerializer(BigInteger.class);
        assertEquals(bigIntegerSerializer, mapper.findContextualSerializer(Number.class, new PropertyName("test2")));
        serializeAndCheck(generator, mapper, new BigInteger("1"), "1");

        generator.close();
    }

    /**
     * Test for handling JsonFormat annotations.
     *
     * @throws IOException if an error occurs during serialization.
     */
    @Test
    public void testJsonFormatAnnotations() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
        ObjectMapper mapper = new ObjectMapper();

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        class TestClass {
            private Number number;
        }

        TestClass testClass = new TestClass();
        testClass.number = new BigDecimal("1.0");

        mapper.writeValue(generator, testClass);
        assertEquals("\"1.0\"", generator.getOutputContentAsString());

        generator.close();
    }

    /**
     * Helper method for serializing a Number instance and checking the result.
     *
     * @param generator The JSON generator to use.
     * @param mapper The ObjectMapper to use.
     * @param number The Number instance to serialize.
     * @param expectedOutput The expected output as a JSON string.
     * @throws IOException if an error occurs during serialization.
     */
    private void serializeAndCheck(JsonGenerator generator, ObjectMapper mapper, Number number, String expectedOutput)
            throws IOException {
        mapper.writeValue(generator, number);
        assertEquals(expectedOutput, generator.getOutputContentAsString());
    }

    /**
     * Helper method for serializing a Number instance and checking the result (null output).
     *
     * @param generator The JSON generator to use.
     * @param mapper The ObjectMapper to use.
     * @param number The Number instance to serialize.
     * @throws IOException if an error occurs during serialization.
     */
    private void serializeAndCheck(JsonGenerator generator, ObjectMapper mapper, Number number) throws IOException {
        mapper.writeValue(generator, number);
        assertNull(generator.getOutputContentAsString());
    }
}