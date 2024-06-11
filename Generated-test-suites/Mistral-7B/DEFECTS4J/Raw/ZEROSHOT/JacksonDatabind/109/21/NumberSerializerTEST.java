package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test class for NumberSerializer
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeAndDeserializeInt() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Integer.class, null);
        assertTrue(jsonSerializer == jsonGenerator);

        JsonParser jsonParser = mapper.getFactory().getParser("42");
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals(42, number.intValue());

        jsonGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Integer.class, null);
        JsonGenerator testGenerator = mapper.getPolymorphicSerializerProvider().findSerializer(NumberSerializer.class);
        assertSame(jsonGenerator, testGenerator);

        jsonGenerator.writeNumber(42);
        JsonParser testParser = mapper.getFactory().getParser(jsonGenerator.getOutputBuffer().toString());
        number = mapper.readValue(testParser, Number.class);
        assertEquals(42, number.intValue());
    }

    @Test
    public void testSerializeAndDeserializeBigInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(BigInteger.class, null);
        assertTrue(numberSerializer == jsonGenerator);

        BigInteger bigInteger = BigInteger.valueOf(42);

        JsonParser jsonParser = mapper.getFactory().getParser(jsonGenerator.getOutputBuffer().toString());
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals(bigInteger, number);

        jsonGenerator = mapper.getPolymorphicSerializerProvider().findSerializer(NumberSerializer.class);
        JsonGenerator testGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(BigInteger.class, null);
        assertSame(jsonGenerator, testGenerator);

        jsonGenerator.writeNumber(bigInteger);
        JsonParser testParser = mapper.getFactory().getParser(jsonGenerator.getOutputBuffer().toString());
        number = mapper.readValue(testParser, Number.class);
        assertEquals(bigInteger, number);
    }

    @Test
    public void testSerializeAndDeserializeBigDecimal() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(BigDecimal.class, null);
        assertTrue(ToStringSerializer.instance == jsonGenerator);

        BigDecimal bigDecimal = new BigDecimal("42.123");

        JsonParser jsonParser = mapper.getFactory().getParser(jsonGenerator.getOutputBuffer().toString());
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals(bigDecimal, number);

        jsonGenerator = mapper.getPolymorphicSerializerProvider().findSerializer(NumberSerializer.class);
        JsonGenerator testGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(BigDecimal.class, null);
        assertNotSame(jsonGenerator, testGenerator);

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        ObjectCodec objectCodec = mapper.getSerializerProvider().getCodec();
        JsonSerializer<Number> serializer = objectCodec.findSerializer(Number.class, format);
        assertEquals(ToStringSerializer.instance, serializer);

        jsonGenerator = mapper.getPolymorphicSerializerProvider().findSerializer(NumberSerializer.class);
        testGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(BigDecimal.class, format);
        assertSame(jsonGenerator, testGenerator);

        jsonGenerator.writeNumber(bigDecimal.toPlainString());
        JsonParser testParser2 = mapper.getFactory().getParser(jsonGenerator.getOutputBuffer().toString());
        number = mapper.readValue(testParser2, Number.class);
        assertEquals(bigDecimal, number);
    }

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = mapper.getPolymorphicSerializerProvider().findValueSerializer(Number.class, null);
        assertTrue(numberSerializer == jsonGenerator);

        ObjectCodec objectCodec = mapper.getSerializerProvider().getCodec();
        BeanProperty property = new BeanProperty("number", Number.class, null);
        JsonFormat format = objectCodec.findValueAnnotation(property, JsonFormat.class);
        JsonFormat.Value formatValue = format == null ? null : format.getValue();

        JsonSerializer<Number> serializer = numberSerializer.createContextual(mapper.getSerializerProvider(), property);
        assertSame(ToStringSerializer.instance, serializer);

        if (formatValue != null && formatValue.getShape() == JsonFormat.Shape.STRING) {
            serializer = numberSerializer.createContextual(mapper.getSerializerProvider(), property);
            assertEquals(ToStringSerializer.instance, serializer);
        } else {
            serializer = numberSerializer;
            assertEquals(numberSerializer, serializer);
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSerializerProvider().getConfig()
                .findRootValueSerializer(Number.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, null);

        JsonFormat.Value format = visitor.getFormatShape();
        JsonParser.NumberType numberType = format.getNumberType();

        assertTrue(format.getShape() == JsonFormat.Shape.NUMBER || format.getShape() == JsonFormat.Shape.INT);
        assertTrue(numberType == JsonParser.NumberType.BIG_DECIMAL || numberType == JsonParser.NumberType.BIG_INTEGER);
    }
}