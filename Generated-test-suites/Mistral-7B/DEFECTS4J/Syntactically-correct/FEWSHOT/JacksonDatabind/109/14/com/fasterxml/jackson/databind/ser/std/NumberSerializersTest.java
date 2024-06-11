package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Test cases for NumberSerializers class.
 */
public class NumberSerializersTest {

    /**
     * Test for addAll method.
     */
    @Test
    public void testAddAll() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new Object();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
    }

    /**
     * Test for IntegerSerializer.
     */
    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Integer number = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(number, mapper.reader().readValue(parser));

        JsonGenerator gen = mapper.writer().withTypeSerializer(NumberSerializers.IntegerSerializer.instance);
        gen.writeNumber(number);
        JsonParser parsedJson = mapper.getFactory().createParser(gen.getOutputStream().toString());

        assertEquals(number, mapper.reader().readValue(parsedJson));
    }

    /**
     * Test for IntLikeSerializer.
     */
    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Number number = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(number, mapper.reader().readValue(parser));

        JsonGenerator gen = mapper.writer().withTypeSerializer(NumberSerializers.IntLikeSerializer.instance);
        gen.writeNumber(number);
        JsonParser parsedJson = mapper.getFactory().createParser(gen.getOutputStream().toString());

        assertEquals(number, mapper.reader().readValue(parsedJson));
    }

    /**
     * Test for ShortSerializer.
     */
    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Short number = (short) 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(number, mapper.reader().readValue(parser));

        JsonGenerator gen = mapper.writer().withTypeSerializer(NumberSerializers.ShortSerializer.instance);
        gen.writeNumber(number);
        JsonParser parsedJson = mapper.getFactory().createParser(gen.getOutputStream().toString());

        assertEquals(number, mapper.reader().readValue(parsedJson));
    }

    /**
     * Test for LongSerializer.
     */
    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Long number = 123456L;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(number, mapper.reader().readValue(parser));

        JsonGenerator gen = mapper.writer().withTypeSerializer(NumberSerializers.LongSerializer.instance);
        gen.writeNumber(number);
        JsonParser parsedJson = mapper.getFactory().createParser(gen.getOutputStream().toString());

        assertEquals(number, mapper.reader().readValue(parsedJson));
    }

    /**
     * Test for FloatSerializer.
     */
    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Float number = 123.45f;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(number, mapper.reader().readValue(parser));

        JsonGenerator gen = mapper.writer().withTypeSerializer(NumberSerializers.FloatSerializer.instance);
        gen.writeNumber(number);
        JsonParser parsedJson = mapper.getFactory().createParser(gen.getOutputStream().toString());

        assertEquals(number, mapper.reader().readValue(parsedJson));
    }

    /**
     * Test for DoubleSerializer.
     */
    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Double number = 123.456;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        JsonParser parser = mapper.getFactory().createParser(json);

        assertEquals(number, mapper.reader().readValue(parser));

        JsonGenerator gen = mapper.writer().withTypeSerializer(NumberSerializers.DoubleSerializer.instance);
        gen.writeNumber(number);
        JsonParser parsedJson = mapper.getFactory().createParser(gen.getOutputStream().toString());

        assertEquals(number, mapper.reader().readValue(parsedJson));
    }
}