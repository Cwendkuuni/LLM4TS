package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(INDENT_OUTPUT, true);
    }

    @Test
    public void testBasicDelegatingSerialization() throws JsonProcessingException {
        Converter<Integer, String> converter = new SimpleStringConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(String.class);
        StdDelegatingSerializer<Object> delegatingSerializer = new StdDelegatingSerializer<>(converter, delegateType, null);

        Integer input = 123;
        String expectedOutput = "\"123\"";

        String output = mapper.writeValueAsString(delegatingSerializer.convertValue(input));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDelegatingSerializationWithTypeHandler() throws JsonProcessingException {
        Converter<Integer, BigDecimal> converter = new SimpleBigDecimalConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(BigDecimal.class);
        StdDelegatingSerializer<Object> delegatingSerializer = new StdDelegatingSerializer<>(converter, delegateType, null);

        Integer input = 123;
        BigDecimal expectedOutput = new BigDecimal("123");

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        mapper.writeValue(byteStream, delegatingSerializer.convertValue(input));
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        JsonParser parser = mapper.reader(BigDecimal.class);

        BigDecimal output = parser.readValue(byteInput);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDelegatingSerializationWithContextualization() throws JsonProcessingException {
        Converter<Integer, String> converter = new SimpleStringConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(String.class);
        StdDelegatingSerializer<Integer> delegatingSerializer = new StdDelegatingSerializer<>(converter, delegateType, null);

        Integer input = 123;
        String expectedOutput = "\"123\"";

        String output = mapper.writeValueAsString(delegatingSerializer);
        StdDelegatingSerializer<Object> contextualized = delegatingSerializer.withDelegate(converter, delegateType, null);

        assertEquals(expectedOutput, mapper.writeValueAsString(contextualized.convertValue(input)));
    }

    @Test
    public void testDelegatingSerializationWithTypeHint() throws JsonProcessingException {
        Converter<Integer, BigDecimal> converter = new SimpleBigDecimalConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(BigDecimal.class);
        StdDelegatingSerializer<Object> delegatingSerializer = new StdDelegatingSerializer<>(converter, delegateType, null);

        Integer input = 123;
        BigDecimal expectedOutput = new BigDecimal("123");

        String outputString = mapper.writeValueAsString(delegatingSerializer);
        ByteArrayInputStream byteInput = new ByteArrayInputStream(outputString.getBytes());
        JsonParser parser = mapper.reader(BigDecimal.class);

        BigDecimal output = parser.readValue(byteInput);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testDelegatingSerializationWithCustomDeserialization() throws JsonProcessingException {
        Converter<Integer, String> converter = new SimpleStringConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(String.class);
        StdDelegatingSerializer<Object> delegatingSerializer = new StdDelegatingSerializer<>(converter, delegateType, null);

        String input = "\"123\"";
        Integer expectedOutput = 123;

        ObjectMapper deserializer = new ObjectMapper();
        deserializer.configure(FAIL_ON_EMPTY_BEANS, false);
        deserializer.configure(INDENT_OUTPUT, true);
        StdDelegatingSerializer<Object> contextualized = delegatingSerializer.withDelegate(converter, delegateType, null);

        Integer output = deserializer.readValue(mapper.writeValueAsString(contextualized), Integer.class);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testNullValue() throws JsonProcessingException {
        Converter<Object, String> converter = new SimpleStringConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(String.class);
        StdDelegatingSerializer<Object> delegatingSerializer = new StdDelegatingSerializer<>(converter, delegateType, null);

        Object input = null;

        String expectedOutput = "null";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(INDENT_OUTPUT, true);
        StdDelegatingSerializer<Object> contextualized = delegatingSerializer.withDelegate(converter, delegateType, null);

        String outputString = objectMapper.writeValueAsString(contextualized);

        assertEquals(expectedOutput, outputString);
    }

    @Test
    public void testErrorWithNullDelegateType() {
        Converter<Object, String> converter = new SimpleStringConverter();

        // JavaType is null here
        try {
            new StdDelegatingSerializer<>(converter, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Can't create StdDelegatingSerializer with null JavaType", e.getMessage());
        }
    }

    @Test
    public void testErrorWithNullDelegateSerializer() {
        Converter<Object, String> converter = new SimpleStringConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(String.class);

        // JsonSerializer is null here
        try {
            new StdDelegatingSerializer<>(converter, delegateType, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Can't create StdDelegatingSerializer with null JsonSerializer", e.getMessage());
        }
    }

    @Test
    public void testErrorWithNonIdenticalTypes() {
        Converter<Object, String> converter = new SimpleStringConverter();
        JavaType delegateType = mapper.getTypeFactory().constructType(Integer.class);

        try {
            new StdDelegatingSerializer<>(converter, delegateType, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Can't create StdDelegatingSerializer for non-identical types", e.getMessage());
        }
    }

    @After
    public void cleanup() {
        mapper = null;
    }

    private static class SimpleStringConverter implements Converter<Object, String> {
        @Override
        public String convert(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString();
        }
    }

    private static class SimpleBigDecimalConverter implements Converter<Object, BigDecimal> {
        @Override
        public BigDecimal convert(Object o) {
            if (o == null) {
                return null;
            }
            return new BigDecimal(o.toString());
        }
    }
}