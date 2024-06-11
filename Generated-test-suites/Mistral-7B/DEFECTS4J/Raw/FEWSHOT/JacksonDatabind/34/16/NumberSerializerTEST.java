package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Test;

@JacksonStdImpl
public class NumberSerializerTest {

    @Test
    public void testSerializeInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        int value = 42;
        serializer.serialize(value, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_NUMBER_INT, par.nextToken());
        assertEquals(Integer.valueOf(42), par.getValueAsNumber());
    }

    @Test
    public void testSerializeLong() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        long value = 42L;
        serializer.serialize(value, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_NUMBER_LONG, par.nextToken());
        assertEquals(Long.valueOf(42), par.getValueAsNumber());
    }

    @Test
    public void testSerializeDouble() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        double value = 42.123;
        serializer.serialize(value, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, par.nextToken());
        assertEquals(new Double(42.123), par.getValueAsNumber());
    }

    @Test
    public void testSerializeFloat() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        float value = 42.123f;
        serializer.serialize(value, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, par.nextToken());
        assertEquals(Float.valueOf(42.123f), par.getValueAsNumber());
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        BigInteger value = new BigInteger("123456789012345678901234567890");
        serializer.serialize(value, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_NUMBER_LONG, par.nextToken());
        assertEquals(value, par.getValueAsNumber().longValueExact());
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        BigDecimal value = new BigDecimal("123.456");
        serializer.serialize(value, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_STRING, par.nextToken());
        TextNode textNode = (TextNode) par.readValueAsTree();
        assertEquals("123.456", textNode.getTextValue());
    }

    @Test
    public void testSerializeUnsupportedNumberType() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());

        Object unsupportedValue = new Object();
        serializer.serialize(unsupportedValue, gen, new SerializerProvider.BaseSerializerProvider());

        JsonParser par = new JsonFactory().createParser(new ByteArrayInputStream(gen.getOutputBytes()));
        assertEquals(JsonToken.VALUE_STRING, par.nextToken());
        TextNode textNode = (TextNode) par.readValueAsTree();
        assertEquals(unsupportedValue.toString(), textNode.getTextValue());
    }

    @Test
    public void testGetSchema() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(JsonMapping.getDefaultSerializerProvider().getSerializerProvider(), Number.class);

        assertEquals("number", schema.getClass().getSimpleName());
        assertEquals(true, schema.isBigDecimalValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getNumberFormatVisitor(mapper.getSerializerProvider(), JsonParser.NumberType.BIG_INTEGER);
        NumberSerializer serializer = NumberSerializer.instance;

        serializer.acceptJsonFormatVisitor(visitor, Number.class.getType());

        assertEquals("number", visitor.getSchemaName());
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getNumberType());
    }
}