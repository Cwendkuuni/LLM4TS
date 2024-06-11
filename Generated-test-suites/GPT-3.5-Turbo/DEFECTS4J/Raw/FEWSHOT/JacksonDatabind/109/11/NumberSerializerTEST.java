
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotNull(numberSerializer);
    }

    @Test
    public void testCreateContextual() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider serializerProvider = null; // instantiate SerializerProvider
        BeanProperty property = null; // instantiate BeanProperty
        assertNull(numberSerializer.createContextual(serializerProvider, property));
    }

    @Test
    public void testSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator g = null; // instantiate JsonGenerator
        SerializerProvider provider = null; // instantiate SerializerProvider
        Number value = new BigDecimal(1);
        try {
            numberSerializer.serialize(value, g, provider);
        } catch (IOException e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null; // instantiate SerializerProvider
        Type typeHint = null; // instantiate Type
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null; // instantiate SerializerProvider
        JavaType typeHint = null; // instantiate JavaType

        JsonFormatVisitorWrapper visitor = null; // instantiate JsonFormatVisitorWrapper
        try {
            numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Exception occurred");
        }
    }
}
