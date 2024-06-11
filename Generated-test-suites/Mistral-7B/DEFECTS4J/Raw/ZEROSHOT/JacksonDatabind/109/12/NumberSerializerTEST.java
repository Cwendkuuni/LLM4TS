package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BigDecimal number = new BigDecimal("123.45");
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class)
                .createContextual(mapper.getSerializerProvider(), null);
        gen.setSerializerProperties(ObjectMapper.SerializedBy.class, NumberSerializer.class);

        mapper.writeValueAsString(number, new StringWriter());

        JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString(number));
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);

        gen = mapper.getSerializerProvider().getSerializerProvider().findValueSerializer(Number.class, null);
        gen.setSerializerProperties(ObjectMapper.SerializedBy.class, NumberSerializer.class);

        StringWriter writer = new StringWriter();
        gen.serialize(number, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
        String serializedNumber = writer.toString();

        parser = mapper.getFactory().createParser(serializedNumber);
        deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BigInteger number = BigInteger.valueOf(12345);
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class)
                .createContextual(mapper.getSerializerProvider(), null);
        gen.setSerializerProperties(ObjectMapper.SerializedBy.class, NumberSerializer.class);

        mapper.writeValueAsString(number, new StringWriter());

        JsonParser parser = mapper.getFactory().createParser(mapper.writeValueAsString(number));
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);

        gen = mapper.getSerializerProvider().getSerializerProvider().findValueSerializer(Number.class, null);
        gen.setSerializerProperties(ObjectMapper.SerializedBy.class, NumberSerializer.class);

        StringWriter writer = new StringWriter();
        gen.serialize(number, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
        String serializedNumber = writer.toString();

        parser = mapper.getFactory().createParser(serializedNumber);
        deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer number = 123;
        JsonGenerator gen = mapper.getSerializerProvider().findValueSerializer(Integer.class, null);

        StringWriter writer = new StringWriter();
        gen.serialize(number, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
        String serializedNumber = writer.toString();

        JsonParser parser = mapper.getFactory().createParser(serializedNumber);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float number = 123.45f;
        JsonGenerator gen = mapper.getSerializerProvider().findValueSerializer(Float.class, null);

        StringWriter writer = new StringWriter();
        gen.serialize(number, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
        String serializedNumber = writer.toString();

        JsonParser parser = mapper.getFactory().createParser(serializedNumber);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double number = 123.45;
        JsonGenerator gen = mapper.getSerializerProvider().findValueSerializer(Double.class, null);

        StringWriter writer = new StringWriter();
        gen.serialize(number, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
        String serializedNumber = writer.toString();

        JsonParser parser = mapper.getFactory().createParser(serializedNumber);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeContextualSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<?> contextualSerializer = mapper.getSerializerProvider()
                .findContextualSerializer(Number.class, mapper.findValueType(JsonParser.NumberType.STRING));

        StringWriter writer = new StringWriter();
        contextualSerializer.serialize(123, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
        String serializedNumber = writer.toString();

        JsonParser parser = mapper.getFactory().createParser(serializedNumber);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(123, deserializedNumber);

        JsonNode schema = mapper.getSerializerProvider().getSerializerProvider().getSerializerProvider()
                .findValueSerializer(JsonNode.class, null).getSchema(mapper.getSerializerProvider(), null);
        JsonNode schemaNumber = schema.path("properties").get("number");
        assertTrue(schemaNumber.isTextual());
        assertEquals("number", schemaNumber.asText());
    }

    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    static class TestClass {
        private BigDecimal number;

        public BigDecimal getNumber() {
            return number;
        }

        public void setNumber(BigDecimal number) {
            this.number = number;
        }
    }

    @Test
    public void testSerializeCustomClass() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TestClass testClass = new TestClass();
        testClass.setNumber(new BigDecimal("123.45"));

        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, testClass);
        String serializedClass = writer.toString();

        JsonParser parser = mapper.getFactory().createParser(serializedClass);
        TestClass deserializedClass = mapper.readValue(parser, TestClass.class);
        assertEquals(testClass.getNumber(), deserializedClass.getNumber());

        JsonNode schema = mapper.getSerializerProvider().getSerializerProvider().getSerializerProvider()
                .findValueSerializer(TestClass.class, null).getSchema(mapper.getSerializerProvider(), null);
        JsonNode schemaNumber = schema.path("properties").path("number").path("properties")
                .path("number");
        assertTrue(schemaNumber.isObject());
        JsonNode schemaNumberType = schemaNumber.path("type");
        assertEquals("number", schemaNumberType.asText());
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeInvalidType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String invalidType = "invalidType";
        JsonGenerator gen = mapper.getSerializerProvider().getSerializerProvider()
                .findValueSerializer(Class.forName(invalidType), null);

        StringWriter writer = new StringWriter();
        gen.serialize(123, mapper.getJsonFactory().getGenerator(writer), mapper.getSerializerProvider());
    }
}