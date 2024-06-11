package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Test
    public void testSerialize() throws IOException {
        numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        
        numberSerializer.serialize(BigDecimal.ONE, generator, null);
        numberSerializer.serialize(BigInteger.ONE, generator, null);
        numberSerializer.serialize(1, generator, null);
        numberSerializer.serialize(1L, generator, null);
        numberSerializer.serialize(1.0, generator, null);
        numberSerializer.serialize(1.0f, generator, null);
        numberSerializer.serialize((byte) 1, generator, null);
        numberSerializer.serialize((short) 1, generator, null);
        numberSerializer.serialize(null, generator, null);
    }

    @Test
    public void testGetSchema() {
        numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null;
        JavaType typeHint = null;
        
        JsonNode jsonNode = numberSerializer.getSchema(provider, typeHint);
        assertNotNull(jsonNode);
        assertTrue(jsonNode.isObject());
        assertTrue(jsonNode.has("type"));
        assertTrue(jsonNode.has("format"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}