package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        numberSerializer.serialize(new BigDecimal("123.45"), jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct BigDecimal value", "123.45", jsonGenerator.getValue());

        numberSerializer.serialize(new BigInteger("1234"), jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct BigInteger value", "1234", jsonGenerator.getValue());

        numberSerializer.serialize(123, jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct Integer value", "123", jsonGenerator.getValue());

        numberSerializer.serialize(123L, jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct Long value", "123", jsonGenerator.getValue());

        numberSerializer.serialize(123.45d, jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct Double value", "123.45", jsonGenerator.getValue());

        numberSerializer.serialize(123.45f, jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct Float value", "123.45", jsonGenerator.getValue());

        numberSerializer.serialize((byte) 123, jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct Byte value", "123", jsonGenerator.getValue());

        numberSerializer.serialize((short) 123, jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct Short value", "123", jsonGenerator.getValue());

        numberSerializer.serialize("123", jsonGenerator, provider);
        assertEquals("JsonGenerator doesn't write correct String value", "123", jsonGenerator.getValue());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();

        JsonNode schema = numberSerializer.getSchema(provider, null);
        assertTrue("Returned schema is not an instance of JsonNode", schema instanceof JsonNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull("Visitor is not assigned any format", visitor.getFormat());

        numberSerializer = new NumberSerializer(BigInteger.class);
        typeHint = new JavaType();
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull("Visitor is not assigned any format", visitor.getFormat());

        numberSerializer = new NumberSerializer(BigDecimal.class);
        typeHint = new JavaType();
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull("Visitor is not assigned any format", visitor.getFormat());

        numberSerializer = new NumberSerializer(Float.class);
        typeHint = new JavaType();
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull("Visitor is not assigned any format", visitor.getFormat());
    }
}