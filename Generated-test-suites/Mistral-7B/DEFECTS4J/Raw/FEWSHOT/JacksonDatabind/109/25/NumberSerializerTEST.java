package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for {@link NumberSerializer} class
 */
public class NumberSerializerTest {

    @Test
    public void testInstance() {
        assertInstanceOf(NumberSerializer.class, NumberSerializer.instance);
    }

    @Test
    public void testSerializedNumberTypes() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();

        // Test serialization for all number types
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("intValue", 42);
        rootNode.put("longValue", 42L);
        rootNode.put("floatValue", 3.14f);
        rootNode.put("doubleValue", 3.14);
        rootNode.put("byteValue", (byte) 42);
        rootNode.put("shortValue", (short) 42);
        rootNode.put("bigDecimal", new BigDecimal("3.14"));
        rootNode.put("bigInteger", new BigInteger("42"));

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(gen, rootNode);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void testSerializeBigDecimal() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(gen, new BigDecimal("3.14"));

        assertEquals("3.14", ser.serializeToString(new BigDecimal("3.14")));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void testSerializeBigInteger() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        mapper.writeValue(gen, new BigInteger("42"));

        assertEquals("42", ser.serializeToString(new BigInteger("42")));
    }

    @Test
    public void testCreateContextualWhenJsonFormatPresent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Number> ser = NumberSerializer.instance;

        BeanDescription beanDesc = BeanDescription. INT_DESCRIPTOR;
        JsonSerializer<Number> contextualSerializer = ser.createContextual(mapper.getSerializerProvider(), beanDesc);

        assertEquals(ToStringSerializer.instance.getClass(), contextualSerializer.getClass());
    }

    @Test
    public void testCreateContextualWhenJsonFormatAbsent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Number> ser = NumberSerializer.instance;

        BeanDescription beanDesc = BeanDescription. INT_DESCRIPTOR;
        JsonSerializer<Number> contextualSerializer = ser.createContextual(mapper.getSerializerProvider(), beanDesc);

        assertEquals(ser.getClass(), contextualSerializer.getClass());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitNumberFormat(JsonFormat.Feature<?> feature, JavaType typeHint, JsonParser.NumberType numberType) {
                assertEquals(JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_NUMBERS, feature);
                assertEquals(BigDecimal.class, typeHint.getRawClass());
                assertEquals(JsonParser.NumberType.BIG_DECIMAL, numberType);
            }
        };

        ser.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorBigInteger() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitNumberFormat(JsonFormat.Feature<?> feature, JavaType typeHint, JsonParser.NumberType numberType) {
                assertEquals(JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_NUMBERS, feature);
                assertEquals(BigInteger.class, typeHint.getRawClass());
                assertEquals(JsonParser.NumberType.BIG_INTEGER, numberType);
            }
        };

        ser.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorDefault() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            @Override
            public void visitNumberFormat(JsonFormat.Feature<?> feature, JavaType typeHint, JsonParser.NumberType numberType) {
                fail("should not be called");
            }
        };

        ser.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testNull() throws Exception {
        JsonSerializer<Number> ser = NumberSerializer.instance;

        assertNull(ser.serialize(null, null, null));
    }
}