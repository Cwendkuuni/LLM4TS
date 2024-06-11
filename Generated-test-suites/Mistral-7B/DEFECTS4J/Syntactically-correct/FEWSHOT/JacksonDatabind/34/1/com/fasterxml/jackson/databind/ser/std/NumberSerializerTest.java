package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(1, generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals(1, parser.readValueAs(Integer.class));
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(1L, generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals(1L, parser.readValueAs(Long.class));
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(1.0, generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1.0"));
        assertEquals(1.0, parser.readValueAs(Double.class), 0.001);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(1.0f, generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1.0"));
        assertEquals(1.0f, parser.readValueAs(Float.class), 0.001f);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize((byte) 1, generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals((byte) 1, parser.readValueAs(Byte.class));
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize((short) 1, generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals((short) 1, parser.readValueAs(Short.class));
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(BigInteger.valueOf(1), generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals(BigInteger.ONE, parser.readValueAs(BigInteger.class));
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(new BigDecimal("1"), generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals(new BigDecimal("1"), parser.readValueAs(BigDecimal.class));
    }

    @Test
    public void testSerializeString() throws IOException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize("1", generator, provider);
        generator.flush();

        JsonParser parser = new JsonFactory().createParser(new StringReader("1"));
        assertEquals(1, parser.readValueAs(Integer.class));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = NumberSerializer.instance.getSchema(new StandardSerializerProvider(), Number.class.getType());
        JsonNodeFactory factory = new JsonNodeFactory();
        JsonNode expected = factory.textNode("number");
        assertEquals(expected.getClass(), schema.getClass());
        assertEquals(expected.asText(), schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFactory factory = new JsonFactory();
        JsonFormatVisitorWrapper visitor = factory.getGeneratorConfig();
        visitor.setNumberIncludeExplicitType(true);
        NumberSerializer serializer = NumberSerializer.instance;

        serializer.acceptJsonFormatVisitor(visitor, Number.class.getType());

        assertEquals("NUMBER", visitor.getFormatName());
        assertTrue(visitor.hasStd(JsonSerializable.Std.NUMBER));
    }
}