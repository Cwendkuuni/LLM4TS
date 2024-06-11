package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        NumberSerializer ns = new NumberSerializer(Number.class);
        ns.serialize(new BigDecimal(10), g, provider);
        // Add assertion here

        ns.serialize(new BigInteger("10"), g, provider);
        // Add assertion here

        ns.serialize(10, g, provider);
        // Add assertion here

        ns.serialize(10L, g, provider);
        // Add assertion here

        ns.serialize(10.0, g, provider);
        // Add assertion here

        ns.serialize(10.0f, g, provider);
        // Add assertion here

        ns.serialize((byte) 10, g, provider);
        // Add assertion here

        ns.serialize((short) 10, g, provider);
        // Add assertion here

        ns.serialize("10", g, provider);
        // Add assertion here
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();

        NumberSerializer ns = new NumberSerializer(Number.class);
        JsonNode schema = ns.getSchema(provider, typeHint);
        // Add assertion here
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        NumberSerializer ns = new NumberSerializer(Number.class);
        ns.acceptJsonFormatVisitor(visitor, typeHint);
        // Add assertion here
    }
}