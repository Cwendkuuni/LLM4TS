package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
        Short value = Short.MIN_VALUE;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(stringWriter);
        shortSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-128", stringWriter.toString());
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Integer> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer value = Integer.MIN_VALUE;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(stringWriter);
        integerSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-2147483648", stringWriter.toString());
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        Number value = Byte.MIN_VALUE;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(stringWriter);
        intLikeSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-128", stringWriter.toString());
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = NumberSerializers.LongSerializer.instance;
        Long value = Long.MIN_VALUE;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(stringWriter);
        longSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-9223372036854775808", stringWriter.toString());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonSerializer<Float> floatSerializer = NumberSerializers.FloatSerializer.instance;
        Float value = Float.MIN_VALUE;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(stringWriter);
        floatSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-3.40282347e38", stringWriter.toString());
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonSerializer<Double> doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        Double value = Double.MIN_VALUE;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(stringWriter);
        doubleSerializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
        assertEquals("-4.94066646241e-324", stringWriter.toString());
    }
}