package com.example; // replace with your test package

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.SchemaBuilderFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumberSerializersTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertNotNull(allDeserializers);
        assertEquals(13, allDeserializers.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDeserializer() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        allDeserializers.put(null, null);
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = Short.MIN_VALUE;
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("-1", result.get("numberValue").asText());
        Short deserializedValue = mapper().readValue(result.toString(), Short.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = Integer.MAX_VALUE;
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("2147483647", result.get("numberValue").asText());
        Integer deserializedValue = mapper().readValue(result.toString(), Integer.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = Byte.MIN_VALUE;
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("-128", result.get("numberValue").asText());
        Byte deserializedValue = mapper().readValue(result.toString(), Byte.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = Long.MAX_VALUE;
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("9223372036854775807", result.get("numberValue").asText());
        Long deserializedValue = mapper().readValue(result.toString(), Long.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = Float.MAX_VALUE;
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("3.4028235E38", result.get("numberValue").asText());
        Float deserializedValue = mapper().readValue(result.toString(), Float.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = Double.MAX_VALUE;
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("1.79769313486231E308", result.get("numberValue").asText());
        Double deserializedValue = mapper().readValue(result.toString(), Double.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("9223372036854775807");
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("9223372036854775807", result.get("numberValue").asText());
        BigInteger deserializedValue = mapper().readValue(result.toString(), BigInteger.class);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("1.234567890E12");
        ObjectNode result = mapper().writeValueAsNode(value);
        assertEquals("1.234567890000E12", result.get("numberValue").asText());
        BigDecimal deserializedValue = mapper().readValue(result.toString(), BigDecimal.class);
        assertEquals(value, deserializedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNaN() throws IOException {
        float nanValue = Float.NaN;
        mapper().writeValueAsString(nanValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeInfinity() throws IOException {
        double infinityValue = Double.POSITIVE_INFINITY;
        mapper().writeValueAsString(infinityValue);
    }

    private ObjectMapper mapper() {
        return objectMapper;
    }

}