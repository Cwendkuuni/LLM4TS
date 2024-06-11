package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@JsonFormat(shape = JsonFormat.Shape.STRING)
class TestNumber {
    private Number num;

    public TestNumber(Number num) {
        this.num = num;
    }

    public Number getNum() {
        return num;
    }
}

public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber(BigDecimal.valueOf(12345.6789));
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber(BigInteger.valueOf(1234567890123456789L));
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber(12345L);
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber(12345.6789);
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber(12345);
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber((byte) 123);
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber((short) 123);
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testSerializeUnsupportedType() throws IOException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TestNumber obj = new TestNumber("unsupported");
        serializer.serialize(obj.getNum(), gen, null);
        gen.flush();
    }

    @Test
    public void testCreateContextualSerializerInteger() throws JsonMappingException {
        JsonSerializer<Number> serializer = new NumberSerializer(Integer.class).createContextual(null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testCreateContextualSerializerBigDecimal() throws JsonMappingException {
        JsonSerializer<Number> serializer = new NumberSerializer(BigDecimal.class).createContextual(null, null);
        assertNotNull(serializer);
    }

    @Test
    public void testCreateContextualSerializerUnsupportedType() throws JsonMappingException {
        JsonSerializer<Number> serializer = new NumberSerializer(String.class).createContextual(null, null);
        assertNull(serializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException, IOException {
        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonNode schema = serializer.getSchema(null, Number.class);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getExtensionLoader(JsonFormatVisitorWrapper.class).findValueInstances().get(0);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getExtensionLoader(JsonFormatVisitorWrapper.class).findValueInstances().get(0);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorUnsupportedType() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(String.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getExtensionLoader(JsonFormatVisitorWrapper.class).findValueInstances().get(0);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testSerializeContextualSerializerWithJsonFormatAnnotation() throws IOException {
        TestNumber obj = new TestNumber(12345.6789);

        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonFormat<Number> format = JsonFormat.value().withShape(JsonFormat.Shape.STRING);
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("", "testNumber", TestNumber.class);

        JsonNode result = mapper.writeValueAsTree(obj, provider.findValueSerializer(property.getType(), property, null));
        JsonParser parser = mapper.getFactory().createParser(result.toString());
        Object readValue = mapper.readValue(parser, TestNumber.class);
        assertEquals(obj.getNum(), readValue.getNum());
    }

    @Test
    public void testSerializeContextualSerializerWithJsonDeserializeAnnotation() throws IOException {
        TestNumber obj = new TestNumber(12345.6789);

        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonDeserialize<TestNumber> deserializer = JsonDeserialize.withContentConverter(NumberDeserializers.FloatingPoint.instance);
        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("", "testNumber", TestNumber.class);

        JsonNode result = mapper.writeValueAsTree(obj, provider.findValueSerializer(property.getType(), property, serializer));
        TestNumber readValue = mapper.readValue(result.toString(), TestNumber.class);
        assertEquals(obj.getNum(), readValue.getNum());
    }
}