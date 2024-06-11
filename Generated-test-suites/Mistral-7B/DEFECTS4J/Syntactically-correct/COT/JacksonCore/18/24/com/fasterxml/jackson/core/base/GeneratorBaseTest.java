package com.fasterxml.jackson.core.base; // adjust the package name if necessary

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.json.*;

public class GeneratorBaseTest {

    

    @Test
    public void testIsEnabled() {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));

        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testConfigure() {
        // Configuration methods are abstract, so cannot be tested directly
        // You may want to test those in a subclass that implements them.
    }

    @Test
    public void testEnable() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        generator.enable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator = new GeneratorBase(0, null);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));
        generator.enable(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testDisable() {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));

        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetFeatureMask() {
        // setFeatureMask() is deprecated, use enable()/disable() instead
    }

    @Test
    public void testOverrideStdFeatures() {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.overrideStdFeatures(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask());
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.overrideStdFeatures(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask());
        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(generator.isEnabled(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testUseDefaultPrettyPrinter() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getPrettyPrinter());
        generator.useDefaultPrettyPrinter();
        assertNotNull(generator.getPrettyPrinter());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().createObjectCodec();
        GeneratorBase generator = new GeneratorBase(0, objectCodec);
        assertEquals(objectCodec, generator.getCodec());
    }

    @Test
    public void testGetOutputContext() {
        GeneratorBase generator = new GeneratorBase(0, null);
        assertNull(generator.getOutputContext());
    }

    // Test methods for 'writeStartArray()', 'writeEndArray()', 'writeStartObject()' and 'writeEndObject()' can be added here

    // Test methods for 'writeString()' can be added here

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase generator = new GeneratorBase(0, null);
        generator.writeRawValue("abc");
        assertEquals("abc", generator.getCurrentValue());
        generator.writeRawValue("def");
        assertEquals("abcdef", generator.getCurrentValue());

        generator = new GeneratorBase(GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask(), null);
        generator.writeRawValue("abc");
        assertEquals("abc", generator.getCurrentValue());
        generator.writeRawValue("def");
        assertEquals("abcdef", generator.getCurrentValue());

        generator = new GeneratorBase(GeneratorBase.Feature.STRICT_DUPLICATE_DETECTION.getMask(), null);
        generator.writeRawValue("abc");
        assertSame("abc", generator.getCurrentValue());
        generator.writeRawValue("abc");
        fail("expected DUPLICATE_OUTPUT_STREAM_EVENT");

        generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
        generator.writeRawValue("123");
        assertEquals("123", generator.getCurrentValue());
        generator.writeNumber(123);
        assertEquals("123", generator.getCurrentValue());
    }

    // Test methods for 'writeBinary()' can be added here

    // Test methods for 'writeNumber()' can be added here

    // Test methods for 'writeBoolean()' and 'writeNull()' can be added here

    // Test methods for 'writeObject()' and 'writeTree()' can be added here

    
}