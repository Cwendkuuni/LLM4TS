package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    @Test
    public void testGeneratorBase() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectCodec codec = mapper.getFactory().getCodec();
        JsonGenerator generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_DETECT.getMask(), codec);

        // Test version
        assertEquals("2.12.4", generator.version().toString());

        // Test basic feature settings
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test feature enable/disable
        generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        generator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));

        generator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test write methods
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_DETECT.getMask(), codec, new JsonWriteContext(DupDetector.rootDetector(generator)));
        generator.writeStartObject();
        generator.writeFieldName("testField");
        generator.writeNumber(12345);
        generator.writeFieldName("testField2");
        generator.writeString("testString");
        generator.writeEndObject();
        generator.flush();
        String actual = output.toString();
        String expected = "{\"testField\":12345,\"testField2\":\"testString\"}";
        assertEquals(expected, actual);

        // Test write methods with writeContext
        output = new ByteArrayOutputStream();
        generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_DETECT.getMask(), codec);
        generator.setPrettyPrinter(new DefaultPrettyPrinter());
        generator.writeStartArray();
        generator.writeNumber(123);
        generator.writeString("testString");
        generator.writeEndArray();
        generator.flush();
        expected = "[123,\"testString\"]";
        assertEquals(expected, output.toString());

        // Test write methods with features
        generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_DETECT.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), codec);
        output = new ByteArrayOutputStream();
        generator.writeStartObject();
        generator.writeNumber(123.45);
        generator.writeEndObject();
        generator.flush();
        actual = output.toString();
        assertEquals("\"123.45\"", actual);

        // Test write methods with horrible surrogate characters
        generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_DETECT.getMask(), codec);
        output = new ByteArrayOutputStream();
        String surrogate1 = "\uD800";
        String surrogate2 = "\uDBFF";
        String text = surrogate1 + surrogate2 + "\uDC00";
        generator.writeString(text);
        generator.flush();
        actual = output.toString();
        assertEquals(text, actual);

        // Test write methods with BigDecimal
        generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_DETECT.getMask(), codec);
        output = new ByteArrayOutputStream();
        BigDecimal decimal = new BigDecimal("123456789012345678901234567890");
        generator.writeNumber(decimal);
        generator.flush();
        actual = output.toString();
        assertEquals(decimal.toString(), actual);
    }
}