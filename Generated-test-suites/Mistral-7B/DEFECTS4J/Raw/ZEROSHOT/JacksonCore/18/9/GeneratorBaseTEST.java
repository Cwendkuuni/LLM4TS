package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonVersion;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorBaseTest {

    @Test
    public void testGeneratorBase() throws IOException, JsonProcessingException {
        // Create a test instance with default features
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        JsonGenerationSchema schema = new JsonGenerationSchema();
        GeneratorBase generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | schema.getFeatureMask(), objectCodec);

        // Test version number
        JsonVersion version = generator.version();
        assertEquals("2.12.0", version.toString());

        // Test default feature flags
        assertTrue(generator.isEnabled(Feature.AUTO_CLOSE_SOURCE));
        assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test write null
        generator.writeNull();
        assertEquals(null, generator.getCurrentValue());

        // Test write boolean
        generator.writeBoolean(true);
        assertEquals(true, generator.getCurrentValue());

        // Test write number
        generator.writeNumber(123);
        assertEquals(123, generator.getCurrentValue());

        // Test write string
        generator.writeString("abc");
        assertEquals("abc", generator.getCurrentValue());

        // Test write object
        Object obj = new Object();
        generator.writeObject(obj);
        assertEquals(obj, generator.getCurrentValue());

        // Test write raw value
        generator.writeRawValue("raw data");
        assertEquals("raw data", generator.getCurrentValue());

        // Test write binary (unsupported)
        try {
            generator.writeBinary(null, null, 0);
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test close
        generator.close();
        assertFalse(generator.isOpen());

        // Test write methods with closed generator
        try {
            generator.writeNull();
            fail();
        } catch (IOException e) {
            // expected
        }

        // Test write methods with null value
        generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | schema.getFeatureMask(), objectCodec);
        generator.writeNull();
        assertNull(generator.getCurrentValue());

        // Test write methods with null object
        generator.writeObject(null);
        assertNull(generator.getCurrentValue());

        // Test write methods with BigDecimal value
        BigDecimal decimal = new BigDecimal("1234567890.123456789");
        generator.writeNumber(decimal);
        assertEquals(decimal.toString(), generator.getCurrentValue());

        // Test useDefaultPrettyPrinter()
        generator.useDefaultPrettyPrinter();
        PrettyPrinter prettyPrinter = generator.getPrettyPrinter();
        assertTrue(prettyPrinter instanceof DefaultPrettyPrinter);

        // Test setCodec() and getCodec()
        ObjectCodec newCodec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        generator.setCodec(newCodec);
        assertEquals(newCodec, generator.getCodec());

        // Test getOutputContext()
        JsonWriteContext context = generator.getOutputContext();
        assertNotNull(context);
        assertEquals(generator, context.getGenerator());
    }

    @Test
    public void testGeneratorBaseWithFeatures() throws IOException, JsonProcessingException {
        // Create a test instance with enabled features
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        JsonGenerationSchema schema = new JsonGenerationSchema();
        GeneratorBase generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask() | schema.getFeatureMask(), objectCodec);

        // Test write number as string
        generator.writeNumber(123);
        assertEquals("123", generator.getCurrentValue());

        // Test escape non-ASCII characters
        generator.writeString("test\u00E4test");
        assertEquals("test\"\"u00E4test\"", generator.getCurrentValue());

        // Test strict duplicate detection
        generator.writeStartArray();
        generator.writeString("test");
        generator.writeString("test");
        generator.writeEndArray();
        try {
            generator.writeString("test");
            fail();
        } catch (IOException e) {
            // expected
        }

        // Test write methods with disabled features
        generator = new GeneratorBase(JsonGenerator.Feature.AUTO_CLOSE_SOURCE.getMask() | schema.getFeatureMask(), objectCodec, generator.getOutputContext());
        generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        generator.writeNumber(123);
        assertEquals(123, generator.getCurrentValue());

        generator.disable(Feature.ESCAPE_NON_ASCII);
        generator.writeString("test\u00E4test");
        assertEquals("test\"\"u00E4test\"", generator.getCurrentValue());

        generator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        generator.writeStartArray();
        generator.writeString("test");
        generator.writeString("test");
        generator.writeEndArray();
        assertEquals("[test, test]", generator.getCurrentValue().toString());
    }
}