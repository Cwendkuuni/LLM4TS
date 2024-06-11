package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@RunWith(JUnit4.class)
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer serializer = NumberSerializer.instance;

        JsonNode expected = JsonNodeFactory.instance.numberNode(123);
        ObjectNode result = mapper.writeValueAsTree(123, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(123.45);
        result = mapper.writeValueAsTree(123.45, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(123L);
        result = mapper.writeValueAsTree(123L, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(123.0F);
        result = mapper.writeValueAsTree(123.0F, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(new BigInteger("123"));
        result = mapper.writeValueAsTree(new BigInteger("123"), serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(new BigDecimal("123.45"));
        result = mapper.writeValueAsTree(new BigDecimal("123.45"), serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(-123);
        result = mapper.writeValueAsTree(-123, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(-123.45);
        result = mapper.writeValueAsTree(-123.45, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(-123L);
        result = mapper.writeValueAsTree(-123L, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(-123.0F);
        result = mapper.writeValueAsTree(-123.0F, serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(new BigInteger("-123"));
        result = mapper.writeValueAsTree(new BigInteger("-123"), serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(new BigDecimal("-123.45"));
        result = mapper.writeValueAsTree(new BigDecimal("-123.45"), serializer);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer serializer = NumberSerializer.instance;

        JsonNode expected = JsonNodeFactory.instance.numberNode(BigInteger.valueOf(Long.MAX_VALUE));
        ObjectNode result = mapper.writeValueAsTree(BigInteger.valueOf(Long.MAX_VALUE), serializer);
        assertEquals(expected, result);

        expected = JsonNodeFactory.instance.numberNode(BigInteger.valueOf(Long.MIN_VALUE));
        result = mapper.writeValueAsTree(BigInteger.valueOf(Long.MIN_VALUE), serializer);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer serializer = NumberSerializer.instance;

        JsonNode expected = JsonNodeFactory.instance.numberNode(new BigDecimal("123456789012345678901234567890.123456789012345678901234567890"));
        ObjectNode result = mapper.writeValueAsTree(new BigDecimal("123456789012345678901234567890.123456789012345678901234567890"), serializer);
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode result = mapper.writeValueAsTree(null, serializer);
        assertEquals(JsonNodeFactory.instance.nullNode(), result);
    }

    @Test
    public void testSerializeInvalid() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer serializer = NumberSerializer.instance;

        assertNull(mapper.writeValueAsTree("invalid", serializer));
    }
}