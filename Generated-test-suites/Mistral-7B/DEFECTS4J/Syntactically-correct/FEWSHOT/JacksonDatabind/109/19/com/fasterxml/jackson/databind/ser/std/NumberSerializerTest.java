package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
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
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    /**
     * Test instance serialization for {@link Number} types.
     */
    @Test
    public void testSerializeNumberTypes() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0, null);

        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectWriter writer = new JsonFactory().createObjectWriter(gen);

        // Test serialization of various number types
        writer.writeValue(gen, 123);
        writer.writeValue(gen, 123L);
        writer.writeValue(gen, 123.0);
        writer.writeValue(gen, 123.0d);
        writer.writeValue(gen, 123.0f);
        writer.writeValue(gen, new BigDecimal("123.45"));
        writer.writeValue(gen, new BigDecimal("123"));
        writer.writeValue(gen, new BigInteger("123"));
        writer.writeValue(gen, new BigInteger("123456789012345678901234567890"));

        gen.close();
    }

    /**
     * Test contextual serialization for {@link JsonFormat} annotations.
     */
    @Test
    public void testContextualSerialization() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(new byte[0]));
        ObjectCodec codec = new ObjectMapper().findValueCodec(parser.getCurrentNameContext().getCurrentScope(), BigDecimal.class);

        // Test serialization with JsonFormat annotation
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0, null);
        JsonSerializer<Number> serializer = codec.findSerializer(BigDecimal.class, null);

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);

        serializer.acceptJsonFormatVisitor(JsonFormatVisitorWrapper.instance, null, jsonFormat);

        ObjectWriter writer = new JsonFactory().createObjectWriter(gen);
        writer.writeValue(gen, new BigDecimal("123.45"));

        gen.close();

        parser.nextToken(); // consume root element
        assertEquals("123.45", parser.getText());

        parser.close();
    }
}