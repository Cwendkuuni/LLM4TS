package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testInstance() {
        assertEquals(NumberSerializer.class, NumberSerializer.instance.getClass());
    }

    @Test
    public void testCreateContextualNull() throws JsonMappingException {
        assertNull(NumberSerializer.instance.createContextual(null, null));
    }

    @Test
    public void testCreateContextualPropertyNull() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        assertNull(NumberSerializer.instance.createContextual(provider, null));
    }

    @Test
    public void testCreateContextualPropertyNumber() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty("number", Number.class);
        assertEquals(NumberSerializer.instance, NumberSerializer.instance.createContextual(provider, property));
    }

    @Test
    public void testCreateContextualPropertyJsonFormat() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty("number", Number.class);
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat annotation = new JsonFormat();
        annotation.setShape(JsonFormat.Shape.STRING);
        property.addAnnotation(annotation);
        assertEquals(ToStringSerializer.instance, NumberSerializer.instance.createContextual(provider, property));
    }

    @Test
    public void testSerializeInt() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.TYPE);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = 123;
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(123, parser.getValueAsInt());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.TYPE);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = 123L;
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
        assertEquals(123, parser.getValueAsLong());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = new BigInteger("123");
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        BigInteger value = parser.getValueAsBigInteger();
        assertEquals(123, value.longValueExact());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = new BigDecimal("123.45");
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("123.45", parser.getText());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.TYPE);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = 123.45f;
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(123.45f, parser.getValueAsFloat(), 0.001f);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.TYPE);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = 123.45;
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(123.45, parser.getValueAsDouble(), 0.001);
    }

    @Test
    public void testSerializeString() throws IOException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        Number number = "123";
        serializer.serialize(number, generator, new DefaultSerializerProvider());
        JsonParser parser = new JsonFactory().getParser(new ByteArrayInputStream(generator.getOutputBytes()));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("123", parser.getText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitIntFormat(JsonFormatVisitorWrapper v, JavaType typeHint, JsonParser.NumberType numberType) {
                assertEquals(JsonParser.NumberType.INT, numberType);
                assertEquals("number", v.getCurrentName());
                assertEquals(Number.class, typeHint);
                assertNull(v.getCurrentValue());
            }
        };
        NumberSerializer serializer = new NumberSerializer(Integer.TYPE);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitFloatFormat(JsonFormatVisitorWrapper v, JavaType typeHint, JsonParser.NumberType numberType) {
                fail("Expected visitNumberFormat with BigDecimal");
            }

            @Override
            public void visitNumberFormat(JsonFormatVisitorWrapper v, JavaType typeHint, JsonParser.NumberType numberType) {
                assertEquals(JsonParser.NumberType.BIG_DECIMAL, numberType);
                assertEquals("number", v.getCurrentName());
                assertEquals(Number.class, typeHint);
                assertNull(v.getCurrentValue());
            }
        };
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorUnsupported() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitIntFormat(JsonFormatVisitorWrapper v, JavaType typeHint, JsonParser.NumberType numberType) {
                fail("Should not call this method");
            }
        };
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testGetSchemaInt() throws JsonProcessingException {
        ObjectCodec codec = new ObjectMapper().finderForGenericType(Number[].class);
        Schema schema = codec.treeToValue(NumberSerializer.instance.getSchema(new DefaultSerializerProvider(), Number[].class.getType()), Number[].class);
        JsonNode jsonNode = schema.getSchemaName();
        assertEquals("number", jsonNode.asText());
        JsonNode properties = schema.getSchemaObject();
        assertNull(properties);
        JsonNode type = schema.getSchemaProperty("type");
        assertEquals("integer", type.asText());
    }

    @Test
    public void testGetSchemaBigDecimal() throws JsonProcessingException {
        ObjectCodec codec = new ObjectMapper().finderForGenericType(Number[].class);
        Schema schema = codec.treeToValue(NumberSerializer.instance.getSchema(new DefaultSerializerProvider(), Number[].class.getType()), Number[].class);
        JsonNode jsonNode = schema.getSchemaName();
        assertEquals("number", jsonNode.asText());
        JsonNode properties = schema.getSchemaObject();
        JsonNode property = properties.get("properties");
        JsonNode type = property.get("number");
        assertEquals("number", type.asText());
        JsonNode subType = type.get("type");
        assertEquals("float", subType.asText());
    }
}