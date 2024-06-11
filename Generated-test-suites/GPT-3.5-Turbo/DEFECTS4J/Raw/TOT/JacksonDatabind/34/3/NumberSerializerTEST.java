
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.456");
        String json = serialize(value);
        Assert.assertEquals("123.456", json);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345678901234567890");
        String json = serialize(value);
        Assert.assertEquals("12345678901234567890", json);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = Integer.valueOf(123);
        String json = serialize(value);
        Assert.assertEquals("123", json);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = Long.valueOf(1234567890L);
        String json = serialize(value);
        Assert.assertEquals("1234567890", json);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = Double.valueOf(123.456);
        String json = serialize(value);
        Assert.assertEquals("123.456", json);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = Float.valueOf(123.456f);
        String json = serialize(value);
        Assert.assertEquals("123.456", json);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = Byte.valueOf((byte) 123);
        String json = serialize(value);
        Assert.assertEquals("123", json);
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = Short.valueOf((short) 123);
        String json = serialize(value);
        Assert.assertEquals("123", json);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeNonNumber() throws IOException {
        ObjectNode value = mapper.createObjectNode();
        serialize(value);
    }

    private String serialize(Object value) throws IOException {
        return mapper.writeValueAsString(value);
    }
}
