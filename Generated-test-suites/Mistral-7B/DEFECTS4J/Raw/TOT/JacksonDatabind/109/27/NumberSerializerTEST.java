package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private static final ObjectNode createTestObject(ObjectNode node, Number number) throws JsonProcessingException {
        node.put("number", number);
        return node;
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, new BigDecimal("123.456"));

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("123.456", output);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, new BigInteger("12345678901234567890"));

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("12345678901234567890", output);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, 12345L);

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("12345", output);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, new Double(123.45));

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("123.45", output);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, new Float(123.5f));

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("123.5", output);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, 123);

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("123", output);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, (byte) 127);

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("127", output);
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, (short) 32767);

        serializer.serialize(testObject, gen, null);

        String output = gen.getOutputString();
        assertEquals("32767", output);
    }

    @Test
    public void testSerializeUnsupportedNumberType() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode testObject = JsonNodeFactory.instance.objectNode();
        testObject = createTestObject(testObject, new Object());

        try {
            serializer.serialize(testObject, gen, null);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("123.456"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals(new BigDecimal("123.456"), number);
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("12345678901234567890"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals(new BigInteger("12345678901234567890"), number);
    }

    @Test
    public void testDeserializeLong() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("12345"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals(12345L, number.longValue());
    }

    @Test
    public void testDeserializeDouble() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("123.45"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("123.5"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals(123.5f, number.floatValue(), 0.001f);
    }

    @Test
    public void testDeserializeInteger() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("123"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals(123, number.intValue());
    }

    @Test
    public void testDeserializeByte() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("127"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals((byte) 127, number.byteValue());
    }

    @Test
    public void testDeserializeShort() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("32767"));
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);

        Number number = testObject.get("number");
        assertEquals((short) 32767, number.shortValue());
    }

    @Test
    public void testDeserializeUnsupportedNumberType() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("{\"number\": \"abc\"}"));
        ObjectMapper mapper = new ObjectMapper();

        try {
            ObjectNode testObject = mapper.readValue(parser, ObjectNode.class);
            Number number = testObject.get("number");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}