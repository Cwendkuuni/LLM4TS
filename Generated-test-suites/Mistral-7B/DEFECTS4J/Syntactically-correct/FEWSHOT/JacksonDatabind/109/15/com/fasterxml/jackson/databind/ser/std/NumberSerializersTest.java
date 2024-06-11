package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;
        Short input = Short.parseShort("123");
        String output;

        OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(out);
        shortSerializer.serialize(input, gen, new JsonSerializer.Context());
        gen.close();
        out.close();
        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        String jsonString = new String(bytes, 0, bytes.length);
        JsonParser parser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectMapper mapper = new ObjectMapper();
        Object readValue = mapper.readValue(parser, Short.class);
        assertEquals(input, readValue);
        output = shortSerializer.asString(input);
        assertEquals("123", output);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonSerializer<Integer> integerSerializer = new IntegerSerializer(Integer.class);
        Integer input = 123;
        String output;

        OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(out);
        integerSerializer.serialize(input, gen, new JsonSerializer.Context());
        gen.close();
        out.close();
        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        String jsonString = new String(bytes, 0, bytes.length);
        JsonParser parser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectMapper mapper = new ObjectMapper();
        Object readValue = mapper.readValue(parser, Integer.class);
        assertEquals(input, readValue);
        output = integerSerializer.asString(input);
        assertEquals("123", output);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;
        Number input = 123;
        String output;

        OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(out);
        intLikeSerializer.serialize(input, gen, new JsonSerializer.Context());
        gen.close();
        out.close();
        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        String jsonString = new String(bytes, 0, bytes.length);
        JsonParser parser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectMapper mapper = new ObjectMapper();
        Object readValue = mapper.readValue(parser, Number.class);
        assertEquals(input, readValue);
        output = intLikeSerializer.asString(input);
        assertEquals("123", output);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonSerializer<Long> longSerializer = NumberSerializers.LongSerializer.instance;
        Long input = 123L;
        String output;

        OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(out);
        longSerializer.serialize(input, gen, new JsonSerializer.Context());
        gen.close();
        out.close();
        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        String jsonString = new String(bytes, 0, bytes.length);
        JsonParser parser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectMapper mapper = new ObjectMapper();
        Object readValue = mapper.readValue(parser, Long.class);
        assertEquals(input, readValue);
        output = longSerializer.asString(input);
        assertEquals("123", output);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonSerializer<Float> floatSerializer = NumberSerializers.FloatSerializer.instance;
        Float input = 123.45f;
        String output;

        OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(out);
        floatSerializer.serialize(input, gen, new JsonSerializer.Context());
        gen.close();
        out.close();
        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        String jsonString = new String(bytes, 0, bytes.length);
        JsonParser parser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectMapper mapper = new ObjectMapper();
        Object readValue = mapper.readValue(parser, Float.class);
        assertEquals(input, readValue);
        output = floatSerializer.asString(input);
        assertEquals("123.45", output);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonSerializer<Double> doubleSerializer = NumberSerializers.DoubleSerializer.instance;
        Double input = 123.45;
        String output;

        OutputStreamOutput out = new OutputStreamOutput(new ByteArrayOutputStream());
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(out);
        doubleSerializer.serialize(input, gen, new JsonSerializer.Context());
        gen.close();
        out.close();
        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        String jsonString = new String(bytes, 0, bytes.length);
        JsonParser parser = factory.createParser(new ByteArrayInputStream(bytes));
        ObjectMapper mapper = new ObjectMapper();
        Object readValue = mapper.readValue(parser, Double.class);
        assertEquals(input, readValue);
        output = doubleSerializer.asString(input);
        assertEquals("123.45", output);
    }
}