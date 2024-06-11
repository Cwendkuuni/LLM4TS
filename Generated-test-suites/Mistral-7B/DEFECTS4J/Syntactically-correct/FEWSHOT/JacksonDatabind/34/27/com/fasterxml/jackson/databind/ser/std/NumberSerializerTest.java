package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.Version.VERSION_2_12;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer.Instance;

@JacksonAnnotationsInside
@JacksonStdImpl
public class NumberSerializerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws IOException {
        JsonGenerator gen = MAPPER.getFactory().createGenerator(System.out, MAPPER.getSerializationConfig());
        NumberSerializer serializer = Instance.instance;

        // Test with different types of Numbers
        serializer.serialize(BigDecimal.ONE, gen, null);
        serializer.serialize(BigInteger.ONE, gen, null);
        serializer.serialize(1, gen, null);
        serializer.serialize(1L, gen, null);
        serializer.serialize(1.0, gen, null);
        serializer.serialize(1.0f, gen, null);
        serializer.serialize((byte) 1, gen, null);
        serializer.serialize((short) 1, gen, null);
        serializer.serialize(-1.0, gen, null);
        serializer.serialize("string number", gen, null);

        gen.close();
    }

    @Test
    public void testDeserializeNumber() throws IOException {
        JsonParser parser = MAPPER.getFactory().createParser(NumberSerializerTest.class.getResourceAsStream("/numbers.json"));
        ObjectReader reader = MAPPER.readerForUpdating(Number.class);

        Number deserialized = reader.readValue(parser);
        assertNotNull(deserialized);

        parser.close();
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        NumberSerializer serializer = Instance.instance;

        JsonFormatVisitorWrapper visitor = MAPPER.schemaFactory().getStandardSerializer(Number.class).acceptJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(visitor, null);

        // Check if the correct schema is generated
        JsonNode schema = visitor.asSchema().getSchema();
        assertEquals(VERSION_2_12, schema.get("$schema").asText());
        assertTrue(schema.has("discriminator"));
        assertTrue(schema.has("properties"));
        assertTrue(schema.get("properties").isObject());
        assertTrue(schema.get("properties").has("number"));
        assertEquals("number", schema.get("properties").get("number").asText());

        visitor.close();
    }

    private static class Instance {
        static final NumberSerializer instance = new NumberSerializer(Number.class);
    }
}