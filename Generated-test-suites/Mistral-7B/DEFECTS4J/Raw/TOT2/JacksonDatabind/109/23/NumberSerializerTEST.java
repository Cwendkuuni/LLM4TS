package com.example;

import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializerConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotNull(numberSerializer);
    }

    @Test
    public void testCreateContextualNullInput() {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = null;
        try {
            NumberSerializer numberSerializer = new NumberSerializer(Number.class).createContextual(provider, property);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected exception
        }
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        numberSerializer.serialize(123, gen, new SerializerProvider());
        String json = gen.getOutputContent();
        assertTrue(json.matches("^\\d+$"));
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(BigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal("123.456");
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        numberSerializer.serialize(bigDecimal, gen, new SerializerProvider());
        String json = sw.toString();
        assertTrue(json.matches("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(BigInteger.class);
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        numberSerializer.serialize(bigInteger, gen, new SerializerProvider());
        String json = sw.toString();
        assertTrue(json.matches("^[-+]?[0-9]+([eE][-+]?[0-9]+)?"));
    }

    @Test
    public void testGetSchemaInteger() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.getSchema(new SerializerProvider(), Number.class);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getDefaultSerializerProvider(), new JsonSerializer.ValueHandler());
        NumberSerializer numberSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertEquals("number", visitor.getCurrentFormat().getShapeName());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigInteger() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getDefaultSerializerProvider(), new JsonSerializer.ValueHandler());
        NumberSerializer numberSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(BigInteger.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertEquals("number", visitor.getCurrentFormat().getShapeName());
    }
}