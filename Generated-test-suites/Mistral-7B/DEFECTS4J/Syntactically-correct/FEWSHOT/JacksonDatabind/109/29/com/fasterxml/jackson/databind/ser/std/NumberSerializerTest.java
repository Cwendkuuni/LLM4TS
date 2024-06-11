package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonSerialize(using = NumberSerializer.class)
@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public class TestNumber {
    private BigDecimal decimal = new BigDecimal("123.45");
    private BigInteger integer = new BigInteger("12345678901234567890");
    private Long longValue = 123L;
    private Double doubleValue = 123.45;
    private Float floatValue = 123.45F;
    private Integer intValue = 123;
    private Byte byteValue = 123;
    private Short shortValue = 123;

    @Test
    public void testSerialize() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(decimal, generator, new SerializerProvider());
        serializer.serialize(integer, generator, new SerializerProvider());
        serializer.serialize(longValue, generator, new SerializerProvider());
        serializer.serialize(doubleValue, generator, new SerializerProvider());
        serializer.serialize(floatValue, generator, new SerializerProvider());
        serializer.serialize(intValue, generator, new SerializerProvider());
        serializer.serialize(byteValue, generator, new SerializerProvider());
        serializer.serialize(shortValue, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonSerializer<Number> contextualSerializer = serializer.createContextual(new SerializerProvider(), null);
        assertEquals(serializer, contextualSerializer);

        JsonFormat format = new JsonFormat(new StandardSerializer(String.class), null, null);
        contextualSerializer = serializer.createContextual(new SerializerProvider(), new BeanProperty("test", String.class));
        assertNotNull(format);
        assertEquals(JsonFormat.Shape.STRING, format.getShape());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper wrapper = new MapperFactory().getDefaultInstance().getSerializerProvider().getSerializers().findValueSerializer(JsonNodeType.NUMBER);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        serializer.acceptJsonFormatVisitor(wrapper, Object.class);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(decimal, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(integer, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(longValue, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(doubleValue, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(floatValue, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeInt() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(intValue, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeByte() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(byteValue, generator, new SerializerProvider());

        generator.close();
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException, IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        JsonSerializer<Number> serializer = NumberSerializer.instance;

        serializer.serialize(shortValue, generator, new SerializerProvider());

        generator.close();
    }
}