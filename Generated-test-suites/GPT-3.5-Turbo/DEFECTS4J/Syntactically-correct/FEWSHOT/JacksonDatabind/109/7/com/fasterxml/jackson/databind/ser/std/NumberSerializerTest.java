package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private BeanProperty beanProperty;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
        beanProperty = null;
        serializerProvider = null;
        objectMapper = new ObjectMapper();
        try {
            jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createContextualTest() throws JsonMappingException {
        JsonSerializer<?> contextualSerializer = numberSerializer.createContextual(serializerProvider, beanProperty);
        assertTrue(contextualSerializer instanceof NumberSerializer);
    }

    @Test
    public void serializeBigDecimalTest() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("123.45", jsonGenerator.toString());
    }

    @Test
    public void serializeBigIntegerTest() throws IOException {
        BigInteger value = new BigInteger("12345");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("12345", jsonGenerator.toString());
    }

    @Test
    public void serializeLongTest() throws IOException {
        Long value = 12345L;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("12345", jsonGenerator.toString());
    }

    @Test
    public void serializeDoubleTest() throws IOException {
        Double value = 12345.6789;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("12345.6789", jsonGenerator.toString());
    }

    @Test
    public void serializeFloatTest() throws IOException {
        Float value = 12345.6789f;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("12345.678", jsonGenerator.toString());
    }

    @Test
    public void serializeIntegerTest() throws IOException {
        Integer value = 12345;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("12345", jsonGenerator.toString());
    }

    @Test
    public void serializeByteTest() throws IOException {
        Byte value = 123;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("123", jsonGenerator.toString());
    }

    @Test
    public void serializeShortTest() throws IOException {
        Short value = 1234;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("1234", jsonGenerator.toString());
    }

    @Test
    public void serializeOtherTypeTest() throws IOException {
        Number value = 12345;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        assertEquals("12345", jsonGenerator.toString());
    }

    @Test
    public void getSchemaTest() {
        JsonNode schema = numberSerializer.getSchema(serializerProvider, null);
        assertTrue(schema.get("type").textValue().equals("number"));
        assertTrue(schema.get("required").booleanValue());
    }

    @Test
    public void acceptJsonFormatVisitorTest() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

}