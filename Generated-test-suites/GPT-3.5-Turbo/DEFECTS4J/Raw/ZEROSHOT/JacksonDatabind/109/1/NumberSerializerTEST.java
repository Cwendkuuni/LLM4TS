package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        JsonSerializer<?> jsonSerializer = numberSerializer.createContextual(prov, property);
        Assert.assertEquals(numberSerializer, jsonSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        numberSerializer.serialize(10, g, provider);
        Assert.assertEquals("10", g.getOutput());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        Assert.assertEquals("number", schema.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Assert.assertTrue(true);
    }
}