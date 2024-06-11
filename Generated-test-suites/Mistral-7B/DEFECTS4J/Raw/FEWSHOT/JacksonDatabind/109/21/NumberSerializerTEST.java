package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        ObjectWriter objectWriter = new JsonFactory().createObjectMapper().writer();

        // Test serialization of different Number types
        numberSerializer.serialize(1, jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(1.0, jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(1.0f, jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new BigInteger("123456789012345678901234567890"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new BigDecimal("123456789012345678901234567890.123456"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new Long("123456789012345678901234567890"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new Double("123456789012345678901234567890.123456"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new Float("123456789012345678901234567890.123456"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new Integer("1234567890"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new Byte("123"), jsonGenerator, new SerializerProvider());
        numberSerializer.serialize(new Short("1234"), jsonGenerator, new SerializerProvider());

        // Test serialization with JsonGenerator and JsonParser
        JsonParser jsonParser = new JsonFactory().createParser("123456789012345678901234567890.123456");
        JsonToken jsonToken = jsonParser.nextToken();
        JsonGenerator jsonGenerator1 = new JsonFactory().createGenerator(System.out);

        numberSerializer.serialize(jsonParser, jsonGenerator1, new SerializerProvider());
        assertEquals("123456789012345678901234567890.123456", jsonParser.getValueAsString());

        // Test serialization with ObjectWriter
        Number number = new BigDecimal("123456789012345678901234567890.123456");
        JsonNode jsonNode = objectWriter.writeValueAsTree(number);
        assertEquals("123456789012345678901234567890.123456", jsonNode.asText());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Test with default configuration
        JsonSerializer<Number> serializer = objectMapper.findValueSerializer(Number.class);
        assertSame(numberSerializer, serializer);

        // Test with custom JsonFormat
        JsonFormat<Number> jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        module.addSerializerModification(NumberSerializer.class, s -> s.setFormat(jsonFormat));
        ObjectMapper objectMapper1 = objectMapper.copyWith(module);
        JsonSerializer<Number> serializer1 = objectMapper1.findValueSerializer(Number.class);
        assertTrue(ToStringSerializer.class.isAssignableFrom(serializer1.getClass()));

        // Test with custom class
        JsonSerializer<BigDecimal> bigDecimalSerializer = new ToStringSerializer(BigDecimal.class);
        module.addSerializerModification(NumberSerializer.class, s -> s.setBigDecimalSerializer(bigDecimalSerializer));
        ObjectMapper objectMapper2 = objectMapper.copyWith(module);
        JsonSerializer<Number> serializer2 = objectMapper2.findValueSerializer(BigDecimal.class);
        assertEquals(bigDecimalSerializer.getClass(), serializer2.getClass());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Test with default configuration
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.schemaWriter().getFormatVisitor();
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, Number.class);

        // Test with custom JsonFormat
        JsonFormat<Number> jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        module.addSerializerModification(NumberSerializer.class, s -> s.setFormat(jsonFormat));
        ObjectMapper objectMapper1 = objectMapper.copyWith(module);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper1 = objectMapper1.schemaWriter().getFormatVisitor();
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper1, Number.class);
        assertTrue(jsonFormatVisitorWrapper1.expectStringFormat(Number.class).isCalled());

        // Test with custom class
        JsonSerializer<BigDecimal> bigDecimalSerializer = new ToStringSerializer(BigDecimal.class);
        module.addSerializerModification(NumberSerializer.class, s -> s.setBigDecimalSerializer(bigDecimalSerializer));
        ObjectMapper objectMapper2 = objectMapper.copyWith(module);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper2 = objectMapper2.schemaWriter().getFormatVisitor();
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper2, BigDecimal.class);
        assertEquals(bigDecimalSerializer.getClass(), jsonFormatVisitorWrapper2.expectSerializer(JsonSerializer.class, BigDecimal.class).get());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Test with default configuration
        JsonNode jsonNode = numberSerializer.getSchema(new SerializerProvider(), Number.class);
        assertEquals("number", jsonNode.asText());

        // Test with custom JsonFormat
        JsonFormat<Number> jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        module.addSerializerModification(NumberSerializer.class, s -> s.setFormat(jsonFormat));
        ObjectMapper objectMapper1 = objectMapper.copyWith(module);
        JsonNode jsonNode1 = objectSerializer.getSchema(new SerializerProvider(), Number.class);
        assertEquals("string", jsonNode1.asText());

        // Test with custom class
        JsonSerializer<BigDecimal> bigDecimalSerializer = new ToStringSerializer(BigDecimal.class);
        module.addSerializerModification(NumberSerializer.class, s -> s.setBigDecimalSerializer(bigDecimalSerializer));
        ObjectMapper objectMapper2 = objectMapper.copyWith(module);
        JsonNode jsonNode2 = objectSerializer.getSchema(new SerializerProvider(), BigDecimal.class);
        assertEquals("number", jsonNode2.asText());
    }
}