
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Test BigDecimal
        BigDecimal bigDecimal = new BigDecimal("123.45");
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        try {
            serializer.serialize(bigDecimal, jsonGenerator, provider);
            jsonGenerator.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String expectedOutput = "123.45";

        assertEquals(expectedOutput, writer.toString());

        // Test BigInteger
        BigInteger bigInteger = new BigInteger("1234567890");
        StringWriter writer2 = new StringWriter();
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(writer2);
        try {
            serializer.serialize(bigInteger, jsonGenerator2, provider);
            jsonGenerator2.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String expectedOutput2 = "1234567890";

        assertEquals(expectedOutput2, writer2.toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = Number.class;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = JavaType.fromClass(Number.class);

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }

        // TODO: Add assertions for the visitor behavior
    }

}
