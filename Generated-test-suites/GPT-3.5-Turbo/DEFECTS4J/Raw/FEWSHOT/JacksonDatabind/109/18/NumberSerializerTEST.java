
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        assertTrue(numberSerializer instanceof NumberSerializer);
    }

    @Test
    public void testCreateContextual() {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        SerializerProvider serializerProvider = null;
        BeanProperty beanProperty = null;
        numberSerializer.createContextual(serializerProvider, beanProperty);
        // Add assertion here
    }

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        numberSerializer.serialize(123.45, jsonGenerator, serializerProvider);
        // Add assertion here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        SerializerProvider serializerProvider = null;
        numberSerializer.getSchema(serializerProvider, Double.class);
        // Add assertion here
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null;
        JavaType javaType = null;
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        // Add assertion here
    }
}
